package com.hivegame.game.voxel;

import com.hivegame.game.world.World;
import com.retro.engine.Framework;
import com.retro.engine.debug.Debug;
import com.retro.engine.defaultcomponent.ComponentColor;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.defaultcomponent.ComponentRotation;
import com.retro.engine.defaultcomponent.ComponentScale;
import com.retro.engine.model.AnimatedRawModel;
import com.retro.engine.model.RawModel;
import com.retro.engine.util.file.RetroFile;
import com.retro.engine.util.vector.Vector3;
import com.retro.engine.util.vector.Vector4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 8/5/2016.
 */
public class UtilVoxel {

    public static final float g_defaultScale = 0.03f;

    public static RawModel getVoxelModel(String fname){
        return getVoxelModelComponent(fname).getModel();
    }

    public static ComponentVoxelModel getVoxelModelComponent(String fname){
        ComponentVoxelModel vox = new ComponentVoxelModel(0, 0.5f, 0, 2f);
        vox.setSizeMatters(false);
        List<Voxel> v = UtilVoxel.loadFromFile(fname);
        for (Voxel vv : v)
            vox.addVoxel(vv, false);
        vox.setScale(g_defaultScale);
        vox.resetModel();

        return vox;
    }
    public static ComponentVoxelModel getVoxelModelComponentAnimation(String fname){
        ComponentVoxelModel vox = new ComponentVoxelModel(0, 0.5f, 0, 2f);
        vox.setSizeMatters(false);
        vox.setModel(UtilVoxel.loadAnimationFromFile(fname));

        vox.setScale(g_defaultScale);

        return vox;
    }

