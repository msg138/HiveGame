package com.hivegame.game.livingthing.species;

import com.hivegame.game.ability.Ability;
import com.hivegame.game.ability.AbilityRosterComponent;
import com.hivegame.game.actualgame.Hivity;
import com.hivegame.game.ai.ChaseAction;
import com.hivegame.game.livingthing.*;
import com.hivegame.game.voxel.ComponentVoxelModel;
import com.hivegame.game.voxel.UtilVoxel;
import com.hivegame.game.world.World;
import com.retro.engine.component.Component;
import com.retro.engine.debug.Debug;
import com.retro.engine.defaultcomponent.ComponentColor;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.defaultcomponent.ComponentRotation;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;

/**
 * Created by Michael on 9/25/2016.
 */
public class IceGiant extends Team {

    public IceGiant(int tn){
        setGroup(tn);
    }

    @Override
    protected Entity generateLeader(World w) {
        Entity e = new Entity();

        ComponentPosition pos = new ComponentPosition(2, 50, 2);

        e.add(pos);
        ComponentVoxelModel vox = UtilVoxel.getVoxelModelComponentAnimation("ig.spa");
        vox.setScale(.09f);
        e.add(vox);
        LivingComponent lc = new LivingComponent(2, 6, .5f, 6, 6, 0, 100, 100, pos, Species.ICE_GIANT);
        lc.setMaxJumps(3);
        lc.setJumpSpeed(.25f);
        lc.setHeight(.75f);
        if(getId() != 0)
            lc.setCurrentAction(new ChaseAction(Hivity.getSelectedUnit()), true);
        e.add(lc);
        e.add(new WorldStorageComponent(w));
        e.add(new ComponentRotation());
        ///ivingComponent(float acc, float pow, float agi, float vit, float str, float exp, float sta, float hea, ComponentPosition p, Species sp){


        e.add(new AbilityRosterComponent(new Ability[]{
                new Ability("Swipe", "Swipe in an arc, dealing basic damage", "swipeattack", 60) {
                    @Override
                    protected void abilityUse(Entity user, Vector3[] affected) {
                        Entity[] ents = getEntitiesAffected(user, affected);
                        for(Entity e : ents)
                            LivingUtil.getLivingComponent(e).modHealth(-25);
                        Debug.out("Skill used.");
                    }

                    @Override
                    protected Vector3[] determineAffected(Entity user, Vector3 v){
                        Vector3 ppos = LivingUtil.getLivingComponent(user).getPosition().toVector3();

                        Vector3 cpos = v.clone();
                        cpos.setX(cpos.getX() * World.getVoxelSize());
                        cpos.setY(cpos.getY() * World.getVoxelSize());
                        cpos.setZ(cpos.getZ() * World.getVoxelSize());

                        Vector3 delta = cpos.subtractFrom(ppos.clone()).normalize();

                        float angle = (float)Math.atan2(delta.getX(), delta.getZ());

                        return new Vector3[]{ new Vector3(ppos.getX() - (float)Math.cos(angle)*1f, ppos.getY(), ppos.getZ() - (float)Math.sin(angle)*1f) };
                    }
                },
                new Ability("Stomp", "Stomp breaking 1st layer of blocks.", "swipeattack", 110) {
                    @Override
                    protected void abilityUse(Entity user, Vector3[] affected) {
                        LivingUtil.getLivingComponent(user).modHealth(-20);

                        for(Vector3 v : affected){
                            v.modY(-1);
                            ((WorldStorageComponent)user.get(WorldStorageComponent.class)).getWorld().removeVoxel(v, true);
                        }
                    }

                    @Override
                    protected Vector3[] determineAffected(Entity user, Vector3 v){

                        v = ((WorldStorageComponent)user.get(WorldStorageComponent.class)).getWorld().convertToVoxelSpaceFlat(LivingUtil.getLivingComponent(user).getPosition().toVector3());
                        v.modY(-1);
                        Vector3 v1 = v.clone();
                        v1.modX(1);
                        Vector3 v2 = v.clone();
                        v2.modX(-1);
                        Vector3 v3 = v.clone();
                        v3.modZ(1);
                        Vector3 v4 = v.clone();
                        v4.modZ(-1);

                        return new Vector3[]{v, v1, v2, v3, v4};
                    }
                },
                new Ability("Swipe", "Swipe in an arc, dealing basic damage", "swipeattack", 110) {
                    @Override
                    protected void abilityUse(Entity user, Vector3[] affected) {
                        LivingUtil.getLivingComponent(user).modHealth(-20);

                        for(Vector3 v : affected){
                            v.modY(0);
                            ((WorldStorageComponent)user.get(WorldStorageComponent.class)).getWorld().addVoxel(v, new ComponentColor(255, 255, 0));
                        }
                    }

                    @Override
                    protected Vector3[] determineAffected(Entity user, Vector3 v){
                        Vector3 v1 = v.clone();
                        v1.modX(1);
                        Vector3 v2 = v.clone();
                        v2.modX(-1);
                        Vector3 v3 = v.clone();
                        v3.modZ(1);
                        Vector3 v4 = v.clone();
                        v4.modZ(-1);

                        return new Vector3[]{v, v1, v2, v3, v4};
                    }
                },
                new Ability("Swipe", "Swipe in an arc, dealing basic damage", "swipeattack", 110) {
                    @Override
                    protected void abilityUse(Entity user, Vector3[] affected) {
                        LivingUtil.getLivingComponent(user).modHealth(25);
                    }
                },
                new Ability("Swipe", "Swipe in an arc, dealing basic damage", "swipeattack", 110) {
                    @Override
                    protected void abilityUse(Entity user, Vector3[] affected) {
                        LivingUtil.getLivingComponent(user).modHealth(50);
                    }
                }

        }));

        return e;
    }
}
