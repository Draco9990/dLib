package dLib.tools.screeneditor.ui.items.preview;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import dLib.tools.screeneditor.ScreenEditorBaseScreen;
import dLib.ui.elements.implementations.Draggable;
import dLib.ui.elements.implementations.Resizeable;

public abstract class UIPreviewItem extends Resizeable {
    /** Variables */
    private boolean proxyDragged;

    /** Constructors */
    public UIPreviewItem(Texture image) {
        super(image);
    }

    public UIPreviewItem(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
    }

    public UIPreviewItem(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    /** Update and render */
    @Override
    public void render(SpriteBatch sb) {
        if(ScreenEditorBaseScreen.instance.getActiveItemsManager().isItemActive(this) && !Settings.isDebug){
            Settings.isDebug = true;
            super.render(sb);
            Settings.isDebug = false;
        }
        else{
            super.render(sb);
        }
    }

    /** Proxy Drag */
    public void setProxyDragged(boolean isProxyDragged){
        proxyDragged = isProxyDragged;
    }

    /** Selection */
    @Override
    protected void onLeftClick() {
        super.onLeftClick();
        if(!proxyDragged){
            ScreenEditorBaseScreen.instance.getActiveItemsManager().addActiveItem(this);
            ScreenEditorBaseScreen.instance.getActiveItemsManager().markAllForDrag();
        }
    }

    @Override
    protected void onLeftClickHeld(float totalDuration) {
        super.onLeftClickHeld(totalDuration);

        if(!proxyDragged) ScreenEditorBaseScreen.instance.getActiveItemsManager().markAllForDragUpdate(totalDuration);
    }

    /** Copy */
    public abstract UIPreviewItem makeCopy();
}
