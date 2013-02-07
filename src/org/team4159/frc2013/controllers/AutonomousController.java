package org.team4159.frc2013.controllers;

import org.team4159.frc2013.IO;
import org.team4159.frc2013.Periodic;
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
		// remember to use this.sleep() in case autonomous mode
		// ends early
		// make sure that only sleep() blocks
		
		IO.driveMotorLeft.set (0.1);
		sleep (1000);
		IO.driveMotorLeft.set (0.0);
	}
}
