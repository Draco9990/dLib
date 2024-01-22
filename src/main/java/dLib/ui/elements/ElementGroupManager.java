package dLib.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.screens.AbstractScreen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class ElementGroupManager {

    private int currentElement = -1;
    private ArrayList<ElementGroup> elements = new ArrayList<>();

    public void addElement(ElementGroup element){
        elements.add(element);
    }

    public void update(){
        elements.forEach(ElementGroup::update);
    }

    public void render(SpriteBatch sb){
        elements.forEach(element -> element.render(sb));
    }

    private ElementGroup getSelectedElementGroup(){
        if(elements.isEmpty()) return null;
        if(currentElement < 0) return null;
        if(currentElement >= elements.size()) return null;

        return elements.get(currentElement);
    }

    public ArrayList<ElementGroup> getElements(){
        return elements;
    }

    public void purgeTempElements(){
        Iterator<ElementGroup> elementGroupIterator = elements.iterator();
        while(elementGroupIterator.hasNext()){
            ElementGroup element = elementGroupIterator.next();
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
        ElementGroup currentGroup = getSelectedElementGroup();
        if(currentGroup != null) currentGroup.deselect();

        currentElement--;
        if(currentElement < 0){
            currentElement = elements.size() - 1;
            screen.onIterationReachedBottom();
        }

        ElementGroup newGroup = getSelectedElementGroup();
        if(newGroup != null) newGroup.select();
    }
    public void onUpInteraction(AbstractScreen screen){
        if(elements.isEmpty()) return;
        ElementGroup currentGroup = getSelectedElementGroup();
        if(currentGroup != null) currentGroup.deselect();

        currentElement++;
        if(currentElement >= elements.size()){
            currentElement = 0;
            screen.onIterationReachedTop();
        }

        ElementGroup newGroup = getSelectedElementGroup();
        if(newGroup != null) newGroup.select();
    }
    public void onLeftInteraction(AbstractScreen screen){
        if(elements.isEmpty()) return;

        ElementGroup currentGroup = getSelectedElementGroup();
        if(currentGroup != null) currentGroup.triggerLeft();
    }
    public void onRightInteraction(AbstractScreen screen){
        if(elements.isEmpty()) return;

        ElementGroup currentGroup = getSelectedElementGroup();
        if(currentGroup != null) currentGroup.triggerRight();
    }
    public void onConfirmInteraction(AbstractScreen screen){
        if(elements.isEmpty()) return;

        ElementGroup currentGroup = getSelectedElementGroup();
        if(currentGroup != null) currentGroup.triggerMiddle();
    }
}
