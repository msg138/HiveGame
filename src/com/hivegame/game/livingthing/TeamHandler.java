package com.hivegame.game.livingthing;

import com.hivegame.game.livingthing.species.IceGiant;

/**
 * Created by Michael on 9/12/2016.
 */
public enum TeamHandler {

    ICE_GIANT(IceGiant.class, "Ice Giant", "");

    private java.lang.Class<? extends Team> m_teamType;

    private String m_teamName;
    private String m_teamDescription;

    private TeamHandler(java.lang.Class<? extends Team> teamType, String tname, String tdesc){
        m_teamName = tname;
        m_teamDescription = tdesc;

        m_teamType = teamType;
    }

    public String getTeamName(){
        return m_teamName;
    }
    public String getTeamDescription(){
        return m_teamDescription;
    }

    public Team getNewTeamObject(){
        try {
            return m_teamType.newInstance();
        }catch(Exception e){
            return null;
        }
    }
}
