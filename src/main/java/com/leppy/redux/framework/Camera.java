package com.leppy.redux.framework;

import com.leppy.redux.util.Constants;
import org.joml.*;

public class Camera {
    private Matrix4f projectionMatrix, cameraMatrix;
    private Vector2f position;
    /** NOT how far the camera is zoomed in */
    public static final float DEPTH = 20.0f;

    public Camera(Vector2f initPos) {
        this.position = initPos;
        this.projectionMatrix = new Matrix4f();
        this.cameraMatrix = new Matrix4f();
        this.adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, Constants.TILE_LENGTH * 80, 0.0f, Constants.TILE_LENGTH * 42, 0.0f, 100.0f);
    }

    public Matrix4f getCameraMatrix() {
        Vector3f frontProj = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f upProj = new Vector3f(0.0f, 1.0f, 0.0f);
        this.cameraMatrix.identity();
        this.cameraMatrix.lookAt(new Vector3f(this.position.x, this.position.y, DEPTH),
                frontProj.add(this.position.x, this.position.y, 0.0f), // Basically just setting it to the eye, just with a different depth
                upProj);

        return this.cameraMatrix;
    }

    public Matrix4f getViewMatrix() {
        return this.getCameraMatrix();
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public void setPosition(Vector2f pos) {
        this.position = pos;
    }

    public void offsetPosition(Vector2f offset) {
        this.position = new Vector2f(this.position.x + offset.x, this.position.y + offset.y);
    }

    public void setPosition(float x, float y) {
        this.setPosition(new Vector2f(x, y));
    }

    public void offsetPosition(float x, float y) {
        this.offsetPosition(new Vector2f(x, y));
    }
}
