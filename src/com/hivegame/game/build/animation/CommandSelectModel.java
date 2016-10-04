package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/17/2016.
 */
public class CommandSelectModel extends Command {

    public CommandSelectModel(){
        super("selectmodel", new String[]{"select"}, 2);
    }

    @Override
    public boolean onCommand(Entity sender, String[] args){

        Build.g_currentModel = Integer.parseInt(args[1]);
        ChatBox.getInstance().addMessage("Current Model: " + Build.g_currentModel, sender);
        return true;
    }
}
