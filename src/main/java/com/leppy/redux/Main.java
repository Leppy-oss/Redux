package com.leppy.redux;

import com.leppy.redux.engine.GameScene;
import com.leppy.redux.engine.ReduxEngine;
import com.leppy.redux.framework.Mouse;
import com.leppy.redux.framework.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Main {
    public Main() {
        if (glfwInit()) System.out.println("GLFW successfully loaded");
        else System.err.println("GLFW failed to load");
    }

    public static void main(String[] args) {
        new Main(); // Initialize things

        ReduxEngine.init();
        ReduxEngine.start();
        ReduxEngine.terminate();
    }
}
