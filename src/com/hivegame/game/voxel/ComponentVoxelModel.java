package com.hivegame.game.voxel;

import com.jogamp.opengl.GL2;
import com.retro.engine.Framework;
import com.retro.engine.component.Component;
import com.retro.engine.debug.Debug;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.defaultcomponent.ComponentRotation;
import com.retro.engine.model.Matrix;
import com.retro.engine.model.RawModel;
import com.retro.engine.util.list.UtilArray;
import com.retro.engine.util.vector.Vector3;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Michael on 7/30/2016.
 */
public class ComponentVoxelModel extends Component {

    private ArrayList<Voxel> m_voxels;
    private HashMap<Vector3, Voxel> m_voxelPositions;

    private RawModel m_model;

    private float m_offsetX, m_offsetZ;// Shouldn't need offsetY.
    private float m_offsetY;
    private float m_voxSize; // A uniform voxel size shared throughout this model.

    private boolean m_sizeMatters = true;

    private float m_scale;

    private float m_scaleX;
    private float m_scaleY;
    private float m_scaleZ;

    private boolean m_generateBelowZero = true;

    public ComponentVoxelModel(float offx, float offy, float offz, float vsize){
        m_voxels = new ArrayList<>();
        m_voxelPositions = new HashMap<>();

        m_scale = 1f;
        m_scaleX = 1f;
        m_scaleY = 1f;
        m_scaleZ = 1f;

        m_offsetX = offx;
        m_offsetZ = offz;
        m_offsetY = offy;
        m_voxSize = vsize;

        updateModel();
    }

    public void setGenerateBelowZero(boolean b){
        m_generateBelowZero = b;
    }
    public boolean getGenerateBelowZero(){
        return m_generateBelowZero;
    }

    public float getOffsetY(){
        return m_offsetY;
    }

    public float getOffsetX(){
        return m_offsetX;
    }
    public float getOffsetZ(){
        return m_offsetZ;
    }
    public float getVoxSize(){
        return m_voxSize;
    }

    public float getScaleX() {
        return m_scaleX;
    }

    public void setScaleX(float m_scaleX) {
        this.m_scaleX = m_scaleX;
    }

    public float getScaleY() {
        return m_scaleY;
    }

    public void setScaleY(float m_scaleY) {
        this.m_scaleY = m_scaleY;
    }

    public float getScaleZ() {
        return m_scaleZ;
    }

    public void setScaleZ(float m_scaleZ) {
        this.m_scaleZ = m_scaleZ;
    }

    public ArrayList<Voxel> getVoxels(){
        return m_voxels;
    }

    public void clearVoxels(){
        m_voxelPositions.clear();
        m_voxels.clear();
    }

    public void resetModel(){
        if(m_model != null)
            m_model.kill();
        m_model = null;
    }

    public boolean doesSizeMatter(){
        return m_sizeMatters;
    }
    public void setSizeMatters(boolean s){
        m_sizeMatters = s;
    }

    public void setModel(RawModel mod){
        m_model = mod;
    }

    public float getScale(){
        return m_scale;
    }
    public void setScale(float s){
        m_scale = s;
    }

