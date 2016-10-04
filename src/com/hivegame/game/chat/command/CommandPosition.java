package com.hivegame.game.chat.command;

import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.retro.engine.camera.Camera;
import com.retro.engine.camera.ThirdPersonCamera;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/30/2016.
 */
public class CommandPosition extends Command {

    public CommandPosition(){
        super("position", new String[]{"pos"}, 0);
    }

    public boolean onCommand(Entity sender, String[] args){
        ChatBox.getInstance().addMessage(Camera.getInstance().getPosition().toVector3().toString() + "  Y:" + Camera.getInstance().getYaw() +" P:" + Camera.getInstance().getPitch(), null);
        if(Camera.getInstance() instanceof ThirdPersonCamera){
            ChatBox.getInstance().addMessage(((ThirdPersonCamera)Camera.getInstance()).getLookYaw() + " " + ((ThirdPersonCamera)Camera.getInstance()).getLookPitch(), null);
        }
        return true;
    }
}
