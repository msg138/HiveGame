package com.hivegame.game.chat.command;

import com.hivegame.game.chat.Command;
import com.hivegame.game.build.player.NoClipComponent;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/23/2016.
 */
public class CommandNoClip extends Command {

    public CommandNoClip(){
        super("noclip", 0);
    }

    public boolean onCommand(Entity sender, String[] args){

        if(sender.has(NoClipComponent.class))
            sender.remove(NoClipComponent.class);
        else
            sender.add(new NoClipComponent());

        return true;
    }
}
