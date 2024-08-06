package me.k128.mcbeGeoParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import me.k128.mcbeGeoParser.exception.InvalidGeometryException;
import me.k128.mcbeGeoParser.exception.UnsupportedFormatException;

import me.k128.mcbeGeoParser.utilities.Vec2i;
import me.k128.mcbeGeoParser.utilities.Vec3f;
/**
 * 
 * @author <a href="https://github.com/El-Karto-Muesca"> k-128 <a/>
 */
public class BedrockGeometry {

    private final String formatVersion;
    private final Geometry[] geometry;
    
    BedrockGeometry(String formatVersion, Geometry[] geometry) {
        this.formatVersion = formatVersion;
        this.geometry = geometry;
    }

    public static BedrockGeometry parse(String filepath) throws 
        ParseException, 
        FileNotFoundException, 
        IOException, 
        UnsupportedFormatException, 
        InvalidGeometryException {
        return Parser.parse(filepath);
    }
    
    public static BedrockGeometry parse(File file) throws 
        ParseException, 
        FileNotFoundException, 
        IOException, 
        UnsupportedFormatException, 
        InvalidGeometryException {
        return Parser.parse(file);
    }

    public String getFormatVersion() {
        return formatVersion;
    }
    public Geometry[] getGeometry() {
        return geometry;
    }
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
    
        public Description getDescription() {
            return description;
        }
        public Bone[] getBones() {
            return bones;
        }
    
        public int getBoneCount() {
            return bones.length;
        }
        public int getCubeCount() {
            return cubeCount;
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
    
        public String getName() {
            return name;
        }
        public Bone getParent() {
            return parent;
        }
        void setParent(Bone parent) {
            this.parent = parent;
        }
        String getParentName() {
            return parentName;
        }
        public Vec3f getPivot() {
            return pivot;
        }
        public Cube[] getCubes() {
            return cubes;
        }
        public Locator[] getLocators() {
            return locators;
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
    
        public String getIdentifier() {
            return identifier;
        }
        public int getTextureWidth() {
            return textureWidth;
        }
        public int getTextureHeight() {
            return textureHeight;
        }
        public float getVisibleBoundsWidth() {
            return visibleBoundsWidth;
        }
        public float getVisibleBoundsHeight() {
            return visibleBoundsHeight;
        }
        public Vec3f getVisibleBoundsOffset() {
            return visibleBoundsOffset;
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
    
        public Vec3f getOrigin() {
            return origin;
        }
        public Vec3f getSize() {
            return size;
        }
        public float getInflate() {
            return inflate;
        }
        public Vec3f getPivot() {
            return pivot;
        }
        public Vec3f getRotation() {
            return rotation;
        }
        public UVType getUVType() {
            return uvType;
        }
        public Vec2i getBoxUV() {
            return boxUV;
        }
        public PerfaceUV getPerfaceUV() {
            return perfaceUV;
        }
    }

    public static class Face {
        private final Vec2i uv, uvSize;
    
        Face(Vec2i uv, Vec2i uvSize) {
            this.uv = uv;
            this.uvSize = uvSize;
        }
    
        public Vec2i getUV() {
            return uv;
        }
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
    
        public Face getNorth() { return faces[0]; }
        public Face getEast() { return faces[1]; }
        public Face getSouth() { return faces[2]; }
        public Face getWest() { return faces[3]; }
        public Face getUp() { return faces[4]; }
        public Face getDown() { return faces[5]; }
    }

    public enum UVType {
        BOX,
        PERFACE 
    }
}
