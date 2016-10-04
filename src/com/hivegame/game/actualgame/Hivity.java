package com.hivegame.game.actualgame;

import com.hivegame.game.ability.AbilityRosterComponent;
import com.hivegame.game.ai.Action;
import com.hivegame.game.ai.ChaseAction;
import com.hivegame.game.ai.MoveAction;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.gui.BannerDisplay;
import com.hivegame.game.gui.Gui;
import com.hivegame.game.gui.InGameGui;
import com.hivegame.game.livingthing.*;
import com.hivegame.game.livingthing.species.IceGiant;
import com.hivegame.game.settings.GameSettings;
import com.hivegame.game.settings.InterfaceInitializer;
import com.hivegame.game.voxel.UtilVoxel;
import com.hivegame.game.world.World;
import com.jogamp.opengl.GL2;
import com.retro.engine.Framework;
import com.retro.engine.camera.Camera;
import com.retro.engine.camera.ThirdPersonCamera;
import com.retro.engine.debug.Debug;
import com.retro.engine.entity.Entity;
import com.retro.engine.event.Event;
import com.retro.engine.event.EventHandler;
import com.retro.engine.model.shader.ShaderLoader;
import com.retro.engine.model.shader.ShaderProgram;
import com.retro.engine.util.vector.Vector3;
import com.retro.engine.util.vector.Vector4;

import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Created by Michael on 8/29/2016.
 */
public class Hivity {
    // This will hold everything that will be used throughout a match or game

    private static Entity m_selectedUnit;

    private Team m_team;

    public Hivity(){
    }

    public static Entity getSelectedUnit(){
        return m_selectedUnit;
    }
    public static void setSelectedUnit(Entity unit){ m_selectedUnit = unit; }

    public void initialize(GL2 gl){

        m_team = TeamHandler.ICE_GIANT.getNewTeamObject();

        ThirdPersonCamera cam = new ThirdPersonCamera();
        cam.setLookat(new Vector3(0, 10, 0));
        cam.setMoveVelocity((float)Math.toRadians(1));
        cam.setLookDistance(25);
        cam.setMoveTarget(true);
        cam.setLookPitch((float)Math.toRadians(45));
        cam.setLookYaw((float)Math.toRadians(45));
        Framework.getInstance().getCamera().setUpCamera(cam);

        Timer.getInstance();

        InterfaceInitializer.Initialize();

        new InGameGui();
    }

