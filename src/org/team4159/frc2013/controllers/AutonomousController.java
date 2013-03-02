package org.team4159.frc2013.controllers;

import org.team4159.frc2013.IO;
import org.team4159.frc2013.subsystems.Elevator;
import org.team4159.frc2013.subsystems.Shooter;
import org.team4159.support.Controller;
import org.team4159.support.ModeEnumerator;

public class AutonomousController extends Controller 
{
	public AutonomousController ()
	{
		super (ModeEnumerator.AUTONOMOUS);
	}
	public void run ()
	{
		// remember to use this.sleep() in case autonomous mode ends early
		// make sure that only sleep() blocks
		
		// retract shooter so elevator can move
		Shooter.instance.retract();
		Controller.sleep(500);
		// calibrate elevator
		Elevator.instance.calibrate ();
		
		// set shooter speed
		Shooter.instance.setMotorOutput(1);
		
		// fire the frisbees
		for (int i = Elevator.NUMBER_OF_TRAYS - 1; i >= 0; i--)
		{
			Elevator.instance.moveTrayToOutput (i);
			
			Elevator.instance.waitUntilAtSetpoint ();
			
			Shooter.instance.extend ();
			Shooter.instance.retract ();
		}
        }
	
	/*public void run ()
	{
		System.out.println ("calibrating ...");
		long start = System.currentTimeMillis ();
		
		Shooter.instance.retract ();
		Elevator.instance.calibrate ();
		
		long elapsed = System.currentTimeMillis () - start;
		System.out.println ("calibrated! (took " + elapsed + " ms)");
	}*/
}
