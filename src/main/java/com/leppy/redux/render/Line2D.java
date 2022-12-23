package com.leppy.redux.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector3f;

@AllArgsConstructor
public class Line2D {
    @Getter
    private Vector2f from, to;
    @Getter
    private Vector3f color;
    private int lifetime;

    public Line2D(Vector2f from, Vector2f to) {
        this.from = from;
        this.to = to;
    }

    public int beginFrame() {
        this.lifetime--;
        return this.lifetime;
    }

    public float lengthSquared() {
        return new Vector2f(to).sub(from).lengthSquared();
    }
}