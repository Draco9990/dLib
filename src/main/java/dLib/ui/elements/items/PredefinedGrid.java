package dLib.ui.elements.items;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class PredefinedGrid extends UIElement {
    private int rowCount;
    private int columnCount;

    private UIElement[][] gridSlots = new UIElement[0][0];

    private VerticalBox mainColumn;
    private HorizontalBox[] rows;

    public PredefinedGrid(int rowCount, int columnCount) {
        this(rowCount, columnCount, Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
    }

    public PredefinedGrid(int rowCount, int columnCount, AbstractDimension width, AbstractDimension height) {
        this(rowCount, columnCount, Pos.px(0), Pos.px(0), width, height);
    }

    public PredefinedGrid(int rowCount, int columnCount, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        this.rowCount = rowCount;
        this.columnCount = columnCount;

        initializeGridSlots();
    }

    private void initializeGridSlots(){
        gridSlots = new UIElement[rowCount][columnCount];
        rows = new HorizontalBox[rowCount];

        mainColumn = new VerticalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
        for(int i = 0; i < rowCount; i++){
            rows[i] = new HorizontalBox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            for(int j = 0; j < columnCount; j++){
                rows[i].addItem(new UIElement(Dim.fill(), Dim.fill()));
            }
            mainColumn.addItem(rows[i]);
        }
        addChildNCS(mainColumn);
    }

    public void setGridSlotElement(int rowIndex, int columnIndex, UIElement element){
        UIElement currentElement = gridSlots[rowIndex][columnIndex];

        UIElement header = rows[rowIndex].getChildren().get(columnIndex);
        if(currentElement != null){
            header.replaceChild(currentElement, element);
        }
        else{
            header.addChildNCS(element);
        }
    }
}
