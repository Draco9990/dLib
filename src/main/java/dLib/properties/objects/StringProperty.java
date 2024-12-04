package dLib.properties.objects;

import dLib.properties.ui.elements.OnValueCommitedStringPropertyEditor;

import java.io.Serializable;

public class StringProperty extends Property<String> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private int characterLimit = -1;

    //endregion

    //region Constructors

    public StringProperty(String defaultValue){
        super(defaultValue);
        propertyEditorClass = OnValueCommitedStringPropertyEditor.class;
    }

    //endregion

    //region Confirmation Mode

    //region Methods

    @Override
    public boolean setValueFromString(String value) {
        return setValue(value);
    }

    @Override
    public StringProperty setName(String newTitle) {
        return (StringProperty) super.setName(newTitle);
    }

    @Override
    public StringProperty setDescription(String description) {
        return (StringProperty) super.setDescription(description);
    }

    //region Max Length

    public StringProperty setCharacterLimit(int characterLimit){
        this.characterLimit = characterLimit;
        return this;
    }

    public int getCharacterLimit(){
        return characterLimit;
    }

    //endregion

    //endregion
}
