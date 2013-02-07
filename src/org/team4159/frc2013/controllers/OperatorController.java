package org.team4159.frc2013.controllers;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
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
	
	private void shiftGearbox ()
	{
		boolean shiftUp = IO.joystick2.getRawButton (3);
		boolean shiftDown = IO.joystick2.getRawButton (2);
		if (shiftUp ^ shiftDown)
		{
			Value val = shiftUp ? Value.kForward : Value.kReverse;
			IO.driveGearboxLeft.set (val);
			IO.driveGearboxRight.set (val);
		}
	}
	
	public void tick ()
	{
		Periodic.tick ();
		shiftGearbox ();
		drive.tankDrive (IO.joystick1, IO.joystick2);
	}
}
