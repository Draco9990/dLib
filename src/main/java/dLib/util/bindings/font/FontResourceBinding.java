package dLib.util.bindings.font;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.properties.objects.FontBindingProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.util.Reflection;
import dLib.util.bindings.font.editors.FontResourceBindingValueEditor;
import dLib.util.bindings.font.fontresource.IFontSource;

import java.io.Serializable;

public class FontResourceBinding extends AbstractFontBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Font";
    private static final String PROPERTY_EDITOR_SHORT_NAME = "font";

    //region Variables

    private final Class<?> resourceClass;
    private final String fieldName;

    //endregion Variables

    public FontResourceBinding(Class<?> resourceClass, String fieldName){
        if(resourceClass.isAssignableFrom(IFontSource.class) && resourceClass != FontHelper.class){
            throw new IllegalArgumentException("The resource class must implement IFontResource or be FontHelper.class");
        }

        this.resourceClass = resourceClass;
        this.fieldName = fieldName;
    }

    //region Methods


    //endregion Methods

    @Override
    public BitmapFont getBoundObject(Object... params) {
        Object fontResult = Reflection.getFieldValue(fieldName, resourceClass);
        if(fontResult instanceof BitmapFont){
            return (BitmapFont) fontResult;
        }
        else{
            return null;
        }
    }

    @Override
    public String getDisplayValue() {
        return resourceClass.getSimpleName() + "/" + fieldName;
    }

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new FontResourceBindingValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new FontResourceBindingValueEditor((FontBindingProperty) property);
    }
}
