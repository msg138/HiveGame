package com.hivegame.game.build;

import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.build.config.Config;
import com.hivegame.game.build.item.InventoryComponent;
import com.hivegame.game.build.item.InventoryView;
import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.livingthing.WorldStorageComponent;
import com.hivegame.game.build.player.ActivePlayerComponent;
import com.hivegame.game.build.player.NoClipComponent;
import com.hivegame.game.build.ui.BuildGui;
import com.hivegame.game.voxel.UtilVoxel;
import com.hivegame.game.voxel.Voxel;
import com.hivegame.game.world.World;
import com.jogamp.opengl.GL2;
import com.retro.engine.Framework;
import com.retro.engine.camera.Camera;
import com.retro.engine.camera.FirstPersonCamera;
import com.retro.engine.defaultcomponent.ComponentColor;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;
import com.retro.engine.event.EventHandler;
import com.retro.engine.light.Light;
import com.retro.engine.light.LightHandler;
import com.retro.engine.model.AnimatedRawModel;
import com.hivegame.game.build.animation.PreviewItem;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.voxel.ComponentVoxelModel;
import com.retro.engine.model.shader.ShaderLoader;
import com.retro.engine.model.shader.ShaderProgram;
import com.retro.engine.util.file.RetroFile;
import com.retro.engine.util.vector.Vector3;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static java.awt.event.KeyEvent.VK_SPACE;

/**
 * Created by Michael on 8/9/2016.
 */
public class Build {

    public static String g_currentFile = "tempVox.txt";
    public static boolean g_keepLower = false;
    public static boolean g_animationMode = false;

    public static int g_currentModel = 0;

    public static float g_degreeInterval = 3;
    public static float g_positionInterval = 0.1f;
    public static float g_animateInterval = 1f;
    public static float g_scaleInterval = 0.01f;

    public static List<String> g_modelNames = new ArrayList<>();

    private static AnimatedRawModel g_model = null;

    private static float m_curFrame = 0;

    private static Entity m_modelE;

    private static Entity m_origin;

    private static Build m_instance;

    private Entity m_player;

    private World m_w;

    private BuildGui m_gui;

    private Build(){
        m_instance = this;
    }
    public static Build getInstance(){
        if(m_instance == null)
            new Build();
        return m_instance;
    }

    public static AnimatedRawModel getAnimation(){
        if(g_model == null)
            g_model = new AnimatedRawModel(0);
        return g_model;
    }
    public static void setAnimation(AnimatedRawModel model){
        g_model = model;
        if(m_modelE != null){
            ((ComponentVoxelModel)m_modelE.get(ComponentVoxelModel.class)).setModel(model);
        }
    }

