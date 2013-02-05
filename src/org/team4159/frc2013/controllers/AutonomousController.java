package org.team4159.frc2013.controllers;

import edu.wpi.first.wpilibj.RobotDrive;
import org.team4159.frc2013.IO;
import org.team4159.frc2013.Periodic;
import org.team4159.support.Controller;

public class AutonomousController extends Controller 
{
	public boolean active ()
	{
		return driverStation.isEnabled () && driverStation.isAutonomous ();
	}
	
	public void run ()
	{
		while (active ())
		{
			beginTiming ();
			
			Periodic.tick ();
			
			endTiming ();
		}
	}
}
