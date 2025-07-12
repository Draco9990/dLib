package dLib.modcompat.saythespire;

import sayTheSpire.ui.mod.Context;

public class DLibSayTheSpireContext extends Context {
    @Override
    public Boolean getAllowVirtualInput() {
        return false;
    }
}
