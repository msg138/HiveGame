package com.hivegame.game.build;

import com.hivegame.game.build.item.Item;
import com.hivegame.game.livingthing.WorldStorageComponent;
import com.retro.engine.camera.Camera;
import com.retro.engine.debug.Debug;
import com.retro.engine.defaultcomponent.ComponentColor;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector4;
import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.voxel.Voxel;
import com.hivegame.game.world.World;

/**
 * Created by Michael on 8/9/2016.
 */
public class PaintItem extends Item {

    public PaintItem(){
        super("Paint brush", "Use this tool to paint blocks.", "TOOL", false, 2, Item.c_itemInfiniteUses, 1);
        setImageName("paintbukket");
    }

    @Override
    public boolean onLeftClick(Entity user){
        if(!user.has(WorldStorageComponent.class))
            return false;

        World w = Player.getWorld(user);
        LivingComponent living = Player.getLiving(user);
        BuildInventoryComponent build = Player.getBuildInventory(user);

        Vector4 v = living.getVoxelLookAt(w, 15, Camera.getInstance().getPitch(), Camera.getInstance().getYaw());

        ComponentColor color = build.getBuildColor();

        Voxel vox = w.getVoxel(v);
        if(vox != null){
            // Paint it the buildcolor.
            vox.getColor().setRed(color.getRed());
            vox.getColor().setGreen(color.getGreen());
            vox.getColor().setBlue(color.getBlue());
            vox.getColor().setAlpha(color.getAlpha());

            Debug.out(w.rebootChunk(v));
        }

        return true;
    }

    @Override
    public boolean onRightClick(Entity user){

        return onLeftClick(user);
    }
}
