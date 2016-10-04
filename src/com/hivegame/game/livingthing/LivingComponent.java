package com.hivegame.game.livingthing;

import com.hivegame.game.ability.AbilityRosterComponent;
import com.hivegame.game.actualgame.Timer;
import com.hivegame.game.ai.Action;
import com.hivegame.game.ai.IdleAction;
import com.hivegame.game.voxel.VoxelFace;
import com.retro.engine.Framework;
import com.retro.engine.component.Component;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.defaultcomponent.ComponentRotation;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.time.UtilTime;
import com.retro.engine.util.vector.Vector3;
import com.retro.engine.util.vector.Vector4;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.voxel.Voxel;
import com.hivegame.game.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 8/3/2016.
 */
public class LivingComponent extends Component {

    public static final float c_staminaRegenBase = 0.01f;
    public static final float c_movementSpeedBase = 0.12f;
    public static final Action c_defaultAction = new IdleAction();

    private ComponentPosition m_position;// Link this with the entity.

    private Species m_species;

    private long m_timeBorn;

    private List<Action> m_actionQueue;

    private float m_height;// The length width and depth of collision cube

    private boolean m_selected;

    private int m_jumps;
    private int m_maxJumps;
    private int m_jumpMod;
    private boolean m_ground;
    private float m_jumpSpeed;
    private float m_currentVelY;
    private float m_currentVelX;
    private float m_currentVelZ;

    private float m_maxHealth;
    private float m_health;
    private float m_healthMod;

    private float m_maxStamina;
    private float m_stamina;
    private float m_staminaMod;

    private float m_experience;
    private float m_level;

    private float m_strength;
    private float m_strMod;

    private float m_vitality;
    private float m_vitMod;

    private float m_agility;
    private float m_agiMod;

    private float m_power;
    private float m_powMod;

    private float m_accuracy;
    private float m_accMod;

    private int m_teamNumber;

    private Team m_owner;

    public LivingComponent(ComponentPosition p){
        this(0, 0, 0, 0, 0, 0, 0, 0, p, null);
    }

    public LivingComponent(float acc, float pow, float agi, float vit, float str, float exp, float sta, float hea, ComponentPosition p, Species sp){
        m_accuracy = acc;
        m_power = pow;
        m_agility = agi;
        m_vitality = vit;
        m_strength = str;
        m_experience = exp;
        // TODO calculate m_level based on m_experience.
        m_level = 0;
        m_jumps = 0;
        m_maxJumps = 1;
        m_maxStamina = sta;
        m_maxHealth = hea;

        m_health = m_maxHealth;
        m_stamina = m_maxStamina;

        m_species = sp;

        m_position = p;

        m_actionQueue = new ArrayList<>();

        m_currentVelX = 0;
        m_currentVelZ = 0;

        m_timeBorn = Timer.getInstance().getCurrentTime();

        m_teamNumber = 0;

        resetModifiers();
    }

    public void setTeamOwner(Team t){
        m_owner = t;
    }
    public Team getTeamOwner(){
        return m_owner;
    }

    public int getTeamNumber(){
        return m_teamNumber;
    }
    public void setTeamNumber(int n){
        m_teamNumber = n;
    }

    public long getTimeBorn(){
        return m_timeBorn;
    }

    public long getAge(){
        return Timer.getInstance().getCurrentTime() - m_timeBorn;
    }

    public void resetModifiers(){
        m_agiMod = 0;
        m_accMod = 0;
        m_powMod = 0;
        m_strMod = 0;
        m_vitMod = 0;

        m_jumpMod = 0;

        m_healthMod = 0;
        m_staminaMod = 0;
    }

    public boolean isSelected(){
        return m_selected;
    }
    public void select(){
        m_selected = true;
    }
    public void deselect(){
        m_selected = false;
    }
    public void setSelected(boolean b){
        m_selected = b;
    }

    public void setCurrentVelX(float x){
        m_currentVelX = x;
    }
    public void setCurrentVelZ(float z){
        m_currentVelZ = z;
    }
    public void setCurrentVelY(float y){
        m_currentVelY = y;
    }

    public float getCurrentVelX(){
        return m_currentVelX;
    }
    public float getCurrentVelZ(){
        return m_currentVelZ;
    }

    public float getCurrentVelY(){
        return m_currentVelY;
    }

    public List<Action> getActionQueue(){
        return m_actionQueue;
    }

    public int getActionQueueSize(){
        return m_actionQueue.size();
    }

