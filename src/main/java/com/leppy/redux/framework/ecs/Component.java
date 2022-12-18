package com.leppy.redux.framework.ecs;

/**
 * Overarching class for components (think "attributes" of gameobjects like sprite renderers, hitboxes, etc.)
 */
public abstract class Component {

    public GameObject gameObject = null;

    public void start() {}

    public abstract void update(float dt);
}