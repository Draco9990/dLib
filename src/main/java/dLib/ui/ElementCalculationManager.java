package dLib.ui;

import basemod.Pair;
import dLib.ui.elements.UIElement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Supplier;

public class ElementCalculationManager {
    public static void calculateElementPositionAndDimension(UIElement topElement){
        ArrayList<UIElement> elements = topElement.getHierarchyForUpdateOrder();
        Iterator<UIElement> iterator = elements.iterator();
        while(iterator.hasNext()){
            UIElement element = iterator.next();
            if(element.getCalculatedWidth() != null && element.getCalculatedHeight() != null &&
               element.getCalculatedLocalPositionX() != null && element.getCalculatedLocalPositionY() != null){
                iterator.remove();
            }
        }

        ArrayList<Pair<Integer, ElementCalculationInstruction>> calculationInstructions = new ArrayList<>();
        for (UIElement element : topElement.getHierarchyForUpdateOrder()){
            if(element.getCalculatedLocalPositionX() == null) calculationInstructions.add(element.getLocalPositionXRaw().getCalculationInstruction(element));
            if(!calculationInstructions.isEmpty() && calculationInstructions.get(calculationInstructions.size() - 1) == null){
                element.getLocalPositionXRaw().getCalculationInstruction(element);
            }
            if(element.getCalculatedLocalPositionY() == null) calculationInstructions.add(element.getLocalPositionYRaw().getCalculationInstruction(element));
            if(!calculationInstructions.isEmpty() && calculationInstructions.get(calculationInstructions.size() - 1) == null){
                element.getLocalPositionYRaw().getCalculationInstruction(element);
            }
            if(element.getCalculatedWidth() == null) calculationInstructions.add(element.getWidthRaw().getCalculationInstruction(element));
            if(!calculationInstructions.isEmpty() && calculationInstructions.get(calculationInstructions.size() - 1) == null){
                element.getWidthRaw().getCalculationInstruction(element);
            }
            if(element.getCalculatedHeight() == null) calculationInstructions.add(element.getHeightRaw().getCalculationInstruction(element));
            if(!calculationInstructions.isEmpty() && calculationInstructions.get(calculationInstructions.size() - 1) == null){
                element.getHeightRaw().getCalculationInstruction(element);
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
    }

    public static class ElementCalculationInstruction{
        private Runnable calculateFormula;
        private Supplier<Boolean>[] prerequisites;

        public ElementCalculationInstruction(Runnable calculateFormula, Supplier<Boolean>... prerequisites){
            this.calculateFormula = calculateFormula;
            this.prerequisites = prerequisites;
        }

        private enum CalculationType{
            POSITION_X,
            POSITION_Y,
            WIDTH,
            HEIGHT
        }
    }
}
