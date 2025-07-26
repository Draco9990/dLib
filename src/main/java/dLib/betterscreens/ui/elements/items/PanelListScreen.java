package dLib.betterscreens.ui.elements.items;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.ui.Alignment;
import dLib.ui.animations.entry.UIAnimation_SlideInUp;
import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.buttons.CancelButton;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.popup.GenericPopupHolder;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.string.AbstractStringBinding;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class PanelListScreen extends GenericPopupHolder {
    public HorizontalBox panelBox;

    public PanelListScreen(){
        super();

        CancelButton cancelButton = new CancelButton();
        cancelButton.postLeftClickEvent.subscribe(cancelButton, this::dispose);
        addChild(cancelButton);

        Scrollbox pScrollbar = new Scrollbox(Pos.px(250), Pos.px(90), Dim.px(1414), Dim.px(852));
        pScrollbar.setIsVertical(false);
        {
            panelBox = new HorizontalBox(Dim.fill(), Dim.fill());
            panelBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.CENTER);
            pScrollbar.addChild(panelBox);
        }
        pScrollbar.setEntryAnimation(new UIAnimation_SlideInUp(pScrollbar));
        pScrollbar.setExitAnimation(new UIAnimation_SlideOutDown(pScrollbar));
        addChild(pScrollbar);
    }

    public static class Panel extends Button {
        public Panel(AbstractStringBinding titleBinding, AbstractTextureBinding textureBinding, AbstractStringBinding description){
            this(titleBinding, textureBinding, description, null);
        }

        public Panel(AbstractStringBinding titleBinding, AbstractTextureBinding textureBinding, AbstractStringBinding description, Runnable onClick){
            super(Dim.px(512), Dim.px(800));

            setTexture(Tex.stat(ImageMaster.MENU_PANEL_BG_GRAY));

            TextBox header = new TextBox(titleBinding.getBoundObject(), Pos.px(95), Pos.px(654), Dim.px(321), Dim.px(83));
            header.setTextRenderColor(Color.GOLD);
            header.setFontSize(24f);
            header.setWrap(true);
            addChild(header);

            Image image = new Image(textureBinding, Pos.px(96), Pos.px(438), Dim.px(320), Dim.px(208));
            addChild(image);

            TextBox descriptionBox = new TextBox(description.getBoundObject(), Pos.px(96), Pos.px(100), Dim.px(320), Dim.px(330));
            descriptionBox.setWrap(true);
            descriptionBox.setFontSize(18f);
            addChild(descriptionBox);

            postLeftClickEvent.subscribe(this, () -> {
                getTopParent().dispose();
                if(onClick != null) onClick.run();
            });
        }
    }
}
