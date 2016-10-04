package com.hivegame.game.particle;

import com.retro.engine.Framework;
import com.retro.engine.component.Component;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.util.vector.Vector3;

import java.util.Random;

/**
 * Created by Michael on 8/17/2016.
 */
public class ParticleGeneratorComponent extends Component {

    private int m_interval;
    private int m_curInterval;
    private ParticleComponent m_clone;

    private Vector3 m_radMove;// how much the particles can move randomly.

    private int m_maxInterval;
    private int m_minInterval;

    public ParticleGeneratorComponent(){
        m_interval = 0;
        m_curInterval = 0;
        m_clone = null;

        m_maxInterval = -1;
        m_minInterval = -1;
        m_radMove = new Vector3();
    }

    public ParticleGeneratorComponent(int i, ParticleComponent pc){// TODO make a system for generator and add particle and generator components to engine in HiveGame
        this();
        // Hold speed and different variations of where particles can move. as well as how often to produce them and the max to produce.
        m_interval = i;
        m_curInterval = 0;
        m_clone = pc;
    }

    public ParticleGeneratorComponent(int maxI, int minI, Vector3 rad, ParticleComponent pc){
        this(maxI, pc);
        m_maxInterval = maxI;
        m_minInterval = minI;
        m_radMove = rad;
    }

    public void generate(){
        m_curInterval ++;
        if(m_curInterval >= m_interval){
            ComponentPosition pos = m_clone.getPositionClone();
            pos.modX(-m_radMove.getX());
            pos.modY(-m_radMove.getY());
            pos.modZ(-m_radMove.getZ());
            Random r = new Random();
            pos.modX(r.nextFloat()%1 * 2 * m_radMove.getX());
            pos.modY(r.nextFloat()%1 * 2 * m_radMove.getY());
            pos.modZ(r.nextFloat()%1 * 2 * m_radMove.getZ());

            Framework.getInstance().getEntityStorage().addEntity(ParticleFactory.makeParticle(pos, m_clone.getDirection(), m_clone.getSpeed(), m_clone.getMaxLifetime(), m_clone.getParticleName()));
            m_curInterval = 0;

            if(m_minInterval != -1 && m_maxInterval != -1){
                m_interval = r.nextInt(m_maxInterval-m_minInterval)+m_minInterval;
            }
        }
    }
}
