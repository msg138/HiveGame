package com.hivegame.game.build.config;

import com.retro.engine.camera.Camera;
import com.retro.engine.camera.FirstPersonCamera;
import com.retro.engine.debug.Debug;
import com.retro.engine.util.file.RetroFile;

/**
 * Created by Michael on 8/10/2016.
 */
public class Config {

    public static String g_defaultConfig = "config.txt";

    public static void loadConfig(String fname){
        Debug.out("Loading config.");
        RetroFile fil = new RetroFile(fname);
        if(fil.fileExists()){
            for(int i=0;i<fil.getLines();i++)
            {
                RetroFile.LineData ld = fil.getLineData(i);
                String[] args = ld.getDataSplit("=");
                if(args.length >= 2){
                    switch(args[0]){
                        case "CAMERASENSITIVITY":
                            ((FirstPersonCamera)Camera.getInstance()).setSensitivity(Float.parseFloat(args[1]));
                            break;
                        case "MOVESPEED":
                            ((FirstPersonCamera)Camera.getInstance()).setMoveVelocity(Float.parseFloat(args[1]));
                            break;
                    }
                }
            }
        }else{
            Debug.out("Config does not exist. Making config");
            writeConfigDefaults(fname);
            loadConfig(fname);
        }
    }

    public static void writeConfigDefaults(String fname){
        RetroFile fil =new RetroFile(fname, "CAMERASENSITIVITY=0.002\nMOVESPEED=0.2\n", true, false);
    }

    public static void writeConfig(String fname){
        Debug.out("Writing configg...");
        String data = "CAMERASENSITIVITY=";
        data+= ((FirstPersonCamera)Camera.getInstance()).getSensitivity() + "\n";
        data += "MOVESPEED=";
        data+= ((FirstPersonCamera)Camera.getInstance()).getMoveVelocity() + "\n";

        RetroFile fil = new RetroFile(fname, data, true, false);
        Debug.out("configg written.");
    }
}