    public void update(){
        if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_E)){
            EventHandler.getInstance().releaseKey(KeyEvent.VK_E);

            GameData.getInstance().initialize();

            GameData.getInstance().addTeam(new IceGiant(0));
            GameData.getInstance().addTeam(new IceGiant(1));

            GameData.getInstance().setupGame();
        }

        if(!GameData.getInstance().isInitialized())
            return;


        Timer.getInstance().tick();
        GameData.getInstance().update();

        // Set the world to generate around what the camera is looking at.
        Vector3 pos = new Vector3();
        if(Camera.getInstance() instanceof ThirdPersonCamera)
            pos = ((ThirdPersonCamera)Camera.getInstance()).getLookat();
        Vector3 microPos = GameData.getInstance().getWorld().convertToVoxelSpaceFlat(pos);
        GameData.getInstance().getWorld().loadChunks(microPos.getX(), microPos.getZ());

        if(GameSettings.g_currentControlScheme.equals(GameSettings.c_controlSchemeMouseMove))
            controlSchemeMouseMove();
        else if(GameSettings.g_currentControlScheme.equals(GameSettings.c_controlSchemeKeyboardMove))
            controlSchemeKeyboard();

        controlSchemeAbilities();

        if(m_selectedUnit != null && GameSettings.g_alwaysLockCamera){
            Camera cam = Camera.getInstance();
            if(cam instanceof ThirdPersonCamera){
                ThirdPersonCamera c = (ThirdPersonCamera)cam;
                c.setLookat(LivingUtil.getLivingComponent(m_selectedUnit).getPosition().toVector3());
            }
        }
    }

    public void kill(){
        GameData.getInstance().kill();
        UnitHandler.getInstance().kill();
        Gui.destroyGui();
    }

    public void controlSchemeAbilities(){
        if(m_selectedUnit == null || !LivingUtil.getLivingComponent(m_selectedUnit).isIdle())
            return;
        AbilityRosterComponent abi = (AbilityRosterComponent)m_selectedUnit.get(AbilityRosterComponent.class);

        // Get the mouse position to world coordinates.
        Vector3 arr = ShaderProgram.unProject(ShaderLoader.defaultShaderColor3d.getProjectionMatrix(), Camera.getInstance().getCameraLookMatrixWithPosition(-Camera.getInstance().getYaw(), -Camera.getInstance().getPitch()),
                new Vector3(EventHandler.getInstance().getMousePosition().getX(), EventHandler.getInstance().getMousePosition().getY(), 0));
        Vector4 vox = UtilVoxel.getVoxelLookAt(GameData.getInstance().getWorld(), arr.getX(), arr.getY(), arr.getZ(), 600, Camera.getInstance().getPitch(), Camera.getInstance().getYaw());
        vox.setX(vox.getX());
        vox.setY(vox.getY());
        vox.setZ(vox.getZ());
        vox.modY(1);


        /** Make sure they have enough abilities to check this far. */
        if(abi.getAmountOfAbilities() < 1)
            return;

        if((GameSettings.c_abilityOne == 0 && EventHandler.getInstance().isMouseL()) || (GameSettings.c_abilityOne == 1 && EventHandler.getInstance().isMouseR()) ||
                EventHandler.getInstance().isKeyPressed(GameSettings.c_abilityOne))
        {
            EventHandler.getInstance().releaseKey(GameSettings.c_abilityOne);
            abi.getAbility(0).useAbility(m_selectedUnit, vox);
        }
        /** Make sure they have enough abilities to check this far. */
        if(abi.getAmountOfAbilities() < 2)
            return;
        if((GameSettings.c_abilityTwo == 0 && EventHandler.getInstance().isMouseL()) || (GameSettings.c_abilityTwo == 1 && EventHandler.getInstance().isMouseR()) ||
                EventHandler.getInstance().isKeyPressed(GameSettings.c_abilityTwo))
        {
            EventHandler.getInstance().releaseKey(GameSettings.c_abilityTwo);
            abi.getAbility(1).useAbility(m_selectedUnit, vox);
        }

        /** Make sure they have enough abilities to check this far. */
        if(abi.getAmountOfAbilities() < 3)
            return;
        if((GameSettings.c_abilityThree == 0 && EventHandler.getInstance().isMouseL()) || (GameSettings.c_abilityThree == 1 && EventHandler.getInstance().isMouseR()) ||
                EventHandler.getInstance().isKeyPressed(GameSettings.c_abilityThree))
        {
            EventHandler.getInstance().releaseKey(GameSettings.c_abilityThree);
            abi.getAbility(2).useAbility(m_selectedUnit, vox);
        }

        /** Make sure they have enough abilities to check this far. */
        if(abi.getAmountOfAbilities() < 4)
            return;
        if((GameSettings.c_abilityFour == 0 && EventHandler.getInstance().isMouseL()) || (GameSettings.c_abilityFour == 1 && EventHandler.getInstance().isMouseR()) ||
                EventHandler.getInstance().isKeyPressed(GameSettings.c_abilityFour))
        {
            EventHandler.getInstance().releaseKey(GameSettings.c_abilityFour);
            abi.getAbility(3).useAbility(m_selectedUnit, vox);
        }

        /** Make sure they have enough abilities to check this far. */
        if(abi.getAmountOfAbilities() < 5)
            return;
        if((GameSettings.c_abilityFive == 0 && EventHandler.getInstance().isMouseL()) || (GameSettings.c_abilityFive == 1 && EventHandler.getInstance().isMouseR()) ||
                EventHandler.getInstance().isKeyPressed(GameSettings.c_abilityFive))
        {
            EventHandler.getInstance().releaseKey(GameSettings.c_abilityFive);
            abi.getAbility(4).useAbility(m_selectedUnit, vox);
        }
    }

    public void controlSchemeKeyboard(){
        if(m_selectedUnit == null) {
            return;
        }else
            ((ThirdPersonCamera)Camera.getInstance()).setCanMove(false);

        if(!LivingUtil.getLivingComponent(m_selectedUnit).isIdle())
            return;

        // Get the vector for whatever direction camera is facing.

        ThirdPersonCamera cam = (ThirdPersonCamera)Camera.getInstance();
        Vector3 dir = cam.getViewDirection();

        LivingComponent lc = LivingUtil.getLivingComponent(m_selectedUnit);

        if(EventHandler.getInstance().isKeyPressed(GameSettings.c_moveCharacterJump))
        {
            EventHandler.getInstance().releaseKey(GameSettings.c_moveCharacterJump);
            lc.jump();
        }

        float angle = 0; // start with degrees.

        if(EventHandler.getInstance().isKeyPressed(GameSettings.c_moveCharacterUp)){
            angle = 180;
            if(EventHandler.getInstance().isKeyPressed(GameSettings.c_moveCharacterLeft)){
                angle += 45;
            }else if(EventHandler.getInstance().isKeyPressed(GameSettings.c_moveCharacterRight)){
                angle -= 45;
            }
        }else if(EventHandler.getInstance().isKeyPressed(GameSettings.c_moveCharacterDown)){
            angle = 0;
            if(EventHandler.getInstance().isKeyPressed(GameSettings.c_moveCharacterLeft)){
                angle -= 45;
            }else if(EventHandler.getInstance().isKeyPressed(GameSettings.c_moveCharacterRight)){
                angle += 45;
            }
        }else if(EventHandler.getInstance().isKeyPressed(GameSettings.c_moveCharacterLeft)){
            angle = 270;
        }else if(EventHandler.getInstance().isKeyPressed(GameSettings.c_moveCharacterRight)){
            angle = 90;
        }else {
            lc.noMove();
            return;
        }
        lc.move((float)Math.toRadians(angle) + GameSettings.c_zeroAngle);

    }

    public void controlSchemeMouseMove(){
        if(EventHandler.getInstance().isMouseR() && m_selectedUnit != null){
            EventHandler.getInstance().releaseMouseR();

            Vector3 arr = ShaderProgram.unProject(ShaderLoader.defaultShaderColor3d.getProjectionMatrix(), Camera.getInstance().getCameraLookMatrixWithPosition(-Camera.getInstance().getYaw(), -Camera.getInstance().getPitch()),
                    new Vector3(EventHandler.getInstance().getMousePosition().getX(), EventHandler.getInstance().getMousePosition().getY(), 0));

            Vector4 vox = UtilVoxel.getVoxelLookAt(GameData.getInstance().getWorld(), arr.getX(), arr.getY(), arr.getZ(), 600, Camera.getInstance().getPitch(), Camera.getInstance().getYaw());
            vox.setX(vox.getX()*GameData.getInstance().getWorld().getVoxelSize());
            vox.setY(LivingUtil.getLivingComponent(m_selectedUnit).getPosition().getY());
            vox.setZ(vox.getZ()*GameData.getInstance().getWorld().getVoxelSize());

            Action nm = new MoveAction(LivingUtil.getLivingComponent(m_selectedUnit).getPosition().toVector3(), vox);

            if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_SHIFT))
                LivingUtil.getLivingComponent(m_selectedUnit).addAction(nm);
            else
                LivingUtil.getLivingComponent(m_selectedUnit).setCurrentAction(nm, true);

            ChatBox.getInstance().addMessage("Ant is moving...", null);

        }

        if(EventHandler.getInstance().isMouseL())
        {
            EventHandler.getInstance().releaseMouseL();
            /*ChatBox.getInstance().addMessage(ShaderProgram.getWorldPoint(new Vector3(EventHandler.getInstance().getMouseX(), EventHandler.getInstance().getMouseY(), 1f),
                    Camera.getInstance().getCameraLookMatrix(), ShaderLoader.defaultShaderColor3d.getProjectionMatrix()).toString(), null);*/
            Vector3 arr = ShaderProgram.unProject(ShaderLoader.defaultShaderColor3d.getProjectionMatrix(), Camera.getInstance().getCameraLookMatrixWithPosition(-Camera.getInstance().getYaw(), -Camera.getInstance().getPitch()),
                    new Vector3(EventHandler.getInstance().getMousePosition().getX(), EventHandler.getInstance().getMousePosition().getY(), 0));

            Vector4 vox = UtilVoxel.getVoxelLookAt(GameData.getInstance().getWorld(), arr.getX(), arr.getY(), arr.getZ(), 600, Camera.getInstance().getPitch(), Camera.getInstance().getYaw());
            vox.setX(vox.getX() * GameData.getInstance().getWorld().getVoxelSize());
            vox.setY(vox.getY() * GameData.getInstance().getWorld().getVoxelSize());
            vox.setZ(vox.getZ() * GameData.getInstance().getWorld().getVoxelSize());
            vox.modY(1);
            List<Entity > ents = UnitHandler.getInstance().getUnits(vox);
            if(ents.size() > 0) {
                if(m_selectedUnit != null)
                    LivingUtil.getLivingComponent(m_selectedUnit).deselect();
                m_selectedUnit = ents.get(0);
                LivingUtil.getLivingComponent(m_selectedUnit).select();
                ChatBox.getInstance().addMessage(vox.toString() + "\n" + arr.toString() + Selection.getInformation((LivingComponent) m_selectedUnit.get(LivingComponent.class), GameData.getInstance().getWorld()), null);
            }
            else {
                if(m_selectedUnit != null)
                    LivingUtil.getLivingComponent(m_selectedUnit).deselect();
                m_selectedUnit = null;
            }
        }
    }
}
