package com.leppy.redux.core;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    @Getter
    private final Matrix4f projectionMatrix, inverseProjection, inverseView;

    private final Matrix4f viewMatrix; // declare separately to avoid lombok warning

    @Getter @Setter
    public Vector2f position;

    @Getter @Setter
    private Vector2f projectionSize = new Vector2f(32.0f * 40.0f, 32.0f * 22.5f);

    @Getter @Setter
    private float zoom = 1.0f;

    public Camera(Vector2f position) {
        this.setPosition(position);
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x * this.zoom,
                0.0f, projectionSize.y * zoom, 0.0f, 100.0f);
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                                        cameraFront.add(position.x, position.y, 0.0f),
                                        cameraUp);
        this.viewMatrix.invert(inverseView);

        return this.viewMatrix;
    }

    public void addZoom(float value) {
        this.zoom += value;
    }
}