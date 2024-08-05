package me.k128.mcpeGeoParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class McGeoParser {
    
    public static McGeo parse(String filepath) throws ParseException, FileNotFoundException, IOException {
        return parse((JSONObject) new JSONParser().parse(new FileReader(filepath)));
    }

    public static McGeo parse(FileReader reader) throws IOException, ParseException {
        return parse((JSONObject) new JSONParser().parse(reader));
    }

    public static McGeo parse(JSONObject jsonObject) {
        // getting the minecraft:geometry array first and only value in there, it's stupid I know
        JSONObject mcGeo = getJSONObject(getJSONArray(jsonObject, "minecraft:geometry"), 0);
        return new McGeo(
            getString(jsonObject, "format_version"), 
            getDescription(getJSONObject(mcGeo, "description")),
            getBones(getJSONArray(mcGeo, "bones"))
        );
    }

    private static McGeoDescription getDescription(JSONObject jsonObject) {
        return new McGeoDescription(
            getString(jsonObject, "identifier"), 
            getInt(jsonObject, "texture_width"), 
            getInt(jsonObject, "texture_height"), 
            getFloat(jsonObject, "visible_bounds_width"), 
            getFloat(jsonObject, "visible_bounds_height"), 
            getVec3f(jsonObject, "visible_bounds_offset")
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
                getVec3f(jsonObject, "pivot"), 
                getCubes(getJSONArray(jsonObject, "cubes")))
            );
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
            UVType uvType = getUVType(jsonObject);
            cubes.add(new McGeoCube(
                getVec3f(jsonObject, "origin"), 
                getVec3f(jsonObject, "size"), 
                getFloat(jsonObject, "inflate"), 
                getVec3f(jsonObject, "povit"), 
                getVec3f(jsonObject, "rotation"), 
                uvType, 
                (uvType == UVType.BOX) ? getVec2i(jsonObject, "uv") : null,
                (uvType == UVType.PERFACE) ? getPerfaceUV(getJSONObject(jsonObject, "uv")) : null
            ));
        }
        return cubes.toArray(new McGeoCube[cubes.size()]);
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
            new Face(getVec2i(faces[0], "uv"), getVec2i(faces[0], "uv_size")),
            new Face(getVec2i(faces[1], "uv"), getVec2i(faces[1], "uv_size")),
            new Face(getVec2i(faces[2], "uv"), getVec2i(faces[2], "uv_size")),
            new Face(getVec2i(faces[3], "uv"), getVec2i(faces[3], "uv_size")),
            new Face(getVec2i(faces[4], "uv"), getVec2i(faces[4], "uv_size")),
            new Face(getVec2i(faces[5], "uv"), getVec2i(faces[5], "uv_size"))
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

    private static float getFloat(JSONObject jsonObject, String key) {
        Object object = jsonObject.get(key);
        return (object != null) ? castToFloat(object) : 0.0f;
    }

    private static int getInt(JSONObject jsonObject, String key) {
        Object object = jsonObject.get(key);
        return (object != null) ? (int) (long) object : 0;
    }

    private static float castToFloat(Object object) {
        return switch(object) {
            case Long o -> (float) o;
            case Double o -> (float) (double) o;
            default -> 0f;
        };
    }

    private static Vec3f getVec3f(JSONObject jsonObject, String key) {
        Object object = jsonObject.get(key);
        return (object != null) ? new Vec3f(
            castToFloat(getJSONArray(jsonObject, key).get(0)),
            castToFloat(getJSONArray(jsonObject, key).get(1)),
            castToFloat(getJSONArray(jsonObject, key).get(2))
        ) : new Vec3f(0, 0, 0);
    }
    private static Vec2i getVec2i(JSONObject jsonObject, String key) {
        Object object = jsonObject.get(key);
        Object x = getJSONArray(jsonObject, key).get(0);
        Object y = getJSONArray(jsonObject, key).get(1);
        return (object != null) ? new Vec2i(
            (x instanceof Long) ? (int) (long) x : Math.round(castToFloat(x)),
            (y instanceof Long) ? (int) (long) y : Math.round(castToFloat(y))
        ) : new Vec2i(0, 0);
    }

    private static JSONArray getJSONArray(JSONObject jsonObject, String key) {
        Object object = jsonObject.get(key);
        return object != null ? (JSONArray) object : new JSONArray();
    }

    private static UVType getUVType(JSONObject jsonObject) {
        return switch(jsonObject.get("uv")) {
            case JSONObject o -> UVType.PERFACE;
            case JSONArray o -> UVType.BOX;
            default -> null;
        };
    }
    //#endregion
}