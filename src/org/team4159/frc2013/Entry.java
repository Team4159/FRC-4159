import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;

public class Entry extends RobotBase
{
	private static final int
		MODE_UNKNOWN = 0,
		MODE_DISABLED = 1,
		MODE_AUTONOMOUS = 2,
		MODE_OPERATOR = 3,
		MODE_TEST = 4;
	
	private int getMode ()
	{
		DriverStation ds = DriverStation.getInstance ();
		if (ds.isDisabled ())
			return MODE_DISABLED;
		if (ds.isTest ())
			return MODE_TEST;
		if (ds.isAutonomous())
			return MODE_AUTONOMOUS;
		else
			return MODE_OPERATOR;
	}
	
	private Controller getController (int ct)
	{
		switch (ct)
		{
			case MODE_DISABLED:
				return new DisabledController ();
			case MODE_AUTONOMOUS:
				return new AutonomousController ();
			case MODE_OPERATOR:
				return new OperatorController ();
			case MODE_TEST:
				return new Testcontroller ();
			default:
				throw new IllegalArgumentException ("unknown controller code");
		}
	}
	
	public void startCompetition ()
	{
		int current_mode = MODE_UNKNOWN;
		Controller controller = null;
		
		while (true)
		{
			int next_mode = getMode ();
			if (current_mode != next_mode)
				controller = createController (current_mode = next_mode);
		}
	}
}