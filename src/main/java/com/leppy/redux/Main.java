package com.leppy.redux;

import com.leppy.redux.engine.ReduxEngine;

import static org.lwjgl.glfw.GLFW.*;

public class Main {
    public static void main(String[] args) {
        if (glfwInit()) System.out.println("GLFW successfully loaded");
        else System.err.println("GLFW failed to load");

        ReduxEngine.init();
        ReduxEngine.run();
    }
}
