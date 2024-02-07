package dLib.ui.elements;

import dLib.ui.data.CompositeUIElementData;
import dLib.ui.data.ListCompositeUIElementData;

import java.util.ArrayList;

public class ListCompositeUIElement extends CompositeUIElement{
    /** Variables */
    private ArrayList<CompositeUIElement> elements = new ArrayList<>();
    private Integer currentIndex;

    /** Constructors */
    public ListCompositeUIElement(int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);
    }

    public ListCompositeUIElement(ListCompositeUIElementData data){
        super(data);
        for(CompositeUIElementData elementData : data.dataList){
            addElement(elementData.makeLiveInstance());
        }
    }

    /** Iterating */
    public boolean iterateNext(){
        deselect();

        currentIndex++;
        if(currentIndex >= elements.size()){
            currentIndex = -1;
            return false;
        }

        onCurrentIndexChanged();
        select();
        return true;
    }

    public boolean iteratePrevious(){
        deselect();

        currentIndex--;
        if(currentIndex < 0){
            currentIndex = -1;
            return false;
        }

        onCurrentIndexChanged();
        select();
        return true;
    }

    /** List */
    protected ListCompositeUIElement addElement(CompositeUIElement element){
        elements.add(element);

        if(currentIndex == null){
            currentIndex = 0;
            onCurrentIndexChanged();
        }

        return this;
    }

    protected void clearElements(){
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
