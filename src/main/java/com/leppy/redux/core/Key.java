package com.leppy.redux.core;

import lombok.AllArgsConstructor;

/**
 * Syntactic sugar for {@link Button}, functionally identical
 */
public class Key extends Button {
    public Key(int code) {
        super(code);
    }

    private void setKeyCode(int code) {
        super.setCode(code);
    }

    public int getKeyCode() {
        return super.getCode();
    }
}
