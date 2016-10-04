package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.retro.engine.camera.Camera;
import com.retro.engine.entity.Entity;
import com.hivegame.game.build.CommandNew;
import com.hivegame.game.build.player.Player;

/**
 * Created by Michael on 8/17/2016.
 */
public class CommandAnimation extends Command {

    public CommandAnimation(){
        super("animation", new String[]{"ani"}, 0);
    }

    @Override
    public boolean onCommand(Entity sender, String[] args){
        Build.g_animationMode = !Build.g_animationMode;
        if(Build.g_animationMode){
            Build.getAnimation().addFrame();// Start with 1 frame by default.
            Player.getWorld(sender).scrapWorld(true);

            Camera.getInstance().getPosition().setX(0);
            Camera.getInstance().getPosition().setY(0);
            Camera.getInstance().getPosition().setZ(0);
        }else {
            Build.setAnimation(null);
            (new CommandNew()).onCommand(sender, new String[]{});
        }
        ChatBox.getInstance().addMessage("Animation mode enabled: "+Build.g_animationMode, sender);

        return true;
    }
}
