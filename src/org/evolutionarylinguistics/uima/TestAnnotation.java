

/* First created by JCasGen Thu Jan 07 17:12:24 JST 2010 */
package org.evolutionarylinguistics.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** Used for testing and development purposes
 * Updated by JCasGen Fri Jan 08 22:32:53 JST 2010
 * XML source: /Users/lukemccrohon1/ParttimeWork/U-Compare/apache-uima/examples/src/org/evolutionarylinguistics/uima/testers/TESTERIfFalseRedBlack.xml
 * @generated */
public class TestAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TestAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TestAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TestAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TestAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TestAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: label

  /** getter for label - gets 
   * @generated */
  public String getLabel() {
    if (TestAnnotation_Type.featOkTst && ((TestAnnotation_Type)jcasType).casFeat_label == null)
      jcasType.jcas.throwFeatMissing("label", "org.evolutionarylinguistics.uima.TestAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TestAnnotation_Type)jcasType).casFeatCode_label);}
    
  /** setter for label - sets  
   * @generated */
  public void setLabel(String v) {
    if (TestAnnotation_Type.featOkTst && ((TestAnnotation_Type)jcasType).casFeat_label == null)
      jcasType.jcas.throwFeatMissing("label", "org.evolutionarylinguistics.uima.TestAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((TestAnnotation_Type)jcasType).casFeatCode_label, v);}    
  }

    