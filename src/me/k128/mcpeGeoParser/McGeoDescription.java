package me.k128.mcpeGeoParser;

public class McGeoDescription {
    private final String identifier;
    private final int textureWidth;
    private final int textureHeight;
    private final float visibleBoundsWidth;
    private final float visibleBoundsHeight;
    private final Vec3f visibleBoundsOffset;
    
    McGeoDescription(
        String identifier, 
        int textureWidth, 
        int textureHeight, 
        float visibleBoundsWidth,
        float visibleBoundsHeight, 
        Vec3f visibleBoundsOffset
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
    public Vec3f getVisibleBoundsOffset() {
        return visibleBoundsOffset;
    }
}
