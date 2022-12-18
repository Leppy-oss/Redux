package com.leppy.redux.engine;

import com.leppy.redux.framework.Camera;
import com.leppy.redux.framework.ControlSystem;
import com.leppy.redux.framework.Keyboard;
import com.leppy.redux.framework.Window;
import com.leppy.redux.framework.ecs.GameObject;
import com.leppy.redux.framework.ecs.Transform;
import com.leppy.redux.framework.ecs.components.SpriteRenderer;
import com.leppy.redux.util.RGBFWrapper;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class GameScene extends Scene {
    private STATE state = STATE.IDLE;
    private static double FADE_CONST = 5.0;
    private volatile double dt = -1;

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float) (1280 - xOffset * 2);
        float totalHeight = (float) (720 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;
        float padding = 0;

        for (int x=0; x < 60; x++) {
            for (int y=0; y < 60; y++) {
                float xPos = xOffset + (x * sizeX) + (padding * x);
                float yPos = yOffset + (y * sizeY) + (padding * y);

                GameObject go = new GameObject("Obj" + x + "" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObjectToScene(go);
            }
        }
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
        camera.offsetPosition((float) (dt * 1000.0f * ControlSystem.keyboard().getAxis(Keyboard.HORIZONTAL_AXIS)), (float) (dt * 1000.0f * ControlSystem.keyboard().getAxis(Keyboard.VERTICAL_AXIS)));
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
        System.out.println("FPS: " + (1.0f / dt));

        for (GameObject go : this.gameObjects) {
            go.update((float) dt);
        }

        this.renderer.render();
    }

    public enum STATE {
        IDLE,
        TRANSITIONING
    }
}
