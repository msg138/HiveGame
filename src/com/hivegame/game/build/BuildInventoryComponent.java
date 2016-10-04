package com.hivegame.game.build;

import com.hivegame.game.build.animation.*;
import com.hivegame.game.chat.ChatBox;
import com.hivegame.game.build.item.InventoryComponent;
import com.retro.engine.defaultcomponent.ComponentColor;

/**
 * Created by Michael on 8/9/2016.
 */
public class BuildInventoryComponent extends InventoryComponent {

    private ComponentColor m_buildColor;

    public BuildInventoryComponent(){
        super(7, 2);
        m_buildColor = new ComponentColor(255,255,255);

        // Add the build commands to the roster
        ChatBox.getInstance().addCommand(new CommandSave());
        ChatBox.getInstance().addCommand(new CommandLoad());
        ChatBox.getInstance().addCommand(new CommandNew());
        ChatBox.getInstance().addCommand(new CommandSetColor());
        ChatBox.getInstance().addCommand(new CommandMoveSpeed());
        ChatBox.getInstance().addCommand(new CommandSensitivity());
        ChatBox.getInstance().addCommand(new CommandRemoveLower());

        // Add animation commands
        ChatBox.getInstance().addCommand(new CommandAddModel());
        ChatBox.getInstance().addCommand(new CommandAnimation());
        ChatBox.getInstance().addCommand(new CommandSaveAnimation());
        ChatBox.getInstance().addCommand(new CommandLoadAnimation());
        ChatBox.getInstance().addCommand(new CommandSelectModel());
        ChatBox.getInstance().addCommand(new CommandFrame());
        ChatBox.getInstance().addCommand(new CommandInterval());
        ChatBox.getInstance().addCommand(new CommandScale());
        ChatBox.getInstance().addCommand(new CommandSetPosition());
        ChatBox.getInstance().addCommand(new CommandSetOrigin());

        addItem(new PickItem());
        addItem(new PaintItem());
        addItem(new EyeDropperItem());

        // Animation iTems
        addItem(new RotateItem("x"));
        addItem(new RotateItem("y"));
        addItem(new RotateItem("z"));
        addItem(new PositionItem("x"));
        addItem(new PositionItem("y"));
        addItem(new PositionItem("z"));
        addItem(new ScaleItem("x"));
        addItem(new ScaleItem("y"));
        addItem(new ScaleItem("z"));

        addItem(new FrameItem());
        addItem(new PreviewItem());
    }

    public ComponentColor getBuildColor(){
        return m_buildColor;
    }
    public void setBuildColor(ComponentColor c){
        m_buildColor = c;
    }
}
