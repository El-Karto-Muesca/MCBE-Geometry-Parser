package me.k128.mcbeGeoParser;

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

import me.k128.mcbeGeoParser.BedrockGeometry.*;

class Parser { 

    private record Format(String version, boolean legacy) {}
    /**
     * These are the tested and supported formats.
     */
    private static final Format[] SUPPORTED_FORMATS = {
        new Format("1.16.0", false),
        new Format("1.12.0", false),
        new Format("1.10.0", true),
        new Format("1.8.0", true)
    };

    public static BedrockGeometry parse(String filepath) throws UnsupportedFormatException, InvalidGeometryException, FileNotFoundException, IOException, ParseException {
        // check for the .geo.json extention
        if (!filepath.endsWith(".geo.json")) throw new InvalidGeometryException(Key.FORMAT_VERSION); 
        
        // parsing into a JSONObject
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(filepath));

        // final we build the model
        return build(jsonObject);
    }

    private static BedrockGeometry build(JSONObject jsonObject) throws InvalidGeometryException, UnsupportedFormatException {
        String format = "0.0.0";
        
        // try and fetch the format version
        // if we catch a null exception then
        // it's probably an invalide geometry
        try { 
            format = (String) jsonObject.get(Key.FORMAT_VERSION);
        } catch (NullPointerException exception) { 
            throw new InvalidGeometryException(Key.FORMAT_VERSION); 
        }

        // now we got the format, we gonna do a quick validation.
        // if it's valid, the method returns a boolean of whether
        // it a legacy version or not.
        boolean legacy = formatValidation(format);

        // and then call the appropriate geometry method 
        return new BedrockGeometry(format, legacy ? new Geometry[] { getGeometryLegacy(jsonObject) } : getGeometry(jsonObject));
    }

    /**
     * @param format fetched
     * @return {@code true} if the valid format is legacy, {@code false} otherwise.
     * @throws UnsupportedFormatException if the given format is not supported.
     */
    private static boolean formatValidation(String format) throws UnsupportedFormatException {
        for (Format SUPPORTED_FORMAT : SUPPORTED_FORMATS) 
        if (format.equals(SUPPORTED_FORMAT.version)) return SUPPORTED_FORMAT.legacy;
        throw new UnsupportedFormatException(format);
    }

    private static Geometry[] getGeometry(JSONObject jsonObject) {
        // Fetch the geometry array
        JSONArray jsonArray = getJSONArray(jsonObject, Key.MINECRAFT_GEOMETRY);

        // create a new array
        Geometry[] geometry = new Geometry[jsonArray.size()];

        // fill the array
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject geoJsonObject = getJSONObject(jsonArray, i);
            geometry[i] = new Geometry(
                new Description(
                    getString(geoJsonObject, Key.IDENTIFIER), 
                    getInt(geoJsonObject, Key.TEXTURE_WIDTH, 1), 
                    getInt(geoJsonObject, Key.TEXTURE_HEIGHT, 1), 
                    getFloat(geoJsonObject, Key.VISIBLE_BOUNDS_WIDTH, 0), 
                    getFloat(geoJsonObject, Key.VISIBLE_BOUNDS_HEIGHT, 0), 
                    getVec3f(geoJsonObject, Key.VISIBLE_BOUNDS_OFFSET, new Vec3f())
                ), 
                getBones(getJSONArray(geoJsonObject, Key.BONES))
            );
        };

        return geometry;
    }

    private static Geometry getGeometryLegacy(JSONObject jsonObject) {
        @SuppressWarnings("unchecked")
        String identifier = (String) jsonObject.keySet().toArray(new Object[jsonObject.keySet().size()])[1];
        JSONObject geoJsonObject = (JSONObject) jsonObject.get(identifier);
        return new Geometry(
            new Description(
                identifier, 
                getInt(geoJsonObject, Key.TEXTURE_WIDTH_LEGACY, 1), 
                getInt(geoJsonObject, Key.TEXTURE_HEIGHT_LEGACY, 1), 
                getFloat(geoJsonObject, Key.VISIBLE_BOUNDS_WIDTH, 0), 
                getFloat(geoJsonObject, Key.VISIBLE_BOUNDS_HEIGHT, 0), 
                getVec3f(geoJsonObject, Key.VISIBLE_BOUNDS_OFFSET, new Vec3f())
            ), 
            getBones(getJSONArray(geoJsonObject, Key.BONES))
        );
    }

    private static Bone[] getBones(JSONArray jsonArray) {
        List<Bone> bones = new ArrayList<>();
        // parsing the bones
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = getJSONObject(jsonArray, i);
            bones.add(new Bone(
                getString(jsonObject, Key.NAME), 
                getString(jsonObject, Key.PARENT),
                getVec3f(jsonObject, Key.PIVOT, new Vec3f()), 
                getCubes(getJSONArray(jsonObject, Key.CUBES)),
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
                getVec3f(jsonObject, Key.ORIGIN, new Vec3f()), 
                getVec3f(jsonObject, Key.SIZE, new Vec3f(1)), 
                getFloat(jsonObject, Key.INFLATE, 1), 
                getVec3f(jsonObject, Key.PIVOT, new Vec3f()), 
                getVec3f(jsonObject, Key.ROTATION, new Vec3f()), 
                uvType, 
                (uvType == UVType.BOX) ? getVec2i(jsonObject, Key.UV, new Vec2i()) : null,
                (uvType == UVType.PERFACE) ? getPerfaceUV(getJSONObject(jsonObject, Key.UV)) : null
            ));
        }
        return cubes.toArray(new Cube[cubes.size()]);
    }

    private static Locator[] getLocators(JSONObject location) {
        List<Locator> locators = new ArrayList<>();

        Object object = location.get(Key.LOCATORS);
        if (object != null) {
            JSONObject jsonObject = getJSONObject(location, Key.LOCATORS);
            @SuppressWarnings("unchecked")
            Set<String> keys = jsonObject.keySet();
            for (String key : keys) locators.add(new Locator(key, getVec3f(jsonObject, key, new Vec3f())));
        }

        return locators.toArray(new Locator[locators.size()]);
    }

    private static PerfaceUV getPerfaceUV(JSONObject jsonObject) {
        JSONObject[] faces = new JSONObject[] {
            getJSONObject(jsonObject, Key.NORTH),
            getJSONObject(jsonObject, Key.EAST),
            getJSONObject(jsonObject, Key.SOUTH),
            getJSONObject(jsonObject, Key.WEST),
            getJSONObject(jsonObject, Key.UP),
            getJSONObject(jsonObject, Key.DOWN)
        };
        return new PerfaceUV(
            faces[0] != null ? new Face(getVec2i(faces[0], Key.UV, new Vec2i()), getVec2i(faces[0], Key.UV_SIZE, new Vec2i())) : new Face(new Vec2i(), new Vec2i()),
            faces[1] != null ? new Face(getVec2i(faces[1], Key.UV, new Vec2i()), getVec2i(faces[1], Key.UV_SIZE, new Vec2i())) : new Face(new Vec2i(), new Vec2i()),
            faces[2] != null ? new Face(getVec2i(faces[2], Key.UV, new Vec2i()), getVec2i(faces[2], Key.UV_SIZE, new Vec2i())) : new Face(new Vec2i(), new Vec2i()),
            faces[3] != null ? new Face(getVec2i(faces[3], Key.UV, new Vec2i()), getVec2i(faces[3], Key.UV_SIZE, new Vec2i())) : new Face(new Vec2i(), new Vec2i()),
            faces[4] != null ? new Face(getVec2i(faces[4], Key.UV, new Vec2i()), getVec2i(faces[4], Key.UV_SIZE, new Vec2i())) : new Face(new Vec2i(), new Vec2i()),
            faces[5] != null ? new Face(getVec2i(faces[5], Key.UV, new Vec2i()), getVec2i(faces[5], Key.UV_SIZE, new Vec2i())) : new Face(new Vec2i(), new Vec2i())
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

    private static int getInt(JSONObject jsonObject, String key, int def) {
        Object object = jsonObject.get(key);
        return (object != null) ? castToInt(object) : def;
    }
    private static int getInt(JSONArray jsonArray, int index, int def) {
        Object object = jsonArray.get(index);
        return (object != null) ? castToInt(object) : def;
    }

    private static int castToInt(Object object) {
        return (object instanceof Long) ? (int) (long) object : Math.round(castToFloat(object));
    }
    private static float castToFloat(Object object) {
        return switch(object) {
            case Long o -> (float) o;
            case Double o -> (float) (double) o;
            default -> 0f;
        };
    }

    private static Vec3f getVec3f(JSONObject jsonObject, String key, Vec3f def) {
        Object object = jsonObject.get(key);
        return (object != null) ? new Vec3f(
            getFloat(getJSONArray(jsonObject, key), 0, def.getX()),
            getFloat(getJSONArray(jsonObject, key), 1, def.getY()),
            getFloat(getJSONArray(jsonObject, key), 2, def.getZ())
        ) : def;
    }
    private static Vec2i getVec2i(JSONObject jsonObject, String key, Vec2i def) {
        Object object = jsonObject.get(key);
        return (object != null) ? new Vec2i(
            getInt(getJSONArray(jsonObject, key), 0, def.getX()),
            getInt(getJSONArray(jsonObject, key), 1, def.getY())
        ) : new Vec2i(0, 0);
    }

    private static JSONArray getJSONArray(JSONObject jsonObject, String key) {
        Object object = jsonObject.get(key);
        return object != null ? (JSONArray) object : new JSONArray();
    }

    private static UVType getUVType(JSONObject jsonObject) {
        if (jsonObject.get(Key.UV) == null) return null;
        return switch(jsonObject.get(Key.UV)) {
            case JSONObject o -> UVType.PERFACE;
            case JSONArray o -> UVType.BOX;
            default -> null;
        };
    }

    // #endregion
}
