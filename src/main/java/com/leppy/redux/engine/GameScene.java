package com.leppy.redux.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leppy.redux.framework.*;
import com.leppy.redux.framework.ecs.*;
import com.leppy.redux.framework.ecs.components.*;
import com.leppy.redux.util.*;
import org.joml.*;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene {
    private STATE state = STATE.IDLE;
    private static double FADE_CONST = 5.0;
    private volatile double dt = -1;
    private GameObject testPlayer;
    private GameObject obj1;
    private int spriteIndex = 0;
    private float spriteFlipTime = 0.1f;
    private float spriteFlipTimeLeft = 0.0f;
    private Spritesheet sprites;
    private boolean showText = false;

    transient Vector2f[] texCoords1 = {
            new Vector2f(0.5f, 0.5f),
            new Vector2f(0.5f, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 0.5f)
    };
    transient Vector2f[] texCoords2 = {
            new Vector2f(1.0f, 1.0f),
            new Vector2f(1.0f, 0.5f),
            new Vector2f(0.5f, 0.5f),
            new Vector2f(0.5f, 1.0f)
    };

    public GameScene() {}

    @Override
    public void init() {
        this.loadResources();

        this.camera = new Camera(new Vector2f(0, 0));
        if (loaded) {
            this.activeGameObject = gameObjects.get(0);
            return;
        }

        testPlayer = new GameObject("Testing Player", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 4);
        this.addGameObjectToScene(testPlayer);

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
                new Vector2f(256, 256)), 2);

        obj1.addComponent(new SpriteRenderer(new Vector4f(1, 0, 0, 1)));
        obj1.addComponent(new Rigidbody());

        /*
        obj1.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("assets/images/blendImage1.png")
        )));
        */

        GameObject obj2 = new GameObject("Object 2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 3);
        obj2.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("assets/images/blendImage2.png")
        )));
        this.addGameObjectToScene(obj1);
        this.addGameObjectToScene(obj2);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
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

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("idle-spritesheet",
                new Spritesheet(AssetPool.getTexture("assets/images/player-spritesheet-run.png"),
                        32, 38, 6, 0));

        AssetPool.getTexture("assets/images/blendImage2.png");
        sprites = AssetPool.getSpritesheet("idle-spritesheet");
    }

    public void draw() {
        // System.out.println("FPS: " + (1.0f / dt));

        for (GameObject go : this.gameObjects) {
            go.update((float) dt);
        }

        spriteFlipTimeLeft -= dt;
        if (spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 5) spriteIndex = 0;
        }
        // this.activeGameObject.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        // this.activeGameObject.transform.position.x += 2f;

        this.renderer.render();
    }

    public enum STATE {
        IDLE,
        TRANSITIONING
    }

    @Override
    public void imgui() {
        ImGui.begin("Redux UI");

        ImGui.text("Standard Towers");
        ImGui.selectable("aaa");
        ImGui.showDemoWindow();

        if (ImGui.button("Show towers")) {
            showText = true;
        }

        if (showText) {
            ImGui.text("You clicked a button");
            if (ImGui.button("Stop showing text")) {
                showText = false;
            }
        }

        ImGui.end();
    }
}