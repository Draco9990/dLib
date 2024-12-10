package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Toggle;
import dLib.ui.themes.UIThemeManager;
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
        this(title, Dim.fill(), Dim.fill());
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
                titleHBox.addItem(toggleArrow = new Toggle(UIThemeManager.getDefaultTheme().arrow_right, UIThemeManager.getDefaultTheme().arrow_down, Pos.px(0), Pos.px(0), Dim.px(50), Dim.px(50)));
                toggleArrow.addOnLeftClickEvent(() -> {
                    if(toggleArrow.isToggled()) {
                        contentBox.showAndEnable();
                    } else {
                        contentBox.hideAndDisable();
                    }
                });
                toggleArrow.setToggled(true);

                titleHBox.addItem(titleBox = new TextBox(title, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()));
                titleBox.setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
            }
            Color renderColor = Color.BLACK.cpy();
            renderColor.a = 0.6f;
            titleHBox.setRenderColor(renderColor);
            mainBox.addItem(titleHBox);

            contentBox = new UIElement(Pos.px(0), Pos.px(0), Dim.fill(), Dim.auto());
            mainBox.addItem(contentBox);
        }
        addChildNCS(mainBox);
    }

    //endregion

    //region Methods

    public void addItem(UIElement child){
        contentBox.addChildNCS(child);
    }

    //endregion
}
