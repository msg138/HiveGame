package com.hivegame.game.particle;

import com.retro.engine.defaultcomponent.ComponentImage;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;

/**
 * Created by Michael on 8/17/2016.
 */
public class ParticleFactory {


    public static Entity makeParticle(ComponentPosition pos, Vector3 dir, float speed, int lifetime, String partName){
        Entity e = new Entity();
        e.add(new ParticleComponent(pos, dir, speed, lifetime, partName));
        e.add(pos);
        ComponentImage img = new ComponentImage(partName);
        img.getImage().set3dSpace(true);
        img.getImage().setLookAtCamera(true);
        e.add(img);

        return e;
    }
}
