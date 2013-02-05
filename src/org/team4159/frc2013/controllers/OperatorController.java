package org.team4159.frc2013.controllers;

import edu.wpi.first.wpilibj.RobotDrive;
import org.team4159.frc2013.IO;
import org.team4159.frc2013.Periodic;
import org.team4159.support.Controller;
import org.team4159.support.ModeEnumerator;

public class OperatorController extends Controller 
{
	private final RobotDrive drive = new RobotDrive (IO.driveMotorLeft, IO.driveMotorRight);
	
	public OperatorController ()
	{
		super (ModeEnumerator.OPERATOR);
	}
	
	public void tick ()
	{
		Periodic.tick ();
		
		drive.arcadeDrive (IO.joystick1);
	}
}
