package org.u_compare.gui.model.parameters.constraints;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.u_compare.gui.model.parameters.InvalidInputException;

public class ConstraintTester {

	@Before
	public void setUp(){
	}

	@After
	public void tearDown(){
	}

	@Test
	public void intergerRangeTest() throws ConstraintFailedException{
		
		IntegerConstraint constraint = new IntegerConstraint();
		constraint.validate("6");
		constraint.validate("7");
		constraint.validate("9");
	}
	
	@Test
	public void intergerRangeTest0() throws ConstraintFailedException{
		
		IntegerConstraint constraint = new IntegerConstraint(6, 9);
		constraint.validate("6");
		constraint.validate("7");
		constraint.validate("9");
	}
	
	@Test(expected=ConstraintFailedException.class)
	public void intergerRangeTest1() throws ConstraintFailedException{

		IntegerConstraint constraint = new IntegerConstraint(6, 9);
		constraint.validate("5");
		
	}
	
	@Test(expected=ConstraintFailedException.class)
	public void intergerRangeTest2() throws ConstraintFailedException{

		IntegerConstraint constraint = new IntegerConstraint(6, 9);
		constraint.validate("10");
		
	}
	
	@Test
	public void integerWhiteListTest() throws ConstraintFailedException{
		
		IntegerConstraint constraint = new IntegerConstraint();
		constraint.setWhiteList(new ArrayList<Integer>(Arrays.asList(1,2,3)));
		constraint.validate("1");
		constraint.validate("3");
		
	}
	
	@Test(expected=ConstraintFailedException.class)
		public void integerWhiteListTest1() throws ConstraintFailedException{
		
		IntegerConstraint constraint = new IntegerConstraint();
		constraint.setWhiteList(new ArrayList<Integer>(Arrays.asList(1,2,3)));
		constraint.validate("5");
		
	}
	
	@Test
	public void integerBlackListTest() throws ConstraintFailedException{
		IntegerConstraint constraint = new IntegerConstraint();
		constraint.setBlackList(new ArrayList<Integer>(Arrays.asList(1,2,3)));
		constraint.validate("0");
		constraint.validate("5");
	}
	
	@Test(expected=ConstraintFailedException.class)
		public void integerBlackListTest1() throws ConstraintFailedException{	
		IntegerConstraint constraint = new IntegerConstraint();
		constraint.setBlackList(new ArrayList<Integer>(Arrays.asList(1,2,3)));
		constraint.validate("2");	
	}
	
	@Test 
	public void string() throws ConstraintFailedException{	
		StringConstraint constraint = new StringConstraint();
		constraint.validate("Hi, I am a string");
	}

	@Test
	public void stringLengthRange1() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint(2,4);
		constraint.validate("aa");
		constraint.validate("bbbb");
	}
	
	@Test(expected=ConstraintFailedException.class)
	public void stringLengthRange2() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint(2,4);
		constraint.validate("a");
	}
	
	@Test(expected=ConstraintFailedException.class)
	public void stringLengthRange3() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint(2,4);
		constraint.validate("ccccc");	
	}
	
	@Test
	public void stringWhiteList() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setWhiteList(new ArrayList<String>(Arrays.asList("a","b","cc")));
		constraint.validate("cc");
		constraint.validate("a");
	}
	
	@Test(expected=ConstraintFailedException.class)
	public void stringWhiteList1() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setWhiteList(new ArrayList<String>(Arrays.asList("a","b","cc")));
		constraint.validate("d");
	}
	
	@Test
	public void stringBlackList() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setBlackList(new ArrayList<String>(Arrays.asList("a","b","cc")));
		constraint.validate("d");
		constraint.validate("a1");
	}
	
	@Test(expected=ConstraintFailedException.class)
	public void stringBlackList1() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setBlackList(new ArrayList<String>(Arrays.asList("a","b","cc")));
		constraint.validate("b");
	}
	
	@Test
	public void stringCharacterSet() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setCharacterSet("abcA");
		constraint.validate("Aabc");
		constraint.validate("");
	}
	
	@Test(expected=ConstraintFailedException.class)
	public void stringCharacterSet1() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setCharacterSet("abcA");
		constraint.validate("Aabcd");
	}
	
	@Test(expected=ConstraintFailedException.class)
	public void stringCharacterSet2() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setCharacterSet("abcA");
		constraint.validate("AB");
	}
	
	@Test
	public void stringCharacterSet3() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setCharacterSet("$~^[]{}]}_?*+|{a-h");
		constraint.validate("$~]}[^?{_*|++_-");
	}
	
	@Test(expected=ConstraintFailedException.class)
	public void stringCharacterSet4() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setCharacterSet("abc/\\.$*+|{}");
		constraint.validate("\\//..$~]}[^?{_*|++_");
	}
	
	@Test(expected=ConstraintFailedException.class)
	public void stringCharacterSet5() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setCharacterSet("a-z");
		constraint.validate("h");
	}
	
	@Test(expected=ConstraintFailedException.class)
	public void stringCharacterSet6() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setCharacterSet("abc/\\.$*+|{}");
		constraint.validate("h");
	}
	
	@Test
	public void stringRegex() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setRegex("a*b*");
		constraint.validate("aaabb");
		constraint.setRegex("[ab]*");
		constraint.validate("abaabb");
		constraint.setRegex("[ab]");
		constraint.validate("b");
	}

	@Test(expected=ConstraintFailedException.class)
	public void stringRegex1() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setRegex("a*b*");
		constraint.validate("aaabba");
	}
	
	@Test(expected=ConstraintFailedException.class)
	public void stringRegex2() throws ConstraintFailedException{
		StringConstraint constraint = new StringConstraint();
		constraint.setRegex("[ab]");
		constraint.validate("aa");
	}
}
