package dLib.ui.elements.items.resourcepicker;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.VerticalCollapsableBox;
import dLib.ui.elements.items.itembox.GridBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.itembox.VerticalListBox;
import dLib.util.Reflection;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractUIListResourcePicker extends AbstractUIResourcePicker{
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

                VerticalListBox<ListResourcePickerItem> fieldBox = new VerticalListBox<>(Dim.fill(), Dim.auto());
                fieldBox.setItemSpacing(10);
                {
                    for(Field field : entry.getValue()){
                        fieldBox.addItem(new ListResourcePickerItem(entry.getKey(), field));
                    }
                }
                fieldBox.onItemSelectionChangedEvent.subscribe(this, selectedItems -> {
                    if(selectedItems.isEmpty()){
                        return;
                    }

                    ListResourcePickerItem item = selectedItems.get(0);
                    onResourceSelectedEvent.invoke(classStringBiConsumer -> classStringBiConsumer.accept(item.clazz, item.field.getName()));
                });

                classBox.addItem(fieldBox);
                mainBox.addItem(classBox);
            }
        }

        return mainBox;
    }

    private static class ListResourcePickerItem {
        public Class<?> clazz;
        public Field field;

        public ListResourcePickerItem(Class<?> clazz, Field field) {
            this.clazz = clazz;
            this.field = field;
        }

        @Override
        public String toString() {
            return clazz.getSimpleName() + "." + field.getName();
        }
    }
}