    public Action getCurrentAction(){
        if(m_actionQueue.size() > 0)
            return m_actionQueue.get(0);
        return c_defaultAction;
    }

    public boolean isIdle(){
        return c_defaultAction == getCurrentAction();
    }
    public void clearActions(){
        m_actionQueue.clear();
    }

    public void addAction(Action ac){
        m_actionQueue.add(ac);
    }

    public void setCurrentAction(Action ac, boolean removeOthers){
        if(removeOthers) {
            clearActions();
            m_actionQueue.add(ac);
        }else
            m_actionQueue.add(0, ac);
    }

    public void removeAction(int id){
        if(m_actionQueue.size() > 0){
            m_actionQueue.remove(id);
        }
    }

    public ComponentPosition getPosition(){
        return m_position;
    }

    public Species getSpecies(){
        return m_species;
    }

    public float getMovementSpeed(){
        return (getAgility() * c_movementSpeedBase);
    }

    public boolean isDead(){
        return m_health <= 0;
    }

    public void update(Entity e){
        World w = ((WorldStorageComponent)e.get(WorldStorageComponent.class)).getWorld();
        onAgeTick((AbilityRosterComponent)e.get(AbilityRosterComponent.class));
        onActionTick(w);
        onMotionTick(w, e);

        ComponentRotation rot = (ComponentRotation)e.get(ComponentRotation.class);
        Vector3 newPos = getPosition().toVector3().clone();
        newPos.modX(m_currentVelX);
        newPos.modY(m_currentVelY);
        newPos.modZ(m_currentVelZ);
        Vector3 delta = newPos.subtractFrom(getPosition().toVector3().clone()).normalize();

        if(m_currentVelX != 0 || m_currentVelZ != 0)
            rot.setRotationY((float)Math.atan2(delta.getX(), delta.getZ()));
    }

    public void onAgeTick(AbilityRosterComponent abi){
        /**
        if(getAge() >= getSocialClass().getLifeSpan())
        {
            modHealth(-0.05f);
        }*/
        for(int i=0;i<abi.getAmountOfAbilities();i++){
            abi.getAbility(i).coolOff(1);
        }
    }

    public void onActionTick(World w){
        if(getCurrentAction().isComplete(this, w))
        {
            removeAction(0);
        }
        getCurrentAction().update(this, w);
    }

    public void onMotionTick(World w, Entity e){
        m_position.setY(m_position.getY() - getHeight());
        Vector3 oldPos = new Vector3(m_position.getX(), m_position.getY(), m_position.getZ());
        Vector3 voxPos = w.convertToVoxelSpaceFlat(oldPos);
        if(w.getVoxel(voxPos) != null && !isJumping()){
            ground();
            oldPos.setY(voxPos.getY()* w.getVoxelSize());
        }else
            notGround();
        Vector3 newPos = getModPosY(w, oldPos);
        newPos = getModPosXZ(w, newPos, e);
        m_position.setX(newPos.getX());
        m_position.setY(newPos.getY() + getHeight());
        m_position.setZ(newPos.getZ());
    }

    public void move(float angle){// Expects radians angle
        m_currentVelX = (float)Math.sin(angle) * getMovementSpeed();
        m_currentVelZ = (float)Math.cos(angle) * getMovementSpeed();
    }

    public void noMove(){
        m_currentVelX = 0;
        m_currentVelZ = 0;
    }

    public Vector3 getModPosXZ(World w, Vector3 oldPos, Entity e){
        Vector3 newPos = oldPos.clone();

        newPos.modX(m_currentVelX * Timer.getInstance().getSpeed());
        newPos.modZ(m_currentVelZ * Timer.getInstance().getSpeed());
        Entity ee = UnitHandler.getInstance().getUnit(newPos, e);
        if((ee != null && ee != e) || !canMoveTo(newPos, w))
        {
            newPos.modX(-m_currentVelX * Timer.getInstance().getSpeed());
            newPos.modZ(-m_currentVelZ * Timer.getInstance().getSpeed());
            m_currentVelX = 0;
            m_currentVelZ = 0;
        }

        return newPos;
    }

    protected boolean canMoveTo(Vector3 nep, World w){
        Vector3 np = nep.clone();
        np.modY(getHeight()/2f);
        Voxel v = w.getVoxel(w.convertToVoxelSpaceFlat(np));
        if(v == null || !v.isCollidable()) {
            np.modY(getHeight()/1f);
            v = w.getVoxel(w.convertToVoxelSpaceFlat(np));
            if(v == null || !v.isCollidable()){
                return true;
            }
        }
        return false;
    }

