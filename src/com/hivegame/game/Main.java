package com.hivegame.game;

import com.retro.engine.AppProcess;
import com.retro.engine.Framework;
import com.retro.engine.RetroEngine;

/**
 * Created by Michael on 7/30/2016.
 */
public class Main {
    public static void main(String[] args){
        Framework.useOpenGL();
         final AppProcess app = RetroEngine.createApp("Hive " + Hive.getVersion(), (int)(1.3333333*640), 640, new HiveGame());

    }
}
