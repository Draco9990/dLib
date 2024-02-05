package dLib.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.screens.AbstractScreen;

import java.util.ArrayList;
import java.util.Iterator;

public class ElementGroupManager {

    private int currentElement = -1;
    private ArrayList<CompositeUIElement> elements = new ArrayList<>();

    public void addElement(CompositeUIElement element){
        elements.add(element);
    }

    public void update(){
        elements.forEach(CompositeUIElement::update);
    }

    public void render(SpriteBatch sb){
        elements.forEach(element -> element.render(sb));
    }

    private CompositeUIElement getSelectedElementGroup(){
        if(elements.isEmpty()) return null;
        if(currentElement < 0) return null;
        if(currentElement >= elements.size()) return null;

        return elements.get(currentElement);
    }

    public ArrayList<CompositeUIElement> getElements(){
        return elements;
    }

    public void removeUIElement(UIElement elementToRemove){
        Iterator<CompositeUIElement> elementIterator = elements.iterator();
        while(elementIterator.hasNext()){
            CompositeUIElement element = elementIterator.next();
            element.removeUIElement(elementToRemove);

            if(element.isEmpty()){
                elementIterator.remove();
            }
        }
    }

    public void purgeTempElements(){
        Iterator<CompositeUIElement> elementGroupIterator = elements.iterator();
        while(elementGroupIterator.hasNext()){
            CompositeUIElement element = elementGroupIterator.next();
            if(element.temporary){
                if(currentElement >= 0 && currentElement < elements.size() && element.equals(elements.get(currentElement))){
                    element.deselect();
                    currentElement = -1;
                }

                elementGroupIterator.remove();
            }
        }
    }

    public void onDownInteraction(AbstractScreen screen){
        if(elements.isEmpty()) return;
        CompositeUIElement currentGroup = getSelectedElementGroup();
        if(currentGroup != null) currentGroup.deselect();

        currentElement--;
        if(currentElement < 0){
            currentElement = elements.size() - 1;
            screen.onIterationReachedBottom();
        }

        CompositeUIElement newGroup = getSelectedElementGroup();
        if(newGroup != null) newGroup.select();
    }
    public void onUpInteraction(AbstractScreen screen){
        if(elements.isEmpty()) return;
        CompositeUIElement currentGroup = getSelectedElementGroup();
        if(currentGroup != null) currentGroup.deselect();

        currentElement++;
        if(currentElement >= elements.size()){
            currentElement = 0;
            screen.onIterationReachedTop();
        }

        CompositeUIElement newGroup = getSelectedElementGroup();
        if(newGroup != null) newGroup.select();
    }
    public void onLeftInteraction(AbstractScreen screen){
        if(elements.isEmpty()) return;

        CompositeUIElement currentGroup = getSelectedElementGroup();
        if(currentGroup != null) currentGroup.triggerLeft();
    }
    public void onRightInteraction(AbstractScreen screen){
        if(elements.isEmpty()) return;

        CompositeUIElement currentGroup = getSelectedElementGroup();
        if(currentGroup != null) currentGroup.triggerRight();
    }
    public void onConfirmInteraction(AbstractScreen screen){
        if(elements.isEmpty()) return;

        CompositeUIElement currentGroup = getSelectedElementGroup();
        if(currentGroup != null) currentGroup.triggerMiddle();
    }
}
