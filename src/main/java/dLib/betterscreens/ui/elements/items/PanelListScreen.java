package dLib.betterscreens.ui.elements.items;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.buttons.CancelButton;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.string.AbstractStringBinding;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;

public class PanelListScreen extends UIElement {
    public HorizontalBox panelBox;

    public PanelListScreen(){
        super();

        CancelButton cancelButton = new CancelButton();
        cancelButton.postLeftClickEvent.subscribe(cancelButton, this::dispose);
        addChild(cancelButton);

        Scrollbox pScrollbar = new Scrollbox(Dim.fill(), Dim.fill());
        pScrollbar.setIsVertical(false);
        {
            panelBox = new HorizontalBox(Dim.fill(), Dim.fill());
            panelBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.CENTER);
            pScrollbar.addChild(panelBox);
        }
        addChild(pScrollbar);
    }

    public static class Panel extends Button {
        public Panel(AbstractStringBinding titleBinding, AbstractTextureBinding textureBinding, AbstractStringBinding description){
            this(titleBinding, textureBinding, description, null);
        }

        public Panel(AbstractStringBinding titleBinding, AbstractTextureBinding textureBinding, AbstractStringBinding description, Runnable onClick){
            super(Dim.fill(), Dim.fill());

            TextBox header = new TextBox(titleBinding.getBoundObject(), Dim.fill(), Dim.auto());
            header.setFontSize(24f);
            header.setWrap(true);
            addChild(header);

            Image image = new Image(textureBinding, Dim.fill(), Dim.fill());
            addChild(image);

            TextBox descriptionBox = new TextBox(description.getBoundObject(), Dim.fill(), Dim.auto());
            descriptionBox.setWrap(true);
            addChild(descriptionBox);

            if(onClick != null){
                postLeftClickEvent.subscribe(this, onClick::run);
            }
        }
    }
}
