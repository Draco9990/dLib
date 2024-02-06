package dLib.ui.elements.implementations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.data.implementations.ResizeableData;
import dLib.ui.elements.misc.ResizeNode;

public class Resizeable extends Draggable{
    /** Variables */
    private ResizeNode[] resizeNodes = new ResizeNode[4];

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
            resizeNodes[i] = new ResizeNode(this, 0, 0);
        }
    }

    /** Update and render */
    @Override
    public void update() {
        if(isSelected()){
            for(ResizeNode node : resizeNodes){
                node.update();
            }
        }

        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        if(isSelected()){
            for(ResizeNode node : resizeNodes){
                node.render(sb);
            }
        }
    }

    /** Resize nodes */
    private void refreshResizeNodePositions(){
        resizeNodes[0].setCenterPosition(x, y + height);
        resizeNodes[1].setCenterPosition(x + width, y + height);
        resizeNodes[2].setCenterPosition(x, y);
        resizeNodes[3].setCenterPosition(x + width, y);
    }

    /** Position */
    @Override
    public Resizeable setPosition(int newPosX, int newPosY) {
        super.setPosition(newPosX, newPosY);
        refreshResizeNodePositions();
        return this;
    }
}
