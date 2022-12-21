package com.leppy.redux.util;

public class Constants {
    public static final double UI_TILE_MULT = 1.0;
    /** Dimensions for the tile (TILE_LENGTH x TILE_LENGTH) in px */
    public static final int TILE_WIDTH = 64;
    public static final int TILE_HEIGHT = 64;

    public static final int UI_TILE_WIDTH = (int) (((double) (TILE_WIDTH)) * UI_TILE_MULT);
    public static final int UI_TILE_HEIGHT = (int) (((double) (TILE_HEIGHT)) * UI_TILE_MULT);
    public static final int GRID_WIDTH = UI_TILE_WIDTH;
    public static final int GRID_HEIGHT = UI_TILE_HEIGHT;
}
