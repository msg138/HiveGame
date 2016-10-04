package com.hivegame.game.livingthing;

import com.retro.engine.component.Component;
import com.hivegame.game.world.World;

/**
 * Created by Michael on 8/3/2016.
 */
public class WorldStorageComponent extends Component {

    private World m_w;

    public WorldStorageComponent(World w){
        m_w = w;
    }

    public World getWorld(){
        return m_w;
    }
    public void setWorld(World w){
        m_w = w;
    }
}
