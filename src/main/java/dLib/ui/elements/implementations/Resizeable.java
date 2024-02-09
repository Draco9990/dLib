package dLib.ui.elements.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.data.implementations.ResizeableData;
import dLib.ui.elements.misc.ResizeNode;

public class Resizeable extends Draggable{
    /** Variables */
    private ResizeNode[] resizeNodes = new ResizeNode[4];

    private int resizeOrigPosX;
    private int resizeOrigPosY;
    private int resizeOrigWidth;
    private int resizeOrigHeight;

    private boolean resizingRootX;
    private boolean resizingRootY;

    private int resizeOrigNodePosX;
    private int resizeOrigNodePosY;

    /** Constructors */
    public Resizeable(Texture image) {
        super(image);
        initialize();
    }

    public Resizeable(Texture image, int xPos, int yPos) {
        super(image, xPos, yPos);
        initialize();
    }

    public Resizeable(Texture image, int xPos, int yPos, int width, int height) {
        super(image, xPos, yPos, width, height);
        initialize();
    }

    public Resizeable(ResizeableData data){
        super(data);

        initialize();
    }

    private void initialize(){
        for(int i = 0; i < 4; i++){
            resizeNodes[i] = new ResizeNode(this, 0, 0){
                @Override
                protected void onLeftClick() {
                    super.onLeftClick();

                    onResizeStarted(this);
                }

                @Override
                protected void onLeftClickHeld(float totalDuration) {
                    int oldXPos = x;
                    int oldYPos = y;

                    super.onLeftClickHeld(totalDuration);

                    onResizeNodeMoved(this, oldXPos, oldYPos, x, y);
                }

                @Override
                protected void onLeftClickRelease() {
                    super.onLeftClickRelease();

                    onResizeFinished(this);
                }
            };
        }
    }

    /** Update and render */
    @Override
    public void update() {
        for(ResizeNode node : resizeNodes){
            node.update();
        }

        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        for(ResizeNode node : resizeNodes){
            if(isHovered() || node.isHovered()) node.render(sb);
        }
    }

    /** Resize nodes */
    private void refreshResizeNodePositions(){
        resizeNodes[0].setCenterPosition(x, y + height);
        resizeNodes[1].setCenterPosition(x + width, y + height);
        resizeNodes[2].setCenterPosition(x, y);
        resizeNodes[3].setCenterPosition(x + width, y);
    }

    /** Resizing */
    public void onResizeStarted(ResizeNode node){
        resizeOrigPosX = x;
        resizeOrigPosY = y;

        resizeOrigWidth = width;
        resizeOrigHeight = height;

        int oldPosCX = node.getPositionX() + (int)((float)node.getWidth() / 2);
        int oldPosCY = node.getPositionY() + (int)((float)node.getHeight() / 2);
        resizingRootX = oldPosCX == x;
        resizingRootY = oldPosCY == y;

        resizeOrigNodePosX = node.getPositionX();
        resizeOrigNodePosY = node.getPositionY();
    }

    public void onResizeNodeMoved(ResizeNode nodeMoved, int oldPosX, int oldPosY, int newPosX, int newPosY){
        int diffX = newPosX - resizeOrigNodePosX;
        int diffY = newPosY - resizeOrigNodePosY;

        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
            int sDiffX = Math.abs(diffX);
            int sDiffY = Math.abs(diffY);
            if(sDiffX > sDiffY) diffY = 0;
            else diffX = 0;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            int sDiffX = Math.abs(diffX);
            int sDiffY = Math.abs(diffY);
            sDiffX = sDiffY = Math.max(sDiffX, sDiffY);

            if(diffX < 0) sDiffX *= -1;
            if(diffY < 0) sDiffY *= -1;
            diffX = sDiffX;
            diffY = sDiffY;
        }

        if(resizingRootX){
            setPositionX(resizeOrigPosX + diffX);
            diffX = -diffX;
        }
        else{
            setPositionX(resizeOrigPosX);
        }
        setWidth(resizeOrigWidth + diffX);

        if(resizingRootY){
            setPositionY(resizeOrigPosY + diffY);
            diffY = -diffY;
        }
        else{
            setPositionY(resizeOrigPosY);
        }
        setHeight(resizeOrigHeight + diffY);
    }

    public void onResizeFinished(ResizeNode node){
        resizeOrigPosX = 0;
        resizeOrigPosY = 0;
        resizeOrigWidth = 0;
        resizeOrigHeight = 0;

        resizingRootX = false;
        resizingRootY = false;

        resizeOrigNodePosX = 0;
        resizeOrigNodePosY = 0;
    }

    /** Position & Width/Height */
    @Override
    public Resizeable setWidth(int newWidth) {
        super.setWidth(newWidth);
        refreshResizeNodePositions();
        return this;
    }

    @Override
    public Resizeable setHeight(int newHeight) {
        super.setHeight(newHeight);
        refreshResizeNodePositions();
        return this;
    }

    @Override
    public Resizeable setDimensions(Integer newWidth, Integer newHeight) {
        if(upperBoundX != null && x + newWidth > upperBoundX){
            newWidth = upperBoundX - x;
        }
        if(upperBoundY != null && y + newHeight > upperBoundY){
            newHeight = upperBoundY - y;
        }
        return (Resizeable) super.setDimensions(newWidth, newHeight);
    }

    @Override
    public Resizeable setPosition(Integer newPosX, Integer newPosY) {
        super.setPosition(newPosX, newPosY);
        refreshResizeNodePositions();
        return this;
    }
}
