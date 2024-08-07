package me.k128.mcbeGeoParser;

public class InvalidGeometryException extends Exception {

    InvalidGeometryException(String key) {
        super("Couldn't find " + key + " key.");
    }

}
