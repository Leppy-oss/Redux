package com.leppy.redux.engine;

import com.leppy.redux.framework.ControlSystem;
import com.leppy.redux.framework.Window;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class ReduxEngine {
    /**
     * global game engine instance
     */
    public static ReduxEngine instance;
    private Scene scene;
    private double dt; // Delta time
    private double prevTime;

    protected static final String glslVersion = "#version 300 es"; // from basic.glsl, default.glsl, etc.
    private ImGuiLayer imguilayer;

    public ReduxEngine() {
        dt = -1;
        prevTime = 0;
        ControlSystem.setInstance(Window.getHandle());
    }

    public static ReduxEngine get() {
        if (instance == null) instance = new ReduxEngine();

        return instance;
    }

    public static void run() {
        start();
        terminate();
    }

    /**
     * make sure to init() first
     */
    public static void start() {
        get().prevTime = glfwGetTime();
        while (!glfwWindowShouldClose(Window.getHandle())) {
            double endTime = glfwGetTime();
            get().dt = endTime - get().prevTime;
            get().prevTime = endTime;
            ControlSystem.update();
            render();
        }
        get().scene.saveExit();
    }

    public static void initImGui() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
    }

    public static void init() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        get(); // ensure openGL has initialized
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        initImGui();

        get().imguilayer = new ImGuiLayer();
        ReduxEngine.changeScene(new GameScene());
    }

    /**
     * Basically an update function
     */
    public static void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        Window.get().clearColor(Window.get().color); // Paint background
        glClear(GL_COLOR_BUFFER_BIT);
        get().scene.update(get().dt);
        get().imguilayer.update(get().scene);

        glfwSwapBuffers(Window.getHandle()); // swap the color buffers
    }

    public static void terminate() {
        // Free up memory with termination code
        get().imguilayer.terminate();
        glfwFreeCallbacks(Window.getHandle());
        glfwDestroyWindow(Window.getHandle());
        glfwTerminate();
        glfwSetErrorCallback(null).free(); // TODO: Add handling for cases where cbfun does not match the register
    }

    public static void changeScene(Scene newScene) {
        get().scene = newScene;
        get().scene.load();
        get().scene.init();
        get().scene.start();
    }

    public static void quit() {
        Window.quit();
    }

    public static Scene getScene() {
        return get().scene;
    }
}
