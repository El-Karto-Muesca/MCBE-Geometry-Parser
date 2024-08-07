package me.k128.mcbeGeoParser;

public class Vec2i {

    private final int[] ints;
    
    Vec2i() {
        this(0);
    }
    Vec2i(int xy) {
        this(xy, xy);
    }
    Vec2i(int x, int y) {
        this.ints = new int[] {x, y};
    }

    public int[] get() { return ints; }

    public int getX() { return ints[0]; }
    public int getY() { return ints[1]; }
}
