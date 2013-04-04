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
import edu.wpi.first.wpilibj.DriverStation;

class ElevatorTest
{
	private double[] samples = new double[8];
	private double[] samplessorted = new double[samples.length];
	private int samplei = 0;
	
	private double maxRate = 0;
	
	public void tick ()
	{
		samples[samplei] = IO.elevatorEncoder.getRate ();
		samplei = (samplei + 1) % samples.length;
		
		System.arraycopy (samples, 0, samplessorted, 0, samples.length);
		Arrays.sort (samplessorted);
		
		double medianRate = samplessorted[samples.length / 2];
		maxRate = IO.joystick1.getRawButton (2) ?
			0 : Math.max (maxRate, Math.abs (medianRate));
		
		double elevatorOutput = IO.joystick1.getZ ();
		//IO.elevatorMotor.set (IO.joystick1.getTrigger () ? elevatorOutput : 0);
		
		DriverStationLCD.setLine (0, "Motor: " + elevatorOutput);
		DriverStationLCD.setLine (1, "Encoder: " + maxRate);
	}
}

class ShooterTest
{
    private double[] samplesraw = new double[21];
    private double[] samplessorted = new double[samplesraw.length];
    private int samplesindex = 0;
    
    public void tick ()
    {
        samplesraw[samplesindex++] = IO.shooterEncoder.getRate ();
        samplesindex = (samplesindex + 1) % samplesraw.length;
        
        System.arraycopy (samplesraw, 0, samplessorted, 0, samplesraw.length);
        Arrays.sort (samplessorted);
        
        DriverStationLCD.setLine (1, "ShtMed: " + samplessorted[samplesraw.length / 2]);
    }
}

public class OperatorController extends Controller 
{
    private boolean smallUnjamDown = false;
    private boolean bigUnjamDown = false;
    private boolean smallUnjamUp = false;
    
    private LowPassFilter shooterLPF = new LowPassFilter (5);
    
	public OperatorController ()
	{
		super (ModeEnumerator.OPERATOR);
	}
	
	public void tick ()
	{
		Drive.instance.arcadeDrive (IO.joystick2);
		
		boolean shiftDown = IO.joystick2.getRawButton (2);
		boolean shiftUp = IO.joystick2.getRawButton (3);
		if (shiftUp ^ shiftDown)
			Drive.instance.setGearboxPosition (shiftUp);
		
		/*
		if (IO.joystick1.getRawButton (6))
			Elevator.instance.moveTrayToInput (0);
		if (IO.joystick1.getRawButton (7))
			Elevator.instance.moveTrayToInput (1);
		if (IO.joystick1.getRawButton (8))
			Elevator.instance.moveTrayToInput (2);
		*/
		
		/*
		boolean elevatorUp = IO.joystick1.getRawButton (6);
		boolean elevatorDown = IO.joystick1.getRawButton (7);
		if (elevatorUp ^ elevatorDown)
			Elevator.instance.setMotorOutput (elevatorDown ? 0.5 : -0.5);
		else
			Elevator.instance.setMotorOutput (0);
		*/
		
		if (IO.joystick1.getRawButton (9))
			Elevator.instance.moveTrayToOutput (0);
		if (IO.joystick1.getRawButton (10))
			Elevator.instance.moveTrayToOutput (1);
		if (IO.joystick1.getRawButton (11))
			Elevator.instance.moveTrayToOutput (2);
                
                {
                    boolean newSmallUnjamDown = IO.joystick2.getRawButton(6);
                    
                    if (smallUnjamDown != newSmallUnjamDown)
                    {
                        smallUnjamDown = newSmallUnjamDown;
                        Elevator.instance.moveDown (smallUnjamDown ? 70 : -70);
                    }
                }
                
                {
                    boolean newBigUnjamDown = IO.joystick2.getRawButton(7);
                    
                    if (bigUnjamDown != newBigUnjamDown)
                    {
                        bigUnjamDown = newBigUnjamDown;
                        Elevator.instance.moveDown (bigUnjamDown ? 700 : -700);
                    }
                }
                
                {
                    boolean newSmallUnjamUp = IO.joystick2.getRawButton(8);
                    
                    if (smallUnjamUp != newSmallUnjamUp)
                    {
                        smallUnjamUp = newSmallUnjamUp;
                        Elevator.instance.moveUp (smallUnjamUp ? 105 : -105);
                    }
                }
		
		if (IO.joystick2.getTrigger ())
			Shooter.instance.extend ();
		else
			Shooter.instance.retract ();
		
                double z = (IO.joystick1.getZ () + 1) / 2;
		Shooter.instance.setMotorOutput (z);
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
                
                shooterLPF.update (Shooter.instance.getSpeed (), Entry.TICK_INTERVAL_MS / 1000.);
		DriverStationLCD.setLine (0, "Angler up? " + Shooter.instance.anglerIsUp ());
                DriverStationLCD.setLine (1, "Shooter Pwr: " + z );
                DriverStationLCD.setLine (2, "Shooter RPS:" + shooterLPF.get());
                /*
		DriverStationLCD.setLine (0, "ShtOut: " + IO.shooterMotor.get ());
		DriverStationLCD.setLine (1, "ShtEncV: " + IO.shooterEncoder.getRate ());
		DriverStationLCD.setLine (2, "ElevEnc: " + IO.elevatorEncoder.getDistance ());
		DriverStationLCD.setLine(3, "shooterPower: " + (IO.joystick1.getZ () + 1) / 2);
                */
	}
}
