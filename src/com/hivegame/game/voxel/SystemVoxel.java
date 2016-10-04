package com.hivegame.game.voxel;

import com.jogamp.opengl.GL2;
import com.retro.engine.Framework;
import com.retro.engine.Messaging.RetroMessage;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.defaultcomponent.ComponentRotation;
import com.retro.engine.entity.Entity;
import com.retro.engine.model.Matrix;
import com.retro.engine.system.System;

/**
 * Created by Michael on 7/30/2016.
 */
public class SystemVoxel extends System {

    public SystemVoxel(){
        super(ComponentVoxelModel.class, true);
    }


    public void handleMessage(RetroMessage msg){

    }

    float f = -2f;

    @Override
    public void processEntity(Entity e, GL2 gl){

        ComponentVoxelModel vox = ((ComponentVoxelModel)e.get(ComponentVoxelModel.class));
        //gl.glTranslatef(vox.getOffsetX(), 0.0f, vox.getOffsetZ());
        Matrix m = Framework.getModelMatrix();
        m.pushMatrix();
        m.translatef(-vox.getOffsetX()/2, -vox.getOffsetY()/2, -vox.getOffsetZ()/2);
        //m.translatef(vox.getOffsetX(), vox.getOffsetY(), vox.getOffsetZ());
        if(e.has(ComponentPosition.class))
            ((ComponentPosition)e.get(ComponentPosition.class)).applyTranslation(m);
        if(e.has(ComponentRotation.class)) {
            ((ComponentRotation) e.get(ComponentRotation.class)).applyRotation(m);
        }
        m.translatef(vox.getOffsetX()*1.5f, vox.getOffsetY()*1.5f, vox.getOffsetZ()*1.5f);
        // Move more if we have to.
        m.scalef(vox.getScale() * vox.getScaleX(), vox.getScale() * vox.getScaleY(), vox.getScale() * vox.getScaleZ());
        m.activateM(gl);

        vox.getModel().render(gl);

        m.popMatrixM(gl);
    }
}
