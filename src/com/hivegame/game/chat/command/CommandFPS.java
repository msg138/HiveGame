package com.hivegame.game.chat.command;

import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.retro.engine.Framework;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/21/2016.
 */
public class CommandFPS extends Command {

    public CommandFPS(){
        super("fps", 0);
    }

    public boolean onCommand(Entity sender, String[] args){

        ChatBox.getInstance().addMessage("Render: " + Framework.getInstance().getCurrentRenderFPS() + "  Logic: " + Framework.getInstance().getCurrentFPS(), sender);

        return true;
    }
}
