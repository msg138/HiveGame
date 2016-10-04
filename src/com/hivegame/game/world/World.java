package com.hivegame.game.world;

import com.hivegame.game.build.player.Player;
import com.hivegame.game.livingthing.LivingComponent;
import com.hivegame.game.livingthing.LivingUtil;
import com.hivegame.game.voxel.ComponentVoxelModel;
import com.retro.engine.Framework;
import com.retro.engine.debug.Debug;
import com.retro.engine.defaultcomponent.ComponentColor;
import com.retro.engine.defaultcomponent.ComponentPosition;
import com.retro.engine.entity.Entity;
import com.retro.engine.pcg.Noise;
import com.retro.engine.pcg.BetterPerlinNoise;
import com.retro.engine.pcg.PerlinNoise;
import com.retro.engine.util.vector.Vector2;
import com.retro.engine.util.vector.Vector3;
import com.hivegame.game.voxel.Voxel;

import java.util.*;

/**
 * Created by Michael on 7/31/2016.
 */
public class World {

    private static final int c_chunkThreadDelay = 25;
    private static final int c_maxHeight = 256;

    private List<Entity> m_chunks;
    private HashMap<Vector3, Entity> m_chunkLocations;

    private List<Entity> m_worldItems;

    private HashMap<Vector2, List<Voxel>> m_voxelsPlaced;
    private HashMap<Vector2, List<Vector3>> m_voxelsDestroyed;

    private long m_seed;

    private float m_gravity;
    private float m_maxGravity;

    private final int m_chunkWidth = 8;
    private final int m_chunkDepth = 8;
    private final int m_chunkHeight = 128;

    private final int m_chunksLoadedX = 10;
    private final int m_chunksLoadedXF = 4;// 4x4 World for now.
    private final int m_chunksUnloadedX = m_chunksLoadedX;

    private final float m_caveThreshold = 0.3f;
    private final float m_caveGemThreshold = .25f;

    private final float c_caveFreguency = .05f;
    private final float c_cavePersistence = .1f;
    private final float c_caveGemFrequency = 0.05f;
    private final float c_caveGemPersistence = 1f;
    private final long c_caveGemSeedInc = 1337;

    private final ComponentColor m_caveGemColor = new ComponentColor(220, 30, 211);

    private List<Spawn> m_spawnPoints;

    private static final float m_voxelSize = .5f;

    private WorldType m_worldType;

    private Noise m_pcg;

    public World(long seed){
        m_seed = seed;
        m_worldType = WorldType.VOID;

        m_pcg = new PerlinNoise();

        m_gravity = 0.007f;
        m_maxGravity = 0.25f;

        m_chunks = new ArrayList<>();
        m_chunkLocations = new HashMap<>();

        m_spawnPoints = new ArrayList<>();

        m_voxelsDestroyed = new HashMap<>();
        m_voxelsPlaced = new HashMap<>();

        m_worldItems = new ArrayList<>();
    }

    public void addSpawn(Spawn s){
        m_spawnPoints.add(s);
    }

    public Spawn getSpawn(Entity e){
        if(m_spawnPoints.size() <= 0)
            return new Spawn(new Vector3(), -1);
        for(Spawn s : m_spawnPoints){
            if(s.getTeam() == LivingUtil.getLivingComponent(e).getTeamNumber()){
                return s;
            }
        }
        return m_spawnPoints.get(0);
    }

    public void spawn(Entity e){
        LivingComponent lc = LivingUtil.getLivingComponent(e);
        Spawn sp = getSpawn(e);
        lc.getPosition().setX(sp.getPosition().getX());
        lc.getPosition().setY(sp.getPosition().getY());
        lc.getPosition().setZ(sp.getPosition().getZ());
    }
    public WorldType getWorldType(){
        return m_worldType;
    }
    public void addWorldlyItem(Entity item){
        if(!item.has(ComponentPosition.class))
            return;
        if(!item.has(WorldItemComponent.class))
            return;
        Framework.getInstance().getEntityStorage().addEntity(item);
        if(m_worldItems.size() <= 0) {
            m_worldItems.add(item);
            return;
        }
        Vector3 v = this.convertToVoxelSpaceFlat(new Vector3((ComponentPosition)item.get(ComponentPosition.class)));
        // compare to other positions, until it finds one that is farther from the origin.
        for(int i=0;i<m_worldItems.size();i++)
        {
            if(Player.getPositionVector(m_worldItems.get(i)).hashCode() >= v.hashCode()){
                m_worldItems.add(i, item);
                break;
            }
        }
    }
    public void removeWorldlyItem(Entity item){
        m_worldItems.remove(item);
        Framework.getInstance().getEntityStorage().removeEntity(item, true);
    }

