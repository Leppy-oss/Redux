package com.leppy.redux.engine;

import com.leppy.redux.framework.*;
import com.leppy.redux.render.PickingTexture;
import com.leppy.redux.scenes.Scene;
import com.leppy.redux.core.Window;
import imgui.*;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import lombok.Getter;

import static com.leppy.redux.util.Constants.glslVersion;
import static imgui.flag.ImGuiConfigFlags.DockingEnable;

public class ImGuiLayer {
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private GameWindow gameWindow;
    private ImGuiStyleHelper styleHelper;

    private final GameWindow gameViewWindow;
    @Getter
    private final PropertiesWindow propertiesWindow;

    public ImGuiLayer(PickingTexture pickingTexture) {
        this.gameViewWindow = new GameWindow();
        this.propertiesWindow = new PropertiesWindow(pickingTexture);
        this.init();
    }

    public void update(Scene currentScene) {
        this.imGuiGlfw.newFrame();
        ImGui.newFrame();
        this.setupDockspace();
        currentScene.imgui();
        ImGui.showDemoWindow();
        gameWindow.imgui();
        propertiesWindow.update(currentScene);
        propertiesWindow.imgui();
        ImGui.end();
        ImGui.render();
        endImGuiFrame();
    }

    public void init() {
        this.imGuiGlfw.init(Window.getHandle(), true);
        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename("imgui.ini");
        io.setConfigFlags(DockingEnable);
        io.setBackendPlatformName("imgui_java_impl_glfw");
        initFont("Ruda-Bold.ttf", 18);

        styleHelper = new ImGuiStyleHelper(ImGui.getStyle());
        gameWindow = new GameWindow();
        this.styleHelper.teakStyle();

        this.imGuiGl3.init(glslVersion);
    }

    public void initFont(String fontName, int pixelSize) {
        final ImGuiIO io = ImGui.getIO();
        final ImFontAtlas atlas = io.getFonts();
        final ImFontConfig config = new ImFontConfig();
        config.setGlyphRanges(atlas.getGlyphRangesDefault());
        config.setPixelSnapH(true);

        atlas.addFontFromFileTTF("assets/fonts/" + fontName, pixelSize, config);
        atlas.setFlags(ImGuiFreeTypeBuilderFlags.LightHinting);
        atlas.build();
        config.destroy();
    }

    public void terminate() {
        this.imGuiGl3.dispose();
        this.imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    public void endImGuiFrame() {
        /*
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            org.lwjgl.glfw.GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
        */
        this.imGuiGl3.renderDrawData(ImGui.getDrawData());
        ImGui.endFrame();
    }

    private void setupDockspace() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoDecoration;
        float offsetX = 0.0f;
        float offsetY = 0.0f;

        ImGui.setNextWindowPos(offsetX, offsetY, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth() - offsetX, Window.getHeight() - offsetY);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);

        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;


        ImGui.begin("Dockspace", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        // Dockspace
        ImGui.dockSpace(ImGui.getID("Dockspace"));
    }

    public static int getStaticWindowFlags() {

        return ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
        ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
        ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus |
        ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse;
    }
}