package com.hivegame.game.build.animation;

import com.retro.engine.defaultcomponent.ComponentScale;
import com.retro.engine.entity.Entity;
import com.retro.engine.model.AnimatedRawModel;
import com.hivegame.game.build.Build;
import com.hivegame.game.build.item.Item;

/**
 * Created by Michael on 8/17/2016.
 */
public class ScaleItem extends Item{

    private String m_axis;

    public ScaleItem(String axis){
        super("Scale" + axis, "Scale along " + axis + " axis", "TOOL", false, 2, Item.c_itemInfiniteUses, 1);
        setImageName("scale" + axis.toLowerCase());

        m_axis = axis.toLowerCase();
    }

    @Override
    public boolean onLeftClick(Entity user){
        if(!Build.g_animationMode)
            return false;

        AnimatedRawModel.AnimationFrame f = Build.getAnimation().getFrame(Build.getAnimation().getCurrentFrame());
        ComponentScale scale = f.getScale(Build.g_currentModel);
        switch(m_axis){
            case "x":
                scale.setScaleX(scale.getScaleX() + Build.g_scaleInterval);
                break;
            case "y":
                scale.setScaleY(scale.getScaleY() + Build.g_scaleInterval);
                break;
            case "z":
                scale.setScaleZ(scale.getScaleZ() + Build.g_scaleInterval);
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public boolean onRightClick(Entity user){
        if(!Build.g_animationMode)
            return false;

        AnimatedRawModel.AnimationFrame f = Build.getAnimation().getFrame(Build.getAnimation().getCurrentFrame());
        ComponentScale scale = f.getScale(Build.g_currentModel);
        switch(m_axis){
            case "x":
                scale.setScaleX(scale.getScaleX() - Build.g_scaleInterval);
                break;
            case "y":
                scale.setScaleY(scale.getScaleY() - Build.g_scaleInterval);
                break;
            case "z":
                scale.setScaleZ(scale.getScaleZ() - Build.g_scaleInterval);
                break;
            default:
                return false;
        }

        return true;
    }
}
