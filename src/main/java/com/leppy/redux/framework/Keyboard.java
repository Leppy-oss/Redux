package com.leppy.redux.framework;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {
    protected static Keyboard instance;
    public static final int SIZE = 350;
    public Key keys[] = new Key[SIZE];
    public static final int HORIZONTAL_AXIS = 0;
    public static final int VERTICAL_AXIS = 1;

    public Keyboard() {
        for (int i = 0; i < SIZE; i++) keys[i] = new Key(i);
    }

    public static Keyboard get() {
        if (Keyboard.instance == null) Keyboard.instance = new Keyboard();
        return instance;
    }

    /*
    public static void keyChangeHandler(long windowHandle, int keyCode, int scanCode, int action, int mods) {
        get().wasJustPressed[keyCode] = false;
        if (keyCode < get().isPressed.length) {
            if (action == GLFW_PRESS) {
                if (!get().pressedCache[keyCode]) get().wasJustPressed[keyCode] = true;
                get().isPressed[keyCode] = true;
            }
            else if (action == GLFW_RELEASE) get().isPressed[keyCode] = false;
        }
        get().pressedCache[keyCode] = get().isPressed[keyCode];
    }
    */

    public static void update(long windowHandle) {
        updateKeys(windowHandle);
    }

    public static void updateKeys(long windowHandle) {
        for (Key key : get().keys) key.update(glfwGetKey(windowHandle, key.getKeyCode()));
    }

    public static boolean isPressed(int keyCode) {
        return get().keys[keyCode].isPressed();
    }

    public static boolean wasJustPressed(int keyCode) {
        return (get().keys[keyCode].wasJustPressed());
    }

    /**
     * Prioritizes up/left is sumOfBoth is false
     * @param sumOfBoth Whether to count two keys in opposite directions as "conflicting" and cancel them out (-1 + 1 = 0)
     * @return
     */
    public static int getAxis(int axis, boolean sumOfBoth) {
        switch (axis) {
            case HORIZONTAL_AXIS:
                int left = (isPressed(GLFW_KEY_A) || isPressed(GLFW_KEY_LEFT)) ? -1 : 0;
                int right = (isPressed(GLFW_KEY_D) || isPressed(GLFW_KEY_RIGHT)) ? 1 : 0;
                return sumOfBoth ? left + right : (left < 0 ? left : right); // TODO: Clean up ternaries

            default:
                int down = (isPressed(GLFW_KEY_S) || isPressed(GLFW_KEY_DOWN)) ? -1 : 0;
                int up = (isPressed(GLFW_KEY_W) || isPressed(GLFW_KEY_UP) || isPressed(GLFW_KEY_SPACE)) ? 1 : 0;
                return sumOfBoth ? down + up : (up > 0 ? up : down); // TODO: Clean up ternaries
        }
    }

    public static int getAxis(int axis) {
        return getAxis(axis, false);
    }

    public static boolean left() {
        return getAxis(HORIZONTAL_AXIS) < 0;
    }

    public static boolean right() {
        return getAxis(HORIZONTAL_AXIS) > 0;
    }

    public static boolean down() {
        return getAxis(VERTICAL_AXIS) < 0;
    }

    public static boolean up() {
        return getAxis(VERTICAL_AXIS) > 0;
    }
}
