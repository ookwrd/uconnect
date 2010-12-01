

/* First created by JCasGen Thu Jan 07 00:29:07 JST 2010 */
package org.evolutionarylinguistics.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** A truth value
 * Updated by JCasGen Fri Jan 08 22:32:53 JST 2010
 * XML source: /Users/lukemccrohon1/ParttimeWork/U-Compare/apache-uima/examples/src/org/evolutionarylinguistics/uima/testers/TESTERIfFalseRedBlack.xml
 * @generated */
public class TruthAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(TruthAnnotation.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TruthAnnotation() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TruthAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TruthAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TruthAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: value

  /** getter for value - gets 
   * @generated */
  public boolean getValue() {
    if (TruthAnnotation_Type.featOkTst && ((TruthAnnotation_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "org.evolutionarylinguistics.uima.TruthAnnotation");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((TruthAnnotation_Type)jcasType).casFeatCode_value);}
    
  /** setter for value - sets  
   * @generated */
  public void setValue(boolean v) {
    if (TruthAnnotation_Type.featOkTst && ((TruthAnnotation_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "org.evolutionarylinguistics.uima.TruthAnnotation");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((TruthAnnotation_Type)jcasType).casFeatCode_value, v);}    
  }

    