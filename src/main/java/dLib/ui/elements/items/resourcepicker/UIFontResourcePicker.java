package dLib.ui.elements.items.resourcepicker;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.text.TextBox;
import dLib.util.Reflection;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.font.FontResourceBinding;
import dLib.util.bindings.font.fontresource.IFontSource;
import dLib.util.ui.dimensions.Dim;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class UIFontResourcePicker extends UIResourcePicker{
    @Override
    public ResourcePickerWindow.ResourcePickerWindowResource createResourcePickerWindowResource(Class<?> clazz, Field field) {
        return new FontResourcePickerWindowResource(clazz, field);
    }

    @Override
    public ArrayList<Class<?>> getResourceTypes() {
        ArrayList<Class<?>> resourceTypes = new ArrayList<>();
        resourceTypes.add(BitmapFont.class);
        return resourceTypes;
    }

    @Override
    public ArrayList<Class<?>> getClassesToIndex() {
        ArrayList<Class<?>> classesToIndex =  Reflection.findClassesOfType(IFontSource.class, false);
        classesToIndex.add(0, FontHelper.class);
        return classesToIndex;
    }

    public static class FontResourcePickerWindowResource extends ResourcePickerWindow.ResourcePickerWindowResource {
        public FontResourcePickerWindowResource(Class<?> clazz, Field field) {
            super(clazz, field);
        }

        @Override
        public UIElement makeResourcePickerWindowResourcePreview(Class<?> clazz, Field field) {
            TextBox textBox = new TextBox("aA", Dim.fill(), Dim.fill());
            textBox.setFont(Font.resource(clazz, field.getName()));

            return textBox;
        }
    }
}
