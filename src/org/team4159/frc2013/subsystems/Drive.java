package org.team4159.frc2013.subsystems;

import org.team4159.frc2013.IO;
import org.team4159.support.Subsystem;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public final class Drive extends RobotDrive implements Subsystem
{
	public static final double KP = 0.5;
	public static final double KI = 0.1;
	public static final double KD = 0.0;
	
	public static final Drive instance = new Drive ();
	
	private Drive ()
	{
		super (IO.driveMotorLeft, IO.driveMotorRight);
		setInvertedMotor (MotorType.kRearLeft, true);
		setInvertedMotor (MotorType.kRearRight, true);
	}
	
	/**
	 * Shifts the gearbox to high or low speed position.
	 * @param high Shift to high-speed if true, low-speed if false.
	 */
	public void setGearboxPosition (boolean high)
	{
		IO.driveGearbox.set (high ? Value.kForward : Value.kReverse);
	}
	
	/**
	 * Gets current position of gearbox.
	 * @return true if high-speed, low-speed if false
	 */
	public boolean getGearboxPosition ()
	{
		return IO.driveGearbox.get () == Value.kForward;
	}
}
