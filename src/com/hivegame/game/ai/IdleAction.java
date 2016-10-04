package com.hivegame.game.ai;

import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.world.World;
import com.retro.engine.debug.Debug;

/**
 * Created by Michael on 8/31/2016.
 */
public class IdleAction implements Action {

    private int m_jumpFrame =0 ;
    private int m_maxJumpFrame = 300;

    public IdleAction(){
    }

    public void update(LivingComponent lc, World w) {
        /*lc.setCurrentVelX(0);
        lc.setCurrentVelZ(0);*/

        m_jumpFrame ++;
        if (m_jumpFrame > m_maxJumpFrame)
        {
            m_jumpFrame = 0;
        }
    }
    public boolean isComplete(LivingComponent lc, World w){
        return false;// Always idle until action is given.
    }

    public String getActionName(){
        return "Idle. Waiting...";
    }

    public float getActionProgress(LivingComponent lc, World w){
        return 1f;// Always complete.
    }
}
