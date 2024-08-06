package me.k128.mcbeGeoParser.exception;

public class InvalidGeometryException extends Exception {

    public InvalidGeometryException(String key) {
        super("Couldn't find " + key + " key.");
    }

}
