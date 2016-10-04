package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.hivegame.game.build.item.Item;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/17/2016.
 */
public class PreviewItem extends Item {

    public PreviewItem(){
        super("Preview", "Hold to see a preview.", "TOOL", false, 2, Item.c_itemInfiniteUses, 1);
        setImageName("frame");
    }

    @Override
    public boolean onLeftClick(Entity user){
        if(!Build.g_animationMode)
            return false;
        return true;
    }

    @Override
    public boolean onRightClick(Entity user){
        if(!Build.g_animationMode)
            return false;
        return true;
    }
}
