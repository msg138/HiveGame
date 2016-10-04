package com.hivegame.game.chat;

import com.hivegame.game.chat.command.*;
import com.retro.engine.Framework;
import com.retro.engine.camera.Camera;
import com.retro.engine.camera.FirstPersonCamera;
import com.retro.engine.defaultcomponent.ComponentColor;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.defaultcomponent.ComponentText;
import com.retro.engine.entity.Entity;
import com.retro.engine.entity.EntityStorage;
import com.retro.engine.event.EventHandler;
import com.retro.engine.util.string.UtilString;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 8/8/2016.
 */
public class ChatBox {

    public static final int c_maxChatLines = 10;
    public static final int c_maxChatLength = 132;

    private static ChatBox m_instance;

    private List<String > m_chatHistory;
    private List<Command> m_commands;

    private Entity m_chatBackground;
    private Entity m_chatText;
    private Entity m_chatInput;

    private boolean m_focus;

    private String m_input;

    private ChatBox(){
        m_focus = false;

        m_chatHistory = new ArrayList<>();

        m_commands = new ArrayList<>();

        m_chatText = new Entity();
        m_chatText.add(new ComponentPosition(0, Framework.getInstance().getGameHeight() - 200,0,0));
        m_chatText.add(new ComponentText(UtilString.getTextWithWrap("Welcome to Hive!", c_maxChatLength), "alphabet", 1.5f));

        m_chatInput = new Entity();
        m_chatInput.add(new ComponentPosition(0, Framework.getInstance().getGameHeight() - 200 + c_maxChatLines*10 + 25, 0, 0));
        m_chatInput.add(new ComponentText("Say: ", "alphabet", 1.5f));

        // Commands default to add

        m_commands.add(new CommandTeleport());
        m_commands.add(new CommandBuildMode());
        m_commands.add(new CommandFPS());
        m_commands.add(new CommandNoClip());
        m_commands.add(new CommandBiome());
        m_commands.add(new CommandPosition());
        m_commands.add(new CommandSwitchThirdPerson());

        // End

        m_instance = this;
    }

    public static ChatBox getInstance(){
        if(m_instance == null)
            new ChatBox();
        return m_instance;
    }

    public static ChatBox getNewInstance(){
        getInstance().kill();
        new ChatBox();
        return m_instance;
    }

    public static ChatBox getSeparateInstance(){
        ChatBox inst = getInstance();
        ChatBox newOne = new ChatBox();
        m_instance = inst;
        return newOne;
    }

    public void addChatbox(){
        Framework.getInstance().getEntityStorage().addEntity(m_chatText);
        Framework.getInstance().getEntityStorage().addEntity(m_chatInput);

        ((ComponentText)m_chatText.get(ComponentText.class)).getTextObject().setColor(new ComponentColor(0, 0, 0, 255));
        ((ComponentText)m_chatText.get(ComponentText.class)).getTextObject().setWrapText(c_maxChatLength);
        ((ComponentText)m_chatInput.get(ComponentText.class)).getTextObject().setColor(new ComponentColor(240, 203, 31, 255));
        ((ComponentText)m_chatInput.get(ComponentText.class)).getTextObject().setWrapText(c_maxChatLength);
    }

    public void kill(){
        if(m_chatText != null)
            Framework.getInstance().getEntityStorage().removeEntity(m_chatText, true);
        if(m_chatInput != null)
            Framework.getInstance().getEntityStorage().removeEntity(m_chatInput, true);

        m_chatText = null;
        m_chatInput = null;
    }

    public void bringUp(){
        {
            EntityStorage es = Framework.getInstance().getEntityStorage();
            es.bringEntityToFront(m_chatText);
            es.bringEntityToFront(m_chatInput);
        }
    }

    public void addCommand(Command c){
        m_commands.add(c);
    }

