package com.hivegame.game.gui;

/**
 * Created by Michael on 9/18/2016.
 */
public abstract class Gui {

    private static Gui m_instance;

    public Gui(){
    }

    protected static void setInstance(Gui g){
        if(m_instance != null)
            m_instance.kill();
        m_instance = g;
    }

    public static void destroyGui(){
        setInstance(null);
    }

    public static Gui getInstance(){
        return m_instance;
    }

    public abstract void update();

    public abstract void kill();

}
