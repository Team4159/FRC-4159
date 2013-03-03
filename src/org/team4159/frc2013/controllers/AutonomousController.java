package org.team4159.frc2013.controllers;

import org.team4159.frc2013.IO;
import org.team4159.frc2013.subsystems.Elevator;
import org.team4159.frc2013.subsystems.Shooter;
import org.team4159.support.Controller;
import org.team4159.support.DriverStationLCD;
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
            
                // set shooter speed
		Shooter.instance.setMotorOutput(.875);
		DriverStationLCD.setLine (2, "Shooter spinned up");
		// retract shooter so elevator can move
                Shooter.instance.raiseAngler();
		Shooter.instance.retract();
		Controller.sleep(500);
                
		// calibrate elevator
		Elevator.instance.calibrate ();
                Controller.sleep(800);
		DriverStationLCD.setLine (1, "Elevator Alligning");
		// fire the frisbees
		for (int i = Elevator.NUMBER_OF_TRAYS - 1; i >= 0; i--)
		{
			Elevator.instance.moveTrayToOutput (i);
			
			Elevator.instance.waitUntilAtSetpoint ();
			Controller.sleep(1000);
                        DriverStationLCD.setLine (1, "Moving Elevator lvl: " + i);
                        
			Shooter.instance.extend ();
                        Controller.sleep (700);
                        DriverStationLCD.setLine (1, "shooting lvl: " + i);
			Shooter.instance.retract ();
                        Controller.sleep (700);
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
