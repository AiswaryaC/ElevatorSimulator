/**
 * 
 */
package com.sample.simulator.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author AiswaryaC
 *
 */
public class ElevatorException extends Throwable{
	public ElevatorException(String message) {
		Logger.getGlobal().log(Level.SEVERE, message);
	}

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

}
