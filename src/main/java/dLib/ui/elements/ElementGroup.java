package dLib.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.modcompat.ModManager;
import dLib.ui.elements.implementations.Interactable;
import sayTheSpire.Output;

import java.util.ArrayList;

public class ElementGroup {
    public UIElement left;
    public UIElement right;
    public UIElement middle;
    public ArrayList<UIElement> other = new ArrayList<>();

    public String onSelectedLine = ""; // Say the Spire mod compatibility
    public String onTriggeredLine = ""; // Say the Spire mod compatibility

    public boolean temporary = false;

    /** Update */
    public void update(){
        if(left != null) left.update();
        if(right != null) right.update();
        if(middle != null) middle.update();
        for(UIElement u : other) u.update();
    }

    /** Render */
    public void render(SpriteBatch sb){
        if(left != null) left.render(sb);
        if(right != null) right.render(sb);
        if(middle != null) middle.render(sb);
        for(UIElement u : other) u.render(sb);
    }

    /** Getters and Setters */
    public ElementGroup setOnSelectedLine(String newLine){
        this.onSelectedLine = newLine;
        return this;
    }
    public String getOnSelectedLine(){ return this.onSelectedLine; }

    public ElementGroup setOnTriggerLine(String newLine){
        this.onTriggeredLine = newLine;
        return this;
    }
    public String getOnTriggeredLine(){ return this.onTriggeredLine; }

    /** Fired whenever the current element group comes into focus */
    public void select(){
        if(ModManager.SayTheSpire.isActive()){
            if(getOnSelectedLine() != null){
                Output.text(getOnSelectedLine(), true);
            }
        }

        if(left != null){
            if(left instanceof Interactable){
                ((Interactable) left).select();
            }
        }
        if(middle != null){
            if(middle instanceof Interactable){
                ((Interactable) middle).select();
            }
        }
        if(right != null){
            if(right instanceof Interactable){
                ((Interactable) right).select();
            }
        }
        for(UIElement otherElement : other) {
            if(otherElement instanceof Interactable){
                ((Interactable) otherElement).select();
            }
        }
    }
    public void deselect(){
        if(left != null){
            if(left instanceof Interactable){
                ((Interactable) left).deselect();
            }
        }
        if(middle != null){
            if(middle instanceof Interactable){
                ((Interactable) middle).deselect();
            }
        }
        if(right != null){
            if(right instanceof Interactable){
                ((Interactable) right).deselect();
            }
        }
        for(UIElement otherElement : other) {
            if(otherElement instanceof Interactable){
                ((Interactable) otherElement).deselect();
            }
        }
    }

    public void triggerLeft(){
        if(left != null){
            if(left instanceof Interactable){
                ((Interactable) left).clickLeft();
            }
        }

        if(getOnTriggeredLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnTriggeredLine(), true);
            }
        }
    }
    public void triggerRight(){
        if(right != null){
            if(right instanceof Interactable){
                ((Interactable) right).clickLeft();
            }
        }

        if(getOnTriggeredLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnTriggeredLine(), true);
            }
        }
    }
    public void triggerMiddle(){
        if(middle != null){
            if(middle instanceof Interactable){
                ((Interactable) middle).clickLeft();
            }
        }

        for(UIElement element : other){
            if(element instanceof Interactable){
                ((Interactable) element).clickLeft();
            }
        }

        if(getOnTriggeredLine() != null){
            if(ModManager.SayTheSpire.isActive()){
                Output.text(getOnTriggeredLine(), true);
            }
        }
    }
}
