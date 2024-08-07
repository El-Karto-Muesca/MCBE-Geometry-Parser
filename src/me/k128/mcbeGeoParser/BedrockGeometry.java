package me.k128.mcbeGeoParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

/**
 * @author <a href="https://github.com/El-Karto-Muesca"> k-128 <a/>
 */
public class BedrockGeometry {

    private final String formatVersion;
    private final Geometry[] geometry;
    
    BedrockGeometry(String formatVersion, Geometry[] geometry) {
        this.formatVersion = formatVersion;
        this.geometry = geometry;
    }

    /**
     * @param filepath to the {@code .geo.json} file. And yes you must include the {@code .geo.json} extention in the URL string. 
     * (Format versions supported are {@code 1.12.0} or higher!)
     * @return a {@link BedrockGeometry} object.
     * @throws FileNotFoundException if your mo... I mean your file was not found... come on, you think I was going to say you MOM? pff... or was I? (piano in bg)
     * @throws IOException if the I/O operations failed or got interrupted.
     * @throws ParseException if the JOSN file is cursed and the JSONParser couldn't parse these nu... couldn't parse the file.
     * @throws UnsupportedFormatException if the format version in earlier than {@code 1.12.0}.
     * @throws InvalidGeometryException if the geometry is cursed, meaning a major property couldn't be found.
     * @author <a href="https://github.com/El-Karto-Muesca"> k-128 <a/>
     * @see #parse(File file)
     */
    public static BedrockGeometry parse(String filepath) throws 
        ParseException, 
        FileNotFoundException, 
        IOException, 
        UnsupportedFormatException, 
        InvalidGeometryException {
        return Parser.parse(filepath);
    }
    
    // /**
    //  * @param file the {@code .geo.json} file. (Format versions supported are {@code 1.12.0} or higher!)
    //  * @return a {@link BedrockGeometry} object.
    //  * @throws FileNotFoundException if your mo... I mean your file was not found... come on, you think I was going to say you MOM? pff... or was I? (piano in bg)
    //  * @throws IOException if the I/O operations failed or got interrupted.
    //  * @throws ParseException if the JOSN file is cursed and the JSONParser couldn't parse these nu... couldn't parse the file.
    //  * @throws UnsupportedFormatException if the format version in earlier than {@code 1.12.0}.
    //  * @throws InvalidGeometryException if the geometry is cursed, meaning a major property couldn't be found.
    //  * @author <a href="https://github.com/El-Karto-Muesca"> k-128 <a/>
    //  * @see #parse(String filepath)
    //  */
    // public static BedrockGeometry parse(File file) throws 
    //     ParseException, 
    //     FileNotFoundException, 
    //     IOException, 
    //     UnsupportedFormatException, 
    //     InvalidGeometryException {
    //     return Parser.parse(file);
    // }

    /**
     * @return the {@code "format_version"}.
     * @see #getGeometry()
     * @see #getGeometryCount()
     */
    public String getFormatVersion() {
        return formatVersion;
    }
    /**
     * @return the {@code "minecraft:geometry"} array.
     * @see #getFormatVersion()
     * @see #getGeometryCount()
     */
    public Geometry[] getGeometry() {
        return geometry;
    }
    /**
     * @return {@link #getGeometry()}.length
     * @see #getFormatVersion()
     * @see #getGeometry()
     */
    public int getGeometryCount() {
        return geometry.length;
    }

    // ---------------- >[ INNER MODELS ]< ---------------- //

    
    public static class Geometry {
        private final Description description;
        private final Bone[] bones;
    
        private final int cubeCount;
    
        Geometry(Description description, Bone[] bones) {
            this.description = description;
            this.bones = bones;
            // counts the cubes of each bone and adds them up
            int cubeCount = 0;
            for (Bone bone : bones) cubeCount += bone.getCubes().length;
            this.cubeCount = cubeCount;
        }

        /**
         * @return the {@code "description"} object.
         * @see #getBones()
         * @see #getBoneCount()
         * @see #getCubeCount()
         */
        public Description getDescription() {
            return description;
        }
        /**
         * @return the {@code "bones"} array. 
         * @see #getDescription()
         * @see #getBoneCount()
         * @see #getCubeCount()
         */
        public Bone[] getBones() {
            return bones;
        }

        /**
         * @return {@link #getBones()}.length
         * @see #getDescription()
         * @see #getBones()
         * @see #getCubeCount()
         */
        public int getBoneCount() {
            return bones.length;
        }
        /**
         * @return the sum of the amount of cubes of all the bones combined.
         * @see #getDescription()
         * @see #getBones()
         * @see #getBoneCount()
         */
        public int getCubeCount() {
            return cubeCount;
        }
    }

    public static class Description {
        private final String identifier;
        private final int textureWidth;
        private final int textureHeight;
        private final float visibleBoundsWidth;
        private final float visibleBoundsHeight;
        private final Vec3f visibleBoundsOffset;
        
