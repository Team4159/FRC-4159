package org.team4159.frc2013.controllers;

import org.team4159.frc2013.IO;
import org.team4159.frc2013.subsystems.Drive;
import org.team4159.frc2013.subsystems.Elevator;
import org.team4159.support.Controller;

public class ResetController extends Controller
{
	public ResetController ()
	{
		super (-1);
	}

	public void run ()
	{
		Elevator.instance.setMotorOutput (0);
		IO.shooterPID.reset ();
		
		Drive.instance.drive (0, 0);
		IO.drivePIDLeft.reset ();
		IO.drivePIDRight.reset ();
	}
}
