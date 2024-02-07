package dLib.tools.screeneditor.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
import dLib.tools.screeneditor.screens.toolbar.ScreenEditorPropertiesScreen;
import dLib.tools.screeneditor.ui.items.preview.UIPreviewItem;
import dLib.util.Reflection;

import java.util.ArrayList;

public class ScreenEditorActiveItemsManager {
    /** Variables */
    private ArrayList<UIPreviewItem> activeItems = new ArrayList<>();

    /** Constructors */
    public ScreenEditorActiveItemsManager(){

    }

    /** Active items management */
    public void addActiveItem(UIPreviewItem item){
        ScreenEditorBaseScreen.instance.getPropertiesScreen().clearScreen();
        if(!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
            clearActiveItems();
        }

        if(!activeItems.contains(item)) {
            activeItems.add(item);

            if(activeItems.size() == 1){
                ScreenEditorBaseScreen.instance.getPropertiesScreen().createPropertiesFor(item);
            }
        }
    }
    public void clearActiveItems(){
        ScreenEditorBaseScreen.instance.getPropertiesScreen().clearScreen();
        activeItems.clear();
    }

    public boolean isItemActive(UIPreviewItem item){
        return activeItems.contains(item);
    }

    /** Group Drag functionality */
    public void markAllForDrag(){
        for(UIPreviewItem item : activeItems){
            item.setProxyDragged(true);
            Reflection.invokeMethod("onLeftClick", item);
            item.setProxyDragged(false);
        }
    }

    public void markAllForDragUpdate(float totalDuration){
        for(UIPreviewItem item : activeItems){
            item.setProxyDragged(true);
            Reflection.invokeMethod("onLeftClickHeld", item, totalDuration);
            item.setProxyDragged(false);
        }
    }

    public void update(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.FORWARD_DEL)){
            for(UIPreviewItem item : activeItems){
                ScreenEditorBaseScreen.instance.getPreviewScreen().deletePreviewItem(item);
            }
            clearActiveItems();
        }
    }
}
