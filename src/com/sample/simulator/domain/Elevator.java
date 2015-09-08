package com.sample.simulator.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.sample.simulator.helper.DisplayMessage;

public class Elevator {

	public static int MAX_FLOOR;
	private static final int GROUND = 1;
	private int currentFloor;
	private int nextFloor;
	private States status;
	private Direction direction;
	private int waitTime;

	public static int MAX_PASSENGER = 3;

	private List<Passenger> passengers;

	private TreeSet<Integer> upCommands;
	private TreeSet<Integer> downCommands;
	private boolean startFlag;

	/**
	 * Default Constructor
	 */
	public Elevator() {

	}

	/**
	 * Copy Constructor
	 * 
	 * @param anotherEevator
	 */
	public Elevator(Elevator anotherElevator) {
		this.currentFloor = anotherElevator.currentFloor;
		this.status = anotherElevator.status;
		this.direction = anotherElevator.direction;
		this.waitTime = anotherElevator.waitTime;
		this.passengers = anotherElevator.passengers;

	}

	/**
	 * default constructor
	 */
	public void emptyElevator() {

	}

	/**
	 * Move the elevator to next floor based on the direction and commands
	 */
	public void moveFloor() {
		DisplayMessage.writeToConsole("Moving " + direction.getDirection()
				+ " from " + currentFloor);
		switch (direction) {
		case UP:
			while (currentFloor != MAX_FLOOR && !upCommands.isEmpty()) {
				nextFloor = upCommands.pollFirst();
				if (currentFloor < nextFloor) {
					moveUp();
					DisplayMessage
							.writeToConsole("/\\"
									+ nextFloor
									+ "Stop! Opening elevator...Loading/Unloading Passengers. Please wait...");
					status = States.OPEN;
					removePassengers();

				}else if(currentFloor == nextFloor){
					DisplayMessage
					.writeToConsole("/\\"
							+ nextFloor
							+ "Stop! Opening elevator...Loading/Unloading Passengers. Please wait...");
					status = States.OPEN;
					removePassengers();
				}
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DisplayMessage.writeToConsole("Closing elevator...");
				status = States.CLOSE;
			}
			if (currentFloor == MAX_FLOOR) {
				DisplayMessage
						.writeToConsole("Reached maximum floor - Change direction to DOWN");
				direction = Direction.DOWN;
				status = States.IDLE;
			} else if (upCommands.isEmpty()) {
				if (!downCommands.isEmpty()) {
					nextFloor = downCommands.pollLast();
					moveUp();
				}
				direction = Direction.DOWN;
			}
			break;
		case DOWN:
			while (currentFloor != GROUND && !downCommands.isEmpty()) {
				nextFloor = downCommands.pollLast();
				if (currentFloor > nextFloor) {
					moveDown();
					DisplayMessage
							.writeToConsole("/\\"
									+ nextFloor
									+ "Stop! Opening elevator...Loading/Unloading Passengers. Please wait...");
					status = States.OPEN;
					removePassengers();
				}else if(currentFloor == nextFloor){
					DisplayMessage
					.writeToConsole("/\\"
							+ nextFloor
							+ "Stop! Opening elevator...Loading/Unloading Passengers. Please wait...");
					status = States.OPEN;
					removePassengers();
				}
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DisplayMessage.writeToConsole("Closing elevator...");
				status = States.CLOSE;
			}
			if (currentFloor == GROUND) {
				DisplayMessage
						.writeToConsole("Reached first floor - Change direction to UP");
				direction = Direction.UP;
				status = States.IDLE;
			} else if (downCommands.isEmpty()) {
				if (!upCommands.isEmpty()) {
					nextFloor = upCommands.pollFirst();
					moveDown();
				}
				direction = Direction.UP;
			}
			break;
		default:
			// TODO throw unknown direction exception
			break;
		}		
		DisplayMessage.logElevatorStatus(currentFloor, direction, status);
	}

	private void removePassengers() {
		List<Passenger> passengersToBeRemoved = new ArrayList<Passenger>();
		for (Passenger passenger : passengers) {
			// if the passenger has reached their desired destination, remove
			// from the list
			if (currentFloor == passenger.getTargetFloor()) {
				passengersToBeRemoved.add(passenger);
			}
		}
		passengers.removeAll(passengersToBeRemoved);

	}

