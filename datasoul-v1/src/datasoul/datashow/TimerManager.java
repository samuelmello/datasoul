/* 
 * Copyright 2005-2008 Samuel Mello & Eduardo Schnell
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; version 2 of the License.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 */

/*
 * TimerManager.java
 *
 * Created on March 22, 2006, 11:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datasoul.datashow;

import datasoul.config.ConfigObj;
import datasoul.render.ContentManager;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author root
 */
public class TimerManager extends Thread {
    
    private static TimerManager instance;
    
    public static final int TIMER_DIRECTION_OFF = 0;
    public static final int TIMER_DIRECTION_FORWARD = 1;
    public static final int TIMER_DIRECTION_BACKWARD = 2;
    public static final int TIMER_DIRECTION_BACKWARD_OVERDUE = 3;
    
    private SimpleDateFormat sdformat;
    private SimpleDateFormat sdformatTimer;
    private volatile boolean stopThread;
    private int timerDirection;
    private long timerForwardCounter;
    private long timerBackwardCounter;
    private long timerBackwardTotal;
    
    /** Creates a new instance of TimerManager */
    private TimerManager() {

        int format = ConfigObj.getInstance().getClockModeIdx();
        
        sdformatTimer = new SimpleDateFormat("H:mm:ss");
        
        // Select the format
        switch(format){
            
            case ConfigObj.CLOCKMODE_24_SEC: 
                sdformat = new SimpleDateFormat("k:mm:ss");
                break;

            case ConfigObj.CLOCKMODE_24_NOSEC: 
                sdformat = new SimpleDateFormat("k:mm");
                break;
                
            case ConfigObj.CLOCKMODE_12_SEC: 
                sdformat = new SimpleDateFormat("h:mm:ss a");
                break;
                
            case ConfigObj.CLOCKMODE_12_NOSEC: 
                sdformat = new SimpleDateFormat("h:mm");
                break;
                
            default:    
                sdformat = new SimpleDateFormat("k:mm:ss");
                
        }
        
        stopThread = false;
        
        start();
        
    }
    
    public void setTimerOff(){
        this.timerDirection = TIMER_DIRECTION_OFF;
    }
    
    public void setTimerForward(long initial){
        this.timerForwardCounter = initial;
        this.timerDirection = TIMER_DIRECTION_FORWARD;
    }

    private void setTimerBackwardOverdue(long initial){
        this.timerForwardCounter = initial;
        this.timerDirection = TIMER_DIRECTION_BACKWARD_OVERDUE;
    }
    
    public void setTimerBackward(long initial, long total){
        this.timerBackwardCounter = initial;
        if (total > 0){
            this.timerBackwardTotal = total;
        }else{
            this.timerBackwardTotal = 1;
        }
        this.timerDirection = TIMER_DIRECTION_BACKWARD;
    }
    
    static public TimerManager getInstance(){
        if (instance == null){
            instance = new TimerManager();
        }
        return instance;
    }
    
    private String formatTimer(long t){
        long ts = t / 1000;
        
        long sec = ts % 60;
        long min = ts / 60;
        
        StringBuffer ret = new StringBuffer();
        
        if (min < 10){
            ret.append("0");
        }
        ret.append(min);
        ret.append(":");
        if (sec < 10){
            ret.append("0");
        }
        ret.append(sec);
        
        return ret.toString();
        
        
    }
    
    public void run(){
        
        long t1, t2;
        ContentManager cm = ContentManager.getInstance();
        
        while (!stopThread){
            try{
                t1 = System.currentTimeMillis();

                // update the clock
                cm.setClockLive( sdformat.format(new Date()) );


                // update the timer
                if (timerDirection == TIMER_DIRECTION_FORWARD || timerDirection == TIMER_DIRECTION_BACKWARD_OVERDUE){
                    timerForwardCounter += 1000;
                    cm.setTimerLive( formatTimer( timerForwardCounter ));
                    if (timerDirection == TIMER_DIRECTION_BACKWARD_OVERDUE){
                        cm.setShowTimer( true );
                    }else{
                        cm.setShowTimer( false );
                    }
                }else if (timerDirection == TIMER_DIRECTION_BACKWARD){
                    timerBackwardCounter += 1000;
                    if ( timerBackwardTotal - timerBackwardCounter <= 0 ){
                        setTimerBackwardOverdue(0);
                    }else{
                        cm.setTimerLive( formatTimer(timerBackwardTotal - timerBackwardCounter ));
                    }
                    cm.setTimerProgress( (float) timerBackwardCounter / (float) timerBackwardTotal );
                    cm.setShowTimer(true);

                }else if (timerDirection == TIMER_DIRECTION_OFF){
                    cm.setTimerLive("");
                    cm.setShowTimer(false);
                }

                // ok, changes done
                cm.slideChangeFromTimerManager();

                // go sleep!
                t2 = System.currentTimeMillis();
                if ( (1000 - (t2 - t1)) > 1 ){
                    try {
                        Thread.sleep( 1000 - (t2 - t1) );
                    } catch (InterruptedException ex) {
                        //ignore
                    }
                }
            }catch(Exception e){
                // Do not stop thread if any error happens
            }
        }
        
    }
    
    
}
