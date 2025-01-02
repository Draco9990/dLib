package dLib.ui.elements.items.resourcepicker;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Image;
import dLib.util.Reflection;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.textureresource.ITextureSource;
import dLib.util.ui.dimensions.Dim;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class UIGlobalTextureResourcePicker extends AbstractUIPreviewResourcePicker {
    @Override
    public ResourcePickerWindowResource createResourcePickerWindowResource(Class<?> clazz, Field field) {
        return new TextureResourcePickerWindowResource(clazz, field);
    }

    @Override
    public ArrayList<Class<?>> getResourceTypes() {
        ArrayList<Class<?>> resourceTypes = new ArrayList<>();
        resourceTypes.add(Texture.class);
        resourceTypes.add(TextureRegion.class);
        return resourceTypes;
    }

    @Override
    public ArrayList<Class<?>> getClassesToIndex() {
        ArrayList<Class<?>> classesToIndex =  Reflection.findClassesOfType(ITextureSource.class, false);
        classesToIndex.add(0, ImageMaster.class);
        return classesToIndex;
    }

    public static class TextureResourcePickerWindowResource extends ResourcePickerWindowResource {
        public TextureResourcePickerWindowResource(Class<?> clazz, Field field) {
            super(clazz, field);
        }

        @Override
        public UIElement makeResourcePickerWindowResourcePreview(Class<?> clazz, Field field) {
            Image image = new Image(Tex.resource(clazz, field.getName()), Dim.fill(), Dim.fill());
            image.setPreserveAspectRatio(true);
            image.setNoUpsize(true);

            return image;
        }
    }
}
