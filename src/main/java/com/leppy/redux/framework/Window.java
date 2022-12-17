package com.leppy.redux.framework;

import com.leppy.redux.engine.Scene;
import com.leppy.redux.util.RGBFWrapper;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public static class Properties {
        private int width;
        private int height;
        private String name;
        private RGBFWrapper color;

        public Properties(int width, int height, String name, RGBFWrapper color) {
            this.width = width;
            this.height = height;
            this.name = name;
            this.color = color;
        }
    }

    public static Window instance;
    private int width, height;
    private String name;
    public RGBFWrapper color;

    protected static final int DEFAULT_WIDTH = 1920;
    protected static final int DEFAULT_HEIGHT = 1080;
    protected static final String DEFAULT_NAME = "Redux Window";
    protected static final RGBFWrapper DEFAULT_COLOR = new RGBFWrapper(1.0f, 1.0f, 1.0f, 1.0f);

    protected long handle;

    public Window(Properties properties) {
        this.width = properties.width;
        this.height = properties.height;
        this.name = properties.name;
        this.color = properties.color;

        this.init();
    }

    public Window() {
        this(new Properties(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_NAME, DEFAULT_COLOR));
    }

    public Window(String name) {
        this(new Properties(DEFAULT_WIDTH, DEFAULT_HEIGHT, name, DEFAULT_COLOR));
    }

    @Deprecated
    /**
     * bad
     */
    public void run() {
        System.out.println("Redux framework with LWJGL version " + Version.getVersion() + " is now running");
        this.init();
    }
    
    public static Window get() {
        if (instance == null) instance = new Window();
        return instance;
    }

    public void init() {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        this.handle = glfwCreateWindow(this.width, this.height, this.name, NULL, NULL);
        if (this.handle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(this.handle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    this.handle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(this.handle);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(this.handle);
    }

    @Deprecated
    public static void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        get().clearColor(get().color); // Paint background
        glClear(GL_COLOR_BUFFER_BIT);
        glfwSwapBuffers(get().handle); // swap the color buffers
    }

    public static void setColor(RGBFWrapper color) {
        get().color = color;
    }

    public static void setColor(float R, float G, float B, float F) {
        setColor(new RGBFWrapper(R, G, B, F));
    }

    public static RGBFWrapper getColor() {
        return get().color;
    }

    public static long getHandle() {
        return get().handle;
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void quit() {
        glfwSetWindowShouldClose(get().handle, true);
    }

    public void clearColor(RGBFWrapper color) {
        glClearColor(color.R, color.G, color.B, color.F);
    }
}