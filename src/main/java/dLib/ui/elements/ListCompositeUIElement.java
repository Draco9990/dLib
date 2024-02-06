package dLib.ui.elements;

import java.util.ArrayList;

public class ListCompositeUIElement extends CompositeUIElement{
    /** Variables */
    private ArrayList<CompositeUIElement> elements = new ArrayList<>();
    private Integer currentIndex;

    /** Constructors */
    public ListCompositeUIElement(int xPos, int yPos){
        super(xPos, yPos);
    }

    /** List */
    public ListCompositeUIElement addElement(CompositeUIElement element){
        elements.add(element);

        if(currentIndex == null){
            currentIndex = 0;
            onCurrentIndexChanged();
        }

        return this;
    }

    public void clearElements(){
        elements.clear();
    }

    private void onCurrentIndexChanged(){
        CompositeUIElement toCopy = elements.get(currentIndex);
        this.left = toCopy.left;
        this.middle = toCopy.middle;
        this.right = toCopy.right;
        this.other = toCopy.other;
    }
}
