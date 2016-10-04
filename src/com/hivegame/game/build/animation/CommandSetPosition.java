package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.retro.engine.camera.Camera;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;
import com.retro.engine.model.AnimatedRawModel;

/**
 * Created by Michael on 9/25/2016.
 */
public class CommandSetPosition extends Command {

    public CommandSetPosition(){
        super("setposition", new String[]{"setpos"}, 0);
    }

    public boolean onCommand(Entity sender, String[] args){

        // Set the position of the currently selected object to the camera position.

        AnimatedRawModel.AnimationFrame f = Build.getAnimation().getFrame(Build.getAnimation().getCurrentFrame());
        ComponentPosition camPos = Camera.getInstance().getPosition();
        f.getPosition(Build.g_currentModel).setX(camPos.getX());
        f.getPosition(Build.g_currentModel).setY(camPos.getY());
        f.getPosition(Build.g_currentModel).setZ(camPos.getZ());

        ChatBox.getInstance().addMessage("Set current model position to camera position.", null);

        return true;
    }
}
