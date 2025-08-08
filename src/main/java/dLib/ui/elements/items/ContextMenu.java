package dLib.ui.elements.items;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.buttons.Toggle;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalDataBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.elements.items.text.TextButton;
import dLib.ui.resources.UICommonResources;
import dLib.ui.util.ESelectionMode;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.string.AbstractStringBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.localevents.RunnableEvent;
import dLib.util.events.serializableevents.SerializableRunnable;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.AbstractPosition;

import java.util.ArrayList;

public class ContextMenu extends HorizontalBox {
    public VerticalDataBox<ContextMenu.IContextMenuOption> optionsBox;

    public ContextMenu(AbstractPosition xPos, AbstractPosition yPos) {
        super(xPos, yPos, Dim.auto(), Dim.auto());

        setTexture(Tex.stat(UICommonResources.button02_square));
        setRenderColor(Color.WHITE);

        setVerticalAlignment(Alignment.VerticalAlignment.TOP);

        setContentPadding(Padd.px(12));
        setHorizontalItemSpacing(3);

        setContextual(true);
        setDrawFocusOnOpen(true);

        optionsBox = new VerticalDataBox<ContextMenu.IContextMenuOption>(Dim.auto(), Dim.auto()){
            @Override
            public UIElement makeUIForItem(IContextMenuOption item) {
                return item.get(ContextMenu.this);
            }
        };
        optionsBox.setTexture(UICommonResources.transparent_pixel);
        optionsBox.setSelectionMode(ESelectionMode.NONE);
        optionsBox.disableToggleOverlay();
        addChild(optionsBox);
    }

    private void resetAt(UIElement option){
        int depthIndex = -1;
        for (int i = 0; i < children.size(); i++) {
            UIElement child = children.get(i);
            if (option.isDescendantOf(child)) {
                depthIndex = i;
                break;
            }
        }

        while(children.size() > depthIndex + 1) {
            children.get(children.size() - 1).dispose();
        }
    }

    public interface IContextMenuOption {
        UIElement get(ContextMenu parent);
    }

    public static class ContextMenuButtonOption implements IContextMenuOption {
        public AbstractStringBinding optionText;
        public RunnableEvent onOptionSelectedEvent = new RunnableEvent();

        public ContextMenuButtonOption(AbstractStringBinding text, SerializableRunnable onOptionSelectedEvent) {
            this.optionText = text;
            this.onOptionSelectedEvent.subscribeManaged(onOptionSelectedEvent);
        }

        @Override
        public UIElement get(ContextMenu cMenu) {
            TextButton button = new TextButton(optionText.resolve(), Dim.auto(), Dim.px(30));

            button.label.setFont(Font.stat(FontHelper.buttonLabelFont));
            button.setTexture(Tex.stat(UICommonResources.button03_square));
            button.label.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);

            button.postLeftClickEvent.subscribe(this, () -> {
                onOptionSelectedEvent.invoke();
                button.getTopParent().dispose(); // Close the context menu after selection
            });

            return button;
        }
    }

    public static class ContextMenuDropdownOption implements IContextMenuOption {
        public AbstractStringBinding optionText;
        public ArrayList<IContextMenuOption> suboptions;

        public ContextMenuDropdownOption(AbstractStringBinding text, ArrayList<IContextMenuOption> suboptions){
            this.optionText = text;
            this.suboptions = suboptions;
        }

        @Override
        public UIElement get(ContextMenu cMenu) {
            Toggle button = new Toggle(Tex.stat(UICommonResources.button03_square), Dim.auto(), Dim.px(30));
            {
                HorizontalBox hBox = new HorizontalBox(Dim.auto(), Dim.fill());
                hBox.setHorizontalItemSpacing(10);
                {
                    TextBox text = new TextBox(optionText.resolve(), Dim.auto(), Dim.fill());
                    text.setFontSize(14);
                    text.setPadding(Padd.px(10));
                    text.setFont(Font.stat(FontHelper.buttonLabelFont));
                    text.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
                    hBox.addChild(text);

                    HorizontalBox inner = new HorizontalBox(Dim.auto(), Dim.fill());
                    inner.setHorizontalAlignment(Alignment.HorizontalAlignment.RIGHT);
                    {
                        Image arrow = new Image(Tex.stat(UICommonResources.arrow_right), Dim.mirror(), Dim.fill());
                        inner.addChild(arrow);
                    }
                    hBox.addChild(inner);
                }
                button.addChild(hBox);
            }

            button.postLeftClickEvent.subscribe(button, () -> {
                cMenu.resetAt(button);

                VerticalDataBox<IContextMenuOption> nOptionBox = new VerticalDataBox<IContextMenuOption>(Dim.auto(), Dim.auto()){
                    @Override
                    public UIElement makeUIForItem(IContextMenuOption item) {
                        return item.get(cMenu);
                    }
                };
                nOptionBox.setTexture(UICommonResources.transparent_pixel);
                nOptionBox.setSelectionMode(ESelectionMode.NONE);
                nOptionBox.disableToggleOverlay();
                cMenu.addChild(nOptionBox);

                nOptionBox.setChildren(suboptions);
            });

            return button;
        }
    }
}
