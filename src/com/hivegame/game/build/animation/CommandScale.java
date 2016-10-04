package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.retro.engine.defaultcomponent.ComponentScale;
import com.retro.engine.entity.Entity;
import com.retro.engine.model.AnimatedRawModel;

/**
 * Created by Michael on 8/17/2016.
 */
public class CommandScale extends Command {

    public CommandScale(){
        super("scale", 2);
    }

    @Override
    public boolean onCommand(Entity sender, String[] args){
        AnimatedRawModel.AnimationFrame f = Build.getAnimation().getFrame(Build.getAnimation().getCurrentFrame());
        ComponentScale scale = f.getScale(Build.g_currentModel);
        scale.setScale(Float.parseFloat(args[1]));
        ChatBox.getInstance().addMessage("Set scale of model "+ Build.g_currentModel +" to " + args[1], sender);
        return true;
    }
}
