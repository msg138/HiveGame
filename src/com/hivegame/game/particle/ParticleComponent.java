package com.hivegame.game.particle;

import com.retro.engine.component.Component;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.util.vector.Vector3;

/**
 * Created by Michael on 8/17/2016.
 */
public class ParticleComponent extends Component{


    private ComponentPosition m_position;
    private Vector3 m_direction;
    private float m_speed;

    private String m_pName;

    private int m_lifetime;
    private int m_maxLifetime;

    public ParticleComponent(ComponentPosition position, Vector3 direction, float speed, int lifetime, String pname){
        m_position = position;
        m_direction = direction;
        m_speed = speed;
        m_maxLifetime = lifetime;
        m_lifetime = 0;
        m_pName = pname;
    }

    public String getParticleName(){
        return m_pName;
    }

    public ComponentPosition getPosition(){
        return m_position;
    }
    public ComponentPosition getPositionClone(){
        return new ComponentPosition(m_position.getX(), m_position.getY(), m_position.getZ(), m_position.getWidth(), m_position.getHeight());
    }
    public Vector3 getDirection(){
        return m_direction;
    }
    public float getSpeed(){
        return m_speed;
    }

    public int getMaxLifetime(){
        return m_maxLifetime;
    }
    public int getLifetime(){
        return m_lifetime;
    }

    public boolean isDead(){
        return m_lifetime >= m_maxLifetime;
    }
    public void live(){
        m_lifetime ++;
    }
}
