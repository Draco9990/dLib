package dLib.properties.objects.templates;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class TDynamicProperty<ItemType> extends TProperty<ItemType, TDynamicProperty<ItemType>> implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    protected ArrayList<ItemType> choices;

    //endregion

    //region Constructors

    public TDynamicProperty(ItemType value, ArrayList<ItemType> choices) {
        super(value);

        this.choices = choices;
    }

    //endregion

    //region Methods

    //region Values

    //endregion

    //region Choices

    public void setChoices(ArrayList<ItemType> choices) {
        this.choices = choices;
    }

    public ArrayList<ItemType> getChoices() {
        return choices;
    }

    //endregion Choices

    //endregion
}
