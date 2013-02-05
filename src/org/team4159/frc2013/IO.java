/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team4159.frc2013;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.HiTechnicColorSensor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.Talon;
import org.team4159.support.CombinedMotor;

/**
 *
 * @author gavin
 */
public class IO
{
	// To avoid changes, make sure all declarations are "public static final".
	
	/****************************************
	 * JOYSTICKS                            *
	 ****************************************/
	public static final Joystick joystick1 = new Joystick (1);
	public static final Joystick joystick2 = new Joystick (2);
	
	/****************************************
	 * MOTORS                               *
	 ****************************************/
	public static final Talon driveMotorLeftFront = new Talon (1);
	public static final Talon driveMotorLeftRear = new Talon (2);
	public static final Talon driveMotorRightFront = new Talon (3);
	public static final Talon driveMotorRightRear = new Talon (4);
	
	public static final CombinedMotor driveMotorLeftCombined =
		new CombinedMotor (driveMotorLeftFront, driveMotorLeftRear);
	public static final CombinedMotor driveMotorRightCombined =
		new CombinedMotor (driveMotorRightFront, driveMotorRightRear);
	
	/****************************************
	 * RELAYS                               *
	 ****************************************/
	public static final Relay pneumaticPump = new Relay (1, Relay.Direction.kForward);
	
	/****************************************
	 * SOLENOIDS                            *
	 ****************************************/
	public static final DoubleSolenoid driveGearboxLeft = new DoubleSolenoid (1, 2);
	public static final DoubleSolenoid driveGearboxRight = new DoubleSolenoid (3, 4);
	
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
	
	public static final DigitalInput pressureSwitch = new DigitalInput (5);
	
	public static final HiTechnicColorSensor frisbeeColorSensor =
		new HiTechnicColorSensor (SensorBase.getDefaultDigitalModule ());
	
	// private constructor to prevent instantiation
	private IO () {}
}
