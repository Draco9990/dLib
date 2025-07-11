package dLib.ui.elements.items;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.itembox.VerticalDataBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.events.localevents.RunnableEvent;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ContextMenu extends VerticalDataBox<ContextMenu.IContextMenuOption> {
    public ContextMenu(AbstractPosition xPos, AbstractPosition yPos) {
        super(xPos, yPos, Dim.px(300), Dim.auto());

        setTexture(Tex.stat(UICommonResources.button02_square));
        setRenderColor(Color.WHITE);

        setContentPadding(Padd.px(10));

        setContextual(true);
        setDrawFocusOnOpen(true);
    }

    @Override
    public UIElement makeUIForItem(IContextMenuOption item) {
        return item.get();
    }

    public static interface IContextMenuOption extends Supplier<UIElement> {

    }

    public static class ContextMenuButtonOption implements IContextMenuOption {
        public String optionText;
        public RunnableEvent onOptionSelectedEvent = new RunnableEvent();

        public ContextMenuButtonOption(String text, Runnable onOptionSelectedEvent) {
            this.optionText = text;
            this.onOptionSelectedEvent.subscribeManaged(onOptionSelectedEvent);
        }

        @Override
        public UIElement get() {
            TextButton button = new TextButton(optionText, Dim.fill(), Dim.px(30));

            button.label.setFont(Font.stat(FontHelper.buttonLabelFont));
            button.setTexture(Tex.stat(UICommonResources.button03_square));
            button.label.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);

            onOptionSelectedEvent.subscribe(this, () -> button.getParentOfType(ContextMenu.class).close());
            button.onLeftClickEvent.subscribe(this, () -> onOptionSelectedEvent.invoke());

            return button;
        }
    }
}
