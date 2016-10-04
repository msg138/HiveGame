package com.hivegame.game.chat;

import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/8/2016.
 */
public abstract class Command {


    private String m_commandName;
    private int m_requiredArguments;
    private String[] m_alias;

    public Command(String cn, String[] alias, int args){
        m_commandName = cn;
        m_requiredArguments = args;
        m_alias = alias;
    }
    public Command(String cn){
        this(cn, new String[]{}, 0);
    }
    public Command(String cn, int args){
        this(cn, new String[]{}, args);
    }

    public boolean isAlias(String cmd){
        for(String c : m_alias)
            if(c.equals(cmd))
                return true;
        return false;
    }

    public String getName(){
        return m_commandName;
    }
    public int getRequiredArguments(){
        return m_requiredArguments;
    }

    public abstract boolean onCommand(Entity sender, String[] args);
}
