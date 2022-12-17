package com.leppy.redux.framework;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    protected static Mouse instance;
    
    private double
            sX, sY, // Scroll wheel x, y
            cX, cY, // Cursor x, y
            lX, lY; // Last x, y

    private boolean isPressed[] = new boolean[3]; // left click, middle click, right click
    private boolean isDragging;
    private int buttonDown = 0;

    protected Mouse() {
        this.sX = 0.0;
        this.sY = 0.0;
        this.cX = 0.0;
        this.cY = 0.0;
        this.lX = 0.0;
        this.lY = 0.0;
        this.buttonDown = 0;
        Arrays.fill(this.isPressed, false);
    }
    
    public static Mouse get() {
        if (instance == null) instance = new Mouse();
        
        return instance;
    }

    public static void reset() {
        get().sX = 0.0;
        get().sY = 0.0;
        get().cX = 0.0;
        get().cY = 0.0;
        get().lX = 0.0;
        get().lY = 0.0;
        get().buttonDown = 0;
        Arrays.fill(get().isPressed, false);
    }

    public static void mouseMoveHandler(long windowHandle, double cX, double cY) {
        get().lX = get().cX;
        get().lY = get().cY;
        get().cX = cX;
        get().cY = cY;
        get().isDragging = get().isAnyPressed();
    }

    public static void mouseButtonHandler(long windowHandle, int buttonDown, int action, int mods) {
        if (buttonDown < get().isPressed.length) {
            if (action == GLFW_PRESS) get().isPressed[buttonDown] = true;
            else if (action == GLFW_RELEASE) {
                get().isPressed[buttonDown] = false;
                get().isDragging = false;
            }
        }
    }

    public static void scrollHandler(long windowHandle, double sX, double sY) {
        get().sX = sX;
        get().sY = sY;
    }

    public static void endFrame() {
        get().sX = 0;
        get().sY = 0;
        get().lX = get().cX;
        get().lY = get().cY;
    }

    public static double sX() {
        return (double) get().sX;
    }

    public static double sY() {
        return (double) get().sY;
    }

    public static double cX() {
        return (double) get().cX;
    }

    public static double cY() {
        return (double) get().cY;
    }
    public static double lX() {
        return (double) get().lX;
    }

    public static double lY() {
        return (double) get().lY;
    }

    public static double dX() {
        return (double) (get().lX - get().cX);
    }

    public static double dY() {
        return (double) (get().lY - get().cY);
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean isPressed(int button) {
        if (button < get().isPressed.length) return get().isPressed[button];
        else return false;
    }

    public static boolean isLeftPressed() {
        return get().isPressed(GLFW_MOUSE_BUTTON_1);
    }

    public static boolean isMidPressed() {
        return get().isPressed(GLFW_MOUSE_BUTTON_2);
    }

    public static boolean isRightPressed() {
        return get().isPressed(GLFW_MOUSE_BUTTON_3);
    }

    public static boolean isAnyPressed() {
        return (get().isLeftPressed() || get().isMidPressed() || get().isRightPressed());
    }
}