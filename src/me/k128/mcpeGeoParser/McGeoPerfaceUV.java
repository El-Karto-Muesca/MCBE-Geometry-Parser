package me.k128.mcpeGeoParser;

public class McGeoPerfaceUV {

    private final McGeoFace[] faces;

    McGeoPerfaceUV(
        McGeoFace north,
        McGeoFace east,
        McGeoFace south,
        McGeoFace west,
        McGeoFace up,
        McGeoFace down
    ) {
        this.faces = new McGeoFace[] {
            north,
            east,
            south,
            west,
            up,
            down
        };
    }

    public McGeoFace getNorth() { return faces[0]; }
    public McGeoFace getEast() { return faces[1]; }
    public McGeoFace getSouth() { return faces[2]; }
    public McGeoFace getWest() { return faces[3]; }
    public McGeoFace getUp() { return faces[4]; }
    public McGeoFace getDown() { return faces[5]; }
}
