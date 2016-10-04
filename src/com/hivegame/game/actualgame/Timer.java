package com.hivegame.game.actualgame;

import com.hivegame.game.settings.TimerSettings;
import com.retro.engine.event.EventHandler;
import com.retro.engine.util.time.UtilTime;

import java.awt.event.KeyEvent;

/**
 * Created by Michael on 9/11/2016.
 */
public class Timer {

    private static Timer m_instance;

    private long m_startTime;
    private long m_lastTime;
    private float m_timePassed;

    private long m_currentTime;

    private float m_currentTimerSpeed;

    private Timer(){
        m_startTime = UtilTime.mill();
        m_lastTime = m_startTime;

        m_currentTimerSpeed = TimerSettings.g_speedSlow;

        m_currentTime = 0;

        m_instance = this;
    }

    public static Timer getInstance(){
        if(m_instance == null)
            new Timer();
        return m_instance;
    }

    public float getCurrentTimerSpeed() {
        return m_currentTimerSpeed;
    }

    public void setCurrentTimerSpeed(float m_currentTimerSpeed) {
        this.m_currentTimerSpeed = m_currentTimerSpeed;
    }

    public long getCurrentTime(){
        return m_currentTime;
    }

    public void tick(){


        /**
         * Handle time changing with the 0-4 keys on number row
         * */
        if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_0)){
            EventHandler.getInstance().releaseKey(KeyEvent.VK_0);
            Timer.getInstance().setCurrentTimerSpeed(TimerSettings.g_speedPause);
        }
        if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_1)){
            EventHandler.getInstance().releaseKey(KeyEvent.VK_1);
            Timer.getInstance().setCurrentTimerSpeed(TimerSettings.g_speedSlow);
        }
        if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_2)){
            EventHandler.getInstance().releaseKey(KeyEvent.VK_2);
            Timer.getInstance().setCurrentTimerSpeed(TimerSettings.g_speedNormal);
        }
        if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_3)){
            EventHandler.getInstance().releaseKey(KeyEvent.VK_3);
            Timer.getInstance().setCurrentTimerSpeed(TimerSettings.g_speedFast);
        }
        if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_4)){
            EventHandler.getInstance().releaseKey(KeyEvent.VK_4);
            Timer.getInstance().setCurrentTimerSpeed(TimerSettings.g_speedFaster);
        }
        /**
         * End
         */

        long nTime = UtilTime.mill();

        if(m_currentTimerSpeed == TimerSettings.g_speedPause)
        {
            m_lastTime = nTime;
            return;
        }

        m_timePassed += nTime - m_lastTime;
        m_lastTime = nTime;

        while(m_timePassed > (float)TimerSettings.g_minuteTime / m_currentTimerSpeed)
        {
            m_currentTime += 1;
            m_timePassed -= (float)TimerSettings.g_minuteTime / m_currentTimerSpeed;
        }
    }

    public float getSpeed(){
        return m_currentTimerSpeed / (float)TimerSettings.g_speedNormal;
    }

    public long getTimeSinceStart(){
        return UtilTime.mill() - m_startTime;
    }

    public long getTimeSinceLast(){
        return UtilTime.mill() - m_lastTime;
    }

    public void kill(){
        m_instance = null;
    }
}
