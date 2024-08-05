package me.k128.mcbeGeoParser.exception;

public class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String format) {
        super(format + " format is not supported.");
    }
}