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
	public static final double KP_down = 0.006;
	public static final double KI_down = 0.000;
	public static final double KD_down = 0.000;
	
	public static final double KP_up = 0.024;
	public static final double KI_up = 0.000;
	public static final double KD_up = 0.000;
	
	/**
	 * Elevator height.
	 */
	public static final double ELEVATOR_HEIGHT = 1289;
	
	/**
	 * The output level at which the motor should be set
	 * during calibration. 
	 */
	public static final double CALIBRATION_OUTPUT = 0.45;
	
	/**
	 * The height of each tray relative to the top of the range of movement
	 * at the output. The first element should represent the
	 * bottom-most tray.
	 */
	public static final double[] TRAY_OUTPUT_POSITIONS = { 16, 82, 155 };
	
	/**
	 * The height of each tray relative to the top of the range of movement
	 * at the input. The first element should represent the
	 * bottom-most tray.
	 */
	public static final double[] TRAY_INPUT_POSITIONS = { 1137, 1210, 1281 };
	
	/**
	 * Number of trays.
	 */
	public static final int NUMBER_OF_TRAYS = TRAY_OUTPUT_POSITIONS.length;
	
	/**
	 * The tolerance smaller than which the elevator can be considered
	 * to be at the proper position.
	 */
	public static final double SETPOINT_TOLERANCE = 2.0;
	
	public static final Elevator instance = new Elevator ();
	
	private boolean calibrated = false;
	private boolean[] trayFilled = new boolean[NUMBER_OF_TRAYS];
	
	// prevent instantiation, must access through #instance
	private Elevator () {}

	/**
	 * Calibrates the elevator by moving elevator up until switch is activated,
	 * at which the encoder would be reset.
	 */
	public void calibrate ()
	{
		// set elevator motor to calibration output
		IO.elevatorMotor.set (-CALIBRATION_OUTPUT);
		
		// wait for switch to be touched
		while (!isAtTop ())
			Controller.sleep (1);
		
		// switch touched, reset and configure sensor here
		IO.elevatorEncoder.setMaxPeriod (0.25);
		IO.elevatorEncoder.setMinRate (0.25);
		IO.elevatorEncoder.reset ();
		
		// configure PID
		IO.elevatorPID.setAbsoluteTolerance (SETPOINT_TOLERANCE);
		IO.elevatorPID.setInputRange (0, ELEVATOR_HEIGHT);
		IO.elevatorPID.setOutputRange (-1.0, 1.0);
		IO.elevatorPID.reset ();
		
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
	 * @return distance in inches
	 */
	public double getDistanceFromTop ()
	{
		return IO.elevatorEncoder.getDistance ();
	}
	
	/**
	 * Gets the distance between the elevator and minimum height.
	 * @return distance in inches
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
		if (!calibrated)
			throw new IllegalStateException ("not calibrated");
	
		/*
		double current = IO.elevatorEncoder.getDistance ();
		if (x >= current)
			IO.elevatorPID.setPID (KP_down, KI_down, KD_down);
		else
			IO.elevatorPID.setPID (KP_up, KI_up, KD_up);
		*/
		
		IO.elevatorPID.setSetpoint (x);
		IO.elevatorPID.reset ();
		IO.elevatorPID.enable ();
		
		System.out.println ("Set DFT to " + x);
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
	 * Checks whether the elevator is at the previously set position.
	 * @return true if the elevator is at the previously set position.
	 */
	public boolean isAtSetpoint ()
	{
		return IO.elevatorPID.onTarget ();
	}
	
	/**
	 * Waits until the elevator is at the previously set position.
	 */
	public void waitUntilAtSetpoint ()
	{
		while (!isAtSetpoint ())
			Controller.sleep (1);
	}
	
	/**
	 * Moves the specified tray to the frisbee pick-up.
	 * @param n the tray to align to the pick-up (0 is bottom-most tray).
	 * @see #getDistanceFromBottom()
	 * @see #getDistanceFromTop()
	 */
	public void moveTrayToInput (int n)
	{
		setDistanceFromTop (TRAY_INPUT_POSITIONS[n]);
	}
	
	/**
	 * Moves the specified tray to the shooter.
	 * @param n the tray to align to the pick-up (0 is bottom-most tray).
	 * @see #getDistanceFromBottom()
	 * @see #getDistanceFromTop()
	 */
	public void moveTrayToOutput (int n)
	{
		setDistanceFromTop (TRAY_OUTPUT_POSITIONS[n]);
	}
	
	/**
	 * Moves the lower-most empty tray to pick-up.
	 */
	public void moveEmptyTrayToInput ()
	{
		for (int i = 0; i < NUMBER_OF_TRAYS; i++)
		{
			if (!trayFilled[i])
			{
				moveTrayToInput (i);
				break;
			}
		}
	}
	
	/**
	 * Moves the upper-most filled tray to shooter.
	 */
	public void moveFilledTrayToOutput ()
	{
		for (int i = NUMBER_OF_TRAYS - 1; i >= 0; i--)
		{
			if (trayFilled[i])
			{
				moveTrayToOutput (i);
				break;
			}
		}
	}
	
	/**
	 * Marks a tray as filled.
	 * @param n tray to mark as filled.
	 */
	public void fillTray (int n)
	{
		trayFilled[n] = true;
	}
	
	/**
	 * Marks a tray as empty.
	 * @param n tray to mark as empty.
	 */
	public void emptyTray (int n)
	{
		trayFilled[n] = false;
	}
	
	/**
	 * Marks all trays as filled.
	 */
	public void fillAllTrays ()
	{
		for (int i = 0; i < NUMBER_OF_TRAYS; i++)
			trayFilled[i] = true;
	}
	
	/**
	 * Marks all trays as empty.
	 */
	public void emptyAllTrays ()
	{
		for (int i = 0; i < NUMBER_OF_TRAYS; i++)
			trayFilled[i] = false;
	}
	
	/**
	 * Checks whether all trays are empty.
	 * @return true if all trays are empty and
	 *         none of the trays are filled.
	 */
	public boolean traysAreEmpty ()
	{
		for (int i = 0; i < NUMBER_OF_TRAYS; i++)
			if (trayFilled[i])
				return false;
		return true;
	}
	
	/**
	 * Checks whether all trays are filled.
	 * @return true if all trays are filled and
	 *         there are no empty trays.
	 */
	public boolean traysAreFilled ()
	{
		for (int i = 0; i < NUMBER_OF_TRAYS; i++)
			if (!trayFilled[i])
				return false;
		return true;
	}
	
	/**
	 * Directly sets the elevator motor output. Note that PID is disabled
	 * when this function is called.
	 * @param x the output to which the motor should be set.
	 */
	public void setMotorOutput (double x)
	{
		IO.elevatorPID.disable ();
		IO.elevatorMotor.set (x);
	}
}