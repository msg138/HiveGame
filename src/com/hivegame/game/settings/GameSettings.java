package com.hivegame.game.settings;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Michael on 9/25/2016.
 */
public class GameSettings {


    public static final boolean g_alwaysLockCamera = true;


    public static final int xpFromMinion = 15;
    public static final int xpFromHero = 100;
    public static final int xpFromMonster = 30;

    public static final int c_deathTimer = 10;

    public static final String c_controlSchemeMouseMove = "MOUSEMOVE";
    public static final String c_controlSchemeKeyboardMove = "KEYMOVE";
    public static final String g_currentControlScheme = c_controlSchemeKeyboardMove;

    public static final float c_zeroAngle = (float)Math.toRadians(45);

    public static final int c_moveCharacterUp = KeyEvent.VK_W;
    public static final int c_moveCharacterDown = KeyEvent.VK_S;
    public static final int c_moveCharacterLeft = KeyEvent.VK_A;
    public static final int c_moveCharacterRight = KeyEvent.VK_D;
    public static final int c_moveCharacterJump = KeyEvent.VK_SPACE;

    public static final int c_abilityCount = 5;

    public static final int c_abilityOne = 0;
    public static final int c_abilityTwo = 1;
    public static final int c_abilityThree = KeyEvent.VK_Q;
    public static final int c_abilityFour = KeyEvent.VK_E;
    public static final int c_abilityFive = KeyEvent.VK_R;

}
