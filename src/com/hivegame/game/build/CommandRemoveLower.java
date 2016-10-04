package com.hivegame.game.build;

import com.hivegame.game.chat.Command;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.world.World;
import com.retro.engine.entity.Entity;
import com.hivegame.game.voxel.Voxel;

import java.util.List;

/**
 * Created by Michael on 8/9/2016.
 */
public class CommandRemoveLower extends Command {

    public CommandRemoveLower(){
        super("removelower", new String[]{"rmlow"}, 0);
    }

    public boolean onCommand(Entity user, String[] args){
        World w = Player.getWorld(user);

        List<Voxel > voxels = w.getAllVoxels(true, false);
        for(Voxel v : voxels){
            if(v.getPosition().getY() < 0)
                w.removeVoxel(v.getPosition());
        }
        Build.g_keepLower = true;

        return true;
    }

}
