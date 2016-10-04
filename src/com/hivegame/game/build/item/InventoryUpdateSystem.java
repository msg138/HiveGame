package com.hivegame.game.build.item;

import com.retro.engine.Framework;
import com.retro.engine.Messaging.RetroMessage;
import com.retro.engine.component.ComponentType;
import com.retro.engine.debug.Debug;
import com.retro.engine.entity.Entity;
import com.retro.engine.system.System;
import com.hivegame.game.build.player.Player;

/**
 * Created by Michael on 8/4/2016.
 */
public class InventoryUpdateSystem extends System{

    public static final String c_messageItemRemove = "RemoveItem";

    public InventoryUpdateSystem(){
        super(InventoryComponent.class);
    }

    @Override
    public void handleMessage(RetroMessage msg){
        if(msg.getRawData().contains(c_messageItemRemove)){
            InventoryComponent inv = Player.getInventory(msg.getEntity());
            inv.removeDeadItems();
            Debug.out("Removed dead items.");
        }
    }

    @Override
    public void processEntity(Entity e){
    }
}