    public List<Entity> getWorldItemsClose(Vector3 pos, float distance){
        List<Entity> ret = new ArrayList<>();

        for(Entity e : m_worldItems){
            if(convertToVoxelSpaceFlat(Player.getPositionVector(e)).distanceTo(pos) <= distance )
                ret.add(e);
        }

        return ret;
    }

    public float getGravity(){
        return m_gravity;
    }
    public float getMaxGravity(){
        return m_maxGravity;
    }

    public Vector3 convertToVoxelSpace(Vector3 world){
        return new Vector3(world.getX() / getVoxelSize(), world.getY() / getVoxelSize(), world.getZ() / getVoxelSize());
    }

    // Same as convertToVoxelSpace, although this one truncates the decimal. Rounds it.
    public Vector3 convertToVoxelSpaceFlat(Vector3 world){
        return new Vector3((float)Math.round(world.getX() / getVoxelSize()),(float)Math.round(world.getY() / getVoxelSize()),(float)Math.round(world.getZ() / getVoxelSize()));
    }

    public static float getVoxelSize(){
        return m_voxelSize;
    }

    public Vector3 getMiddleChunkVoxel(){
        return new Vector3(getVoxelSize() * m_chunksLoadedXF/2 * m_chunkWidth, 0,  getVoxelSize() * m_chunksLoadedXF/2 * m_chunkDepth);
    }
    public Vector3 getMiddleChunkVoxelFlat(){
        return new Vector3(m_chunksLoadedXF/2 * m_chunkWidth, 0,  m_chunksLoadedXF/2 * m_chunkDepth);
    }

    public void generateSingleVoxel(boolean scrap){
        if(scrap){
            scrapWorld();

            for(int i=0;i<m_chunksLoadedX;i++)
            {
                for(int j=0;j<m_chunksLoadedX;j++)
                {
                    Entity e = new Entity();

                    Vector3 loc = new Vector3(i, .0f, j);
                    ComponentVoxelModel vox = new ComponentVoxelModel(loc.getX() * m_chunkWidth * m_voxelSize, 0f, loc.getZ() * m_chunkDepth * m_voxelSize, m_voxelSize);
                    vox.resetModel();
                    e.add(vox);
                    addChunk(e, loc);
                }
            }
        }
        Vector3 loc = getMiddleChunkVoxel();
        loc.setX(loc.getX() / getVoxelSize());
        loc.setZ(loc.getZ() / getVoxelSize());
        loc.setY(-1);
        removeVoxel(loc);
        addVoxel(loc, new ComponentColor(255, 0, 255));
    }

    public void generateGrassLand(){
        scrapWorld();
        m_worldType = WorldType.GRASSLAND;
        //m_pcg.set(1.5f, 6f, 1, 2 , m_seed);
        //m_pcg.set(2f, 2f, 1, 2 , m_seed);
        m_pcg.set(.1f, 0.01f, 3, 2, m_seed);
        // BIOMEm_pcg.set(0.1f, 0.01f, 5, 3, m_seed + 666);
        //m_pcg.set(0.2f, 0.05f, 7, 5, m_seed);
        /*for(int i=0;i<m_chunksLoadedX;i++)
        {
            for(int j=0;j<m_chunksLoadedX;j++)
            {
                Vector3 loc = new Vector3(i, .0f, j);
                Entity e = generateGrassLandChunk(m_seed, i, j);
                addChunk(e, loc);
            }
        }*/
    }

