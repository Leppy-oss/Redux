package com.leppy.redux.ecs.components;

import com.leppy.redux.core.Camera;
import com.leppy.redux.core.Input;
import com.leppy.redux.util.Constants;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Component that handles behavior for the camera when editing maps
 * TODO: Create a GameCamera that handles the logic for updating a camera in-game (shouldn't require too much)
 */
public class EditorCamera extends Component {
    private final Camera camera;
    private Vector2f clickOrigin;
    private boolean reset = false;

    private float lerpTime = 0.0f;
    private static final float dragSens = 30.0f;
    private static final float scrollSens = 0.1f;

    public EditorCamera(Camera camera) {
        this.camera = camera;
        this.clickOrigin = new Vector2f();
    }

    @Override
    public void update(float dt) {
        if (Input.mouseJustPressed(GLFW_MOUSE_BUTTON_MIDDLE)) {
            this.clickOrigin = new Vector2f(Input.getOrthoX(), Input.getOrthoY());
            return;
        } else if (Input.isMousePressed(GLFW_MOUSE_BUTTON_MIDDLE)) {
            Vector2f mousePos = new Vector2f(Input.getOrthoX(), Input.getOrthoY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);
            // Hopefully the timestep is somewhat fixed?
            camera.position.sub(delta.mul(dt).mul(dragSens));
            this.clickOrigin.lerp(mousePos, dt);
        }

        if (Input.getSY() != 0.0f) {
            float addValue = (float)Math.pow(Math.abs(Input.getSY() * scrollSens),
                    1 / camera.getZoom());
            addValue *= -Math.signum(Input.getSY());
            camera.addZoom(addValue);
        }

        if (Input.isPressed(GLFW_KEY_PERIOD)) {
            reset = true;
        }

        if (reset) {
            camera.position.lerp(Constants.CAMERA_ORIGIN, lerpTime);
            camera.setZoom(this.camera.getZoom() +
                    ((1.0f - camera.getZoom()) * lerpTime));
            this.lerpTime += 0.1f * dt;
            if (Math.abs(camera.position.x) <= 5.0f &&
                    Math.abs(camera.position.y) <= 5.0f) {
                this.lerpTime = 0.0f;
                camera.position.set(0f, 0f);
                this.camera.setZoom(1.0f);
                reset = false;
            }
        }
    }
}