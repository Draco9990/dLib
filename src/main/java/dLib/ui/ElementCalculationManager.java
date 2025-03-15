package dLib.ui;

import basemod.Pair;
import dLib.ui.elements.UIElement;

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
            if(element.getLocalPositionXRaw().needsRecalculation()) {
                calculationInstructions.add(element.getLocalPositionXRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getLocalPositionXRaw().getCalculationInstruction(element); //* For Debug
                }
            }
            if(element.getLocalPositionYRaw().needsRecalculation()){
                calculationInstructions.add(element.getLocalPositionYRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getLocalPositionYRaw().getCalculationInstruction(element); //* For Debug
                }
            }

            if(element.getMinimumWidthRaw().needsRecalculation()) {
                calculationInstructions.add(element.getMinimumWidthRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getMinimumWidthRaw().getCalculationInstruction(element); //* For Debug
                }
            }
            if(element.getMinimumHeightRaw().needsRecalculation()) {
                calculationInstructions.add(element.getMinimumHeightRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getMinimumHeightRaw().getCalculationInstruction(element); //* For Debug
                }
            }

            if(element.getMaximumWidthRaw().needsRecalculation()) {
                calculationInstructions.add(element.getMaximumWidthRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getMaximumWidthRaw().getCalculationInstruction(element); //* For Debug
                }
            }
            if(element.getMaximumHeightRaw().needsRecalculation()) {
                calculationInstructions.add(element.getMaximumHeightRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getMaximumHeightRaw().getCalculationInstruction(element); //* For Debug
                }
            }

            if(element.needsWidthCalculation()) {
                calculationInstructions.add(element.getWidthRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getWidthRaw().getCalculationInstruction(element); //* For Debug
                }
            }
            if(element.needsHeightCalculation()) {
                calculationInstructions.add(element.getHeightRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getHeightRaw().getCalculationInstruction(element); //* For Debug
                }
            }

            if(element.getPaddingLeftRaw().needsRecalculation()){
                calculationInstructions.add(element.getPaddingLeftRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getPaddingLeftRaw().getCalculationInstruction(element); //* For Debug
                }
            }
            if(element.getPaddingBottomRaw().needsRecalculation()){
                calculationInstructions.add(element.getPaddingBottomRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getPaddingBottomRaw().getCalculationInstruction(element); //* For Debug
                }
            }
            if(element.getPaddingRightRaw().needsRecalculation()){
                calculationInstructions.add(element.getPaddingRightRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getPaddingRightRaw().getCalculationInstruction(element); //* For Debug
                }
            }
            if(element.getPaddingTopRaw().needsRecalculation()){
                calculationInstructions.add(element.getPaddingTopRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getPaddingTopRaw().getCalculationInstruction(element); //* For Debug
                }
            }
        }

        calculationInstructions.sort(Comparator.comparingInt(Pair::getKey));

        while(!calculationInstructions.isEmpty()){
            boolean somethingChanged = false;

            Iterator<Pair<Integer, ElementCalculationInstruction>> iterator2 = calculationInstructions.iterator();
            while(iterator2.hasNext()){
                Pair<Integer, ElementCalculationInstruction> pair = iterator2.next();

                boolean canCalculate = true;
                for (Supplier<Boolean> prerequisite : pair.getValue().prerequisites){
                    if(!prerequisite.get()){
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
                int j = 0;
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
