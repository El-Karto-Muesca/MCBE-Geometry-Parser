package me.k128.mcGeoParser;

public class PerfaceUV {

    private final Face[] faces;

    PerfaceUV(
        Face north,
        Face east,
        Face south,
        Face west,
        Face up,
        Face down
    ) {
        this.faces = new Face[] {
            north,
            east,
            south,
            west,
            up,
            down
        };
    }

    public Face getNorth() { return faces[0]; }
    public Face getEast() { return faces[1]; }
    public Face getSouth() { return faces[2]; }
    public Face getWest() { return faces[3]; }
    public Face getUp() { return faces[4]; }
    public Face getDown() { return faces[5]; }
}
