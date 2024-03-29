package org.u_compare.gui.model;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.TypeOrFeature;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeInclude;
import org.apache.uima.resource.ResourceCreationSpecifier;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.URISpecifier;
import org.apache.uima.resource.metadata.Capability;
import org.apache.uima.resource.metadata.ConfigurationGroup;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.FsIndexCollection;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.OperationalProperties;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.apache.uima.resource.metadata.ResourceManagerConfiguration;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.u_compare.gui.model.parameters.AbstractParameter;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.FloatParameter;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterGroup;
import org.u_compare.gui.model.parameters.StringParameter;
import org.u_compare.gui.model.uima.AggregateAnalysisEngine;
import org.u_compare.gui.model.uima.CasConsumer;
import org.u_compare.gui.model.uima.CollectionReader;
import org.u_compare.gui.model.uima.PrimitiveAnalysisEngine;
import org.u_compare.gui.model.uima.SOAPComponent;

/**
 * Abstract base class implementing much of the functionality common to all
 * components.
 * 
 * See also AbstractUIMAAggregateComponent.
 * 
 * @author Luke McCrohon
 */
public abstract class AbstractComponent implements Component {

	/**
	 * Possible values of the components internal locked status UNLOCKED
	 * DIRECTLOCK a lock was directly placed on this component INDIRECTLOCK
	 * component is locked due to a lock being placed on a parent component
	 */
	public static enum LockStatusEnum {
		UNLOCKED, DIRECTLOCK, INDIRECTLOCK
	}

	/**
	 * Possible values of the components minimized status MINIMIZED - Only the
	 * title bar showing PARTIAL - Title, Description and Signature MAXIMIZED -
	 * All parameters and settings displayed.
	 */
	public static enum MinimizedStatusEnum {
		MINIMIZED, PARTIAL, MAXIMIZED
	}

	private Component parentComponent;

	// private (rather than protected) to ensure use of proper set methods by
	// extending classes
	private String name = "Unnamed";
	private String identifier = null;
	private String implementationName = "Unknown";
	private String description = "Undescribed";
	private String vendor = "Unknown";
	private String version = "Unspecified";
	private String copyright = "Copyright information unknown";
	private ArrayList<String> languages = new ArrayList<String>();
	private ArrayList<AnnotationTypeOrFeature> inputTypes = new ArrayList<AnnotationTypeOrFeature>();
	private ArrayList<AnnotationTypeOrFeature> outputTypes = new ArrayList<AnnotationTypeOrFeature>();
	private ParameterGroup basicParameters = new ParameterGroup(this);
	private ParameterGroup commonParameters = new ParameterGroup(this);
	private ArrayList<ParameterGroup> parameterGroups = new ArrayList<ParameterGroup>();
	private String parameterSearchStratergy = null;
	private String parameterDefaultGroup = null;
	private boolean unsavedChanges = false;
	private MinimizedStatusEnum minimized = MinimizedStatusEnum.MAXIMIZED;
	protected LockStatusEnum lockStatus = LockStatusEnum.UNLOCKED;

	// Base UIMA Metadata Objects
	protected TypeSystemDescription typeSystemDescription;
	protected TypePriorities typePriorities;
	protected FsIndexCollection fsIndexCollection;
	protected Capability[] capabilities;
	protected OperationalProperties operationalProperties;
	protected ResourceManagerConfiguration resourceManagerConfiguration;

	// Change listeners
	private ArrayList<DescriptionChangeListener> componentDescriptionChangeListeners = new ArrayList<DescriptionChangeListener>();
	private ArrayList<DistributionInformationChangeListener> distributionInformationChangeListeners = new ArrayList<DistributionInformationChangeListener>();
	private ArrayList<LanguageChangeListener> languageChangeListeners = new ArrayList<Component.LanguageChangeListener>();
	private ArrayList<InputOutputChangeListener> inputOutputChangeListeners = new ArrayList<InputOutputChangeListener>();
	private ArrayList<SavedStatusChangeListener> savedStatusChangeListeners = new ArrayList<SavedStatusChangeListener>();
	private ArrayList<MinimizedStatusChangeListener> minimizedStatusChangeListeners = new ArrayList<MinimizedStatusChangeListener>();
	private ArrayList<LockedStatusChangeListener> lockedStatusChangeListeners = new ArrayList<LockedStatusChangeListener>();
	private ArrayList<LockedStatusChangeListener> parentLockedStatusChangeListeners = new ArrayList<LockedStatusChangeListener>();
	private ArrayList<ParameterConfigurationChangeListener> parameterConfigurationChangeListeners = new ArrayList<Component.ParameterConfigurationChangeListener>();
	private ArrayList<ParameterGroupsChangeListener> parameterGroupsChangeListeners = new ArrayList<Component.ParameterGroupsChangeListener>();
	private ArrayList<ParametersChangedListener> parametersChangedListeners = new ArrayList<Component.ParametersChangedListener>();
	private ArrayList<FlowControlChangeListener> flowControlChangeListeners = new ArrayList<Component.FlowControlChangeListener>();

