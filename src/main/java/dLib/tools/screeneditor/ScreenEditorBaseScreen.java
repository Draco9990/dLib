package dLib.tools.screeneditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.DLib;
import dLib.tools.screeneditor.ui.items.preview.UIPreviewItem;
import dLib.tools.screeneditor.util.ScreenEditorActiveItemsManager;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.screens.AbstractScreen;
import dLib.ui.themes.UITheme;

public class ScreenEditorBaseScreen extends AbstractScreen {
    /** Singleton */
    public static ScreenEditorBaseScreen instance;

    /** Variables */
    private ScreenEditorMenuScreen menu;

    private ScreenEditorPreviewScreen preview;

    private ScreenEditorToolbarScreen toolbar;

    private ScreenEditorActiveItemsManager activeItemsManager;

    /** Constructors */
    public ScreenEditorBaseScreen(){
        addElementToBackground(new Renderable(UITheme.whitePixel, 0, 0, 1920, 1080).setRenderColor(Color.valueOf("#151515FF")));

        menu = new ScreenEditorMenuScreen();

        preview = new ScreenEditorPreviewScreen();

        toolbar = new ScreenEditorToolbarScreen(){
            @Override
            public void onElementToAddChosen(UIPreviewItem previewItem) {
                super.onElementToAddChosen(previewItem);
                preview.makeNewPreviewItem(previewItem);
            }
        };

        activeItemsManager = new ScreenEditorActiveItemsManager();

        instance = this;
    }

    /** Update and Render */
    @Override
    public void update() {
        super.update();

        menu.update();

        preview.update();

        toolbar.update();

        activeItemsManager.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        preview.render(sb);

        menu.render(sb);

        toolbar.render(sb);
    }

    /** Active Item Manager */
    public ScreenEditorActiveItemsManager getActiveItemsManager(){
        return activeItemsManager;
    }

    /** Preview */
    public ScreenEditorPreviewScreen getPreviewScreen(){
        return preview;
    }

    @Override
    public String getModId() {
        return DLib.getModID();
    }
}
