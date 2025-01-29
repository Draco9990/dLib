package dLib.ui.elements.items.resourcepicker;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public abstract class AbstractUIResourcePicker extends UIElement {
    public Event<BiConsumer<Class<?>, String>> onResourceSelectedEvent = new Event<>();

    public AbstractUIResourcePicker() {
        super(Dim.fill(), Dim.fill());

        setModal(true);
        setDrawFocusOnOpen(true);

        addChild(new ResourcePickerWindow(this));
    }

    public abstract ArrayList<Class<?>> getResourceTypes();
    public abstract ArrayList<Class<?>> getClassesToIndex();

    public abstract UIElement buildContent(AbstractUIResourcePicker resourcePicker);

    public static class ResourcePickerWindow extends Renderable {
        public ResourcePickerWindow(AbstractUIResourcePicker resourcePicker) {
            super(Tex.stat(UICommonResources.background_big), Dim.fill(), Dim.fill());

            TextButton cancelButton = new TextButton("Cancel", Pos.px(126), Pos.px(1080-930), Dim.px(161), Dim.px(74));
            cancelButton.onLeftClickEvent.subscribe(this, () -> {
                AbstractUIResourcePicker parent = getParentOfType(AbstractUIResourcePicker.class);
                parent.close();
            });
            cancelButton.setTexture(Tex.stat(UICommonResources.cancelButtonSmall));
            addChild(cancelButton);

            Scrollbox scrollbox = new Scrollbox(Pos.px(336), Pos.px(1080-922), Dim.px(1242), Dim.px(814));
            scrollbox.setIsHorizontal(false);
            {
                UIElement content = resourcePicker.buildContent(resourcePicker);
                scrollbox.addChild(content);
            }
            addChild(scrollbox);
        }

        @Override
        public boolean onCancelInteraction() {
            AbstractUIResourcePicker parent = getParentOfType(AbstractUIResourcePicker.class);
            parent.close();
            return true;
        }
    }
}
