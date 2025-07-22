package dLib.betterscreens.ui.elements.items;

import com.megacrit.cardcrawl.cards.AbstractCard;
import dLib.betterscreens.ui.elements.items.gameplayitems.CardSlot;
import dLib.ui.Alignment;
import dLib.ui.animations.entry.UIAnimation_SlideInUp;
import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.buttons.CancelButton;
import dLib.ui.elements.items.buttons.ConfirmButton;
import dLib.ui.elements.items.itembox.VerticalDataBox;
import dLib.ui.elements.items.popup.GenericPopupHolder;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.resources.UICommonResources;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;

public class CardSelectPopup extends GenericPopupHolder {
    public VerticalDataBox<AbstractCard> cardBox; // Im lazy

    public ConsumerEvent<ArrayList<AbstractCard>> onCardsSelectedEvent = new ConsumerEvent<>();

    public CardSelectPopup(){
        super();

        Scrollbox cardSelectBox = new Scrollbox(Pos.px(277), Pos.px(80), Dim.px(1331), Dim.px(920));
        cardSelectBox.setIsHorizontal(false);
        {
            cardBox = new VerticalDataBox<AbstractCard>(Dim.fill(), Dim.fill()){
                @Override
                public UIElement makeUIForItem(AbstractCard item) {
                    CardSlot s = new CardSlot(Pos.px(0), Pos.px(0), 0.75f);
                    s.setCard(item);
                    return s;
                }
            };
            cardBox.setTexture(UICommonResources.transparent_pixel);
            cardBox.setGridMode(true);
            cardBox.setPadding(Padd.px(25));
            cardBox.setItemSpacing(10);
            cardBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.CENTER);
            cardSelectBox.addChild(cardBox);
        }
        cardSelectBox.setEntryAnimation(new UIAnimation_SlideInUp(cardSelectBox));
        cardSelectBox.setExitAnimation(new UIAnimation_SlideOutDown(cardSelectBox));
        addChild(cardSelectBox);

        CancelButton cancelButton = new CancelButton();
        cancelButton.postLeftClickEvent.subscribe(cancelButton, this::dispose);
        addChild(cancelButton);

        ConfirmButton confirmButton = new ConfirmButton();
        confirmButton.postLeftClickEvent.subscribe(confirmButton, () -> {
            onCardsSelectedEvent.invoke(cardBox.getSelectedItems());
            dispose();
        });
        addChild(confirmButton);
    }
}
