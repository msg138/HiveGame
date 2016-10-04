package com.hivegame.game.build.item;

import com.hivegame.game.build.player.Player;
import com.hivegame.game.voxel.VoxelFace;
import com.retro.engine.camera.Camera;
import com.retro.engine.debug.Debug;
import com.retro.engine.defaultcomponent.ComponentColor;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;
import com.retro.engine.util.vector.Vector4;

/**
 * Created by Michael on 8/4/2016.
 */
public class BuildItem extends Item {

    private ComponentColor m_color;

    public BuildItem(ComponentColor c){
        super(""+c.getRed()+""+c.getGreen()+c.getBlue(), "Place this down to make designs.", "BLOCK",
                true, 1, c_itemOneUse, 1);
        this.m_color = c;
        setImageName("block");
    }

    @Override
    protected boolean onRightClick(Entity user){
        Vector4 v = Player.getLiving(user).getVoxelLookAt(Player.getWorld(user), 5, Camera.getInstance().getPitch(), Camera.getInstance().getYaw());
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
        return Player.getWorld(user).addVoxel(newLoc, m_color);
    }
}
