package com.leppy.redux.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static org.lwjgl.glfw.GLFW.*;

@Accessors(fluent = true)
public class Button {
    @Getter
    protected boolean isPressed, previouslyPressed, wasJustPressed, wasJustReleased;
    @Accessors(fluent = false) @Getter @Setter
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
}