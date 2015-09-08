/**
 * 
 */
package com.sample.simulator.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import com.sample.simulator.domain.Direction;
import com.sample.simulator.domain.Elevator;
import com.sample.simulator.domain.Passenger;
import com.sample.simulator.domain.States;
import com.sample.simulator.helper.DisplayMessage;

/**
 * @author AiswaryaC
 *
 */
public class ElevatorController extends Thread {

	private Set<Integer> upwardFloorsSet;
	private Set<Integer> downwardFloorsSet;
	private Queue<Passenger> boardingQueue;
	private int maximumFloors;
	private Elevator elevator;
	private static final int GROUND = 1;
	private volatile boolean stopFlag = false;

	/**
	 * Default Constructor
	 */
	public ElevatorController(int maximumFloors) {
		super();
		this.maximumFloors = maximumFloors;
		initializeElevator();

	}

	/**
	 * Copy Constructor
	 * 
	 * @param controller
	 */
	public ElevatorController(ElevatorController controller) {
		this.upwardFloorsSet = controller.upwardFloorsSet;
		this.downwardFloorsSet = controller.downwardFloorsSet;
		this.boardingQueue = controller.boardingQueue;
		this.elevator = controller.elevator;
	}

	/**
	 * Build an empty elevator
	 * 
	 * @return new elevator with default parameters
	 */
	public Elevator buildEmptyElevator() {
		Elevator elevator = new Elevator();
		List<Passenger> listOfPassengers = new ArrayList<Passenger>();
		elevator.setCurrentFloor(GROUND);
		elevator.setDirection(Direction.UP);
		elevator.setPassengers(listOfPassengers);
		return elevator;
	}

	/**
	 * Add the floor command button pressed by the passenger to the appropriate
	 * queue
	 * 
	 * @param elevator
	 * @param floorRequest
	 */
	public void addFloor(int floorRequest) {
		if (Direction.UP.equals(elevator.getDirection())
				&& floorRequest <= maximumFloors) {
			elevator.getUpCommands().add(floorRequest);
		} else if (Direction.DOWN.equals(elevator.getDirection())
				&& floorRequest >= GROUND) {
			elevator.getDownCommands().add(floorRequest);
		}
	}

	/**
	 * If more passengers are added or if there is a hall call then update the
	 * commands accordingly
	 * 
	 * @param boardingQueue
	 */
	public synchronized void updateCommands() {
		List<Passenger> toBeRemoved = new ArrayList<Passenger>();
		boolean addedPassenger = false;
		if (boardingQueue != null) {
			for (Passenger passenger : boardingQueue) {
				if (elevator.getPassengers().size() < Elevator.MAX_PASSENGER) {
					addedPassenger = elevator.loadPassenger(passenger);													
				} else {
					DisplayMessage
							.writeToConsole("Elevator reached the capacity. No more passengers can be loaded");
					break;
				}
				if(addedPassenger){
					addFloor(passenger.getTargetFloor());
					toBeRemoved.add(passenger);	
				}else{
					//add the position to the commands since that is the hall command for the elevator
					addFloor(passenger.getPosition());
				}
			}
			DisplayMessage.writeToConsole("Removing Passenger "+toBeRemoved);
			boardingQueue.removeAll(toBeRemoved);
		}
		

	}
	/**
	 * @return the upwardFloorsSet
	 */
	public Set<Integer> getUpwardFloorsSet() {
		return upwardFloorsSet;
	}

	/**
	 * @param upwardFloorsSet
	 *            the upwardFloorsSet to set
	 */
	public void setUpwardFloorsSet(Set<Integer> upwardFloorsSet) {
		this.upwardFloorsSet = upwardFloorsSet;
	}

	/**
	 * @return the downwardFloorsSet
	 */
	public Set<Integer> getDownwardFloorsSet() {
		return downwardFloorsSet;
	}

	/**
	 * @param downwardFloorsSet
	 *            the downwardFloorsSet to set
	 */
	public void setDownwardFloorsSet(Set<Integer> downwardFloorsSet) {
		this.downwardFloorsSet = downwardFloorsSet;
	}

	/**
	 * @return the boardingQueue
	 */
	public Queue<Passenger> getBoardingQueue() {
		if(boardingQueue == null){
			boardingQueue = new PriorityQueue<Passenger>();
		}
		return boardingQueue;
	}

