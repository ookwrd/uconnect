
/* First created by JCasGen Thu Jan 07 00:29:07 JST 2010 */
package org.evolutionarylinguistics.uima;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** A truth value
 * Updated by JCasGen Fri Jan 08 22:32:53 JST 2010
 * @generated */
public class TruthAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TruthAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TruthAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TruthAnnotation(addr, TruthAnnotation_Type.this);
  			   TruthAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TruthAnnotation(addr, TruthAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = TruthAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.evolutionarylinguistics.uima.TruthAnnotation");
 
  /** @generated */
  final Feature casFeat_value;
  /** @generated */
  final int     casFeatCode_value;
  /** @generated */ 
  public boolean getValue(int addr) {
        if (featOkTst && casFeat_value == null)
      jcas.throwFeatMissing("value", "org.evolutionarylinguistics.uima.TruthAnnotation");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_value);
  }
  /** @generated */    
  public void setValue(int addr, boolean v) {
        if (featOkTst && casFeat_value == null)
      jcas.throwFeatMissing("value", "org.evolutionarylinguistics.uima.TruthAnnotation");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_value, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
  * @generated */
  public TruthAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_value = jcas.getRequiredFeatureDE(casType, "value", "uima.cas.Boolean", featOkTst);
    casFeatCode_value  = (null == casFeat_value) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_value).getCode();

  }
}



    