    public void setInput(String i){
        m_input = i;
        //Debug.out("\t\t" + i);
        ((ComponentText)m_chatInput.get(ComponentText.class)).getTextObject().setText("Say: " + m_input);
    }
    public String getInput(){
        return m_input;
    }
    public void clearInput(){
        setInput("");
    }

    public void gainFocus(){
        m_focus = true;
    }
    public void loseFocus(){
        m_focus = false;
    }
    public boolean isFocused(){
        return m_focus;
    }
    public void addMessage(String msg, Entity sender){
        m_chatHistory.add(msg);
        if(!msg.startsWith("/"))
            msg = ">> " + msg;
        ComponentText text = (ComponentText)m_chatText.get(ComponentText.class);

        text.getTextObject().setTextWithWrap(text.getTextObject().getText() + '\n'+msg);
        String txt = text.getTextObject().getText();
        int lines = UtilString.countChars(txt, '\n');
        while(lines > c_maxChatLines){
            int index = txt.indexOf('\n');
            if(index == -1)
                break;
            txt = txt.substring(index+1);
            lines--;
        }
        text.getTextObject().setText(txt);

        if(sender == null)
            return;
        // Check it against commands.
        int cmdIndex = msg.indexOf("/");
        if(cmdIndex != 0)
            return;
        int in = msg.indexOf(' ');
        String cmsg;
        if(in == -1)
            cmsg = msg.substring(1);
        else
            cmsg = msg.substring(1, in);
        String[] args = msg.substring(1).split(" ");
        for(Command c : m_commands){
            if((c.getName().equals(cmsg) || c.isAlias(cmsg)) && c.getRequiredArguments() <= args.length) {
                boolean success = false;
                try{
                    success = c.onCommand(sender, args);
                }catch(Exception e){
                    e.printStackTrace();
                }
                if (!success) {
                    addMessage("Command failed.", sender);
                }
            }
            if((c.getName().equals(cmsg) || c.isAlias(cmsg)))
                return;
        }
        addMessage("Command failed. " + cmsg + "Does not exist.", sender);
    }


    public void update(){
        bringUp();

        // Update the chatbox.
        if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_ENTER))
        {
            if(ChatBox.getInstance().isFocused()){
                ChatBox.getInstance().addMessage(EventHandler.getInstance().getInput(), new Entity());
                EventHandler.getInstance().resetInput();
                ChatBox.getInstance().loseFocus();
                ChatBox.getInstance().clearInput();
                if(Camera.getInstance() instanceof FirstPersonCamera) {
                    ((FirstPersonCamera) Camera.getInstance()).setCanRotate(true);
                    ((FirstPersonCamera) Camera.getInstance()).setCanMove(true);
                }
            }else{
                ChatBox.getInstance().gainFocus();
                if(Camera.getInstance() instanceof FirstPersonCamera) {
                    ((FirstPersonCamera) Camera.getInstance()).setCanRotate(false);
                    ((FirstPersonCamera) Camera.getInstance()).setCanMove(false);
                }
            }
            EventHandler.getInstance().releaseKey(KeyEvent.VK_ENTER);
        }

        if(ChatBox.getInstance().isFocused()) {
            if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_ESCAPE)) {
                ChatBox.getInstance().loseFocus();
                EventHandler.getInstance().stopTakingInput();
                EventHandler.getInstance().resetInput();
                ChatBox.getInstance().setInput(EventHandler.getInstance().getInput());
            }else {
                EventHandler.getInstance().takeInput();
                ChatBox.getInstance().setInput(EventHandler.getInstance().getInput());
            }
            return;
        }else if(EventHandler.getInstance().isKeyPressed(KeyEvent.VK_SLASH))
        {
            ChatBox.getInstance().gainFocus();
            EventHandler.getInstance().setInput("/");

            if(Camera.getInstance() instanceof FirstPersonCamera) {
                ((FirstPersonCamera) Camera.getInstance()).setCanRotate(false);
                ((FirstPersonCamera) Camera.getInstance()).setCanMove(false);
            }
        }
        else
            EventHandler.getInstance().stopTakingInput();
    }
}
