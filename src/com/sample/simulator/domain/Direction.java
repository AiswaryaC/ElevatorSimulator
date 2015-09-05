/**
 * 
 */
package com.sample.simulator.domain;

/**
 * @author Aiswarya
 *
 */
public enum Direction {
	/**
	 * Moving UP
	 */
	UP("Up"),
	/**
	 * Moving DOWN
	 */
	DOWN("DOWN");
	
	private String direction;
	
	Direction(String direction){
		this.setDirection(direction);
	}

	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
}
