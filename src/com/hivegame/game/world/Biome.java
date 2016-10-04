package com.hivegame.game.world;

import com.retro.engine.defaultcomponent.ComponentColor;

/**
 * Created by Michael on 8/23/2016.
 */
public enum Biome {
    // Order matters because of how the getBiome method works.
    WATER("Ocean", 0, 0, new ComponentColor[]{new ComponentColor(0,0,156)}),
    BEACH("Beach", 1, 1, new ComponentColor[]{new ComponentColor(200, 150, 42)}),
    GRASSLAND("GrassLand", 2, 2, new ComponentColor[]{new ComponentColor(100, 255, 42)}),
    TUNDRA("Tundra", 3, 3, new ComponentColor[]{new ComponentColor(200, 200, 255)}),
    MARSH("Marsh", 4, 4, new ComponentColor[]{new ComponentColor(50, 150, 10)}),
    ;

    private String m_name;
    private float m_humidity;
    private float m_elevation;
    private ComponentColor[] m_colors;


    private Biome(String name, float hum, float elevation, ComponentColor[] colors){
        m_name = name;
        m_humidity = hum;
        m_elevation = elevation;
        m_colors = colors;
    }

    public String getName(){
        return m_name;
    }

    public float getHumidity(){
        return m_humidity;
    }
    public float getElevation(){
        return m_elevation;
    }

    public ComponentColor[] getColors(){
        return m_colors;
    }


    public static Biome getBiome(float elevation, float humidity){
        Biome ret = null;

        for(Biome b : Biome.values()){
            if(ret == null || (b.getElevation()<=elevation && b.getHumidity()<=humidity))
                ret = b;
        }

        return ret;
    }
}
