package com.hivegame.game.voxel;

import com.jogamp.opengl.GL2;
import com.retro.engine.debug.Debug;
import com.retro.engine.defaultcomponent.ComponentColor;
import com.retro.engine.model.RawModel;
import com.retro.engine.util.list.UtilArray;
import com.retro.engine.util.vector.Vector3;

/**
 * Created by Michael on 7/30/2016.
 */
public class Voxel {

    private float m_x;
    private float m_y;
    private float m_z;

    private int m_vx;
    private int m_vy;
    private int m_vz;

    private float m_size;

    private ComponentColor m_color;

    private RawModel model;

    private boolean m_collidable = true;

    public Voxel(float x, float y, float z, float size, ComponentColor cc){
        m_x = x;
        m_y = y;
        m_z = z;

        m_vx = Math.round(x);
        m_vy = Math.round(y);
        m_vz = Math.round(z);
        m_size = size;

        m_color = cc;
    }

    public void setCollidable(boolean c){
        m_collidable = c;
    }
    public boolean isCollidable(){
        return m_collidable;
    }

    public void setVX(int vx){
        m_vx = vx;
    }
    public void setVY(int vy){
        m_vy = vy;
    }
    public void setVZ(int vz){
        m_vz = vz;
    }

    public Vector3 getPosition(){
        return new Vector3(m_vx, m_vy, m_vz);
    }

    public ComponentColor getColor(){
        return m_color;
    }
    public void setColor(ComponentColor color){
        m_color = color;
    }

    public float[] getColors(boolean top, boolean bottom, boolean left, boolean right, boolean back, boolean front){

        float r = m_color.getRedf();
        float g = m_color.getGreenf();
        float b = m_color.getBluef();
        float[] ret = new float[0];

        if(top)
            ret = UtilArray.concat(ret, new float[]{r,g,b,r,g,b,r,g,b,r,g,b,r,g,b,r,g,b,});
        if(bottom)
            ret = UtilArray.concat(ret, new float[]{r,g,b,r,g,b,r,g,b,r,g,b,r,g,b,r,g,b,});
        if(left)
            ret = UtilArray.concat(ret, new float[]{r,g,b,r,g,b,r,g,b,r,g,b,r,g,b,r,g,b,});
        if(right)
            ret = UtilArray.concat(ret, new float[]{r,g,b,r,g,b,r,g,b,r,g,b,r,g,b,r,g,b,});
        if(back)
            ret = UtilArray.concat(ret, new float[]{r,g,b,r,g,b,r,g,b,r,g,b,r,g,b,r,g,b,});
        if(front)
            ret = UtilArray.concat(ret, new float[]{r,g,b,r,g,b,r,g,b,r,g,b,r,g,b,r,g,b,});

        return ret;
    }

    public float[] getFace(VoxelFace vf){
        switch(vf){
            case FACE_BACK:
                return new float[]{
                        // Back
                        m_x * m_size + m_size/2*1.0f,  m_y * m_size + m_size/2*1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*1.0f,  m_y * m_size + m_size/2*1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2*-1.0f,};
            case FACE_LEFT:
                return new float[]{
                        // Left
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2* 1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2* 1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2* 1.0f,};
            case FACE_BOTTOM:
                return new float[]{
                        // Bottom
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2*-1.0f,  m_z * m_size + m_size/2*1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2* 1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2*-1.0f,  m_z * m_size + m_size/2*1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2*-1.0f,};
            case FACE_FRONT:
                return new float[]{
                        // Front
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2* 1.0f,  m_z * m_size + m_size/2*1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2*-1.0f,  m_z * m_size + m_size/2*1.0f,
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2*-1.0f,  m_z * m_size + m_size/2*1.0f,
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2* 1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2* 1.0f,
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2* 1.0f,};
            case FACE_RIGHT:
                return new float[]{
                        // Right
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2* 1.0f,
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2* 1.0f,
                        m_x * m_size + m_size/2*1.0f,  m_y * m_size + m_size/2*1.0f, m_z * m_size + m_size/2* 1.0f,
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2*-1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2*-1.0f,};
            default:
                return new float[]{
                        // Top
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2* 1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2* 1.0f,
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2* 1.0f,
                        m_x * m_size + m_size/2*1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2*-1.0f,
                        m_x * m_size + m_size/2*-1.0f, m_y * m_size + m_size/2* 1.0f, m_z * m_size + m_size/2*-1.0f,};
        }
    }

