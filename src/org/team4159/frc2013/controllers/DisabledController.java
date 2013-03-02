package org.team4159.frc2013.controllers;

import org.team4159.frc2013.IO;
import org.team4159.frc2013.subsystems.Elevator;
import org.team4159.support.Controller;
import org.team4159.support.ModeEnumerator;

public class DisabledController extends Controller 
{
	public DisabledController ()
	{
		super (ModeEnumerator.DISABLED);
	}
	
	public void tick ()
	{
		Elevator.instance.setMotorOutput (0);
		
		System.out.println (
			"elev raw: " + IO.elevatorEncoder.get () +
			"\t" + 
			"elev: " + IO.elevatorEncoder.getDistance ()
		);
		
		System.out.println (
			"shooter dist: " + IO.shooterEncoder.getDistance () + "\t" + 
			"shooter rate: " + IO.shooterEncoder.getRate ()
		);
	}
}
