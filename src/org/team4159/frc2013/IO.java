/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team4159.frc2013;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.HiTechnicColorSensor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.ADXL345_I2C.DataFormat_Range;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import org.team4159.frc2013.subsystems.Drive;
import org.team4159.frc2013.subsystems.Elevator;
import org.team4159.frc2013.subsystems.Shooter;
import org.team4159.support.ADXL345_Extended;
import org.team4159.support.CombinedMotor;
import org.team4159.support.DeadzoneCompensatedMotor;

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
        
        public final AxisCamera camera = AxisCamera.getInstance ("10.41.59.11");
	
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
		final double inchesPerPulse = 0.0001; // FIX ME!!!!!! (inches per pulse)
		driveEncoderLeft.setDistancePerPulse (inchesPerPulse);
		driveEncoderRight.setDistancePerPulse (inchesPerPulse);
		driveEncoderLeft.setPIDSourceParameter (Encoder.PIDSourceParameter.kDistance);
		driveEncoderRight.setPIDSourceParameter (Encoder.PIDSourceParameter.kDistance);
		driveEncoderLeft.start ();
		driveEncoderRight.start ();
	}
	
	public static final Encoder elevatorEncoder = new Encoder (9, 10);
	static {
		elevatorEncoder.setDistancePerPulse (1.0);
		elevatorEncoder.setPIDSourceParameter (Encoder.PIDSourceParameter.kDistance);
		elevatorEncoder.start ();
	}
	
	public static final Encoder shooterEncoder = new Encoder (11, 12);
	static {
                double pulsesPerRevolution = 180;
                double revolutionsPerPulse = 1 / pulsesPerRevolution;
		shooterEncoder.setDistancePerPulse (revolutionsPerPulse);
		shooterEncoder.setPIDSourceParameter (Encoder.PIDSourceParameter.kRate);
		shooterEncoder.start ();
	}
	
	public static final Gyro gyroscope =
		new Gyro (1);
	public static final ADXL345_Extended accelerometer =
		new ADXL345_Extended ();
	
	/****************************************
	 * MOTORS                               *
	 ****************************************/
	public static final Talon driveMotorLeft = new Talon (1);
	public static final Talon driveMotorRight = new Talon (2);
	public static final Talon shooterMotor = new Talon (3);
	public static final Victor elevatorRawMotor = new Victor (4);
	public static final DeadzoneCompensatedMotor elevatorMotor =
		new DeadzoneCompensatedMotor (elevatorRawMotor, Elevator.DEADZONE_LOWER, Elevator.DEADZONE_UPPER);
	public static final Victor innerPickupMotor = new Victor (5);
	public static final Victor outerPickupMotor = new Victor (6);
	
	public static final PIDController drivePIDLeft =
		new PIDController (Drive.KP, Drive.KI, Drive.KD, driveEncoderLeft, driveMotorLeft);
	public static final PIDController drivePIDRight = 
		new PIDController (Drive.KP, Drive.KI, Drive.KD, driveEncoderRight, driveMotorRight);
	public static final PIDController shooterPID =
		new PIDController (Shooter.KP, Shooter.KI, Shooter.KD, shooterEncoder, shooterMotor);
	
	/****************************************
	 * RELAYS                               *
	 ****************************************/
	public static final Compressor pneumaticPump = new Compressor (1, 1);
	static {
		pneumaticPump.start ();
	}
	
	/****************************************
	 * SOLENOIDS                            *
	 ****************************************/
	public static final DoubleSolenoid driveGearbox = new DoubleSolenoid (1, 2);
	static {
		driveGearbox.set (Value.kForward);
	}
	
	public static final DoubleSolenoid shooterPiston = new DoubleSolenoid (3, 4);
	static {
		shooterPiston.set (Value.kReverse);
	}
	
	public static final DoubleSolenoid shooterAngler = new DoubleSolenoid (5, 6);
	static {
		shooterAngler.set (Value.kForward);
	}
        
	public static final Solenoid cameraLED = new Solenoid(8);
        static {
            cameraLED.set (true);
        }
        
	// private constructor to prevent instantiation
	private IO () {}
	
	static {
		System.out.println ("IO ready.");
	}
}
