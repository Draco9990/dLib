package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.ui.data.implementations.DraggableData;
import dLib.util.DLibLogger;

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
    public Draggable(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
    }
    public Draggable(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
    }

    public Draggable(DraggableData data){
        super(data);

        this.canDragX = data.canDragX;
        this.canDragY = data.canDragY;
    }

    //endregion

    //region Methods

    //region Drag

    @Override
    protected void onLeftClick() {
        super.onLeftClick();
        if(canDragX) xDragOffset = (int) (InputHelper.mX - this.x * Settings.xScale);
        if(canDragY) yDragOffset = (int) (InputHelper.mY - this.y * Settings.yScale);
    }

    @Override
    protected void onLeftClickHeld(float totalDuration) {
        super.onLeftClickHeld(totalDuration);

        int xPos = canDragX ? (int) ((InputHelper.mX - xDragOffset) / Settings.xScale) : x;
        int yPos = canDragY ? (int) ((InputHelper.mY - yDragOffset) / Settings.yScale) : y;

        setPosition(xPos, yPos);
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
