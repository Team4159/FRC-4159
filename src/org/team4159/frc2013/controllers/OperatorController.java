package org.team4159.frc2013.controllers;

import org.team4159.frc2013.IO;
import org.team4159.frc2013.subsystems.Drive;
import org.team4159.frc2013.subsystems.Elevator;
import org.team4159.frc2013.subsystems.Shooter;
import org.team4159.support.Controller;
import org.team4159.support.ModeEnumerator;
import edu.wpi.first.wpilibj.DriverStation;

public class OperatorController extends Controller 
{
	private int ticks = 0;
	
	public OperatorController ()
	{
		super (ModeEnumerator.OPERATOR);
	}
	
	public void tick ()
	{
		Drive.instance.arcadeDrive (IO.joystick1);
		
		boolean shiftDown = IO.joystick1.getRawButton (2);
		boolean shiftUp = IO.joystick1.getRawButton (3);
		if (shiftUp ^ shiftDown)
			Drive.instance.setGearboxPosition (shiftUp);
		
		/*
		boolean elevatorDown = IO.joystick1.getRawButton (4);
		boolean elevatorUp = IO.joystick1.getRawButton (5);
		if (elevatorDown ^ elevatorUp)
			Elevator.instance.setMotorOutput (elevatorUp ? -0.7 : 0.4);
		else
			Elevator.instance.setMotorOutput (0);
		*/
		
		if (IO.joystick1.getRawButton (6))
			Elevator.instance.moveTrayToInput (0);
		if (IO.joystick1.getRawButton (7))
			Elevator.instance.moveTrayToInput (1);
		if (IO.joystick1.getRawButton (8))
			Elevator.instance.moveTrayToInput (2);
		
		if (IO.joystick1.getRawButton (9))
			Elevator.instance.moveTrayToOutput (0);
		if (IO.joystick1.getRawButton (10))
			Elevator.instance.moveTrayToOutput (1);
		if (IO.joystick1.getRawButton (11))
			Elevator.instance.moveTrayToOutput (2);
		
		
		if (IO.joystick1.getTrigger ())
			Shooter.instance.extend ();
		else
			Shooter.instance.retract ();
		
		Shooter.instance.setMotorOutput ((IO.joystick1.getZ () + 1) / 2);
		
		DriverStation ds = DriverStation.getInstance ();
		IO.elevatorPID.setPID (
			ds.getAnalogIn (1),
			ds.getAnalogIn (2),
			ds.getAnalogIn (3)
		);
		
		if (ticks++ % 10 == 0)
		{
			System.out.println (
				"raw: " + IO.elevatorEncoder.get () +
				"\t" + 
				"elev: " + IO.elevatorEncoder.getDistance ()
			);
		}
	}
}