        Description(
            String identifier, 
            int textureWidth, 
            int textureHeight, 
            float visibleBoundsWidth,
            float visibleBoundsHeight, 
            Vec3f visibleBoundsOffset
        ) {
            this.identifier = identifier;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            this.visibleBoundsWidth = visibleBoundsWidth;
            this.visibleBoundsHeight = visibleBoundsHeight;
            this.visibleBoundsOffset = visibleBoundsOffset;
        }
    
        /**
         * @return the {@code identifier}.
         * @see #getTextureWidth()
         * @see #getTextureHeight()
         * @see #getVisibleBoundsWidth()
         * @see #getVisibleBoundsHeight()
         * @see #getVisibleBoundsOffset()
         */
        public String getIdentifier() {
            return identifier;
        }
        /**
         * @return the {@code texture_width}.
         * @see #getIdentifier()
         * @see #getTextureHeight()
         * @see #getVisibleBoundsWidth()
         * @see #getVisibleBoundsHeight()
         * @see #getVisibleBoundsOffset()
         */
        public int getTextureWidth() {
            return textureWidth;
        }
        /**
         * @return the {@code texture_height}.
         * @see #getIdentifier()
         * @see #getTextureWidth()
         * @see #getVisibleBoundsWidth()
         * @see #getVisibleBoundsHeight()
         * @see #getVisibleBoundsOffset()
         */
        public int getTextureHeight() {
            return textureHeight;
        }
        /**
         * @return the {@code visible_bounds_width}.
         * @see #getIdentifier()
         * @see #getTextureWidth()
         * @see #getTextureHeight()
         * @see #getVisibleBoundsHeight()
         * @see #getVisibleBoundsOffset()
         */
        public float getVisibleBoundsWidth() {
            return visibleBoundsWidth;
        }
        /**
         * @return the {@code visible_bounds_height}.
         * @see #getIdentifier()
         * @see #getTextureWidth()
         * @see #getTextureHeight()
         * @see #getVisibleBoundsWidth()
         * @see #getVisibleBoundsOffset()
         */
        public float getVisibleBoundsHeight() {
            return visibleBoundsHeight;
        }
        /**
         * @return the {@code visible_bounds_offset}.
         * @see #getIdentifier()
         * @see #getTextureWidth()
         * @see #getTextureHeight()
         * @see #getVisibleBoundsWidth()
         * @see #getVisibleBoundsHeight()
         */
        public Vec3f getVisibleBoundsOffset() {
            return visibleBoundsOffset;
        }
    }

    public static class Bone {
        private final String name;
        private final String parentName;
        private Bone parent = null;
        private final Vec3f pivot;
        private final Cube[] cubes;
        private final Locator[] locators;
    
        Bone(
            String name,
            String parentName,
            Vec3f pivot,
            Cube[] cubes,
            Locator[] locators
        ) {
            this.name = name;
            this.parentName = parentName;
            this.pivot = pivot;
            this.cubes = cubes;
            this.locators = locators;
        }
    
        /**
         * @return the bone's {@code name}.
         * @see #getParent()
         * @see #getPivot()
         * @see #getCubes()
         * @see #getLocators()
         */
        public String getName() {
            return name;
        }
        /**
         * @return the bone's {@code parent}.
         * @see #getName()
         * @see #getPivot()
         * @see #getCubes()
         * @see #getLocators()
         */
        public Bone getParent() {
            return parent;
        }
        void setParent(Bone parent) {
            this.parent = parent;
        }
        String getParentName() {
            return parentName;
        }
        /**
         * @return the bone's {@code pivot}.
         * @see #getName()
         * @see #getParent()
         * @see #getCubes()
         * @see #getLocators()
         */
        public Vec3f getPivot() {
            return pivot;
        }
        /**
         * @return the bone's array of {@code cubes}.
         * @see #getName()
         * @see #getParent()
         * @see #getPivot()
         * @see #getLocators()
         */
        public Cube[] getCubes() {
            return cubes;
        }
        /**
         * @return the bone's array of {@code locators}.
         * @see #getName()
         * @see #getParent()
         * @see #getPivot()
         * @see #getCubes()
         */
        public Locator[] getLocators() {
            return locators;
        }
    }

    public static class Cube {
        private final Vec3f origin;
        private final Vec3f size;
        private final float inflate;
        private final Vec3f pivot;
        private final Vec3f rotation;
        private final UVType uvType;
        private final Vec2i boxUV;
        private final PerfaceUV perfaceUV;
    
        Cube(
            Vec3f origin, 
            Vec3f size, 
            float inflate, 
            Vec3f pivot, 
            Vec3f rotation, 
            UVType uvType, 
            Vec2i boxUV,
            PerfaceUV perfaceUV
        ) {
            this.origin = origin;
            this.size = size;
            this.inflate = inflate;
            this.pivot = pivot;
            this.rotation = rotation;
            this.uvType = uvType;
            this.boxUV = boxUV;
            this.perfaceUV = perfaceUV;
        }
    
        /**
         * @return The cube's {@code origin}.
         * @see #getSize()
         * @see #getInflate()
         * @see #getPivot()
         * @see #getRotation()
         * @see #getUVType()
         * @see #getBoxUV()
         * @see #getPerfaceUV()
         */
        public Vec3f getOrigin() {
            return origin;
        }

