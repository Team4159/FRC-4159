package org.team4159.frc2013.controllers;

public class TestController extends Controller 
{
	public boolean active ()
	{
		return driverStation.isEnabled () && driverStation.isTest ();
	}
}
