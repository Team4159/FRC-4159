package org.team4159.frc2013.controllers;

public class DisabledController extends Controller 
{
	public boolean active ()
	{
		return driverStation.isDisabled ();
	}
}
