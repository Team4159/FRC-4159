package org.team4159.frc2013.controllers;

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
		while (active ())
		{
			beginTiming ();
			
			Periodic.tick ();
			
			endTiming ();
		}
	}
}
