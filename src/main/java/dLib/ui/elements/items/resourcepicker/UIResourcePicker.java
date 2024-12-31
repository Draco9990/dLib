package dLib.ui.elements.items.resourcepicker;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Button;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.VerticalCollapsableBox;
import dLib.ui.elements.items.itembox.GridBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.Reflection;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class UIResourcePicker extends UIElement {
    public Event<BiConsumer<Class<?>, String>> onResourceSelectedEvent = new Event<>();

    public UIResourcePicker() {
        super(Dim.fill(), Dim.fill());

        setModal(true);
        setDrawFocusOnOpen(true);

        addChildNCS(new ResourcePickerWindow(this));
    }

    public abstract ResourcePickerWindow.ResourcePickerWindowResource createResourcePickerWindowResource(Class<?> clazz, Field field);

    public abstract ArrayList<Class<?>> getResourceTypes();
    public abstract ArrayList<Class<?>> getClassesToIndex();

    public static class ResourcePickerWindow extends Renderable {
        public ResourcePickerWindow(UIResourcePicker resourcePicker) {
            super(Tex.stat(UICommonResources.background_big), Dim.fill(), Dim.fill());

            TextButton cancelButton = new TextButton("Cancel", Pos.px(126), Pos.px(1080-930), Dim.px(161), Dim.px(74));
            cancelButton.onLeftClickEvent.subscribe(this, () -> {
                UIResourcePicker parent = getParentOfType(UIResourcePicker.class);
                parent.close();
            });
            cancelButton.setImage(Tex.stat(UICommonResources.cancelButtonSmall));
            addChildCS(cancelButton);

            Scrollbox scrollbox = new Scrollbox(Pos.px(336), Pos.px(1080-922), Dim.px(1242), Dim.px(814));
            scrollbox.setIsHorizontal(false);
            {
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
                                fieldBox.addItem(resourcePicker.createResourcePickerWindowResource(entry.getKey(), field));
                            }
                        }

                        classBox.addItem(fieldBox);
                        mainBox.addItem(classBox);
                    }
                }
                scrollbox.addChildNCS(mainBox);
            }
            addChildCS(scrollbox);
        }

        @Override
        public boolean onCancelInteraction() {
            UIResourcePicker parent = getParentOfType(UIResourcePicker.class);
            parent.close();
            return true;
        }

        public abstract static class ResourcePickerWindowResource extends UIElement{
            public ResourcePickerWindowResource(Class<?> clazz, Field field) {
                super(Dim.px(150), Dim.px(225));

                Button button = new Button(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
                button.setImage(Tex.stat(UICommonResources.white_pixel));
                Color darkTransparent = new Color(0, 0, 0, 0.4f);
                button.setRenderColor(darkTransparent);
                button.onLeftClickEvent.subscribe(this, () -> {
                    UIResourcePicker parent = getParentOfType(UIResourcePicker.class);
                    parent.onResourceSelectedEvent.invoke(classStringBiConsumer -> classStringBiConsumer.accept(clazz, field.getName()));
                    parent.close();
                });
                addChildNCS(button);

                VerticalBox contentBox = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
                {
                    UIElement preview = makeResourcePickerWindowResourcePreview(clazz, field);
                    preview.setPassthrough(true);
                    contentBox.addItem(preview);

                    TextBox box = new TextBox(field.getName(), Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(75));
                    box.setWrap(true);
                    contentBox.addItem(box);
                }
                addChildNCS(contentBox);
            }

            public abstract UIElement makeResourcePickerWindowResourcePreview(Class<?> clazz, Field field);
        }
    }
}
