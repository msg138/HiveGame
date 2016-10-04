package com.hivegame.game.build;

import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.build.item.Item;
import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.livingthing.WorldStorageComponent;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.world.World;
import com.retro.engine.camera.Camera;
import com.retro.engine.defaultcomponent.ComponentColor;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector4;
import com.hivegame.game.voxel.Voxel;

/**
 * Created by Michael on 8/9/2016.
 */
public class EyeDropperItem extends Item {

    public EyeDropperItem(){
        super("Eye Dropper", "Use this tool to get \nthe color of a block.", "TOOL", false, 2, Item.c_itemInfiniteUses, 1);
        setImageName("eyedropper");
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
            ComponentColor c = vox.getColor();
            color.setRed(c.getRed());
            color.setBlue(c.getBlue());
            color.setGreen(c.getGreen());
            color.setAlpha(c.getAlpha());
        }else
            return false;

        ChatBox.getInstance().addMessage("New color: " + color.toString(), user);

        return true;
    }

    @Override
    public boolean onRightClick(Entity user){

        return onLeftClick(user);
    }
}
