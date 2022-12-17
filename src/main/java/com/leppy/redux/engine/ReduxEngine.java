package com.leppy.redux.engine;

import com.leppy.redux.framework.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.opengl.GL11.*;

public class ReduxEngine {
    private Window window;
    private boolean d_u = true; // TODO: only here for experimentation

    public ReduxEngine() {
        window = new Window("Redux"); // should init and set all the props in the constructor
        ControlSystem.setInstance(window.getHandle());
    }

    public void start() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        while (!glfwWindowShouldClose(window.getHandle())) {
            this.render();
        }
    }

    /**
     * Basically an update function
     */
    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        window.clearColor(window.color); // Paint background

        glfwSwapBuffers(window.getHandle()); // swap the color buffers

        ControlSystem.update();

        if (ControlSystem.keyboard().wasJustPressed(GLFW_KEY_SPACE)) {
            if (d_u) {
                if (window.color.R < 1.0f) {
                    window.color.R += 0.05f;
                    window.color.G += 0.05f;
                }
                else d_u = false;
            }
            else {
                if (window.color.R > 0.0f) {
                    window.color.R -= 0.05f;
                    window.color.G -= 0.05f;
                }
                else d_u = true;
            }
        }
    }


    public void terminate() {
        // Free up memory with termination code
        glfwFreeCallbacks(window.getHandle());
        glfwDestroyWindow(window.getHandle());
        glfwTerminate();
        glfwSetErrorCallback(null).free(); // TODO: Add handling for cases where cbfun does not match the register
    }
}
