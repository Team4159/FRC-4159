package org.team4159.frc2013.controllers;

public class OperatorController extends Controller 
{
	public boolean active ()
	{
		return driverStation.isEnabled () && driverStation.isOperatorControl ();
	}
	
	public void tick ()
	{
		
	}
}
