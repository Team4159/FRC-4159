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
		/*OLD SHOOTER VALUES!!!!
                Shooter.instance.setMotorOutput(1.0);//shooting for center
                //Shooter.instance.setMotorOutput(.875)// shooting from back corner
                */
                //SVR VALUES!!! USE THESE WITH THE NEW SHOOTER!!
                Shooter.instance.setMotorOutput(.80);
                
                
		DriverStationLCD.setLine (2, "Shooter spinned up");
		// retract shooter so elevator can move
                Shooter.instance.raiseAngler();
		Shooter.instance.retract();
		// calibrate elevator
		//Elevator.instance.calibrate ();
                Controller.sleep(1000);
		DriverStationLCD.setLine (1, "Elevator Alligning");
		// fire the frisbees
		for (int i = 0; i < 3; i++)
		{
			//Elevator.instance.moveTrayToOutput (i);
			
			//Elevator.instance.waitUntilAtSetpoint ();
                        Shooter.instance.setMotorOutput(.6+i*.1);
			Controller.sleep(3500);//shooting from center, longer wait for spin up
                        //Controller.sleep(1000); //shooting from corners, shorter wait for spin up
                        DriverStationLCD.setLine (1, "Moving Elevator lvl: " + i);
                        
			Shooter.instance.extend ();
                        Controller.sleep (600);
                        DriverStationLCD.setLine (1, "shooting lvl: " + i);
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