    public static AnimatedRawModel loadAnimationFromFile(String fname){
        AnimatedRawModel ret = new AnimatedRawModel(0);

        RetroFile f = new RetroFile(fname);
        if(f.getLines() <= 1) {
            Debug.out("Could not load file: " + fname);
            return ret;
        }
        if(f.getLineData(0).getData().equals("animationv01")) {
            // Load the models for the animation.
            {
                String[] data = f.getLineData(1).getDataSplit("\\^");
                for(int i=0;i<data.length;i++)
                {
                    ret.addModel(getVoxelModel(data[i]), data[i]);
                    Debug.out("Added model "+data[i]);
                }
            }
            // Get the states of position and rotation for each frame.
            final int start = 2;
            for(int i=start;i<f.getLines();i++)
            {
                String[] data = f.getLineData(i).getDataSplit("\\^");

                if(data.length < ret.getModelCount()*6)// Todo change when we add scaling as a factor for animations. Or just make a new animation file version.
                    continue;
                ret.addFrame();
                for(int j=0;j<ret.getModelCount();j++)
                {
                    int d = j * 6;
                    if(d + 5 > data.length)
                        continue;
                    ComponentPosition pos = ret.getFrame(i-start).getPosition(j);
                    ComponentRotation rot = ret.getFrame(i-start).getRotation(j);
                    // Position first then rotation
                    pos.setX(Float.parseFloat(data[d+0]));
                    pos.setY(Float.parseFloat(data[d+1]));
                    pos.setZ(Float.parseFloat(data[d+2]));
                    rot.setRotationX(Float.parseFloat(data[d+3]));
                    rot.setRotationY(Float.parseFloat(data[d+4]));
                    rot.setRotationZ(Float.parseFloat(data[d+5]));
                }
            }
        }// Only real difference is this version is interpolation of the frames.
        else if(f.getLineData(0).getData().equals("animationv02")) {
            String frameNum = f.getLineData(1).getData();
            ret.setMaxFrames(Integer.parseInt(frameNum));
            ret.setInterpolated(true);
            // Load the models for the animation.
            {
                String[] data = f.getLineData(2).getDataSplit("\\^");
                for(int i=0;i<data.length;i++)
                {
                    ret.addModel(getVoxelModel(data[i]), data[i]);
                    Debug.out("Added model "+data[i]);
                }
            }
            // Get the states of position and rotation for each frame.
            final int start = 3;
            for(int i=start;i<f.getLines();i++)
            {
                String[] data = f.getLineData(i).getDataSplit("\\^");

                if(data.length < ret.getModelCount()*6)
                    continue;
                ret.addFrame();
                for(int j=0;j<ret.getModelCount();j++)
                {
                    int d = j * 6;
                    if(d + 5 > data.length)
                        continue;
                    ComponentPosition pos = ret.getFrame(i-start).getPosition(j);
                    ComponentRotation rot = ret.getFrame(i-start).getRotation(j);
                    // Position first then rotation
                    pos.setX(Float.parseFloat(data[d+0]));
                    pos.setY(Float.parseFloat(data[d+1]));
                    pos.setZ(Float.parseFloat(data[d+2]));
                    rot.setRotationX(Float.parseFloat(data[d+3]));
                    rot.setRotationY(Float.parseFloat(data[d+4]));
                    rot.setRotationZ(Float.parseFloat(data[d+5]));
                }
            }
        }// Only real difference is this version is scale
        else if(f.getLineData(0).getData().equals("animationv03")) {
            String frameNum = f.getLineData(1).getData();
            ret.setMaxFrames(Integer.parseInt(frameNum));
            ret.setInterpolated(true);
            // Load the models for the animation.
            {
                String[] data = f.getLineData(2).getDataSplit("\\^");
                for(int i=0;i<data.length;i++)
                {
                    ret.addModel(getVoxelModel(data[i]), data[i]);
                    Debug.out("Added model "+data[i]);
                }
            }
            // Get the states of position and rotation for each frame.
            final int start = 3;
            for(int i=start;i<f.getLines();i++)
            {
                String[] data = f.getLineData(i).getDataSplit("\\^");

                if(data.length < ret.getModelCount()*9)
                    continue;
                ret.addFrame();
                for(int j=0;j<ret.getModelCount();j++)
                {
                    int d = j * 9;
                    if(d + 8 > data.length)
                        continue;
                    ComponentPosition pos = ret.getFrame(i-start).getPosition(j);
                    ComponentRotation rot = ret.getFrame(i-start).getRotation(j);
                    ComponentScale sca = ret.getFrame(i-start).getScale(j);
                    // Position first then rotation
                    pos.setX(Float.parseFloat(data[d+0]));
                    pos.setY(Float.parseFloat(data[d+1]));
                    pos.setZ(Float.parseFloat(data[d+2]));
                    rot.setRotationX(Float.parseFloat(data[d+3]));
                    rot.setRotationY(Float.parseFloat(data[d+4]));
                    rot.setRotationZ(Float.parseFloat(data[d+5]));
                    sca.setScaleX(Float.parseFloat(data[d+6]));
                    sca.setScaleY(Float.parseFloat(data[d+7]));
                    sca.setScaleZ(Float.parseFloat(data[d+8]));
                }
            }
        }
        return ret;
    }
    public static boolean saveAnimationToFile(String fname, AnimatedRawModel ani, String[] modelNames){

        boolean intFlag = ani.isInterpolated();

        RetroFile file = new RetroFile();
        String data = "animationv03\n";
        data += ani.getMaxFrames() + "\n";
        ani.setInterpolated(false);
        for(int i=0;i<modelNames.length;i++){
            if(i!=0)
                data += "^";
            data += modelNames[i];
        }
        data += "\n";
        for(int i=0;i<ani.getMaxFrames();i++){
            for(int j=0;j<modelNames.length;j++) {
                if(modelNames[j].isEmpty())
                    continue;
                if(j != 0)
                    data += "^";
                ComponentPosition pos = ani.getFrame(i).getPosition(j);
                ComponentRotation rot = ani.getFrame(i).getRotation(j);
                ComponentScale sca = ani.getFrame(i).getScale(j);
                data += pos.getX() + "^" + pos.getY() + "^" + pos.getZ() + "^";
                data += rot.getRotationX() + "^" + rot.getRotationY() + "^" + rot.getRotationZ() + "^";
                data += sca.getScaleX() + "^" + sca.getScaleY() + "^" + sca.getScaleZ();
            }
            data += "\n";
        }
        file.writeFile(fname, data, true, false);
        if(intFlag)
            ani.setInterpolated(true);
        return true;
    }

    public static List<Voxel> loadFromFile(String fname){
        return loadFromFile(fname, 0, 0, 0);
    }

