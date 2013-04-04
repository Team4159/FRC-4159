/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team4159.support;

import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SensorBase;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Waylin
 */
public class FastEncoder extends SensorBase implements PIDSource
{
    private static final int I2C_ADDRESS = 0x0E;
    
    private long previous = 0;
    private final long samples[];
    private int index = 0;
    
    private final Timer timer;
    private final long period;
    
    private class Task extends TimerTask
    {
        private final DigitalModule dm = DigitalModule.getInstance (getDefaultDigitalModule ());
        private final byte[] result = new byte[2];
        
        public void run ()
        {
            // get the sample
            transact ();
            
            // calculate sample and account for overflow
            long sample = (previous & 0xFFFFFFFFFFFF0000L) | (result[1] << 8) | result[0];
            if (sample < previous) // overflow
                sample += 0x10000;
            
            // update state
            synchronized (samples)
            {
                previous = sample;
                samples[index] = sample;
                index = (index + 1) % samples.length;
            }
        }
        
        public void transact ()
        {
            highSDA ();
            highSCL ();
            
            lowSDA ();
            lowSCL ();
            
            sendByte ((0x0e << 0) | 0x00);
            System.out.println ("ack1: " + readACK ());
            
            sendByte (0x00);
            readACK ();
            
            highSDA ();
            highSCL ();
            
            lowSDA ();
            lowSCL ();
            
            sendByte ((0x0e << 1) | 0x01);
            System.out.println ("ack2: " + readACK ());
            
            readByte ();
            writeACK ();
            
            readByte ();
            writeNACK ();
            
            highSCL ();
            highSDA ();
        }
    }
    
    public FastEncoder (int samples, long period)
    {
        this.samples = new long[samples];
        
        timer = new Timer ();
        timer.scheduleAtFixedRate(new Task (), 0, this.period = period);
    }
    
    public void free ()
    {
        timer.cancel ();
    }
    
    public long getPosition ()
    {
        synchronized (samples)
        {
            return samples[index];
        }
    }
    
    public double getVelocity ()
    {
        long sampleA, sampleB;
        
        synchronized (samples)
        {
            int lastIndex = (index == 0) ? samples.length - 1 : index - 1;
            sampleA = samples[index];
            sampleB = samples[lastIndex];
        }
        
        double d = sampleB - sampleA;
        double t = (samples.length - 1) * period / 1000.0;
        return d / t;
    }
    
    public double pidGet ()
    {
        return getVelocity ();
    }
}
