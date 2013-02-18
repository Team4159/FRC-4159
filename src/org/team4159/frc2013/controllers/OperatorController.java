package org.team4159.frc2013.controllers;

import org.team4159.frc2013.IO;
import org.team4159.frc2013.subsystems.Drive;
import org.team4159.support.Controller;
import org.team4159.support.ModeEnumerator;

public class OperatorController extends Controller 
{
	public OperatorController ()
	{
		super (ModeEnumerator.OPERATOR);
	}
	
	private void shiftGearbox ()
	{
		boolean shiftUp = IO.joystick2.getRawButton (3);
		boolean shiftDown = IO.joystick2.getRawButton (2);
		if (shiftUp ^ shiftDown)
			Drive.instance.setGearboxPosition (shiftUp);
	}
	
	public void tick ()
	{
		shiftGearbox ();
		Drive.instance.tankDrive (IO.joystick1, IO.joystick2);
	}
}
