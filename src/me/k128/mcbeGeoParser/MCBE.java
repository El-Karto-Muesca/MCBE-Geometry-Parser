package me.k128.mcbeGeoParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.k128.mcbeGeoParser.utilities.Vec3f;
import me.k128.mcbeGeoParser.utilities.Vec2i;
import me.k128.mcbeGeoParser.exception.InvalidGeometry;
import me.k128.mcbeGeoParser.exception.UnsupportedFormatException;

public class MCBE {
    public final static class Geo {
        private static final String[] SUPPORTED_FORMATS = {
            "1.16.0",
            "1.12.0"
        }; 
        
        public static Model parse(String filepath) throws ParseException, FileNotFoundException, IOException, UnsupportedFormatException, InvalidGeometry {
            return parse(new File(filepath));
        }
        
        public static Model parse(File file) throws ParseException, FileNotFoundException, IOException, UnsupportedFormatException, InvalidGeometry {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(file));
            return parse(jsonObject);
        }

        private static Model parse(JSONObject jsonObject) throws UnsupportedFormatException, InvalidGeometry {
            String format = "";
            // format checking
            try { formatCheck(format = (String) jsonObject.get("format_version")); } 
            catch (NullPointerException exception) { throw new InvalidGeometry("format_version"); }
            return new Model(format, getGeometry(jsonObject));
        }

        private static void formatCheck(String format) throws UnsupportedFormatException {
            for (String supportedFormat : SUPPORTED_FORMATS) if (format.equals(supportedFormat)) return;
            throw new UnsupportedFormatException(format);
        }

