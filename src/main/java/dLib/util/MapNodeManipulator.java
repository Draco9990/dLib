package dLib.util;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;

import java.util.ArrayList;

public class MapNodeManipulator {
    public static class NodeEdgePair{
        public MapRoomNode node;
        public MapEdge edge;

        public NodeEdgePair(MapRoomNode node, MapEdge edge){
            this.node = node;
            this.edge = edge;
        }
    }

    @SpirePatch2(clz = MapRoomNode.class, method = "<class>")
    public static class NodeFields{
        public static SpireField<Boolean> isCustom = new SpireField<>(() -> false);

        public static SpireField<ArrayList<MapRoomNode>> parents = new SpireField<>(ArrayList::new);
        public static SpireField<ArrayList<NodeEdgePair>> children = new SpireField<>(ArrayList::new);
    }

    public static void connectNodes(MapRoomNode from, MapRoomNode to){
        MapEdge connectingEdge = new MapEdge(from.x, from.y, from.offsetX, from.offsetY, to.x, to.y, to.offsetX, to.offsetY, false);
        from.addEdge(connectingEdge);
        addParent(to, from);
        addChild(from, to, connectingEdge);
    }

    private static void addParent(MapRoomNode node, MapRoomNode parent){
        NodeFields.isCustom.set(node, true);
        NodeFields.parents.get(node).add(parent);
    }

    private static void addChild(MapRoomNode node, MapRoomNode child, MapEdge connectingEdge){
        NodeFields.isCustom.set(node, true);
        NodeFields.children.get(node).add(new NodeEdgePair(child, connectingEdge));
    }

    /** Patches */
    public static class Patches{
        @SpirePatch2(clz = MapRoomNode.class, method = "isConnectedTo")
        public static class IsConnectedToPatch{
            public static SpireReturn<Boolean> Prefix(MapRoomNode __instance, MapRoomNode node){
                if(NodeFields.isCustom.get(__instance)){
                    for(NodeEdgePair nodeEdgePair : NodeFields.children.get(__instance)){
                        if(nodeEdgePair.node.equals(node)){
                            return SpireReturn.Return(true);
                        }
                    }
                }

                return SpireReturn.Continue();
            }
        }
        @SpirePatch2(clz = MapRoomNode.class, method = "wingedIsConnectedTo")
        public static class WingedIsConnectedToPatch{
            public static SpireReturn<Boolean> Prefix(MapRoomNode __instance, MapRoomNode node){
                if(NodeFields.isCustom.get(__instance)){
                    for(NodeEdgePair nodeEdgePair : NodeFields.children.get(__instance)){
                        if(nodeEdgePair.node.equals(node)){
                            return SpireReturn.Return(true);
                        }
                    }
                }

                return SpireReturn.Continue();
            }
        }

        @SpirePatch2(clz = MapRoomNode.class, method = "getEdgeConnectedTo")
        public static class GetEdgeConnectedToPatch{
            public static SpireReturn<MapEdge> Prefix(MapRoomNode __instance, MapRoomNode node){
                if(NodeFields.isCustom.get(__instance)){
                    for(NodeEdgePair nodeEdgePair : NodeFields.children.get(__instance)){
                        if(nodeEdgePair.node.equals(node)){
                            return SpireReturn.Return(nodeEdgePair.edge);
                        }
                    }
                }

                return SpireReturn.Continue();
            }
        }

        @SpirePatch2(clz = MapRoomNode.class, method = "leftNodeAvailable")
        public static class leftNodeAvailablePatch{
            public static SpireReturn<Boolean> Prefix(MapRoomNode __instance){
                if(NodeFields.isCustom.get(__instance)){
                    for(NodeEdgePair nodeEdgePair : NodeFields.children.get(__instance)){
                        if(nodeEdgePair.node.x < __instance.x){
                            return SpireReturn.Return(true);
                        }
                    }
                }

                return SpireReturn.Continue();
            }
        }

        @SpirePatch2(clz = MapRoomNode.class, method = "centerNodeAvailable")
        public static class centerNodeAvailablePatch{
            public static SpireReturn<Boolean> Prefix(MapRoomNode __instance){
                if(NodeFields.isCustom.get(__instance)){
                    for(NodeEdgePair nodeEdgePair : NodeFields.children.get(__instance)){
                        if(nodeEdgePair.node.x == __instance.x){
                            return SpireReturn.Return(true);
                        }
                    }
                }

                return SpireReturn.Continue();
            }
        }

        @SpirePatch2(clz = MapRoomNode.class, method = "rightNodeAvailable")
        public static class rightNodeAvailablePatch{
            public static SpireReturn<Boolean> Prefix(MapRoomNode __instance){
                if(NodeFields.isCustom.get(__instance)){
                    for(NodeEdgePair nodeEdgePair : NodeFields.children.get(__instance)){
                        if(nodeEdgePair.node.x > __instance.x){
                            return SpireReturn.Return(true);
                        }
                    }
                }

                return SpireReturn.Continue();
            }
        }
    }
}
