package com.leppy.redux.framework;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {
    public static final int MINSET = 32;
    public static final int MAXSET = 349;
    public Key keys[] = new Key[MAXSET - MINSET];
    public final int HORIZONTAL_AXIS = 0;
    public final int VERTICAL_AXIS = 1;

    public Keyboard() {
        for (int i = 0; i < MAXSET - MINSET; i++) keys[i] = new Key(i + MINSET);
        System.out.println(keys[0].getKeyCode());
    }

    /*
    public void keyChangeHandler(long windowHandle, int keyCode, int scanCode, int action, int mods) {
        this.wasJustPressed[keyCode] = false;
        if (keyCode < this.isPressed.length) {
            if (action == GLFW_PRESS) {
                if (!this.pressedCache[keyCode]) this.wasJustPressed[keyCode] = true;
                this.isPressed[keyCode] = true;
            }
            else if (action == GLFW_RELEASE) this.isPressed[keyCode] = false;
        }
        this.pressedCache[keyCode] = this.isPressed[keyCode];
    }
    */

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
                int left = (isPressed(GLFW_KEY_A) || isPressed(GLFW_KEY_LEFT)) ? -1 : 0;
                int right = (isPressed(GLFW_KEY_D) || isPressed(GLFW_KEY_RIGHT)) ? 1 : 0;
                return sumOfBoth ? left + right : (left < 0 ? left : right); // TODO: Clean up ternaries

            default:
                int down = (isPressed(GLFW_KEY_S) || isPressed(GLFW_KEY_DOWN)) ? -1 : 0;
                int up = (isPressed(GLFW_KEY_W) || isPressed(GLFW_KEY_UP) || isPressed(GLFW_KEY_SPACE)) ? 1 : 0;
                return sumOfBoth ? down + up : (up > 0 ? up : down); // TODO: Clean up ternaries
        }
    }

    public int getAxis(int axis) {
        return getAxis(axis, false);
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
