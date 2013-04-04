package org.team4159.frc2013.controllers;

import org.team4159.frc2013.Entry;
import org.team4159.frc2013.IO;
import org.team4159.frc2013.subsystems.Drive;
import org.team4159.frc2013.subsystems.Elevator;
import org.team4159.frc2013.subsystems.Shooter;
import org.team4159.support.Controller;
import org.team4159.support.DriverStationLCD;
import org.team4159.support.ModeEnumerator;
import org.team4159.support.filters.LowPassFilter;
import com.sun.squawk.util.Arrays;

public class OperatorController extends Controller 
{
    private boolean finePressed = false;
    private boolean fineShooter = false;
    private double fineShooterBase = 0.0;
    private double fineShooterLevel = 0.0;
    
    private LowPassFilter shooterLPF = new LowPassFilter (5);
    
	public OperatorController ()
	{
		super (ModeEnumerator.OPERATOR);
	}
	
	public void tick ()
	{
		Drive.instance.tankDrive(IO.joystick2,IO.joystick3);
		
		boolean shiftDown = IO.joystick2.getRawButton (2) || IO.joystick3.getRawButton(2);
		boolean shiftUp = IO.joystick2.getRawButton (3) || IO.joystick3.getRawButton(3);
		if (shiftUp ^ shiftDown)
			Drive.instance.setGearboxPosition (shiftUp);
		
		if (IO.joystick1.getTrigger ())
			Shooter.instance.extend ();
		else
			Shooter.instance.retract ();
		
                double shooterOutput;
                {
                    double z = (IO.joystick1.getZ () + 1) / 2;
                    if (Math.abs (z - fineShooterBase) > 0.1)
                        fineShooter = false;

                    boolean fasterPressed = IO.joystick1.getRawButton (3);
                    boolean slowerPressed = IO.joystick1.getRawButton (2);
                    
                    if (fasterPressed || slowerPressed)
                    {
                        if (!fineShooter)
                        {
                            fineShooter = true;
                            fineShooterBase = z;
                            fineShooterLevel = Math.floor (z * 100) / 100;
                        }
                        
                        if (!finePressed)
                        {
                            if (fasterPressed)
                                fineShooterLevel += 0.01;
                            else
                                fineShooterLevel -= 0.01;
                        }
                    }
                    
                    finePressed = fasterPressed || slowerPressed;
                    
                    Shooter.instance.setMotorOutput (shooterOutput = fineShooter ? fineShooterLevel : z);
                }
                //Shooter.instance.setSpeed (z * Shooter.MAXIMUM_REVOLUTIONS_PER_SECOND);
                
                if (false)
                {
                    // PID TESTING CODE
                    
                    IO.shooterPID.setPID (
                        driverStation.getAnalogIn(1),
                        driverStation.getAnalogIn(2),
                        driverStation.getAnalogIn(3)
                    );
                }
                
		boolean anglerDown = IO.joystick1.getRawButton (4);
		boolean anglerUp = IO.joystick1.getRawButton (5);
		if (anglerDown != anglerUp)
		{
			if (anglerUp)
				Shooter.instance.raiseAngler ();
			if (anglerDown)
				Shooter.instance.lowerAngler ();
		}
                if(IO.joystick1.getRawButton(3)){
                    IO.innerPickupMotor.set(1);
                }
                else{
                    IO.innerPickupMotor.set(0);
                }
                
                //shooterLPF.update (Shooter.instance.getSpeed (), Entry.TICK_INTERVAL_MS / 1000.);
		DriverStationLCD.setLine (0, "Angler up? " + Shooter.instance.anglerIsUp ());
                DriverStationLCD.setLine (1, "Shooter Pwr: " + (int)(shooterOutput * 100) + "%");
                DriverStationLCD.setLine (2, "Shooter RPS:" + shooterLPF.get());
                /*
		DriverStationLCD.setLine (0, "ShtOut: " + IO.shooterMotor.get ());
		DriverStationLCD.setLine (1, "ShtEncV: " + IO.shooterEncoder.getRate ());
		DriverStationLCD.setLine (2, "ElevEnc: " + IO.elevatorEncoder.getDistance ());
		DriverStationLCD.setLine(3, "shooterPower: " + (IO.joystick1.getZ () + 1) / 2);
                */
	}
}
