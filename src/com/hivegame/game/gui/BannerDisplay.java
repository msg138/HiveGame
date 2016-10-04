package com.hivegame.game.gui;

import com.retro.engine.Framework;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.defaultcomponent.ComponentText;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.time.UtilTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 9/25/2016.
 */
public class BannerDisplay {

    private static List<String> messages = new ArrayList<>();

    private static Entity currentDisplay;

    private static long startTime = 0;
    private static long duration = 5000;

    public static void queueDisplay(String msg){
        messages.add(msg);
    }

    public static void update(){
        if(currentDisplay == null){
            if(messages.size() > 0){
                createNextDisplay();
            }
        }

        if(currentDisplay != null) {
            Framework.getInstance().getEntityStorage().bringEntityToFront(currentDisplay);

            if(UtilTime.mill() - startTime >= duration)
                kill();
        }
    }

    public static void createNextDisplay(){
        if(messages.size() <= 0)
            return;
        currentDisplay = new Entity();

        currentDisplay.add(new ComponentText(messages.get(0), "alphabet", 3f));
        currentDisplay.add(new ComponentPosition(Framework.getInstance().getGameWidth()/2f - ((ComponentText)currentDisplay.get(ComponentText.class)).getTextObject().getWidth()/2, 200));

        Framework.getInstance().getEntityStorage().addEntity(currentDisplay);
        messages.remove(0);

        startTime = UtilTime.mill();
    }

    public static void kill(){
        if(currentDisplay != null){
            Framework.getInstance().getEntityStorage().removeEntity(currentDisplay, true);
            currentDisplay = null;
        }
    }
}
