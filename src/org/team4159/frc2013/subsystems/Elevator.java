package org.team4159.frc2013.subsystems;

import java.util.Timer;
import java.util.TimerTask;
import org.team4159.frc2013.IO;
import org.team4159.support.Controller;
import org.team4159.support.Subsystem;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.image.CurveOptions;

/**
 * The {@link Elevator} class implements all code required to
 * calibrate the elevator to the top and precisely move it to arbitrary
 * positions along the range of movement.
 */
public final class Elevator implements Subsystem
{
	/**
	 * Elevator height.
	 */
	public static final double ELEVATOR_HEIGHT = 1300;
	
	/**
	 * The output level at which the motor should be set
	 * during calibration. 
	 */
	public static final double CALIBRATION_OUTPUT = 0.22;
	
	/**
	 * The height of each tray relative to the top of the range of movement
	 * at the output. The first element should represent the
	 * bottom-most tray.
	 */
	public static final double[] TRAY_OUTPUT_POSITIONS = { 13.25, 81.5, 152. };
	
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
	public static final double SETPOINT_TOLERANCE = 3.0;
	
	/**
	 * Lower motor output of deadzone.
	 */
	public static final double DEADZONE_LOWER = -0.38;
	
	/**
	 * Upper motor output of deadzone.
	 */
	public static final double DEADZONE_UPPER = 0.18;
	
	public static final double INTEGRAL_COEFFICIENT = 0.0204;
	public static final double SLOWDOWN_COEFFICIENT = 0.70;
	public static final double SLOWDOWN_DISTANCE_UP = 500;
	public static final double SLOWDOWN_DISTANCE_DOWN = 500;
	public static final double MAXIMUM_OUTPUT_UP = 0.9;
	public static final double MAXIMUM_OUTPUT_DOWN = 0.7;
	
	/**
	 * Singleton instance of this class.
	 */
	public static final Elevator instance = new Elevator ();
	
	private boolean calibrated = false;
	private boolean[] trayFilled = new boolean[NUMBER_OF_TRAYS];
	
	private Timer setpointTaskTimer;
	private boolean setpointEnabled = false;
	private double setpointFromTop;
	
	// prevent instantiation, must access through #instance
	private Elevator ()
	{
		setpointTaskTimer = new Timer ();
		setpointTaskTimer.scheduleAtFixedRate (new Task (), 0, 25);
	}

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
		
		// stop elevator motor
		IO.elevatorMotor.set (0);
		
		// switch touched, reset and configure sensor here
		IO.elevatorEncoder.setMaxPeriod (0.25);
		IO.elevatorEncoder.setMinRate (0.25);
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
		{
			System.out.println ("warning: elevator not calibrated!");
			return;
		}
		
		setpointFromTop = x;
		synchronized (this) { setpointEnabled = true; }
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
        
        public void moveDown (double x)
        {
            if (this.setpointEnabled)
                setpointFromTop += x;
        }
        
        public void moveUp (double x)
        {
            if (this.setpointEnabled)
                setpointFromTop -= x;
        }
	
	/**
	 * Checks whether the elevator is at the previously set position.
	 * @return true if the elevator is at the previously set position.
	 */
	public boolean isAtSetpoint ()
	{
		return Math.abs (IO.elevatorEncoder.getDistance () - setpointFromTop) <= SETPOINT_TOLERANCE;
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
		synchronized (this) { setpointEnabled = false; }
		IO.elevatorMotor.set (x);
	}
	
	public static final boolean USE_TASK = true;
	
	private class Task extends TimerTask
	{
		private boolean lastUp = false;
		private double accumulatorFromTop = 0;
		
		public void run ()
		{
			double currentFromTop = IO.elevatorEncoder.getDistance ();
			
			double offset = setpointFromTop - currentFromTop;
			if (Math.abs (offset) <= SETPOINT_TOLERANCE)
			{
				accumulatorFromTop = 0;
				set (0);
				return;
			}
			
			boolean currentUp = offset < 0;
			if (lastUp != currentUp)
				accumulatorFromTop = 0;
			lastUp = currentUp;
			
			try {
				
				double output;
				
				if (currentUp)
				{
					// going up
					if (-offset > SLOWDOWN_DISTANCE_UP)
						output = (-MAXIMUM_OUTPUT_UP);
					else
						output = (-MAXIMUM_OUTPUT_UP * MathUtils.pow (-offset / SLOWDOWN_DISTANCE_UP, SLOWDOWN_COEFFICIENT));
				}
				else
				{
					// going down
					if (offset > SLOWDOWN_DISTANCE_DOWN)
						output = (MAXIMUM_OUTPUT_DOWN);
					else
						output = (MAXIMUM_OUTPUT_DOWN * MathUtils.pow (offset / SLOWDOWN_DISTANCE_DOWN, SLOWDOWN_COEFFICIENT));
				}
				
				double add = accumulatorFromTop;
				accumulatorFromTop += output * INTEGRAL_COEFFICIENT;
				output += add;
				set (output);
				
			} finally {
				lastUp = currentUp;
			}
		}
		
		private void set (double x)
		{
			accumulatorFromTop += INTEGRAL_COEFFICIENT * x;
			synchronized (Elevator.this)
			{
				if (setpointEnabled)
				{
					IO.elevatorMotor.set (x);
					System.out.println ("motor to " + x);
				}
			}
		}
	}
}