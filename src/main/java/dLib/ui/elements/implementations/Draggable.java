package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.util.IntegerVector2;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class Draggable extends Interactable{
    //region Variables

    private boolean canDragX = true;
    private boolean canDragY = true;

    private int xDragOffset;
    private int yDragOffset;

    //endregion

    //region Constructors

    public Draggable(Texture image) {
        super(image);
    }
    public Draggable(Texture image, AbstractPosition xPos, AbstractPosition yPos) {
        this(image, xPos, yPos, Dim.fill(), Dim.fill());
    }
    public Draggable(Texture image, AbstractDimension width, AbstractDimension height) {
        this(image, Pos.perc(0), Pos.perc(0), width, height);
    }
    public Draggable(Texture image, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(image, xPos, yPos, width, height);
    }

    //endregion

    //region Methods

    //region Drag

    @Override
    protected void onLeftClick() {
        super.onLeftClick();
        if(canDragX) xDragOffset = (int) (InputHelper.mX - this.getWorldPositionX() * Settings.xScale);
        if(canDragY) yDragOffset = (int) (InputHelper.mY - this.getWorldPositionY() * Settings.yScale);
    }

    @Override
    protected void onLeftClickHeld(float totalDuration) {
        super.onLeftClickHeld(totalDuration);

        int xPos = canDragX ? (int) ((InputHelper.mX - xDragOffset) / Settings.xScale) : getWorldPositionX();
        int yPos = canDragY ? (int) ((InputHelper.mY - yDragOffset) / Settings.yScale) : getWorldPositionY();

        setWorldPosition(xPos, yPos);
    }

    public Draggable setCanDragX(boolean canDragX){
        this.canDragX = canDragX;
        return this;
    }
    public Draggable setCanDragY(boolean canDragY){
        this.canDragY = canDragY;
        return this;
    }
    //endregion

    //endregion
}
