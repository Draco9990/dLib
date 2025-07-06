package dLib.ui;

import basemod.Pair;
import dLib.ui.elements.UIElement;
import dLib.util.DLibLogger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Supplier;

public class ElementCalculationManager {
    public static void calculate(UIElement topElement){
        ArrayList<UIElement> updatedElements = calculateElementPositionAndDimension(topElement);

        for (UIElement element : updatedElements){
            element.ensureElementWithinBounds();
        }
    }

    private static ArrayList<UIElement> calculateElementPositionAndDimension(UIElement topElement){
        ArrayList<UIElement> elementsThatNeedUpdate = new ArrayList<>();
        ArrayList<Pair<Integer, ElementCalculationInstruction>> calculationInstructions = new ArrayList<>();
        for (UIElement element : topElement.getHierarchyForUpdateOrder()){
            ArrayList<Pair<Integer, ElementCalculationInstruction>> childInstructions = element.collectCalculationInstructions();
            if(!childInstructions.isEmpty()){
                elementsThatNeedUpdate.add(element);
            }

            calculationInstructions.addAll(childInstructions);
        }

        calculationInstructions.sort(Comparator.comparingInt(Pair::getKey));

        while(!calculationInstructions.isEmpty()){
            boolean somethingChanged = false;

            Iterator<Pair<Integer, ElementCalculationInstruction>> iterator2 = calculationInstructions.iterator();
            while(iterator2.hasNext()){
                Pair<Integer, ElementCalculationInstruction> pair = iterator2.next();

                boolean canCalculate = true;
                for (Supplier<Boolean> prerequisite : pair.getValue().prerequisites){
                    boolean prerequisiteResult = prerequisite.get();
                    if(!prerequisiteResult){
                        canCalculate = false;
                        break;
                    }
                }

                if(canCalculate){
                    pair.getValue().calculateFormula.run();
                    iterator2.remove();
                    somethingChanged = true;
                }
            }

            if(!somethingChanged){
                //throw new RuntimeException("Circular dependency detected in element calculation");
                int i = 0;
            }
        }

        return elementsThatNeedUpdate;
    }

    public static class ElementCalculationInstruction{
        private Runnable calculateFormula;
        private Supplier<Boolean>[] prerequisites;

        public ElementCalculationInstruction(Runnable calculateFormula, Supplier<Boolean>... prerequisites){
            this.calculateFormula = calculateFormula;
            this.prerequisites = prerequisites;
        }
    }
}
