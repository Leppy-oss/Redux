package com.leppy.redux.components;

import com.leppy.redux.framework.PropertiesWindow;
import com.leppy.redux.framework.Sprite;
import com.leppy.redux.core.Input;

public class ScaleGizmo extends Gizmo {
    public ScaleGizmo(Sprite scaleSprite, PropertiesWindow propertiesWindow) {
        super(scaleSprite, propertiesWindow);
    }

    @Override
    public void update(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.scale.x -= Input.mouse().getWdX();
            } else if (yAxisActive) {
                activeGameObject.transform.scale.y -= Input.mouse().getWdY();
            }
        }

        super.update(dt);
    }
}