package dLib.util.bindings.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.properties.objects.TextureBindingProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.util.Reflection;
import dLib.util.bindings.texture.editors.TextureResourceBindingValueEditor;
import dLib.util.bindings.texture.textureresource.ITextureSource;

import java.io.Serializable;

public class TextureResourceBinding extends AbstractTextureBinding implements Serializable {
    private static final long serialVersionUID = 1L;

    //For property editors
    private static final String PROPERTY_EDITOR_LONG_NAME = "Resource Texture";
    private static final String PROPERTY_EDITOR_SHORT_NAME = "resource";

    //region Variables

    private final Class<?> resourceClass;
    private final String fieldName;

    //endregion Variables

    public TextureResourceBinding(Class<?> resourceClass, String fieldName){
        if(resourceClass.isAssignableFrom(ITextureSource.class) && resourceClass != ImageMaster.class){
            throw new IllegalArgumentException("The resource class must implement ITextureResource or be ImageMaster.class");
        }

        this.resourceClass = resourceClass;
        this.fieldName = fieldName;
    }

    //region Methods


    //endregion Methods

    @Override
    public NinePatch getBoundObject(Object... params) {
        Object textureResult = Reflection.getFieldValue(fieldName, resourceClass);
        if(textureResult instanceof TextureRegion){
            return new NinePatch((TextureRegion) textureResult);
        }
        else if(textureResult instanceof Texture){
            return new NinePatch((Texture) textureResult);
        }
        else if(textureResult instanceof NinePatch){
            return (NinePatch) textureResult;
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
        return new TextureResourceBindingValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new TextureResourceBindingValueEditor((TextureBindingProperty) property);
    }
}
