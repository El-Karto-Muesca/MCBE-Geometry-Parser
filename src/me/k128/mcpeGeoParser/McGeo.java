package me.k128.mcpeGeoParser;

public class McGeo {
    private final String formatVersion;
    private final McGeoGeometry[] geometry;

    McGeo(String formatVersion, McGeoGeometry[] geometry) {
        this.formatVersion = formatVersion;
        this.geometry = geometry;
    }

    public String getFormatVersion() {
        return formatVersion;
    }
    public McGeoGeometry[] getGeometry() {
        return geometry;
    }
    public int getGeometryCount() {
        return geometry.length;
    }
}