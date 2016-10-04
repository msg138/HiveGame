package com.hivegame.game.voxel;

/**
 * Created by Michael on 8/3/2016.
 */
public enum VoxelFace {
    FACE_NIL(-1),
    FACE_LEFT(0),
    FACE_RIGHT(1),
    FACE_TOP(2),
    FACE_BOTTOM(3),
    FACE_FRONT(4),
    FACE_BACK(5);


    private final int m_faceId;

    private VoxelFace(int faceId){
        m_faceId = faceId;
    }

    public int getFace(){
        return m_faceId;
    }

    public static String getFaceName(int f){
        if(FACE_TOP.getFace() == f)
            return "TOP";
        else if(FACE_BOTTOM.getFace() == f)
            return "BOTTOM";
        else if(FACE_LEFT.getFace() == f)
            return "LEFT";
        else if(FACE_RIGHT.getFace() == f)
            return "RIGHT";
        else if(FACE_FRONT.getFace() == f)
            return "FRONT";
        else if(FACE_BACK.getFace() == f)
            return "BACK";
        else
            return "NIL";
    }
}
