package com.leppy.redux.engine;

import com.leppy.redux.framework.Camera;

public abstract class Scene {
    protected Camera camera;
    public Scene() {}
    public void init() {}
    public abstract void update(double dt);
}
