package com.leppy.redux.framework;

import com.leppy.redux.util.RGBFWrapper;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

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

    private int width, height;
    private String name;
    private static Window window = null;
    private RGBFWrapper color;

    public static int DEFAULT_WIDTH = 1920;
    public static int DEFAULT_HEIGHT = 1080;
    public static String DEFAULT_NAME = "Redux Window";
    public static RGBFWrapper DEFAULT_COLOR = new RGBFWrapper(1.0f, 1.0f, 1.0f, 1.0f);

    protected long handle;

    public Window(Properties properties) {
        this.width = properties.width;
        this.height = properties.height;
        this.name = properties.name;
        this.color = properties.color;
    }

    public Window() {
        this(new Properties(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_NAME, DEFAULT_COLOR));
    }

    public Window(String name) {
        this(new Properties(DEFAULT_WIDTH, DEFAULT_HEIGHT, name, DEFAULT_COLOR));
    }

    public static void setWindow(Window window) {
        Window.window = window;
    }

    public static Window getWindow() {
        if (Window.window == null) Window.window = new Window();

        return Window.window;
    }

    public static Window getWindow(Properties properties) {
        if (Window.window == null) Window.window = new Window(properties);

        return Window.getWindow();
    }

    public void run() {
        System.out.println("Redux framework with LWJGL version " + Version.getVersion() + " is now running");

        init();
        loop();
    }

    private void init() {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        handle = glfwCreateWindow(window.width, window.height, window.name, NULL, NULL);
        if ( handle == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Set up a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(handle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    handle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(handle);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(handle);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        this.clearColor(this.color);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(handle) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glfwSwapBuffers(handle); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public void setColor(RGBFWrapper color) {
        this.color = color;
    }

    public void clearColor(RGBFWrapper color) {
        glClearColor(color.R, color.G, color.B, color.F);
    }
}
