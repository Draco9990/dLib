package dLib.ui;

import basemod.Pair;
import dLib.ui.elements.UIElement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Supplier;

public class ElementCalculationManager {
    public static void calculate(UIElement topElement){
        ArrayList<UIElement> elementsThatNeedUpdate = new ArrayList<>(topElement.getHierarchyForUpdateOrder());

        while(!elementsThatNeedUpdate.isEmpty()){
            elementsThatNeedUpdate.removeIf(UIElement::calculationPass);
        }

        // TODO
        /*for (UIElement element : updatedElements){
            element.ensureElementWithinBounds();
        }*/
    }
}
