package com.leppy.redux.components;

import com.leppy.redux.core.Input;
import com.leppy.redux.framework.PropertiesWindow;
import com.leppy.redux.framework.Sprite;

public class TranslateGizmo extends Gizmo {

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        super(arrowSprite, propertiesWindow);
    }

    @Override
    public void update(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.position.x -= Input.mouse().getWdX();
            } else if (yAxisActive) {
                activeGameObject.transform.position.y -= Input.mouse().getWdY();
            }
        }

        super.update(dt);
    }
}