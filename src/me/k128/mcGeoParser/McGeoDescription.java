package me.k128.mcGeoParser;

import me.k128.mcGeoParser.utilities.Vector3f;

public class McGeoDescription {
    private final String identifier;
    private final int textureWidth;
    private final int textureHeight;
    private final float visibleBoundsWidth;
    private final float visibleBoundsHeight;
    private final Vector3f visibleBoundsOffset;
    
    public McGeoDescription(
        String identifier, 
        int textureWidth, 
        int textureHeight, 
        float visibleBoundsWidth,
        float visibleBoundsHeight, 
        Vector3f visibleBoundsOffset
    ) {
        this.identifier = identifier;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.visibleBoundsWidth = visibleBoundsWidth;
        this.visibleBoundsHeight = visibleBoundsHeight;
        this.visibleBoundsOffset = visibleBoundsOffset;
    }

    public String getIdentifier() {
        return identifier;
    }
    public int getTextureWidth() {
        return textureWidth;
    }
    public int getTextureHeight() {
        return textureHeight;
    }
    public float getVisibleBoundsWidth() {
        return visibleBoundsWidth;
    }
    public float getVisibleBoundsHeight() {
        return visibleBoundsHeight;
    }
    public Vector3f getVisibleBoundsOffset() {
        return visibleBoundsOffset;
    }
}
