package com.hivegame.game.build.item;

import com.retro.engine.Framework;
import com.retro.engine.Messaging.RetroMessage;
import com.retro.engine.component.ComponentType;
import com.retro.engine.entity.Entity;
import com.retro.engine.model.RawModel;
import com.hivegame.game.voxel.UtilVoxel;

/**
 * Created by Michael on 8/4/2016.
 */
public class Item {

    public static final int c_itemInfiniteUses = -1;
    public static final int c_itemOneUse = -2;

    private String m_name;
    private String m_description;

    private String m_imageName;

    private boolean m_stackable;

    private int m_usesLeft;
    private int m_maxUses;

    private boolean m_destroyAfterUse = true;

    private int m_amount;

    private String m_components;// COmponents to make the item.

    private RawModel m_model;

    public Item(String name, String description, String components, boolean stackable, int usesLeft, int maxUses, int amount){
        m_name = name;
        m_description = description;
        m_components = components;

        m_stackable = stackable;

        m_usesLeft = usesLeft;
        m_maxUses = maxUses;
        m_amount = amount;

        m_imageName = "crosshair";

        m_model = generateModel();
    }

    protected RawModel generateModel(){
        /*ComponentVoxelModel vox = new ComponentVoxelModel(0, 0, 0, 2f);
        vox.addVoxel(new Voxel(0, 0, 0, 2f, new ComponentColor(255, 0, 255)), false);
        vox.resetModel();

        return vox.getModel();*/
        return UtilVoxel.getVoxelModel("block.spv");
    }

    public RawModel getVoxelModel(){
        return m_model;
    }

    public void useItemRight(Entity user) {
        if (m_usesLeft <= 0){
            Framework.getInstance().getSystemManager().handleMessage(new RetroMessage(Framework.getInstance().getSystemManager().getSystem(ComponentType.getComponentType(InventoryComponent.class)).getID(),
                    InventoryUpdateSystem.c_messageItemRemove, user));
            return;
        }
        if(onRightClick(user)){
            if(m_maxUses != c_itemInfiniteUses)
                m_usesLeft --;
        }
        if(m_usesLeft <= 0 && m_maxUses != c_itemInfiniteUses && destroyAfterUse())
        {
            removeItem();
            if(m_maxUses == c_itemOneUse)
                m_usesLeft = 1;
            Framework.getInstance().getSystemManager().handleMessage(new RetroMessage(Framework.getInstance().getSystemManager().getSystem(ComponentType.getComponentType(InventoryComponent.class)).getID(),
                    InventoryUpdateSystem.c_messageItemRemove, user));
        }
    }

    protected boolean onRightClick(Entity user){



        return false;
    }
    public boolean onLeftClick(Entity user){
        return false;
    }

    public boolean destroyAfterUse(){
        return m_destroyAfterUse;
    }
    public void setDestroyAfterUse(boolean m){
        m_destroyAfterUse = m;
    }

    public String getImageName(){
        return m_imageName;
    }

    public void setImageName(String im){
        m_imageName = im;
    }

    public String getName(){
        return m_name;
    }
    public String getDescription(){
        return m_description;
    }
    public String[] getComponentsList(){
        return m_components.split(" ");
    }

    public boolean isStackable(){
        return m_stackable;
    }

    public void addItem(){
        m_amount ++;
    }
    public void removeItem(){
        m_amount --;
    }

    public int getAmount(){
        return m_amount;
    }
    public void setAmount(int na){
        m_amount = na;
    }

    public int getUsesLeft(){
        return m_usesLeft;
    }
    public int getMaxUses(){
        return m_maxUses;
    }
}
