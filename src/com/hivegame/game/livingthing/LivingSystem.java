package com.hivegame.game.livingthing;

import com.hivegame.game.actualgame.GameData;
import com.hivegame.game.build.player.ActivePlayerComponent;
import com.hivegame.game.build.player.NoClipComponent;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.gui.BannerDisplay;
import com.hivegame.game.settings.InterfaceInitializer;
import com.jogamp.opengl.GL2;
import com.retro.engine.Framework;
import com.retro.engine.Messaging.RetroMessage;
import com.retro.engine.camera.Camera;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;
import com.retro.engine.system.System;

/**
 * Created by Michael on 8/3/2016.
 */
public class LivingSystem extends System {

    public LivingSystem(){
        super(LivingComponent.class);
        renderSystem();
        logicSystem();
    }

    public void handleMessage(RetroMessage msg){
        String[] msgs = msg.getRawData().split(" ");
        switch(msgs[0]){
            case "DEATH":
                String data = "Entity '";
                data += LivingUtil.getLivingComponent(msg.getEntity()).getSpecies().getName();
                data += "' killed by : ";
                data += msgs[1];
                ChatBox.getInstance().addMessage(data, null);

                UnitHandler.getInstance().removeUnit(msg.getEntity());
                Framework.getInstance().getEntityStorage().removeEntity(msg.getEntity(), true);

                BannerDisplay.queueDisplay(LivingUtil.getLivingComponent(msg.getEntity()).getSpecies().getName() + " has been slain!");

                GameData.getInstance().addDeath(new GameData.GameDeathTimer(LivingUtil.getLivingComponent(msg.getEntity()).getTeamOwner()));
                break;
        }
    }

    @Override
    public void processEntity(Entity e){
        if(e.has(NoClipComponent.class))
            return;
        LivingComponent l = (LivingComponent)e.get(LivingComponent.class);

        if(l.isDead()){
            handleMessage(new RetroMessage(this.getID(), "DEATH NATURAL", e));
            return;
        }

        l.update(e);
        l.modStamina(LivingComponent.c_staminaRegenBase * l.getAgility());
    }

    @Override
    public void processEntity(Entity e, GL2 gl){
        LivingComponent l = (LivingComponent)e.get(LivingComponent.class);

        ComponentPosition pos = l.getPosition();

        InterfaceInitializer.g_healthbar.setScaleX(l.getHealth()/l.getMaxHealth());
        InterfaceInitializer.g_healthbar.render(gl, pos.getX(), pos.getY()+2, pos.getZ(), 0, (float)Math.toRadians(45), 0);

        if(l.isSelected())
        {
            InterfaceInitializer.g_diamond.render(gl, pos.getX(), pos.getY() + 3.5f, pos.getZ());
        }
    }
}
