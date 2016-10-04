package com.hivegame.game.gui;

import com.hivegame.game.ability.AbilityRosterComponent;
import com.hivegame.game.actualgame.Hivity;
import com.hivegame.game.actualgame.Timer;
import com.hivegame.game.settings.GameSettings;
import com.hivegame.game.settings.TimerSettings;
import com.retro.engine.Framework;
import com.retro.engine.debug.Debug;
import com.retro.engine.defaultcomponent.*;
import com.retro.engine.entity.Entity;

import java.awt.*;

/**
 * Created by Michael on 9/18/2016.
 */
public class InGameGui extends Gui{

    private static final int c_timeWheelSize = 150;

    private Entity m_detailsWindow;
    private Entity m_timeText;
    private Entity m_timeImage;

    private Entity[] m_abilityBack;
    private Entity[] m_abilityImage;
    private Entity[] m_abilityCover;

    private boolean m_bringUpNecessary = true;

    public InGameGui(){

        //m_detailsWindow = new Entity();

        m_timeText = new Entity();
        m_timeText.add(new ComponentPosition(300, 0));
        m_timeText.add(new ComponentText("GameTime: ", "alphabet", 2f));
        Framework.getInstance().getEntityStorage().addEntity(m_timeText);
        m_timeImage = new Entity();
        m_timeImage.add(new ComponentPosition(300, -.5f*c_timeWheelSize, c_timeWheelSize, c_timeWheelSize));
        m_timeImage.add(new ComponentImage("dnwheel"));
        ((ComponentImage)m_timeImage.get(ComponentImage.class)).getImage().setRotationPoint(new ComponentPosition(-c_timeWheelSize, -c_timeWheelSize));
        m_timeImage.add(new ComponentRotation());
        Framework.getInstance().getEntityStorage().addEntity(m_timeImage);

        m_abilityBack = new Entity[GameSettings.c_abilityCount];
        for(int i=0;i< GameSettings.c_abilityCount;i++) {
            m_abilityBack[i] = new Entity();
            m_abilityBack[i].add(new ComponentPosition(300 + i*70, Framework.getInstance().getGameHeight() - 70, 64, 64));
            m_abilityBack[i].add(new ComponentImage("hotbarslot"));

            Framework.getInstance().getEntityStorage().addEntity(m_abilityBack[i]);
        }

        setInstance(this);
    }

    private void initializeAbilityImages(){
        if(m_abilityImage != null){
            for(Entity e : m_abilityImage){
                if(e != null)
                    Framework.getInstance().getEntityStorage().removeEntity(e, true);
            }
            m_abilityImage = null;
        }
        if(Hivity.getSelectedUnit() == null)
            return;
        AbilityRosterComponent abi = (AbilityRosterComponent) Hivity.getSelectedUnit().get(AbilityRosterComponent.class);

        m_abilityImage = new Entity[GameSettings.c_abilityCount];
        for(int i=0;i< GameSettings.c_abilityCount;i++) {
            if(i >= abi.getAmountOfAbilities())
            {
                m_abilityImage[i] = null;
                continue;
            }
            m_abilityImage[i] = new Entity();
            m_abilityImage[i].add(new ComponentPosition(302 + i*70, Framework.getInstance().getGameHeight() - 68, 60, 60));
            m_abilityImage[i].add(new ComponentImage(abi.getAbility(i).getImageName()));

            Framework.getInstance().getEntityStorage().addEntity(m_abilityImage[i]);
        }
    }

    public void setInformation(String text){
        if(text.equals("")){
            if(m_detailsWindow != null){
                Framework.getInstance().getEntityStorage().removeEntity(m_detailsWindow, true);
                m_detailsWindow = null;
            }
        }else{
            if(m_detailsWindow == null){
                m_detailsWindow = new Entity();
                m_detailsWindow.add(new ComponentPosition(0, 0));
                m_detailsWindow.add(new ComponentText("Details: ", "alphabet", 1.4f));
                ((ComponentText)m_detailsWindow.get(ComponentText.class)).getTextObject().setColor(new ComponentColor(255, 255, 100));
            }
            ((ComponentText)m_detailsWindow.get(ComponentText.class)).getTextObject().setText(text);
        }
    }

    public void bringUp(){
        Framework.getInstance().getEntityStorage().bringEntityToFront(m_timeImage);
        Framework.getInstance().getEntityStorage().bringEntityToFront(m_timeText);
        for(int i=0;i< GameSettings.c_abilityCount;i++) {
            Framework.getInstance().getEntityStorage().bringEntityToFront(m_abilityBack[i]);


            if(m_abilityImage != null)
                if(m_abilityImage[i] != null)
                    Framework.getInstance().getEntityStorage().bringEntityToFront(m_abilityImage[i]);
        }

        m_bringUpNecessary = false;
    }

    public void update(){

        // Update ability images if the player exists.
        if(m_abilityImage == null && Hivity.getSelectedUnit() != null){
            initializeAbilityImages();
            m_bringUpNecessary = true;
        }

        //((ComponentRotation)m_timeImage.get(ComponentRotation.class)).setRotationZ( ((float)Timer.getInstance().getCurrentTime()) / TimerSettings.g_minutesInDay * (float)(2*Math.PI) );

        ((ComponentText)m_timeText.get(ComponentText.class)).getTextObject().setText("GameTime: " + Timer.getInstance().getCurrentTime());

        if(Timer.getInstance().getCurrentTime()%3 == 0)
            m_bringUpNecessary = true;

        // Bring up the banner if neccessary and update it.
        BannerDisplay.update();

        if(m_bringUpNecessary)
            bringUp();
    }

    public void kill(){
        if(m_timeText != null){
            Framework.getInstance().getEntityStorage().removeEntity(m_timeText, true);
            m_timeText = null;
        }
        if(m_timeImage != null){
            Framework.getInstance().getEntityStorage().removeEntity(m_timeImage, true);
            m_timeImage = null;
        }
        if(m_detailsWindow != null){
            Framework.getInstance().getEntityStorage().removeEntity(m_detailsWindow, true);
            m_detailsWindow = null;
        }
        if(m_abilityBack != null)
        {
            for(Entity e : m_abilityBack)
                Framework.getInstance().getEntityStorage().removeEntity(e, true);
            m_abilityBack = null;
        }
        if(m_abilityImage != null)
        {
            for(Entity e : m_abilityImage)
                if(e != null)
                    Framework.getInstance().getEntityStorage().removeEntity(e, true);
            m_abilityImage = null;
        }

        BannerDisplay.kill();
    }
}