    public void updateModel(){
        float[] vertices = {};
        float[] colors = {};

        for(Voxel v : m_voxels){
            if(!m_generateBelowZero && v.getPosition().getY() - 1 < 0)
                continue;
            Vector3 pos = v.getPosition();
            Voxel top = getVoxel(new Vector3(pos.getX(), pos.getY()+1, pos.getZ()));
            Voxel bottom = getVoxel(new Vector3(pos.getX(), pos.getY()-1, pos.getZ()));
            Voxel left = getVoxel(new Vector3(pos.getX()-1, pos.getY(), pos.getZ()));
            Voxel right = getVoxel(new Vector3(pos.getX()+1, pos.getY(), pos.getZ()));
            Voxel front = getVoxel(new Vector3(pos.getX(), pos.getY(), pos.getZ()+1));
            Voxel back = getVoxel(new Vector3(pos.getX(), pos.getY(), pos.getZ()-1));
            vertices = UtilArray.concat(vertices, v.getVertices(top == null, bottom == null, left == null, right == null, back == null, front == null));
            colors = UtilArray.concat(colors, v.getColors(top == null, bottom == null, left == null, right == null, back == null, front == null));
        }

        if(m_model != null)
            m_model.kill();
        m_model = new RawModel(vertices, colors);
    }
    public void updateModel(ComponentVoxelModel north, ComponentVoxelModel east, ComponentVoxelModel south, ComponentVoxelModel west, int width, int depth){
        float[] vertices = {};
        float[] colors = {};

        for(Voxel v : m_voxels){
            if(!m_generateBelowZero && v.getPosition().getY() < 0)
                continue;
            Vector3 pos = v.getPosition();
            float n = pos.getZ() - 1;
            float e = pos.getX() + 1;
            float s = pos.getZ() + 1;
            float w = pos.getX() - 1;

            Voxel top = getVoxel(new Vector3(pos.getX(), pos.getY()+1, pos.getZ()));
            Voxel bottom = getVoxel(new Vector3(pos.getX(), pos.getY()-1, pos.getZ()));

            Voxel left = getVoxel(new Vector3(pos.getX()-1, pos.getY(), pos.getZ()));
            if(w < 0 && west != null)
            {
                left = west.getVoxel(new Vector3(width - 1, pos.getY(), pos.getZ()));
            }

            Voxel right = getVoxel(new Vector3(pos.getX()+1, pos.getY(), pos.getZ()));
            if(e >= width && east != null)
            {
                right = east.getVoxel(new Vector3(0, pos.getY(), pos.getZ()));
            }

            Voxel front = getVoxel(new Vector3(pos.getX(), pos.getY(), pos.getZ()+1));
            if(s >= depth && south != null){
                front = south.getVoxel(new Vector3(pos.getX(), pos.getY(), 0));
            }

            Voxel back = getVoxel(new Vector3(pos.getX(), pos.getY(), pos.getZ()-1));
            if(n < 0 && north != null){
                back = north.getVoxel(new Vector3(pos.getX(), pos.getY(), depth - 1));
            }


            vertices = UtilArray.concat(vertices, v.getVertices(top == null, bottom == null, left == null, right == null, back == null, front == null));
            colors = UtilArray.concat(colors, v.getColors(top == null, bottom == null, left == null, right == null, back == null, front == null));
        }

        if(m_model != null)
            m_model.kill();
        m_model = new RawModel(vertices, colors);
    }

    public void reboot(ComponentVoxelModel n, ComponentVoxelModel e, ComponentVoxelModel s, ComponentVoxelModel w, int wi, int de){
        updateModel(n, e, s, w, wi, de);
    }

    public boolean addVoxel(Voxel v){
        if(v.getSize() == m_voxSize || !m_sizeMatters) {
            if(m_voxelPositions.get(v.getPosition()) != null)
            {
                Debug.out("Voxel already exists in that spot.");
                return false;
            }

            m_voxels.add(v);
            m_voxelPositions.put(v.getPosition(), v);
            return true;
        }else {
            Debug.out("Voxel size does not match model. Was not added.");
            return false;
        }
    }
    public boolean addVoxel(Voxel v, boolean resetBuffers){
        boolean res = addVoxel(v);
        if(resetBuffers)
            updateModel();
        return res;
    }
    public Voxel removeVoxel(Voxel v, boolean resetBuffers){
        m_voxels.remove(v);
        m_voxelPositions.remove(v.getPosition());
        if(resetBuffers)
            updateModel();
        return v;
    }
    public Voxel removeVoxel(Vector3 pos, boolean resetBuffers){
        return removeVoxel(getVoxel(pos), resetBuffers);
    }

    public Voxel getVoxel(Vector3 pos){
        return m_voxelPositions.get(pos);
    }

    public RawModel getModel(){
        if(m_model == null)
            updateModel();
        return m_model;
    }

    public void render(GL2 gl, float x, float y, float z){
        render(gl, x, y, z, 0, 0, 0);
    }

    public void render(GL2 gl, float x, float y, float z, float rx, float ry, float rz){

        //gl.glTranslatef(vox.getOffsetX(), 0.0f, vox.getOffsetZ());
        Matrix m = Framework.getModelMatrix();
        m.pushMatrix();
        m.translatef(-this.getOffsetX()/2, -this.getOffsetY()/2, -this.getOffsetZ()/2);
        //m.translatef(vox.getOffsetX(), vox.getOffsetY(), vox.getOffsetZ());
        m.translatef(x, y, z);
        m.rotatef(rz, 0, 0, 1f);
        m.rotatef(rx, 1f, 0, 0);
        m.rotatef(ry, 0, 1f, 0);
        m.translatef(this.getOffsetX()*1.5f, this.getOffsetY()*1.5f, this.getOffsetZ()*1.5f);
        // Move more if we have to.
        m.scalef(this.getScale() * this.getScaleX(), this.getScale() * this.getScaleY(), this.getScale() * this.getScaleZ());
        m.activateM(gl);

        this.getModel().render(gl);

        m.popMatrixM(gl);
    }

    @Override
    public void kill(){
        Debug.out("Should be killing model...");
        if(m_model != null)
            m_model.kill();
    }

}
