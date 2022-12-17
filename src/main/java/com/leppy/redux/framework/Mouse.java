package com.leppy.redux.framework;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    public static final int SIZE = 3;

    protected double
            sX, sY, // Scroll wheel x, y
            cX, cY, // Cursor x, y
            lX, lY; // Last x, y

    private Button mouseButtons[] = new Button[SIZE]; // left click, middle click, right click

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
        this.sX = 0;
        this.sY = 0;
        this.lX = this.cX;
        this.lY = this.cY;
    }

    public void endFrame() {
        this.sX = 0;
        this.sY = 0;
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
}