package dLib.tools.screeneditor.util;

public class ScreenEditorProperties {
    //region Grid

    private boolean grid = false;

    public void toggleGrid(){
        grid = !grid;
    }
    public boolean isGridOn(){
        return grid;
    }

    //endregion
}