    public float[] getVertices(boolean top, boolean bottom, boolean left, boolean right, boolean back, boolean front){

        float[] ret = new float[0];

        if(top)
            ret = UtilArray.concat(ret, getFace(VoxelFace.FACE_TOP));
        if(bottom)
            ret = UtilArray.concat(ret, getFace(VoxelFace.FACE_BOTTOM));
        if(left)
            ret = UtilArray.concat(ret, getFace(VoxelFace.FACE_LEFT));
        if(right)
            ret = UtilArray.concat(ret, getFace(VoxelFace.FACE_RIGHT));
        if(back)
            ret = UtilArray.concat(ret, getFace(VoxelFace.FACE_BACK));
        if(front)
            ret = UtilArray.concat(ret, getFace(VoxelFace.FACE_FRONT));

        return ret;
    }
    public float getSize(){
        return m_size;
    }

    public void draw(GL2 gl){
        if(!model.goodBuffers())
            model.generateBuffers(gl);

        gl.glPushMatrix();
        gl.glTranslatef(m_vx * m_size, m_vy * m_size, m_vz * m_size);
        model.render(gl);

        gl.glPopMatrix();
        /*
        float i = getSize() / 2;
        m_color.activateColor(gl);

        gl.glBegin(gl.GL_QUADS);

        // Front
        gl.glVertex3f(i * -1.0f, i * -1.0f, i * 1.0f);
        gl.glVertex3f(i * -1.0f, i * 1.0f, i * 1.0f);
        gl.glVertex3f(i * 1.0f, i * 1.0f, i * 1.0f);
        gl.glVertex3f(i * 1.0f, i * -1.0f, i * 1.0f);

        // Back
        gl.glVertex3f(i * -1.0f, i * -1.0f, i * -1.0f);
        gl.glVertex3f(i * -1.0f, i * 1.0f, i * -1.0f);
        gl.glVertex3f(i * 1.0f, i * 1.0f, i * -1.0f);
        gl.glVertex3f(i * 1.0f, i * -1.0f, i * -1.0f);

        // Left
        gl.glVertex3f(i * -1.0f, i * -1.0f, i * 1.0f);
        gl.glVertex3f(i * -1.0f, i * -1.0f, i * -1.0f);
        gl.glVertex3f(i * -1.0f, i * 1.0f, i * -1.0f);
        gl.glVertex3f(i * -1.0f, i * 1.0f, i * 1.0f);

        // Right
        gl.glVertex3f(i * 1.0f, i * -1.0f, i * 1.0f);
        gl.glVertex3f(i * 1.0f, i * -1.0f, i * -1.0f);
        gl.glVertex3f(i * 1.0f, i * 1.0f, i * -1.0f);
        gl.glVertex3f(i * 1.0f, i * 1.0f, i * 1.0f);

        // Top
        gl.glVertex3f(i * -1.0f, i * 1.0f, i * 1.0f);
        gl.glVertex3f(i * -1.0f, i * 1.0f, i * -1.0f);
        gl.glVertex3f(i * 1.0f, i * 1.0f, i * -1.0f);
        gl.glVertex3f(i * 1.0f, i * 1.0f, i * 1.0f);

        // Bottom
        gl.glVertex3f(i * -1.0f, i * -1.0f, i * 1.0f);
        gl.glVertex3f(i * -1.0f, i * -1.0f, i * -1.0f);
        gl.glVertex3f(i * 1.0f, i * -1.0f, i * -1.0f);
        gl.glVertex3f(i * 1.0f, i * -1.0f, i * 1.0f);

        gl.glEnd();
        gl.glPopMatrix();*/
        Debug.out("Drawn.");
    }

    public String toString(){
        String ret = getPosition().toString();
        ret += " S " + getSize() + " ";
        ret += " C " + getColor().toString();
        return ret;
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof Voxel){
            Voxel v = (Voxel)o;
            return v.getColor().equals(getColor()) && v.getPosition().equals(getPosition());
        }
        return false;
    }
}
