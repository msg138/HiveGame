package com.hivegame.game.build.player;

import com.hivegame.game.build.item.InventoryComponent;
import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.livingthing.WorldStorageComponent;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;
import com.hivegame.game.build.BuildInventoryComponent;
import com.hivegame.game.world.World;

/**
 * Created by Michael on 8/2/2016.
 */
public class Player {

    private Vector3 m_pos;

    public static Entity createPlayer(LivingComponent lc, ComponentPosition pos, World w){
        Entity e = new Entity();
        e.add(lc);
        e.add(pos);
        e.add(new WorldStorageComponent(w));
        return e;
    }
    public static BuildInventoryComponent getBuildInventory(Entity e){
        if(e.has(BuildInventoryComponent.class))
            return (BuildInventoryComponent)e.get(BuildInventoryComponent.class);
        return null;
    }
    public static InventoryComponent getInventory(Entity e){
        if(e == null)
            return null;
        if(e.has(InventoryComponent.class))
            return (InventoryComponent)e.get(InventoryComponent.class);
        if(e.has(BuildInventoryComponent.class))
            return (BuildInventoryComponent)e.get(BuildInventoryComponent.class);
        return null;
    }

    public static LivingComponent getLiving(Entity e){
        if(e.has(LivingComponent.class))
            return (LivingComponent) e.get(LivingComponent.class);
        return null;
    }
    public static ComponentPosition getPosition(Entity e){
        if(e.has(ComponentPosition.class))
            return (ComponentPosition) e.get(ComponentPosition.class);
        return null;
    }
    public static Vector3 getPositionVector(Entity e){
        ComponentPosition p = getPosition(e);
        if(p!=null)
            return new Vector3(p);
        else
            return null;
    }
    public static World getWorld(Entity e){
        if(e.has(WorldStorageComponent.class))
            return ((WorldStorageComponent)e.get(WorldStorageComponent.class)).getWorld();
        return null;
    }
}
