package dLib.ui.elements.items;

import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;

public class ContextMenu extends Renderable{
    public VerticalBox optionsBox;

    public ContextMenu(AbstractPosition xPos, AbstractPosition yPos) {
        super(Tex.stat(UICommonResources.button02_square), xPos, yPos, Dim.px(300), Dim.auto());

        UIElement paddedBox = new UIElement(Dim.fill(), Dim.auto());
        paddedBox.setPaddingHorizontal(Padd.px(20)); //TODO this is a mess
        paddedBox.setPaddingTop(Padd.px(-10));
        paddedBox.setPaddingBottom(Padd.px(10));
        {
            optionsBox = new VerticalBox(Dim.fill(), Dim.auto());
            paddedBox.addChildNCS(optionsBox);
        }
        addChildNCS(paddedBox);

        setContextual(true);
        setDrawFocusOnOpen(true);
    }

    public void addOption(ContextMenuOption option){
        optionsBox.addItem((UIElement) option);
    }

    private static interface ContextMenuOption{

    }

    public static class ContextMenuButtonOption extends TextButton implements ContextMenuOption {
        public Event<Runnable> onOptionSelectedEvent = new Event<>();

        public ContextMenuButtonOption(String text) {
            super(text, Dim.fill(), Dim.px(30));

            label.setFont(Font.stat(FontHelper.buttonLabelFont));
            setImage(Tex.stat(UICommonResources.itembox_itembg_horizontal));
            label.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);

            onOptionSelectedEvent.subscribe(this, () -> getParentOfType(ContextMenu.class).close());
            onLeftClickEvent.subscribe(this, () -> onOptionSelectedEvent.invoke(Runnable::run));
        }
    }
}
