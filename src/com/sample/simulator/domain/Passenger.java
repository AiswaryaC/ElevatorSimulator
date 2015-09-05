/**
 * 
 */
package com.sample.simulator.domain;

import java.io.Serializable;

/**
 * @author AiswaryaC
 *
 */
public class Passenger implements Comparable<Passenger>, Serializable{
	
	
	/**
	 * version ID default
	 */
	private static final long serialVersionUID = 1L;
	
	//private int sourceFloor;
	private int targetFloor;
	private int position;
	
	
	/**
	 * @return the targetFloor
	 */
	public int getTargetFloor() {
		return targetFloor;
	}
	/**
	 * @param targetFloor the targetFloor to set
	 */
	public void setTargetFloor(int targetFloor) {
		this.targetFloor = targetFloor;
	}
	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
	/**
	 * @param i the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	
	@Override
	public int compareTo(Passenger p2) {
		if(this.targetFloor > p2.targetFloor){
			return 1;
		}
		if(this.targetFloor < p2.targetFloor){
			return -1;
		}
		return 0;
	}
	
	public String toString(){
		return "Passenger travelling from "+position+" to "+targetFloor;
	}
	

}
