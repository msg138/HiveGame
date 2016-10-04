package com.hivegame.game.build.ui;

import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.build.item.*;
import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.build.player.Player;
import com.hivegame.game.world.World;
import com.hivegame.game.world.WorldItem;
import com.hivegame.game.world.WorldItemComponent;
import com.retro.engine.Framework;
import com.retro.engine.camera.Camera;
import com.retro.engine.defaultcomponent.*;
import com.retro.engine.entity.Entity;
import com.retro.engine.entity.EntityStorage;
import com.retro.engine.event.EventHandler;
import com.retro.engine.model.RawModel;
import com.retro.engine.util.vector.Vector3;
import com.retro.engine.util.vector.Vector4;
import com.hivegame.game.build.player.NoClipComponent;
import com.hivegame.game.voxel.UtilVoxel;

import java.awt.*;
import java.util.List;

/**
 * Created by Michael on 8/2/2016.
 */
public class BuildGui {

    private static BuildGui m_instance;

    private Entity m_crosshair;
    private Entity m_worldPosition;

    private Entity m_currentItem;
    private Entity m_currentItemBack;

    private boolean m_lookingAtItem;

    private BuildGui(){

        m_lookingAtItem = false;

        m_instance = this;
    }

    public static BuildGui getInstance(){
        if(m_instance == null)
            new BuildGui();
        return m_instance;
    }

    public void loadDefault(){

        m_currentItem = new Entity();
        m_currentItem.add(new ComponentPosition(Framework.getInstance().getGameWidth() - 40, Framework.getInstance().getGameHeight() - 40, 32, 32));

        m_currentItemBack = new Entity();
        m_currentItemBack.add(m_currentItem.get(ComponentPosition.class));
        m_currentItemBack.add(new ComponentImage("activehotbar"));

        m_crosshair = new Entity();
        m_crosshair.add(new ComponentPosition(Framework.getInstance().getGameWidth()/2f-5, Framework.getInstance().getGameHeight()/2f-5, 10, 10));
        m_crosshair.add(new ComponentImage("crosshair"));

        m_worldPosition = new Entity();
        m_worldPosition.add(new ComponentPosition(100, 0, 10, 10));
        m_worldPosition.add(new ComponentText("", "alphabet", 3.0f));

        ChatBox.getNewInstance();
    }

    public void kill(){
        if(m_currentItem != null)
            Framework.getInstance().getEntityStorage().removeEntity(m_currentItem, true);
        if(m_currentItemBack != null)
            Framework.getInstance().getEntityStorage().removeEntity(m_currentItemBack, true);
        if(m_crosshair != null)
            Framework.getInstance().getEntityStorage().removeEntity(m_crosshair, true);
        if(m_worldPosition != null)
            Framework.getInstance().getEntityStorage().removeEntity(m_worldPosition, true);

        m_currentItem = null;
        m_currentItemBack = null;
        m_crosshair = null;
        m_worldPosition = null;

        ChatBox.getInstance().kill();
    }

    public void addDefault(){

        Framework.getInstance().getEntityStorage().addEntity(m_crosshair);
        Framework.getInstance().getEntityStorage().addEntity(m_worldPosition);

        Framework.getInstance().getEntityStorage().addEntity(m_currentItemBack);
        Framework.getInstance().getEntityStorage().addEntity(m_currentItem);

        ChatBox.getInstance().addChatbox();
    }

    public void bringUp(){
        /**
         * Bring the GUI to the front of the display.
         */
        {
            EntityStorage es = Framework.getInstance().getEntityStorage();

            es.bringEntityToFront(m_crosshair);
            es.bringEntityToFront(m_worldPosition);

            es.bringEntityToFront(m_currentItemBack);
            es.bringEntityToFront(m_currentItem);

            ChatBox.getInstance().bringUp();
        }
    }

    public void updateGUI(Entity p, World w) {
        if(m_currentItem == null || m_currentItemBack == null || p == null || Player.getInventory(p) == null || w == null)
            return;

        if(!InventoryView.isInventoryOpen(Player.getInventory(p)))
            bringUp();

        InventoryComponent inv = Player.getInventory(p);
        Item item = inv.getItemInSlot(inv.getCurrentSelection());
        if(item != null){
            if(!m_currentItem.has(ComponentImage.class))
            {
                m_currentItem.add(new ComponentImage(item.getImageName()));
            }else {
                ComponentImage img = (ComponentImage) m_currentItem.get(ComponentImage.class);
                if (!img.getImage().getImageName().equals(item.getImageName())) {
                    m_currentItem.remove(ComponentImage.class);
                    m_currentItem.add(new ComponentImage(item.getImageName()));
                }
            }
        }else if(m_currentItem.has(ComponentImage.class))
            m_currentItem.remove(ComponentImage.class);


        if(m_worldPosition != null && m_worldPosition.has(ComponentText.class)) {
            if (p.has(NoClipComponent.class)) {
                Vector3 pos = w.convertToVoxelSpaceFlat(new Vector3(Camera.getInstance().getPosition()));
                Vector3 mid = w.getMiddleChunkVoxelFlat();
                pos.modX(-mid.getX());
                pos.modZ(-mid.getZ());
                if(!m_lookingAtItem)
                    //((ComponentText) m_worldPosition.get(ComponentText.class)).getTextObject().setText("B: " + pos.toString());
                    ((ComponentText) m_worldPosition.get(ComponentText.class)).getTextObject().setText(World.getDirection(Camera.getInstance().getYaw()) + " FPS:" + Framework.getInstance().getCurrentFPS() + " RFPS:" + Framework.getInstance().getCurrentRenderFPS());
            } else {

                Vector3 vPos = w.convertToVoxelSpaceFlat(new Vector3(Player.getPosition(p)));
                if(!m_lookingAtItem)
                    //((ComponentText) m_worldPosition.get(ComponentText.class)).getTextObject().setText(vPos.toString());
                    ((ComponentText) m_worldPosition.get(ComponentText.class)).getTextObject().setText(World.getDirection(Camera.getInstance().getYaw()).substring(0, 1) + " " + w.getBiome(vPos) + " " + vPos.toString());

                m_lookingAtItem = false;

                // Worldly item things
                List<Entity> items = w.getWorldItemsClose(w.convertToVoxelSpaceFlat(new Vector3(Camera.getInstance().getPosition())), 5.0f);
                if (items.size() <= 0)
                    return;

                List<Vector4> voxels = Player.getLiving(p).getVoxelsLookAt(w, 5f, Camera.getInstance().getPitch(), Camera.getInstance().getYaw());
                for (Vector4 v : voxels) {
                    if(m_lookingAtItem)
                        break;
                    for (Entity e : items) {
                        Vector3 epos = w.convertToVoxelSpaceFlat(new Vector3(Player.getPosition(e)));
                        if (v.getX() == epos.getX() && v.getY() == epos.getY() && v.getZ() == epos.getZ()) {
                            WorldItemComponent wi = WorldItem.getWorldItemComponent(e);
                            if(wi.getWorldItem().isDead())
                            {
                                w.removeWorldlyItem(e);
                                continue;
                            }
                            if (!((ComponentText) m_worldPosition.get(ComponentText.class)).getTextObject().getText().equals(wi.getName()))
                                ((ComponentText) m_worldPosition.get(ComponentText.class)).getTextObject().setText(wi.getName());
                            if (EventHandler.getInstance().isMouseL()) {
                                wi.onLeftClick(p);
                            }
                            if (EventHandler.getInstance().isMouseR()) {
                                wi.onRightClick(p);
                            }
                            m_lookingAtItem = true;
                            break;
                        }
                    }
                }
            }
        }
    }
}
