package com.leppy.redux.engine;

import com.leppy.redux.framework.*;
import com.leppy.redux.framework.render.Shader;
import com.leppy.redux.util.*;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class GameScene extends Scene {
    private STATE state = STATE.IDLE;
    private static double FADE_CONST = 5.0;
    private volatile double dt = -1;

    private int vertexID, fragmentID, shaderProgram, vaoID, vboID, eboID;
    private Shader defaultShader;

    private float[] vertexArray = {
            // position               // color
            100.5f, 0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            0.5f,  100.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            100.5f, 100.5f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            0.5f, 0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            /*
                    x        x
                    x        x
             */
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        this.initializeShaders();
    }

    public void initializeShaders() {
        defaultShader = new Shader("assets/shaders/basic.glsl");
        defaultShader.compile();
        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(double dt) {
        this.dt = dt;
        this.handleControls();
        this.handleState();
        this.draw();
    }

    public void handleControls() {
        if (state != STATE.TRANSITIONING && ControlSystem.keyboard().wasJustPressed(GLFW_KEY_SPACE)) state = STATE.TRANSITIONING;
        if (ControlSystem.keyboard().wasJustPressed(GLFW_KEY_ESCAPE)) ReduxEngine.quit();
        camera.offsetPosition((float) (dt * 100.0f * ControlSystem.keyboard().getAxis(Keyboard.HORIZONTAL_AXIS)), (float) (dt * 100.0f * ControlSystem.keyboard().getAxis(Keyboard.VERTICAL_AXIS)));
    }

    public void handleState() {
        switch (state) {
            case TRANSITIONING: {
                RGBFWrapper windowColor = Window.getColor();
                if (windowColor.R > 0 || windowColor.G > 0 || windowColor.B > 0) {
                    Window.get().color.R -= dt * FADE_CONST;
                    Window.get().color.G -= dt * FADE_CONST;
                    Window.get().color.B -= dt * FADE_CONST;
                } else state = STATE.IDLE;
                break;
            }
            case IDLE:
            default:
                break;
        }
    }

    public void draw() {
        defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getCameraMatrix());
        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        defaultShader.detach();
    }

    public enum STATE {
        IDLE,
        TRANSITIONING
    }
}
