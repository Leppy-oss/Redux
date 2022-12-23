package com.leppy.redux.framework;

import com.leppy.redux.render.Texture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

@Getter @Setter @AllArgsConstructor
public class Sprite {
    private Texture texture = null;
    private Vector2f[] texCoords = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };
    private double width, height;

    public Sprite() {}

    public Sprite(Texture texture) {
        this.texture = texture;
        this.texCoords = new Vector2f[] { // set initial texcoords to the whole thing
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
    }

    public Sprite(Texture texture, Vector2f[] texCoords) {
        this.texture = texture;
        this.texCoords = texCoords;
    }

    public Sprite(Texture texture, double width, double height) {
        this(texture, new Vector2f[] { // set initial texcoords to the whole thing
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        }, width, height);
    }

    public int getTexId() {
        return texture == null ? -1 : texture.getId();
    }
}