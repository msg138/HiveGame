package com.hivegame.game.livingthing;

/**
 * Created by Michael on 8/31/2016.
 */
public enum Species {

    ICE_GIANT("Ice Giant");

    private String m_name;

    private Species(String name){
        m_name = name;
    }

    public String getName(){
        return m_name;
    }
}
