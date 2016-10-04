package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.hivegame.game.build.player.ActivePlayerComponent;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.hivegame.game.chat.command.CommandTeleport;
import com.retro.engine.camera.Camera;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;
import com.retro.engine.model.AnimatedRawModel;
import com.retro.engine.util.vector.Vector3;

/**
 * Created by Michael on 9/27/2016.
 */
public class CommandSetOrigin extends Command {

    public CommandSetOrigin(){
        super("setorigin", 0);
    }

    public boolean onCommand(Entity sender, String[] args){

        if(sender.has(ActivePlayerComponent.class)){
            // Get the distance between current cam position to origin, basically the coordinates
            Vector3 d = Camera.getInstance().getPosition().toVector3();

            // now go through and modify every model to its new position, for EVERY frame
            for(int i = 0;i<Build.getAnimation().getMaxFrames();i++)
            {
                AnimatedRawModel.AnimationFrame frame = Build.getAnimation().getFrame(i);

                for(int m = 0; m<Build.getAnimation().getModelCount();m++){
                    ComponentPosition pos = frame.getPosition(m);
                    pos.modX(-d.getX());
                    pos.modY(-d.getY());
                    pos.modZ(-d.getZ());
                }
            }
            ChatBox.getInstance().addMessage("Origin changed.", null);

            (new CommandTeleport()).onCommand(sender, new String[]{"0", "0", "0"});
        }else
            return false;

        return true;
    }
}
