package dLib.util.ui.dimensions;

import dLib.properties.objects.DimensionProperty;
import dLib.properties.objects.templates.TProperty;
import dLib.properties.ui.elements.AbstractValueEditor;
import dLib.properties.ui.elements.MirrorDimensionValueEditor;
import dLib.ui.annotations.DisplayClass;
import dLib.ui.elements.UIElement;

import java.io.Serializable;

@DisplayClass(shortDisplayName = "mirror")
public class MirrorDimension extends AbstractDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    //region Constructors

    public MirrorDimension(){
        super();
    }

    //endregion

    //region Class Methods

    //region Calculation Methods

    @Override
    protected Float tryCalculateValue_Width(UIElement forElement) {
        if(forElement.getHeightRaw().needsRecalculation()) return null;
        registerDependency(forElement.getHeightRaw());
        if(forElement.getPaddingBottomRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingBottomRaw());
        if(forElement.getPaddingTopRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingTopRaw());

        if(forElement.getPaddingLeftRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingLeftRaw());
        if(forElement.getPaddingRightRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingRightRaw());

        float elementFullHeight = forElement.getHeight() + forElement.getPaddingTop() + forElement.getPaddingBottom();
        return elementFullHeight - forElement.getPaddingLeft() - forElement.getPaddingRight();
    }

    @Override
    protected Float tryCalculateValue_Height(UIElement forElement) {
        if(forElement.getWidthRaw().needsRecalculation()) return null;
        registerDependency(forElement.getWidthRaw());
        if(forElement.getPaddingLeftRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingLeftRaw());
        if(forElement.getPaddingRightRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingRightRaw());

        if(forElement.getPaddingTopRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingTopRaw());
        if(forElement.getPaddingBottomRaw().needsRecalculation()) return null;
        registerDependency(forElement.getPaddingBottomRaw());

        float elementFullWidth = forElement.getWidth() + forElement.getPaddingLeft() + forElement.getPaddingRight();
        return elementFullWidth - forElement.getPaddingTop() - forElement.getPaddingBottom();
    }

    //endregion

    //region Editor

    @Override
    public AbstractValueEditor makeEditorFor() {
        return new MirrorDimensionValueEditor(this);
    }

    @Override
    public AbstractValueEditor makeEditorFor(TProperty property) {
        return new MirrorDimensionValueEditor((DimensionProperty) property);
    }

    //endregion

    //region Utility

    @Override
    public AbstractDimension cpy() {
        MirrorDimension mirror = new MirrorDimension();
        mirror.copyFrom(this);
        return mirror;
    }

    //endregion

    //endregion
}
