package com.hivegame.game.build.item;

import com.retro.engine.component.Component;
import com.retro.engine.debug.Debug;

/**
 * Created by Michael on 8/4/2016.
 */
public class InventoryComponent extends Component {

    private Item[] m_items;

    private int m_width;
    private int m_height;

    private int m_currentSelection;

    public InventoryComponent(int width, int height){
        m_width = width;
        m_height = height;
        m_currentSelection = 0;

        m_items = new Item[width*height];
    }

    public int getWidth(){
        return m_width;
    }
    public int getHeight(){
        return m_height;
    }

    public int getCurrentSelection(){
        return m_currentSelection;
    }
    public void setCurrentSelection(int cs){
        m_currentSelection = cs;
    }

    public Item getItemInSlot(int itemSlot){
        return m_items[itemSlot];
    }

    public boolean isItemInSlot(int itemSlot){
        return m_items[itemSlot] != null;
    }

    public boolean addItem(Item i){
        return addItem(i, -1);
    }

    public boolean addItem(Item item, int slot){
        if(slot == -1){
            if(item.isStackable())
                for(int i=0;i<getWidth()*getHeight(); i++){
                    if(m_items[i] == null)
                        continue;
                    if(m_items[i].getName().equals(item.getName())) {
                        m_items[i].setAmount(item.getAmount() + m_items[i].getAmount());
                        Debug.out("Added existing item.");
                        // Reset the inventory.
                        if (InventoryView.isInventoryOpen(this)){
                            InventoryView.reopenInventory(this, 0, 0);
                        }
                        return true;
                    }
                }
            for(int i=0;i<getWidth()*getHeight(); i++){
                if(m_items[i] == null){
                    m_items[i] = item;
                    Debug.out("Added new item.");
                    // Reset the inventory.
                    InventoryView.reopenInventoryIfOpen(this, 0, 0);
                    return true;
                }
            }
        }
        return false;
    }

    public void removeItem(int slot){
        m_items[slot] = null;
    }

    public boolean removeDeadItems(){
        for(int i=0;i<m_items.length;i++)
        {
            if(m_items[i] == null)
                continue;
            if(m_items[i].getAmount() <= 0)
            {
                m_items[i] = null;
                InventoryView.reopenInventoryIfOpen(this, 0, 0);
            }
        }
        while(!isItemInSlot(m_currentSelection)){
            if(m_currentSelection == 0)
                break;
            m_currentSelection--;
        }
        return true;
    }
}
