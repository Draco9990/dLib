package dLib.tools.screeneditor.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dLib.tools.screeneditor.screens.ScreenEditorBaseScreen;
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
        if(!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
            activeItems.clear();
        }

        if(!activeItems.contains(item)) activeItems.add(item);
    }
    public void clearActiveItems(){
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
        if(Gdx.input.isKeyJustPressed(Input.Keys.DEL)){
            for(UIPreviewItem item : activeItems){
                ScreenEditorBaseScreen.instance.getPreviewScreen().deletePreviewItem(item);
            }
            activeItems.clear();
        }
    }
}
