

/* First created by JCasGen Thu Jan 07 16:34:41 JST 2010 */
package org.evolutionarylinguistics.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Jan 08 22:32:53 JST 2010
 * XML source: /Users/lukemccrohon1/ParttimeWork/U-Compare/apache-uima/examples/src/org/evolutionarylinguistics/uima/testers/TESTERIfFalseRedBlack.xml
 * @generated */
public class XmlAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(XmlAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected XmlAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public XmlAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public XmlAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public XmlAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: tagType

  /** getter for tagType - gets 
   * @generated */
  public String getTagType() {
    if (XmlAnnotation_Type.featOkTst && ((XmlAnnotation_Type)jcasType).casFeat_tagType == null)
      jcasType.jcas.throwFeatMissing("tagType", "org.evolutionarylinguistics.uima.XmlAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((XmlAnnotation_Type)jcasType).casFeatCode_tagType);}
    
  /** setter for tagType - sets  
   * @generated */
  public void setTagType(String v) {
    if (XmlAnnotation_Type.featOkTst && ((XmlAnnotation_Type)jcasType).casFeat_tagType == null)
      jcasType.jcas.throwFeatMissing("tagType", "org.evolutionarylinguistics.uima.XmlAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((XmlAnnotation_Type)jcasType).casFeatCode_tagType, v);}    
  }

    