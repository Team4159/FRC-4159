/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team4159.frc2013;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.HiTechnicColorSensor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.ADXL345_I2C.DataFormat_Range;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder.PIDSourceParameter;
import org.team4159.frc2013.subsystems.Drive;
import org.team4159.frc2013.subsystems.Elevator;
import org.team4159.support.CombinedMotor;

/**
 *
 * @author gavin
 */
public class IO
{
	// To avoid changes, make sure all declarations are "public static final".
	static {
		System.out.println ("Initializing IO ...");
	}
	
	/****************************************
	 * JOYSTICKS                            *
	 ****************************************/
	public static final Joystick joystick1 = new Joystick (1);
	public static final Joystick joystick2 = new Joystick (2);
	
	/****************************************
	 * SENSORS                              *
	 ****************************************/
	public static final Encoder driveEncoderLeft = new Encoder (1, 2);
	public static final Encoder driveEncoderRight = new Encoder (3, 4);
	static {
		final double distancePerPulse = 0.0001; // FIX ME!!!!!!
		driveEncoderLeft.setDistancePerPulse (distancePerPulse);
		driveEncoderRight.setDistancePerPulse (distancePerPulse);
		driveEncoderLeft.start ();
		driveEncoderRight.start ();
	}
	
	public static final Encoder elevatorEncoder = new Encoder (5, 6);
	static {
		final double distancePerPulse = 0.0001; // FIX ME!!!!!!
		elevatorEncoder.setDistancePerPulse (distancePerPulse);
		elevatorEncoder.setPIDSourceParameter (PIDSourceParameter.kDistance);
		elevatorEncoder.start ();
	}
	
	public static final DigitalInput elevatorTop = new DigitalInput (11);
	
/*
	public static final HiTechnicColorSensor frisbeeColorSensor =
		new HiTechnicColorSensor (SensorBase.getDefaultDigitalModule ());
*/
	
	public static final ADXL345_I2C accelerometer =
		new ADXL345_I2C (SensorBase.getDefaultDigitalModule (), DataFormat_Range.k16G);
	
	/****************************************
	 * MOTORS                               *
	 ****************************************/
	public static final Talon driveMotorLeft = new Talon (1);
	public static final Talon driveMotorRight = new Talon (2);
	public static final Talon elevatorMotor = new Talon (3);
	
	public static final PIDController drivePIDLeft =
		new PIDController (Drive.KP, Drive.KI, Drive.KD, driveEncoderLeft, driveMotorLeft);
	public static final PIDController drivePIDRight = 
		new PIDController (Drive.KP, Drive.KI, Drive.KD, driveEncoderRight, driveMotorRight);
	public static final PIDController elevatorPID = 
		new PIDController (Elevator.KP, Elevator.KI, Elevator.KD, elevatorEncoder, elevatorMotor);
	
	static {
		// FIXME: tolerance in WHAT UNITS and HOW MUCH
		elevatorPID.setAbsoluteTolerance (1);
	}
	
	/****************************************
	 * RELAYS                               *
	 ****************************************/
	public static final Compressor pneumaticPump = new Compressor (5, 1);
	static {
		pneumaticPump.start ();
	}
	
	/****************************************
	 * SOLENOIDS                            *
	 ****************************************/
	public static final DoubleSolenoid driveGearbox = new DoubleSolenoid (1, 2);
	static {
		final Value val = Value.kForward;
		driveGearbox.set (val);
	}
	
	// private constructor to prevent instantiation
	private IO () {}
	
	static {
		System.out.println ("IO ready.");
	}
}
