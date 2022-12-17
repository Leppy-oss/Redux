package com.leppy.redux.framework;

import static org.lwjgl.glfw.GLFW.*;

public class Button {
    private boolean isPressed;
    private boolean previouslyPressed;
    private boolean wasJustPressed;
    private int code;

    public Button(int code) {
        isPressed = false;
        previouslyPressed = false;
        wasJustPressed = false;
        this.setCode(code);
    }

    public void update(int status) {
        wasJustPressed = false;
        if (status == GLFW_PRESS) {
            if(!previouslyPressed) wasJustPressed = true;
            wasJustPressed = true;
        }
        else if (status == GLFW_RELEASE) isPressed = false;
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

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}