package com.leppy.redux.engine;

import com.leppy.redux.framework.GameWindow;
import com.leppy.redux.scenes.Scene;
import com.leppy.redux.core.Window;
import imgui.*;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import org.joml.Vector3f;

import static com.leppy.redux.util.Constants.glslVersion;
import static imgui.flag.ImGuiConfigFlags.DockingEnable;
import static imgui.flag.ImGuiCol.*;

public class ImGuiLayer {
    private boolean showText = false;
    private ImFont font;
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    public static final Vector3f color_for_text = new Vector3f(236.f / 255.f, 240.f / 255.f, 241.f / 255.f);
    public static final Vector3f color_for_head = new Vector3f(41.f / 255.f, 128.f / 255.f, 185.f / 255.f);
    public static final Vector3f color_for_area = new Vector3f(57.f / 255.f, 79.f / 255.f, 105.f / 255.f);
    public static final Vector3f color_for_body = new Vector3f(44.f / 255.f, 62.f / 255.f, 80.f / 255.f);
    public static final Vector3f color_for_pops = new Vector3f(33.f / 255.f, 46.f / 255.f, 60.f / 255.f);

    public ImGuiLayer() {
        this.init();
    }

    public void update(Scene currentScene) {
        this.imGuiGlfw.newFrame();
        ImGui.newFrame();
        this.setupDockspace();
        currentScene.sceneImgui();
        ImGui.showDemoWindow();
        GameWindow.imgui();
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

        // this.easyColorTheme(color_for_text, color_for_head, color_for_area, color_for_body, color_for_pops);
        this.sonicTheme();

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

    public void easyColorTheme(Vector3f color_for_text, Vector3f color_for_head, Vector3f color_for_area, Vector3f color_for_body, Vector3f color_for_pops) {
        ImGuiStyle style = ImGui.getStyle();

        style.setColor(Text, color_for_text.x, color_for_text.y, color_for_text.z, 1.00f );
        style.setColor(TextDisabled, color_for_text.x, color_for_text.y, color_for_text.z, 0.58f );
        style.setColor(WindowBg, color_for_body.x, color_for_body.y, color_for_body.z, 0.95f );
//        style.setColor(ChildWindowBg, color_for_area.x, color_for_area.y, color_for_area.z, 0.58f );
        style.setColor(Border, color_for_body.x, color_for_body.y, color_for_body.z, 0.00f );
        style.setColor(BorderShadow, color_for_body.x, color_for_body.y, color_for_body.z, 0.00f );
        style.setColor(FrameBg, color_for_area.x, color_for_area.y, color_for_area.z, 1.00f );
        style.setColor(FrameBgHovered, color_for_head.x, color_for_head.y, color_for_head.z, 0.78f );
        style.setColor(FrameBgActive, color_for_head.x, color_for_head.y, color_for_head.z, 1.00f );
        style.setColor(TitleBg, color_for_area.x, color_for_area.y, color_for_area.z, 1.00f );
        style.setColor(TitleBgCollapsed, color_for_area.x, color_for_area.y, color_for_area.z, 0.75f );
        style.setColor(TitleBgActive, color_for_head.x, color_for_head.y, color_for_head.z, 1.00f );
        style.setColor(MenuBarBg, color_for_area.x, color_for_area.y, color_for_area.z, 0.47f );
        style.setColor(ScrollbarBg, color_for_area.x, color_for_area.y, color_for_area.z, 1.00f );
        style.setColor(ScrollbarGrab, color_for_head.x, color_for_head.y, color_for_head.z, 0.21f );
        style.setColor(ScrollbarGrabHovered, color_for_head.x, color_for_head.y, color_for_head.z, 0.78f );
        style.setColor(ScrollbarGrabActive, color_for_head.x, color_for_head.y, color_for_head.z, 1.00f );
//        style.setColor(ComboBg, color_for_area.x, color_for_area.y, color_for_area.z, 1.00f );
        style.setColor(CheckMark, color_for_head.x, color_for_head.y, color_for_head.z, 0.80f );
        style.setColor(SliderGrab, color_for_head.x, color_for_head.y, color_for_head.z, 0.50f );
        style.setColor(SliderGrabActive, color_for_head.x, color_for_head.y, color_for_head.z, 1.00f );
        style.setColor(Button, color_for_head.x, color_for_head.y, color_for_head.z, 0.50f );
        style.setColor(ButtonHovered, color_for_head.x, color_for_head.y, color_for_head.z, 0.86f );
        style.setColor(ButtonActive, color_for_head.x, color_for_head.y, color_for_head.z, 1.00f );
        style.setColor(Header, color_for_head.x, color_for_head.y, color_for_head.z, 0.76f );
        style.setColor(HeaderHovered, color_for_head.x, color_for_head.y, color_for_head.z, 0.86f );
        style.setColor(HeaderActive, color_for_head.x, color_for_head.y, color_for_head.z, 1.00f );
//        style.setColor(Column, color_for_head.x, color_for_head.y, color_for_head.z, 0.32f );
//        style.setColor(ColumnHovered, color_for_head.x, color_for_head.y, color_for_head.z, 0.78f );
//        style.setColor(ColumnActive, color_for_head.x, color_for_head.y, color_for_head.z, 1.00f );
        style.setColor(ResizeGrip, color_for_head.x, color_for_head.y, color_for_head.z, 0.15f );
        style.setColor(ResizeGripHovered, color_for_head.x, color_for_head.y, color_for_head.z, 0.78f );
        style.setColor(ResizeGripActive, color_for_head.x, color_for_head.y, color_for_head.z, 1.00f );
//        style.setColor(CloseButton, color_for_text.x, color_for_text.y, color_for_text.z, 0.16f );
//        style.setColor(CloseButtonHovered, color_for_text.x, color_for_text.y, color_for_text.z, 0.39f );
//        style.setColor(CloseButtonActive, color_for_text.x, color_for_text.y, color_for_text.z, 1.00f );
        style.setColor(PlotLines, color_for_text.x, color_for_text.y, color_for_text.z, 0.63f );
        style.setColor(PlotLinesHovered, color_for_head.x, color_for_head.y, color_for_head.z, 1.00f );
        style.setColor(PlotHistogram, color_for_text.x, color_for_text.y, color_for_text.z, 0.63f );
        style.setColor(PlotHistogramHovered, color_for_head.x, color_for_head.y, color_for_head.z, 1.00f );
        style.setColor(TextSelectedBg, color_for_head.x, color_for_head.y, color_for_head.z, 0.43f );
        style.setColor(PopupBg, color_for_pops.x, color_for_pops.y, color_for_pops.z, 0.92f );
//        style.setColor(ModalWindowDarkening, color_for_area.x, color_for_area.y, color_for_area.z, 0.73f );
    }
    
    public void sonicTheme() {
        ImGuiStyle style = ImGui.getStyle();

        style.setFrameRounding(4.0f);
        style.setWindowBorderSize(0.0f);
        style.setPopupBorderSize(0.0f);
        style.setGrabRounding(4.0f);

        style.setColor(Text, 1.00f, 1.00f, 1.00f, 1.00f);
        style.setColor(TextDisabled, 0.73f, 0.75f, 0.74f, 1.00f);
        style.setColor(WindowBg, 0.09f, 0.09f, 0.09f, 0.94f);
        style.setColor(ChildBg, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(PopupBg, 0.08f, 0.08f, 0.08f, 0.94f);
        style.setColor(Border, 0.20f, 0.20f, 0.20f, 0.50f);
        style.setColor(BorderShadow, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(FrameBg, 0.71f, 0.39f, 0.39f, 0.54f);
        style.setColor(FrameBgHovered, 0.84f, 0.66f, 0.66f, 0.40f);
        style.setColor(FrameBgActive, 0.84f, 0.66f, 0.66f, 0.67f);
        style.setColor(TitleBg, 0.47f, 0.22f, 0.22f, 0.67f);
        style.setColor(TitleBgActive, 0.47f, 0.22f, 0.22f, 1.00f);
        style.setColor(TitleBgCollapsed, 0.47f, 0.22f, 0.22f, 0.67f);
        style.setColor(MenuBarBg, 0.34f, 0.16f, 0.16f, 1.00f);
        style.setColor(ScrollbarBg, 0.02f, 0.02f, 0.02f, 0.53f);
        style.setColor(ScrollbarGrab, 0.31f, 0.31f, 0.31f, 1.00f);
        style.setColor(ScrollbarGrabHovered, 0.41f, 0.41f, 0.41f, 1.00f);
        style.setColor(ScrollbarGrabActive, 0.51f, 0.51f, 0.51f, 1.00f);
        style.setColor(CheckMark, 1.00f, 1.00f, 1.00f, 1.00f);
        style.setColor(SliderGrab, 0.71f, 0.39f, 0.39f, 1.00f);
        style.setColor(SliderGrabActive, 0.84f, 0.66f, 0.66f, 1.00f);
        style.setColor(Button, 0.47f, 0.22f, 0.22f, 0.65f);
        style.setColor(ButtonHovered, 0.71f, 0.39f, 0.39f, 0.65f);
        style.setColor(ButtonActive, 0.20f, 0.20f, 0.20f, 0.50f);
        style.setColor(Header, 0.71f, 0.39f, 0.39f, 0.54f);
        style.setColor(HeaderHovered, 0.84f, 0.66f, 0.66f, 0.65f);
        style.setColor(HeaderActive, 0.84f, 0.66f, 0.66f, 0.00f);
        style.setColor(Separator, 0.43f, 0.43f, 0.50f, 0.50f);
        style.setColor(SeparatorHovered, 0.71f, 0.39f, 0.39f, 0.54f);
        style.setColor(SeparatorActive, 0.71f, 0.39f, 0.39f, 0.54f);
        style.setColor(ResizeGrip, 0.71f, 0.39f, 0.39f, 0.54f);
        style.setColor(ResizeGripHovered, 0.84f, 0.66f, 0.66f, 0.66f);
        style.setColor(ResizeGripActive, 0.84f, 0.66f, 0.66f, 0.66f);
        style.setColor(Tab, 0.71f, 0.39f, 0.39f, 0.54f);
        style.setColor(TabHovered, 0.84f, 0.66f, 0.66f, 0.66f);
        style.setColor(TabActive, 0.84f, 0.66f, 0.66f, 0.66f);
        style.setColor(TabUnfocused, 0.07f, 0.10f, 0.15f, 0.97f);
        // style.setColor(TabUnfocusedActive, 0.14f, 0.26f, 0.42f, 1.00f);
        style.setColor(PlotLines, 0.61f, 0.61f, 0.61f, 1.00f);
        style.setColor(PlotLinesHovered, 1.00f, 0.43f, 0.35f, 1.00f);
        style.setColor(PlotHistogram, 0.90f, 0.70f, 0.00f, 1.00f);
        style.setColor(PlotHistogramHovered, 1.00f, 0.60f, 0.00f, 1.00f);
        style.setColor(TextSelectedBg, 0.26f, 0.59f, 0.98f, 0.35f);
        style.setColor(DragDropTarget, 1.00f, 1.00f, 0.00f, 0.90f);
        style.setColor(NavHighlight, 0.41f, 0.41f, 0.41f, 1.00f);
        style.setColor(NavWindowingHighlight, 1.00f, 1.00f, 1.00f, 0.70f);
        style.setColor(NavWindowingDimBg, 0.80f, 0.80f, 0.80f, 0.20f);
        style.setColor(ModalWindowDimBg, 0.80f, 0.80f, 0.80f, 0.35f);
        style.setColor(TabUnfocusedActive, 0.0f, 0.0f, 0.0f, 0.0f);
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
        int windowFlags =
                ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus |
                ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse;

        return windowFlags;
    }
}