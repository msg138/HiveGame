package com.hivegame.game.world;

import com.retro.engine.component.Component;
import com.retro.engine.entity.Entity;

/**
 * Created by Michael on 8/6/2016.
 */
public class WorldItemComponent extends Component {

    private String m_name;
    private WorldItem m_worldItem;

    public WorldItemComponent(String name){
        m_name = name;
        m_worldItem = new WorldItem();
    }
    public WorldItemComponent(String name, WorldItem wi){
        m_name = name;
        m_worldItem = wi;
    }

    public boolean onRightClick(Entity user){
        return m_worldItem.onRightClick(user);
    }
    public boolean onLeftClick(Entity user){
        return m_worldItem.onLeftClick(user);
    }

    public WorldItem getWorldItem(){
        return m_worldItem;
    }

    public String getName(){
        return m_name;
    }

    public void destroy(){
        // To be overridden.
    }
}
