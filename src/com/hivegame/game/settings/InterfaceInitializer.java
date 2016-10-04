package com.hivegame.game.settings;

import com.hivegame.game.voxel.ComponentVoxelModel;
import com.hivegame.game.voxel.UtilVoxel;
import com.retro.engine.Framework;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;
import com.retro.engine.model.RawModel;

/**
 * Created by Michael on 9/11/2016.
 */
public class InterfaceInitializer {

    public static ComponentVoxelModel g_healthbar;
    public static ComponentVoxelModel g_diamond;


    public static void Initialize(){

        g_healthbar = UtilVoxel.getVoxelModelComponent("Files/ui/healthbar.spv");
        g_healthbar.setScale(.1f);
        g_diamond = UtilVoxel.getVoxelModelComponent("Files/ui/diamond.spv");
        g_diamond.setScale(.1f);
    }
}
