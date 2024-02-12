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
        if(currentIndex == -1) select();
        if(currentIndex > 0 && currentIndex < elements.size()) elements.get(currentIndex).deselect();

        currentIndex++;
        if(currentIndex >= elements.size()){
            currentIndex = -1;
            deselect();
            return false;
        }

        elements.get(currentIndex).select();
        return true;
    }

    public boolean iteratePrevious(){
        if(currentIndex == -1) {
            select();
            currentIndex = elements.size();
        }
        if(currentIndex > 0 && currentIndex < elements.size()) elements.get(currentIndex).deselect();

        currentIndex--;
        if(currentIndex < 0){
            currentIndex = -1;
            return false;
        }

        elements.get(currentIndex).select();
        return true;
    }

    /** List */
    protected ListCompositeUIElement addElement(CompositeUIElement element){
        elements.add(element);

        return this;
    }

    protected void clearElements(){
        elements.clear();
    }

    /** Interactable? */
    @Override
    public boolean isUserInteractable() {
        return true;
    }
}
