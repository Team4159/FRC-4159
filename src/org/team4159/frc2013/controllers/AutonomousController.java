package org.team4159.frc2013.controllers;

import org.team4159.frc2013.Controller;

public class AutonomousController extends Controller 
{
	public boolean active ()
	{
		return driverStation.isEnabled () && driverStation.isAutonomous ();
	}
	
	public void tick ()
	{
	}
}