	/**
	 * Move elevator up by one level
	 * 
	 * @param nextFloor
	 */
	public void moveUp() {
		for (; currentFloor < nextFloor; currentFloor++) {
			DisplayMessage.writeToConsole("/\\" + currentFloor);
			status = States.CLOSE;
		}

	}

	/**
	 * Move elevator down by one level
	 * 
	 * @param nextFloor
	 */
	public void moveDown() {
		// TODO Make this private
		for (; currentFloor > nextFloor; currentFloor--) {
			DisplayMessage.writeToConsole("\\/" + currentFloor);
			status = States.CLOSE;
		}

	}

	/**
	 * Add passenger to the elevator if the elevator is not full
	 * 
	 * @param passenger
	 */
	public boolean loadPassenger(Passenger passenger) {
		boolean loaded = false;
		if (passenger != null && passenger.getPosition() == currentFloor
				&& isPassengerSameDirection(passenger)) {
			getPassengers().add(passenger);
			loaded = true;
		}
		return loaded;
	}

	/**
	 * Check if the passenger is travelling the same direction as the elevator,
	 * then add passenger
	 * 
	 * @param passenger
	 *            to be checked
	 * @return true if same direction
	 */
	private boolean isPassengerSameDirection(Passenger pass) {
		boolean isSameDirection = false;
		if (Direction.UP.equals(direction)
				&& pass.getTargetFloor() > currentFloor) {
			isSameDirection = true;
		} else if (Direction.DOWN.equals(direction)
				&& pass.getTargetFloor() < currentFloor) {
			isSameDirection = true;
		}
		return isSameDirection;
	}

	/**
	 * Remove passenger from the queue at each stop
	 * 
	 * @param passenger
	 */
	public void unloadPassenger(Passenger passenger) {
		if (passengers != null && passengers.contains(passenger)) {
			passengers.remove(passenger);
		}

	}

	/**
	 * Stop elevator and set state as IDLE
	 */
	public void stopElevator() {
		DisplayMessage.writeToConsole("Elevator shuttin down.Please wait...");
		status = States.IDLE;
		DisplayMessage.closeConsole();
		startFlag = false;
	}

	public void startElevator() {
		DisplayMessage.writeToConsole("Elevator Started! Welcome!");
		this.direction = Direction.UP;
		setStartFlag(true);
		DisplayMessage.logElevatorStatus(this.currentFloor, this.direction,
				this.status);
		DisplayMessage.writeToConsole("Waiting for commands...");

	}

	/**
	 * @return the currentFloor
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}

	/**
	 * @param currentFloor
	 *            the currentFloor to set
	 */
	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}

	/**
	 * @return the nextFloor
	 */
	public int getNextFloor() {
		return nextFloor;
	}

	/**
	 * @param nextFloor
	 *            the nextFloor to set
	 */
	public void setNextFloor(int nextFloor) {
		this.nextFloor = nextFloor;
	}

	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * @return the waitTime
	 */
	public int getWaitTime() {
		return waitTime;
	}

	/**
	 * @param waitTime
	 *            the waitTime to set
	 */
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	/**
	 * @return the passengers
	 */
	public List<Passenger> getPassengers() {
		if (passengers == null) {
			passengers = new ArrayList<Passenger>();
		}
		return passengers;
	}

	/**
	 * @param passengers
	 *            the passengers to set
	 */
	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	/**
	 * @return the state
	 */
	public States getStatus() {
		return status;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setStatus(States state) {
		this.status = state;
	}

	/**
	 * @return the upCommands
	 */
	public TreeSet<Integer> getUpCommands() {
		return upCommands;
	}

	/**
	 * @param upCommands
	 *            the upCommands to set
	 */
	public void setUpCommands(TreeSet<Integer> upCommands) {
		this.upCommands = upCommands;
	}

	/**
	 * @return the downCommands
	 */
	public TreeSet<Integer> getDownCommands() {
		return downCommands;
	}

	/**
	 * @param downCommands
	 *            the downCommands to set
	 */
	public void setDownCommands(TreeSet<Integer> downCommands) {
		this.downCommands = downCommands;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * @return the startFlag
	 */
	public boolean isStartFlag() {
		return startFlag;
	}

	/**
	 * @param startFlag
	 *            the startFlag to set
	 */
	public void setStartFlag(boolean startFlag) {
		this.startFlag = startFlag;
	}

}
