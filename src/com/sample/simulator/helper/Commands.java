/**
 * 
 */
package com.sample.simulator.helper;

/**
 * @author Aiswarya
 *
 */
public enum Commands {
	ADD_PASSENGER(1,"ADD PASSENGERS"), GOTO(2,"GOTO FLOOR"), EXIT(3,"EXIT"), GET_STATUS(4,"GET STATUS");

	public int operation;
	public String displayName;

	Commands(int operation, String displayName) {
		this.operation = operation;
		this.displayName = displayName;
	}

	public int getCommand() {
		return operation;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public static Commands getCommands(int input) {
	      for (Commands comm : Commands.values()) {
	          if (comm.operation == input) 
	        	  return comm;
	      }
	      throw new IllegalArgumentException("Invalid Operation");
	   }

	public static String getString() {
		StringBuffer stringCommands = new StringBuffer("**********************************************************\n");
		stringCommands.append("Enter any one of the below operations to be performed\n");
		for (Commands command : Commands.values()) {
			stringCommands.append(command.getCommand() + ". " + command.getDisplayName() + "\n");
		}
		stringCommands.append("*********************************************************");
		return stringCommands.toString();
	}
}
