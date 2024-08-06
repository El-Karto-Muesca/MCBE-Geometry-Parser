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
import me.k128.mcbeGeoParser.exception.InvalidGeometryException;
import me.k128.mcbeGeoParser.exception.UnsupportedFormatException;

class Parser { 
    private static final String FORMAT_VERSION_KEY = "format_version";
    private static class SupportedFormat {
        private static final String FORMAT = "1.12.0";   
        private static final int MAJOR = Integer.parseInt(FORMAT.split(".")[0]);   
        private static final int MINOR = Integer.parseInt(FORMAT.split(".")[1]);   
    }
    
    static BedrockGeometry parse(String filepath) throws ParseException, FileNotFoundException, IOException, UnsupportedFormatException, InvalidGeometryException {
        return parse(new File(filepath));
    }
    
    static BedrockGeometry parse(File file) throws ParseException, FileNotFoundException, IOException, UnsupportedFormatException, InvalidGeometryException {
        return parse((JSONObject) new JSONParser().parse(new FileReader(file)));
    }

    private static BedrockGeometry parse(JSONObject jsonObject) throws UnsupportedFormatException, InvalidGeometryException {
        try { 
            String format = (String) jsonObject.get(FORMAT_VERSION_KEY);
            formatCheck(format); 
            return new BedrockGeometry(format, getGeometry(jsonObject));
        } catch (NullPointerException exception) { 
            throw new InvalidGeometryException(FORMAT_VERSION_KEY); 
        }
    }

    private static void formatCheck(String format) throws UnsupportedFormatException {
        String[] tokens = format.split(".");
        if (Integer.parseInt(tokens[0]) == SupportedFormat.MAJOR && Integer.parseInt(tokens[1]) >= SupportedFormat.MINOR) return;
        throw new UnsupportedFormatException(format);
    }

    private static BedrockGeometry.Geometry[] getGeometry(JSONObject jsonObject) {
        List<BedrockGeometry.Geometry> geometry = new ArrayList<>();
        JSONArray jsonArray = getJSONArray(jsonObject, "minecraft:geometry");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject geo = (JSONObject) jsonArray.get(i);
            geometry.add(new BedrockGeometry.Geometry(
                getDescription(getJSONObject(geo, "description")), 
                getBones(getJSONArray(geo, "bones"))
            ));
        }
        return geometry.toArray(new BedrockGeometry.Geometry[geometry.size()]);
    }

    private static BedrockGeometry.Description getDescription(JSONObject jsonObject) {
        return new BedrockGeometry.Description(
            getString(jsonObject, "identifier"), 
            getInt(jsonObject, "texture_width"), 
            getInt(jsonObject, "texture_height"), 
            getFloat(jsonObject, "visible_bounds_width", 1), 
            getFloat(jsonObject, "visible_bounds_height", 1), 
            getVec3f(jsonObject, "visible_bounds_offset", new Vec3f())
        );
    }

    private static BedrockGeometry.Bone[] getBones(JSONArray jsonArray) {
        List<BedrockGeometry.Bone> bones = new ArrayList<>();
        // parsing the bones
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = getJSONObject(jsonArray, i);
            bones.add(new BedrockGeometry.Bone(
                getString(jsonObject, "name"), 
                getString(jsonObject, "parent"),
                getVec3f(jsonObject, "pivot", new Vec3f()), 
                getCubes(getJSONArray(jsonObject, "cubes")),
                getLocators(jsonObject)
            ));
        }
        // Setting each bone parent
        for (BedrockGeometry.Bone bone : bones) {
            for (BedrockGeometry.Bone bone2 : bones) {
                if (bone2.getName().equals(bone.getParentName())) { bone.setParent(bone2); }
            }
        }
        return bones.toArray(new BedrockGeometry.Bone[bones.size()]);
    }
    
    private static BedrockGeometry.Cube[] getCubes(JSONArray jsonArray) {
        List<BedrockGeometry.Cube> cubes = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = getJSONObject(jsonArray, i);
            BedrockGeometry.UVType uvType = getUVType(jsonObject);
            cubes.add(new BedrockGeometry.Cube(
                getVec3f(jsonObject, "origin", new Vec3f()), 
                getVec3f(jsonObject, "size", new Vec3f(1)), 
                getFloat(jsonObject, "inflate", 1), 
                getVec3f(jsonObject, "povit", new Vec3f()), 
                getVec3f(jsonObject, "rotation", new Vec3f()), 
                uvType, 
                (uvType == BedrockGeometry.UVType.BOX) ? getVec2i(jsonObject, "uv") : null,
                (uvType == BedrockGeometry.UVType.PERFACE) ? getPerfaceUV(getJSONObject(jsonObject, "uv")) : null
            ));
        }
        return cubes.toArray(new BedrockGeometry.Cube[cubes.size()]);
    }

    private static BedrockGeometry.Locator[] getLocators(JSONObject location) {
        List<BedrockGeometry.Locator> locators = new ArrayList<>();

        Object object = location.get("locators");
        if (object != null) {
            JSONObject jsonObject = getJSONObject(location, "locators");
            @SuppressWarnings("unchecked")
            Set<String> keys = jsonObject.keySet();
            for (String key : keys) locators.add(new BedrockGeometry.Locator(key, getVec3f(jsonObject, key, new Vec3f())));
        }

        return locators.toArray(new BedrockGeometry.Locator[locators.size()]);
    }

    private static BedrockGeometry.PerfaceUV getPerfaceUV(JSONObject jsonObject) {
        JSONObject[] faces = new JSONObject[] {
            getJSONObject(jsonObject, "north"),
            getJSONObject(jsonObject, "east"),
            getJSONObject(jsonObject, "south"),
            getJSONObject(jsonObject, "west"),
            getJSONObject(jsonObject, "up"),
            getJSONObject(jsonObject, "down")
        };
        return new BedrockGeometry.PerfaceUV(
            faces[0] != null ? new BedrockGeometry.Face(getVec2i(faces[0], "uv"), getVec2i(faces[0], "uv_size")) : new BedrockGeometry.Face(new Vec2i(), new Vec2i()),
            faces[1] != null ? new BedrockGeometry.Face(getVec2i(faces[1], "uv"), getVec2i(faces[1], "uv_size")) : new BedrockGeometry.Face(new Vec2i(), new Vec2i()),
            faces[2] != null ? new BedrockGeometry.Face(getVec2i(faces[2], "uv"), getVec2i(faces[2], "uv_size")) : new BedrockGeometry.Face(new Vec2i(), new Vec2i()),
            faces[3] != null ? new BedrockGeometry.Face(getVec2i(faces[3], "uv"), getVec2i(faces[3], "uv_size")) : new BedrockGeometry.Face(new Vec2i(), new Vec2i()),
            faces[4] != null ? new BedrockGeometry.Face(getVec2i(faces[4], "uv"), getVec2i(faces[4], "uv_size")) : new BedrockGeometry.Face(new Vec2i(), new Vec2i()),
            faces[5] != null ? new BedrockGeometry.Face(getVec2i(faces[5], "uv"), getVec2i(faces[5], "uv_size")) : new BedrockGeometry.Face(new Vec2i(), new Vec2i())
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

    private static BedrockGeometry.UVType getUVType(JSONObject jsonObject) {
        if (jsonObject.get("uv") == null) return null;
        return switch(jsonObject.get("uv")) {
            case JSONObject o -> BedrockGeometry.UVType.PERFACE;
            case JSONArray o -> BedrockGeometry.UVType.BOX;
            default -> null;
        };
    }
    // public enum BedrockGeometry.UVType {
    //     BOX,
    //     PERFACE 
    // }
}
