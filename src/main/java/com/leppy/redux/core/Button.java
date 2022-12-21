package com.leppy.redux.core;

import static org.lwjgl.glfw.GLFW.*;

public class Button {
    protected boolean isPressed;
    protected boolean previouslyPressed;
    protected boolean wasJustPressed;
    protected boolean wasJustReleased;
    protected int code;

    public Button(int code) {
        isPressed = false;
        previouslyPressed = false;
        wasJustPressed = false;
        wasJustReleased = false;
        this.setCode(code);
    }

    public void update(int status) {
        wasJustPressed = false;
        wasJustReleased = false;
        if (status == GLFW_PRESS) {
            if(!previouslyPressed) wasJustPressed = true;
            isPressed = true;
        }
        else if (status == GLFW_RELEASE) {
            if (previouslyPressed) wasJustReleased = true;
            isPressed = false;
        }
        previouslyPressed = isPressed;
    }

    public boolean isPressed() {
        return this.isPressed;
    }

    public boolean previouslyPressed() {
        return previouslyPressed;
    }

    public boolean wasJustPressed() {
        return wasJustPressed;
    }

    public boolean wasJustReleased() {
        return wasJustReleased;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}