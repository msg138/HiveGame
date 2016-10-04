package com.hivegame.game.actualgame;

import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.world.World;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;

import java.util.List;

/**
 * Created by Michael on 9/1/2016.
 */
public class Selection {



    public static List<Entity> getSelction(World w, Vector3 start, Vector3 end){
        return null;
    }

    public static String getInformation(LivingComponent lc, World w){
        if(lc == null)
            return "NIL";
        String data = "Species: ";
        data += lc.getSpecies().getName() + "\n";
        data += "Health: " + lc.getHealth() + " / " + lc.getMaxHealth() + "\n";

        data += "Action: " + lc.getCurrentAction().getActionName() +" , " + lc.getCurrentAction().getActionProgress(lc, w)*100 + "%";


        return data;
    }
}