        private static Geometry[] getGeometry(JSONObject jsonObject) {
            List<Geometry> geometry = new ArrayList<>();
            JSONArray jsonArray = getJSONArray(jsonObject, "minecraft:geometry");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject geo = (JSONObject) jsonArray.get(i);
                geometry.add(new Geometry(
                    getDescription(getJSONObject(geo, "description")), 
                    getBones(getJSONArray(geo, "bones"))
                ));
            }
            return geometry.toArray(new Geometry[geometry.size()]);
        }

        private static Description getDescription(JSONObject jsonObject) {
            return new Description(
                getString(jsonObject, "identifier"), 
                getInt(jsonObject, "texture_width"), 
                getInt(jsonObject, "texture_height"), 
                getFloat(jsonObject, "visible_bounds_width", 1), 
                getFloat(jsonObject, "visible_bounds_height", 1), 
                getVec3f(jsonObject, "visible_bounds_offset", new Vec3f())
            );
        }

        private static Bone[] getBones(JSONArray jsonArray) {
            List<Bone> bones = new ArrayList<>();
            // parsing the bones
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = getJSONObject(jsonArray, i);
                bones.add(new Bone(
                    getString(jsonObject, "name"), 
                    getString(jsonObject, "parent"),
                    getVec3f(jsonObject, "pivot", new Vec3f()), 
                    getCubes(getJSONArray(jsonObject, "cubes")),
                    getLocators(jsonObject)
                ));
            }
            // Setting each bone parent
            for (Bone bone : bones) {
                for (Bone bone2 : bones) {
                    if (bone2.getName().equals(bone.getParentName())) { bone.setParent(bone2); }
                }
            }
            return bones.toArray(new Bone[bones.size()]);
        }
        
        private static Cube[] getCubes(JSONArray jsonArray) {
            List<Cube> cubes = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = getJSONObject(jsonArray, i);
                UVType uvType = getUVType(jsonObject);
                cubes.add(new Cube(
                    getVec3f(jsonObject, "origin", new Vec3f()), 
                    getVec3f(jsonObject, "size", new Vec3f(1)), 
                    getFloat(jsonObject, "inflate", 1), 
                    getVec3f(jsonObject, "povit", new Vec3f()), 
                    getVec3f(jsonObject, "rotation", new Vec3f()), 
                    uvType, 
                    (uvType == UVType.BOX) ? getVec2i(jsonObject, "uv") : null,
                    (uvType == UVType.PERFACE) ? getPerfaceUV(getJSONObject(jsonObject, "uv")) : null
                ));
            }
            return cubes.toArray(new Cube[cubes.size()]);
        }

        private static Locator[] getLocators(JSONObject location) {
            List<Locator> locators = new ArrayList<>();

            Object object = location.get("locators");
            if (object != null) {
                JSONObject jsonObject = getJSONObject(location, "locators");
                @SuppressWarnings("unchecked")
                Set<String> keys = jsonObject.keySet();
                for (String key : keys) locators.add(new Locator(key, getVec3f(jsonObject, key, new Vec3f())));
            }

            return locators.toArray(new Locator[locators.size()]);
        }

        private static PerfaceUV getPerfaceUV(JSONObject jsonObject) {
            JSONObject[] faces = new JSONObject[] {
                getJSONObject(jsonObject, "north"),
                getJSONObject(jsonObject, "east"),
                getJSONObject(jsonObject, "south"),
                getJSONObject(jsonObject, "west"),
                getJSONObject(jsonObject, "up"),
                getJSONObject(jsonObject, "down")
            };
            return new PerfaceUV(
                faces[0] != null ? new Face(getVec2i(faces[0], "uv"), getVec2i(faces[0], "uv_size")) : new Face(new Vec2i(), new Vec2i()),
                faces[1] != null ? new Face(getVec2i(faces[1], "uv"), getVec2i(faces[1], "uv_size")) : new Face(new Vec2i(), new Vec2i()),
                faces[2] != null ? new Face(getVec2i(faces[2], "uv"), getVec2i(faces[2], "uv_size")) : new Face(new Vec2i(), new Vec2i()),
                faces[3] != null ? new Face(getVec2i(faces[3], "uv"), getVec2i(faces[3], "uv_size")) : new Face(new Vec2i(), new Vec2i()),
                faces[4] != null ? new Face(getVec2i(faces[4], "uv"), getVec2i(faces[4], "uv_size")) : new Face(new Vec2i(), new Vec2i()),
                faces[5] != null ? new Face(getVec2i(faces[5], "uv"), getVec2i(faces[5], "uv_size")) : new Face(new Vec2i(), new Vec2i())
            );
        }

        // [ -------- < Utility Methods > -------- ] //

        // #region Utility Methods

        private static JSONObject getJSONObject(JSONArray jsonArray, int index) {
            return (JSONObject) jsonArray.get(index);
        }
        private static JSONObject getJSONObject(JSONObject jsonObject, String key) {
            return (JSONObject) jsonObject.get(key);
        }

        private static String getString(JSONObject jsonObject, String key) {
            Object object = jsonObject.get(key);
            return (object != null) ? (String) object : "";
        }

        private static float getFloat(JSONObject jsonObject, String key, float def) {
            Object object = jsonObject.get(key);
            return (object != null) ? castToFloat(object) : def;
        }
        private static float getFloat(JSONArray jsonArray, int index, float def) {
            Object object = jsonArray.get(index);
            return (object != null) ? castToFloat(object) : def;
        }

        private static int getInt(JSONObject jsonObject, String key) {
            Object object = jsonObject.get(key);
            return (object != null) ? castToInt(object) : 0;
        }
        private static int getInt(JSONArray jsonArray, int index) {
            Object object = jsonArray.get(index);
            return (object != null) ? castToInt(object) : 0;
        }

        private static float castToFloat(Object object) {
            return switch(object) {
                case Long o -> (float) o;
                case Double o -> (float) (double) o;
                default -> 0f;
            };
        }
        private static int castToInt(Object object) {
            return (object instanceof Long) ? (int) (long) object : Math.round(castToFloat(object));
        }

        private static Vec3f getVec3f(JSONObject jsonObject, String key, Vec3f def) {
            Object object = jsonObject.get(key);
            return (object != null) ? new Vec3f(
                getFloat(getJSONArray(jsonObject, key), 0, def.getX()),
                getFloat(getJSONArray(jsonObject, key), 1, def.getY()),
                getFloat(getJSONArray(jsonObject, key), 2, def.getZ())
            ) : def;
        }
        private static Vec2i getVec2i(JSONObject jsonObject, String key) {
            Object object = jsonObject.get(key);
            return (object != null) ? new Vec2i(
                getInt(getJSONArray(jsonObject, key), 0),
                getInt(getJSONArray(jsonObject, key), 1)
            ) : new Vec2i(0, 0);
        }

        private static JSONArray getJSONArray(JSONObject jsonObject, String key) {
            Object object = jsonObject.get(key);
            return object != null ? (JSONArray) object : new JSONArray();
        }

        private static UVType getUVType(JSONObject jsonObject) {
            if (jsonObject.get("uv") == null) return null;
            return switch(jsonObject.get("uv")) {
                case JSONObject o -> UVType.PERFACE;
                case JSONArray o -> UVType.BOX;
                default -> null;
            };
        }

        //#endregion

        //#region Models

        public static class Model {
            private final String formatVersion;
            private final Geometry[] geometry;
        
            private Model(String formatVersion, Geometry[] geometry) {
                this.formatVersion = formatVersion;
                this.geometry = geometry;
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
        }

        public static class Geometry {
            private final Description description;
            private final Bone[] bones;
        
            private final int cubeCount;
        
            private Geometry(Description description, Bone[] bones) {
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
        
            private Bone(
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
            
            private Description(
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
        
            private Cube(
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
        
            private Face(Vec2i uv, Vec2i uvSize) {
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
        
            private PerfaceUV(
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
}
