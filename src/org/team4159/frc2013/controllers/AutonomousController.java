package org.team4159.frc2013.controllers;

import org.team4159.frc2013.subsystems.Shooter;
import org.team4159.support.Controller;
import org.team4159.support.ModeEnumerator;

public class AutonomousController extends Controller
{
	public static final int MODE_LEFT = 1;
	public static final int MODE_RIGHT = 2;
	
	public AutonomousController ()
	{
		super (ModeEnumerator.AUTONOMOUS);
	}

	public void run ()
	{
		// remember to use this.sleep() in case autonomous mode ends early
		// make sure that only sleep() blocks
		
		Shooter.instance.setSpeed (60);
		// Shooter.instance.setSpeed (driverStation.getAnalogIn(1) * 100);
		Shooter.instance.raiseAngler ();
		Shooter.instance.retract ();
		
		Controller.sleep (1000);
		
		for (int i = 0; i < 3; i++)
		{
			Shooter.instance.waitForShooter ();
			Controller.sleep (500);
			
			Shooter.instance.extend ();
			Shooter.instance.waitForPiston ();
			Shooter.instance.retract ();
			Shooter.instance.waitForPiston ();
		}
		
		Controller.sleep (1000);
		
		Shooter.instance.setSpeed (0);
		Shooter.instance.lowerAngler ();
	}
}
