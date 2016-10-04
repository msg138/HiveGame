package com.hivegame.game.world;

import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/6/2016.
 */
public class WorldItem {

    private int m_worldlyItemArrayPosition = 0;

    private boolean m_dead = false;

    public WorldItem(){}

    public void setArrayPosition(int ap){
        m_worldlyItemArrayPosition = ap;
    }
    public int getArrayPosition(){
        return m_worldlyItemArrayPosition;
    }

    public boolean onRightClick(Entity user){
        return false;
    }
    public boolean onLeftClick(Entity user){
        return false;
    }

    public void destroy(){
        m_dead = true;
    }

    public boolean isDead(){
        return m_dead;
    }

    public static WorldItemComponent getWorldItemComponent(Entity e){
        if(e.has(WorldItemComponent.class))
            return (WorldItemComponent)e.get(WorldItemComponent.class);
        return null;
    }
    public static WorldItem getWorldItem(Entity e){
        WorldItemComponent wi = getWorldItemComponent(e);
        if(wi != null)
            return wi.getWorldItem();
        return null;
    }
}