    private Entity generateGrassLandChunk(float i, float j){
        ComponentColor[] colors = new ComponentColor[]{
                new ComponentColor(97, 191, 0),
                new ComponentColor(80, 158, 0),
                new ComponentColor(65, 127, 0),

                // DIrt color
                new ComponentColor(87, 68, 22),
                new ComponentColor(140, 140, 140),
        };
        ComponentColor caveColor = new ComponentColor(200, 200, 0);
        Entity e = new Entity();

        Vector3 loc = new Vector3(i, .0f, j);
        ComponentVoxelModel vox = new ComponentVoxelModel(loc.getX() * m_chunkWidth * m_voxelSize, 0f, loc.getZ() * m_chunkDepth * m_voxelSize, m_voxelSize);

        Noise caveGen = new BetterPerlinNoise();
        caveGen.setNoiseSettings(m_pcg.getNoiseSettings());
        caveGen.setFrequency(c_caveFreguency);
        caveGen.setPersistence(c_cavePersistence);

        Random r = new Random();
        r.setSeed(m_seed);
        for(int x=0; x<m_chunkWidth; x++){
            for(int z=0; z<m_chunkDepth; z++){
                int y = (int)Math.floor(m_pcg.getNoise2D(new Vector3(i*m_chunkWidth + x, j*m_chunkDepth + z, 0)).getValue()) + (int)(m_pcg.getAmplitude()*2);
                if(y<0)
                    y = 0;
                if(y>c_maxHeight)
                    y = c_maxHeight;
                vox.addVoxel(new Voxel(x, y, z, m_voxelSize, colors[r.nextInt(colors.length-3)]));
                //y - m_pcg.getPersistence()
                for(int yy = y - 1; yy>99 || yy > y - m_pcg.getPersistence() - 2;yy--){
                    if(yy > y - m_pcg.getPersistence())
                        vox.addVoxel(new Voxel(x, yy, z, m_voxelSize, colors[3]));
                    else
                        vox.addVoxel(new Voxel(x, yy, z, m_voxelSize, colors[4]));
                }
                if(y > 100)
                    for(y=(int)(m_pcg.getAmplitude()*2)+m_chunkHeight;y>(int)(m_pcg.getAmplitude()*2); y--) {
                        float is = (caveGen.getNoise3D(new Vector3(i * m_chunkWidth + x, y, j * m_chunkDepth + z)).getValue());
                        if(is < m_caveThreshold){
                            /*Voxel vvvv = getVoxelAtHighest(vox, new Vector3(x, y, z));
                            if(vvvv != null && vvvv.getPosition().getY() > y && vox.getVoxel(new Vector3(x, y-1, z)) == null)
                                vox.addVoxel(new Voxel(x, y, z, m_voxelSize, colors[r.nextInt(colors.length-1)]));
                            else */
                            Voxel vvvv = getVoxelAtHighest(vox, new Vector3(x, y, z));
                            if(vox.getVoxel(new Vector3(x, y, z)) != null)
                                vox.removeVoxel(new Vector3(x, y, z), false);
                            if(vvvv != null && y <= vvvv.getPosition().getY()) {
                                vox.addVoxel(new Voxel(x, y, z, getVoxelSize(), caveColor));
                            }
                        }
                        caveGen.setPersistence(c_caveGemPersistence);
                        caveGen.setFrequency(c_caveGemFrequency);
                        caveGen.setSeed(m_seed + c_caveGemSeedInc);
                        is = (caveGen.getNoise3D(new Vector3(i * m_chunkWidth + x, y, j * m_chunkDepth + z)).getValue());
                        if(is < m_caveGemThreshold){
                            /*Voxel vvvv = getVoxelAtHighest(vox, new Vector3(x, y, z));
                            if(vvvv != null && vvvv.getPosition().getY() > y && vox.getVoxel(new Vector3(x, y-1, z)) == null)
                                vox.addVoxel(new Voxel(x, y, z, m_voxelSize, colors[r.nextInt(colors.length-1)]));
                            else */
                            Voxel vvvv = getVoxelAtHighest(vox, new Vector3(x, y, z));
                            if(vox.getVoxel(new Vector3(x, y, z)) != null)
                                vox.removeVoxel(new Vector3(x, y, z), false);
                            if(vvvv != null && y <= vvvv.getPosition().getY()) {
                                vox.addVoxel(new Voxel(x, y, z, getVoxelSize(), m_caveGemColor));
                            }
                        }
                        caveGen.setFrequency(c_caveFreguency);
                        caveGen.setPersistence(c_cavePersistence);
                        caveGen.setSeed(m_seed);
                    }
            }
        }
        List<Voxel> voxels = vox.getVoxels();
        for(int a=0;a<voxels.size();a++){
            Voxel v = voxels.get(a);
            float x = v.getPosition().getX();
            float y = v.getPosition().getY();
            float z = v.getPosition().getZ();
            if(v.getColor().equals(colors[4]))
            {
                //float is = (caveGen.getNoise3D(new Vector3(i * m_chunkWidth + x, y, j * m_chunkDepth + z)).getValue());
                Voxel vvvv1 = vox.getVoxel(new Vector3(x+1, y  , z  ));
                if(x + 1 >= m_chunkWidth){
                    float is = (caveGen.getNoise3D(new Vector3(i * m_chunkWidth + x + 1, y, j * m_chunkDepth + z)).getValue());
                    if(is < m_caveThreshold){
                        vvvv1 = new Voxel(x+1, y, z, getVoxelSize(), caveColor);
                    }
                }
                Voxel vvvv2 = vox.getVoxel(new Vector3(x-1, y  , z  ));
                if(x - 1 < 0){
                    float is = (caveGen.getNoise3D(new Vector3(i * m_chunkWidth + x - 1, y, j * m_chunkDepth + z)).getValue());
                    if(is < m_caveThreshold){
                        vvvv2 = new Voxel(x-1, y, z, getVoxelSize(), caveColor);
                    }
                }
                Voxel vvvv3 = vox.getVoxel(new Vector3(x  , y  , z+1));
                if(z + 1 >= m_chunkDepth){
                    float is = (caveGen.getNoise3D(new Vector3(i * m_chunkWidth + x, y, j * m_chunkDepth + z + 1)).getValue());
                    if(is < m_caveThreshold){
                        vvvv3 = new Voxel(x, y, z+1, getVoxelSize(), caveColor);
                    }
                }
                Voxel vvvv4 = vox.getVoxel(new Vector3(x  , y  , z-1));
                if(z - 1 < 0){
                    float is = (caveGen.getNoise3D(new Vector3(i * m_chunkWidth + x, y, j * m_chunkDepth + z - 1)).getValue());
                    if(is < m_caveThreshold){
                        vvvv4 = new Voxel(x, y, z-1, getVoxelSize(), caveColor);
                    }
                }
                Voxel vvvv5 = vox.getVoxel(new Vector3(x  , y-1, z  ));
                Voxel vvvv6 = vox.getVoxel(new Vector3(x  , y+1, z  ));
                if((vvvv1 != null && !vvvv1.getColor().equals(colors[4]) && !vvvv1.getColor().equals(m_caveGemColor))
                        || (vvvv2 != null && !vvvv2.getColor().equals(colors[4]) && !vvvv2.getColor().equals(m_caveGemColor))
                        || (vvvv3 != null && !vvvv3.getColor().equals(colors[4]) && !vvvv3.getColor().equals(m_caveGemColor))
                        || (vvvv4 != null && !vvvv4.getColor().equals(colors[4]) && !vvvv4.getColor().equals(m_caveGemColor))
                        || (vvvv5 != null && !vvvv5.getColor().equals(colors[4]) && !vvvv5.getColor().equals(m_caveGemColor))
                        || (vvvv6 != null && !vvvv6.getColor().equals(colors[4]) && !vvvv6.getColor().equals(m_caveGemColor))) {
                }else{
                    voxels.remove(v);
                    vox.removeVoxel(v.getPosition(), false);
                    a--;
                }
            }

        }//*/
        for(int a=0;a<voxels.size();a++){
            Voxel v = voxels.get(a);
            float x = v.getPosition().getX();
            float y = v.getPosition().getY();
            float z = v.getPosition().getZ();
            if(v.getColor().equals(caveColor))
            {
                voxels.remove(v);
                vox.removeVoxel(v.getPosition(), false);
                a--;//
            }

        }//*/
        vox = generateBiomes(i, j, vox);
        vox.setGenerateBelowZero(true);
        handlePreviousChunk(vox, (int)i, (int)j);
        e.add(vox);

        Debug.out("Chunk generated");
        return e;
    }

