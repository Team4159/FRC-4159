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
import edu.wpi.first.wpilibj.Victor;
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
	
	// 1 is in use by compressor
	public static final DigitalInput elevatorTop = new DigitalInput (2);
	public static final DigitalInput pickupFirst = new DigitalInput (3);
	public static final DigitalInput pickupSecond = new DigitalInput (4);
	
	public static final Encoder driveEncoderLeft = new Encoder (5, 6);
	public static final Encoder driveEncoderRight = new Encoder (7, 8);
	static {
		final double distancePerPulse = 0.0001; // FIX ME!!!!!! (inches per pulse)
		driveEncoderLeft.setDistancePerPulse (distancePerPulse);
		driveEncoderRight.setDistancePerPulse (distancePerPulse);
		driveEncoderLeft.setPIDSourceParameter (PIDSourceParameter.kDistance);
		driveEncoderRight.setPIDSourceParameter (PIDSourceParameter.kDistance);
		driveEncoderLeft.start ();
		driveEncoderRight.start ();
	}
	
	public static final Encoder elevatorEncoder = new Encoder (9, 10);
	static {
		final double distancePerPulse = 0.0001; // FIX ME!!!!!! (inches per pulse)
		elevatorEncoder.setDistancePerPulse (distancePerPulse);
		elevatorEncoder.setPIDSourceParameter (PIDSourceParameter.kDistance);
		elevatorEncoder.start ();
	}
	
	public static final Encoder shooterEncoder = new Encoder (11, 12);
	static {
		final double distancePerPulse = 0.0001; // FIX ME!!!!!! (revolutions per pulse)
		shooterEncoder.setDistancePerPulse (distancePerPulse);
		shooterEncoder.setPIDSourceParameter (PIDSourceParameter.kRate);
		shooterEncoder.start ();
	}
	
	public static final ADXL345_I2C accelerometer =
		new ADXL345_I2C (SensorBase.getDefaultDigitalModule (), DataFormat_Range.k16G);
	
	/****************************************
	 * MOTORS                               *
	 ****************************************/
	public static final Talon driveMotorLeft = new Talon (1);
	public static final Talon driveMotorRight = new Talon (2);
	public static final Talon shooterMotor = new Talon (3);
	public static final Victor elevatorMotor = new Victor (4);
	public static final Victor innerPickupMotor = new Victor (5);
	public static final Victor outerPickupMotor = new Victor (6);
	
	public static final PIDController drivePIDLeft =
		new PIDController (Drive.KP, Drive.KI, Drive.KD, driveEncoderLeft, driveMotorLeft);
	public static final PIDController drivePIDRight = 
		new PIDController (Drive.KP, Drive.KI, Drive.KD, driveEncoderRight, driveMotorRight);
	public static final PIDController elevatorPID = 
		new PIDController (Elevator.KP, Elevator.KI, Elevator.KD, elevatorEncoder, elevatorMotor);
	
	static {
		elevatorPID.setAbsoluteTolerance (Elevator.SETPOINT_TOLERANCE);
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
