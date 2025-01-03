package dLib.external;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import dLib.ui.elements.items.Renderable;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.Pos;

public class MainMenuPatches {
    @SpirePatch2(clz = MainMenuScreen.class, method = "render")
    public static class PluginStatusRendererPatch{
        private static final Texture pluginStatusIcon = ImageMaster.loadImage("dLibResources/images/ui/intellijplugin/statusicon.png");

        private static Renderable statusIcon;

        public static void Postfix(SpriteBatch sb){
            if(statusIcon == null){
                statusIcon = new Renderable(Tex.stat(pluginStatusIcon), Pos.px(1841), Pos.px(1080-1053), Dim.px(50), Dim.px(50)){
                    //TODO turn into overlay
                    @Override
                    protected boolean shouldRender() {
                        return ExternalEditorCommunicationManager.isEnabled();
                    }

                    @Override
                    protected Color getColorForRender() {
                        return (ExternalEditorCommunicationManager.isRunning() ? Color.GREEN : Color.RED);
                    }
                };
            }

            statusIcon.render(sb);
        }
    }
}