    // Generate moisture for tile
    private ComponentVoxelModel generateBiomes(float i, float j, ComponentVoxelModel vox){
        PerlinNoise biome = new PerlinNoise(0.1f, 0.01f, 6, 3, m_seed + 666);
        ComponentColor black = new ComponentColor(0, 0, 0);

        for(int x=0; x<m_chunkWidth; x++){
            for(int z=0; z<m_chunkDepth; z++){
                int y = (int)Math.floor(biome.getNoise2D(new Vector3(i*m_chunkWidth + x, j*m_chunkDepth + z, 0)).getValue());
                y+=biome.getAmplitude();
                while(y<0)
                    y+=biome.getAmplitude();
                while(y>biome.getAmplitude())
                    y-=biome.getAmplitude();
                Vector3 vvv = new Vector3(i*m_chunkWidth + x, 0, j * m_chunkDepth + z);
                Voxel vv = getVoxelAtHighest(vox, vvv);
                if(vv != null) {
                    vox.addVoxel(new Voxel(x, -1, z, vv.getSize(), new ComponentColor((int)vv.getPosition().getY()-(int)(m_pcg.getAmplitude()*1.5), y, 0)));
                    if(!vv.getColor().equals(m_caveGemColor) && !vv.getColor().equals(black))
                        vv.setColor(Biome.getBiome((int)vv.getPosition().getY()-(int)(m_pcg.getAmplitude()*1.5), y).getColors()[0]);
                }else{
                    vox.addVoxel(new Voxel(x, -1, z, getVoxelSize(), new ComponentColor(0, y, 0)));
                }
            }
        }
        return vox;
    }
    public Biome getBiome(Vector3 v){
        float el=0, hum=0;
        Vector3 clone = v.clone();
        clone.setY(-1);
        Voxel vv = getVoxel(clone);
        if(vv != null)
        {
            el = vv.getColor().getRed();
            hum = vv.getColor().getGreen();
        }

        return Biome.getBiome(el, hum);
    }
    public Biome getBiome(Voxel v){
        float el=0, hum=0;
        Vector3 clone = v.getPosition().clone();
        clone.setY(-1);
        Voxel vv = getVoxel(clone);
        if(vv != null)
        {
            el = vv.getColor().getRed();
            hum = vv.getColor().getGreen();
        }

        return Biome.getBiome(el, hum);
    }

