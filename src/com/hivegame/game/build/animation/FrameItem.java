package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.hivegame.game.build.item.Item;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/17/2016.
 */
public class FrameItem extends Item {

    public FrameItem(){
        super("Frame changer", "CHange frames.", "TOOL", false, 2, Item.c_itemInfiniteUses, 1);
        setImageName("frame");
    }

    @Override
    public boolean onLeftClick(Entity user){
        if(!Build.g_animationMode)
            return false;

        if(Build.getAnimation().getCurrentFrame() == Build.getAnimation().getMaxFrames())
            Build.getAnimation().setCurrentFrame(0);
        else
            Build.getAnimation().setCurrentFrame(Build.getAnimation().getCurrentFrame()+1);

        return true;
    }

    @Override
    public boolean onRightClick(Entity user){
        if(!Build.g_animationMode)
            return false;

        Build.getAnimation().setCurrentFrame(Build.getAnimation().getCurrentFrame()-1);
        if(Build.getAnimation().getCurrentFrame() < 0)
            Build.getAnimation().setCurrentFrame(Build.getAnimation().getMaxFrames()-1);

        return true;
    }
}
