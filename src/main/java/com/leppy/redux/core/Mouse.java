package com.leppy.redux.core;

import com.leppy.redux.engine.ReduxEngine;
import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    public static final int SIZE = 3;

    protected double
            sX, sY, // Scroll wheel x, y
            cX, cY, // Cursor x, y
            lX, lY; // Last x, y

    private Button mouseButtons[] = new Button[SIZE]; // left click, middle click, right click

    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();

    /**
     * Dragging only works with LMB
     */
    protected boolean isDragging;

    private int buttonDown = 0;

    public Mouse() {
        this.reset();
    }
    
    public void reset() {
        this.sX = 0.0;
        this.sY = 0.0;
        this.cX = 0.0;
        this.cY = 0.0;
        this.lX = 0.0;
        this.lY = 0.0;
        this.buttonDown = 0;
        this.isDragging = false;
        for (int i = 0; i < SIZE; i++) mouseButtons[i] = new Button(i);
    }

    public void update(long windowHandle) {
        for (Button mb : this.mouseButtons) mb.update(glfwGetMouseButton(windowHandle, mb.getCode()));
        if (!this.isLeftPressed()) this.isDragging = false;
    }

    public void endFrame() {
        this.sX = 0.0;
        this.sY = 0.0;
        this.lX = this.cX;
        this.lY = this.cY;
    }

    public double sX() {
        return (double) this.sX;
    }

    public double sY() {
        return (double) this.sY;
    }

    public double cX() {
        return (double) this.cX;
    }

    public double cY() {
        return (double) this.cY;
    }
    public double lX() {
        return (double) this.lX;
    }

    public double lY() {
        return (double) this.lY;
    }

    public double dX() {
        return (double) (this.lX - this.cX);
    }

    public double dY() {
        return (double) (this.lY - this.cY);
    }

    public boolean isDragging() {
        return this.isDragging;
    }

    public boolean isPressed(int button) {
        if (button < SIZE) return this.mouseButtons[button].isPressed();
        else return false;
    }

    public boolean wasJustPressed(int button) {
        if (button < SIZE) return this.mouseButtons[button].wasJustPressed();
        else return false;
    }

    public boolean wasJustReleased(int button) {
        if (button < SIZE) return this.mouseButtons[button].wasJustReleased();
        else return false;
    }

    public boolean isLeftPressed() {
        return this.isPressed(GLFW_MOUSE_BUTTON_1);
    }

    public boolean isMidPressed() {
        return this.isPressed(GLFW_MOUSE_BUTTON_2);
    }

    public boolean isRightPressed() {
        return this.isPressed(GLFW_MOUSE_BUTTON_3);
    }

    public boolean isAnyPressed() {
        for (Button b : mouseButtons) if (b.isPressed()) return true;
        return false;
    }

    public float getScreenX() {
        float currentX = (float) (this.cX() - this.gameViewportPos.x);
        currentX = (currentX / this.gameViewportSize.x) * Window.getWidth();
        return currentX;
    }

    public float getScreenY() {
        float currentY = (float) (this.cY() - this.gameViewportPos.y);
        currentY = Window.getHeight() - ((currentY / this.gameViewportSize.y) * Window.getHeight());
        return currentY;
    }

    public float getOrthoX() {
        float currentX = (float) (this.cX() - this.gameViewportPos.x);
        currentX = (currentX / this.gameViewportSize.x) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);

        Camera camera = ReduxEngine.getScene().camera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);
        tmp.mul(viewProjection);
        currentX = tmp.x;

        return currentX;
    }

    public float getOrthoY() {
        float currentY = (float) (this.cY() - this.gameViewportPos.y);
        currentY = -((currentY / this.gameViewportSize.y) * 2.0f - 1.0f);
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);

        Camera camera = ReduxEngine.getScene().camera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);
        tmp.mul(viewProjection);
        currentY = tmp.y;

        return currentY;
    }

    public void setGameViewportPos(Vector2f gameViewportPos) {
        this.gameViewportPos.set(gameViewportPos);
    }

    public void setGameViewportSize(Vector2f gameViewportSize) {
        this.gameViewportSize.set(gameViewportSize);
    }
}