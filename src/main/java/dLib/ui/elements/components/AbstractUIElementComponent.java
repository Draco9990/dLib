package dLib.ui.elements.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import dLib.ui.elements.UIElement;

public abstract class AbstractUIElementComponent<RequiredElementType extends UIElement> implements Disposable {
    public void onRegisterComponent(RequiredElementType owner){}
    public void onUnregisterComponent(RequiredElementType owner){}

    public void onUpdate(RequiredElementType owner){}
    public void onRender(RequiredElementType owner, SpriteBatch sb){}

    @Override
    public void dispose() {
    }
}
