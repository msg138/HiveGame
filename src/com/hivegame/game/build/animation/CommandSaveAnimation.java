package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.hivegame.game.chat.Command;
import com.retro.engine.entity.Entity;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.build.player.NoClipComponent;
import com.hivegame.game.voxel.UtilVoxel;

/**
 * Created by Michael on 8/9/2016.
 */
public class CommandSaveAnimation extends Command {

    public CommandSaveAnimation(){
        super("savea", 0);
    }

    public boolean onCommand(Entity sender, String[] args){
        if(!sender.has(NoClipComponent.class))
            return false;

        String fname ="modelVox.spa";
        if(args.length > 1 && !args[1].endsWith(".spa"))
            fname = args[1] + ".spa";
        else if(args.length > 1)
            fname = args[1];

        ChatBox.getInstance().addMessage("Saving animation...", sender);

        UtilVoxel.saveAnimationToFile(fname, Build.getAnimation(), Build.g_modelNames.toArray(new String[0]));

        ChatBox.getInstance().addMessage("Animation saved to '" + fname + "'", sender);

        return true;
    }
}
