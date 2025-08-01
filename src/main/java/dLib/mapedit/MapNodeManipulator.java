package dLib.mapedit;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import com.megacrit.cardcrawl.vfx.FadeWipeParticle;
import com.megacrit.cardcrawl.vfx.MapCircleEffect;
import dLib.util.Reflection;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Supplier;

public class MapNodeManipulator {
    //region Variables

    private static final float ORIG_SPACING_X = 128.0F * Settings.xScale;
    private static final float ORIG_MAP_DST_Y = 150.0F * Settings.scale;

    //endregion Variables

    //region Methods

    //region Node Grid Edit

    public static void resetMapRowsAndColumns(){
        Reflection.setFieldValue("SPACING_X", MapRoomNode.class, ORIG_SPACING_X);
        Settings.MAP_DST_Y = ORIG_MAP_DST_Y;
    }

    public static void setMapColumnAmount(int columnAmount){
        float multiplier = 7.f / columnAmount;
        Reflection.setFieldValue("SPACING_X", MapRoomNode.class, ORIG_SPACING_X * multiplier);
    }

    public static void setMapRowAmount(int rowAmount){
        float multiplier = 15.f / rowAmount;
        Settings.MAP_DST_Y = ORIG_MAP_DST_Y * multiplier;
    }

    //endregion Node Grid Edit

    //region Node Navigation

    public static void markAsEntryNode(MapRoomNode node){
        Patches.NodeFields.isEntryNode.set(node, true);
    }
    public static void unmarkAsEntryNode(MapRoomNode node){
        Patches.NodeFields.isEntryNode.set(node, false);
    }
    public static boolean isEntryNode(MapRoomNode node){
        return Patches.NodeFields.isEntryNode.get(node);
    }

    public static void markAsPreBossNode(MapRoomNode node){
        Patches.NodeFields.isPreBossNode.set(node, true);
    }
    public static void unmarkAsPreBossNode(MapRoomNode node){
        Patches.NodeFields.isPreBossNode.set(node, false);
    }
    public static boolean isPreBossNode(MapRoomNode node){
        return Patches.NodeFields.isPreBossNode.get(node);
    }

    public static void connectNodes(MapRoomNode from, MapRoomNode to){
        MapEdge connectingEdge = new MapEdge(from.x, from.y, from.offsetX, from.offsetY, to.x, to.y, to.offsetX, to.offsetY, false);
        from.addEdge(connectingEdge);
        addParent(to, from);
        addChild(from, to, connectingEdge);
    }

    private static void addParent(MapRoomNode node, MapRoomNode parent){
        Patches.NodeFields.parents.get(node).add(parent);
    }

    private static void addChild(MapRoomNode node, MapRoomNode child, MapEdge connectingEdge){
        Patches.NodeFields.children.get(node).add(new NodeEdgePair(child, connectingEdge));
    }

    //endregion Node Navigation

    //region Boss Room Override

    public static void setBossRoomOverride(AbstractDungeon dungeon, Supplier<AbstractRoom> roomSupplier){
        Patches.DungeonFields.bossRoomOverride.set(dungeon, roomSupplier);
    }
    public static AbstractRoom generateBossRoom(AbstractDungeon dungeon){
        return Patches.DungeonFields.bossRoomOverride.get(dungeon).get();
    }

    //endregion Boss Room Override

    //endregion Methods

    public static class NodeEdgePair{
        public MapRoomNode node;
        public MapEdge edge;

        public NodeEdgePair(MapRoomNode node, MapEdge edge){
            this.node = node;
            this.edge = edge;
        }
    }

    //region Patches

    public static class Patches{
        @SpirePatch2(clz = MapRoomNode.class, method = SpirePatch.CLASS)
        public static class NodeFields{
            public static SpireField<ArrayList<MapRoomNode>> parents = new SpireField<>(ArrayList::new);
            public static SpireField<ArrayList<NodeEdgePair>> children = new SpireField<>(ArrayList::new);

            public static SpireField<Boolean> isEntryNode = new SpireField<>(() -> false);
            public static SpireField<Boolean> isPreBossNode = new SpireField<>(() -> false);
        }

