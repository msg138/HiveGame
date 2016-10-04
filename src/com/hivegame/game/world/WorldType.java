package com.hivegame.game.world;

/**
 * Created by Michael on 8/21/2016.
 */
public enum WorldType {


    VOID("void"),
    FLAT("flat"),
    GRASSLAND("gland");

    private String m_type;

    private WorldType(String t){
        m_type = t;
    }

    public String getType(){
        return m_type;
    }
}
