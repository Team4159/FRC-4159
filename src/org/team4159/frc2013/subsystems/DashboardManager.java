package org.team4159.frc2013.subsystems;

import org.team4159.frc2013.controllers.AutonomousController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.ADXL345_I2C;
import org.team4159.frc2013.IO;

public class DashboardManager
{
	public static final DashboardManager instance = new DashboardManager ();
	
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
}
