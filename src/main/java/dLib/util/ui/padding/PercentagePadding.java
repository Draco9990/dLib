package dLib.util.ui.padding;

import basemod.Pair;
import dLib.ui.elements.UIElement;
import dLib.util.helpers.UIHelpers;

import java.io.Serializable;

public class PercentagePadding extends AbstractPadding implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Variables

    private float perc;

    //endregion

    //region Constructors

    public PercentagePadding(float perc){
        if(perc < 0) perc = 0;
        if(perc > 1) perc = 1;

        this.perc = perc;
    }

    //endregion

    //region Class Methods

    //region Calculation


    @Override
    protected Float tryCalculateValue_Horizontal(UIElement forElement) {
        Pair<Float, UIElement> parentWidth = UIHelpers.getCalculatedParentWidthInHierarchyWithParent(forElement);
        if(parentWidth.getKey() == null) return null;
        if(parentWidth.getValue() != null) registerDependency(parentWidth.getValue().getWidthRaw());

        return parentWidth.getKey() * perc;
    }

    @Override
    protected Float tryCalculateValue_Vertical(UIElement forElement) {
        Pair<Float, UIElement> parentHeight = UIHelpers.getCalculatedParentHeightInHierarchyWithParent(forElement);
        if(parentHeight.getKey() == null) return null;
        if(parentHeight.getValue() != null) registerDependency(parentHeight.getValue().getHeightRaw());

        return parentHeight.getKey() * perc;
    }

    //endregion

    //region Utility

    @Override
    public PercentagePadding cpy() {
        PercentagePadding padding = new PercentagePadding(perc);
        padding.copyFrom(this);
        return padding;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PercentagePadding)) {
            return false;
        }

        PercentagePadding padding = (PercentagePadding) obj;
        return padding.perc == perc;
    }

    //endregion

    //endregion
}
