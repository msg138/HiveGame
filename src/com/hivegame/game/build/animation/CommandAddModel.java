package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.hivegame.game.voxel.UtilVoxel;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/17/2016.
 */
public class CommandAddModel extends Command {

    public CommandAddModel(){
        super("addmodel", new String[]{"addmod"}, 2);
    }

    @Override
    public boolean onCommand(Entity sender, String[] args){
        if(!Build.g_animationMode) {
            ChatBox.getInstance().addMessage("Not in animation mode", sender);
            return false;
        }

        int index = -1;
        if(args.length > 2)
            index = Integer.parseInt(args[2]);

        if(index == -1) {
            Build.getAnimation().addModel(UtilVoxel.getVoxelModel(args[1]), args[1]);
            Build.g_modelNames.add(args[1]);
            ChatBox.getInstance().addMessage("Model '"+ args[1] +"' loaded as index: " + (Build.getAnimation().getModelCount()-1), sender);
        }else
        {
            Build.getAnimation().setModel(index, UtilVoxel.getVoxelModel(args[1]));
            Build.g_modelNames.set(index, args[1]);
            ChatBox.getInstance().addMessage("Model '"+ args[1] +"' loaded as index: " + (index), sender);
        }
        return true;
    }
}