    public Entity handlePreviousChunk(Entity chunk, int i, int j){
        ComponentVoxelModel vox = (ComponentVoxelModel)chunk.get(ComponentVoxelModel.class);
        handlePreviousChunk(vox, i, j);
        return chunk;
    }
    public ComponentVoxelModel handlePreviousChunk(ComponentVoxelModel vox, int i, int j){
        List<Vector3> vd = m_voxelsDestroyed.get(new Vector2(i, j));
        if(vd != null){
            Debug.out("Previous destroyed!!!!\n\n\n\n\n\n\n\nHi");
            for(Vector3 v: vd){
                vox.removeVoxel(v, false);
            }
        }
        List<Voxel> vp = m_voxelsPlaced.get(new Vector2(i, j));
        if(vp != null){
            for(Voxel v: vp){
                vox.addVoxel(v, false);
            }
        }
        return vox;
    }

    public void chunkRemoveVoxel(Vector3 v, int i, int j){
        List<Vector3> vx = m_voxelsDestroyed.get(new Vector2(i, j));
        if(vx != null){
            boolean has = false;
            for(Vector3 vv : vx){
                if(vv.equals(v)) {
                    has = true;
                    break;
                }
            }
            if(!has)
                vx.add(v);
        }else{
            vx = new ArrayList<>();
            vx.add(v);
            m_voxelsDestroyed.put(new Vector2(i, j), vx);
        }
        int hasVox = chunkHasAddVoxel(v, i , j);
        if(hasVox != -1)
        {
            m_voxelsPlaced.get(new Vector2(i, j)).remove(hasVox);
        }
    }

    public int chunkHasAddVoxel(Vector3 v, int i, int j){
        List<Voxel> vx = m_voxelsPlaced.get(new Vector2(i, j));
        if(vx != null)
        {
            for(int a=0;a<vx.size();a++){
                if(vx.get(a).getPosition().equals(v))
                    return a;
            }
        }
        return -1;
    }

    public void chunkAddVoxel(Voxel v, int i, int j){
        List<Voxel> vx = m_voxelsPlaced.get(new Vector2(i, j));
        if(vx != null){
            boolean has = false;
            for(Voxel vv : vx){
                if(vv.equals(v)) {
                    has = true;
                    break;
                }
            }
            if(!has)
                vx.add(v);
        }else{
            vx = new ArrayList<>();
            vx.add(v);
            m_voxelsPlaced.put(new Vector2(i, j), vx);
        }
    }

    public void generateFlat(){
        scrapWorld();
        m_worldType = WorldType.FLAT;
        for(int i=0;i<m_chunksLoadedXF;i++)
        {
            for(int j=0;j<m_chunksLoadedXF;j++)
            {
                Vector3 loc = new Vector3(i, .0f, j);
                Entity e = generateFlatChunk(i, j);
                addChunk(e, loc);
            }
        }
    }

    public Entity generateFlatChunk(int i, int j){
        ComponentColor[] colors = new ComponentColor[]{
                new ComponentColor(255,255,255),
                new ComponentColor(255,0,255)
        };
        Entity e = new Entity();

        Vector3 loc = new Vector3(i, .0f, j);
        ComponentVoxelModel vox = new ComponentVoxelModel(loc.getX() * m_chunkWidth * m_voxelSize, 0f, loc.getZ() * m_chunkDepth * m_voxelSize, m_voxelSize);
        for(int x=0; x<m_chunkWidth; x++){
            for(int z=0; z<m_chunkDepth; z++){
                vox.addVoxel(new Voxel(x, -1, z, m_voxelSize, colors[0]));
            }
        }
        vox.resetModel();
        e.add(vox);
        return e;
    }

