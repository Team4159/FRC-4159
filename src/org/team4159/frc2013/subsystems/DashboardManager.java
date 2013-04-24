package org.team4159.frc2013.subsystems;

import org.team4159.frc2013.controllers.AutonomousController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DashboardManager
{
	public static final DashboardManager instance = new DashboardManager ();
	
	private SendableChooser autonomousMode;
	
	private DashboardManager ()
	{
		SendableChooser autonomousMode = new SendableChooser ();
		autonomousMode.addObject ("Left", new Integer (AutonomousController.MODE_LEFT));
		autonomousMode.addObject ("Right", new Integer (AutonomousController.MODE_RIGHT));
		SmartDashboard.putData ("Autonomous Mode", autonomousMode);
	}
	
	public int getAutonomousMode ()
	{
		return ((Integer) autonomousMode.getSelected ()).intValue ();
	}
}
