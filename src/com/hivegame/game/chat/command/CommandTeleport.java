package com.hivegame.game.chat.command;

import com.hivegame.game.chat.Command;
import com.hivegame.game.build.player.ActivePlayerComponent;
import com.retro.engine.camera.Camera;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/8/2016.
 */
public class CommandTeleport extends Command {

    public CommandTeleport(){
        super("teleport",new String[]{"tp"}, 4);
    }

    public boolean onCommand(Entity sender,String[] args){
        if(args.length < 4)
            return false;
        if(!sender.has(ActivePlayerComponent.class))
            return false;
        ComponentPosition position = Camera.getInstance().getPosition();

        if(args[1].equalsIgnoreCase("p"))
            args[1] = Float.toString(position.getX());
        if(args[2].equalsIgnoreCase("p"))
            args[2] = Float.toString(position.getY());
        if(args[3].equalsIgnoreCase("p"))
            args[3] = Float.toString(position.getZ());

        try{
            position.setX(Float.parseFloat(args[1]));
            position.setY(Float.parseFloat(args[2]));
            position.setZ(Float.parseFloat(args[3]));
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
