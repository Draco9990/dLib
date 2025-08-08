package dLib.util.bindings.font;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;

//! Warning, not serializable
public class FontStaticBinding extends AbstractFontBinding {

    private BitmapFont font;

    public FontStaticBinding(BitmapFont font){
        this.font = font;
    }

    @Override
    public BitmapFont resolve(Object... params) {
        return font;
    }

    @Override
    public String toString() {
        return "STATIC";
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        throw new UnsupportedOperationException("Not implemented - Font Static Binding should never be edited in-app");
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        throw new UnsupportedOperationException("Not implemented - Font Static Binding should never be edited in-app");
    }
}
