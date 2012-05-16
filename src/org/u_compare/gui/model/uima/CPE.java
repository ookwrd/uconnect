package org.u_compare.gui.model.uima;

import java.util.ArrayList;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.collection.impl.metadata.cpe.CpeDescriptorFactory;
import org.apache.uima.collection.metadata.CpeCasProcessor;
import org.apache.uima.collection.metadata.CpeCasProcessors;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.collection.metadata.CpeComponentDescriptor;
import org.apache.uima.collection.metadata.CpeConfiguration;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.MetaDataObject;
import org.u_compare.gui.control.WorkflowViewerController.WorkflowFactory;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;

public class CPE extends Workflow implements StatusCallbackListener {

	public static WorkflowFactory emptyCPEFactory = new WorkflowFactory() {
		@Override
		public Workflow constructWorkflow() {
			return Workflow
					.constructWorkflowFromXML("src/org/u_compare/gui/model/uima/descriptors/emptyCPE.xml");
		}
	};

	private CpeCollectionReader[] collectionReaders;
	private CpeCasProcessors cpeCasProcessors;
	private CpeConfiguration cpeConfiguration;

	private String sourceFileName = "CPE Workflow";

	private CollectionProcessingEngine mCPE;
	private boolean paused;

	public CPE(CpeDescription desc) throws CpeDescriptorException {

		collectionReaders = desc.getAllCollectionCollectionReaders();
		for (CpeCollectionReader reader : collectionReaders) {

			Component comp;
			if (reader.getDescriptor().getImport() != null) {

				comp = AbstractComponent.constructComponentFromXML(reader
						.getDescriptor().getImport());

			} else if (reader.getDescriptor().getInclude() != null) {

				comp = AbstractComponent.constructComponentFromXML(reader
						.getDescriptor().getInclude());

			} else {
				assert (false);
				System.err.println("Unknown CpeCollectionReader type in "
						+ CPE.class.getName());
				comp = null;
			}
			super.addSubComponent(comp);
		}

		cpeCasProcessors = desc.getCpeCasProcessors(); // <- this is where the
														// subcomponents are
		for (CpeCasProcessor processor : cpeCasProcessors
				.getAllCpeCasProcessors()) {

			Component comp;
			if (processor.getCpeComponentDescriptor().getImport() != null) {

				comp = AbstractComponent.constructComponentFromXML(processor
						.getCpeComponentDescriptor().getImport());

			} else if (processor.getCpeComponentDescriptor().getInclude() != null) {

				comp = AbstractComponent.constructComponentFromXML(processor
						.getCpeComponentDescriptor().getInclude());

			} else {
				assert (false);
				System.err.println("Unknown CpeCollectionReader type in "
						+ CPE.class.getName());
				comp = null;
			}
			super.addSubComponent(comp);
		}

		cpeConfiguration = desc.getCpeConfiguration();

		if (desc.getSourceUrlString() != null) {
			String urlString = desc.getSourceUrlString();
			sourceFileName = urlString.substring(
					urlString.lastIndexOf("/") + 1, urlString.length() - 4);
		}
	}

	@Override
	public void runResumeWorkflow() {
		Runnable runWorkflow = new WorkflowInitializer();
		if (mCPE == null || !paused) {
			Thread newTread = new Thread(runWorkflow);
			newTread.start();
			paused = false;
		} else {
			mCPE.resume();
			resumeWorkflow();
			paused = false;
		}
	}

	@Override
	public void stopWorkflow() {
		mCPE.removeStatusCallbackListener(this);// Doesnt work... UIMA bug?
		mCPE.stop();
		mCPE = null;
		notifyWorkflowMessageListeners("Workflow processing aborted");
		paused = false;
		super.stopWorkflow();
	}

	@Override
	public void pauseWorkflow() {
		mCPE.pause();
		paused = true;
		super.pauseWorkflow();
	}

