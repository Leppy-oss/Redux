package com.leppy.redux.framework;

import com.leppy.redux.framework.ecs.*;
import com.leppy.redux.framework.ecs.components.*;
import org.joml.Vector2f;

/**
 * Generates GameObject prefabs
 */
public class Prefabs {
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject block = new GameObject("Sprite_Object_Gen",
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }
}