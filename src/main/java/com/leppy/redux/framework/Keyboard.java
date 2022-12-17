package com.leppy.redux.framework;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {
    public static final int MINSET = 32;
    public static final int MAXSET = 349;
    public Key keys[] = new Key[MAXSET - MINSET];
    public static final int HORIZONTAL_AXIS = 0;
    public static final int VERTICAL_AXIS = 1;

    public Keyboard() {
        for (int i = 0; i < MAXSET - MINSET; i++) keys[i] = new Key(i + MINSET);
    }

    public void update(long windowHandle) {
        updateKeys(windowHandle);
    }

    public void updateKeys(long windowHandle) {
        for (Key key : this.keys) {
            key.update(glfwGetKey(windowHandle, key.getKeyCode()));
        }
    }

    public boolean isPressed(int keyCode) {
        if (keyCode < MAXSET && keyCode >= MINSET) return this.keys[keyCode - MINSET].isPressed();
        else return false;
    }

    public boolean wasJustPressed(int keyCode) {
        if (keyCode < MAXSET && keyCode >= MINSET) return (this.keys[keyCode - MINSET].wasJustPressed());
        else return false;
    }

    /**
     * Prioritizes up/left is sumOfBoth is false
     * @param sumOfBoth Whether to count two keys in opposite directions as "conflicting" and cancel them out (-1 + 1 = 0)
     * @return
     */
    public int getAxis(int axis, boolean sumOfBoth) {
        switch (axis) {
            case HORIZONTAL_AXIS:
                int left = (isPressed(GLFW_KEY_LEFT)) ? -1 : 0;
                int right = (isPressed(GLFW_KEY_RIGHT)) ? 1 : 0;
                return sumOfBoth ? left + right : (left < 0 ? left : right); // TODO: Clean up ternaries

            default:
                int down = (isPressed(GLFW_KEY_DOWN)) ? -1 : 0;
                int up = (isPressed(GLFW_KEY_UP)) ? 1 : 0;
                return sumOfBoth ? down + up : (up > 0 ? up : down); // TODO: Clean up ternaries
        }
    }

    public int getAxis(int axis) {
        return getAxis(axis, true);
    }

    public boolean left() {
        return getAxis(HORIZONTAL_AXIS) < 0;
    }

    public boolean right() {
        return getAxis(HORIZONTAL_AXIS) > 0;
    }

    public boolean down() {
        return getAxis(VERTICAL_AXIS) < 0;
    }

    public boolean up() {
        return getAxis(VERTICAL_AXIS) > 0;
    }
}
