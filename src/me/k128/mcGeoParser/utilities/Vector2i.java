package me.k128.mcGeoParser.utilities;

public class Vector2i extends Vector<Integer> {

    public Vector2i(int x, int y) {
        super(2);
        super.set(0, x);
        super.set(1, y);
    }

    public int getX() { return super.get(0); }
    public int getY() { return super.get(1); }
}
