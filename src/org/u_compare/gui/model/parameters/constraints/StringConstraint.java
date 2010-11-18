package org.u_compare.gui.model.parameters.constraints;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.u_compare.gui.model.parameters.InvalidInputException;


public class StringConstraint extends Constraint {
	
		private int minlength = 0;
		private int maxLength = Integer.MAX_VALUE;
		
		private ArrayList<String> whiteList; //Null corresponds to no whiteList restriction
		private ArrayList<String> blackList; //Null corresponds to no blackList restriction
		
		private Pattern regex; //Null corresponds to no regex checking
		private Pattern characters; //Null corresponds to no character checking
		
		public StringConstraint(){}
		
		public StringConstraint(int min, int max){
			this.minlength = min;
			this.maxLength = max;
		}
		
		public void setMaxLength(int max){
			this.maxLength = max;
		}
		
		public void setMinLength(int min){
			this.minlength = min;
		}
		
		public void setLengthRange(int min, int max){
			this.minlength = min;
			this.maxLength = max;
		}
		
		public void setWhiteList(ArrayList<String> whiteList){
			this.whiteList = whiteList;
		}
		
		public void setBlackList(ArrayList<String> blackList){
			this.blackList = blackList;
		}
		
		public void setCharacterSet(String characters) {
			
			String charactersEscaped = "";
			for(int i = 0; i < characters.length(); i++){
				char c = characters.charAt(i);
				if(c == '\\' || c =='[' || c== ']' || c =='-'){
					charactersEscaped += '\\';
				}	
				charactersEscaped += c;
			}
			this.characters = Pattern.compile("[" + charactersEscaped + "]*");
		}
		

		public void setRegex(String regex) {
			this.regex = Pattern.compile(regex);
		}	
		
		public void validate(String value) throws ConstraintFailedException{		
			
			if(value.length() < minlength){
				throw new ConstraintFailedException("Input is too short. Please input a string of at least " + minlength + " characters.");			
			}
			
			if(value.length() > maxLength){
				throw new ConstraintFailedException("Input is too long. Please input a string of less than " + maxLength + " characters.");			
			}
			
			if(whiteList != null){
				boolean found = false;
				for(String i : whiteList){
					if (i.equals(value)) {
						found = true;
						break;
					}
				}	
				
				if(!found){
					throw new ConstraintFailedException("Input string does not belong to whiteListed set of acceptable values");
				}	
			}
			
			if(blackList != null){
				
				for(String i : blackList){
					
					if(i.equals(value)){
						throw new ConstraintFailedException("Input string belongs to blackListed set of unacceptable values");
					}
				}
			}
			
			if(regex != null){
				Matcher matcher = regex.matcher(value);
				
				if(!matcher.matches()){
					throw new ConstraintFailedException("Input string does not match the specified regex constraint: " + regex.pattern());
				}
			}
			
			if(characters != null){
				Matcher matcher = characters.matcher(value);
				
				if(!matcher.matches()){
					throw new ConstraintFailedException("Input string does not match the specified character set constraint: " + characters.pattern().substring(1,characters.pattern().length()-2));
				}
			}
		}

	}


