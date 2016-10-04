package com.hivegame.game.ai;

import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.livingthing.LivingUtil;
import com.hivegame.game.world.World;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;

/**
 * Created by Michael on 9/26/2016.
 */
public class ChaseAction implements Action {

    private Entity m_toChase;

    public ChaseAction(Entity toChase){
        m_toChase = toChase;
    }

    @Override
    public void update(LivingComponent lc, World w) {
        Vector3 ppos = lc.getPosition().toVector3();

        Vector3 delta = ppos.clone().subtractFrom(LivingUtil.getLivingComponent(m_toChase).getPosition().toVector3()).normalize();

        float angle = (float)Math.atan2(delta.getX(), delta.getZ()) - (float)Math.toRadians(90);

        lc.setCurrentVelX((float)Math.cos(angle)*lc.getMovementSpeed());
        lc.setCurrentVelZ((float)Math.sin(angle)*-lc.getMovementSpeed());
    }

    @Override
    public boolean isComplete(LivingComponent lc, World w) {
        return m_toChase == null || LivingUtil.getLivingComponent(m_toChase) == null || LivingUtil.getLivingComponent(m_toChase).isDead();
    }

    @Override
    public String getActionName() {
        return "Chasing";
    }

    @Override
    public float getActionProgress(LivingComponent lc, World w) {
        return 0;
    }
}