        /**
         * @return The cube's {@code size}.
         * @see #getOrigin()
         * @see #getInflate()
         * @see #getPivot()
         * @see #getRotation()
         * @see #getUVType()
         * @see #getBoxUV()
         * @see #getPerfaceUV()
         */
        public Vec3f getSize() {
            return size;
        }

        /**
         * @return The cube's {@code inflate} factor.
         * @see #getOrigin()
         * @see #getSize()
         * @see #getPivot()
         * @see #getRotation()
         * @see #getUVType()
         * @see #getBoxUV()
         * @see #getPerfaceUV()
         */
        public float getInflate() {
            return inflate;
        }

        /**
         * @return The cube's {@code pivot} point.
         * @see #getOrigin()
         * @see #getSize()
         * @see #getInflate()
         * @see #getRotation()
         * @see #getUVType()
         * @see #getBoxUV()
         * @see #getPerfaceUV()
         */
        public Vec3f getPivot() {
            return pivot;
        }

        /**
         * @return The cube's {@code rotation}.
         * @see #getOrigin()
         * @see #getSize()
         * @see #getInflate()
         * @see #getPivot()
         * @see #getUVType()
         * @see #getBoxUV()
         * @see #getPerfaceUV()
         */
        public Vec3f getRotation() {
            return rotation;
        }

        /**
         * @return The cube's {@link UVType}.
         * It can be a {@link UVType BOX} or a {@link UVType PERFACE} 
         * @see #getOrigin()
         * @see #getSize()
         * @see #getInflate()
         * @see #getPivot()
         * @see #getRotation()
         * @see #getBoxUV()
         * @see #getPerfaceUV()
         */
        public UVType getUVType() {
            return uvType;
        }

        /**
         * @return The cube's box {@code uv} coordinates if the {@link UVType} is {@link UVType BOX}, otherwise {@code null}.
         * @see #getOrigin()
         * @see #getSize()
         * @see #getInflate()
         * @see #getPivot()
         * @see #getRotation()
         * @see #getUVType()
         * @see #getPerfaceUV()
         */
        public Vec2i getBoxUV() {
            return boxUV;
        }

        /**
         * @return The cube's perface {@code uv} coordinates if the {@link UVType} is {@link UVType PERFACE}, otherwise {@code null}.
         * @see #getOrigin()
         * @see #getSize()
         * @see #getInflate()
         * @see #getPivot()
         * @see #getRotation()
         * @see #getUVType()
         * @see #getBoxUV()
         */
        public PerfaceUV getPerfaceUV() {
            return perfaceUV;
        }

    }

    public static class Locator {
        private final String name;
        private final Vec3f coords;
    
        Locator(
            String name, 
            Vec3f coords
        ) {
            this.name = name;
            this.coords = coords;
        }
        
        public String getName() {
            return name;
        }
        public Vec3f getCoords() {
            return coords;
        }
    }

    public static class Face {
        private final Vec2i uv, uvSize;
    
        Face(Vec2i uv, Vec2i uvSize) {
            this.uv = uv;
            this.uvSize = uvSize;
        }   
         
        /**
         * @return The face's {@code uv}.
         * @see #getUVSize()
         */
        public Vec2i getUV() {
            return uv;
        }
        /**
         * @return The face's {@code uv_size}.
         * @see #getUV()
         */
        public Vec2i getUVSize() {
            return uvSize;
        }
    }

    public static class PerfaceUV {
        private final Face[] faces;
    
        PerfaceUV(
            Face north,
            Face east,
            Face south,
            Face west,
            Face up,
            Face down
        ) {
            this.faces = new Face[] {
                north,
                east,
                south,
                west,
                up,
                down
            };
        }

        /**
         * @return The {@code Face} on the north side.
         * @see #getEast()
         * @see #getSouth()
         * @see #getWest()
         * @see #getUp()
         * @see #getDown()
         */
        public Face getNorth() { return faces[0]; }
        /**
         * @return The {@code Face} on the east side.
         * @see #getNorth()
         * @see #getSouth()
         * @see #getWest()
         * @see #getUp()
         * @see #getDown()
         */
        public Face getEast() { return faces[1]; }
        /**
         * @return The {@code Face} on the south side.
         * @see #getNorth()
         * @see #getEast()
         * @see #getWest()
         * @see #getUp()
         * @see #getDown()
         */
        public Face getSouth() { return faces[2]; }
        /**
         * @return The {@code Face} on the west side.
         * @see #getNorth()
         * @see #getEast()
         * @see #getSouth()
         * @see #getUp()
         * @see #getDown()
         */
        public Face getWest() { return faces[3]; }
        /**
         * @return The {@code Face} on the top side.
         * @see #getNorth()
         * @see #getEast()
         * @see #getSouth()
         * @see #getWest()
         * @see #getDown()
         */
        public Face getUp() { return faces[4]; }
        /**
         * @return The {@code Face} on the bottom side.
         * @see #getNorth()
         * @see #getEast()
         * @see #getSouth()
         * @see #getWest()
         * @see #getUp()
         */
        public Face getDown() { return faces[5]; }
    }

    public enum UVType {
        BOX,
        PERFACE 
    }
}
