package com.hivegame.game.build;

/**
 * Created by Michael on 8/9/2016.
 */

import com.hivegame.game.build.player.Player;
import com.retro.engine.Messaging.RetroMessage;
import com.retro.engine.debug.Debug;
import com.retro.engine.entity.Entity;
import com.retro.engine.system.System;

public class BuildInventoryUpdateSystem extends System {
    public static final String c_messageItemRemove = "RemoveItem";

    public BuildInventoryUpdateSystem(){
        super(BuildInventoryComponent.class);
    }

    @Override
    public void handleMessage(RetroMessage msg){
        if(msg.getRawData().contains(c_messageItemRemove)){
            BuildInventoryComponent inv = Player.getBuildInventory(msg.getEntity());
            inv.removeDeadItems();
            Debug.out("Removed dead items.");
        }
    }

    @Override
    public void processEntity(Entity e){
    }
}
