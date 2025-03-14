package dLib.ui.elements.items;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class VerticalCollapsableBox extends UIElement {
    //region Variables

    public Toggle toggleArrow;
    public TextBox titleBox;
    public UIElement contentBox;

    //endregion

    //region Constructors

    public VerticalCollapsableBox(String title) {
        this(title, Dim.fill(), Dim.auto());
    }
    public VerticalCollapsableBox(String title, AbstractDimension width, AbstractDimension height) {
        this(title, Pos.px(0), Pos.px(0), width, height);
    }
    public VerticalCollapsableBox(String title, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        VerticalBox mainBox = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto());
        {
            HorizontalBox titleHBox = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(50));
            {
                titleHBox.addChild(toggleArrow = new Toggle(Tex.stat(UICommonResources.arrow_right), Tex.stat(UICommonResources.arrow_down), Pos.px(0), Pos.px(0), Dim.px(50), Dim.px(50)));
                toggleArrow.onLeftClickEvent.subscribeManaged(() -> {
                    if(toggleArrow.isToggled()) {
                        contentBox.showAndEnable();
                    } else {
                        contentBox.hideAndDisable();
                    }
                    VerticalCollapsableBox.this.onDimensionsChanged();
                });
                toggleArrow.setToggled(true);

                titleHBox.addChild(titleBox = new TextBox(title, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()));
                titleBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
            }
            Color renderColor = Color.BLACK.cpy();
            renderColor.a = 0.6f;
            titleHBox.setTexture(Tex.stat(UICommonResources.white_pixel));
            titleHBox.setRenderColor(renderColor);
            mainBox.addChild(titleHBox);

            contentBox = new UIElement(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto());
            mainBox.addChild(contentBox);
        }
        addChild(mainBox);
    }

    //endregion

    //region Methods

    public void addItem(UIElement child){
        contentBox.addChild(child);
    }

    //endregion
}
