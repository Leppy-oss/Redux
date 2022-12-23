package com.leppy.redux.ecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

/**
 * De facto component describing the position and scale of a {@link com.leppy.redux.ecs.GameObject}
 */
@Getter @Setter @AllArgsConstructor
public class Transform {
    public Vector2f position, scale;
    /**
     * Rotation in DEGREES
     */
    public float rotation;

    public Transform() {
        this(new Vector2f(), new Vector2f(), 0.0f);
    }

    public Transform(Vector2f position) {
        this(position, new Vector2f(), 0.0f);
    }

    public Transform(Vector2f position, Vector2f scale) {
        this(position, scale, 0.0f);
    }

    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale), 0.0f);
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Transform t)) return false;

        return t.position.equals(this.position) && t.scale.equals(this.scale);
    }
}