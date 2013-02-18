package org.team4159.frc2013.subsystems;

import org.team4159.frc2013.IO;
import org.team4159.support.Controller;
import org.team4159.support.Subsystem;

/**
 * The {@link Elevator} class implements all code required to
 * calibrate the elevator to the top and precisely move it to arbitrary
 * positions along the range of movement.
 */
public final class Elevator implements Subsystem
{
	// PID constants
	public static final double KP = 0.2;
	public static final double KI = 0.0;
	public static final double KD = 0.0;
	
	// FIXME: set ELEVATOR_HEIGHT to range of elevator motion
	/**
	 * The range of elevator movement in inches.
	 */
	public static final double ELEVATOR_HEIGHT = 200;
	
	/**
	 * The output level at which the motor should be set
	 * during calibration. 
	 */
	public static final double CALIBRATION_OUTPUT = 0.4;
	
	/**
	 * The height of each tray relative to the bottom of the range of movement
	 * when the elevator is at the bottom.
	 */
	public static final double[] TRAY_POSITIONS = { 10, 20, 30 };

	/**
	 * The height of the frisbee input relative to the bottom of the
	 * range of movement.
	 */
	public static final double INPUT_POSITION = 40;
	
	/**
	 * The height of the shooter output relative to the bottom of the
	 * range of movement. 
	 */
	public static final double OUTPUT_POSITION = 160;
	
	public static final Elevator instance = new Elevator ();
	
	private boolean calibrated = false;

	/**
	 * Calibrates the elevator by moving elevator up until switch is activated,
	 * at which the encoder would be reset.
	 */
	public void calibrate ()
	{
		// set elevator motor to calibration output
		IO.elevatorMotor.set (CALIBRATION_OUTPUT);
		
		// wait for switch to be touched
		while (!isAtTop ())
			Controller.sleep (1);
		
		// switch touched, reset sensor there
		IO.elevatorEncoder.reset ();
		
		// we are calibrated!
		calibrated = true;
	}
	
	/**
	 * Checks whether the elevator has been calibrated.
	 * @return true if calibrated.
	 */
	public boolean isCalibrated ()
	{
		return calibrated;
	}
	
	/**
	 * Checks whether the elevator is at the top of its range.
	 * @return true if elevator is at the top of its range.
	 */
	public boolean isAtTop ()
	{
		if (calibrated)
			return getDistanceFromTop () <= 0;
		else
			return IO.elevatorTop.get () == false;
	}
	
	/**
	 * Checks whether the elevator is at the bottom of its range.
	 * @return true if the elevator is at the bottom of its range.
	 * @throws IllegalStateException elevator is not calibrated.
	 */
	public boolean isAtBottom ()
	{
		if (calibrated)
			return getDistanceFromBottom () <= 0;
		else
			throw new IllegalStateException ("not calibrated");
	}
	
	/**
	 * Gets the distance between the elevator and maximum height.
	 * @return distance in [UNITS]
	 */
	public double getDistanceFromTop ()
	{
		return -IO.elevatorEncoder.getDistance ();
	}
	
	/**
	 * Gets the distance between the elevator and minimum height.
	 * @return distance in [UNITS]
	 */
	public double getDistanceFromBottom ()
	{
		return ELEVATOR_HEIGHT - getDistanceFromTop ();
	}
	
	/**
	 * Sets the distance between the elevator and maximum height. Note that
	 * the elevator will not reach the position until {@link Elevator#isAtSetpoint()} returns true.
	 * @param x distance in inches
	 */
	public void setDistanceFromTop (double x)
	{
		IO.elevatorPID.reset ();
		IO.elevatorPID.setSetpoint (x);
		IO.elevatorPID.enable ();
	}
	
	/**
	 * Sets the distance between the elevator and minimum height. Note that
	 * the elevator will not reach the position until {@link Elevator#isAtSetpoint()} returns true.
	 * @param x distance in inches
	 */
	public void setDistanceFromBottom (double x)
	{
		setDistanceFromTop (ELEVATOR_HEIGHT - x);
	}
	
	/**
	 * Checks whether the elevator is at the previously set position. The tolerance
	 * can be set in the {@link IO} class.
	 * @return true if the elevator is at the previously set position.
	 */
	public boolean isAtSetpoint ()
	{
		return IO.elevatorPID.onTarget ();
	}
}