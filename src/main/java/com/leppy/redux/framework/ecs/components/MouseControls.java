package com.leppy.redux.framework.ecs.components;

import com.leppy.redux.core.Input;
import com.leppy.redux.engine.ReduxEngine;
import com.leppy.redux.framework.ecs.GameObject;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static com.leppy.redux.util.Constants.*;

/**
 * Wrapper component for handling drag/drop functionality
 */
public class MouseControls extends Component {
    GameObject holdingObject = null;

    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        ReduxEngine.getScene().addGameObjectToScene(go);
    }

    public void place() {
        this.holdingObject = null;
    }

    @Override
    public void update(float dt) {
        // if (!Input.isDragging()) holdingObject = null;
        if (holdingObject != null) {
            holdingObject.transform.position.x = Input.getOrthoX() - UI_TILE_WIDTH / 2.0f;
            holdingObject.transform.position.y = Input.getOrthoY() - UI_TILE_HEIGHT / 2.0f;

            if (Input.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}