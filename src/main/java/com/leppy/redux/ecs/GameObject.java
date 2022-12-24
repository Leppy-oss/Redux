package com.leppy.redux.ecs;

import com.leppy.redux.components.Component;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameObject {
    @Setter
    public String name;

    private final List<Component> components;
    @Setter
    public Transform transform;
    @Setter
    private int zIndex;
    private static int ID_COUNTER = 0; // keeps track of current UID
    @Setter
    private int uid = -1;
    @Accessors(fluent = true) @Getter
    private boolean doSerialization = true;

    public GameObject(String name) {
        this(name, new Transform(), 0);
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.zIndex = zIndex;
        this.components = new ArrayList<>();
        this.transform = transform;

        this.setUid(ID_COUNTER++);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i=0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        c.generateId();
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float dt) {
        for (Component component : components) component.update(dt);
    }

    public void start() {
        for (Component component : components) component.start();
    }

    public void imgui() {
        for (Component c : components) c.imgui();
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }


    public void setNoSerialize() {
        this.doSerialization = false;
    }
}