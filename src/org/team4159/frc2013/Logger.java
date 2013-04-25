package org.team4159.frc2013;

import org.team4159.frc2013.subsystems.Shooter;
import org.team4159.support.DataCollector;
import edu.wpi.first.wpilibj.DriverStation;
import java.util.Timer;
import java.util.TimerTask;

public class Logger
{
	private Timer timer;
	private DataCollector dataCollector;
	
	public Logger ()
	{
		dataCollector = new DataCollector ("/data2013/data");
		dataCollector.addColumn ("time", "Time (ms)");
		dataCollector.addColumn ("voltage", "Battery Voltage");
		dataCollector.addColumn ("shooterSpeed", "Shooter Speed");
		dataCollector.addColumn ("shooterTarget", "Shooter Target Speed");
		dataCollector.addColumn ("shooterPower", "Shooter Power");
		dataCollector.addColumn ("pistonState", "Piston State");
	}
	
	public void start ()
	{
		if (timer != null)
			return;
		timer = new Timer ();
		timer.scheduleAtFixedRate (new Task (), 0, 50);
	}
	
	public void stop ()
	{
		if (timer == null)
			return;
		timer.cancel ();
		timer = null;
	}
	
	public void sample ()
	{
		dataCollector.setRowValue ("time", System.currentTimeMillis ());
		dataCollector.setRowValue ("voltage", DriverStation.getInstance ().getBatteryVoltage ());
		dataCollector.setRowValue ("shooterSpeed", IO.shooterEncoder.getRate ());
		dataCollector.setRowValue ("shooterTarget", IO.shooterPID.getSetpoint ());
		dataCollector.setRowValue ("shooterPower", IO.shooterMotor.get ());
		dataCollector.setRowValue ("pistonState", Shooter.instance.pistonIsRetracted () ? 1 : 0);
		dataCollector.writeRow ();
	}
	
	private class Task extends TimerTask
	{
		public void run ()
		{
			sample ();
		}
	}
}
