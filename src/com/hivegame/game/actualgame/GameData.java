package com.hivegame.game.actualgame;

import com.hivegame.game.gui.BannerDisplay;
import com.hivegame.game.livingthing.LivingUtil;
import com.hivegame.game.livingthing.Team;
import com.hivegame.game.livingthing.UnitHandler;
import com.hivegame.game.settings.GameSettings;
import com.hivegame.game.world.World;
import com.retro.engine.Framework;
import com.retro.engine.entity.Entity;
import com.retro.engine.event.EventHandler;
import com.retro.engine.util.vector.Vector3;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 9/28/2016.
 */
public class GameData {

    private static GameData m_instance;

    private boolean m_initialized = false;
    private int m_gameStage = 0;

    private World m_world;// World that contains terrain and objects within it.

    private List<Team> m_teams;

    private List<GameDeathTimer> m_deaths;// Death handler.

    private GameData(){

        m_instance = this;
    }

    public static GameData getInstance(){
        if(m_instance == null)
            new GameData();
        return m_instance;
    }

    public int getStage(){
        return m_gameStage;
    }
    public boolean isInitialized(){
        return m_initialized;
    }

    public World getWorld(){
        return m_world;
    }

    public void handleDeaths(){
        for(int i=0;i<m_deaths.size();i++)
        {
            if(m_deaths.get(i).isReady()){
                spawn(m_deaths.get(i).getTeam());
                m_deaths.remove(i);
                i--;
            }
        }
    }

    public void addDeath(GameDeathTimer death){
        m_deaths.add(death);
    }

    public void initialize(){

        m_world = new World(0l);
        m_world.generateGrassLand();

        m_world.addSpawn(new World.Spawn(new Vector3(0, 50, 0), 0));
        m_world.addSpawn(new World.Spawn(new Vector3(20, 50, 20), 1));

        m_teams = new ArrayList<>();
        m_deaths = new ArrayList<>();

        m_initialized = true;
        m_gameStage = 0;
    }

    public void addTeam(Team t){
        m_teams.add(t);
    }

    public Entity spawn(Team t){
        Entity e = t.getLeader(GameData.getInstance().getWorld());
        LivingUtil.getLivingComponent(e).modHealth(100);

        UnitHandler.getInstance().addUnit(e);

        Framework.getInstance().getEntityStorage().bringEntityToBack(e);

        BannerDisplay.queueDisplay(LivingUtil.getLivingComponent(e).getSpecies().getName() + " has joined the match!");

        m_world.spawn(e);

        return e;
    }

    public void setupGame(){

        for(int i=0;i<m_teams.size();i++){

            Entity e = spawn(m_teams.get(i));

            if(i==0) // zero should always be player team.
            {
                Hivity.setSelectedUnit(e);
            }
        }
    }

    public void update(){
        handleDeaths();
    }

    public void kill(){
        m_instance = null;
        if(!isInitialized())
            return;
        if(m_world != null)
            m_world.scrapWorld();
    }

    public static class GameDeathTimer{
        private Team m_team;

        private long m_startTime;

        public GameDeathTimer(Team t){
            m_team = t;
            m_startTime = Timer.getInstance().getCurrentTime();
        }

        public boolean isReady(){
            return m_startTime + GameSettings.c_deathTimer < Timer.getInstance().getCurrentTime();
        }

        public Team getTeam(){
            return m_team;
        }
    }
}