        @SpirePatch2(clz = AbstractDungeon.class, method = SpirePatch.CLASS)
        public static class DungeonFields{
            public static SpireField<Supplier<AbstractRoom>> bossRoomOverride = new SpireField<>(() -> MonsterRoomBoss::new);
        }

        @SpirePatch2(clz = MapRoomNode.class, method = SpirePatch.CONSTRUCTOR)
        public static class ConstructorPatch{
            @SpirePostfixPatch
            public static void Postfix(MapRoomNode __instance, int x, int y){
                if(__instance.y == 0){
                    markAsEntryNode(__instance);
                }
                if(__instance.y == 14 || (AbstractDungeon.id.equals("TheEnding") && __instance.y == 2)){
                    markAsPreBossNode(__instance);
                }
            }
        }

        @SpirePatch2(clz = MapRoomNode.class, method = "isConnectedTo")
        public static class IsConnectedToPatch{
            public static SpireReturn<Boolean> Prefix(MapRoomNode __instance, MapRoomNode node){
                for(NodeEdgePair nodeEdgePair : NodeFields.children.get(__instance)){
                    if(nodeEdgePair.node.equals(node)){
                        return SpireReturn.Return(true);
                    }
                }

                return SpireReturn.Continue();
            }
        }
        @SpirePatch2(clz = MapRoomNode.class, method = "wingedIsConnectedTo")
        public static class WingedIsConnectedToPatch{
            public static SpireReturn<Boolean> Prefix(MapRoomNode __instance, MapRoomNode node){
                for(NodeEdgePair nodeEdgePair : NodeFields.children.get(__instance)){
                    if(nodeEdgePair.node.equals(node)){
                        return SpireReturn.Return(true);
                    }
                }

                return SpireReturn.Continue();
            }
        }

        @SpirePatch2(clz = MapRoomNode.class, method = "getEdgeConnectedTo")
        public static class GetEdgeConnectedToPatch{
            public static SpireReturn<MapEdge> Prefix(MapRoomNode __instance, MapRoomNode node){
                for(NodeEdgePair nodeEdgePair : NodeFields.children.get(__instance)){
                    if(nodeEdgePair.node.equals(node)){
                        return SpireReturn.Return(nodeEdgePair.edge);
                    }
                }

                return SpireReturn.Continue();
            }
        }

        @SpirePatch2(clz = MapRoomNode.class, method = "leftNodeAvailable")
        public static class leftNodeAvailablePatch{
            public static SpireReturn<Boolean> Prefix(MapRoomNode __instance){
                for(NodeEdgePair nodeEdgePair : NodeFields.children.get(__instance)){
                    if(nodeEdgePair.node.x < __instance.x){
                        return SpireReturn.Return(true);
                    }
                }

                return SpireReturn.Continue();
            }
        }
        @SpirePatch2(clz = MapRoomNode.class, method = "rightNodeAvailable")
        public static class rightNodeAvailablePatch{
            public static SpireReturn<Boolean> Prefix(MapRoomNode __instance){
                for(NodeEdgePair nodeEdgePair : NodeFields.children.get(__instance)){
                    if(nodeEdgePair.node.x > __instance.x){
                        return SpireReturn.Return(true);
                    }
                }

                return SpireReturn.Continue();
            }
        }
        @SpirePatch2(clz = MapRoomNode.class, method = "centerNodeAvailable")
        public static class centerNodeAvailablePatch{
            public static SpireReturn<Boolean> Prefix(MapRoomNode __instance){
                for(NodeEdgePair nodeEdgePair : NodeFields.children.get(__instance)){
                    if(nodeEdgePair.node.x == __instance.x){
                        return SpireReturn.Return(true);
                    }
                }

                return SpireReturn.Continue();
            }
        }