    public static List<Voxel> loadFromFile(String fname, int displacex, int displacey, int displacez){
        List<Voxel> ret = new ArrayList<>();

        RetroFile f = new RetroFile(fname);
        if(f.getLines() <= 0) {
            Debug.out("Could not load file: " + fname);
            return ret;
        }
        if(f.getLineData(0).getData().equals("is")) {
            for(int i=1;i<f.getLines();i++)
            {
                String[] data = f.getLineData(i).getDataSplit("\\^");

                if(data.length < 12)
                    continue;
                Voxel v = new Voxel(Float.parseFloat(data[0]) / Float.parseFloat(data[11]), Float.parseFloat(data[1]) / Float.parseFloat(data[11]), Float.parseFloat(data[2]) / Float.parseFloat(data[11])
                        , 8*Float.parseFloat(data[11]),
                        new ComponentColor(Integer.parseInt(data[6]), Integer.parseInt(data[7]), Integer.parseInt(data[8]), Integer.parseInt(data[9])));
                v.setVX((int)(100 * 8*Float.parseFloat(data[0]) * Float.parseFloat(data[11])));
                v.setVY((int)(100 * 8*Float.parseFloat(data[1]) * Float.parseFloat(data[11])));
                v.setVZ((int)(100 * 8*Float.parseFloat(data[2]) * Float.parseFloat(data[11])));
                ret.add(v);
            }
        }else if(f.getLineData(0).getData().equals("v1")){
            for(int i=1;i<f.getLines();i++)
            {
                String[] data = f.getLineData(i).getDataSplit("\\^");

                if(data.length < 8)
                    continue;
                Voxel v = new Voxel(Float.parseFloat(data[0]) + displacex, Float.parseFloat(data[1]) + displacey, Float.parseFloat(data[2]) + displacez
                        , Float.parseFloat(data[3]),
                        new ComponentColor(Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), Integer.parseInt(data[7])));

                ret.add(v);
            }
        }
        return ret;
    }

    public static boolean saveToFile(String file, List<Voxel> voxels, boolean isModel){
        if(voxels.size() < 0)
            return false;

        RetroFile f = new RetroFile();

        String data = "v1\n";
        for(Voxel v : voxels){
            Vector3 pos = v.getPosition();
            if(pos.getY() < 0 && isModel){
                continue;
            }
            ComponentColor c = v.getColor();
            data += "" + pos.getX() + "^" + pos.getY() + "^" + pos.getZ() + "^" + v.getSize() + "^" + c.getRed() + "^" + c.getGreen() + "^" + c.getBlue() + "^" + c.getAlpha() + "\n";
        }

        f.writeFile(file, data, true, false);

        return true;
    }

    public static Vector4 getVoxelLookAt(World w, float mx, float my, float mz, float distance, float pitch, float yaw){
        Vector4 ret = new Vector4();
        boolean found = false;
        for (float j = 0; j < distance && !found; j += 0.03f){
            float xx, yy, zz;
            xx = (Framework.getInstance().getCamera().getPosition().getX())+ mx*j;//((float) Math.sin(yaw)*j*w.getVoxelSize());// - (float) Math.sin(rad)*(j*w.getVoxelSize());
            zz = (Framework.getInstance().getCamera().getPosition().getZ())+ mz*j;//((float) Math.cos(yaw)*j*w.getVoxelSize());// - (float) Math.cos(rad)*(j*w.getVoxelSize());

            yy = (Framework.getInstance().getCamera().getPosition().getY())+ my*j;//((float) Math.tan(pitch)*j*w.getVoxelSize());// + (float) Math.tan((pitch))*j*w.getVoxelSize();

            Vector3 coords = new Vector3(xx, yy, zz);
            Vector3 voxx = w.convertToVoxelSpaceFlat(coords);

            if(w.getVoxel(voxx) != null) {
                Vector3 vox = new Vector3(voxx.getX() * w.getVoxelSize(), voxx.getY() * w.getVoxelSize(), voxx.getZ() * w.getVoxelSize());
                Vector3[] points = new Vector3[6];
                points[VoxelFace.FACE_TOP.getFace()] = new Vector3(vox.getX(), vox.getY() + w.getVoxelSize()/2, vox.getZ());
                points[VoxelFace.FACE_BOTTOM.getFace()] = new Vector3(vox.getX(), vox.getY() - w.getVoxelSize()/2, vox.getZ());

                points[VoxelFace.FACE_LEFT.getFace()] = new Vector3(vox.getX(), vox.getY(), vox.getZ() + w.getVoxelSize()/2);
                points[VoxelFace.FACE_RIGHT.getFace()] = new Vector3(vox.getX(), vox.getY(), vox.getZ() - w.getVoxelSize()/2);

                points[VoxelFace.FACE_FRONT.getFace()] = new Vector3(vox.getX() + w.getVoxelSize()/2, vox.getY(), vox.getZ());
                points[VoxelFace.FACE_BACK.getFace()] = new Vector3(vox.getX() - w.getVoxelSize()/2, vox.getY(), vox.getZ());

                int closest = 0;
                float curDistance = coords.distanceTo(points[closest]);
                for(int i=0;i<6;i++)
                {
                    if(i == closest)
                        continue;
                    if(coords.distanceTo(points[i]) < curDistance)
                    {
                        closest = i;
                        curDistance = coords.distanceTo(points[closest]);
                    }
                }
                ret = new Vector4(voxx, closest);

                found = true;
            }
        }
        if(!found)
            return new Vector4(-1, -1, -1, 0);
        return ret;
    }
    public static List<Vector4> getVoxelsLookAt(World w, float distance, float pitch, float yaw){
        List<Vector4> ret = new ArrayList<>();
        List<Vector3> alreadyGot = new ArrayList<>();
        boolean found = false;
        for (float j = 0; j < distance && !found; j += 0.03f){
            float xx, yy, zz;
            float rad = yaw;// Convert into radians.
            xx = Framework.getInstance().getCamera().getPosition().getX() - (float) Math.sin(rad)*(j*w.getVoxelSize());
            zz = Framework.getInstance().getCamera().getPosition().getZ() - (float) Math.cos(rad)*(j*w.getVoxelSize());

            yy = Framework.getInstance().getCamera().getPosition().getY() + (float) Math.tan((pitch))*j*w.getVoxelSize();

            Vector3 coords = new Vector3(xx, yy, zz);
            Vector3 voxx = w.convertToVoxelSpaceFlat(coords);

            Voxel v = w.getVoxel(voxx);
            boolean nope = false;
            for(Vector3 vv : alreadyGot)
                if(vv.equals(voxx))
                {
                    nope = true;
                    break;
                }
            if(nope)
                continue;
            alreadyGot.add(voxx);

            if(v != null) {
                Vector3 vox = new Vector3(voxx.getX() * w.getVoxelSize(), voxx.getY() * w.getVoxelSize(), voxx.getZ() * w.getVoxelSize());
                Vector3[] points = new Vector3[6];
                points[VoxelFace.FACE_TOP.getFace()] = new Vector3(vox.getX(), vox.getY() + w.getVoxelSize()/2, vox.getZ());
                points[VoxelFace.FACE_BOTTOM.getFace()] = new Vector3(vox.getX(), vox.getY() - w.getVoxelSize()/2, vox.getZ());

                points[VoxelFace.FACE_LEFT.getFace()] = new Vector3(vox.getX(), vox.getY(), vox.getZ() + w.getVoxelSize()/2);
                points[VoxelFace.FACE_RIGHT.getFace()] = new Vector3(vox.getX(), vox.getY(), vox.getZ() - w.getVoxelSize()/2);

                points[VoxelFace.FACE_FRONT.getFace()] = new Vector3(vox.getX() + w.getVoxelSize()/2, vox.getY(), vox.getZ());
                points[VoxelFace.FACE_BACK.getFace()] = new Vector3(vox.getX() - w.getVoxelSize()/2, vox.getY(), vox.getZ());

                int closest = 0;
                float curDistance = coords.distanceTo(points[closest]);
                for(int i=0;i<6;i++)
                {
                    if(i == closest)
                        continue;
                    if(coords.distanceTo(points[i]) < curDistance)
                    {
                        closest = i;
                        curDistance = coords.distanceTo(points[closest]);
                    }
                }
                ret.add(new Vector4(voxx, closest));
            }else
                ret.add(new Vector4(voxx, VoxelFace.FACE_NIL.getFace()));
        }
        return ret;
    }
}
