package com.hivegame.game.livingthing;

import com.hivegame.game.world.World;
import com.retro.engine.Framework;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 9/1/2016.
 */
public class UnitHandler {

    public static final float c_selectionThreshold = 2f;

    private static UnitHandler m_instance;

    private List<Entity> m_units;


    private UnitHandler(){

        m_units = new ArrayList<>();

        m_instance = this;
    }

    public static UnitHandler getInstance(){
        if(m_instance == null)
            new UnitHandler();
        return m_instance;
    }


    public void addUnit(Entity e){
        m_units.add(e);
        Framework.getInstance().getEntityStorage().addEntity(e);
    }
    public Entity removeUnit(Entity e){
        m_units.remove(e);
        Framework.getInstance().getEntityStorage().removeEntity(e, true);
        return e;
    }
    public Entity removeUnit(int id){
        Entity ret = m_units.get(id);
        return removeUnit(ret);
    }

    public Entity getUnit(Vector3 v){
        return getUnit(v, null);
    }
    public Entity getUnit(Vector3 v, Entity ee){
        for(Entity e : m_units){
            if(ee == e && ee != null)
                continue;
            ComponentPosition p = LivingUtil.getLivingComponent(e).getPosition();

            if(p.toVector3().distanceTo(v) < c_selectionThreshold)
                return e;
        }
        return null;
    }
    public List<Entity> getUnits(Vector3 v){
        List<Entity> ret = new ArrayList<>();
        for(Entity e : m_units){
            ComponentPosition p = LivingUtil.getLivingComponent(e).getPosition();

            if(p.toVector3().distanceTo(v) < c_selectionThreshold)
                ret.add(e);
        }
        return ret;
    }

    public void kill(){
        for(Entity e : m_units)
        {
            Framework.getInstance().getEntityStorage().removeEntity(e, true);
        }
        m_units.clear();
    }


}
