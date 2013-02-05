package org.team4159.frc2013.controllers;

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
			endTiming ();
		}
	}
}
