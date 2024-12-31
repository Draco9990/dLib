package dLib.util.bindings.property;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.properties.objects.Property;
import dLib.properties.objects.TextureBindingProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.elements.UIElement;
import dLib.util.Reflection;
import dLib.util.bindings.property.editors.PropertyElementPathBindingValueEditor;
import dLib.util.bindings.texture.editors.TextureResourceBindingValueEditor;
import dLib.util.bindings.texture.textureresource.ITextureSource;

import java.io.Serializable;

public class PropertyElementPathBinding extends AbstractPropertyBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Property Path";
    private static final String PROPERTY_EDITOR_SHORT_NAME = "path";

    //region Variables

    private final String elementPath;
    private final String propertyName;

    //endregion Variables

    public PropertyElementPathBinding(String elementPath, String propertyName){
        this.elementPath = elementPath;
        this.propertyName = propertyName;
    }

    @Override
    public TProperty getBoundObject(Object... params) {
        UIElement owner = (UIElement) params[0];
        return Reflection.getFieldValue(propertyName, owner.findChildFromPath(elementPath));
    }

    @Override
    public String getDisplayValue() {
        return elementPath + "/" + propertyName;
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new PropertyElementPathBindingValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new PropertyElementPathBindingValueEditor((Property<AbstractPropertyBinding>) property);
    }
}