        @SpirePatch(clz = DungeonMap.class, method = "update")
        public static class BossRoomPatcher {
            @SpireInsertPatch(locator = Locator.class)
            public static void Insert(DungeonMap __instance){
                if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMPLETE &&
                        AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP &&
                        (Settings.isDebug || isPreBossNode(AbstractDungeon.getCurrMapNode())) &&
                        __instance.bossHb.hovered && (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed())) {
                    AbstractDungeon.getCurrMapNode().taken = true;
                    MapRoomNode node2 = AbstractDungeon.getCurrMapNode();
                    Iterator var2 = node2.getEdges().iterator();

                    while(var2.hasNext()) {
                        MapEdge e = (MapEdge)var2.next();
                        if (e != null) {
                            e.markAsTaken();
                        }
                    }

                    InputHelper.justClickedLeft = false;
                    CardCrawlGame.music.fadeOutTempBGM();
                    MapRoomNode node = new MapRoomNode(-1, 15);
                    node.room = generateBossRoom(CardCrawlGame.dungeon);
                    AbstractDungeon.nextRoom = node;
                    if (AbstractDungeon.pathY.size() > 1) {
                        AbstractDungeon.pathX.add(AbstractDungeon.pathX.get(AbstractDungeon.pathX.size() - 1));
                        AbstractDungeon.pathY.add(AbstractDungeon.pathY.get(AbstractDungeon.pathY.size() - 1) + 1);
                    } else {
                        AbstractDungeon.pathX.add(1);
                        AbstractDungeon.pathY.add(15);
                    }

                    AbstractDungeon.nextRoomTransitionStart();
                }
                else if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMPLETE && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP && (Settings.isDebug || AbstractDungeon.getCurrMapNode().y == 14 || AbstractDungeon.id.equals("TheEnding") && AbstractDungeon.getCurrMapNode().y == 2) && __instance.bossHb.hovered && (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed())){
                    InputHelper.justClickedLeft = false;
                    CInputActionSet.select.justPressed = false;
                }
            }

            private static class Locator extends SpireInsertLocator {
                public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                    Matcher finalMatcher = new Matcher.MethodCallMatcher(DungeonMap.class, "updateReticle");
                    return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                }
            }
        }

        @SpirePatch(clz = MapRoomNode.class, method = "update")
        public static class EntryRoomPatcher {
            @SpireEnum
            public static AbstractRoom.RoomPhase PROXY;

            @SpireInsertPatch(locator = Locator.class)
            public static void Insert(MapRoomNode __instance){
                if (!AbstractDungeon.firstRoomChosen && isEntryNode(__instance) && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMPLETE) {
                    if (__instance.hb.hovered) {
                        if (__instance.hb.justHovered) {
                            Reflection.invokeMethod("playNodeHoveredSound", __instance);
                        }

                        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP && (CInputActionSet.select.isJustPressed() || AbstractDungeon.dungeonMapScreen.clicked)) {
                            Reflection.invokeMethod("playNodeSelectedSound", __instance);
                            AbstractDungeon.dungeonMapScreen.clicked = false;
                            AbstractDungeon.dungeonMapScreen.clickTimer = 0.0F;
                            AbstractDungeon.dungeonMapScreen.dismissable = true;
                            if (!AbstractDungeon.firstRoomChosen) {
                                AbstractDungeon.firstRoomChosen = true;
                            }

                            float SPACING_X = Reflection.getFieldValue("SPACING_X", MapRoomNode.class);
                            float OFFSET_Y = Reflection.getFieldValue("OFFSET_Y", MapRoomNode.class);
                            float angle = Reflection.getFieldValue("OFFSET_X", __instance);

                            AbstractDungeon.topLevelEffects.add(new MapCircleEffect((float)__instance.x * SPACING_X + MapRoomNode.OFFSET_X + __instance.offsetX, (float)__instance.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY + __instance.offsetY, angle));
                            AbstractDungeon.topLevelEffects.add(new FadeWipeParticle());

                            Reflection.setFieldValue("animWaitTimer", __instance, 0.25F);
                        }

                        __instance.highlighted = true;
                    } else {
                        __instance.color = MapRoomNode.AVAILABLE_COLOR.cpy();
                    }
                }
                else if(!AbstractDungeon.firstRoomChosen && !isEntryNode(__instance) && __instance.y == 0){
                    __instance.y = -69420;
                }
            }

            @SpirePostfixPatch
            public static void Postfix(MapRoomNode __instance) {
                if(__instance.y == -69420){
                    __instance.y = 0;
                }
            }

            private static class Locator extends SpireInsertLocator {
                public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                    Matcher finalMatcher = new Matcher.MethodCallMatcher(Hitbox.class, "update");
                    return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                }
            }
        }
    }

    //endregion Patches
}
