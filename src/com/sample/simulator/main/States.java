/**
 * 
 */
package com.sample.simulator.main;

/**
 * @author Aiswarya
 * 
 * This enum holds the state of the elevator if it is moving up/down or open/close or stays idle
 *
 */
public enum States {
	/**
	 * IDLE State
	 */
	IDLE("Idle"),
	/**
	 * OPEN state
	 */
	OPEN("Open"),
	/**
	 * CLOSE STATE
	 */
	CLOSE("Close");
	

	/**
	 * State of the elevator
	 */
	private String state;
	
	/**
	 * Constructor
	 * @param state
	 */
	States(String state){
		this.setState(state);
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	
}
