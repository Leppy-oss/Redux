package com.leppy.redux.core;

import com.leppy.redux.engine.ReduxEngine;
import lombok.Getter;
import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    public static final int SIZE = 3;

    @Getter
    protected double
            sX, sY, // Scroll wheel x, y
            cX, cY, // Cursor x, y
            lX, lY, // Last x, y
            dX, dY,
            wX, wY,
            lwX, lwY,
            wdX, wdY; // Difference between prev. and curr. cursor positions

    private final Button[] mouseButtons = new Button[SIZE]; // left click, middle click, right click

    // TODO: may need to fix some of this using project lombok setters or delegates instead of set() nested within custom setter
    private final Vector2f gameViewportPos = new Vector2f();
    private final Vector2f gameViewportSize = new Vector2f();

    /**
     * Dragging only works with LMB
     */
    protected boolean isDragging;

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
        this.dX = 0.0;
        this.dY = 0.0;
        this.wX = 0.0;
        this.wY = 0.0;
        this.lwX = 0.0;
        this.lwY = 0.0;
        this.wdX = 0.0;
        this.wdY = 0.0;

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
        this.dX = this.lX - this.cX;
        this.dY = this.lY - this.cY;
        this.wdX = this.lwX - this.wX;
        this.wdY = this.lwY - this.wY;
        this.lX = this.cX;
        this.lY = this.cY;
        this.lwX = this.wX;
        this.lwY = this.wY;
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
        float currentX = (float) (this.getCX() - this.gameViewportPos.x);
        currentX = (currentX / this.gameViewportSize.x) * Window.getWidth();
        return currentX;
    }

    public float getScreenY() {
        float currentY = (float) (this.getCY() - this.gameViewportPos.y);
        currentY = Window.getHeight() - ((currentY / this.gameViewportSize.y) * Window.getHeight());
        return currentY;
    }

    public float getOrthoX() {
        return (float) this.wX;
    }

    protected void calcOrthoX() {
        float currentX = (float) (this.getCX() - this.gameViewportPos.x);
        currentX = (currentX / this.gameViewportSize.x) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);

        Camera camera = ReduxEngine.getScene().camera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);
        tmp.mul(viewProjection);

        this.wX = tmp.x;
    }

    public float getOrthoY() {
        return (float) this.wY;
    }

    protected void calcOrthoY() {
        float currentY = (float) (this.getCY() - this.gameViewportPos.y);
        currentY = -((currentY / this.gameViewportSize.y) * 2.0f - 1.0f);
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);

        Camera camera = ReduxEngine.getScene().camera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);
        tmp.mul(viewProjection);

        this.wY = tmp.y;
    }

    public void setGameViewportPos(Vector2f gameViewportPos) {
        this.gameViewportPos.set(gameViewportPos);
    }

    public void setGameViewportSize(Vector2f gameViewportSize) {
        this.gameViewportSize.set(gameViewportSize);
    }
}