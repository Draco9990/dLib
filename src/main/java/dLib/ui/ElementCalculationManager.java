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

        Iterator<UIElement> iterator;

        boolean calculatedSomething;
        do{
            calculatedSomething = false;
            iterator = elementsThatNeedUpdate.iterator();
            while(iterator.hasNext()){
                UIElement next = iterator.next();

                Pair<Boolean, Boolean> calcResult = next.calculationPass(CalculationPass.FIRST);
                if(calcResult.getKey()){
                    iterator.remove();
                    continue;
                }

                calculatedSomething |= calcResult.getValue();
            }
        }while(calculatedSomething && !elementsThatNeedUpdate.isEmpty());

        do{
            calculatedSomething = false;
            iterator = elementsThatNeedUpdate.iterator();
            while(iterator.hasNext()){
                UIElement next = iterator.next();

                Pair<Boolean, Boolean> calcResult = next.calculationPass(CalculationPass.SECOND);
                if(calcResult.getKey()){
                    iterator.remove();
                    continue;
                }

                calculatedSomething |= calcResult.getValue();
            }
        }while(calculatedSomething && !elementsThatNeedUpdate.isEmpty());


        while(!elementsThatNeedUpdate.isEmpty()){
            iterator = elementsThatNeedUpdate.iterator();
            while(iterator.hasNext()){
                UIElement next = iterator.next();

                Pair<Boolean, Boolean> calcResult = next.calculationPass(CalculationPass.THIRD);
                if(calcResult.getKey()){
                    iterator.remove();
                }
            }
        }

        // TODO
        /*for (UIElement element : updatedElements){
            element.ensureElementWithinBounds();
        }*/
    }

    public enum CalculationPass {
        FIRST,
        SECOND,
        THIRD,
    }
}
