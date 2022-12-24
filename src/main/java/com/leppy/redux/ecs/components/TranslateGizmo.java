package com.leppy.redux.ecs.components;

import com.leppy.redux.ecs.*;
import com.leppy.redux.engine.ReduxEngine;
import com.leppy.redux.framework.*;
import com.leppy.redux.util.Constants;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.Objects;

public class TranslateGizmo extends Component {
    private final Vector4f xAxisColor = new Vector4f(1, 0, 0, 1);
    private final Vector4f xAxisColorHover = new Vector4f();
    private final Vector4f yAxisColor = new Vector4f(0, 1, 0, 1);
    private final Vector4f yAxisColorHover = new Vector4f();

    private final GameObject xAxisObject, yAxisObject;
    private final SpriteRenderer xAxisSprite, yAxisSprite;
    private GameObject activeGameObject = null;

    private Vector2f xAxisOffset = new Vector2f(128, -18);
    private Vector2f yAxisOffset = new Vector2f(44, 128);

    private final PropertiesWindow propertiesWindow;

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, Constants.TILE_WIDTH, Constants.TILE_HEIGHT * 2, "xAxisObj");
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, Constants.TILE_WIDTH, Constants.TILE_HEIGHT * 2, "yAxisObj");
        this.xAxisSprite = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        ReduxEngine.getScene().addGameObjectToScene(this.xAxisObject);
        ReduxEngine.getScene().addGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void start() {
        this.xAxisObject.transform.setRotation(90f);
        this.yAxisObject.transform.setRotation(180f);
        this.xAxisObject.setNoSerialize();
        this.yAxisObject.setNoSerialize();
    }

    @Override
    public void update(float dt) {
        this.xAxisObject.setZIndex(-1);
        this.yAxisObject.setZIndex(-1);

        if (this.activeGameObject != null) {
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisObject.transform.position.add(this.xAxisOffset);
            this.yAxisObject.transform.position.add(this.yAxisOffset);
        }

        this.activeGameObject = this.propertiesWindow.getActiveGameObject();
        if (this.activeGameObject == null) this.setInactive();
        else if (Objects.equals(this.propertiesWindow.getActiveGameObject().getName(), "Sprite_Object_Gen")) {
            this.setActive();
        }
    }

    private void setActive() {
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }

    private void setInactive() {
        this.activeGameObject = null;
        this.xAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
        this.yAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
    }
}