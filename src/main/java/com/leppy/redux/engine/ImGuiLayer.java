package com.leppy.redux.engine;

import com.leppy.redux.scenes.Scene;
import com.leppy.redux.core.Window;
import imgui.*;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiFreeTypeBuilderFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import static com.leppy.redux.engine.ReduxEngine.glslVersion;

public class ImGuiLayer {
    private boolean showText = false;
    private ImFont font;
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();

    public ImGuiLayer() {
        this.init();
    }

    public void update(Scene currentScene) {
        this.imGuiGlfw.newFrame();
        ImGui.newFrame();
        currentScene.sceneImgui();
        ImGui.showDemoWindow();
        ImGui.render();
        this.imGuiGl3.renderDrawData(ImGui.getDrawData());
        endImGuiFrame();
    }

    public void init() {
        this.imGuiGlfw.init(Window.getHandle(), true);

        final ImGuiIO io = ImGui.getIO();
        final ImFontAtlas atlas = io.getFonts();
        final ImFontConfig config = new ImFontConfig();
        config.setGlyphRanges(atlas.getGlyphRangesDefault());

        config.setPixelSnapH(true);
        atlas.addFontFromFileTTF("assets/fonts/open-sans.ttf", 24, config);
        atlas.setFlags(ImGuiFreeTypeBuilderFlags.LightHinting);
        atlas.build();
        config.destroy();
        io.setIniFilename("imgui.ini");

        this.imGuiGl3.init(glslVersion);
    }

    public void terminate() {
        this.imGuiGl3.dispose();
        this.imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    public void endImGuiFrame() {
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            org.lwjgl.glfw.GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
        ImGui.endFrame();
    }
}