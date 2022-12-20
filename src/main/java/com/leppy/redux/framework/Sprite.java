package com.leppy.redux.framework;

import com.leppy.redux.framework.render.Texture;
import org.joml.Vector2f;

public class Sprite {
    private Texture texture = null;
    private double width, height;

    private Vector2f[] texCoords = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

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

    /**
     * @param texture The texture for the sprite (img, color, etc.)
     * @param texCoords The portion of the texture, in UV coords, to be rendered
     * @param width Width in world coords of the sprite
     * @param height Height in world coords of the sprite
     */
    public Sprite(Texture texture, Vector2f[] texCoords, double width, double height) {
        this.texture = texture;
        this.texCoords = texCoords;
        this.width = width;
        this.height = height;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2f[] getTexCoords() {
        return this.texCoords;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getTexId() {
        return texture == null ? -1 : texture.getId();
    }
}