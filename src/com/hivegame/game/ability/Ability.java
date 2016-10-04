package com.hivegame.game.ability;

import com.hivegame.game.livingthing.LivingUtil;
import com.hivegame.game.livingthing.UnitHandler;
import com.hivegame.game.world.World;
import com.retro.engine.defaultcomponent.ComponentRotation;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 9/25/2016.
 */
public abstract class Ability {

    private int m_currentCooldown;
    private int m_maxCooldown;

    private String m_name;
    private String m_imageName;
    private String m_description;

    public Ability(){
        m_currentCooldown = 0;
        m_maxCooldown = 0;
        m_imageName = "hotbarback";
    }

    public Ability(String name, String desc, String iname, int cd){
        this();
        m_maxCooldown = cd;
        m_imageName = iname;
        m_name = name;
        m_description = desc;
    }

    public String getName() {
        return m_name;
    }

    public String getImageName() {
        return m_imageName;
    }

    public String getDescription() {
        return m_description;
    }


    public boolean useAbility(Entity user, Vector3 position){
        if(onCooldown())
            return false;

        // iF we use the ability, turn to face wherever it happens.
        ComponentRotation rot = (ComponentRotation)user.get(ComponentRotation.class);
        Vector3 cpos = position.clone();
        cpos.setX(cpos.getX() * World.getVoxelSize());
        cpos.setY(cpos.getY() * World.getVoxelSize());
        cpos.setZ(cpos.getZ() * World.getVoxelSize());
        Vector3 delta = cpos.subtractFrom(LivingUtil.getPosition(user).toVector3().clone()).normalize();
        rot.setRotationY((float)Math.atan2(delta.getX(), delta.getZ()));

        abilityUse(user, determineAffected(user, position));

        putOnCooldown();

        return true;
    }

    public boolean onCooldown(){
        return m_currentCooldown > 0;
    }

    public void coolOff(int amount){
        m_currentCooldown -= amount;
        if(m_currentCooldown < 0)
            m_currentCooldown = 0;
    }

    public int getMaxCooldown(){
        return m_maxCooldown;
    }
    public int getCurrentCooldown(){
        return m_currentCooldown;
    }

    public void putOnCooldown(){
        m_currentCooldown = m_maxCooldown;
    }

    protected Vector3[] determineAffected(Entity user, Vector3 pos){
        return new Vector3[]{ pos };
    }

    protected Entity[] getEntitiesAffected(Entity user, Vector3[] affected){
        List<Entity> ents = new ArrayList<>();
        for(Vector3 a : affected) {
            Entity e = UnitHandler.getInstance().getUnit(a, user);
            if(e == null || LivingUtil.getLivingComponent(e) == null)
                continue;
            if(LivingUtil.getLivingComponent(e).getTeamNumber() != LivingUtil.getLivingComponent(user).getTeamNumber())
                ents.add(e);
        }
        return ents.toArray(new Entity[0]);
    }

    protected abstract void abilityUse(Entity user, Vector3[] affected);
}
