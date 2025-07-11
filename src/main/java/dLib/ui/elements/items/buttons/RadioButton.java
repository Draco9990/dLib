package dLib.ui.elements.items.buttons;

import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class RadioButton extends Toggle {
    //region Variables

    private static HashMap<String, ArrayList<RadioButton>> radioButtonMap = new HashMap<>();

    private String groupId = "";

    //endregion

    //region Constructors

    public RadioButton(String groupId, AbstractPosition xPos, AbstractPosition yPos){
        this(groupId, xPos, yPos, Dim.fill(), Dim.fill());
    }
    public RadioButton(String groupId, AbstractDimension width, AbstractDimension height){
        this(groupId, Pos.px(0), Pos.px(0), width, height);
    }
    public RadioButton(String groupId, AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(Tex.stat(UICommonResources.radiobutton_checked_unchecked), Tex.stat(UICommonResources.radiobutton_checked_checked), xPos, yPos, width, height);

        this.groupId = groupId;
    }

    public RadioButton(RadioButtonData data) {
        super(data);
    }

    @Override
    public void postConstruct() {
        super.postConstruct();

        if(!radioButtonMap.containsKey(groupId)){
            radioButtonMap.put(groupId, new ArrayList<>());
        }
        radioButtonMap.get(groupId).add(this);
    }

    @Override
    public void dispose() {
        super.dispose();

        if(radioButtonMap.containsKey(groupId)){
            radioButtonMap.get(groupId).remove(this);
            if(radioButtonMap.get(groupId).isEmpty()){
                radioButtonMap.remove(groupId);
            }
        }
    }

    //endregion

    //region Methods

    @Override
    public void toggle(boolean byProxy) {
        if(this.isToggled()){
            return;
        }

        super.toggle(byProxy);
    }

    @Override
    public void onToggledStateChanged() {
        super.onToggledStateChanged();

        if(isToggled()){
            // If this radio button is toggled, untoggle all other radio buttons in the same group
            if(radioButtonMap.containsKey(groupId)){
                radioButtonMap.get(groupId).forEach(radioButton -> {
                    if(radioButton != this) {
                        radioButton.setToggled(false);
                    }
                });
            }
        }
    }

    //endregion

    public static class RadioButtonData extends ToggleData implements Serializable {
        private static final long serialVersionUID = 1L;

        //region Variables

        //endregion

        //region Constructors

        public RadioButtonData() {
            super();
        }

        //endregion

        //region Methods

        @Override
        public RadioButton makeUIElement_internal() {
            return new RadioButton(this);
        }

        //endregion
    }
}
