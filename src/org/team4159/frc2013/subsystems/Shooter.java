package org.team4159.frc2013.subsystems;

import org.team4159.frc2013.IO;
import org.team4159.support.Controller;
import org.team4159.support.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public final class Shooter implements Subsystem
{
	public static final DoubleSolenoid.Value RETRACTED_SOLENOID_VALUE = DoubleSolenoid.Value.kReverse;
	public static final DoubleSolenoid.Value EXTENDED_SOLENOID_VALUE = DoubleSolenoid.Value.kForward;
	
	public static final int RETRACTED = 1;
	public static final int EXTENDED = 2;
	
	public static final double KP = 0.160;
	public static final double KI = 0.010;
	public static final double KD = 0.300;
	
	/**
	 * Time (in milliseconds) required to fully retract.
	 */
	public static final int RETRACTION_DURATION = 1000;
	
	/**
	 * Time (in milliseconds) required to fully extend.
	 */
	public static final int EXTENSION_DURATION = 1000;
	
	/**
	 * The tolerance (in RPS) smaller than which the shooter can be considered
	 * to be at the proper speed.
	 */
	public static final double RPS_TOLERANCE = 0.5;
        
        /**
         * Estimated maximum rotational speed of the unloaded shooter wheel.
         */
        public static final double MAXIMUM_REVOLUTIONS_PER_SECOND = 90.0;
	
	public static final Shooter instance = new Shooter ();
	
	private int pistonState = EXTENDED;
	private long pistonStateChangeEnd;
        private double shooterSpeed = Double.NaN;
	
	private Shooter ()
	{
		// configure PID
                IO.shooterPID.setAbsoluteTolerance(RPS_TOLERANCE);
                IO.shooterPID.setOutputRange (0.0, 1.0);
	}

	/**
	 * Sets the speed of the shooter in RPS.
	 * @param x speed of shooter in RPS
	 */
	public void setSpeed (double x)
	{
            if (shooterSpeed != x)
            {
                IO.shooterPID.setSetpoint (shooterSpeed = x);
                IO.shooterPID.reset ();
            }
            
            IO.shooterPID.enable ();
	}
	
	/**
	 * Gets the speed of the shooter in RPS.
	 * @return speed of shooter in RPS
	 */
	public double getSpeed ()
	{
		return IO.shooterEncoder.getRate ();
	}
	
	/**
	 * Checks whether the shooter is at the previously set speed.
	 * @return true if the shooter is at the previously set speed.
	 */
	public boolean shooterIsReady ()
	{
		return IO.shooterPID.onTarget ();
	}
	
	/**
	 * Waits until the shooter is at the previously set speed.
	 */
	public void waitForShooter ()
	{
		while (!shooterIsReady ())
			Controller.sleep (1);
	}
	
	/**
	 * Extends the shooter piston.
	 */
	public void extend ()
	{
		IO.shooterPiston.set (EXTENDED_SOLENOID_VALUE);
		if (pistonState == RETRACTED)
			_toggle ();
	}
	
	/**
	 * Retracts the shooter piston.
	 */
	public void retract ()
	{
		IO.shooterPiston.set (RETRACTED_SOLENOID_VALUE);
		if (pistonState == EXTENDED)
			_toggle ();
	}
	
	public boolean pistonIsRetracted ()
	{
		return pistonState == RETRACTED;
	}
	
	/**
	 * Checks whether piston is at set position.
	 * @return true if piston is at set position.
	 */
	public boolean pistonIsReady ()
	{
		return System.currentTimeMillis () >= pistonStateChangeEnd;
	}
	
	/**
	 * Waits until the piston is at set position.
	 */
	public void waitForPiston ()
	{
		long rem = pistonStateChangeEnd - System.currentTimeMillis ();
		if (rem > 0)
			Controller.sleep (rem);
	}
	
	private void _toggle ()
	{
		long src_duration;
		long tgt_duration;
		
		if (pistonState == RETRACTED)
		{
			pistonState = EXTENDED;
			src_duration = RETRACTION_DURATION;
			tgt_duration = EXTENSION_DURATION;
		}
		else
		{
			pistonState = RETRACTED;
			src_duration = EXTENSION_DURATION;
			tgt_duration = RETRACTION_DURATION;
		}
		
		long now = System.currentTimeMillis ();
		
		if (now >= pistonStateChangeEnd)
		{
			pistonStateChangeEnd = now + tgt_duration;
		}
		else
		{
			long stateChangeStart = pistonStateChangeEnd - src_duration;
			pistonStateChangeEnd = now + tgt_duration * (now - stateChangeStart) / src_duration;
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
	
	public void raiseAngler ()
	{
		IO.shooterAngler.set (Value.kForward);
	}
	
	public void lowerAngler ()
	{
		IO.shooterAngler.set (Value.kReverse);
	}
        
        public boolean anglerIsUp ()
        {
            return IO.shooterAngler.get () == Value.kForward;
        }
}