package dLib.ui.elements.items.resourcepicker;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.VerticalCollapsableBox;
import dLib.ui.elements.items.itembox.GridBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.Reflection;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractUIPreviewResourcePicker extends AbstractUIResourcePicker {

    @Override
    public UIElement buildContent(AbstractUIResourcePicker resourcePicker) {
        VerticalBox mainBox = new VerticalBox(Dim.fill(), Dim.fill());
        {
            LinkedHashMap<Class<?>, ArrayList<Field>> resources = new LinkedHashMap<>();

            for(Class<?> resourceSourceClass : resourcePicker.getClassesToIndex()){
                for(Class<?> resourceClass : resourcePicker.getResourceTypes()){
                    for(Field field : Reflection.getFieldsByClass(resourceClass, resourceSourceClass)){
                        if(Modifier.isStatic(field.getModifiers())){
                            if(!resources.containsKey(resourceSourceClass)){
                                resources.put(resourceSourceClass, new ArrayList<>());
                            }
                            resources.get(resourceSourceClass).add(field);
                        }
                    }
                }
            }

            for(Map.Entry<Class<?>, ArrayList<Field>> entry : resources.entrySet()){
                VerticalCollapsableBox classBox = new VerticalCollapsableBox(entry.getKey().getSimpleName());
                classBox.setPaddingLeft(Padd.px(50));

                GridBox fieldBox = new GridBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto());
                fieldBox.setItemSpacing(10);
                {
                    for(Field field : entry.getValue()){
                        fieldBox.addItem(createResourcePickerWindowResource(entry.getKey(), field));
                    }
                }

                classBox.addItem(fieldBox);
                mainBox.addItem(classBox);
            }
        }

        return mainBox;
    }

    public abstract ResourcePickerWindowResource createResourcePickerWindowResource(Class<?> clazz, Field field);

    public abstract static class ResourcePickerWindowResource extends UIElement{
        public ResourcePickerWindowResource(Class<?> clazz, Field field) {
            super(Dim.px(150), Dim.px(225));

            Button button = new Button(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            button.setImage(Tex.stat(UICommonResources.white_pixel));
            Color darkTransparent = new Color(0, 0, 0, 0.4f);
            button.setRenderColor(darkTransparent);
            button.onLeftClickEvent.subscribe(this, () -> {
                AbstractUIResourcePicker parent = getParentOfType(AbstractUIResourcePicker.class);
                parent.onResourceSelectedEvent.invoke(classStringBiConsumer -> classStringBiConsumer.accept(clazz, field.getName()));
                parent.close();
            });
            addChild(button);

            VerticalBox contentBox = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            {
                UIElement preview = makeResourcePickerWindowResourcePreview(clazz, field);
                preview.setPassthrough(true);
                contentBox.addItem(preview);

                TextBox box = new TextBox(field.getName(), Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(75));
                box.setWrap(true);
                contentBox.addItem(box);
            }
            addChild(contentBox);
        }

        public abstract UIElement makeResourcePickerWindowResourcePreview(Class<?> clazz, Field field);
    }
}
