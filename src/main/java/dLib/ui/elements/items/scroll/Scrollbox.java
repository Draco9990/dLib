package dLib.ui.elements.items.scroll;

import dLib.properties.objects.BooleanProperty;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.Spacer;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

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

    public Scrollbox(ScrollboxData data) {
        super(data);

        isVertical = data.isVertical.getValue();
        isHorizontal = data.isHorizontal.getValue();

        mainBox = findChildById("contentBox");

        verticalScroll = findChildById("verticalScrollbar");
        verticalScroll.setBoundElement(mainBox);
        horizontalScroll = findChildById("horizontalScrollbar");
        horizontalScroll.setBoundElement(mainBox);

        reinitChildLayout();
    }

    //endregion

    //region Methods

    @Override
    public void addChild(UIElement child) {
        if(redirectChildren){
            mainBox.addChild(child);
            return;
        }
        super.addChild(child);
    }

    private void reinitChildLayout(){
        if(mainBox != null && mainBox.hasParent()) mainBox.getParent().removeChild(mainBox);
        if(horizontalScroll != null && horizontalScroll.hasParent()) horizontalScroll.getParent().removeChild(horizontalScroll);
        if(verticalScroll != null && verticalScroll.hasParent()) verticalScroll.getParent().removeChild(verticalScroll);

        disposeChildren();

        redirectChildren = false;

        if(alwaysShow){
            HorizontalBox horizontalBox = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            {
                VerticalBox firstColumn = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
                {
                    firstColumn.addChild(mainBox);

                    if(isHorizontal){
                        UIElement horizontalScrollParent = new UIElement(Pos.px(0), Pos.px(0), Dim.fill(), Dim.px(horizontalScrollbarHeight));
                        {
                            horizontalScrollParent.addChild(horizontalScroll);
                        }
                        firstColumn.addChild(horizontalScrollParent);
                    }
                }
                horizontalBox.addChild(firstColumn);

                if(isVertical){
                    VerticalBox secondColumn = new VerticalBox(Pos.px(0), Pos.px(0), Dim.px(verticalScrollbarWidth), Dim.fill());
                    {
                        secondColumn.addChild(verticalScroll);
                        if(isHorizontal){
                            secondColumn.addChild(new Spacer(Dim.fill(), Dim.px(horizontalScrollbarHeight)));
                        }
                    }
                    horizontalBox.addChild(secondColumn);
                }
            }
            addChild(horizontalBox);
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

    public static class ScrollboxData extends UIElementData implements Serializable {
        public static float serialVersionUID = 1L;

        public UIElementData contentBox = new UIElementData();
        public VerticalScrollbar.VerticalScrollbarData verticalScrollbar = new VerticalScrollbar.VerticalScrollbarData();
        public HorizontalScrollbar.HorizontalScrollbarData horizontalScrollbar = new HorizontalScrollbar.HorizontalScrollbarData();

        public BooleanProperty isVertical = new BooleanProperty(true)
                .setName("Vertical")
                .setDescription("Whether the scrollbox should scroll vertically")
                .setCategory("Scrollbox");

        public BooleanProperty isHorizontal = new BooleanProperty(true)
                .setName("Horizontal")
                .setDescription("Whether the scrollbox should scroll horizontally")
                .setCategory("Scrollbox");

        public ScrollboxData(){
            width.setValue(Dim.px(300));
            height.setValue(Dim.px(300));

            contentBox.id.setValue("contentBox");
            children.add(contentBox);

            //TODO we need to add the temp vbox and hbox generated elements as children otherwise we get a crash. yipee.

            verticalScrollbar.id.setValue("verticalScrollbar");
            verticalScrollbar.boundElement.addIsPropertyVisibleFunction(abstractUIElementBindingProperty -> false);
            children.add(verticalScrollbar);

            horizontalScrollbar.id.setValue("horizontalScrollbar");
            horizontalScrollbar.boundElement.addIsPropertyVisibleFunction(abstractUIElementBindingProperty -> false);
            children.add(horizontalScrollbar);
        }

        @Override
        public UIElement makeUIElement_internal() {
            return new Scrollbox(this);
        }
    }
}
