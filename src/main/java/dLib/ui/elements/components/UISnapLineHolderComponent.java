package dLib.ui.elements.components;

import dLib.ui.elements.UIElement;

public class UISnapLineHolderComponent extends AbstractUIElementComponent<UIElement> {
    @Override
    public void onRegisterComponent(UIElement owner) {
        super.onRegisterComponent(owner);
    }

    @Override
    public void onUnregisterComponent(UIElement owner) {
        super.onUnregisterComponent(owner);
    }

    private static class SnapLine {
        private int localPos;
        private Type type;

        public SnapLine(int localPos, Type type){
            this.localPos = localPos;
            this.type = type;
        }

        public enum Type{
            LEFT,
            H_CENTER,
            RIGHT,

            BOTTOM,
            V_CENTER,
            TOP
        }
    }
}
