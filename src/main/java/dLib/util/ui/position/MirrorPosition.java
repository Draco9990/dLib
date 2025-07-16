package dLib.util.ui.position;

import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "mirror")
public class MirrorPosition extends AbstractPosition implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Constructors

    public MirrorPosition(){
        super();
    }

    //endregion

    //region Class Methods

    //region Calculation Methods

    @Override
    protected Float tryCalculateValue_X(UIElement forElement) {
        if(forElement.getPaddingLeftRaw().needsRecalculation()) return null;

        if(forElement.getLocalPositionYRaw().needsRecalculation()) return null;
        if(forElement.getPaddingBottomRaw().needsRecalculation()) return null;

        float calculatedVal = forElement.getLocalPositionY() - forElement.getPaddingBottom();

        calculatedVal += forElement.getOffsetX();
        calculatedVal += forElement.getPaddingLeft();

        return calculatedVal;
    }

    @Override
    protected Float tryCalculateValue_Y(UIElement forElement) {
        if(forElement.getPaddingTopRaw().needsRecalculation()) return null;

        if(forElement.getLocalPositionXRaw().needsRecalculation()) return null;
        if(forElement.getPaddingRightRaw().needsRecalculation()) return null;

        float calculatedVal = forElement.getLocalPositionX() - forElement.getPaddingRight();

        calculatedVal += forElement.getOffsetY();
        calculatedVal += forElement.getPaddingTop();

        return calculatedVal;
    }

    //endregion

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        // TODO
        throw new UnsupportedOperationException("MirrorPosition does not support makeEditorFor without a property.");
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        // TODO
        throw new UnsupportedOperationException("MirrorPosition does not support makeEditorFor with a property.");
    }

    //endregion

    //region Utility

    @Override
    public AbstractPosition cpy() {
        MirrorPosition mirror = new MirrorPosition();
        mirror.copyFrom(this);
        return mirror;
    }

    //endregion

    //endregion
}
