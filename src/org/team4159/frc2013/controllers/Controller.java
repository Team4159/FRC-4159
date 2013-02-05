/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team4159.frc2013.controllers;

import edu.wpi.first.wpilibj.DriverStation;
import org.team4159.frc2013.Entry;

/**
 *
 * @author Team4159
 */
public abstract class Controller
{
	private final long timingInterval;
	private long timingStart;
	private long timingAccumulator;
	
	protected final DriverStation driverStation = DriverStation.getInstance ();
	
	protected Controller ()
	{
		this (Entry.TICK_INTERVAL_MS);
	}
	
	protected Controller (long interval)
	{
		timingInterval = interval;
		resetTiming ();
	}
	
	protected final void beginTiming ()
	{
		timingStart = System.currentTimeMillis ();
	}

	protected final void endTiming ()
	{
		long now = System.currentTimeMillis ();
		long elapsed = now - timingStart;

		timingAccumulator = timingAccumulator - elapsed + timingInterval;
		if (timingAccumulator > 0)
		{
			try {
				Thread.sleep (timingAccumulator);
			} catch (InterruptedException ex) {}
			timingAccumulator = 0;
		}
	}
	
	protected final void resetTiming ()
	{
		timingStart = 0;
		timingAccumulator = 0;
	}
	
	public abstract boolean active ();
	
	public void tick ()
	{
		/* OVERRIDE ME */
	}
	
	public void run ()
	{
		while (active ())
		{
			beginTiming ();
			tick ();
			endTiming ();
		}
	}
}
