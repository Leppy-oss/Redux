package com.leppy.redux.core;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Global input system for Redux Engine
 */
public class Input {
    public static Input instance;
    private Mouse mouse;
    private Keyboard keyboard;
    private long windowHandle;

    /**
     * @param wh window handle
     */
    private Input(long wh) {
        mouse = new Mouse();
        keyboard = new Keyboard();
        windowHandle = wh;

        glfwSetCursorPosCallback(windowHandle, Input::mouseMoveHandler);
        glfwSetScrollCallback(windowHandle, Input::scrollHandler);
    }
    
    public static void setInstance(long wh) {
        instance = new Input(wh);
    }

    /**
     * WARNING: DOES NOT HAVE PROTECTION FOR IF get() IS NOT YET INITIALIZED <br>
     * MAKE SURE TO CALL {@link #setInstance(long)} FIRST
     * @return the specific, global get() of the control system
     */
    public static Input get() {
        return instance;
    }

    public static void update() {
        get().mouse.update(get().windowHandle);
        get().keyboard.update(get().windowHandle);
        glfwPollEvents();
    }

    public static void scrollHandler(long windowHandle, double sX, double sY) {
        get().mouse.sX = sX;
        get().mouse.sY = sY;
    }

    public static void mouseMoveHandler(long windowHandle, double cX, double cY) {
        get().mouse.lX = get().mouse.cX;
        get().mouse.lY = get().mouse.cY;
        get().mouse.cX = cX;
        get().mouse.cY = cY;
        get().mouse.isDragging = get().mouse.isAnyPressed();
    }

    public static Keyboard keyboard() {
        return get().keyboard;
    }

    public static Mouse mouse() {
        return get().mouse;
    }

    public static boolean wasJustPressed(int keyCode) {
        return keyboard().wasJustPressed(keyCode);
    }

    public static boolean wasJustReleased(int keyCode) {
        return keyboard().wasJustReleased(keyCode);
    }

    public static boolean isPressed(int keyCode) {
        return keyboard().isPressed(keyCode);
    }

    public static boolean mouseJustPressed(int mouseCode) {
        return mouse().wasJustPressed(mouseCode);
    }

    public static boolean mouseJustReleased(int mouseCode) {
        return mouse().wasJustReleased(mouseCode);
    }

    public static boolean isMousePressed(int mouseCode) {
        return mouse().isPressed(mouseCode);
    }

    /**
     * Shorthand for {@link #isMousePressed(int)}
     */
    public static boolean mouseButtonDown(int mouseCode) {
        return isMousePressed(mouseCode);
    }

    public static float getOrthoX() {
        return mouse().getOrthoX();
    }

    public static float getOrthoY() {
        return mouse().getOrthoY();
    }

    public static double getSX() {
        return mouse().getSX();
    }

    public static double getSY() {
        return mouse().getSY();
    }

    public static double getCX() {
        return mouse().getCX();
    }

    public static double getLX() {
        return mouse().getLX();
    }

    public static double getLY() {
        return mouse().getLY();
    }

    public static void endFrame() {
        mouse().endFrame();
    }
    
    public static boolean isDragging() {
        return mouse().isDragging();
    }
}