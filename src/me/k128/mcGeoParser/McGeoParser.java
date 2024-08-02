package me.k128.mcGeoParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.k128.mcGeoParser.utilities.Vector2i;
import me.k128.mcGeoParser.utilities.Vector3f;

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
            getVector3f(jsonObject, "visible_bounds_offset")
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
                getVector3f(jsonObject, "pivot"), 
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
            cubes.add(new McGeoCube(
                getVector3f(jsonObject, "origin"), 
                getVector3f(jsonObject, "size"), 
                getVector3f(jsonObject, "povit"), 
                getVector3f(jsonObject, "rotation"), 
                getVector2i(jsonObject, "uv")
            ));
        }
        return cubes.toArray(new McGeoCube[cubes.size()]);
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

    private static Vector3f getVector3f(JSONObject jsonObject, String key) {
        Object object = jsonObject.get(key);
        return (object != null) ? new Vector3f(
            castToFloat(getJSONArray(jsonObject, key).get(0)),
            castToFloat(getJSONArray(jsonObject, key).get(1)),
            castToFloat(getJSONArray(jsonObject, key).get(2))
        ) : new Vector3f(0, 0, 0);
    }
    private static Vector2i getVector2i(JSONObject jsonObject, String key) {
        Object object = jsonObject.get(key);
        return (object != null) ? new Vector2i(
            (int) (long) getJSONArray(jsonObject, key).get(0),
            (int) (long) getJSONArray(jsonObject, key).get(1)
        ) : new Vector2i(0, 0);
    }

    private static JSONArray getJSONArray(JSONObject jsonObject, String key) {
        Object object = jsonObject.get(key);
        return object != null ? (JSONArray) object : new JSONArray();
    }
    //#endregion
}