package dLib.ui.elements.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dLib.ui.elements.UIElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ElementGroupModifierComponent extends UIElementComponent<UIElement> {
    private static HashMap<String, ArrayList<ElementGroupModifierComponent>> groupComponents = new HashMap<>();

    private UIElement owner;

    private String groupName;

    private UUID multiSelectComponentID;

    private UUID onDragStartEventID;
    private UUID onDragEventID;

    private boolean isSelected = false;

    public ElementGroupModifierComponent(UIElement element, String groupname){
        this.owner = element;
        this.groupName = groupname;

        ensureGroupExists(groupname);
        groupComponents.get(groupname).add(this);
    }

    @Override
    public void onRegisterComponent(UIElement owner) {
        super.onRegisterComponent(owner);

        multiSelectComponentID = owner.onLeftClickEvent.subscribeManaged(this::select);

        if(owner.hasComponent(UIDraggableComponent.class)){
            onDragStartEventID = owner.onLeftClickEvent.subscribeManaged(() -> {
                for(ElementGroupModifierComponent comp : groupComponents.get(groupName)){
                    if(comp != this && comp.isSelected && comp.owner.hasComponent(UIDraggableComponent.class)){
                        comp.owner.getComponent(UIDraggableComponent.class).onLeftClick();
                    }
                }
            });

            onDragEventID = owner.onLeftClickHeldEvent.subscribeManaged((delta) -> {
                for(ElementGroupModifierComponent comp : groupComponents.get(groupName)){
                    if(comp != this && comp.isSelected && comp.owner.hasComponent(UIDraggableComponent.class)){
                        comp.owner.getComponent(UIDraggableComponent.class).onLeftClickHeld(delta);
                    }
                }
            });
        }
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        super.onUnregisterComponent(owner);

        owner.onLeftClickHeldEvent.unsubscribeManaged(multiSelectComponentID);

        if(owner.hasComponent(UIDraggableComponent.class)){
            owner.onLeftClickEvent.unsubscribeManaged(onDragStartEventID);
            owner.onLeftClickHeldEvent.unsubscribeManaged(onDragEventID);
        }
    }

    public static void deselectAllGroupComponentsExcept(String exceptionID){
        for(ArrayList<ElementGroupModifierComponent> group : groupComponents.values()){
            for(ElementGroupModifierComponent comp : group){
                if(!comp.groupName.equals(exceptionID)){
                    comp.isSelected = false;
                }
            }
        }
    }

    public static void deselectGroupComponents(String groupID){
        ensureGroupExists(groupID);
        for(ElementGroupModifierComponent comp : groupComponents.get(groupID)){
            comp.isSelected = false;
        }
    }

    public boolean isSelected(){
        return isSelected;
    }

    public void select(){
        deselectAllGroupComponentsExcept(groupName);

        if(!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && !Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)){
            deselectGroupComponents(groupName);
        }

        isSelected = true;
    }

    private static void ensureGroupExists(String groupName){
        groupComponents.computeIfAbsent(groupName, k -> new ArrayList<>());
    }
}
