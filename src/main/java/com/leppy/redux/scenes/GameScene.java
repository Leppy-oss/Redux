package com.leppy.redux.scenes;

import com.google.gson.*;
import com.leppy.redux.core.*;
import com.leppy.redux.engine.ReduxEngine;
import com.leppy.redux.framework.*;
import com.leppy.redux.framework.ecs.*;
import com.leppy.redux.framework.ecs.components.*;
import com.leppy.redux.framework.render.DebugDraw;
import com.leppy.redux.util.*;
import imgui.*;
import org.joml.*;

import java.lang.Math;

import static imgui.flag.ImGuiCol.Text;
import static org.lwjgl.glfw.GLFW.*;
import static com.leppy.redux.util.Constants.*;

public class GameScene extends Scene {
    private static final double FADE_CONST = 5.0;

    private STATE state = STATE.IDLE;
    private volatile double dt = -1;
    private Spritesheet tiles;
    private GameObject gameIntrinsics;

    public GameScene() {}

    @Override
    public void init() {
        this.loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        this.gameIntrinsics = new GameObject("Game Intrinsics", new Transform(new Vector2f()), 0);
        this.gameIntrinsics.addComponent(new MouseControls());
        this.gameIntrinsics.addComponent(new GridLines());
        this.gameIntrinsics.addComponent(new EditorCamera(camera));
        this.addGameObjectToScene(gameIntrinsics);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    float t = 0.0f;

    @Override
    public void update(double dt) {
        this.dt = dt;

        this.handleControls();
        this.handleState();
        this.draw();
    }

    public void handleControls() {
        if (state != STATE.TRANSITIONING && Input.keyboard().wasJustPressed(GLFW_KEY_SPACE)) state = STATE.TRANSITIONING;
        if (Input.wasJustPressed(GLFW_KEY_ESCAPE)) ReduxEngine.quit();
        camera.position.add((float) (dt * 1000.0f * Input.keyboard().getAxis(Keyboard.HORIZONTAL_AXIS)), (float) (dt * 1000.0f * Input.keyboard().getAxis(Keyboard.VERTICAL_AXIS)));
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
        AssetPool.getShader("assets/shaders/basic.glsl");

        AssetPool.addSpritesheet("64x-tiles",
                new Spritesheet(AssetPool.getTexture("assets/images/td/Tilesheet/towerDefense_tilesheet.png"),
                        64, 64, 89, 0));

        AssetPool.getTexture("assets/images/blendImage2.png");

        // Ensure that each GameObject has its sprite loaded
        for (GameObject g : gameObjects) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
        }

        tiles = AssetPool.getSpritesheet("64x-tiles");
    }

    public void draw() {
        float x = ((float) Math.sin(t) * 200.0f) + 600;
        float y = ((float) Math.cos(t) * 200.0f) + 400;
        t += 0.05f;
        DebugDraw.addLine2D(new Vector2f(600, 400), new Vector2f(x, y), new Vector3f(0, 0, 1), 10);
        DebugDraw.addCircle(new Vector2f(x, y), 64, new Vector3f(0, 1, 0), 10);
        // System.out.println("FPS: " + (1.0f / dt));
        this.camera.adjustProjection(); // update camera intrinsics based on new zoom, orthography, etc.

        for (GameObject go : this.gameObjects) {
            go.update((float) dt);
        }


        if (this.gameIntrinsics.getComponent(MouseControls.class).shouldCreateNewObject()) {
            SpriteRenderer oldSprite = gameIntrinsics.getComponent(MouseControls.class).getHoldingObject().getComponent(SpriteRenderer.class);
            GameObject object = Prefabs.generateSpriteObject(oldSprite.getSprite(), (float) oldSprite.getSprite().getWidth(), (float) oldSprite.getSprite().getHeight());
            gameIntrinsics.getComponent(MouseControls.class).pickupObject(object);
        }

        this.renderer.render();
    }

    public enum STATE {
        IDLE,
        TRANSITIONING
    }

    @Override
    public void imgui() {
        ImGui.showDemoWindow();

        ImGui.pushStyleColor(Text, 1.0f, 1.0f, 0.0f, 1.00f);
        ImGui.begin("Map Editor");
        ImGui.popStyleColor(1);

        ImGui.text("Standard Tiles");

        if (ImGui.button("Click For More Tiles")) {
            if (!ImGui.button("Stop Showing \"Extra\" Tiles")) ImGui.text("jk i havent put them in yet");
        }

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i=0; i < tiles.size(); i++) {
            Sprite sprite = tiles.getSprite(i);
            double spriteWidth = sprite.getWidth() * UI_TILE_MULT;
            double spriteHeight = sprite.getHeight() * UI_TILE_MULT;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, (float) spriteWidth, (float) spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) { //apparent mismatch in coords allows it to be properly flipped
                GameObject object = Prefabs.generateSpriteObject(sprite, (float) spriteWidth, (float) spriteHeight);
                gameIntrinsics.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            double lastButtonX2 = lastButtonPos.x;
            double nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < tiles.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }

    @Override
    public void render() {
        this.renderer.render();
    }
}