/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team4159.frc2013;

import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author gavin
 */
public class Periodic
{
	public static void maintainPressure ()
	{
		// might have reversed direction
		IO.pneumaticPump.set (IO.pressureSwitch.get () ?
			Relay.Value.kForward : Relay.Value.kOff);
	}
	
	public static void tick ()
	{
		maintainPressure ();
	}
}
