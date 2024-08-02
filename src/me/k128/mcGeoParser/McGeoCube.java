package me.k128.mcGeoParser;

public class McGeoCube {
    private final Vec3f origin;
    private final Vec3f size;
    private final float inflate;
    private final Vec3f pivot;
    private final Vec3f rotation;
    private final UVType uvType;
    private final Vec2i boxUV;
    private final PerfaceUV perfaceUV;

    McGeoCube(
        Vec3f origin, 
        Vec3f size, 
        float inflate, 
        Vec3f pivot, 
        Vec3f rotation, 
        UVType uvType, 
        Vec2i boxUV,
        PerfaceUV perfaceUV
    ) {
        this.origin = origin;
        this.size = size;
        this.inflate = inflate;
        this.pivot = pivot;
        this.rotation = rotation;
        this.uvType = uvType;
        this.boxUV = boxUV;
        this.perfaceUV = perfaceUV;
    }

    public Vec3f getOrigin() {
        return origin;
    }
    public Vec3f getSize() {
        return size;
    }
    public float getInflate() {
        return inflate;
    }
    public Vec3f getPivot() {
        return pivot;
    }
    public Vec3f getRotation() {
        return rotation;
    }
    public UVType getUVType() {
        return uvType;
    }
    public Vec2i getBoxUV() {
        return boxUV;
    }
    public PerfaceUV getPerfaceUV() {
        return perfaceUV;
    }
}