package dLib.debug;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.monsters.exordium.TheGuardian;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.TheEndingScene;
import dLib.mapedit.MapNodeManipulator;
import dLib.util.Reflection;

import java.util.ArrayList;

public class PlaygroundDungeon extends AbstractDungeon {
    public static final String DUNGEON_ID = "Playground";

    public PlaygroundDungeon(AbstractPlayer p) {
        super(name, DUNGEON_ID, p, new ArrayList<>());
        mapRng = new Random(Settings.seed + (long)(AbstractDungeon.actNum * 300));

        this.initializeDungeon();

        AbstractDungeon.currMapNode = new MapRoomNode(0, -1);
        AbstractDungeon.currMapNode.room = (AbstractRoom)new EmptyRoom();

        fadeIn();
    }

    public PlaygroundDungeon(AbstractPlayer p, SaveFile saveFile) {
        super(DUNGEON_ID, p, saveFile);
        miscRng = new Random(Settings.seed + (long)saveFile.floor_num);
        mapRng = new Random(Settings.seed + (long)(saveFile.act_num * 300));

        this.initializeDungeon();

        firstRoomChosen = true;
        this.populatePathTaken(saveFile);
    }

    public void initializeDungeon(){
        if (scene != null) {
            scene.dispose();
        }

        scene = new TheEndingScene();
        fadeColor = Color.valueOf("0a1e1eff");
        sourceFadeColor = Color.valueOf("0a1e1eff");

        this.initializeLevelSpecificChances();

        generateSpecialMap();

        CardCrawlGame.music.changeBGM(TheCity.ID);
    }

    private void generateSpecialMap(){
        map = new ArrayList<>();

        int playerCount = 11;
        MapNodeManipulator.setMapColumnAmount((int) Math.ceil(playerCount * 0.5f));

        ArrayList<MapRoomNode> previousRowRooms = new ArrayList<>();

        int remainingRoomCount = (int) Math.ceil(playerCount * 0.5f);
        int depth = 0;
        while(remainingRoomCount > 1){
            int roomsInRow = remainingRoomCount;

            ArrayList<MapRoomNode> rowRooms = new ArrayList<>();
            for(int i = 0; i < roomsInRow; i++){
                MapRoomNode fightRoom = new MapRoomNode(i, depth);
                fightRoom.room = new MonsterRoom();
                rowRooms.add(fightRoom);

                if(!previousRowRooms.isEmpty()){
                    MapRoomNode previousRoom = previousRowRooms.remove(0);

                    float SPACING_X = Reflection.getFieldValue("SPACING_X", MapRoomNode.class);
                    fightRoom.x = previousRoom.x;
                    fightRoom.offsetX += SPACING_X * 0.5f;

                    MapNodeManipulator.connectNodes(previousRoom, fightRoom);
                }
                if(!previousRowRooms.isEmpty()){
                    MapRoomNode previousRoom = previousRowRooms.remove(0);
                    MapNodeManipulator.connectNodes(previousRoom, fightRoom);
                }
            }
            map.add(new ArrayList<>(rowRooms));

            previousRowRooms = new ArrayList<>(rowRooms);

            remainingRoomCount = Math.round(remainingRoomCount * 0.5f);
            depth++;
        }

        ArrayList<MapRoomNode> bossRow = new ArrayList<>();
        MapRoomNode bossRoom = new MapRoomNode((int) Math.ceil(playerCount * 0.25f), 14);
        bossRoom.room = new EventRoom();

        // ! - If a room doesn't have an edge, it won't render on the map. Create a blank empty edge to ensure paths to it render
        bossRoom.addEdge(new MapEdge(-1, -1, -1, -1));

        // ! - The way the final boss works is by generating a "room" behind the actual boss icon to render a path to it, but it's not an actual room
        // ! - Actual boss 'node' is bosshb in DungeonMap.class, and it's set to trigger for specific room .y values only

        bossRow.add(bossRoom);
        map.add(bossRow);

        for(MapRoomNode lastRowNode : previousRowRooms){
            MapNodeManipulator.markAsPreBossNode(lastRowNode);
            MapNodeManipulator.connectNodes(lastRowNode, bossRoom);
        }
    }

    @Override
    protected void initializeLevelSpecificChances() {
        eventRoomChance = 1.0F;
    }

    @Override
    protected ArrayList<String> generateExclusions() {
        return null;
    }

    @Override
    protected void generateMonsters() {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo("Cultist", 2.0F));
        monsters.add(new MonsterInfo("Jaw Worm", 2.0F));
        monsters.add(new MonsterInfo("2 Louse", 2.0F));
        monsters.add(new MonsterInfo("Small Slimes", 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, 3, false);
    }

    @Override
    protected void generateWeakEnemies(int i) {

    }

    @Override
    protected void generateStrongEnemies(int i) {

    }

    @Override
    protected void generateElites(int i) {

    }

    @Override
    protected void initializeBoss() {
        bossList.add(TheGuardian.ID);
    }

    @Override
    protected void initializeEventList() {
        eventList.add("Bonfire Elementals");
    }

    @Override
    protected void initializeEventImg() {
        if (eventBackgroundImg != null) {
            eventBackgroundImg.dispose();
            eventBackgroundImg = null;
        }

        eventBackgroundImg = ImageMaster.loadImage("images/ui/event/panel.png");
    }

    @Override
    protected void initializeShrineList() {

    }

    /*@SpirePatch2(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = {String.class, AbstractPlayer.class})
    public static class DungeonLoader {
        public static SpireReturn<AbstractDungeon> Prefix(AbstractPlayer p){
            CardCrawlGame.nextDungeon = DUNGEON_ID;
            return SpireReturn.Return(new PlaygroundDungeon(p));
        }
    }

    @SpirePatch2(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = {String.class, AbstractPlayer.class, SaveFile.class})
    public static class Test2{
        public static SpireReturn<AbstractDungeon> DungeonLoaderFromSave(AbstractPlayer p, SaveFile saveFile){
            return SpireReturn.Return(new PlaygroundDungeon(p, saveFile));
        }
    }*/
}
