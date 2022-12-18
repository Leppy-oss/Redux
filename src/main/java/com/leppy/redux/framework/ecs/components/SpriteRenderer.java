package com.leppy.redux.framework.ecs.components;

import com.leppy.redux.framework.ecs.Component;
import com.leppy.redux.util.RGBFWrapper;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    private boolean firstTime = false;
    private Vector4f color;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }

    @Override
    public void start() {
    }

    @Override
    public void update(float dt) {
    }

    public Vector4f getColor() {
        return this.color;
    }
}