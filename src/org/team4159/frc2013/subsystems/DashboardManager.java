package org.team4159.frc2013.subsystems;

import org.team4159.frc2013.controllers.AutonomousController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
<<<<<<< HEAD
=======
import edu.wpi.first.wpilibj.ADXL345_I2C;
import org.team4159.frc2013.IO;
>>>>>>> origin/SVR

public class DashboardManager
{
	public static final DashboardManager instance = new DashboardManager ();
	
<<<<<<< HEAD
	private SendableChooser autonomousMode;
	
	private DashboardManager ()
	{
		SendableChooser autonomousMode = new SendableChooser ();
		autonomousMode.addObject ("Left", new Integer (AutonomousController.MODE_LEFT));
		autonomousMode.addObject ("Right", new Integer (AutonomousController.MODE_RIGHT));
		autonomousMode.addObject ("Static", new Integer (AutonomousController.MODE_STATIC));
		SmartDashboard.putData ("Autonomous Mode", autonomousMode);
	}
	
	public int getAutonomousMode ()
	{
		return ((Integer) autonomousMode.getSelected ()).intValue ();
	}
=======
	private DashboardManager ()
	{
	}
        
        public void update ()
        {
            SmartDashboard.putBoolean ("Angler Up", Shooter.instance.anglerIsUp ());
            SmartDashboard.putNumber ("Shooter Target", IO.shooterPID.getSetpoint());
            SmartDashboard.putNumber ("Shooter Actual", Shooter.instance.getSpeed ());
            SmartDashboard.putBoolean ("Shoot!", IO.shooterPID.onTarget());
            SmartDashboard.putNumber ("Shooter Power", IO.shooterMotor.get());
        }
>>>>>>> origin/SVR
}
