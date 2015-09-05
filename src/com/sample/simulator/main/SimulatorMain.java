/**
 * 
 */
package com.sample.simulator.main;

import java.util.Scanner;

import com.sample.simulator.domain.Passenger;
import com.sample.simulator.helper.DisplayMessage;
import com.sample.simulator.service.ElevatorController;

/**
 * @author AiswaryaC
 *
 */
public class SimulatorMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int command = 1;
		int floorRequest = 1;

		// hard Coding this to be one for now. In future we can read that as the
		// input argument and create that many elevators
		// int noOfElevator = scan.nextInt;
		// get the number of floors in the building
		DisplayMessage
				.displayMessage("Enter the number of floors (less than 100): ");
		int numberOfFloors = scan.nextInt();
		DisplayMessage.displayMessage("Building the Floors... Floors 1 to "
				+ numberOfFloors + " built.");
		ElevatorController controller = new ElevatorController(numberOfFloors);
		controller.start();
		while (command != 3) {
			DisplayMessage
					.displayMessage("Enter Elevator Commands: 1. ADD PASSENGERS, 2. MOVE ELEVATOR 3. EXIT 4. GET STATUS");
			command = scan.nextInt();
			switch (command) {
			case 1:
				int noOfPassengers;
				int position;
				String getMore = "Y";
				do {
					DisplayMessage
							.displayMessage("Enter the number of passengers to be added and the Floor sperated by space ");
					noOfPassengers = scan.nextInt();
					position = scan.nextInt();
					DisplayMessage
							.displayMessage("Enter the destination Floor for "
									+ noOfPassengers
									+ " passengers seperated by space ");
					Passenger passenger;
					for (int i = 0; i < noOfPassengers; i++) {
						passenger = new Passenger();
						passenger.setPosition(position);
						passenger.setTargetFloor(scan.nextInt());
						controller.addPassenger(passenger);
					}
					System.out.println("Passengers added "
							+ controller.getBoardingQueue());
					DisplayMessage
							.displayMessage("Do you want to continue? Y/N");
					getMore = scan.next();
				} while ("Y".equals(getMore));
				//check if there are any hall calls or passengers in the lift and add commands appropriately			
				controller.updateCommands();
				
				break;
			case 2:
				DisplayMessage
						.displayMessage("Enter the destination floor (less than 100): ");
				floorRequest = scan.nextInt();
				controller.addFloor(floorRequest);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			
			case 3:
				DisplayMessage
						.displayMessage("Are you sure you want to exit? Y/N");
				if ("Y".equals(scan.next())) {
					controller.stopElevator();					
				} else {
					DisplayMessage.displayMessage("Program continues to run");
					command = 0;
				}
				DisplayMessage.closeConsole();	
				System.exit(0);
				break;
			case 4:
				DisplayMessage
						.displayMessage("Getting Elevator Status and the Commands pending");
				controller.getElevatorStatus();
				break;
			default:
				DisplayMessage
						.displayMessage("Please enter a valid option as a number");
				continue;				

			}

		}

		scan.close();

	}

}
