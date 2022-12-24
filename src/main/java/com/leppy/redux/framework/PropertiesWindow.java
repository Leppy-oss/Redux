package com.leppy.redux.framework;

import com.leppy.redux.components.NonPickable;
import com.leppy.redux.core.Input;
import com.leppy.redux.ecs.GameObject;
import com.leppy.redux.render.PickingTexture;
import com.leppy.redux.scenes.Scene;
import imgui.ImGui;
import lombok.Getter;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    @Getter
    private GameObject activeGameObject = null;
    private final PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
    }

    public void update(Scene currentScene) {
        if (Input.isMousePressed(GLFW_MOUSE_BUTTON_LEFT)) {
            int x = (int) Input.mouse().getScreenX();
            int y = (int) Input.mouse().getScreenY();
            int gameObjectId = pickingTexture.readPixel(x, y);
            if (currentScene.getGameObject(gameObjectId) != null) {
                GameObject pickedObj = currentScene.getGameObject(gameObjectId);
                if (pickedObj != null && pickedObj.getComponent(NonPickable.class) == null) {
                    activeGameObject = pickedObj;
                } else if (pickedObj == null && !Input.isDragging()) {
                    activeGameObject = null;
                }
            }
        }
    }

    public void imgui() {
        if (activeGameObject != null) {
            ImGui.begin("Properties");
            activeGameObject.imgui();
            ImGui.end();
        }
    }
}