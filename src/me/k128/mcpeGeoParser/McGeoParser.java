package me.k128.mcpeGeoParser;

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

public class McGeoParser {

    private static final String[] SUPPORTED_FORMATS = {
        "1.16.0",
        "1.12.0"
    }; 

    public static McGeo parse(String filepath) throws ParseException, FileNotFoundException, IOException {
        return parse(new File(filepath));
    }
    
    public static McGeo parse(File file) throws ParseException, FileNotFoundException, IOException {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(file));
        return parse(jsonObject);
    }

    private static McGeo parse(JSONObject jsonObject) {
        String format = "";
        // format checking
        try {
            format = (String) jsonObject.get("format_version");
            formatCheck(format);
        } catch (NullPointerException exception) {
            // throw new InvalidGeometry("format_version");
        }
        return new McGeo(format, getGeometry(jsonObject));
    }

    private static void formatCheck(String format) {
        for (String supportedFormat : SUPPORTED_FORMATS) if (format.equals(supportedFormat)) return;
        // throw new UnsupportedFormatException(format);
    }

    private static McGeoGeometry[] getGeometry(JSONObject jsonObject) {
        List<McGeoGeometry> geometry = new ArrayList<>();
        JSONArray jsonArray = getJSONArray(jsonObject, "minecraft:geometry");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject geo = (JSONObject) jsonArray.get(i);
            geometry.add(new McGeoGeometry(
                getDescription(getJSONObject(geo, "description")), 
                getBones(getJSONArray(geo, "bones"))
            ));
        }
        return geometry.toArray(new McGeoGeometry[geometry.size()]);
    }

    private static McGeoDescription getDescription(JSONObject jsonObject) {
        return new McGeoDescription(
            getString(jsonObject, "identifier"), 
            getInt(jsonObject, "texture_width"), 
            getInt(jsonObject, "texture_height"), 
            getFloat(jsonObject, "visible_bounds_width", 1), 
            getFloat(jsonObject, "visible_bounds_height", 1), 
            getVec3f(jsonObject, "visible_bounds_offset", new Vec3f())
        );
    }

    private static McGeoBone[] getBones(JSONArray jsonArray) {
        List<McGeoBone> bones = new ArrayList<>();
        // parsing the bones
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = getJSONObject(jsonArray, i);
            bones.add(new McGeoBone(
                getString(jsonObject, "name"), 
                getString(jsonObject, "parent"),
                getVec3f(jsonObject, "pivot", new Vec3f()), 
                getCubes(getJSONArray(jsonObject, "cubes")),
                getLocators(jsonObject)
            ));
        }
        // Setting each bone parent
        for (McGeoBone bone : bones) {
            for (McGeoBone bone2 : bones) {
                if (bone2.getName().equals(bone.getParentName())) { bone.setParent(bone2); }
            }
        }
        return bones.toArray(new McGeoBone[bones.size()]);
    }
    
    private static McGeoCube[] getCubes(JSONArray jsonArray) {
        List<McGeoCube> cubes = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = getJSONObject(jsonArray, i);
            McGeoUVType uvType = getUVType(jsonObject);
            cubes.add(new McGeoCube(
                getVec3f(jsonObject, "origin", new Vec3f()), 
                getVec3f(jsonObject, "size", new Vec3f(1)), 
                getFloat(jsonObject, "inflate", 1), 
                getVec3f(jsonObject, "povit", new Vec3f()), 
                getVec3f(jsonObject, "rotation", new Vec3f()), 
                uvType, 
                (uvType == McGeoUVType.BOX) ? getVec2i(jsonObject, "uv") : null,
                (uvType == McGeoUVType.PERFACE) ? getPerfaceUV(getJSONObject(jsonObject, "uv")) : null
            ));
        }
        return cubes.toArray(new McGeoCube[cubes.size()]);
    }

    private static McGeoLocator[] getLocators(JSONObject location) {
        List<McGeoLocator> locators = new ArrayList<>();

        Object object = location.get("locators");
        if (object != null) {
            JSONObject jsonObject = getJSONObject(location, "locators");
            Set<String> keys = jsonObject.keySet();
            for (String key : keys) locators.add(new McGeoLocator(key, getVec3f(jsonObject, key, new Vec3f())));
        }

        return locators.toArray(new McGeoLocator[locators.size()]);
    }

    private static McGeoPerfaceUV getPerfaceUV(JSONObject jsonObject) {
        JSONObject[] faces = new JSONObject[] {
            getJSONObject(jsonObject, "north"),
            getJSONObject(jsonObject, "east"),
            getJSONObject(jsonObject, "south"),
            getJSONObject(jsonObject, "west"),
            getJSONObject(jsonObject, "up"),
            getJSONObject(jsonObject, "down")
        };
        return new McGeoPerfaceUV(
            faces[0] != null ? new McGeoFace(getVec2i(faces[0], "uv"), getVec2i(faces[0], "uv_size")) : new McGeoFace(new Vec2i(), new Vec2i()),
            faces[1] != null ? new McGeoFace(getVec2i(faces[1], "uv"), getVec2i(faces[1], "uv_size")) : new McGeoFace(new Vec2i(), new Vec2i()),
            faces[2] != null ? new McGeoFace(getVec2i(faces[2], "uv"), getVec2i(faces[2], "uv_size")) : new McGeoFace(new Vec2i(), new Vec2i()),
            faces[3] != null ? new McGeoFace(getVec2i(faces[3], "uv"), getVec2i(faces[3], "uv_size")) : new McGeoFace(new Vec2i(), new Vec2i()),
            faces[4] != null ? new McGeoFace(getVec2i(faces[4], "uv"), getVec2i(faces[4], "uv_size")) : new McGeoFace(new Vec2i(), new Vec2i()),
            faces[5] != null ? new McGeoFace(getVec2i(faces[5], "uv"), getVec2i(faces[5], "uv_size")) : new McGeoFace(new Vec2i(), new Vec2i())
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

    private static McGeoUVType getUVType(JSONObject jsonObject) {
        if (jsonObject.get("uv") == null) return null;
        return switch(jsonObject.get("uv")) {
            case JSONObject o -> McGeoUVType.PERFACE;
            case JSONArray o -> McGeoUVType.BOX;
            default -> null;
        };
    }
    //#endregion
}