	public AbstractComponent() {

	}

	/**
	 * Components directly extending this class are generally not aggregate.
	 * Aggregates will usually indirectly extend this class via
	 * UIMAAggregateComponent.
	 */
	@Override
	public boolean isAggregate() {
		// False by default
		return false;
	}

	@Override
	public ArrayList<Component> getSubComponents() {
		// No subcomponents by default
		return new ArrayList<Component>();
	}

	/**
	 * Return false as by default implementing components won't be workflows.
	 * Override as necessary.
	 */
	@Override
	public boolean isWorkflow() {
		// False by default
		return false;
	}

	/**
	 * Returns the Name of the component.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the Name of the component.
	 */
	@Override
	public void setName(String title) {

		if (this.name != null && this.name.equals(title)) {
			return;
		}

		this.name = title;
		notifyComponentDescriptionChangeListeners();
	}

	/**
	 * Returns the Description of the Component.
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description of the Component.
	 */
	@Override
	public void setDescription(String description) {

		if (this.description != null && this.description.equals(description)) {
			return;
		}

		this.description = description;
		notifyComponentDescriptionChangeListeners();
	}

	@Override
	public String getImplementationName() {
		return implementationName;
	}

	@Override
	public void setImplementationName(String implementationName) {

		if (this.implementationName != null
				&& this.implementationName.equals(implementationName)) {
			return;
		}

		this.implementationName = implementationName;
		notifyDistributionInformationChangedListeners();
	}

	@Override
	public String getVendor() {
		return vendor;
	}

	@Override
	public void setVendor(String vendor) {
		if (this.vendor != null && this.vendor.equals(vendor)) {
			return;
		}

		this.vendor = vendor;
		notifyDistributionInformationChangedListeners();
	}

	@Override
	public String getCopyright() {
		return copyright;
	}

