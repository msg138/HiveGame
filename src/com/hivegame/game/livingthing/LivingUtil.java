package com.hivegame.game.livingthing;

import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/31/2016.
 */
public class LivingUtil {

    public static LivingComponent getLivingComponent(Entity e){
        if(e.has(LivingComponent.class))
            return (LivingComponent)e.get(LivingComponent.class);
        return null;
    }

    public static ComponentPosition getPosition(Entity e){
        if(e.has(ComponentPosition.class))
            return (ComponentPosition)e.get(ComponentPosition.class);
        return null;
    }
}
