package com.hivegame.game.build;

import com.hivegame.game.chat.Command;
import com.retro.engine.entity.Entity;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.build.player.NoClipComponent;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.voxel.UtilVoxel;
import com.hivegame.game.world.World;

/**
 * Created by Michael on 8/9/2016.
 */
public class CommandSave extends Command {

    public CommandSave(){
        super("save", 0);
    }

    public boolean onCommand(Entity sender, String[] args){
        if(!sender.has(NoClipComponent.class))
            return false;

        String fname ="modelVox.spv";
        if(args.length > 1 && !args[1].endsWith(".spv"))
            fname = args[1] + ".spv";
        else if(args.length > 1)
            fname = args[1];

        ChatBox.getInstance().addMessage("Saving model...", sender);
        World w = Player.getWorld(sender);

        UtilVoxel.saveToFile(fname, w.getAllVoxels(Build.g_keepLower, true), !Build.g_keepLower);

        ChatBox.getInstance().addMessage("Model saved to '" + fname + "'", sender);

        return true;
    }
}
