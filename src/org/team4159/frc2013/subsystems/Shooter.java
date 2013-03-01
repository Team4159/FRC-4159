package org.team4159.frc2013.subsystems;

import org.team4159.frc2013.IO;
import org.team4159.support.Controller;
import org.team4159.support.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public final class Shooter implements Subsystem
{
	public static final DoubleSolenoid.Value RETRACTED_SOLENOID_VALUE = DoubleSolenoid.Value.kReverse;
	public static final DoubleSolenoid.Value EXTENDED_SOLENOID_VALUE = DoubleSolenoid.Value.kForward;
	
	public static final int RETRACTED = 1;
	public static final int EXTENDED = 2;
	
	public static final double KP = 1.0;
	public static final double KI = 0.1;
	public static final double KD = 0.05;
	
	/**
	 * Time (in milliseconds) required to fully retract.
	 */
	public static final int RETRACTION_DURATION = 800;
	
	/**
	 * Time (in milliseconds) required to fully extend.
	 */
	public static final int EXTENSION_DURATION = 800;
	
	/**
	 * The tolerance (in RPM) smaller than which the shooter can be considered
	 * to be at the proper speed.
	 */
	public static final double SETPOINT_TOLERANCE = 300;
	
	public static final Shooter instance = new Shooter ();
	
	private int state = EXTENDED;
	private long stateChangeEnd;
	
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
	public boolean shooterReady ()
	{
		return IO.shooterPID.onTarget ();
	}
	
	/**
	 * Waits until the shooter is at the previously set speed.
	 */
	public void waitForShooter ()
	{
		while (!shooterReady ())
			Controller.sleep (1);
	}
	
	/**
	 * Extends the shooter piston.
	 */
	public void extend ()
	{
		IO.shooterPiston.set (EXTENDED_SOLENOID_VALUE);
		if (state == RETRACTED)
			_toggle ();
	}
	
	/**
	 * Retracts the shooter piston.
	 */
	public void retract ()
	{
		IO.shooterPiston.set (RETRACTED_SOLENOID_VALUE);
		if (state == EXTENDED)
			_toggle ();
	}
	
	/**
	 * Checks whether piston is at set position.
	 * @return true if piston is at set position.
	 */
	public boolean pistonIsReady ()
	{
		return System.currentTimeMillis () >= stateChangeEnd;
	}
	
	/**
	 * Waits until the piston is at set position.
	 */
	public void waitForPiston ()
	{
		long rem = stateChangeEnd - System.currentTimeMillis ();
		if (rem > 0)
			Controller.sleep (rem);
	}
	
	private void _toggle ()
	{
		long src_duration;
		long tgt_duration;
		
		if (state == RETRACTED)
		{
			state = EXTENDED;
			src_duration = RETRACTION_DURATION;
			tgt_duration = EXTENSION_DURATION;
		}
		else
		{
			state = RETRACTED;
			src_duration = EXTENSION_DURATION;
			tgt_duration = RETRACTION_DURATION;
		}
		
		long now = System.currentTimeMillis ();
		
		if (now >= stateChangeEnd)
		{
			stateChangeEnd = now + tgt_duration;
		}
		else
		{
			long stateChangeStart = stateChangeEnd - src_duration;
			stateChangeEnd = now + tgt_duration * (now - stateChangeStart) / src_duration;
		}
	}

	/**
	 * Directly sets the shooter motor output. Note that PID is disabled
	 * when this function is called.
	 * @param x the output to which the motor should be set.
	 */
	public void setMotorOutput (double x)
	{
		IO.shooterPID.disable ();
		IO.shooterMotor.set (x);
	}
}