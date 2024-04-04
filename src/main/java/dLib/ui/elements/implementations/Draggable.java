package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import dLib.util.IntegerVector2;

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

        IntegerVector2 worldPos = new IntegerVector2(xPos, yPos);
        IntegerVector2 localPos = worldToLocal(worldPos);
        onDragForValueAdjust(localPos);
        worldPos = localToWorld(localPos);

        setWorldPosition(worldPos.x, worldPos.y);
    }

    public Draggable setCanDragX(boolean canDragX){
        this.canDragX = canDragX;
        return this;
    }
    public Draggable setCanDragY(boolean canDragY){
        this.canDragY = canDragY;
        return this;
    }

    protected void onDragForValueAdjust(IntegerVector2 newPos){
    }

    //endregion

    //endregion
}
