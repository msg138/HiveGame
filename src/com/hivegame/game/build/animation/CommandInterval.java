package com.hivegame.game.build.animation;

import com.retro.engine.entity.Entity;
import com.hivegame.game.build.Build;
import com.hivegame.game.chat.Command;

/**
 * Created by Michael on 8/17/2016.
 */
public class CommandInterval extends Command {

    public CommandInterval(){
        super("interval", new String[]{"int", "ival"}, 3);
    }

    @Override
    public boolean onCommand(Entity sender, String[] args){
        switch(args[1]){
            case "set":
                if(args.length < 4)
                    return false;
                switch(args[2]){
                    case "degree":
                        Build.g_degreeInterval = Float.parseFloat(args[3]);
                        break;
                    case "position":
                        Build.g_positionInterval = Float.parseFloat(args[3]);
                        break;
                    case "animation":
                        Build.g_animateInterval = Float.parseFloat(args[3]);
                        break;
                    case "scale":
                        Build.g_scaleInterval = Float.parseFloat(args[3]);
                        break;
                }
                break;
            default:
                return false;
        }
        return true;
    }
}
