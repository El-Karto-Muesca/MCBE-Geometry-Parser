package me.k128.mcGeoParser;

import me.k128.mcGeoParser.utilities.Vector2i;
import me.k128.mcGeoParser.utilities.Vector3f;

public class McGeoCube {
    private final Vector3f origin;
    private final Vector3f size;
    private final float inflate;
    private final Vector3f pivot;
    private final Vector3f rotation;
    private final Vector2i uv;

    public McGeoCube(
        Vector3f origin, 
        Vector3f size, 
        float inflate, 
        Vector3f pivot,
        Vector3f rotation, 
        Vector2i uv
    ) {
        this.origin = origin;
        this.size = size;
        this.inflate = inflate;
        this.pivot = pivot;
        this.rotation = rotation;
        this.uv = uv;
    }

    public Vector3f getOrigin() {
        return origin;
    }
    public Vector3f getSize() {
        return size;
    }
    public float getInflate() {
        return inflate;
    }
    public Vector3f getPivot() {
        return pivot;
    }
    public Vector3f getRotation() {
        return rotation;
    }
    public Vector2i getUv() {
        return uv;
    }
}