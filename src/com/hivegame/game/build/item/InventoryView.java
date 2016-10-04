package com.hivegame.game.build.item;

import com.retro.engine.Framework;
import com.retro.engine.debug.Debug;
import com.retro.engine.defaultcomponent.ComponentCollision;
import com.retro.engine.defaultcomponent.ComponentImage;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.defaultcomponent.ComponentText;
import com.retro.engine.entity.Entity;
import com.retro.engine.event.EventHandler;
import com.retro.engine.repository.ImageRepository;
import com.retro.engine.util.collision.UtilCollision;
import com.retro.engine.util.vector.Vector2;
import com.hivegame.game.build.BuildInventoryComponent;
import com.hivegame.game.build.ui.GuiComponent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by Michael on 8/4/2016.
 */
public class InventoryView {

    public static final float c_guiInventoryItemSize = 64;
    public static final float c_guiInventoryItemSpace= 7;

    public static final float c_guiInventoryDefaultX = 100;
    public static final float c_guiInventoryDefaultY = 200;

    private static HashMap<InventoryComponent, InventoryView> m_openInventories = new HashMap<>();

    private Entity[] m_itemBack;
    private Entity[] m_itemImage;

    private Entity m_colorSlider;
    private Entity m_currentColor;

    private Entity m_itemHoverText;

    private InventoryView(InventoryComponent inv){
        m_itemBack = new Entity[inv.getHeight() * inv.getWidth()];
        m_itemImage = new Entity[inv.getHeight() * inv.getWidth()];

        m_itemHoverText = new Entity();
        m_itemHoverText.add(new ComponentText("", "alphabet", 2));
        m_itemHoverText.add(new ComponentPosition(0,0));

        if(inv instanceof BuildInventoryComponent) {
            m_colorSlider = new Entity();
            m_currentColor = new Entity();
        }
    }

