package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class Scrollbox extends UIElement {
    //region Variables

    private boolean isVertical = true;
    private boolean isHorizontal = true;

    private boolean alwaysShow = true;

    private UIElement mainBox;
    private VerticalScrollbar verticalScroll;
    private HorizontalScrollbar horizontalScroll;

    private int verticalScrollbarWidth = 49;
    private int horizontalScrollbarHeight = 49;

    private boolean redirectChildren = false;

    //endregion

    //region Constructors

    public Scrollbox() {
        this(Dim.fill(), Dim.fill());
    }
    public Scrollbox(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public Scrollbox(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public Scrollbox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(xPos, yPos, width, height);

        mainBox = new UIElement(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        verticalScroll = new VerticalScrollbar(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        verticalScroll.setBoundElement(mainBox);
        horizontalScroll = new HorizontalScrollbar(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        horizontalScroll.setBoundElement(mainBox);

        reinitChildLayout();
    }

    //endregion

    //region Methods

    @Override
    public UIElement addChild(UIElementChild child) {
        if(redirectChildren){
            return mainBox.addChild(child);
        }
        return super.addChild(child);
    }

    private void reinitChildLayout(){
        clearChildren();

        redirectChildren = false;

        if(alwaysShow){
            HorizontalBox horizontalBox = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill(), true);
            {
                VerticalBox firstColumn = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill(), true);
                {
                    firstColumn.addItem(mainBox);

                    if(isHorizontal){
                        UIElement horizontalScrollParent = new UIElement(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(horizontalScrollbarHeight));
                        {
                            horizontalScrollParent.addChildNCS(horizontalScroll);
                        }
                        firstColumn.addItem(horizontalScrollParent);
                    }
                }
                horizontalBox.addItem(firstColumn);

                if(isVertical){
                    VerticalBox secondColumn = new VerticalBox(Pos.px(0), Pos.px(0), Dim.px(verticalScrollbarWidth), Dim.fill(), true);
                    {
                        secondColumn.addItem(verticalScroll);
                        if(isHorizontal){
                            secondColumn.addItem(new Spacer(Dim.fill(), Dim.px(horizontalScrollbarHeight)));
                        }
                    }
                    horizontalBox.addItem(secondColumn);
                }
            }
            addChildNCS(horizontalBox);
        }
        else{
            //TODO;
        }

        redirectChildren = true;
    }

    //region Scrollbars

    public void setIsVertical(boolean isVertical) {
        this.isVertical = isVertical;
        reinitChildLayout();
    }
    public void setIsHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
        reinitChildLayout();
    }

    public boolean isVertical() {
        return isVertical;
    }
    public boolean isHorizontal() {
        return isHorizontal;
    }


    public void setVerticalScrollbarWidth(int verticalScrollbarWidth) {
        this.verticalScrollbarWidth = verticalScrollbarWidth;
        reinitChildLayout();
    }

    public void setHorizontalScrollbarHeight(int horizontalScrollbarHeight) {
        this.horizontalScrollbarHeight = horizontalScrollbarHeight;
        reinitChildLayout();
    }

    public int getVerticalScrollbarWidth() {
        return verticalScrollbarWidth;
    }

    public int getHorizontalScrollbarHeight() {
        return horizontalScrollbarHeight;
    }

    //endregion

    //region Always Show

    public void setAlwaysShow(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        reinitChildLayout();
    }

    public boolean isAlwaysShow() {
        return alwaysShow;
    }

    //endregion

    //endregion
}
