package org.team4159.frc2013.controllers;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team4159.frc2013.IO;
import org.team4159.frc2013.subsystems.Elevator;
import org.team4159.frc2013.subsystems.Shooter;
import org.team4159.support.Controller;
import org.team4159.support.DriverStationLCD;
import org.team4159.support.ModeEnumerator;

public class AutonomousController extends Controller 
{
	public AutonomousController ()
	{
		super (ModeEnumerator.AUTONOMOUS);
	}
	public void run ()
	{
		// remember to use this.sleep() in case autonomous mode ends early
		// make sure that only sleep() blocks
            
                // set shooter speed
		/*OLD SHOOTER VALUES!!!!
                Shooter.instance.setMotorOutput(1.0);//shooting for center
                //Shooter.instance.setMotorOutput(.875)// shooting from back corner
                */
                //SVR VALUES!!! USE THESE WITH THE NEW SHOOTER!!
                /*double speed = driverStation.getAnalogIn(1)*100;
                if(speed < 5){
                    speed = 60;
                }*/
                Shooter.instance.setSpeed(60);
                //Shooter.instance.setSpeed(driverStation.getAnalogIn(1) * 100);
                
		DriverStationLCD.setLine (2, "Shooter done");
		// retract shooter so elevator can move
                Shooter.instance.raiseAngler();
		Shooter.instance.retract();
		// calibrate elevator
		//Elevator.instance.calibrate ();
                Controller.sleep(1000);
		//DriverStationLCD.setLine (1, "Elevator Alligning");
		// fire the frisbees
		for (int i = 0; i < 3; i++)
		{
                    DriverStationLCD.setLine (0, "Angler up? " + Shooter.instance.anglerIsUp ());
                DriverStationLCD.setLine (1, "Shooter TGT: " + IO.shooterPID.getSetpoint());
                DriverStationLCD.setLine (2, "Shooter RPS:" + Shooter.instance.getSpeed ());
                DriverStationLCD.setLine (3, "Shooter ASP:" + IO.shooterPID.onTarget());
                DriverStationLCD.setLine (4, "Shooter PWR:" + IO.shooterMotor.get());
			//Elevator.instance.moveTrayToOutput (i);
			
			//Elevator.instance.waitUntilAtSetpoint ();
			Shooter.instance.waitForShooter();//shooting from center, longer wait for spin up
                        Controller.sleep (500);
                        //Controller.sleep(1000); //shooting from corners, shorter wait for spin up
			Shooter.instance.extend ();
                        Shooter.instance.waitForPiston ();
                        DriverStationLCD.setLine (5, "shooting disc #: " + (i+1));
			Shooter.instance.retract ();
                        Shooter.instance.waitForPiston ();
		}
                Controller.sleep(1500);
                Shooter.instance.setSpeed(0);
                Shooter.instance.lowerAngler();
                
        }
	
	/*public void run ()
	{
		System.out.println ("calibrating ...");
		long start = System.currentTimeMillis ();
		
		Shooter.instance.retract ();
		Elevator.instance.calibrate ();
		
		long elapsed = System.currentTimeMillis () - start;
		System.out.println ("calibrated! (took " + elapsed + " ms)");
	}*/
}
