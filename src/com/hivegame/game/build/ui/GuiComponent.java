package com.hivegame.game.build.ui;

import com.retro.engine.component.Component;

/**
 * Created by Michael on 8/4/2016.
 */
public class GuiComponent extends Component{

    private int infoX, infoY;

    public GuiComponent(int x, int y){
        infoX = x;
        infoY = y;
    }
    public int getInfoX(){
        return infoX;
    }
    public int getInfoY(){
        return infoY;
    }
}
