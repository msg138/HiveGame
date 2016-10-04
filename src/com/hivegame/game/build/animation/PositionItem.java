package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.hivegame.game.build.item.Item;
import com.retro.engine.entity.Entity;
import com.retro.engine.model.AnimatedRawModel;

/**
 * Created by Michael on 8/17/2016.
 */
public class PositionItem extends Item {

    private String m_axis;

    public PositionItem(String axis){
        super("Position" + axis, "Move along " + axis + " axis", "TOOL", false, 2, Item.c_itemInfiniteUses, 1);
        setImageName("position" + axis.toLowerCase());

        m_axis = axis.toLowerCase();
    }

    @Override
    public boolean onLeftClick(Entity user){
        if(!Build.g_animationMode)
            return false;

        AnimatedRawModel.AnimationFrame f = Build.getAnimation().getFrame(Build.getAnimation().getCurrentFrame());
        switch(m_axis){
            case "x":
                f.getPosition(Build.g_currentModel).modX(Build.g_positionInterval);
                break;
            case "y":
                f.getPosition(Build.g_currentModel).modY(Build.g_positionInterval);
                break;
            case "z":
                f.getPosition(Build.g_currentModel).modZ(Build.g_positionInterval);
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
                f.getPosition(Build.g_currentModel).modX(-Build.g_positionInterval);
                break;
            case "y":
                f.getPosition(Build.g_currentModel).modY(-Build.g_positionInterval);
                break;
            case "z":
                f.getPosition(Build.g_currentModel).modZ(-Build.g_positionInterval);
                break;
            default:
                return false;
        }

        return true;
    }
}
