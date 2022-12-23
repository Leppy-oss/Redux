package com.leppy.redux.framework;

import com.leppy.redux.util.AssetPool;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.joml.Vector2f;
import com.leppy.redux.framework.render.Texture;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
    private final Texture texture;
    @Getter @Delegate
    private final List<Sprite> sprites;

    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        this.sprites = new ArrayList<>();

        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;
        for (int i=0; i < numSprites; i++) {
            float topY = (currentY + spriteHeight) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Sprite sprite = new Sprite(this.texture, texCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    /**
     * not yet working
     */
    @Deprecated
    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacingX, int spacingY, int leftOffsetX, int topOffsetY, int rightOffsetX, int bottomOffsetY) {
        this.sprites = new ArrayList<>();

        this.texture = texture;
        int currentX = leftOffsetX;
        int currentY = texture.getHeight() - spriteHeight - topOffsetY + bottomOffsetY;
        for (int i=0; i < numSprites; i++) {
            float topY = (currentY + spriteHeight) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite(this.texture, texCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacingX;
            if (currentX >= texture.getWidth() + rightOffsetX / texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacingY;
            }
        }
    }

    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }
}