    public List<Voxel > getAllVoxels(boolean includeBelowZero, boolean allFromMid){
        List<Voxel> ret = new ArrayList<>();

        Vector3 mid = this.getMiddleChunkVoxelFlat();
        for(Entity e : m_chunks){
            ComponentVoxelModel vmod = (ComponentVoxelModel)e.get(ComponentVoxelModel.class);
            List<Voxel > voxels = vmod.getVoxels();
            for(Voxel v : voxels){
                Vector3 pos = v.getPosition();
                if(pos.getY() >= 0 || includeBelowZero){
                    if(allFromMid) {
                        Voxel voxel = new Voxel((pos.getX() + vmod.getOffsetX() / getVoxelSize()) - mid.getX(), pos.getY(), (pos.getZ() + vmod.getOffsetZ() / getVoxelSize()) - mid.getZ(), v.getSize(), v.getColor());
                        ret.add(voxel);
                    }else {
                        Voxel voxel = new Voxel((pos.getX() + vmod.getOffsetX() / getVoxelSize()), pos.getY(), (pos.getZ() + vmod.getOffsetZ() / getVoxelSize()), v.getSize(), v.getColor());
                        ret.add(voxel);
                    }
                }
            }
        }

        return ret;
    }

    public void scrapWorld(){
        scrapWorld(false);
    }

    public void scrapWorld(boolean keepChunks){
        if(!keepChunks){
            for(Entity e : m_chunks){
                Framework.getInstance().getEntityStorage().removeEntity(e, true);
            }
            m_chunks.clear();
            m_chunkLocations.clear();
        }else{
            for(Entity e : m_chunks){
                ComponentVoxelModel vox = (ComponentVoxelModel)e.get(ComponentVoxelModel.class);
                vox.clearVoxels();
                vox.resetModel();
            }
        }
    }

    public void addChunk(Entity e, Vector3 v){
        m_chunks.add(e);
        m_chunkLocations.put(v, e);
        Debug.out("HASH: "+v.hashCode());

        Framework.getInstance().getEntityStorage().addEntity(e);
    }
    public Entity getChunk(Vector3 v){
        Entity e = m_chunkLocations.get(v);
        return e;
    }

    public Voxel getVoxelAtHighest(Vector3 v){
        Voxel ret = null;
        Vector3 chunkLoc = new Vector3((float)Math.floor(v.getX() / m_chunkWidth), .0f, (float)Math.floor(v.getZ() / m_chunkDepth));
        ComponentVoxelModel chunk = (ComponentVoxelModel) getChunk(chunkLoc).get(ComponentVoxelModel.class);
        Vector3 chunkVoxLoc = new Vector3(v.getX() % m_chunkWidth, v.getY(), v.getZ() % m_chunkDepth);
        List<Voxel> voxels = chunk.getVoxels();
        for(Voxel voxel : voxels){
            if(voxel.getPosition().getX() == chunkVoxLoc.getX() && voxel.getPosition().getZ() == chunkVoxLoc.getZ())
            {
                if(ret == null || voxel.getPosition().getY() > ret.getPosition().getY())
                    ret = voxel;
            }
        }

        return ret;
    }
    public Voxel getVoxelAtHighest(ComponentVoxelModel voxMod, Vector3 v){
        Voxel ret = null;
        Vector3 chunkVoxLoc = new Vector3(v.getX() % m_chunkWidth, v.getY(), v.getZ() % m_chunkDepth);
        List<Voxel> voxels = voxMod.getVoxels();
        for(Voxel voxel : voxels){
            if(voxel.getPosition().getX() == chunkVoxLoc.getX() && voxel.getPosition().getZ() == chunkVoxLoc.getZ())
            {
                if(ret == null || voxel.getPosition().getY() > ret.getPosition().getY())
                    ret = voxel;
            }
        }

        return ret;
    }

    public boolean removeChunk(Vector3 v){
        Entity e = getChunk(v);

        if(e != null){
            m_chunkLocations.remove(v);
            m_chunks.remove(e);
            Framework.getInstance().getEntityStorage().removeEntity(e, true);
            return true;
        }
        return false;
    }

    public Voxel getVoxel(Vector3 v){
        try {
            // Translate the world coordinate into chunk coordinates.
            Vector3 chunkLoc = new Vector3((float)Math.floor(v.getX() / m_chunkWidth), .0f, (float)Math.floor(v.getZ() / m_chunkDepth));
            ComponentVoxelModel chunk = (ComponentVoxelModel) getChunk(chunkLoc).get(ComponentVoxelModel.class);
            Vector3 chunkVoxLoc = new Vector3(v.getX() % m_chunkWidth, v.getY(), v.getZ() % m_chunkDepth);
            return chunk.getVoxel(chunkVoxLoc);
        }catch(Exception e)
        {
            return null;
        }
    }