    public Vector3 getModPosY(World w, Vector3 oldPos){
        Vector3 newPos = new Vector3(oldPos.getX(), oldPos.getY(), oldPos.getZ());
        if(!isGround() || m_currentVelY > 0.0f)
        {
            // Apply gravity.
            m_currentVelY -= w.getGravity();
            if(m_currentVelY < -w.getMaxGravity())
                m_currentVelY = -w.getMaxGravity();
            Vector3 jumpPos = newPos.clone();
            jumpPos.modY(getHeight()*1.25f + m_currentVelY * Timer.getInstance().getSpeed());
            if(w.getVoxel(w.convertToVoxelSpaceFlat(jumpPos)) != null){
                m_currentVelY = 0;
                return newPos;
            }
        }else
        {
            m_jumps = 0;
            m_currentVelY = 0;
        }

        newPos.setY(newPos.getY() + m_currentVelY * Timer.getInstance().getSpeed());
        return newPos;
    }

    public void modHealth(float mod){
        m_health += mod;

        if(m_health > getMaxHealth())
            m_health = getMaxHealth();
        if(m_health < 0)
            m_health = 0;
    }
    public void setHealth(float h){
        m_health = h;
        modHealth(0);
    }
    public void modStamina(float mod){
        m_stamina += mod;

        if(m_stamina > getMaxStamina())
            m_stamina = getMaxStamina();
        if(m_stamina < 0)
            m_stamina = 0;
    }
    public void setStamina(float s){
        m_stamina = s;
        modStamina(0);
    }

    public float getHeight(){
        return m_height;
    }

    public void setHeight(float h){
        m_height = h;
    }

    public int getMaxJumps(){
        return m_maxJumps + m_jumpMod;
    }
    public void setMaxJumps(int j){
        m_maxJumps = j;
    }

    public int getCurrentJumps(){
        return m_jumps;
    }

    public float getJumpSpeed(){
        return m_jumpSpeed;
    }
    public void setJumpSpeed(float js){
        m_jumpSpeed = js;
    }

    public boolean isGround(){
        return m_ground;
    }

    public void ground(){
        m_ground = true;
    }

    public void notGround(){
        m_ground = false;
    }

    public boolean isJumping(){
        return m_currentVelY > 0.0f;
    }

    public boolean jump(){
        if(m_jumps < m_maxJumps) {
            m_jumps++;
            m_currentVelY = m_jumpSpeed;
            return true;
        }
        return false;
    }

    public float getMaxHealth() {
        return m_maxHealth + m_healthMod;
    }

    public float getHealth() {
        return m_health;
    }

    public float getMaxStamina() {
        return m_maxStamina + m_staminaMod;
    }

    public float getStamina() {
        return m_stamina;
    }

    public float getExperience() {
        return m_experience;
    }

    public float getLevel() {
        return m_level;
    }

    public float getStrength() {
        return m_strength + m_strMod;
    }

    public float getVitality() {
        return m_vitality + m_vitMod;
    }

    public float getAgility() {
        return m_agility + m_agiMod;
    }

    public float getPower() {
        return m_power + m_powMod;
    }

    public float getAccuracy() {
        return m_accuracy + m_accMod;
    }


    /**
     * Deprecated, only kept because of old buildmode.
     */

