package me.k128.mcGeoParser;

public class Vec2i {

    private final int[] ints;
    
    public Vec2i(int x, int y) {
        this.ints = new int[] {x, y};
    }

    public int getX() { return ints[0]; }
    public int getY() { return ints[1]; }
}