    public boolean addVoxel(Vector3 v, ComponentColor vColor){
        try {
            // Translate the world coordinate into chunk coordinates.
            Vector3 chunkLoc = new Vector3((float)Math.floor(v.getX() / m_chunkWidth), .0f, (float)Math.floor(v.getZ() / m_chunkDepth));
            ComponentVoxelModel chunk = (ComponentVoxelModel) getChunk(chunkLoc).get(ComponentVoxelModel.class);
            Vector3 chunkVoxLoc = new Vector3(v.getX() % m_chunkWidth, v.getY(), v.getZ() % m_chunkDepth);
            if(chunk.getVoxel(chunkVoxLoc) == null) {
                Voxel vv = new Voxel((int) chunkVoxLoc.getX(), (int) chunkVoxLoc.getY(), (int) chunkVoxLoc.getZ(), getVoxelSize(), vColor);
                chunk.addVoxel(vv, false);
                rebootChunk(v);
                chunkAddVoxel(vv, (int)chunkLoc.getX(), (int)chunkLoc.getZ());
                return true;
            }
        }catch(Exception e)
        {
        }
        return false;
    }

    public boolean rebootChunk(Vector3 v){
        return rebootChunk(v, 0);
    }

    public boolean rebootChunk(Vector3 v, int ln){
        Vector3 chunkLoc = new Vector3((float)Math.floor(v.getX() / m_chunkWidth), .0f, (float)Math.floor(v.getZ() / m_chunkDepth));
        if(getChunk(chunkLoc) != null)
            try {
                // Translate the world coordinate into chunk coordinates.
                ComponentVoxelModel chunk = (ComponentVoxelModel) getChunk(chunkLoc).get(ComponentVoxelModel.class);
                if(chunk != null) {
                    Vector3 n = chunkLoc.clone();
                    Vector3 e = chunkLoc.clone();
                    Vector3 s = chunkLoc.clone();
                    Vector3 w = chunkLoc.clone();
                    n.modZ(-1);
                    e.modX(1);
                    s.modZ(1);
                    w.modX(-1);

                    if(ln == 0)
                    {
                        Vector3 tn = v.clone();
                        tn.modX(1*m_chunkWidth);
                        rebootChunk(tn, ln+1);
                        tn.modX(-2*m_chunkWidth);
                        rebootChunk(tn, ln+1);
                        tn.modX(1*m_chunkWidth);
                        tn.modZ(-1*m_chunkDepth);
                        rebootChunk(tn, ln+1);
                        tn.modZ(2*m_chunkDepth);
                        rebootChunk(tn, ln+1);
                    }
                    ComponentVoxelModel nvox = null;
                    Entity nchunk = getChunk(n);
                    if(nchunk != null)
                        nvox = ((ComponentVoxelModel)nchunk.get(ComponentVoxelModel.class));
                    ComponentVoxelModel evox = null;
                    Entity echunk = getChunk(e);
                    if(echunk != null)
                        evox = ((ComponentVoxelModel)echunk.get(ComponentVoxelModel.class));
                    ComponentVoxelModel svox = null;
                    Entity schunk = getChunk(s);
                    if(schunk != null)
                        svox = ((ComponentVoxelModel)schunk.get(ComponentVoxelModel.class));
                    ComponentVoxelModel wvox = null;
                    Entity wchunk = getChunk(w);
                    if(wchunk != null)
                        wvox = ((ComponentVoxelModel)wchunk.get(ComponentVoxelModel.class));

                    chunk.reboot(nvox, evox, svox, wvox, m_chunkWidth, m_chunkDepth);
                    return true;
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        return false;
    }
    public Voxel removeVoxel(Vector3 v){
        return removeVoxel(v, true);
    }
    public Voxel removeVoxel(Vector3 v, boolean reboot){
        try {
            // Translate the world coordinate into chunk coordinates.
            Vector3 chunkLoc = new Vector3((float)Math.floor(v.getX() / m_chunkWidth), .0f, (float)Math.floor(v.getZ() / m_chunkDepth));
            ComponentVoxelModel chunk = (ComponentVoxelModel) getChunk(chunkLoc).get(ComponentVoxelModel.class);
            if(chunk == null)
                return null;
            Vector3 chunkVoxLoc = new Vector3(v.getX() % m_chunkWidth, v.getY(), v.getZ() % m_chunkDepth);
            Voxel ret = chunk.removeVoxel(chunkVoxLoc, false);
            chunkRemoveVoxel(chunkVoxLoc, (int)chunkLoc.getX(), (int)chunkLoc.getZ());
            if(reboot)
                rebootChunk(v);
            return ret;
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean chunkExists(Vector3 v){
        try{
            Vector3 chunkLoc = new Vector3((float)Math.floor(v.getX() / m_chunkWidth), .0f, (float)Math.floor(v.getZ() / m_chunkDepth));
            ComponentVoxelModel chunk = (ComponentVoxelModel) getChunk(chunkLoc).get(ComponentVoxelModel.class);
            if(chunk != null)
                return true;
        }catch(Exception e){

        }
        return false;
    }


    private float m_lastX=-1, m_lastZ=-1;

    private Thread m_generationThread;

    public void loadChunks(float x, float z){
        if((m_lastX == (float)Math.floor(x / m_chunkWidth) && m_lastZ == (float)Math.floor(z / m_chunkDepth)) || m_generationThread != null || getWorldType() == WorldType.FLAT)
            return;
        m_lastX = (float)Math.floor(x / m_chunkWidth);
        m_lastZ = (float)Math.floor(z / m_chunkDepth);

        int d = c_chunkThreadDelay;
        if(!chunkExists(new Vector3(m_lastX*m_chunkWidth, 0, m_lastZ*m_chunkWidth)))
            d = 0;
        final int delay = d;

        m_generationThread = new Thread() {

            public void run() {
                HashMap<Vector2, Entity> newChunks = new HashMap<>();
                switch (getWorldType()) {
                    case VOID:
                        break;
                    case GRASSLAND:
                        for (int i = (int) Math.floor((m_lastX - m_chunksLoadedX)); i < (int) Math.floor((m_lastX + m_chunksLoadedX)); i += 1) {
                            for (int j = (int) Math.floor((m_lastZ - m_chunksLoadedX)); j < (int) Math.floor((m_lastZ + m_chunksLoadedX)); j += 1) {
                                int nx = i * m_chunkWidth;
                                int nz = j * m_chunkDepth;
                                if (!chunkExists(new Vector3(nx, 0, nz))) {
                                    nx = (int) Math.floor(i);
                                    nz = (int) Math.floor(j);
                                    try {
                                        Entity newChunk = generateGrassLandChunk(nx, nz);
                                        newChunks.put(new Vector2(nx, nz), newChunk);
                                        //for(int a = -1; a<=1;a++)
                                        //for(int b = -1;b<=1;b++)
                                    }catch(Exception e){
                                        e.printStackTrace();
                                        System.exit(-1);
                                    }
                                }
                            }
                        }
                        break;
                    case FLAT:
                        break;
                }
                for(Vector2 v : newChunks.keySet().toArray(new Vector2[0]))
                {
                    try{
                        this.sleep(delay);
                    }catch(Exception e){

                    }
                    addChunk(newChunks.get(v), new Vector3(v.getX(), 0 ,v.getY()));
                    rebootChunk(new Vector3(v.getX()*m_chunkWidth + 1, 0, v.getY()*m_chunkDepth + 1));
                }

                // Work on removing chunks that are too far away that they no longer matter.
                try {
                    Vector3[] existingChunks = m_chunkLocations.keySet().toArray(new Vector3[0]);
                    for (Vector3 v : existingChunks) {
                        if (Math.abs(v.getX() - m_lastX) >= m_chunksUnloadedX || Math.abs(v.getZ() - m_lastZ) >= m_chunksUnloadedX) {
                            try{
                                this.sleep(delay);
                            }catch(Exception e){

                            }
                            removeChunk(v);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                m_generationThread = null;
            }
        };
        m_generationThread.start();
        if(d == 0)
            while(!chunkExists(new Vector3(m_lastX*m_chunkWidth, 0, m_lastZ*m_chunkWidth))){
            //basically just wait until thread is done.

            }

    }

    public static String getDirection(float yaw){
        yaw += Math.toRadians(45);
        yaw = yaw % (float)Math.toRadians(360);
        if(yaw >= Math.toRadians(270))
            return "EAST";
        else if(yaw >= Math.toRadians(180))
            return "SOUTH";
        else if(yaw >= Math.toRadians(90))
            return "WEST";
        else
            return "NORTH";
    }



    public static class Spawn{
        private Vector3 m_position;
        private int m_team;

        public Spawn(Vector3 pos, int team){
            m_position = pos;
            m_team = team;
        }

        public int getTeam(){
            return m_team;
        }
        public Vector3 getPosition(){
            return m_position;
        }
    }
}
