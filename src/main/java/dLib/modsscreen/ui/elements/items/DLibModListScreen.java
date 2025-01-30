package dLib.modsscreen.ui.elements.items;

import basemod.ModBadge;
import basemod.ModPanel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import dLib.ui.Alignment;
import dLib.ui.animations.entry.UIAnimation_SlideInUp;
import dLib.ui.animations.exit.UIAnimation_SlideOutDown;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.DarkenLayer;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.buttons.CancelButton;
import dLib.ui.elements.items.itembox.HorizontalBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.itembox.VerticalDataBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.elements.items.wrappers.BaseModUIWrapper;
import dLib.ui.resources.UICommonResources;
import dLib.ui.util.ESelectionMode;
import dLib.util.Reflection;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class DLibModListScreen extends UIElement {
    private DarkenLayer darkenLayer;
    private DLibModListList modListList;
    private DLibModListModDetails modDetails;

    public DLibModListScreen() {
        super();

        darkenLayer = new DarkenLayer();
        addChild(darkenLayer);

        modListList = new DLibModListList();
        addChild(modListList);

        modDetails = new DLibModListModDetails();
        addChild(modDetails);

        CancelButton closeButton = new CancelButton();
        closeButton.label.setText("Return");
        closeButton.onLeftClickEvent.subscribeManaged(() -> getTopParent().close());
        addChild(closeButton);
    }

    private static class DLibModListList extends Renderable {
        public DLibModListList() {
            super(Tex.stat(UICommonResources.bg02_background), Pos.px(35), Pos.px(1080-819), Dim.px(531), Dim.px(735 + 50));

            setRenderColor(Color.valueOf("#2A4955"));

            setEntryAnimation(new UIAnimation_SlideInUp(this));
            setExitAnimation(new UIAnimation_SlideOutDown(this));

            Scrollbox scrollbox = new Scrollbox();
            scrollbox.setIsHorizontal(false);
            scrollbox.setPadding(Padd.px(25));
            {
                VerticalDataBox<ModFileExtended> modBadgeBox = new VerticalDataBox<ModFileExtended>(Dim.fill(), Dim.fill()){
                    @Override
                    public UIElement makeUIForItem(ModFileExtended item) {
                        return new ModBadgeWrapepr(item);
                    }
                };
                modBadgeBox.setSelectionMode(ESelectionMode.SINGLE_NOPERSIST);
                modBadgeBox.setChildren(getRegisteredMods());
                modBadgeBox.setTexture(UICommonResources.bg02_inner);
                modBadgeBox.setRenderColor(Color.valueOf("#1F272B"));
                modBadgeBox.onItemSelectionChangedEvent.subscribeManaged((items) -> {
                    if(items != null && !items.isEmpty()){
                        getParentOfType(DLibModListScreen.class).modDetails.buildModContentBox(items.get(0));
                    }
                });
                scrollbox.addChild(modBadgeBox);
            }
            addChild(scrollbox);
        }

        private static class ModBadgeWrapepr extends Renderable {
            private ModFileExtended modFile;

            public ModBadgeWrapepr(ModFileExtended modFile) {
                super(Tex.stat(UICommonResources.button03_square), Dim.fill(), Dim.px(50));

                this.modFile = modFile;

                Texture badgeIcon = Reflection.getFieldValue("texture", modFile.modBadge);
                if(badgeIcon != null){
                    Image badgeIconImage = new Image(Tex.stat(badgeIcon), Pos.px(16), Pos.px(10), Dim.px(30), Dim.px(30));
                    addChild(badgeIconImage);
                }

                TextBox modName = new TextBox(modFile.modInfo.Name, Pos.px(56), Pos.px(10), Dim.px(358), Dim.px(30));
                modName.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
                modName.setFontSize(18);
                addChild(modName);
            }
        }
    }

    private static class DLibModListModDetails extends Renderable {
        private Image modContentBox;

        private ModFileExtended currentModFile;

        public DLibModListModDetails() {
            super(Tex.stat(UICommonResources.bg02_background), Pos.px(599), Pos.px(1080-1038), Dim.px(1278), Dim.px(955 + 50));

            setRenderColor(Color.valueOf("#2A4955"));

            setEntryAnimation(new UIAnimation_SlideInUp(this));
            setExitAnimation(new UIAnimation_SlideOutDown(this));

            modContentBox = new Image(Tex.stat(UICommonResources.bg02_inner), Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
            modContentBox.setRenderColor(Color.valueOf("#1F272B"));
            modContentBox.setPadding(Padd.px(25));
            addChild(modContentBox);
        }

        public void buildModContentBox(ModFileExtended modFile){
            modContentBox.clearChildren();

            currentModFile = modFile;

            ImageTextBox titleBox = new ImageTextBox(modFile.modInfo.Name, Pos.px(0), Pos.px(905-110 + 50), Dim.fill(), Dim.px(100));
            titleBox.textBox.setFontSize(50);
            titleBox.textBox.setFont(FontHelper.cardEnergyFont_L);
            titleBox.textBox.setTextRenderColor(Color.valueOf("#FFEDA7"));
            titleBox.setPaddingHorizontal(Padd.px(10));
            modContentBox.addChild(titleBox);

            ImageTextBox versionBox = new ImageTextBox(modFile.modInfo.ModVersion.toString(), Pos.px(530), Pos.px(757 + 50), Dim.px(150), Dim.px(43));
            versionBox.textBox.setFontSize(25);
            versionBox.textBox.setFont(FontHelper.cardEnergyFont_L);
            modContentBox.addChild(versionBox);

            ImageTextBox descriptionBox = new ImageTextBox(modFile.modInfo.Description, Pos.px(0), Pos.px(905-335 + 50), Dim.fill(), Dim.px(160));
            descriptionBox.setPaddingHorizontal(Padd.px(10));
            descriptionBox.textBox.setWrap(true);
            descriptionBox.textBox.setContentAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.TOP);
            modContentBox.addChild(descriptionBox);

            HorizontalBox buttonsBox = new HorizontalBox(Pos.px(895), Pos.px(10), Dim.px(320), Dim.px(79));
            buttonsBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.RIGHT);
            {
                if(modFile.modBadge != null){
                    ModPanel modPanel = Reflection.getFieldValue("modPanel", modFile.modBadge);
                    if(modPanel != null){
                        Button configButton = new Button(Dim.px(80), Dim.px(79));
                        configButton.setTexture(Tex.stat(UICommonResources.settingsButton));
                        configButton.onLeftClickEvent.subscribe(configButton, () -> {
                            ModPanel modPanel1 = Reflection.getFieldValue("modPanel", currentModFile.modBadge);
                            ModPanelUIWrapper panelWrapper = new ModPanelUIWrapper(modPanel1);
                            panelWrapper.open();
                        });
                        buttonsBox.addChild(configButton);
                    }
                }

                Object steamUrlObject = Reflection.getFieldValue("steamWorkshopDetails", modFile.modInfo);
                if(steamUrlObject != null){
                    Object publishedFileID = Reflection.getFieldValue("publishedFileID", steamUrlObject);
                    if(publishedFileID != null){
                        String modPublicId = Reflection.invokeMethod("ToPublicID", publishedFileID);
                        if(modPublicId != null){
                            Button steamButton = new Button(Dim.px(80), Dim.px(79));
                            steamButton.setTexture(Tex.stat(UICommonResources.steamButton));
                            steamButton.onLeftClickEvent.subscribe(steamButton, () -> {
                                Desktop desktop = Desktop.getDesktop();
                                try {
                                    URI uri = new URI("https://steamcommunity.com/sharedfiles/filedetails/?id=" + modPublicId);
                                    desktop.browse(uri);
                                } catch (Exception ignored) {}
                            });
                            buttonsBox.addChild(steamButton);
                        }
                    }
                }

                Object extendedModInfo = Reflection.getFieldValue("modInfoExtended", modFile.modInfo);
                if(extendedModInfo != null){
                    String patreonUrl = Reflection.getFieldValue("patreon_url", extendedModInfo);
                    if(patreonUrl != null && !patreonUrl.isEmpty()){
                        Button patreonButton = new Button(Dim.px(80), Dim.px(79));
                        patreonButton.setTexture(Tex.stat(UICommonResources.patreonButton));
                        patreonButton.onLeftClickEvent.subscribe(patreonButton, () -> {
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                URI uri = new URI(patreonUrl);
                                desktop.browse(uri);
                            } catch (Exception ignored) {}
                        });
                        buttonsBox.addChild(patreonButton);
                    }

                    String discordUrl = Reflection.getFieldValue("discord_url", extendedModInfo);
                    if(discordUrl != null && !discordUrl.isEmpty()){
                        Button discordButton = new Button(Dim.px(80), Dim.px(79));
                        discordButton.setTexture(Tex.stat(UICommonResources.discordButton));
                        discordButton.onLeftClickEvent.subscribe(discordButton, () -> {
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                URI uri = new URI(discordUrl);
                                desktop.browse(uri);
                            } catch (Exception ignored) {}
                        });
                        buttonsBox.addChild(discordButton);
                    }
                }
            }
            modContentBox.addChild(buttonsBox);

            ImageTextBox idBox = new ImageTextBox(modFile.modInfo.ID, Pos.px(5), Pos.px(5), Dim.px(250), Dim.px(59));
            idBox.textBox.setFontSize(18);
            idBox.textBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
            modContentBox.addChild(idBox);

            TextBox authorsBox = new TextBox("Created by: ", Pos.px(21), Pos.px(955-425), Dim.px(346), Dim.px(50));
            authorsBox.setFontSize(20);
            authorsBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
            modContentBox.addChild(authorsBox);

            String authors = "";
            for(String author : modFile.modInfo.Authors){
                authors += author + "\n";
            }
            ImageTextBox authorsTextBox = new ImageTextBox(authors, Pos.px(13), Pos.px(955-684), Dim.px(349), Dim.px(259));
            authorsTextBox.textBox.setContentAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.TOP);
            modContentBox.addChild(authorsTextBox);

            TextBox creditsBox = new TextBox("Credits:", Pos.px(392), Pos.px(955-425), Dim.px(851), Dim.px(50));
            creditsBox.setFontSize(20);
            creditsBox.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
            modContentBox.addChild(creditsBox);

            ImageTextBox creditsTextBox = new ImageTextBox(modFile.modInfo.Credits, Pos.px(392), Pos.px(955-684), Dim.px(851), Dim.px(259));
            creditsTextBox.textBox.setContentAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.TOP);
            creditsTextBox.textBox.setWrap(true);
            modContentBox.addChild(creditsTextBox);
        }
    }

    private static ArrayList<ModFileExtended> getRegisteredMods(){
        try{
            ArrayList<ModFileExtended> modFiles = new ArrayList<>();

            Class modsScreenClass = Class.forName("com.evacipated.cardcrawl.modthespire.patches.modsscreen.ModsScreen");
            Map<URL, Object> baseModBadges = Reflection.getFieldValue("baseModBadges", modsScreenClass);

            for(ModInfo modInfo : Loader.MODINFOS){
                ModBadge badge = (ModBadge) baseModBadges.get(modInfo.jarURL);
                modFiles.add(new ModFileExtended(badge, modInfo));
            }

            return modFiles;
        }catch (Exception ignored){
            return new ArrayList<>();
        }
    }

    private static class ModFileExtended {
        public ModBadge modBadge;
        public ModInfo modInfo;

        public ModFileExtended(ModBadge modBadge, ModInfo modInfo) {
            this.modBadge = modBadge;
            this.modInfo = modInfo;
        }
    }

    private static class Patches{
        @SpirePatch2(cls = "com.evacipated.cardcrawl.modthespire.patches.modsscreen.ModMenuButton$ButtonEffect", method = "Postfix")
        public static class ModButtonEffectReplacer {
            public static void Replace(MenuButton __instance) {
                if(__instance.result.name().equals("MODS")){
                    DLibModListScreen screen = new DLibModListScreen();
                    screen.open();
                }
            }
        }
    }
}
