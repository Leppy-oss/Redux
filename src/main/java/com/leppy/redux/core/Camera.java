package com.leppy.redux.core;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f position;
    /**
     * Must match the screen resolution aspect ratio (here, 1.7777... beacuse 1920/1080 = 40/22.5)
     */
    private Vector2f projectionSize = new Vector2f(32.0f * 40.0f, 32.0f * 22.5f);

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x, 0.0f, projectionSize.y, 0.0f, 100.0f);
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

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4f getInverseProjection() {
        return this.inverseProjection;
    }

    public Matrix4f getInverseView() {
        return this.inverseView;
    }

    public void offsetPosition(Vector2f offset) {
        this.position.x += offset.x();
        this.position.y += offset.y();
    }

    public void offsetPosition(float offsetX, float offsetY) {
        // Not just referencing {@link #offsetPosition(Vector2f)} because this is more efficient
        this.position.x += offsetX;
        this.position.y += offsetY;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }


    public Vector2f getProjectionSize() {
        return this.projectionSize;
    }
}