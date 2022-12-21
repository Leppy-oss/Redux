package com.leppy.redux.framework;

import com.leppy.redux.util.AssetPool;

import java.util.ArrayList;
import java.util.List;

@Deprecated
/**
 * Doesn't work with new rendering, sprite system, components, etc.
 */
public class MultiSpriteHandler {
    private List<Sprite> sprites = new ArrayList<>();

    public MultiSpriteHandler() {}

    /**
     * @param directory The directory the images are located
     * @param constPrefix A string representing the first characters of each image name
     * @param subfix A string representing
     * @param spriteCount The number of sprites with constPrefix and subfix within directory to extract
     * @param startIndex The starting number of the sprites (e.g. sprite0...5 -> 0, sprite1...6 -> 1)
     */
    public MultiSpriteHandler(String directory, String constPrefix, String subfix, int spriteCount, int startIndex) {
        this.setSprites(directory, constPrefix, subfix, spriteCount, startIndex);
    }

    public void setSprites(String directory, String constPrefix, String subfix, int spriteCount, int startIndex) {
        this.sprites = new ArrayList<>();

        for (int i = startIndex; i < startIndex + spriteCount; i++) {
            // this is not updated to contain the new width/height metadata required for serialization and use of sprites
            this.sprites.add(new Sprite(AssetPool.getTexture(directory + constPrefix + i + subfix)));
        }
    }

    public List<Sprite> getSprites() {
        return this.sprites;
    }

    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }
}
