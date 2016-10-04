package com.hivegame.game.build;

import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.retro.engine.entity.Entity;
import com.hivegame.game.build.player.Player;

/**
 * Created by Michael on 8/9/2016.
 */
public class CommandSetColor extends Command {

    public CommandSetColor(){
        super("setcolor", new String[]{ "sc", "color" }, 4);
    }

    public boolean onCommand(Entity sender, String[] args){

        BuildInventoryComponent inv = Player.getBuildInventory(sender);
        inv.getBuildColor().setRed(Integer.parseInt(args[1]));
        inv.getBuildColor().setGreen(Integer.parseInt(args[2]));
        inv.getBuildColor().setBlue(Integer.parseInt(args[3]));
        if(args.length > 4)
            inv.getBuildColor().setAlpha(Integer.parseInt(args[4]));

        ChatBox.getInstance().addMessage("Color set!", sender);

        return true;
    }
}
