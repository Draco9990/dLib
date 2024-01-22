package dLib.plugin.intellij;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import dLib.ui.elements.implementations.Renderable;

public class MainMenuPatches {
    @SpirePatch2(clz = MainMenuScreen.class, method = "render")
    public static class PluginStatusRendererPatch{
        private static final Texture pluginStatusIcon = ImageMaster.loadImage("dLibResources/images/ui/intellijplugin/statusicon.png");

        private static Renderable statusIcon;

        public static void Postfix(SpriteBatch sb){
            if(statusIcon == null){
                statusIcon = new Renderable(pluginStatusIcon, 1841, 1080-1053, 50, 50){
                    @Override
                    protected boolean shouldRender() {
                        return PluginManager.isEnabled();
                    }

                    @Override
                    protected Color getColorForRender() {
                        return (PluginManager.isRunning() ? Color.GREEN : Color.RED);
                    }
                };
            }

            statusIcon.render(sb);
        }
    }
}
