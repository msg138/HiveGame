package com.hivegame.game.ai;

import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.world.World;
import com.retro.engine.debug.Debug;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.defaultcomponent.ComponentRotation;
import com.retro.engine.util.vector.Vector3;

/**
 * Created by Michael on 8/31/2016.
 */
public class MoveAction implements Action {

    private Vector3 m_startPos;
    private Vector3 m_endPos;

    private float m_threshold = .5f;

    private float m_moveThreshold = .25f;

    private boolean m_firstRun = true;

    public MoveAction(Vector3 sp, Vector3 ep){
        m_startPos = sp;
        m_endPos = ep;
    }

    public Vector3 getEndPos() {
        return m_endPos;
    }

    public Vector3 getStartPos() {
        return m_startPos;
    }

    public void update(LivingComponent lc, World w){
        if(lc.getPosition().getX() - m_moveThreshold > m_endPos.getX())
            lc.setCurrentVelX(-lc.getMovementSpeed());
        else if(lc.getPosition().getX() + m_moveThreshold < m_endPos.getX())
            lc.setCurrentVelX(lc.getMovementSpeed());
        else
            lc.setCurrentVelX(0);

        if(lc.getPosition().getZ() - m_moveThreshold > m_endPos.getZ() )
            lc.setCurrentVelZ(-lc.getMovementSpeed());
        else if(lc.getPosition().getZ() + m_moveThreshold < m_endPos.getZ())
            lc.setCurrentVelZ(lc.getMovementSpeed());
        else
            lc.setCurrentVelZ(0);


        m_firstRun = false;
    }
    public boolean isComplete(LivingComponent lc, World w){
        float dis = lc.getPosition().toVector3().distanceTo(m_endPos);
        if(dis < m_threshold || (lc.getCurrentVelX() == 0 && lc.getCurrentVelZ() == 0 && !m_firstRun))
        {
            return true;
        }
        return false;
    }

    public String getActionName(){
        return "Moving";
    }

    public float getActionProgress(LivingComponent lc, World w){
        return 1f - (lc.getPosition().toVector3().distanceTo(m_endPos) / m_startPos.distanceTo(m_endPos));
    }
}
