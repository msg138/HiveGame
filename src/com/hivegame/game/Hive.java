package com.hivegame.game;

/**
 * Created by Michael on 7/30/2016.
 */
public class Hive {
    private static String c_version = "NILVERSION";// This will be loaded from a file to verify client integrity.

    public static String getVersion(){
        return c_version;
    }
}