	private class WorkflowInitializer implements Runnable {
		@Override
		public void run() {
			try {
				CPE.super.runResumeWorkflow();

				CpeDescription cpeDesc = (CpeDescription) getWorkflowDescription();
				notifyWorkflowMessageListeners("Workflow Descriptor initialzed.");

				setStatus(Workflow.WorkflowStatus.LOADING);
				mCPE = UIMAFramework.produceCollectionProcessingEngine(cpeDesc);
				mCPE.addStatusCallbackListener(CPE.this);
				notifyWorkflowMessageListeners("Workflow loaded Successfully.");

				mCPE.process();// Runs on a seperate thread.
			} catch (ResourceInitializationException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public MetaDataObject getWorkflowDescription() {
		CpeDescription retVal = UIMAFramework.getResourceSpecifierFactory()
				.createCpeDescription();

		try {
			retVal.setAllCollectionCollectionReaders(collectionReaders);

			if (getSubComponents().size() != 0 && getSubComponents().get(0) instanceof CollectionReader) {
				CollectionReader comp = (CollectionReader) getSubComponents()
						.get(0);
				retVal.setAllCollectionCollectionReaders(new CpeCollectionReader[] { constructCpeCollectionReader(comp) });
			}

			// Cas processors
			cpeCasProcessors.removeAllCpeCasProcessors();
			ArrayList<String> names = new ArrayList<String>();
			for (int i = 0; i < getSubComponents().size(); i++) {
				Component comp = getSubComponents().get(i);
				if (comp instanceof CollectionReader) {
					assert (i == 0);
					continue;
				}

				// CPE requires unique component names
				if (names.contains(comp.getName())) {
					int suffix = 1;
					while (true) {
						String newName = comp.getName() + suffix;
						if (!names.contains(newName)) {
							notifyWorkflowMessageListeners("Duplicate component names prohibited in CPE descriptors; Component "
									+ comp.getName() + " renamed to " + newName);
							comp.setName(newName);
							break;
						}
						suffix++;
					}
				}
				names.add(comp.getName());
				cpeCasProcessors
						.addCpeCasProcessor(constructCpeCasProcessor(comp));
			}

		} catch (CpeDescriptorException e) {
			e.printStackTrace();
		}

		retVal.setCpeCasProcessors(cpeCasProcessors);
		retVal.setCpeConfiguration(cpeConfiguration);

		return retVal;
	}

	private CpeCasProcessor constructCpeCasProcessor(Component comp)
			throws CpeDescriptorException {
		CpeCasProcessor processor = CpeDescriptorFactory
				.produceCasProcessor(comp.getName());
		String savedLocation = toFile(comp.getResourceCreationSpecifier());
		CpeComponentDescriptor desc = CpeDescriptorFactory
				.produceComponentDescriptor(savedLocation);
		// Why can I only build it from the file system??? I have my specifiers
		// in memory!
		processor.setCpeComponentDescriptor(desc);
		processor.setBatchSize(10000);
		processor.getErrorHandling().getErrorRateThreshold()
				.setMaxErrorCount(0);
		return processor;
	}

	private CpeCollectionReader constructCpeCollectionReader(
			CollectionReader comp) throws CpeDescriptorException {
		String savedLocation = toFile(comp.getResourceCreationSpecifier());
		CpeCollectionReader reader = CpeDescriptorFactory
				.produceCollectionReader(savedLocation);
		return reader;
	}

	/**
	 * CPE Workflows don't have a name field so use the description field
	 * instead.
	 */
	@Override
	public String getName() {
		return sourceFileName;
	}

	@Override
	public String getDescription() {
		return "This is a CPE workflow and so certain functionality may be unavailable. It is highly reccomended that users make use of UIMA AS workflows when possible.";
	}

	@Override
	public void aborted() {
	}

	/**
	 * TODO paused and resumed are never called. I checked the UIMA CPM panel
	 * and RunAE and these don't seem to be called either. I think this may be a
	 * bug in UIMA.
	 */
	@Override
	public void paused() {
		setStatus(WorkflowStatus.PAUSED);
	}

	@Override
	public void resumed() {
		setStatus(WorkflowStatus.RUNNING);
	}

	@Override
	public void batchProcessComplete() {
	}

	@Override
	public void collectionProcessComplete() {
		setStatus(WorkflowStatus.FINISHED);
		mCPE = null;
	}

	@Override
	public void initializationComplete() {
		setStatus(WorkflowStatus.RUNNING);
	}

	@Override
	public void entityProcessComplete(CAS arg0, EntityProcessStatus arg1) {
		// doesn't report if mCPE is set to null as removeStatusCallbackListener
		// doesn't work
		if (mCPE != null) {
			notifyWorkflowMessageListeners("Entity processing complete with status: "
					+ arg1.getStatusMessage());
		}
	}

	/**
	 * Overridden to ensure the first component is always a collection reader as
	 * per the requirements of a CPE workflow.
	 */
	@Override
	public boolean canAddSubComponent(Component component, int position) {
		// Can't add a collection reader except into first place
		if (component instanceof CollectionReader && position > 0) {
			return false;
		}

		// Can't add something in front of a collection reader
		if (position == 0) {
			ArrayList<Component> subComponents = getSubComponents();
			if (subComponents.size() > 0
					&& subComponents.get(0) instanceof CollectionReader) {
				return false;
			}
		}
		return super.canAddSubComponent(component, position);
	}

	/**
	 * Overridden to ensure the first component is always a collection reader as
	 * per the requirements of a CPE workflow.
	 */
	@Override
	public boolean canReorderSubComponent(Component component, int position) {
		// Can't move a collection reader except into first place
		if (component instanceof CollectionReader && position > 0) {
			return false;
		}

		// Can't move something in front of a collection reader
		if (position == 0) {
			ArrayList<Component> subComponents = getSubComponents();
			if (!(component instanceof CollectionReader)
					&& subComponents.get(0) instanceof CollectionReader) {
				return false;
			}
		}
		return super.canReorderSubComponent(component, position);
	}
}
