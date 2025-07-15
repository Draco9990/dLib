package dLib.commands.objects;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import dLib.ui.elements.items.hierarchyviewer.ControllerSelectableHierarchyViewer;
import dLib.ui.elements.items.hierarchyviewer.HierarchyViewerPopup;
import dLib.ui.UIManager;

public class CSHirearchyViewerOpenerCommand extends ConsoleCommand {

    public CSHirearchyViewerOpenerCommand() {
        this.requiresPlayer = false;
        this.maxExtraTokens = 0;
        this.simpleCheck = true;
    }

    public void execute(String[] tokens, int depth) {
        if(UIManager.getOpenElementOfType(HierarchyViewerPopup.class) == null) {
            new HierarchyViewerPopup(new ControllerSelectableHierarchyViewer()).open();
        }
        else{
            UIManager.getOpenElementOfType(HierarchyViewerPopup.class).dispose();
        }
    }

    public void errorMsg() {
        cmdDrawHelp();
    }

    private static void cmdDrawHelp() {
        DevConsole.couldNotParse();
    }
}