package com.hivegame.game;

import com.hivegame.game.ability.AbilityRosterComponent;
import com.hivegame.game.actualgame.Hivity;
import com.hivegame.game.build.Build;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.build.item.*;
import com.hivegame.game.gui.Gui;
import com.hivegame.game.livingthing.WorldStorageComponent;
import com.hivegame.game.particle.ParticleComponent;
import com.hivegame.game.particle.ParticleSystem;
import com.hivegame.game.voxel.*;
import com.jogamp.opengl.GL2;
import com.retro.engine.Framework;
import com.retro.engine.RetroProcess;
import com.retro.engine.component.ComponentType;
import com.retro.engine.debug.Debug;
import com.retro.engine.light.Light;
import com.retro.engine.loading.LoadData;
import com.retro.engine.light.LightHandler;
import com.retro.engine.model.shader.ShaderLoader;
import com.retro.engine.model.shader.ShaderProgram;
import com.retro.engine.scene.SceneType;
import com.retro.engine.util.file.RetroFile;
import com.retro.engine.util.vector.Vector3;
import com.hivegame.game.build.BuildInventoryComponent;
import com.hivegame.game.build.BuildInventoryUpdateSystem;
import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.livingthing.LivingSystem;
import com.hivegame.game.particle.ParticleGeneratorComponent;
import com.hivegame.game.particle.ParticleGeneratorSystem;
import com.hivegame.game.build.player.ActivePlayerComponent;
import com.hivegame.game.build.player.NoClipComponent;
import com.hivegame.game.build.ui.GuiComponent;
import com.hivegame.game.world.WorldItemComponent;

/**
 * Created by Michael on 7/30/2016.
 */
public class HiveGame extends RetroProcess {

    /**
     * Reminder of what each scene is
     * Scene001 - BuildMode for models and animations.
     * Scene000 - Game playing
     */

    private boolean m_enterBuildMode;

    private Hivity m_gameData;// Store the game data and handle everything within that.

    public HiveGame(){
        super(1080, 720);
        m_enterBuildMode = false;
    }

    @Override
    public void scene(SceneType sceneType) {
        if(Gui.getInstance() != null)
            Gui.getInstance().update();
        if(sceneType.equals(SceneType.SCENE001))// BuildMode
            Build.getInstance().buildMode();
        else{
            ChatBox.getInstance().update();

            if(sceneType.equals(SceneType.SCENE000))
                m_gameData.update();
        }
    }

    @Override
    public void initialize() {
        Debug.out("All done :D");
    }

    @Override
    public LoadData load(SceneType nextScene) {

        if(m_enterBuildMode)
            Framework.getInstance().setNextScene(SceneType.SCENE001);

        if(nextScene.equals(SceneType.SCENE001) || m_enterBuildMode)
            return new LoadData("buildload.txt"){
                @Override
                public void lastMinuteLoads(GL2 gl){
                    if(m_gameData != null)
                        m_gameData.kill();
                    m_gameData = null;
                    Build.getInstance().initialize(gl);
                }
            };
        if(nextScene.equals(SceneType.SCENE000))
            return new LoadData("testload.txt"){
                @Override
                public void lastMinuteLoads(GL2 gl) {
                    //Setup the new shader.
                    {
                        RetroFile vs = new RetroFile("Files/Shaders/vertex.txt");
                        RetroFile fs = new RetroFile("Files/Shaders/fragment.txt");
                        ShaderLoader.defaultShaderColor3d = new ShaderProgram(vs.getAllData(), fs.getAllData(), gl);
                        ShaderLoader.defaultShaderColor3d.updateProjectionMatrix(gl, 45.0f, Framework.getInstance().getGameWidth(), Framework.getInstance().getGameHeight(), .1f, 1000.0f);
                        ShaderLoader.setActiveShader(ShaderLoader.defaultShaderColor3d);

                        // Sun you could say.
                        Light l = new Light();
                        l.setAmbient(new Vector3(.1f* 0.72f, .1f * .63f, .1f * .12f));
                        l.setDiffuse(new Vector3(0.8f * 0.72f, 0.8f * .63f, 0.8f * .12f));
                        l.setSpecular(new Vector3(0.72f, .63f, .12f));
                        l.setPosition(new Vector3(-0.25f, -1f, 0f));
                        l.setDirectional(true);

                        LightHandler.getInstance().addLight(l);
                    }
                    ChatBox.getInstance().addChatbox();

                    if(m_gameData != null)
                        m_gameData.kill();
                    m_gameData = new Hivity();
                    m_gameData.initialize(gl);
                }
            };
        return null;
    }

    @Override
    public void initializeGL(GL2 gl){
        gl.glClearColor(0.39f, 0.58f, 0.93f, 0.0f);
        // Load up with our systems and components.
        Debug.out("Loading systems and components.");

        new SystemVoxel();
        new LivingSystem();
        new InventoryUpdateSystem();
        new BuildInventoryUpdateSystem();
        new ParticleGeneratorSystem();
        new ParticleSystem();
        ComponentType.initializeComponent(LivingComponent.class);
        ComponentType.initializeComponent(ComponentVoxelModel.class);
        ComponentType.initializeComponent(WorldStorageComponent.class);
        ComponentType.initializeComponent(ActivePlayerComponent.class);
        ComponentType.initializeComponent(NoClipComponent.class);
        ComponentType.initializeComponent(InventoryComponent.class);
        ComponentType.initializeComponent(BuildInventoryComponent.class);
        ComponentType.initializeComponent(GuiComponent.class);
        ComponentType.initializeComponent(WorldItemComponent.class);
        ComponentType.initializeComponent(ParticleComponent.class);
        ComponentType.initializeComponent(ParticleGeneratorComponent.class);
        ComponentType.initializeComponent(AbilityRosterComponent.class);
    }

    public void finish(){
        if(m_gameData != null)
            m_gameData.kill();
        if(Gui.getInstance() != null)
            Gui.getInstance().kill();
    }
}
