package org.team4159.frc2013;

import org.team4159.support.Controller;
import edu.wpi.first.wpilibj.RobotBase;
import org.team4159.frc2013.controllers.AutonomousController;
import org.team4159.frc2013.controllers.DisabledController;
import org.team4159.frc2013.controllers.OperatorController;
import org.team4159.frc2013.controllers.TestController;
import org.team4159.frc2013.subsystems.DashboardManager;
import org.team4159.support.ModeEnumerator;

public class Entry extends RobotBase
{
	public static final int TICK_INTERVAL_MS = 20;
	
	private Logger logger = new Logger ();
	
	public Entry ()
	{
		System.out.println ("Entry initializing ...");
		
		// wake up the IO class
		IO.class.equals (null);
		
		// initialize dashboard manager
		DashboardManager.class.equals (null);
		
		System.out.println ("Entry instantiated.");
	}
	
	private Controller createController (int ct)
	{
		switch (ct)
		{
			case ModeEnumerator.DISABLED:
				return new DisabledController ();
			case ModeEnumerator.AUTONOMOUS:
				return new AutonomousController ();
			case ModeEnumerator.OPERATOR:
				return new OperatorController ();
			case ModeEnumerator.TEST:
				return new TestController ();
			default:
				throw new IllegalArgumentException ("unknown controller state");
		}
	}
	
	public void startCompetition ()
	{
		System.out.println ("Entry.startCompetition() called.");
		
		while (true)
		{
			int mode = ModeEnumerator.getMode();
			Controller controller = createController (mode);
			
			System.out.println ("Controller set to " + controller.getClass ().getName ());
			if (mode != ModeEnumerator.DISABLED)
				logger.start ();
			else
				logger.stop ();
			
			controller.start ();
			while (ModeEnumerator.getMode () == mode)
				try {
					Thread.sleep (TICK_INTERVAL_MS);
				} catch (InterruptedException e) {}
			controller.stop ();
		}
	}
}