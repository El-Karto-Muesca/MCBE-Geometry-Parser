package me.k128.mcbeGeoParser.exception;

public class InvalidGeometry extends Exception {

    public InvalidGeometry(String key) {
        super("Couldn't find " + key + " key.");
    }

}
