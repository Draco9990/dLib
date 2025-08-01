package dLib.mapedit;

import com.megacrit.cardcrawl.map.MapRoomNode;

public class CustomMapRoomNode extends MapRoomNode {
    public CustomMapRoomNode(int x, int y, boolean startingNode, boolean preBossNode) {
        super(x, y);

        if(startingNode){
            MapNodeManipulator.markAsEntryNode(this);
        }
        if(preBossNode){
            MapNodeManipulator.markAsPreBossNode(this);
        }
    }

    public void createPathTo(MapRoomNode other){
        MapNodeManipulator.connectNodes(this, other);
    }
}
