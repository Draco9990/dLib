package dLib.tools.screeneditorold.screensold;

import com.badlogic.gdx.graphics.Color;
import dLib.tools.screeneditorold.ui.items.implementations.menu.ScreenEditorMenu;
import dLib.tools.screeneditorold.ui.items.implementations.preview.ScreenEditorPreview;
import dLib.tools.screeneditorold.ui.items.implementations.toolbar.ScreenEditorElementList;
import dLib.tools.screeneditorold.ui.items.implementations.toolbar.ScreenEditorElementProperties;
import dLib.tools.screeneditorold.ui.items.implementations.toolbar.ScreenEditorToolbox;
import dLib.tools.screeneditorold.util.ScreenEditorActiveItemsManager;
import dLib.tools.screeneditorold.util.ScreenEditorProperties;
import dLib.tools.screeneditorold.util.ScreenEditorSaveManager;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Renderable;
import dLib.ui.resources.UICommonResources;
import dLib.ui.screens.GeneratedAbstractScreen;

import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class ScreenEditorBaseScreen extends UIElement {
    //region Variables

    private String editingScreen;

    private ScreenEditorMenu menu;

    private ScreenEditorPreview preview;

    private ScreenEditorToolbox toolbar;
    private ScreenEditorElementProperties properties;
    private ScreenEditorElementList elementListScreen;

    private ScreenEditorSaveManager saveManager;
    private ScreenEditorActiveItemsManager activeItemsManager;
    private ScreenEditorProperties editorProperties;

    //endregion

    //region Constructors

    public ScreenEditorBaseScreen(String editingClass){
        super(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        initialize(editingClass);
    }
    public ScreenEditorBaseScreen(GeneratedAbstractScreen.GeneratedScreenData initialData){
        super(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        initialize(initialData.screenClass);

        /*for(ScreenEditorItem item : initialData.getEditorItems()){
            preview.addPreviewItem(item);
        }*/

        activeItemsManager.clearActiveItems();
    }

    private void initialize(String editingClass){
        this.editingScreen = editingClass;

        addChildNCS(new Renderable(Tex.stat(UICommonResources.white_pixel), Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()).setRenderColor(Color.valueOf("#151515FF")));

        menu = new ScreenEditorMenu();
        addChildNCS(menu);

        preview = new ScreenEditorPreview();
        addChildNCS(preview);

        toolbar = new ScreenEditorToolbox();
        addChildNCS(toolbar);
        properties = new ScreenEditorElementProperties();
        addChildNCS(properties);
        elementListScreen = new ScreenEditorElementList();
        addChildNCS(elementListScreen);

        saveManager = new ScreenEditorSaveManager(this);
        activeItemsManager = new ScreenEditorActiveItemsManager(this);
        editorProperties = new ScreenEditorProperties();
    }


    //endregion

    //region Methods
    //endregion

    /** Variables */

    /** Constructors */

    /** Update and Render */
    @Override
    public void updateSelf() {
        super.updateSelf();
        activeItemsManager.update();
    }

    /** Editing Screen cache */
    public ScreenEditorBaseScreen setEditingScreen(String screen){
        editingScreen = screen;
        return this;
    }

    public String getEditingScreen(){
        return editingScreen;
    }

    /** Managers */
    public ScreenEditorSaveManager getSaveManager(){
        return saveManager;
    }
    public ScreenEditorActiveItemsManager getActiveItemsManager(){
        return activeItemsManager;
    }
    public ScreenEditorProperties getEditorProperties() { return editorProperties; }

    /** Preview */
    public ScreenEditorPreview getPreviewScreen(){
        return preview;
    }

    /** Toolbar */
    public ScreenEditorToolbox getToolbarScreen(){
        return toolbar;
    }
    public ScreenEditorElementProperties getPropertiesScreen() { return properties; }
    public ScreenEditorElementList getElementListScreen(){
        return elementListScreen;
    }

    /** Management */
    public void hideAllToolbarItems(){
        toolbar.hideAndDisable();
        properties.hideAndDisable();
        elementListScreen.hideAndDisable();
    }
}
