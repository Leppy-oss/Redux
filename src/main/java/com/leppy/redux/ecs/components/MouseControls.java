package com.leppy.redux.ecs.components;

import com.leppy.redux.core.Input;
import com.leppy.redux.engine.ReduxEngine;
import com.leppy.redux.ecs.GameObject;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static com.leppy.redux.util.Constants.*;

/**
 * Wrapper component for handling drag/drop functionality
 */
public class MouseControls extends Component {
    private GameObject holdingObject = null;
    private boolean isHolding = false;
    private boolean wasJustDragging = false;
    private boolean shouldCreateNewObject = false;
    private float oldPosX = 0;
    private float oldPosY = 0;

    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        this.isHolding = true;
        ReduxEngine.getScene().addGameObjectToScene(go);
    }

    /**
     * Stop doing stuff
     */
    public void place() {
        this.holdingObject = null;
        this.wasJustDragging = false;
        this.isHolding = false;
    }

    public boolean isHolding() {
        return this.isHolding;
    }

    public boolean shouldCreateNewObject() {
        return this.shouldCreateNewObject;
    }

    public void requestNewObjectPickup() {
        this.shouldCreateNewObject = true;
    }

    public void dontCreateNewObject() {
        this.shouldCreateNewObject = false;
    }

    public GameObject getHoldingObject() {
        return this.holdingObject;
    }

    @Override
    public void update(float dt) {
        // if (!Input.isDragging()) holdingObject = null;
        if (holdingObject != null) {
            holdingObject.transform.position.x = Input.getOrthoX();
            holdingObject.transform.position.y = Input.getOrthoY();
            // System.out.println(Input.getOrthoX() + ", " + Input.getOrthoY());
            holdingObject.transform.position.x = (int)(holdingObject.transform.position.x / GRID_WIDTH) * GRID_WIDTH;
            holdingObject.transform.position.y = (int)(holdingObject.transform.position.y / GRID_HEIGHT) * GRID_HEIGHT;
            if (((holdingObject.transform.position.x != oldPosX) || (holdingObject.transform.position.y != oldPosY)) && Input.isDragging()) {
                this.wasJustDragging = true;
                this.requestNewObjectPickup();
            }
            else this.dontCreateNewObject();

            oldPosX = holdingObject.transform.position.x;
            oldPosY = holdingObject.transform.position.y;
            if (!Input.isDragging() && this.wasJustDragging || (!Input.isDragging()) && Input.mouseJustPressed(GLFW_MOUSE_BUTTON_LEFT)) place();
        }
    }
}