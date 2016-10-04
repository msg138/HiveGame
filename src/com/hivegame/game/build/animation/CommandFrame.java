package com.hivegame.game.build.animation;

import com.hivegame.game.build.Build;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/17/2016.
 */
public class CommandFrame extends Command {

    public CommandFrame(){
        super("frame", 2);
    }

    @Override
    public boolean onCommand(Entity sender, String[] args){
        switch(args[1]){
            case "add":
                Build.getAnimation().addFrame();
                ChatBox.getInstance().addMessage("Frame added, new total: "+ Build.getAnimation().getMaxFrames(), sender);
                break;
            case "set":
                if(args.length > 2){
                    Build.getAnimation().setCurrentFrame(Integer.parseInt(args[2]));
                    ChatBox.getInstance().addMessage("Current frame set: " + args[2], sender);
                }
                break;
            case "setmax":
                if(args.length > 2){
                    Build.getAnimation().setMaxFrames(Integer.parseInt(args[2]));
                    ChatBox.getInstance().addMessage("Max frames set: " + args[2], sender);
                }
                break;
            case "interpolate":
                Build.getAnimation().setInterpolated(!Build.getAnimation().isInterpolated());
                ChatBox.getInstance().addMessage("Interpolation: " + Build.getAnimation().isInterpolated(), sender);
                break;
            case "remove":
                if(args.length > 2){
                    Build.getAnimation().removeFrame(Integer.parseInt(args[2]));
                    ChatBox.getInstance().addMessage("Frame " + args[2] + " removed.", sender);
                }
                break;
            case "clone":
                if(args.length > 2) {
                    Build.getAnimation().cloneFrame(Integer.parseInt(args[2]));
                    ChatBox.getInstance().addMessage("Frame " + args[2] + " cloned to the end.", sender);
                }else {
                    Build.getAnimation().cloneFrame(Build.getAnimation().getCurrentFrame());
                    ChatBox.getInstance().addMessage("Frame " + Build.getAnimation().getCurrentFrame() + " cloned to the end.", sender);
                }
                break;
            default:
                return false;
        }
        return true;
    }
}
