package com.hivegame.game.build;

import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.retro.engine.camera.Camera;
import com.retro.engine.camera.FirstPersonCamera;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/10/2016.
 */
public class CommandMoveSpeed extends Command {

    public CommandMoveSpeed(){
        super("movespeed", new String[]{ "mv", "spd"}, 2);
    }

    public boolean onCommand(Entity sender, String[] args){

        ((FirstPersonCamera)Camera.getInstance()).setMoveVelocity(Float.parseFloat(args[1]));
        ChatBox.getInstance().addMessage("Speed set.", sender);

        return true;
    }
}