    public void updateMouseHover(Vector2 mp, InventoryComponent inv){
        ComponentPosition pos = (ComponentPosition)m_itemHoverText.get(ComponentPosition.class);
        pos.setX(mp.getX());
        pos.setY(mp.getY());
        UtilCollision.ActiveCollision col = UtilCollision.getCollision(EventHandler.getInstance().getMouseEntity());
        if (col != null) {
            Entity ent = col.getEntityTwo();
            if (ent.has(GuiComponent.class)) {
                GuiComponent info = ((GuiComponent) ent.get(GuiComponent.class));
                if(info.getInfoY() == 2 && EventHandler.getInstance().isMouseL()){
                    ComponentPosition p = (ComponentPosition)ent.get(ComponentPosition.class);
                    // This should be the color slider.
                    float width = p.getWidth();
                    float height= p.getHeight();
                    Vector2 nmp = new Vector2(mp.getX() - p.getX(), mp.getY() - p.getY());
                    Debug.out(nmp.toString());
                    BufferedImage img = ImageRepository.getInstance().getExistingImage("colorpickertriangle");
                    nmp.setX(nmp.getX() * img.getWidth()/width);
                    nmp.setY(nmp.getY() * img.getHeight()/height);

                    BuildInventoryComponent in = (BuildInventoryComponent)inv;
                    Color c = in.getBuildColor().toColor();
                    try {
                        c = new Color(img.getRGB((int) nmp.getX(), (int) nmp.getY()));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    in.getBuildColor().setRed(c.getRed());
                    in.getBuildColor().setGreen(c.getGreen());
                    in.getBuildColor().setBlue(c.getBlue());

                    Debug.out(in.getBuildColor().toString());

                    ComponentPosition pp = (ComponentPosition)m_currentColor.get(ComponentPosition.class);
                    pp.setX(mp.getX()-5);
                    pp.setY(mp.getY()-5);

                }else if(info.getInfoY() != 2) {
                    Item i;
                    i = inv.getItemInSlot(info.getInfoX());
                    if (i != null)
                        ((ComponentText) m_itemHoverText.get(ComponentText.class)).getTextObject().setText(i.getName() + "  x" + i.getAmount() + "\n" +
                                i.getDescription());
                    if (EventHandler.getInstance().isMouseL()) {
                        inv.setCurrentSelection(info.getInfoX());
                        InventoryView.reopenInventoryIfOpen(inv, InventoryView.c_guiInventoryDefaultX, InventoryView.c_guiInventoryDefaultY);
                        Debug.out("Doing gui thing.");
                    }
                }
            }
        }else
            ((ComponentText)m_itemHoverText.get(ComponentText.class)).getTextObject().setText(" ");
    }


    private static InventoryView getInventoryView(InventoryComponent inv){
        if(m_openInventories.get(inv) == null){
            InventoryView iv = new InventoryView(inv);
            m_openInventories.put(inv, iv);
            return iv;
        }
        return m_openInventories.get(inv);
    }

    public static InventoryView openInventory(InventoryComponent inv, float x, float y){// Inventory to open as well as the position to open it at.
        InventoryView invView = m_openInventories.get(inv);
        if(invView != null)
        {
            return invView;
        }
        invView = InventoryView.getInventoryView(inv);
        if(invView != null){
            for(int i=0;i<invView.m_itemBack.length;i++) {
                invView.m_itemBack[i] = new Entity();
                if(i == inv.getCurrentSelection())
                    invView.m_itemBack[i].add(new ComponentImage("activehotbar"));
                else
                    invView.m_itemBack[i].add(new ComponentImage("hotbar"));
                invView.m_itemBack[i].add(new ComponentPosition(i%inv.getWidth()*(c_guiInventoryItemSize + c_guiInventoryItemSpace) +x,
                        (float)Math.floor(i/inv.getWidth())* (c_guiInventoryItemSize + c_guiInventoryItemSpace) + y, c_guiInventoryItemSize, c_guiInventoryItemSize));
                invView.m_itemBack[i].add(new ComponentCollision(true));
                invView.m_itemBack[i].add(new GuiComponent(i, 0));
                Framework.getInstance().getEntityStorage().addEntity(invView.m_itemBack[i]);
            }
            for(int i=0;i<invView.m_itemImage.length;i++) {
                if(inv.getItemInSlot(i) == null)
                    continue;
                invView.m_itemImage[i] = new Entity();
                invView.m_itemImage[i].add(new ComponentImage("" + inv.getItemInSlot(i).getImageName()));
                invView.m_itemImage[i].add(new ComponentPosition(i%inv.getWidth()*(c_guiInventoryItemSize + c_guiInventoryItemSpace) +x,
                        (float)Math.floor(i/inv.getWidth())* (c_guiInventoryItemSize + c_guiInventoryItemSpace) + y, c_guiInventoryItemSize, c_guiInventoryItemSize));
                Framework.getInstance().getEntityStorage().addEntity(invView.m_itemImage[i]);
            }

            if(inv instanceof BuildInventoryComponent){
                invView.m_currentColor.add(new ComponentImage("crosshair"));
                invView.m_currentColor.add(new ComponentPosition(x +100, y + inv.getHeight()*c_guiInventoryItemSize + 100, 10, 10));
                invView.m_colorSlider.add(new ComponentImage("colorpickertriangle"));
                invView.m_colorSlider.add(new ComponentPosition(x + 100, y + inv.getHeight()*c_guiInventoryItemSize + 100, 250, 250));
                invView.m_colorSlider.add(new ComponentCollision());
                invView.m_colorSlider.add(new GuiComponent(0, 2));

                Framework.getInstance().getEntityStorage().addEntity(invView.m_colorSlider);
                Framework.getInstance().getEntityStorage().addEntity(invView.m_currentColor);
            }

            Framework.getInstance().getEntityStorage().addEntity(invView.m_itemHoverText);
        }
        return invView;
    }

    public static boolean isInventoryOpen(InventoryComponent inv){
        return m_openInventories.get(inv) != null;
    }

    public static void closeInventory(InventoryComponent inv){
        InventoryView invView = m_openInventories.get(inv);
        if(invView != null){
            // Destroy everything and remove entities.
            for(Entity e : invView.m_itemImage)
                Framework.getInstance().getEntityStorage().removeEntity(e, true);
            for(Entity e : invView.m_itemBack)
                Framework.getInstance().getEntityStorage().removeEntity(e, true);
            if(inv instanceof  BuildInventoryComponent)
            {
                Framework.getInstance().getEntityStorage().removeEntity(invView.m_currentColor, true);
                Framework.getInstance().getEntityStorage().removeEntity(invView.m_colorSlider, true);
            }
            Framework.getInstance().getEntityStorage().removeEntity(invView.m_itemHoverText, true);
            m_openInventories.remove(inv);
        }
    }

    public static void reopenInventory(InventoryComponent inv, float x, float y){
        if(isInventoryOpen(inv)){
            closeInventory(inv);
        }
        openInventory(inv, x, y);
    }
    public static void reopenInventoryIfOpen(InventoryComponent inv, float x, float y){
        if(isInventoryOpen(inv)){
            closeInventory(inv);
            openInventory(inv, x, y);
        }
    }
}
