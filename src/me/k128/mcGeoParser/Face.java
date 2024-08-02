package me.k128.mcGeoParser;

public class Face {
    private final Vec2i uv, uvSize;

    Face(Vec2i uv, Vec2i uvSize) {
        this.uv = uv;
        this.uvSize = uvSize;
    }

    public Vec2i getUV() {
        return uv;
    }
    public Vec2i getUVSize() {
        return uvSize;
    }
}
