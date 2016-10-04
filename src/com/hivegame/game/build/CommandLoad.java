package com.hivegame.game.build;

import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.chat.Command;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.voxel.UtilVoxel;
import com.hivegame.game.world.World;
import com.retro.engine.debug.Debug;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;
import com.hivegame.game.voxel.Voxel;

import java.util.List;

/**
 * Created by Michael on 8/9/2016.
 */
public class CommandLoad extends Command {

    public CommandLoad(){
        super("load", 2);
    }

    public boolean onCommand(Entity user, String[] args){

        if(args.length < 2)
            return false;

        Build.g_keepLower = false;
        Build.g_animationMode = false;

        if(!args[1].endsWith(".spv"))
            args[1] = args[1] + ".spv";

        boolean generateFlat = true;
        World w = Player.getWorld(user);
        Vector3 loc = w.getMiddleChunkVoxelFlat();
        List<Voxel> voxels = UtilVoxel.loadFromFile(args[1], (int)loc.getX(), 0, (int)loc.getZ());

        for(Voxel v : voxels){
            if(v.getPosition().getY() < 0){
                Build.g_keepLower = true;
                generateFlat = false;
                break;
            }
        }

        if(generateFlat)
            w.generateFlat();
        w.generateSingleVoxel(!generateFlat);
        for(Voxel v : voxels){
            w.addVoxel(v.getPosition(), v.getColor());
            Debug.out("Load: " + v.getPosition().toString());
        }
        ChatBox.getInstance().addMessage("Successfully loaded.", user);

        return true;
    }

}
