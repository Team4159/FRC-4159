package org.team4159.frc2013.subsystems;

import org.team4159.frc2013.IO;
import org.team4159.support.Controller;
import org.team4159.support.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Shooter implements Subsystem
{
	public static final double KP = 1.0;
	public static final double KI = 0.1;
	public static final double KD = 0.05;
	
	/**
	 * The tolerance (in RPM) smaller than which the shooter can be considered
	 * to be at the proper speed.
	 */
	public static final double SETPOINT_TOLERANCE = 300;
	
	public static final Shooter instance = new Shooter ();
	
	private Shooter ()
	{
		// configure PID
		IO.shooterPID.setAbsoluteTolerance (SETPOINT_TOLERANCE);
		IO.shooterPID.reset ();
	}

	/**
	 * Sets the speed of the shooter in RPM.
	 * @param x speed of shooter in RPM
	 */
	public void setSpeed (double x)
	{
		IO.shooterPID.setSetpoint (x);
		IO.shooterPID.enable ();
	}
	
	/**
	 * Gets the speed of the shooter in RPM.
	 * @return speed of shooter in RPM
	 */
	public double getSpeed ()
	{
		return IO.shooterEncoder.getRate ();
	}
	
	/**
	 * Checks whether the shooter is at the previously set speed.
	 * @return true if the shooter is at the previously set speed.
	 */
	public boolean isAtSetpoint ()
	{
		return IO.shooterPID.onTarget ();
	}
	
	/**
	 * Waits until the shooter is at the previously set speed.
	 */
	public void waitUntilAtSetpoint ()
	{
		while (!isAtSetpoint ())
			Controller.sleep (1);
	}
	
	/**
	 * Sets the position of the shooter piston.
	 * @param out true if the piston should be extended,
	 *            false if it should be retracted
	 */
	public void setPosition (boolean out)
	{
		IO.shooterPiston.set (out ? Value.kForward : Value.kReverse);
	}
	
	/**
	 * Extends the shooter piston.
	 */
	public void extend ()
	{
		setPosition (true);
	}
	
	/**
	 * Retracts the shooter piston.
	 */
	public void retract ()
	{
		setPosition (false);
	}
}