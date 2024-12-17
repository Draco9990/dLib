package dLib.ui.elements.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.ui.elements.UIElement;
import dLib.ui.resources.UICommonResources;

import dLib.util.IntegerVector2;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

public class Resizeable extends Draggable {
    //region Variables

    private ResizeNode[] resizeNodes = new ResizeNode[4];

    private int resizeOrigPosX;
    private int resizeOrigPosY;
    private int resizeOrigWidth;
    private int resizeOrigHeight;

    private boolean resizingRootX;
    private boolean resizingRootY;

    private int resizeOrigNodePosX;
    private int resizeOrigNodePosY;

    //endregion

    //region Constructors

    public Resizeable(Texture image, int width, int height){
        this(image, Pos.px(0), Pos.px(0), width, height);
    }
    public Resizeable(Texture image, AbstractPosition xPos, AbstractPosition yPos, int width, int height) {
        super(image, xPos, yPos, Dim.px(width), Dim.px(height));
        initialize();
    }

    private void initialize() {
        Resizeable resizeable = this;
        clearChildren();

        for (int i = 0; i < 4; i++) {
            resizeNodes[i] = new ResizeNode(Pos.px(0), Pos.px(0)) {
                @Override
                protected void onLeftClick() {
                    super.onLeftClick();

                    onResizeStarted(this);
                }

                @Override
                protected void onLeftClickHeld(float totalDuration) {
                    super.onLeftClickHeld(totalDuration);

                    onResizeNodeMoved(this);
                }

                @Override
                protected void onLeftClickRelease() {
                    super.onLeftClickRelease();

                    onResizeFinished(this);
                }

                @Override
                public boolean isVisible() {
                    return isHovered() || resizeable.isHovered();
                }
            };

            addChildNCS(resizeNodes[i]);
        }

        refreshResizeNodePositions();
    }

    //endregion

    //region Methods

    //region Resize

    public void onResizeStarted(ResizeNode node) {
        resizeOrigPosX = getLocalPositionX();
        resizeOrigPosY = getLocalPositionY();

        resizeOrigWidth = getWidth();
        resizeOrigHeight = getHeight();

        IntegerVector2 oldPosC = worldToLocal(node.getWorldPositionCentered());
        resizingRootX = oldPosC.x == getLocalPositionX();
        resizingRootY = oldPosC.y == getLocalPositionY();

        resizeOrigNodePosX = node.getWorldPositionX();
        resizeOrigNodePosY = node.getWorldPositionY();
    }

    public void onResizeNodeMoved(ResizeNode nodeMoved) {
        int diffX = nodeMoved.getWorldPositionX() - resizeOrigNodePosX;
        int diffY = nodeMoved.getWorldPositionY() - resizeOrigNodePosY;

        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            int sDiffX = Math.abs(diffX);
            int sDiffY = Math.abs(diffY);
            if (sDiffX > sDiffY) diffY = 0;
            else diffX = 0;
        } else if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            int sDiffX = Math.abs(diffX);
            int sDiffY = Math.abs(diffY);
            sDiffX = sDiffY = Math.max(sDiffX, sDiffY);

            if (diffX < 0) sDiffX *= -1;
            if (diffY < 0) sDiffY *= -1;
            diffX = sDiffX;
            diffY = sDiffY;
        }

        if(diffX != 0){
            if (resizingRootX) {
                setLocalPositionX(resizeOrigPosX + diffX);
                diffX = -diffX;
            } else {
                setLocalPositionX(resizeOrigPosX);
            }
            setWidth(Dim.px(resizeOrigWidth + diffX));
        }

        if(diffY != 0){
            if (resizingRootY) {
                setLocalPositionY(resizeOrigPosY + diffY);
                diffY = -diffY;
            } else {
                setLocalPositionY(resizeOrigPosY);
            }
            setHeight(Dim.px(resizeOrigHeight + diffY));
        }
    }

    public void onResizeFinished(ResizeNode node) {
        resizeOrigPosX = 0;
        resizeOrigPosY = 0;
        resizeOrigWidth = 0;
        resizeOrigHeight = 0;

        resizingRootX = false;
        resizingRootY = false;

        resizeOrigNodePosX = 0;
        resizeOrigNodePosY = 0;
    }

    //endregion

    private void refreshResizeNodePositions() {
        if(resizeNodes != null && resizeNodes.length == 4){
            resizeNodes[0].setLocalPositionCentered(0, getHeight());
            resizeNodes[1].setLocalPositionCentered(getWidth(), getHeight());
            resizeNodes[2].setLocalPositionCentered(0, 0);
            resizeNodes[3].setLocalPositionCentered(getWidth(), 0);
        }
    }

    @Override
    public UIElement setDimensions(AbstractDimension newWidth, AbstractDimension newHeight) {
        super.setDimensions(newWidth, newHeight);
        refreshResizeNodePositions(); //TODO RF remove
        return this;
    }

    //endregion

    private static class ResizeNode extends Draggable {
        //region Variables

        //endregion

        //region Constructors

        public ResizeNode(AbstractPosition xPos, AbstractPosition yPos) {
            super(UICommonResources.white_pixel, xPos, yPos, Dim.px(20), Dim.px(20));

            this.renderColor = Color.RED;
        }

        //endregion

        //region Methods
        //endregion
    }
}