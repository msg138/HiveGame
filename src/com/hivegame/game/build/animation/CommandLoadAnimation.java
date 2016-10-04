package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.hivegame.game.voxel.UtilVoxel;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/9/2016.
 */
public class CommandLoadAnimation extends Command {

    public CommandLoadAnimation(){
        super("loada", 0);
    }

    public boolean onCommand(Entity sender, String[] args){

        if(!args[1].endsWith(".spa"))
            args[1] = args[1] + ".spa";

        ChatBox.getInstance().addMessage("Loading animation "+args[1]+" ...", sender);

        Build.setAnimation(UtilVoxel.loadAnimationFromFile(args[1]));
        Build.g_modelNames = Build.getAnimation().getModelNames();

        ChatBox.getInstance().addMessage("Animation loaded!", sender);

        return true;
    }
}
