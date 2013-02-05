package org.team4159.frc2013.controllers;

import org.team4159.support.Controller;

public class TestController extends Controller 
{
	public boolean active ()
	{
		return driverStation.isEnabled () && driverStation.isTest ();
	}
}
