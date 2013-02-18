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
		Elevator.instance.calibrate ();
		
		// set shooter speed
		Shooter.instance.setSpeed (3000);
		
		// fire the frisbees
		for (int i = Elevator.NUMBER_OF_TRAYS - 1; i >= 0; i--)
		{
			Elevator.instance.moveTrayToOutput (i);
			
			Elevator.instance.waitUntilAtSetpoint ();
			Shooter.instance.waitUntilAtSetpoint ();
			
			Shooter.instance.extend ();
			Controller.sleep (500);
			Shooter.instance.retract ();
			Controller.sleep (500);
		}
	}
}
