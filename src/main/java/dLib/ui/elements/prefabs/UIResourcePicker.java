package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.resources.UICommonResources;
import dLib.util.Reflection;
import dLib.util.bindings.texture.Tex;
import dLib.util.bindings.texture.textureresource.ITextureResource;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class UIResourcePicker extends UIElement {
    private BiConsumer<Class<?>, String> onResourceSelected;

    public UIResourcePicker(BiConsumer<Class<?>, String> onResourceSelected) {
        super(Dim.fill(), Dim.fill());

        this.onResourceSelected = onResourceSelected;
        setModal(true);
        setDrawFocusOnOpen(true);

        addChildNCS(new ResourcePickerWindow());
    }

    private static class ResourcePickerWindow extends Renderable {
        public ResourcePickerWindow() {
            super(Tex.stat(UICommonResources.background_big), Dim.fill(), Dim.fill());

            TextButton cancelButton = new TextButton("Cancel", Pos.px(126), Pos.px(1080-930), Dim.px(161), Dim.px(74));
            cancelButton.onLeftClickEvent.subscribe(this, () -> {
                UIResourcePicker parent = getParentOfType(UIResourcePicker.class);
                parent.close();
            });
            cancelButton.getButton().setImage(Tex.stat(UICommonResources.cancelButtonSmall));
            addChildCS(cancelButton);

            TextButton confirmButton = new TextButton("Confirm", Pos.px(1626), Pos.px(1080-930), Dim.px(173), Dim.px(74));
            confirmButton.onLeftClickEvent.subscribe(this, () -> {
                UIResourcePicker parent = getParentOfType(UIResourcePicker.class);
                parent.onResourceSelected.accept(null, null);
                parent.close();
            });
            confirmButton.getButton().setImage(Tex.stat(UICommonResources.confirmButtonSmall));
            addChildCS(confirmButton);

            Scrollbox scrollbox = new Scrollbox(Pos.px(336), Pos.px(1080-922), Dim.px(1242), Dim.px(814));
            scrollbox.setIsHorizontal(false);
            {
                VerticalBox mainBox = new VerticalBox(Dim.fill(), Dim.fill());
                {
                    HashMap<Class<?>, ArrayList<Field>> resources = new HashMap<>();
                    for(Field texture : Reflection.getFieldsByClass(Texture.class, ImageMaster.class)){
                        if(Modifier.isStatic(texture.getModifiers())){
                            if(!resources.containsKey(texture.getDeclaringClass())){
                                resources.put(texture.getDeclaringClass(), new ArrayList<>());
                            }
                            resources.get(texture.getDeclaringClass()).add(texture);
                        }
                    }
                    for(Field texture : Reflection.getFieldsByClass(TextureRegion.class, ImageMaster.class)){
                        if(Modifier.isStatic(texture.getModifiers())){
                            if(!resources.containsKey(texture.getDeclaringClass())){
                                resources.put(texture.getDeclaringClass(), new ArrayList<>());
                            }
                            resources.get(texture.getDeclaringClass()).add(texture);
                        }
                    }

                    for(Class<? extends ITextureResource> clazz : Reflection.findClassesOfType(ITextureResource.class, false)){
                        for(Field field : Reflection.getFieldsByClass(Texture.class, clazz)){
                            if(Modifier.isStatic(field.getModifiers())){
                                if(!resources.containsKey(clazz)){
                                    resources.put(clazz, new ArrayList<>());
                                }
                                resources.get(clazz).add(field);
                            }
                        }
                        for(Field field : Reflection.getFieldsByClass(TextureRegion.class, clazz)){
                            if(Modifier.isStatic(field.getModifiers())){
                                if(!resources.containsKey(clazz)){
                                    resources.put(clazz, new ArrayList<>());
                                }
                                resources.get(clazz).add(field);
                            }
                        }
                    }

                    for(Map.Entry<Class<?>, ArrayList<Field>> entry : resources.entrySet()){
                        VerticalCollapsableBox classBox = new VerticalCollapsableBox(entry.getKey().getSimpleName());

                        GridItemBox<ResourcePickerWindowResource> fieldBox = new GridItemBox<ResourcePickerWindowResource>(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto()){
                            @Override
                            public UIElement makeUIForItem(ResourcePickerWindowResource item) {
                                return item;
                            }
                        };
                        {
                            for(Field field : entry.getValue()){
                                fieldBox.addItem(new ResourcePickerWindowResource(entry.getKey(), field));
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

        private static class ResourcePickerWindowResource extends UIElement{
            public ResourcePickerWindowResource(Class<?> clazz, Field field) {
                super(Dim.px(150), Dim.px(225));

                VerticalBox contentBox = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(150));
                {
                    Image image = new Image(Tex.resource(clazz, field.getName()), Dim.fill(), Dim.fill());
                    contentBox.addItem(image);

                    TextBox box = new TextBox(field.getName(), Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(75));
                    box.setWrap(true);
                    contentBox.addItem(box);
                }
                addChildNCS(contentBox);
            }
        }
    }
}
