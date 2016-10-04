package com.hivegame.game.chat.command;

import com.hivegame.game.chat.Command;
import com.retro.engine.Framework;
import com.retro.engine.entity.Entity;
import com.retro.engine.scene.SceneType;
import com.hivegame.game.build.player.NoClipComponent;

/**
 * Created by Michael on 8/9/2016.
 */
public class CommandBuildMode extends Command {

    public CommandBuildMode(){
        super("buildmode", 0);
    }

    public boolean onCommand(Entity sender, String[] args){

        if(!sender.has(NoClipComponent.class))
            Framework.getInstance().loadScene(SceneType.SCENE001);
        else
            Framework.getInstance().loadScene(SceneType.SCENE000);

        return true;
    }
}
