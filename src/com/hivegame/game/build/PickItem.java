package com.hivegame.game.build;

import com.retro.engine.camera.Camera;
import com.retro.engine.debug.Debug;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;
import com.retro.engine.util.vector.Vector4;
import com.hivegame.game.build.item.Item;
import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.livingthing.WorldStorageComponent;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.voxel.VoxelFace;
import com.hivegame.game.world.World;

/**
 * Created by Michael on 8/9/2016.
 */
public class PickItem extends Item {

    public PickItem(){
        super("Pickaxe", "Use this tool to place and dig blocks.", "TOOL", false, 2, Item.c_itemInfiniteUses, 1);
        setImageName("pickaxe");
    }

    @Override
    public boolean onLeftClick(Entity user){
        if(!user.has(WorldStorageComponent.class))
            return false;

        World w = Player.getWorld(user);
        LivingComponent living = Player.getLiving(user);

        Vector4 v = living.getVoxelLookAt(w, 15, Camera.getInstance().getPitch(), Camera.getInstance().getYaw());
        Debug.out("Mining:"+ v.toString() + "F: " + VoxelFace.getFaceName((int)v.getW()));

        w.removeVoxel(v);

        return true;
    }

    @Override
    public boolean onRightClick(Entity user){

        World w = Player.getWorld(user);
        LivingComponent living = Player.getLiving(user);

        Vector4 v = living.getVoxelLookAt(w, 15, Camera.getInstance().getPitch(), Camera.getInstance().getYaw());
        Debug.out("Building:"+ v.toString() + "F: " + VoxelFace.getFaceName((int)v.getW()));

        Vector3 newLoc = v.toVector3();
        if(newLoc.getX() <= -1 || newLoc.getZ() <= -1)
            return false;
        if(VoxelFace.FACE_TOP.getFace() == v.getW())
            newLoc.modY(1);
        else if(VoxelFace.FACE_BOTTOM.getFace() == v.getW())
            newLoc.modY(-1);
        else if(VoxelFace.FACE_LEFT.getFace() == v.getW())
            newLoc.modZ(1);
        else if(VoxelFace.FACE_RIGHT.getFace() == v.getW())
            newLoc.modZ(-1);
        else if(VoxelFace.FACE_FRONT.getFace() == v.getW())
            newLoc.modX(1);
        else
            newLoc.modX(-1);
        return w.addVoxel(newLoc, Player.getBuildInventory(user).getBuildColor().clone());
    }
}
