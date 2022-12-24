package com.leppy.redux.ecs;

import com.leppy.redux.framework.Sprite;
import com.leppy.redux.ecs.components.*;
import org.joml.Vector2f;

/**
 * Generates GameObject prefabs
 */
public class Prefabs {
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        return generateSpriteObject(sprite, sizeX, sizeY, "Sprite_Object_Gen");
    }

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY, String name) {
        GameObject block = new GameObject(name,
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }
}