	@Override
	public void setCopyright(String copyright) {
		if (this.copyright != null && this.copyright.equals(copyright)) {
			return;
		}
		this.copyright = copyright;
		notifyDistributionInformationChangedListeners();
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public void setVersion(String version) {
		if (this.version != null && this.version.equals(version)) {
			return;
		}
		this.version = version;
		notifyDistributionInformationChangedListeners();
	}

	@Override
	public Component getSuperComponent() {
		return parentComponent;
	}

	@Override
	public void setSuperComponent(Component superComp) {
		this.parentComponent = superComp;
	}

	@Override
	public ArrayList<String> getLanguageTypes() {
		return languages;
	}

	@Override
	public void addLanguageType(String language) {

		if (languages.contains(language)) {
			return;
		}

		languages.add(language);
		notifyLanguagesChangeListener();
	}

	@Override
	public void removeLanguageType(String language) {

		if (!languages.contains(language)) {
			return;
		}

		languages.remove(language);
		notifyLanguagesChangeListener();
	}

	@Override
	public void setLanguageTypes(ArrayList<String> languagesIn) {

		if (languages.containsAll(languagesIn)
				&& languagesIn.size() == languages.size()) {
			return;
		}

		languages = languagesIn;
		notifyLanguagesChangeListener();
	}

	@Override
	public ArrayList<AnnotationTypeOrFeature> getInputTypes() {
		return inputTypes;
	}

	@Override
	public void addInputType(AnnotationTypeOrFeature inputType) {

		if (inputTypes.contains(inputType)) {
			return;
		}

		inputTypes.add(inputType);
		notifyInputOutputChangeListeners();

	}

	@Override
	public void removeInputType(AnnotationTypeOrFeature inputType) {

		if (!inputTypes.contains(inputType)) {
			return;
		}

		inputTypes.remove(inputType);
		notifyInputOutputChangeListeners();
	}

	@Override
	public void setInputTypes(ArrayList<AnnotationTypeOrFeature> newInputTypes) {

		if (inputTypes.containsAll(newInputTypes)
				&& inputTypes.size() == newInputTypes.size()) {
			return;
		}

		inputTypes = new ArrayList<AnnotationTypeOrFeature>();
		inputTypes.addAll(newInputTypes);
		notifyInputOutputChangeListeners();

	}

	@Override
	public ArrayList<AnnotationTypeOrFeature> getOutputTypes() {
		return outputTypes;
	}

	@Override
	public void addOutputType(AnnotationTypeOrFeature outputType) {

		if (outputTypes.contains(outputType)) {
			return;
		}

		outputTypes.add(outputType);
		notifyInputOutputChangeListeners();
	}

	@Override
	public void removeOutputType(AnnotationTypeOrFeature outputType) {

		if (!outputTypes.contains(outputType)) {
			return;
		}

		outputTypes.remove(outputType);
		notifyInputOutputChangeListeners();

	}

	@Override
	public void setOutputTypes(ArrayList<AnnotationTypeOrFeature> newOutputTypes) {

		if (outputTypes.containsAll(newOutputTypes)
				&& newOutputTypes.size() == outputTypes.size()) {// Only works
																	// because
																	// its a
																	// set.
			return;
		}

		outputTypes = new ArrayList<AnnotationTypeOrFeature>();
		outputTypes.addAll(newOutputTypes);
		notifyInputOutputChangeListeners();
	}

	@Override
	public MinimizedStatusEnum getMinimizedStatus() {
		return minimized;
	}

	@Override
	public void setMinimizedStatus(MinimizedStatusEnum minimized) {

		if (this.minimized != minimized) {
			this.minimized = minimized;
			notifyMinimizedStatusChangeListeners();
		}
	}

	@Override
	public boolean getLockedStatus() {
		return toBooleanLock(lockStatus);
	}

	/**
	 * Converts the internal lock status value to a boolean representing whether
	 * it represents a lock or not.
	 * 
	 * @param in
	 * @return
	 */
	private boolean toBooleanLock(LockStatusEnum in) {
		if (lockStatus == LockStatusEnum.DIRECTLOCK
				|| lockStatus == LockStatusEnum.INDIRECTLOCK) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setLocked() {
		setLocked(LockStatusEnum.DIRECTLOCK);
	}

	@Override
	public void setUnlocked() {
		setLocked(LockStatusEnum.UNLOCKED);
	}

	@Override
	public void indirectlyLocked() {
		if (lockStatus != LockStatusEnum.DIRECTLOCK) {
			setLocked(LockStatusEnum.INDIRECTLOCK);
		}
	}

	@Override
	public void indirectlyUnlocked() {
		if (lockStatus != LockStatusEnum.DIRECTLOCK) {
			setLocked(LockStatusEnum.UNLOCKED);
		}
	}

	/**
	 * Use this method to update the lockStatus field as it ensures correct
	 * listeners are notified.
	 * 
	 * @param val
	 */
	protected void setLocked(LockStatusEnum val) {
		if (lockStatus != val) {
			lockStatus = val;
			notifyLockedStatusChangeListeners();
		}
	}

	@Override
	public ArrayList<Parameter> getConfigurationParameters() {
		return basicParameters.getConfigurationParameters();
	}

	@Override
	public void setConfigurationParameters(ArrayList<Parameter> params) {
		basicParameters.setConfigurationParameters(params);
	}

	@Override
	public ArrayList<Parameter> getCommonParameters() {
		return commonParameters.getConfigurationParameters();
	}

	@Override
	public void setCommonParameters(ArrayList<Parameter> params) {
		commonParameters.setConfigurationParameters(params);
	}

	@Override
	public ArrayList<ParameterGroup> getParameterGroups() {
		return parameterGroups;
	}

	@Override
	public void setParameterGroups(ArrayList<ParameterGroup> parameterGroups) {
		this.parameterGroups = parameterGroups;
		notifyParameterGroupsChangeListeners();
	}

	@Override
	public String getParameterSearchStratergy() {
		return parameterSearchStratergy;
	}

	@Override
	public void setParameterSearchStratergy(String stratergy) {
		if (parameterSearchStratergy != null
				&& parameterSearchStratergy.equals(stratergy)) {
			return;
		}
		parameterSearchStratergy = stratergy;
		notifyParameterConfigurationChangeListers();
	}

	@Override
	public String getDefaultParameterGroup() {
		return parameterDefaultGroup;
	}

	@Override
	public void setDefaultParameterGroup(String defaultGroup) {
		if (parameterDefaultGroup != null
				&& parameterDefaultGroup.equals(defaultGroup)) {
			return;
		}
		parameterDefaultGroup = defaultGroup;
		notifyParameterConfigurationChangeListers();
	}

	@Override
	public boolean checkUnsavedChanges() {
		return unsavedChanges;
	}

	@Override
	public void setComponentChanged() {
		if (!unsavedChanges) {
			unsavedChanges = true;
			notifySavedStatusChangeListeners();
			if (parentComponent != null) {
				parentComponent.setComponentChanged();
			}
		}
	}

	@Override
	public void setComponentSaved() {
		if (unsavedChanges) {
			unsavedChanges = false;
			notifySavedStatusChangeListeners();
		}
	}

	@Override
	public String getFlowControllerIdentifier() {
		return identifier;
	}

	@Override
	public void setFlowControllerIdentifier(String identifier) {
		if (this.identifier == null || !this.identifier.equals(identifier)) {
			this.identifier = identifier;
			notifyFlowControlChangeListeners();
		}

	}

	/**
	 * Registers a new component to be notified if the general description of
	 * the component is changed. See listener interface for list of parameters
	 * covered.
	 * 
	 * @param The
	 *            Component to register
	 */
	@Override
	public void registerComponentDescriptionChangeListener(
			DescriptionChangeListener listener) {
		assert (listener != null);
		componentDescriptionChangeListeners.add(listener);
	}

	protected void notifyComponentDescriptionChangeListeners() {
		for (DescriptionChangeListener listener : componentDescriptionChangeListeners) {
			listener.ComponentDescriptionChanged(this);
		}
		setComponentChanged();
	}

	@Override
	public void registerDistibutionInformationChangedListener(
			DistributionInformationChangeListener listener) {
		assert (listener != null);
		distributionInformationChangeListeners.add(listener);
	}

	protected void notifyDistributionInformationChangedListeners() {
		for (DistributionInformationChangeListener listener : distributionInformationChangeListeners) {
			listener.distributionInformationChanged(this);
		}
		setComponentChanged();
	}

	@Override
	public void registerLanguagesChangeListener(LanguageChangeListener listener) {
		assert (listener != null);
		languageChangeListeners.add(listener);
	}

	protected void notifyLanguagesChangeListener() {
		for (LanguageChangeListener listener : languageChangeListeners) {
			listener.languagesChanged(this);
		}
		setComponentChanged();
	}

	@Override
	public void registerInputOutputChangeListener(
			InputOutputChangeListener listener) {
		assert (listener != null);
		inputOutputChangeListeners.add(listener);
	}

	protected void notifyInputOutputChangeListeners() {
		for (InputOutputChangeListener listener : inputOutputChangeListeners) {
			listener.inputOutputChanged(this);
		}
		setComponentChanged();
	}

	@Override
	public void registerSavedStatusChangeListener(
			SavedStatusChangeListener listener) {
		assert (listener != null);
		savedStatusChangeListeners.add(listener);
	}

	protected void notifySavedStatusChangeListeners() {
		for (SavedStatusChangeListener listener : savedStatusChangeListeners) {
			listener.savedStatusChanged(this);
		}
	}

	@Override
	public void registerParametersChangedListener(
			ParametersChangedListener listener) {
		parametersChangedListeners.add(listener);
	}

	public void notifyParametersChangedListeners() {
		for (ParametersChangedListener listener : parametersChangedListeners) {
			listener.parametersChanged(this);
		}
		setComponentChanged();
	}

	@Override
	public void registerParameterConfigurationChangeListener(
			ParameterConfigurationChangeListener listener) {
		assert (listener != null);
		parameterConfigurationChangeListeners.add(listener);
	}

	protected void notifyParameterConfigurationChangeListers() {
		for (ParameterConfigurationChangeListener listener : parameterConfigurationChangeListeners) {
			listener.parameterConfigurationChanged(this);
		}
		setComponentChanged();
	}

	@Override
	public void registerMinimizedStatusChangeListener(
			MinimizedStatusChangeListener listener) {
		assert (listener != null);
		minimizedStatusChangeListeners.add(listener);
	}

	protected void notifyMinimizedStatusChangeListeners() {
		for (MinimizedStatusChangeListener listener : minimizedStatusChangeListeners) {
			listener.minimizedStatusChanged(this);
		}
		// This is only a display property so no need to notify saved change
		// listeners.
	}

	@Override
	public void registerLockedStatusChangeListener(
			LockedStatusChangeListener listener) {
		lockedStatusChangeListeners.add(listener);
	}

	protected void notifyLockedStatusChangeListeners() {
		for (LockedStatusChangeListener listener : lockedStatusChangeListeners) {
			listener.lockStatusChanged(this);
		}
		// This is only a display property so no need to notify saved change
		// listeners.
	}

	@Override
	public void registerParentLockedStatusChangeListener(
			LockedStatusChangeListener listener) {
		parentLockedStatusChangeListeners.add(listener);
	}

	@Override
	public void notifyParentLockedStatusChangeListeners(Component parent) {
		for (LockedStatusChangeListener listener : parentLockedStatusChangeListeners) {
			listener.lockStatusChanged(parent);
		}
	}

	@Override
	public void registerParameterGroupsChangeListener(
			ParameterGroupsChangeListener listener) {
		parameterGroupsChangeListeners.add(listener);
	}

	protected void notifyParameterGroupsChangeListeners() {
		for (ParameterGroupsChangeListener listener : parameterGroupsChangeListeners) {
			listener.parameterGroupsChanged(this);
		}
		setComponentChanged();
	}

	@Override
	public void registerFlowControlChangedListener(
			FlowControlChangeListener listener) {
		flowControlChangeListeners.add(listener);
	}

	protected void notifyFlowControlChangeListeners() {
		for (FlowControlChangeListener listener : flowControlChangeListeners) {
			listener.flowControlChanged(this);
		}
		setComponentChanged();
	}

	@Override
	public ResourceSpecifier getResourceCreationSpecifier() {
		// TODO move this to somewhere Analysis engine specific?
		AnalysisEngineDescription description = UIMAFramework
				.getResourceSpecifierFactory()
				.createAnalysisEngineDescription();

		setupProcessingResourceMetaData(description.getAnalysisEngineMetaData());
		setupResourceCreationSpecifier(description);

		return description;
	}

	protected void setupResourceCreationSpecifier(
			ResourceCreationSpecifier description) {

		description.setImplementationName(getImplementationName());
		description
				.setResourceManagerConfiguration(resourceManagerConfiguration);
		if (description instanceof AnalysisEngineDescription) {
			((AnalysisEngineDescription) description)
					.setPrimitive(!isAggregate());
		}

	}

	protected void setupProcessingResourceMetaData(
			ProcessingResourceMetaData metaData) {

		setupResourceMetaData(metaData);

		metaData.setTypeSystem(typeSystemDescription);
		metaData.setTypePriorities(typePriorities);
		metaData.setFsIndexCollection(fsIndexCollection);
		metaData.setOperationalProperties(operationalProperties);

		// Use capabilities from the model to override base capabilities
		if (capabilities != null && capabilities.length > 0) {
			Capability capabilityOne = capabilities[0];
			capabilityOne.setInputs(convertTypeOrFeatureList(getInputTypes()));
			capabilityOne
					.setOutputs(convertTypeOrFeatureList(getOutputTypes()));
			capabilityOne.setLanguagesSupported(getLanguageTypes().toArray(
					new String[getLanguageTypes().size()]));
			capabilities[0] = capabilityOne;
			metaData.setCapabilities(capabilities);
		} else {
			Capability capabilityOne = UIMAFramework
					.getResourceSpecifierFactory().createCapability();
			capabilityOne.setInputs(convertTypeOrFeatureList(getInputTypes()));
			capabilityOne
					.setOutputs(convertTypeOrFeatureList(getOutputTypes()));
			capabilityOne.setLanguagesSupported(getLanguageTypes().toArray(
					new String[getLanguageTypes().size()]));
			Capability[] newCapabilities = new Capability[1];
			newCapabilities[0] = capabilityOne;
			metaData.setCapabilities(capabilities);
		}
	}

	private TypeOrFeature[] convertTypeOrFeatureList(
			ArrayList<AnnotationTypeOrFeature> list) {

		ArrayList<TypeOrFeature> returnVals = new ArrayList<TypeOrFeature>();
		for (AnnotationTypeOrFeature tof : list) {
			TypeOrFeature newTypeOrFeature = UIMAFramework
					.getResourceSpecifierFactory().createTypeOrFeature();

			newTypeOrFeature.setName(tof.getTypeName());
			newTypeOrFeature.setType(tof.isType());
			newTypeOrFeature.setAllAnnotatorFeatures(tof
					.isAllAnnotatorFeatures());

			returnVals.add(newTypeOrFeature);
		}
		return returnVals.toArray(new TypeOrFeature[returnVals.size()]);
	}

	protected void setupResourceMetaData(ResourceMetaData metaData) {

		// Basic MetaData
		metaData.setName(getName());
		metaData.setDescription(getDescription());
		metaData.setVendor(getVendor());
		metaData.setVersion(getVersion());
		metaData.setCopyright(getCopyright());

		// Parameters
		ConfigurationParameterDeclarations settings = metaData
				.getConfigurationParameterDeclarations();
		ConfigurationParameterSettings values = metaData
				.getConfigurationParameterSettings();
		setupConfigurationParameters(settings, values);

		// TODO Attributes/UUID?
	}

	protected void setupConfigurationParameters(
			ConfigurationParameterDeclarations declarations,
			ConfigurationParameterSettings settings) {

		declarations.setSearchStrategy(getParameterSearchStratergy());
		declarations.setDefaultGroupName(getDefaultParameterGroup());

		// Basic Parameters
		for (Parameter param : getConfigurationParameters()) {
			ConfigurationParameter newParam = UIMAFramework
					.getResourceSpecifierFactory()
					.createConfigurationParameter();
			Object value = constructConfigurationParameter(param, newParam);
			if (value != null) {
				settings.setParameterValue(param.getName().equals("") ? null
						: param.getName(), value);
			}

			declarations.addConfigurationParameter(newParam);
		}

		// Common Parameters
		for (Parameter param : getCommonParameters()) {
			ConfigurationParameter newParam = UIMAFramework
					.getResourceSpecifierFactory()
					.createConfigurationParameter();
			Object value = constructConfigurationParameter(param, newParam);
			if (value != null) {
				settings.setParameterValue(param.getName(), value);
			}

			declarations.addCommonParameter(newParam);
		}

		// Configuration Groups
		ArrayList<ConfigurationGroup> groups = new ArrayList<ConfigurationGroup>();
		for (ParameterGroup group : getParameterGroups()) {
			ConfigurationGroup configGroup = UIMAFramework
					.getResourceSpecifierFactory().createConfigurationGroup();
			configGroup.setNames(group.getNames());

			ArrayList<ConfigurationParameter> parameters = new ArrayList<ConfigurationParameter>();
			for (Parameter param : group.getConfigurationParameters()) {
				ConfigurationParameter newParam = UIMAFramework
						.getResourceSpecifierFactory()
						.createConfigurationParameter();
				Object value = constructConfigurationParameter(param, newParam);
				if (value != null) {
					settings.setParameterValue(group.getNames()[0]/*
																 * just the
																 * first
																 */,
							param.getName(), value);
				}
				parameters.add(newParam);
			}
			configGroup.setConfigurationParameters(parameters
					.toArray(new ConfigurationParameter[parameters.size()]));

			groups.add(configGroup);
		}

		declarations.setConfigurationGroups(groups
				.toArray(new ConfigurationGroup[groups.size()]));
	}

	/**
	 * Based on the input parameter param, produces a new ConfigurationParameter
	 * newParam whose value is returned as an Object.
	 * 
	 * @param param
	 *            Input from the model.
	 * @param newParameter
	 *            Output ConfigurationParameter
	 * @return the value of newParameter
	 */
	protected Object constructConfigurationParameter(Parameter param,
			ConfigurationParameter newParameter) {

		Object value = null;

		newParameter.setName(param.getName());
		newParameter.setDescription(param.getDescription().equals("") ? null
				: param.getDescription());
		newParameter.setMandatory(param.isMandatory());
		newParameter.setMultiValued(param.isMultivalued());

		if (!param.isMultivalued()) {
			// Single valued parameters
			if (param instanceof BooleanParameter) {
				newParameter.setType(ConfigurationParameter.TYPE_BOOLEAN);
				value = ((BooleanParameter) param).getParameter();
			} else if (param instanceof StringParameter) {
				newParameter.setType(ConfigurationParameter.TYPE_STRING);
				value = ((StringParameter) param).getParameter();
			} else if (param instanceof IntegerParameter) {
				newParameter.setType(ConfigurationParameter.TYPE_INTEGER);
				value = ((IntegerParameter) param).getParameter();
			} else if (param instanceof FloatParameter) {
				newParameter.setType(ConfigurationParameter.TYPE_FLOAT);
				value = ((FloatParameter) param).getParameter();
			} else {
				assert (false);
			}
		} else {
			// Multi-valued parameters
			if (param instanceof BooleanParameter) {
				newParameter.setType(ConfigurationParameter.TYPE_BOOLEAN);
				value = ((BooleanParameter) param).getParameters();
			} else if (param instanceof StringParameter) {
				newParameter.setType(ConfigurationParameter.TYPE_STRING);
				value = ((StringParameter) param).getParameters();
			} else if (param instanceof IntegerParameter) {
				newParameter.setType(ConfigurationParameter.TYPE_INTEGER);
				value = ((IntegerParameter) param).getParameters();
			} else if (param instanceof FloatParameter) {
				newParameter.setType(ConfigurationParameter.TYPE_FLOAT);
				value = ((FloatParameter) param).getParameters();
			} else {
				assert (false);
			}
		}

		// TODO Do we need to set Overrides?

		return value;
	}

	protected void extractFromSpecifier(ResourceCreationSpecifier spec) {
		setImplementationName(spec.getImplementationName());
		resourceManagerConfiguration = spec.getResourceManagerConfiguration();
	}

	protected void extractFromProcessingResouceMetaData(
			ProcessingResourceMetaData metaData) {
		extractFromResourceMetaData(metaData);

		typeSystemDescription = metaData.getTypeSystem();
		typePriorities = metaData.getTypePriorities();
		fsIndexCollection = metaData.getFsIndexCollection();
		operationalProperties = metaData.getOperationalProperties();

		capabilities = metaData.getCapabilities();
		if (capabilities.length > 0) {
			Capability capabilityOne = capabilities[0];
			ArrayList<AnnotationTypeOrFeature> inputs = extractAnnotationTypeOrFeatureList(capabilityOne
					.getInputs());
			setInputTypes(inputs);
			ArrayList<AnnotationTypeOrFeature> outputs = extractAnnotationTypeOrFeatureList(capabilityOne
					.getOutputs());
			setOutputTypes(outputs);
			String[] languages = capabilityOne.getLanguagesSupported();
			setLanguageTypes(new ArrayList<String>(Arrays.asList(languages)));
		} else {
			setInputTypes(new ArrayList<AnnotationTypeOrFeature>());
			setOutputTypes(new ArrayList<AnnotationTypeOrFeature>());
			setLanguageTypes(new ArrayList<String>());
		}
	}

	private ArrayList<AnnotationTypeOrFeature> extractAnnotationTypeOrFeatureList(
			TypeOrFeature[] list) {
		ArrayList<AnnotationTypeOrFeature> retVals = new ArrayList<AnnotationTypeOrFeature>();
		for (TypeOrFeature tof : list) {
			AnnotationTypeOrFeature annotationTypeOrFeature = new AnnotationTypeOrFeature(
					tof.getName());
			annotationTypeOrFeature.setAllAnnotatorFeatures(tof
					.isAllAnnotatorFeatures());
			annotationTypeOrFeature.setType(tof.isType());
			retVals.add(annotationTypeOrFeature);
		}
		return retVals;
	}

	protected void extractFromResourceMetaData(ResourceMetaData metaData) {

		// Basic metaData
		setName(metaData.getName());
		setDescription(metaData.getDescription());
		setVendor(metaData.getVendor());
		setVersion(metaData.getVersion());
		setCopyright(metaData.getCopyright());

		// Parameters
		ConfigurationParameterSettings settings = metaData
				.getConfigurationParameterSettings();
		ConfigurationParameterDeclarations declarations = metaData
				.getConfigurationParameterDeclarations();

		setParameterSearchStratergy(declarations.getSearchStrategy());
		setDefaultParameterGroup(declarations.getDefaultGroupName());

		// Basic Parameters
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		for (ConfigurationParameter param : declarations
				.getConfigurationParameters()) {
			Parameter newParameter = AbstractParameter.constructParameter(
					param, settings.getParameterValue(param.getName()));
			parameters.add(newParameter);
		}
		setConfigurationParameters(parameters);

		// Common Parameters
		parameters = new ArrayList<Parameter>();
		for (ConfigurationParameter param : declarations.getCommonParameters()) {
			Parameter newParameter = AbstractParameter.constructParameter(
					param, settings.getParameterValue(param.getName()));
			parameters.add(newParameter);
		}
		setCommonParameters(parameters);

		// Parameter Groups
		ArrayList<ParameterGroup> groups = new ArrayList<ParameterGroup>();
		for (ConfigurationGroup group : declarations.getConfigurationGroups()) {
			ParameterGroup parameterGroup = new ParameterGroup(this);
			parameterGroup.setNames(group.getNames());

			ArrayList<Parameter> groupParameters = new ArrayList<Parameter>();
			for (ConfigurationParameter param : group
					.getConfigurationParameters()) {
				Object paramValue = settings.getParameterValue(
						group.getNames()[0], param.getName());
				Parameter newParameter = AbstractParameter.constructParameter(
						param, paramValue);
				groupParameters.add(newParameter);
			}
			parameterGroup.setConfigurationParameters(groupParameters);

			groups.add(parameterGroup);
		}
		setParameterGroups(groups);
	}

	public static Component constructComponentFromXML(CpeInclude include) {

		// TODO this method is not guranteed to work

		String path = include.getSourceUrlString();
		String pathBase = path.substring(0, path.lastIndexOf("/") + 1);

		String pathname = include.get();

		// System.out.println("include" + include);
		// System.out.println("URL " + include.getSourceUrlString());

		pathname = pathname.replace('\\', '/');// TODO
		// System.out.println("finalComp" + pathBase+"../../" +pathname);
		return AbstractComponent.constructComponentFromXML(pathBase + "../../"
				+ pathname);

	}

	public static Component constructComponentFromXML(Import imp) {
		String name = imp.getName();

		if (name != null) {
			// example: <import
			// name="some.classpath.based.path.without.extension.and.dot.delimted">
			// <import name> field is without .xml, and separated with dot but
			// not slash
			String classPathBasedLocation = name.replace('.', '/') + ".xml";

			// TODO for kano replaced by actual class loader for each workflow
			ClassLoader classLoader = AbstractComponent.class.getClassLoader();
			URL resource = classLoader.getResource(classPathBasedLocation);

			try {
				XMLInputSource xmlInputSource = new XMLInputSource(resource);
				Component subComponent = AbstractComponent
						.constructComponentFromXML(xmlInputSource);
				return subComponent;
			} catch (IOException e) {
				System.err.println("IOException in "
						+ AbstractComponent.class.getName()
						+ " due to reading import name field : " + resource);
				e.printStackTrace();
				return null;
			}
		} else {
			// This assumes location contains a relative path TODO check if its
			// really relative
			String path = imp.getSourceUrlString();
			String pathBase = path.substring(0, path.lastIndexOf("/") + 1);
			return AbstractComponent.constructComponentFromXML(pathBase
					+ imp.getLocation());
		}
	}

	public static Component constructComponentFromXML(String descriptorLocation) {
		try {
			return constructComponentFromXML(new XMLInputSource(
					descriptorLocation));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Component constructComponentFromXML(XMLInputSource inputSource) {
		try {
			ResourceSpecifier resourceSpecifier = (ResourceSpecifier) UIMAFramework
					.getXMLParser().parse(inputSource);
			return constructComponentFromXML(resourceSpecifier);
		} catch (InvalidXMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Component constructComponentFromXML(
			ResourceSpecifier resourceSpecifier) {

		if (resourceSpecifier instanceof AnalysisEngineDescription) {
			AnalysisEngineDescription desc = (AnalysisEngineDescription) resourceSpecifier;

			if (desc.isPrimitive()) {
				return new PrimitiveAnalysisEngine(desc);
			} else {
				return new AggregateAnalysisEngine(desc);
			}

		} else if (resourceSpecifier instanceof CasConsumerDescription) {
			CasConsumerDescription description = (CasConsumerDescription) resourceSpecifier;
			return new CasConsumer(description);

		} else if (resourceSpecifier instanceof CollectionReaderDescription) {
			CollectionReaderDescription description = (CollectionReaderDescription) resourceSpecifier;
			return new CollectionReader(description);

		} else if (resourceSpecifier instanceof URISpecifier) {

			System.out.println("URISpecifier");
			URISpecifier spec = (URISpecifier) resourceSpecifier;
			return new SOAPComponent(spec);

		} else if (resourceSpecifier instanceof CpeDescription) {

			// TODO
			System.out.println("yep its a cpedescription");

			return null;

		} else {

			System.err.println("In " + AbstractComponent.class.getName()
					+ " unrecognized " + resourceSpecifier.getClass());
			return null;
		}
	}
}
