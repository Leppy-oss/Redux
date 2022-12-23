package com.leppy.redux.engine;

import com.leppy.redux.core.Input;
import com.leppy.redux.core.Window;
import com.leppy.redux.render.*;
import com.leppy.redux.scenes.GameScene;
import com.leppy.redux.scenes.Scene;
import com.leppy.redux.util.AssetPool;
import com.leppy.redux.util.ImageParser;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import org.lwjgl.glfw.GLFWImage;
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
    private ImGuiLayer imguilayer;
    private final ImageParser logoIcon = ImageParser.load_image("assets/images/branding/redux-logo.png");
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;

    public ReduxEngine() {
        dt = -1;
        prevTime = 0;
        Input.setInstance(Window.getHandle());
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
            Input.update();
            render();
            Input.endFrame();
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

        GLFWImage image = GLFWImage.malloc(); GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        image.set(get().logoIcon.getWidth(), get().logoIcon.getHeight(), get().logoIcon.getImage());
        imagebf.put(0, image);
        glfwSetWindowIcon(Window.getHandle(), imagebf);

        initImGui();

        get().framebuffer = new Framebuffer(Window.getWidth(), Window.getHeight());
        get().pickingTexture = new PickingTexture(Window.getWidth(), Window.getHeight());
        get().imguilayer = new ImGuiLayer(get().pickingTexture);
        glViewport(0, 0, Window.getWidth(), Window.getHeight());

        ReduxEngine.changeScene(new GameScene());
    }

    /**
     * Basically an update function
     */
    public static void render() {
        Shader defaultShader = AssetPool.getShader("assets/shaders/basic.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        // Render pass 1. Render to picking texture
        glDisable(GL_BLEND);
        get().pickingTexture.enableWriting();

        glViewport(0, 0, Window.getWidth(), Window.getHeight());
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Renderer.bindShader(pickingShader);
        get().scene.render();

        get().pickingTexture.disableWriting();
        glEnable(GL_BLEND);

        // Render pass 2. Render actual game
        get().framebuffer.bind();

        Window.get().clearColor(Window.get().color); // Paint background

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        Renderer.bindShader(defaultShader);

        DebugDraw.beginFrame();
        DebugDraw.draw();

        get().scene.update(get().dt);
        get().scene.render();

        get().framebuffer.unbind(); // The framebuffer MUST be unbound before the imgui viewport is updated aaaaaa
        get().imguilayer.update(get().scene);

        glfwSwapBuffers(Window.getHandle()); // swap the color buffers
    }

    public static void terminate() {
        // Free up memory with termination code
        get().imguilayer.terminate();
        glfwFreeCallbacks(Window.getHandle());
        glfwDestroyWindow(Window.getHandle());
        glfwTerminate();
        // glfwSetErrorCallback(null).free();
    }

    public static void changeScene(Scene newScene) {
        get().scene = newScene;
        // get().scene.load();
        get().scene.init();
        get().scene.start();
    }

    public static void quit() {
        Window.quit();
    }

    public static Scene getScene() {
        return get().scene;
    }


    public static Framebuffer getFramebuffer() {
        return get().framebuffer;
    }

    public static float getTargetAspectRatio() {
        return (float) Window.getWidth() / (float) Window.getHeight();
    }
}
