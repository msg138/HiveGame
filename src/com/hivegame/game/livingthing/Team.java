package com.hivegame.game.livingthing;

import com.hivegame.game.world.World;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 9/11/2016.
 */
public abstract class Team {

    private int m_teamGroupIdentifier = 0;

    public Team(){
    }


    public int getId(){
        return m_teamGroupIdentifier;
    }

    public void setGroup(int id){
        m_teamGroupIdentifier = id;
    }

    protected abstract Entity generateLeader(World w);

    public Entity getLeader(World w){
        Entity e = generateLeader(w);
        LivingUtil.getLivingComponent(e).setTeamOwner(this);
        LivingUtil.getLivingComponent(e).setTeamNumber(getId());
        return e;
    }


}