    // Only works in first person. for now
    public Vector4 getVoxelLookAt(World w, float distance, float pitch, float yaw){
        Vector4 ret = new Vector4();
        boolean found = false;
        for (float j = 0; j < distance && !found; j += 0.03f){
            float xx, yy, zz;
            float rad = yaw;// Convert into radians.
            xx = Framework.getInstance().getCamera().getPosition().getX() - (float) Math.sin(rad)*(j*w.getVoxelSize());
            zz = Framework.getInstance().getCamera().getPosition().getZ() - (float) Math.cos(rad)*(j*w.getVoxelSize());

            yy = Framework.getInstance().getCamera().getPosition().getY() + (float) Math.tan((pitch))*j*w.getVoxelSize();

            Vector3 coords = new Vector3(xx, yy, zz);
            Vector3 voxx = w.convertToVoxelSpaceFlat(coords);

            if(w.getVoxel(voxx) != null) {
                Vector3 vox = new Vector3(voxx.getX() * w.getVoxelSize(), voxx.getY() * w.getVoxelSize(), voxx.getZ() * w.getVoxelSize());
                Vector3[] points = new Vector3[6];
                points[VoxelFace.FACE_TOP.getFace()] = new Vector3(vox.getX(), vox.getY() + w.getVoxelSize()/2, vox.getZ());
                points[VoxelFace.FACE_BOTTOM.getFace()] = new Vector3(vox.getX(), vox.getY() - w.getVoxelSize()/2, vox.getZ());

                points[VoxelFace.FACE_LEFT.getFace()] = new Vector3(vox.getX(), vox.getY(), vox.getZ() + w.getVoxelSize()/2);
                points[VoxelFace.FACE_RIGHT.getFace()] = new Vector3(vox.getX(), vox.getY(), vox.getZ() - w.getVoxelSize()/2);

                points[VoxelFace.FACE_FRONT.getFace()] = new Vector3(vox.getX() + w.getVoxelSize()/2, vox.getY(), vox.getZ());
                points[VoxelFace.FACE_BACK.getFace()] = new Vector3(vox.getX() - w.getVoxelSize()/2, vox.getY(), vox.getZ());

                int closest = 0;
                float curDistance = coords.distanceTo(points[closest]);
                for(int i=0;i<6;i++)
                {
                    if(i == closest)
                        continue;
                    if(coords.distanceTo(points[i]) < curDistance)
                    {
                        closest = i;
                        curDistance = coords.distanceTo(points[closest]);
                    }
                }
                ret = new Vector4(voxx, closest);

                found = true;
            }
        }
        if(!found)
            return new Vector4(-1, -1, -1, 0);
        return ret;
    }
    public List<Vector4> getVoxelsLookAt(World w, float distance, float pitch, float yaw){
        List<Vector4> ret = new ArrayList<>();
        List<Vector3> alreadyGot = new ArrayList<>();
        boolean found = false;
        for (float j = 0; j < distance && !found; j += 0.03f){
            float xx, yy, zz;
            float rad = yaw;// Convert into radians.
            xx = Framework.getInstance().getCamera().getPosition().getX() - (float) Math.sin(rad)*(j*w.getVoxelSize());
            zz = Framework.getInstance().getCamera().getPosition().getZ() - (float) Math.cos(rad)*(j*w.getVoxelSize());

            yy = Framework.getInstance().getCamera().getPosition().getY() + (float) Math.tan((pitch))*j*w.getVoxelSize();

            Vector3 coords = new Vector3(xx, yy, zz);
            Vector3 voxx = w.convertToVoxelSpaceFlat(coords);

            Voxel v = w.getVoxel(voxx);
            boolean nope = false;
            for(Vector3 vv : alreadyGot)
                if(vv.equals(voxx))
                {
                    nope = true;
                    break;
                }
            if(nope)
                continue;
            alreadyGot.add(voxx);

            if(v != null) {
                Vector3 vox = new Vector3(voxx.getX() * w.getVoxelSize(), voxx.getY() * w.getVoxelSize(), voxx.getZ() * w.getVoxelSize());
                Vector3[] points = new Vector3[6];
                points[VoxelFace.FACE_TOP.getFace()] = new Vector3(vox.getX(), vox.getY() + w.getVoxelSize()/2, vox.getZ());
                points[VoxelFace.FACE_BOTTOM.getFace()] = new Vector3(vox.getX(), vox.getY() - w.getVoxelSize()/2, vox.getZ());

                points[VoxelFace.FACE_LEFT.getFace()] = new Vector3(vox.getX(), vox.getY(), vox.getZ() + w.getVoxelSize()/2);
                points[VoxelFace.FACE_RIGHT.getFace()] = new Vector3(vox.getX(), vox.getY(), vox.getZ() - w.getVoxelSize()/2);

                points[VoxelFace.FACE_FRONT.getFace()] = new Vector3(vox.getX() + w.getVoxelSize()/2, vox.getY(), vox.getZ());
                points[VoxelFace.FACE_BACK.getFace()] = new Vector3(vox.getX() - w.getVoxelSize()/2, vox.getY(), vox.getZ());

                int closest = 0;
                float curDistance = coords.distanceTo(points[closest]);
                for(int i=0;i<6;i++)
                {
                    if(i == closest)
                        continue;
                    if(coords.distanceTo(points[i]) < curDistance)
                    {
                        closest = i;
                        curDistance = coords.distanceTo(points[closest]);
                    }
                }
                ret.add(new Vector4(voxx, closest));
            }else
                ret.add(new Vector4(voxx, VoxelFace.FACE_NIL.getFace()));
        }
        return ret;
    }
}
