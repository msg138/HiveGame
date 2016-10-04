package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.retro.engine.entity.Entity;
import com.retro.engine.model.AnimatedRawModel;
import com.hivegame.game.build.item.Item;

/**
 * Created by Michael on 8/17/2016.
 */
public class RotateItem extends Item{

    private String m_axis;

    public RotateItem(String axis){
        super("Rotate" + axis, "Rotate along " + axis + " axis", "TOOL", false, 2, Item.c_itemInfiniteUses, 1);
        setImageName("rotate" + axis.toLowerCase());

        m_axis = axis.toLowerCase();
    }

    @Override
    public boolean onLeftClick(Entity user){
        if(!Build.g_animationMode)
            return false;

        AnimatedRawModel.AnimationFrame f = Build.getAnimation().getFrame(Build.getAnimation().getCurrentFrame());
        switch(m_axis){
            case "x":
                f.getRotation(Build.g_currentModel).setRotationX(f.getRotation(Build.g_currentModel).getRotationX() + (float)Math.toRadians(Build.g_degreeInterval));
                break;
            case "y":
                f.getRotation(Build.g_currentModel).setRotationY(f.getRotation(Build.g_currentModel).getRotationY() + (float)Math.toRadians(Build.g_degreeInterval));
                break;
            case "z":
                f.getRotation(Build.g_currentModel).setRotationZ(f.getRotation(Build.g_currentModel).getRotationZ() + (float)Math.toRadians(Build.g_degreeInterval));
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
        switch(m_axis){
            case "x":
                f.getRotation(Build.g_currentModel).setRotationX(f.getRotation(Build.g_currentModel).getRotationX() - (float)Math.toRadians(Build.g_degreeInterval));
                break;
            case "y":
                f.getRotation(Build.g_currentModel).setRotationY(f.getRotation(Build.g_currentModel).getRotationY() - (float)Math.toRadians(Build.g_degreeInterval));
                break;
            case "z":
                f.getRotation(Build.g_currentModel).setRotationZ(f.getRotation(Build.g_currentModel).getRotationZ() - (float)Math.toRadians(Build.g_degreeInterval));
                break;
            default:
                return false;
        }
        return true;
    }
}