    public static void updateAnimation(Entity p){
        if(!g_animationMode && m_modelE != null){
            Framework.getInstance().getEntityStorage().removeEntity(m_modelE, true);
            m_modelE = null;
        }else if(m_modelE == null && g_animationMode){
            m_modelE = new Entity();
            ComponentVoxelModel vox = new ComponentVoxelModel(0, 0, 0, 2f);
            vox.setModel(g_model);
            m_modelE.add(vox);

            Framework.getInstance().getEntityStorage().addEntity(m_modelE);

            g_modelNames.clear();
        }

        if(Player.getInventory(p).getItemInSlot(Player.getInventory(p).getCurrentSelection()) instanceof PreviewItem)
        {
            // Preview the animation
            m_curFrame += g_animateInterval;

            if(m_curFrame >= getAnimation().getMaxFrames())
                m_curFrame = 0;

            getAnimation().setCurrentFrame((int)Math.floor(m_curFrame));
        }
    }
    public void buildMode(){
        if(m_w == null || m_player == null || m_gui == null || Player.getInventory(m_player) == null)
            return;
        Build.updateAnimation(m_player);

        m_gui.updateGUI(m_player, m_w);
        Vector3 playerPos = m_w.convertToVoxelSpaceFlat(Player.getPositionVector(m_player));
        m_w.loadChunks(playerPos.getX(), playerPos.getZ());

        // Update the chatbox.
        if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_ENTER))
        {
            if(ChatBox.getInstance().isFocused()){
                ChatBox.getInstance().addMessage(EventHandler.getInstance().getInput(), m_player);
                EventHandler.getInstance().resetInput();
                ChatBox.getInstance().loseFocus();
                ChatBox.getInstance().clearInput();
                ((FirstPersonCamera)Camera.getInstance()).setCanRotate(true);
                ((FirstPersonCamera)Camera.getInstance()).setCanMove(true);
            }else{
                ChatBox.getInstance().gainFocus();
                ((FirstPersonCamera)Camera.getInstance()).setCanRotate(false);
                ((FirstPersonCamera)Camera.getInstance()).setCanMove(false);
            }
            EventHandler.getInstance().releaseKey(KeyEvent.VK_ENTER);
        }

        if(ChatBox.getInstance().isFocused()) {
            if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_ESCAPE)) {
                ChatBox.getInstance().loseFocus();
                EventHandler.getInstance().stopTakingInput();
                EventHandler.getInstance().resetInput();
                ChatBox.getInstance().setInput(EventHandler.getInstance().getInput());
            }else {
                EventHandler.getInstance().takeInput();
                ChatBox.getInstance().setInput(EventHandler.getInstance().getInput());
            }
            return;
        }else if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_SLASH))
        {
            ChatBox.getInstance().gainFocus();
            EventHandler.getInstance().setInput("/");

            ((FirstPersonCamera)Camera.getInstance()).setCanRotate(false);
            ((FirstPersonCamera)Camera.getInstance()).setCanMove(false);
        }
        else
            EventHandler.getInstance().stopTakingInput();

        // End

        if(EventHandler.getInstance().isKeyPressed(VK_SPACE)){
            Camera.getInstance().getPosition().modY(((FirstPersonCamera)Camera.getInstance()).getMoveVelocity()/1.5f);
        }
        if(EventHandler.getInstance().isKeyPressed(VK_SHIFT)){
            Camera.getInstance().getPosition().modY(-((FirstPersonCamera)Camera.getInstance()).getMoveVelocity()/1.5f);
        }

        if(EventHandler.getInstance().isKeyPressed(VK_E)){
            if(InventoryView.isInventoryOpen(Player.getInventory(m_player)))
                InventoryView.closeInventory(Player.getInventory(m_player));
            else
                InventoryView.openInventory(Player.getInventory(m_player), InventoryView.c_guiInventoryDefaultX, InventoryView.c_guiInventoryDefaultY);
            EventHandler.getInstance().releaseKey(VK_E);
        }

        {
            InventoryComponent inv = Player.getInventory(m_player);
            if (InventoryView.isInventoryOpen(inv)) {
                InventoryView iV = InventoryView.openInventory(inv, 0, 0);
                iV.updateMouseHover(EventHandler.getInstance().getMousePosition(), inv);
            }else if(EventHandler.getInstance().isMouseR()){
                if(inv.isItemInSlot(inv.getCurrentSelection())){
                    inv.getItemInSlot(inv.getCurrentSelection()).useItemRight(m_player);
                }
            }else if(EventHandler.getInstance().isMouseL()){
                if(inv.isItemInSlot(inv.getCurrentSelection())){
                    inv.getItemInSlot(inv.getCurrentSelection()).onLeftClick(m_player);
                }
            }
        }
        if(InventoryView.isInventoryOpen(Player.getInventory(m_player)) || ChatBox.getInstance().isFocused()) {
            ((FirstPersonCamera) Camera.getInstance()).setCanRotate(false);
            ((FirstPersonCamera) Camera.getInstance()).setCanMove(false);
        }else {
            ((FirstPersonCamera) Camera.getInstance()).setCanRotate(true);
            ((FirstPersonCamera) Camera.getInstance()).setCanMove(true);
        }

        EventHandler.getInstance().releaseMouseL();
        EventHandler.getInstance().releaseMouseR();

        ((FirstPersonCamera) Camera.getInstance()).setMoveVelocity(Player.getLiving(m_player).getMovementSpeed());
    }

    public void initialize(GL2 gl){
        FirstPersonCamera cam = new FirstPersonCamera(){
            @Override
            public boolean canMove(Vector3 newPos, float dir){
                if(m_player.has(NoClipComponent.class))
                    return true;
                Vector3 np = this.getMovePosition(1.0f, dir);//new Vector3(newPos.getX(), newPos.getY(), newPos.getZ());
                Voxel v = m_w.getVoxel(m_w.convertToVoxelSpaceFlat(np));
                if(v == null || !v.isCollidable()) {
                    np.modY(-Player.getLiving(m_player).getHeight()/1.25f);
                    v = m_w.getVoxel(m_w.convertToVoxelSpaceFlat(np));
                    if(v == null || !v.isCollidable()){
                        np.modY(Player.getLiving(m_player).getHeight()/1f);
                        v = m_w.getVoxel(m_w.convertToVoxelSpaceFlat(np));
                        if(v == null || !v.isCollidable()){
                            return true;
                        }
                    }
                }
                return false;
            }
            @Override
            public void onMove(Vector3 newPos, float dir){
            }
            @Override
            public void onRotate(float y, float p){
            }
        };
        cam.setMoveVelocity(0.12f);
        cam.setSensitivity(0.002f);
        Framework.getInstance().getCamera().setUpCamera(cam);

        {
            RetroFile vs = new RetroFile("Files/Shaders/vertex.txt");
            RetroFile fs = new RetroFile("Files/Shaders/fragment.txt");
            ShaderLoader.defaultShaderColor3d = new ShaderProgram(vs.getAllData(), fs.getAllData(), gl);
            ShaderLoader.defaultShaderColor3d.updateProjectionMatrix(gl, 45.0f, Framework.getInstance().getGameWidth(), Framework.getInstance().getGameHeight(), 1f, 250.0f);
            ShaderLoader.setActiveShader(ShaderLoader.defaultShaderColor3d);
            Light l = new Light();
            l.setAmbient(new Vector3(0.05f, 0.05f, 0.05f));
            l.setDiffuse(new Vector3(0.8f, 0.8f, 0.8f));
            l.setSpecular(new Vector3(1.0f, 1.0f, 1.0f));
            l.setPosition(new Vector3(-0.25f, -1f, 0f));
            l.setDirectional(true);

            LightHandler.getInstance().addLight(l);
        }

        ComponentPosition cp = new ComponentPosition();
        LivingComponent l = new LivingComponent(0,0,1,0,0,0,0,0, cp, null);
        l.setMaxJumps(2);
        m_player = Player.createPlayer(l, cp, m_w);
        m_player.add(new ActivePlayerComponent());
        Player.getLiving(m_player).setHeight(4f);
        Player.getLiving(m_player).setJumpSpeed(10f);


        m_w = new World(0l);
        m_w.generateFlat();
        m_w.generateSingleVoxel(false);

        m_gui = BuildGui.getInstance();

        m_gui.loadDefault();
        m_gui.addDefault();

        ChatBox.getInstance().addMessage("Welcome to build mode!", m_player);

        Vector3 v = m_w.getMiddleChunkVoxel();
        Camera.getInstance().getPosition().setX(v.getX());
        Camera.getInstance().getPosition().setY(v.getY() + m_w.getVoxelSize()*2f);
        Camera.getInstance().getPosition().setZ(v.getZ());

        m_player.add(new NoClipComponent());

        m_player.add(new BuildInventoryComponent());

        m_player.remove(WorldStorageComponent.class);
        m_player.add(new WorldStorageComponent(m_w));

        m_origin = new Entity();
        ComponentVoxelModel vox = new ComponentVoxelModel(0, 0, 0, 1f);
        vox.addVoxel(new Voxel(0, 0, 0, 1f, new ComponentColor(255, 0, 255)));
        vox.updateModel();
        m_origin.add(vox);
        Framework.getInstance().getEntityStorage().addEntity(m_origin);

        // Load the config defaults.
        Config.loadConfig(Config.g_defaultConfig);
    }
}
