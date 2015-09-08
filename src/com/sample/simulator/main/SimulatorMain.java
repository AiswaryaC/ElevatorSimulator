/**
 * 
 */
package com.sample.simulator.main;

import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sample.simulator.domain.Passenger;
import com.sample.simulator.helper.Commands;
import com.sample.simulator.helper.DisplayMessage;
import com.sample.simulator.helper.ElevatorException;
import com.sample.simulator.service.ElevatorController;

/**
 * @author AiswaryaC
 *
 */
public class SimulatorMain {

	/**
	 * Scanner to get the user inputs
	 */
	private static Scanner scan = new Scanner(System.in);
	/**
	 * Number of floors in the elevator
	 */
	private static int numberOfFloors;

	/**
	 * The main method to get the elevator controller functionality from the
	 * user
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Commands command = Commands.GOTO;
		int floorRequest = 1;		
			// hard Coding this to be one for now. In future we can read that as
			// the
			// input argument and create that many elevators
			// int noOfElevator = scan.nextInt;
			// get the number of floors in the building
			DisplayMessage.displayMessage("Enter the number of floors (less than 100): ");
			try{
			numberOfFloors = scan.nextInt();			
			while(numberOfFloors <=1 || numberOfFloors >100){
				DisplayMessage.displayMessage("Elevator not supported for the given number. Please enter a valid value between 1 an 100");
				numberOfFloors = scan.nextInt();
			}
			DisplayMessage.displayMessage("Building the Floors... Floors 1 to " + numberOfFloors + " built.");
			ElevatorController controller = new ElevatorController(numberOfFloors);
			controller.start();
			while (command != Commands.EXIT) {
				try {
				DisplayMessage.displayMessage(Commands.getString());
				command = Commands.getCommands(scan.nextInt());
				switch (command) {
				case ADD_PASSENGER:
					PriorityQueue<Passenger> passengerQueue = new PriorityQueue<Passenger>();
					String getMore = "Y";
					do {
						passengerQueue.addAll(getPassengersToBeQueued());
						DisplayMessage.displayMessage("Do you want to add more passengers? Y/N");
						getMore = getYesNoUserInput();
					} while ("Y".equals(getMore));
					controller.addPassenger(passengerQueue);
					// check if there are any hall calls or passengers in the
					// lift
					// and add commands appropriately
					controller.updateCommands();
					break;
				case GOTO:
					DisplayMessage.displayMessage("Enter the destination floor (less than 100): ");
					floorRequest = getIntUserInput(1, numberOfFloors);
					;
					if (floorRequest < 1 || floorRequest > numberOfFloors) {
						throw new IllegalArgumentException("Not a valid input for floor number");
					}
					controller.addFloor(floorRequest);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				case EXIT:
					DisplayMessage.displayMessage("Are you sure you want to exit? Y/N");
					if ("Y".equalsIgnoreCase(scan.next())) {
						controller.stopElevator();
						DisplayMessage.closeConsole();
						System.exit(0);
					} else {
						DisplayMessage.displayMessage("Program continues to run");
						command = Commands.GOTO;						
					}					
					break;
				case GET_STATUS:
					DisplayMessage.displayMessage("Getting Elevator Status and the Commands pending");
					controller.getElevatorStatus();
					break;
				default:
					DisplayMessage.displayMessage("Please enter a valid option as a number");
					continue;

				}

			}catch (ElevatorException e) {
				Logger.getGlobal().log(Level.SEVERE, "Exception Occured in getting inputs");
				command = Commands.GOTO;
				continue;
			}
		} 
		

		scan.close();
			}catch(Exception e){
				DisplayMessage.displayMessage("Exception Occurred. System will exit "+e.getMessage());
				System.exit(0);
			}

	}

	/**
	 * Get the number of passengers and their position from the user to be added
	 * to the controller
	 * 
	 * @return list of passengers to be queued
	 * @throws ElevatorException
	 */
	private static PriorityQueue<Passenger> getPassengersToBeQueued() throws ElevatorException {
		int noOfPassengers;
		int position;
		int floorRequest;
		PriorityQueue<Passenger> passengerQueue = new PriorityQueue<Passenger>();
		DisplayMessage.displayMessage("Enter the number of passengers to be added and their position (between 1 and "
				+ numberOfFloors + ") sperated by space ");
		noOfPassengers = getIntUserInput(1, 100);
		position = getIntUserInput(1, numberOfFloors);
		if (noOfPassengers < 0 || position < 1 || position > numberOfFloors) {
			throw new IllegalArgumentException("Value exceeds the maximum/minimum limit");
		}
		DisplayMessage.displayMessage(
				"Enter the destination Floor for " + noOfPassengers + " passengers seperated by space ");
		Passenger passenger;
		for (int i = 0; i < noOfPassengers; i++) {
			passenger = new Passenger();
			passenger.setPosition(position);
			floorRequest = getIntUserInput(1, numberOfFloors);
			if (floorRequest == position) {
				throw new ElevatorException("Current and Destination floor cannot be the same");
			}
			passenger.setTargetFloor(floorRequest);
			passengerQueue.add(passenger);
		}
		return passengerQueue;
	}

	/**
	 * Get the integer from user and validate if it is within the allowable
	 * limit
	 * 
	 * @param min
	 * @param max
	 * @return
	 * @throws ElevatorException
	 */
	private static int getIntUserInput(int min, int max) throws ElevatorException {
		int input;
		try {
			input = scan.nextInt();
			if (input < min || input > max) {
				throw new IllegalArgumentException("Value exceeds the maximum/minimum limit of " + min + " and " + max);
			}
		} catch (IllegalArgumentException e) {
			throw new ElevatorException(e.getMessage());
		}
		return input;
	}

	/**
	 * Get yes or no user input and vaidate
	 * 
	 * @return
	 * @throws ElevatorException
	 */
	private static String getYesNoUserInput() throws ElevatorException {
		String input;
		try {
			input = scan.next();
			if (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N")) {
				throw new IllegalArgumentException("Value has to be \"Y\" or \"N\"");
			}
		} catch (IllegalArgumentException e) {
			throw new ElevatorException(e.getMessage());
		}
		return input;
	}
}