	/**
	 * @param boardingQueue
	 *            the boardingQueue to set
	 */
	public void setBoardingQueue(Queue<Passenger> boardingQueue) {
		this.boardingQueue = boardingQueue;
	}

	/**
	 * @return the maximumFloors
	 */
	public int getMaximumFloors() {
		return maximumFloors;
	}

	/**
	 * @param maximumFloors
	 *            the maximumFloors to set
	 */
	public void setMaximumFloors(int maximumFloors) {
		this.maximumFloors = maximumFloors;
	}

	/**
	 * @return the elevator
	 */
	public Elevator getElevator() {
		return elevator;
	}

	/**
	 * @param elevator
	 *            the elevator to set
	 */
	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}

	/**
	 * add passenger to the queue
	 * 
	 * @param passenger
	 */
	public void addPassenger(Passenger passenger) {		
		getBoardingQueue().add(passenger);		
	}
	
	/**
	 * add all the passengers to the queue
	 * 
	 * @param passengers
	 */
	public void addPassenger(PriorityQueue<Passenger> passengers) {		
		if(passengers != null && !passengers.isEmpty()){
			getBoardingQueue().addAll(passengers);		
		}
	}

	/**
	 * initialize elevator with defaults
	 */
	protected void initializeElevator() {
		this.elevator = new Elevator();
		Elevator.MAX_FLOOR = getMaximumFloors();
		elevator.setUpCommands(new TreeSet<Integer>());
		elevator.setDownCommands(new TreeSet<Integer>());
		elevator.setPassengers(new ArrayList<Passenger>());
		elevator.setCurrentFloor(GROUND);
		elevator.setDirection(Direction.UP);
		elevator.setStatus(States.IDLE);
		elevator.setWaitTime(2000);
		elevator.setUpCommands(new TreeSet<Integer>());		
	}

	/**
	 * Remove the floor from the list
	 * 
	 * @param floorRequest
	 */
	public void removeFloor(int floorRequest) {

	}

	@Override
	public void run() {
		startElevator();
		while (!stopFlag) {
			if (!isElevatorEmpty()) {					
				//Move to the requested floor based on commands
				elevator.moveFloor();				
				updateCommands();
				
			}			
			//Set status as idle if there are not commands to operate on
			if(elevator.getUpCommands().isEmpty() && elevator.getDownCommands().isEmpty()){
				elevator.setStatus(States.IDLE);
			}
		}
		if (stopFlag) {
			if (elevator.getUpCommands().isEmpty()
					&& elevator.getDownCommands().isEmpty()) {
				DisplayMessage.writeToConsole("Exiting!");
			} else {
				elevator.emptyElevator();
			}
		}
	}

	/**
	 * Check if the commands are null and passenger in the elevator car are
	 * empty
	 * 
	 * @return true if empty
	 */
	private boolean isElevatorEmpty() {
		boolean isEmptyElevator = false;
		if (elevator.getDownCommands().isEmpty()
				&& elevator.getUpCommands().isEmpty()) {
			isEmptyElevator = true;
		}
		return isEmptyElevator;
	}

	/**
	 * Start elevator if not already started
	 */
	public void startElevator() {
		if (!elevator.isStartFlag()) {
			elevator.startElevator();
		} else {
			DisplayMessage.displayMessage("Elevator is Running");
			DisplayMessage.logElevatorStatus(elevator.getCurrentFloor(),
					elevator.getDirection(), elevator.getStatus());
		}

	}

	/**
	 * Stop elevator and stop the thread
	 */
	public void stopElevator() {
		if (!stopFlag) {
			elevator.stopElevator();
			stopFlag = true;
			DisplayMessage.displayMessage("Elevator Shutting down! Thank you!");
		} else {
			DisplayMessage
					.displayMessage("Elevator is stopped. Press start to continue.");
			DisplayMessage.logElevatorStatus(elevator.getCurrentFloor(),
					elevator.getDirection(), elevator.getStatus());
		}

	}

	/**
	 * Display the status of the elevator
	 */
	public void getElevatorStatus() {
		DisplayMessage.displayMessage(new Date().toString());
		DisplayMessage.displayMessage("Elevator Up Commands "+elevator.getUpCommands());
		DisplayMessage.displayMessage("Elevator Down Commands "+elevator.getDownCommands());
		DisplayMessage.displayMessage("Elevator Passengers "+elevator.getPassengers());
		DisplayMessage.logElevatorStatus(elevator.getCurrentFloor(),
				elevator.getDirection(), elevator.getStatus());
	
	}

}
