/**
 * 
 */
package com.sample.simulator.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JFrame;

import bsh.util.JConsole;

import com.sample.simulator.domain.Direction;
import com.sample.simulator.domain.States;

/**
 * @author AiswaryaC
 *
 */
public class DisplayMessage {
	
	String message = "";
	
	public static DisplayMessage instance;
	public static File file = new File("C:\\personal\\Java Coding\\Elevator\\Elevator.log");
	private static JConsole console = buildConsole();
	private static JFrame frame;
	
	private DisplayMessage(){	
		buildConsole();
	}
	
	private static JConsole buildConsole() {
		//define a frame and add a console to it
    	JFrame frame = new JFrame("Elevator Workflow");
    	console = new JConsole();

    	frame.getContentPane().add(console);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(600,400);

    	frame.setVisible(true);    	
    	return console;
		
	}

	public DisplayMessage getInstance(){
		if(instance == null){
				instance = new DisplayMessage();			
		}
		return instance;
	}
	
	public static void writeToFile(String message){	
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)));
			writer.write(message);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();				
		}
		finally{
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void writeToConsole(String message){		
		console.println(message);

	}
	
	public static void displayMessage(String message){
		System.out.println(message);
	}

	public static void logElevatorStatus(int currentFloor, Direction direction, States status) {
		writeToConsole("Elevator is "+direction.getDirection()+"!\n Currect Floor: "+currentFloor + " Status: "+status.getState());
		
	}

	public static void closeConsole() {
		if(frame != null){
			frame.dispose();
		}
		
	}

}
