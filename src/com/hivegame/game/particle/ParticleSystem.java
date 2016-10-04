package com.hivegame.game.particle;

import com.jogamp.opengl.GL2;
import com.retro.engine.Framework;
import com.retro.engine.Messaging.RetroMessage;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;
import com.retro.engine.system.System;
import com.retro.engine.util.vector.Vector3;

/**
 * Created by Michael on 8/17/2016.
 */
public class ParticleSystem extends System {

    public ParticleSystem(){
        super(ParticleComponent.class, true);
    }

    public void handleMessage(RetroMessage msg){

    }

    @Override
    public void processEntity(Entity e, GL2 gl){

        ParticleComponent part = (ParticleComponent)e.get(ParticleComponent.class);

        part.live();
        if(part.isDead())
        {
            Framework.getInstance().getEntityStorage().removeEntity(e, true);
            return;
        }

        Vector3 dir = part.getDirection();
        ComponentPosition pos = part.getPosition();

        pos.setX(pos.getX() + dir.getX());
        pos.setY(pos.getY() + dir.getY());
        pos.setZ(pos.getZ() + dir.getZ());
    }
}
