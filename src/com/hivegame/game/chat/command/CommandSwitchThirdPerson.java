package com.hivegame.game.chat.command;

import com.hivegame.game.chat.Command;
import com.retro.engine.camera.Camera;
import com.retro.engine.camera.ThirdPersonCamera;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/31/2016.
 */
public class CommandSwitchThirdPerson extends Command {

    public CommandSwitchThirdPerson(){
        super("switch3", 0);
    }

    public boolean onCommand(Entity sender, String[] args){

        ((ThirdPersonCamera) Camera.getInstance()).setMoveTarget(!((ThirdPersonCamera) Camera.getInstance()).getMoveTarget());

        return true;
    }
}
