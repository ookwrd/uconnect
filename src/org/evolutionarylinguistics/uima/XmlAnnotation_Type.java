
/* First created by JCasGen Wed Jan 06 01:53:52 JST 2010 */
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

/** 
 * Updated by JCasGen Fri Jan 08 22:32:53 JST 2010
 * @generated */
public class XmlAnnotation_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (XmlAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = XmlAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new XmlAnnotation(addr, XmlAnnotation_Type.this);
  			   XmlAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new XmlAnnotation(addr, XmlAnnotation_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = XmlAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.evolutionarylinguistics.uima.XmlAnnotation");
 
  /** @generated */
  final Feature casFeat_tagType;
  /** @generated */
  final int     casFeatCode_tagType;
  /** @generated */ 
  public String getTagType(int addr) {
        if (featOkTst && casFeat_tagType == null)
      jcas.throwFeatMissing("tagType", "org.evolutionarylinguistics.uima.XmlAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_tagType);
  }
  /** @generated */    
  public void setTagType(int addr, String v) {
        if (featOkTst && casFeat_tagType == null)
      jcas.throwFeatMissing("tagType", "org.evolutionarylinguistics.uima.XmlAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_tagType, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
  * @generated */
  public XmlAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_tagType = jcas.getRequiredFeatureDE(casType, "tagType", "uima.cas.String", featOkTst);
    casFeatCode_tagType  = (null == casFeat_tagType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_tagType).getCode();

  }
}



    