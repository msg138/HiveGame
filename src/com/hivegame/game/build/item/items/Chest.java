package com.hivegame.game.build.item.items;

import com.hivegame.game.build.item.InventoryComponent;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;
import com.retro.engine.util.vector.Vector3;
import com.hivegame.game.build.item.Item;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.voxel.ComponentVoxelModel;
import com.hivegame.game.voxel.UtilVoxel;
import com.hivegame.game.voxel.Voxel;
import com.hivegame.game.world.WorldItem;
import com.hivegame.game.world.WorldItemComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 8/24/2016.
 */
public class Chest {

    public static Entity generateChest(Vector3 pos, Item[] items){
        Entity e = new Entity();
        ComponentVoxelModel vox = new ComponentVoxelModel(-.5f*.1f, -9.25f * 0.1f, 1 * 0.1f, 2f);
        vox.setSizeMatters(false);
        List<Voxel> v = UtilVoxel.loadFromFile("chest.spv");
        for(Voxel vv : v)
            vox.addVoxel(vv, false);
        vox.setScale(0.1f);
        vox.resetModel();
        e.add(vox);
        e.add(new ComponentPosition(pos.getX(), pos.getY(), pos.getZ()));

        String chestName = "Chest" + Math.random() + "\n";
        for(Item i : items)
            chestName += i.getName() +"-"+i.getDescription()+"\n";

        e.add(new WorldItemComponent(chestName, new WorldChest(items)));
        return e;
    }

    public static class WorldChest extends WorldItem{

        private List<Item> m_items;

        public WorldChest(){
            super();
            m_items = new ArrayList<>();
        }
        public WorldChest(Item[] items){
            this();
            for(Item i : items)
                addItem(i);
        }
        public void addItem(Item i){
            m_items.add(i);
        }

        @Override
        public boolean onRightClick(Entity user){

            InventoryComponent inv = Player.getInventory(user);
            if(inv != null) {

                for(int i=0;i<m_items.size();i++)
                {
                    if(inv.addItem(m_items.get(i))){
                        m_items.remove(i);
                        i--;
                        continue;
                    }else
                        break;
                }

                if(m_items.size() <= 0)
                    destroy();
            }
            return true;
        }
    }
}
