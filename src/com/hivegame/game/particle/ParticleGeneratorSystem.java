package com.hivegame.game.particle;

import com.retro.engine.Messaging.RetroMessage;
import com.retro.engine.entity.Entity;
import com.retro.engine.system.System;

/**
 * Created by Michael on 8/17/2016.
 */
public class ParticleGeneratorSystem extends System {

    public ParticleGeneratorSystem(){
        super(ParticleGeneratorComponent.class, false);
    }

    public void handleMessage(RetroMessage msg){

    }

    @Override
    public void processEntity(Entity e){
        ParticleGeneratorComponent generator = (ParticleGeneratorComponent)e.get(ParticleGeneratorComponent.class);
        // Simple as that.
        generator.generate();
    }
}
