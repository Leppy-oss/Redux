package com.leppy.redux.framework;

import static org.lwjgl.glfw.GLFW.*;

public class ControlSystem {
    public static ControlSystem instance;
    private Mouse mouse;
    private Keyboard keyboard;
    private long windowHandle;

    /**
     * @param wh window handle
     */
    private ControlSystem(long wh) {
        mouse = new Mouse();
        keyboard = new Keyboard();
        windowHandle = wh;

        glfwSetCursorPosCallback(windowHandle, ControlSystem::mouseMoveHandler);
        glfwSetScrollCallback(windowHandle, ControlSystem::scrollHandler);
    }
    
    public static void setInstance(long wh) {
        instance = new ControlSystem(wh);
    }

    /**
     * WARNING: DOES NOT HAVE PROTECTION FOR IF get() IS NOT YET INITIALIZED <br>
     * MAKE SURE TO CALL {@link #setInstance(long)} FIRST
     * @return the specific, global get() of the control system
     */
    public static ControlSystem get() {
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
}
