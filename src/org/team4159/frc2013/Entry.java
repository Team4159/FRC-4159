package org.team4159.frc2013;

import org.team4159.support.Controller;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import org.team4159.frc2013.controllers.AutonomousController;
import org.team4159.frc2013.controllers.DisabledController;
import org.team4159.frc2013.controllers.OperatorController;
import org.team4159.frc2013.controllers.TestController;
import org.team4159.support.ModeEnumerator;

public class Entry extends RobotBase
{
	public static final int TICK_INTERVAL_MS = 20;
	
	public Entry ()
	{
		System.out.println ("Entry instantiated.");
		
		// wake up the IO class
		IO.class.getName ();
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
		
		int current_mode = ModeEnumerator.UNKNOWN;
		Controller controller = null;
		
		while (true)
		{
			int next_mode = ModeEnumerator.getMode();
			if (current_mode != next_mode)
				controller = createController (current_mode = next_mode);
			
			System.out.println ("Controller to " + controller.getClass ().getName ());
			controller.run ();
		}
	}
}