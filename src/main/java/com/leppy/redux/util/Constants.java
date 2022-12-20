package com.leppy.redux.util;

public class Constants {
    public static final double UI_TILE_MULT = 0.5;
    /** Dimensions for the tile (TILE_LENGTH x TILE_LENGTH) in px */
    public static final int TILE_WIDTH = 64;
    public static final int TILE_HEIGHT = 64;

    public static final int UI_TILE_WIDTH = (int) (((double) (TILE_WIDTH)) * UI_TILE_MULT);
    public static final int UI_TILE_HEIGHT = (int) (((double) (TILE_HEIGHT)) * UI_TILE_MULT);
}
