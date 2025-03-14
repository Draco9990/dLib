package dLib.ui;

import basemod.Pair;
import dLib.ui.descriptors.ElementDescriptor;
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
            if(element.needsLocalPositionXRecalculation()) {
                calculationInstructions.add(element.getLocalPositionXRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getLocalPositionXRaw().getCalculationInstruction(element); //* For Debug
                }
            }

            if(element.needsLocalPositionYRecalculation()){
                calculationInstructions.add(element.getLocalPositionYRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getLocalPositionYRaw().getCalculationInstruction(element); //* For Debug
                }
            }

            if(element.getCalculatedWidth() == null) {
                calculationInstructions.add(element.getWidthRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getWidthRaw().getCalculationInstruction(element); //* For Debug
                }
            }

            if(element.getCalculatedHeight() == null) {
                calculationInstructions.add(element.getHeightRaw().getCalculationInstruction(element));
                if(!elementsThatNeedUpdate.contains(element)) elementsThatNeedUpdate.add(element);

                if(calculationInstructions.get(calculationInstructions.size() - 1) == null){
                    element.getHeightRaw().getCalculationInstruction(element); //* For Debug
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
                throw new RuntimeException("Circular dependency detected in element calculation");
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
