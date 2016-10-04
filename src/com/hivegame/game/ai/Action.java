package com.hivegame.game.ai;

import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.world.World;

/**
 * Created by Michael on 8/31/2016.
 */
public interface Action {

    public void update(LivingComponent lc, World w);

    public boolean isComplete(LivingComponent lc, World w);

    public String getActionName();

    // Get the progress as a percentage.
    public float getActionProgress(LivingComponent lc, World w);

}
