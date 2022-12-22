package com.leppy.redux.framework.ecs.components;

import com.leppy.redux.core.Camera;
import com.leppy.redux.core.Input;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Component that handles behavior for the camera when editing maps
 * TODO: Create a GameCamera that handles the logic for updating a camera in-game (shouldn't require too much)
 */
public class EditorCamera extends Component {
    private float dragDebounce = 0.032f;

    private Camera editorCamera;
    private Vector2f clickOrigin;
    private boolean reset = false;

    private float lerpTime = 0.0f;
    private float dragSensitivity = 30.0f;
    private float scrollSensitivity = 0.1f;

    public EditorCamera(Camera editorCamera) {
        this.editorCamera = editorCamera;
        this.clickOrigin = new Vector2f();
    }

    @Override
    public void update(float dt) {
        if (Input.mouseJustPressed(GLFW_MOUSE_BUTTON_MIDDLE)) {
            this.clickOrigin = new Vector2f(Input.getOrthoX(), Input.getOrthoY());
            dragDebounce -= dt;
            return;
        } else if (Input.isMousePressed(GLFW_MOUSE_BUTTON_MIDDLE)) {
            Vector2f mousePos = new Vector2f(Input.getOrthoX(), Input.getOrthoY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);
            // Hopefully the timestep is somewhat fixed?
            editorCamera.position.sub(delta.mul(dt).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, dt);
        }

        if (dragDebounce <= 0.0f && !Input.isMousePressed(GLFW_MOUSE_BUTTON_MIDDLE)) { // just use mouseJustPressed() instead
            dragDebounce = 0.1f;
        }

        if (Input.sY() != 0.0f) {
            float addValue = (float)Math.pow(Math.abs(Input.sY() * scrollSensitivity),
                    1 / editorCamera.getZoom());
            addValue *= -Math.signum(Input.sY());
            editorCamera.addZoom(addValue);
        }

        if (Input.isPressed(GLFW_KEY_PERIOD)) {
            reset = true;
        }

        if (reset) {
            editorCamera.position.lerp(new Vector2f(), lerpTime);
            editorCamera.setZoom(this.editorCamera.getZoom() +
                    ((1.0f - editorCamera.getZoom()) * lerpTime));
            this.lerpTime += 0.1f * dt;
            if (Math.abs(editorCamera.position.x) <= 5.0f &&
                    Math.abs(editorCamera.position.y) <= 5.0f) {
                this.lerpTime = 0.0f;
                editorCamera.position.set(0f, 0f);
                this.editorCamera.setZoom(1.0f);
                reset = false;
            }
        }
    }
}