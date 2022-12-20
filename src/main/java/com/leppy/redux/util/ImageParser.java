package com.leppy.redux.util;

import org.lwjgl.system.MemoryStack;

import java.nio.*;

import static org.lwjgl.stb.STBImage.stbi_load;

/**
 * Utility class for parsing images from raw bytes
 */
public class ImageParser {
    public ByteBuffer getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private ByteBuffer image;
    private int width, height;

    ImageParser(int width, int height, ByteBuffer image) {
        this.image = image;
        this.height = height;
        this.width = width;
    }
    public static ImageParser load_image(String path) {
        ByteBuffer image;
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            image = stbi_load(path, w, h, comp, 4);
            if (image == null) {
                // throw new resource_error("Could not load image resources.");
            }
            width = w.get();
            height = h.get();
        }
        return new ImageParser(width, height, image);
    }
}