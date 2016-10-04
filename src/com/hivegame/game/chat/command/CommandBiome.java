package com.hivegame.game.chat.command;

import com.hivegame.game.chat.Command;
import com.retro.engine.camera.Camera;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.build.player.NoClipComponent;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.world.Biome;

/**
 * Created by Michael on 8/23/2016.
 */
public class CommandBiome extends Command {

    public CommandBiome(){
        super("biome", new String[]{"b"}, 0);
    }

    public boolean onCommand(Entity sender, String[] args){
        Vector3 pos = Player.getWorld(sender).convertToVoxelSpaceFlat(Player.getPositionVector(sender));
        if(sender.has(NoClipComponent.class))
            pos = Player.getWorld(sender).convertToVoxelSpaceFlat(Camera.getInstance().getPosition().toVector3());
        pos.setY(-1);
        Biome b = Player.getWorld(sender).getBiome(pos);
        String data = b.getName() + " El: " + b.getElevation() + " Hu: " + b.getHumidity() + "\n ";
        if(Player.getWorld(sender).getVoxel(pos) != null)
            data += Player.getWorld(sender).getVoxel(pos).getColor().toString();
        ChatBox.getInstance().addMessage(data, sender);

        return true;
    }
}
