package com.leppy.redux;

import com.leppy.redux.framework.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Main {
    protected static Window window;

    public Main() {
        if (glfwInit()) System.out.println("GLFW successfully loaded");
        else System.err.println("GLFW failed to load");

        Window.setWindow(new Window("Redux"));
        this.window = Window.getWindow();
    }

    public static void main(String[] args) {
        new Main();

        window.run();
    }
}
