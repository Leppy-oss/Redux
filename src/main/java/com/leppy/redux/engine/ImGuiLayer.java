package com.leppy.redux.engine;

import com.leppy.redux.framework.*;
import com.leppy.redux.framework.render.PickingTexture;
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
    private GameWindow gameWindow;
    private long glfwHandle;

    public static final Vector3f color_for_text = new Vector3f(236.f / 255.f, 240.f / 255.f, 241.f / 255.f);
    public static final Vector3f color_for_head = new Vector3f(41.f / 255.f, 128.f / 255.f, 185.f / 255.f);
    public static final Vector3f color_for_area = new Vector3f(57.f / 255.f, 79.f / 255.f, 105.f / 255.f);
    public static final Vector3f color_for_body = new Vector3f(44.f / 255.f, 62.f / 255.f, 80.f / 255.f);
    public static final Vector3f color_for_pops = new Vector3f(33.f / 255.f, 46.f / 255.f, 60.f / 255.f);

    private GameWindow gameViewWindow;
    private PropertiesWindow propertiesWindow;

    public ImGuiLayer(long glfwHandle, PickingTexture pickingTexture) {
        this.glfwHandle = glfwHandle;
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

        gameWindow = new GameWindow();
        // this.easyColorTheme(color_for_text, color_for_head, color_for_area, color_for_body, color_for_pops);
        // this.sonicTheme();
        this.teakStyle();

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
    
    public void teakStyle() {
        ImGuiStyle style = ImGui.getStyle();

        style.setWindowPadding(15, 15);
        style.setWindowRounding(5.0f);
        style.setFramePadding(5, 5);
        style.setFrameRounding(4.0f);
        style.setItemSpacing(12, 8);
        style.setItemInnerSpacing(8, 6);
        style.setIndentSpacing(25.0f);
        style.setScrollbarSize(15.0f);
        style.setScrollbarRounding(9.0f);
        style.setGrabMinSize(5.0f);
        style.setGrabRounding(3.0f);

        style.setColor(Text, 0.80f, 0.80f, 0.83f, 1.00f);
        style.setColor(TextDisabled, 0.24f, 0.23f, 0.29f, 1.00f);
        style.setColor(WindowBg, 0.06f, 0.05f, 0.07f, 0.5f);
//        style.setColor(ChildWindowBg, 0.07f, 0.07f, 0.09f, 1.00f);
        style.setColor(PopupBg, 0.07f, 0.07f, 0.09f, 1.00f);
        style.setColor(Border, 0.80f, 0.80f, 0.83f, 0.88f);
        style.setColor(BorderShadow, 0.92f, 0.91f, 0.88f, 0.00f);
        style.setColor(FrameBg, 0.10f, 0.09f, 0.12f, 1.00f);
        style.setColor(FrameBgHovered, 0.24f, 0.23f, 0.29f, 1.00f);
        style.setColor(FrameBgActive, 0.56f, 0.56f, 0.58f, 1.00f);
        style.setColor(TitleBg, 0.10f, 0.09f, 0.12f, 1.00f);
        style.setColor(TitleBgCollapsed, 1.00f, 0.98f, 0.95f, 0.75f);
        style.setColor(TitleBgActive, 0.07f, 0.07f, 0.09f, 1.00f);
        style.setColor(MenuBarBg, 0.10f, 0.09f, 0.12f, 1.00f);
        style.setColor(ScrollbarBg, 0.10f, 0.09f, 0.12f, 1.00f);
        style.setColor(ScrollbarGrab, 0.80f, 0.80f, 0.83f, 0.31f);
        style.setColor(ScrollbarGrabHovered, 0.56f, 0.56f, 0.58f, 1.00f);
        style.setColor(ScrollbarGrabActive, 0.06f, 0.05f, 0.07f, 1.00f);
//        style.setColor(ComboBg, 0.19f, 0.18f, 0.21f, 1.00f);
        style.setColor(CheckMark, 0.80f, 0.80f, 0.83f, 0.31f);
        style.setColor(SliderGrab, 0.80f, 0.80f, 0.83f, 0.31f);
        style.setColor(SliderGrabActive, 0.06f, 0.05f, 0.07f, 1.00f);
        style.setColor(Button, 0.10f, 0.09f, 0.12f, 1.00f);
        style.setColor(ButtonHovered, 0.24f, 0.23f, 0.29f, 1.00f);
        style.setColor(ButtonActive, 0.56f, 0.56f, 0.58f, 1.00f);
        style.setColor(Header, 0.10f, 0.09f, 0.12f, 1.00f);
        style.setColor(HeaderHovered, 0.56f, 0.56f, 0.58f, 1.00f);
        style.setColor(HeaderActive, 0.06f, 0.05f, 0.07f, 1.00f);
//        style.setColor(Column, 0.56f, 0.56f, 0.58f, 1.00f);
//        style.setColor(ColumnHovered, 0.24f, 0.23f, 0.29f, 1.00f);
//        style.setColor(ColumnActive, 0.56f, 0.56f, 0.58f, 1.00f);
        style.setColor(ResizeGrip, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(ResizeGripHovered, 0.56f, 0.56f, 0.58f, 1.00f);
        style.setColor(ResizeGripActive, 0.06f, 0.05f, 0.07f, 1.00f);
//        style.setColor(CloseButton, 0.40f, 0.39f, 0.38f, 0.16f);
//        style.setColor(CloseButtonHovered, 0.40f, 0.39f, 0.38f, 0.39f);
//        style.setColor(CloseButtonActive, 0.40f, 0.39f, 0.38f, 1.00f);
        style.setColor(PlotLines, 0.40f, 0.39f, 0.38f, 0.63f);
        style.setColor(PlotLinesHovered, 0.25f, 1.00f, 0.00f, 1.00f);
        style.setColor(PlotHistogram, 0.40f, 0.39f, 0.38f, 0.63f);
        style.setColor(PlotHistogramHovered, 0.25f, 1.00f, 0.00f, 1.00f);
        style.setColor(TextSelectedBg, 0.25f, 1.00f, 0.00f, 0.43f);
        style.setColor(Tab, 13.0f / 255.0f, 18.0f / 255.0f, 0.1f, 0.43f);
        style.setColor(TabActive, 13.0f / 255.0f, 18.0f / 255.0f, 0.1f, 0.43f);
        style.setColor(TabUnfocusedActive, 13.0f / 255.0f, 18.0f / 255.0f, 0.1f, 0.43f);
        style.setColor(SeparatorActive, 13.0f / 255.0f, 18.0f / 255.0f, 0.1f, 0.43f);
        style.setColor(SeparatorHovered, 18.0f / 255.0f, 23.0f / 255.0f, 0.1f, 0.43f);
        style.setColor(TabHovered, 18.0f / 255.0f, 23.0f / 255.0f, 0.1f, 0.43f);
        // style.setColor(Tab, 18.0f, 23.0f, 0255.0f, 0.43f);
//        style.setColor(ModalWindowDarkening, 1.00f, 0.98f, 0.95f, 0.73f);
    }

    public void pacomeStyle() {
        ImGuiStyle style = ImGui.getStyle();

        style.setAlpha(1.0f);
        style.setFrameRounding(3.0f);
        style.setColor(Text, 0.00f, 0.00f, 0.00f, 1.00f);
        style.setColor(TextDisabled, 0.60f, 0.60f, 0.60f, 1.00f);
        style.setColor(WindowBg, 0.94f, 0.94f, 0.94f, 0.94f);
//        style.setColor(ChildWindowBg, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(PopupBg, 1.00f, 1.00f, 1.00f, 0.94f);
        style.setColor(Border, 0.00f, 0.00f, 0.00f, 0.39f);
        style.setColor(BorderShadow, 1.00f, 1.00f, 1.00f, 0.10f);
        style.setColor(FrameBg, 1.00f, 1.00f, 1.00f, 0.94f);
        style.setColor(FrameBgHovered, 0.26f, 0.59f, 0.98f, 0.40f);
        style.setColor(FrameBgActive, 0.26f, 0.59f, 0.98f, 0.67f);
        style.setColor(TitleBg, 0.96f, 0.96f, 0.96f, 1.00f);
        style.setColor(TitleBgCollapsed, 1.00f, 1.00f, 1.00f, 0.51f);
        style.setColor(TitleBgActive, 0.82f, 0.82f, 0.82f, 1.00f);
        style.setColor(MenuBarBg, 0.86f, 0.86f, 0.86f, 1.00f);
        style.setColor(ScrollbarBg, 0.98f, 0.98f, 0.98f, 0.53f);
        style.setColor(ScrollbarGrab, 0.69f, 0.69f, 0.69f, 1.00f);
        style.setColor(ScrollbarGrabHovered, 0.59f, 0.59f, 0.59f, 1.00f);
        style.setColor(ScrollbarGrabActive, 0.49f, 0.49f, 0.49f, 1.00f);
//        style.setColor(ComboBg, 0.86f, 0.86f, 0.86f, 0.99f);
        style.setColor(CheckMark, 0.26f, 0.59f, 0.98f, 1.00f);
        style.setColor(SliderGrab, 0.24f, 0.52f, 0.88f, 1.00f);
        style.setColor(SliderGrabActive, 0.26f, 0.59f, 0.98f, 1.00f);
        style.setColor(Button, 0.26f, 0.59f, 0.98f, 0.40f);
        style.setColor(ButtonHovered, 0.26f, 0.59f, 0.98f, 1.00f);
        style.setColor(ButtonActive, 0.06f, 0.53f, 0.98f, 1.00f);
        style.setColor(Header, 0.26f, 0.59f, 0.98f, 0.31f);
        style.setColor(HeaderHovered, 0.26f, 0.59f, 0.98f, 0.80f);
        style.setColor(HeaderActive, 0.26f, 0.59f, 0.98f, 1.00f);
//        style.setColor(Column, 0.39f, 0.39f, 0.39f, 1.00f);
//        style.setColor(ColumnHovered, 0.26f, 0.59f, 0.98f, 0.78f);
//        style.setColor(ColumnActive, 0.26f, 0.59f, 0.98f, 1.00f);
        style.setColor(ResizeGrip, 1.00f, 1.00f, 1.00f, 0.50f);
        style.setColor(ResizeGripHovered, 0.26f, 0.59f, 0.98f, 0.67f);
        style.setColor(ResizeGripActive, 0.26f, 0.59f, 0.98f, 0.95f);
//        style.setColor(CloseButton, 0.59f, 0.59f, 0.59f, 0.50f);
//        style.setColor(CloseButtonHovered, 0.98f, 0.39f, 0.36f, 1.00f);
//        style.setColor(CloseButtonActive, 0.98f, 0.39f, 0.36f, 1.00f);
        style.setColor(PlotLines, 0.39f, 0.39f, 0.39f, 1.00f);
        style.setColor(PlotLinesHovered, 1.00f, 0.43f, 0.35f, 1.00f);
        style.setColor(PlotHistogram, 0.90f, 0.70f, 0.00f, 1.00f);
        style.setColor(PlotHistogramHovered, 1.00f, 0.60f, 0.00f, 1.00f);
        style.setColor(TextSelectedBg, 0.26f, 0.59f, 0.98f, 0.35f);
//        style.setColor(ModalWindowDarkening], 0.20f, 0.20f, 0.20f, 0.35f);
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
    
    public void embraceTheDarkness() {
        ImGuiStyle style = ImGui.getStyle();
        
        style.setColor(Text, 1.00f, 1.00f, 1.00f, 1.00f);
        style.setColor(TextDisabled, 0.50f, 0.50f, 0.50f, 1.00f);
        style.setColor(WindowBg, 0.10f, 0.10f, 0.10f, 1.00f);
        style.setColor(ChildBg, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(PopupBg, 0.19f, 0.19f, 0.19f, 0.92f);
        style.setColor(Border, 0.19f, 0.19f, 0.19f, 0.29f);
        style.setColor(BorderShadow, 0.00f, 0.00f, 0.00f, 0.24f);
        style.setColor(FrameBg, 0.05f, 0.05f, 0.05f, 0.54f);
        style.setColor(FrameBgHovered, 0.19f, 0.19f, 0.19f, 0.54f);
        style.setColor(FrameBgActive, 0.20f, 0.22f, 0.23f, 1.00f);
        style.setColor(TitleBg, 0.00f, 0.00f, 0.00f, 1.00f);
        style.setColor(TitleBgActive, 0.06f, 0.06f, 0.06f, 1.00f);
        style.setColor(TitleBgCollapsed, 0.00f, 0.00f, 0.00f, 1.00f);
        style.setColor(MenuBarBg, 0.14f, 0.14f, 0.14f, 1.00f);
        style.setColor(ScrollbarBg, 0.05f, 0.05f, 0.05f, 0.54f);
        style.setColor(ScrollbarGrab, 0.34f, 0.34f, 0.34f, 0.54f);
        style.setColor(ScrollbarGrabHovered, 0.40f, 0.40f, 0.40f, 0.54f);
        style.setColor(ScrollbarGrabActive, 0.56f, 0.56f, 0.56f, 0.54f);
        style.setColor(CheckMark, 0.33f, 0.67f, 0.86f, 1.00f);
        style.setColor(SliderGrab, 0.34f, 0.34f, 0.34f, 0.54f);
        style.setColor(SliderGrabActive, 0.56f, 0.56f, 0.56f, 0.54f);
        style.setColor(Button, 0.05f, 0.05f, 0.05f, 0.54f);
        style.setColor(ButtonHovered, 0.19f, 0.19f, 0.19f, 0.54f);
        style.setColor(ButtonActive, 0.20f, 0.22f, 0.23f, 1.00f);
        style.setColor(Header, 0.00f, 0.00f, 0.00f, 0.52f);
        style.setColor(HeaderHovered, 0.00f, 0.00f, 0.00f, 0.36f);
        style.setColor(HeaderActive, 0.20f, 0.22f, 0.23f, 0.33f);
        style.setColor(Separator, 0.28f, 0.28f, 0.28f, 0.29f);
        style.setColor(SeparatorHovered, 0.44f, 0.44f, 0.44f, 0.29f);
        style.setColor(SeparatorActive, 0.40f, 0.44f, 0.47f, 1.00f);
        style.setColor(ResizeGrip, 0.28f, 0.28f, 0.28f, 0.29f);
        style.setColor(ResizeGripHovered, 0.44f, 0.44f, 0.44f, 0.29f);
        style.setColor(ResizeGripActive, 0.40f, 0.44f, 0.47f, 1.00f);
        style.setColor(Tab, 0.00f, 0.00f, 0.00f, 0.52f);
        style.setColor(TabHovered, 0.14f, 0.14f, 0.14f, 1.00f);
        style.setColor(TabActive, 0.20f, 0.20f, 0.20f, 0.36f);
        style.setColor(TabUnfocused, 0.00f, 0.00f, 0.00f, 0.52f);
        style.setColor(TabUnfocusedActive, 0.14f, 0.14f, 0.14f, 1.00f);
        style.setColor(DockingPreview, 0.33f, 0.67f, 0.86f, 1.00f);
        style.setColor(DockingEmptyBg, 1.00f, 0.00f, 0.00f, 1.00f);
        style.setColor(PlotLines, 1.00f, 0.00f, 0.00f, 1.00f);
        style.setColor(PlotLinesHovered, 1.00f, 0.00f, 0.00f, 1.00f);
        style.setColor(PlotHistogram, 1.00f, 0.00f, 0.00f, 1.00f);
        style.setColor(PlotHistogramHovered, 1.00f, 0.00f, 0.00f, 1.00f);
        style.setColor(TableHeaderBg, 0.00f, 0.00f, 0.00f, 0.52f);
        style.setColor(TableBorderStrong, 0.00f, 0.00f, 0.00f, 0.52f);
        style.setColor(TableBorderLight, 0.28f, 0.28f, 0.28f, 0.29f);
        style.setColor(TableRowBg, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(TableRowBgAlt, 1.00f, 1.00f, 1.00f, 0.06f);
        style.setColor(TextSelectedBg, 0.20f, 0.22f, 0.23f, 1.00f);
        style.setColor(DragDropTarget, 0.33f, 0.67f, 0.86f, 1.00f);
        style.setColor(NavHighlight, 1.00f, 0.00f, 0.00f, 1.00f);
        style.setColor(NavWindowingHighlight, 1.00f, 0.00f, 0.00f, 0.70f);
        style.setColor(NavWindowingDimBg, 1.00f, 0.00f, 0.00f, 0.20f);
        style.setColor(ModalWindowDimBg, 1.00f, 0.00f, 0.00f, 0.35f);
        

        style.setWindowPadding(8.00f, 8.00f);
        style.setFramePadding(5.00f, 2.00f);
        style.setCellPadding(6.00f, 6.00f);
        style.setItemSpacing(6.00f, 6.00f);
        style.setItemInnerSpacing(6.00f, 6.00f);
        style.setTouchExtraPadding(0.00f, 0.00f);
        style.setIndentSpacing(25);
        style.setScrollbarSize(15);
        style.setGrabMinSize(10);
        style.setWindowBorderSize(1);
        style.setChildBorderSize(1);
        style.setPopupBorderSize(1);
        style.setFrameBorderSize(1);
        style.setTabBorderSize(1);
        style.setWindowRounding(7);
        style.setChildRounding(4);
        style.setFrameRounding(3);
        style.setPopupRounding(4);
        style.setScrollbarRounding(9);
        style.setGrabRounding(3);
        style.setLogSliderDeadzone(4);
        style.setTabRounding(4);
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