package dLib.properties.objects.templates;

import java.io.Serializable;

public abstract class TStringProperty<PropertyType extends TStringProperty<PropertyType>> extends TProperty<String, PropertyType> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    private int characterLimit = -1;

    //endregion

    //region Constructors

    public TStringProperty(String defaultValue){
        super(defaultValue);
    }

    //endregion

    //region Confirmation Mode

    //region Methods

    @Override
    public boolean setValueFromString(String value) {
        return setValue(value);
    }

    //region Max Length

    public PropertyType setCharacterLimit(int characterLimit){
        this.characterLimit = characterLimit;
        return (PropertyType) this;
    }

    public int getCharacterLimit(){
        return characterLimit;
    }

    //endregion

    //endregion
}
