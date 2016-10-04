package com.hivegame.game.build;

import com.hivegame.game.chat.Command;
import com.retro.engine.camera.Camera;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.world.World;

/**
 * Created by Michael on 8/9/2016.
 */
public class CommandNew extends Command {

    public CommandNew(){
        super("new");
    }

    public boolean onCommand(Entity user, String[] args){

        Build.g_keepLower = false;
        Build.g_animationMode = false;

        boolean generateFlat = true;
        if(args.length >= 2){
            if(args[1].equalsIgnoreCase("blank")) {
                generateFlat = false;
                Build.g_keepLower = true;
            }
        }

        World w = Player.getWorld(user);
        if(generateFlat)
            w.generateFlat();
        w.generateSingleVoxel(!generateFlat);

        Vector3 v = w.getMiddleChunkVoxel();

        Camera.getInstance().getPosition().setX(v.getX());
        Camera.getInstance().getPosition().setY(v.getY() + w.getVoxelSize()*2f);
        Camera.getInstance().getPosition().setZ(v.getZ());

        return true;
    }
}
