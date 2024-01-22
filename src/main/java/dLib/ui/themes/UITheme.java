package dLib.ui.themes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import dLib.util.TextureManager;

public class UITheme {
    private String ID;
    private String resourceLocation;

    public Texture background;

    public Texture button_large;
    public Texture button_large_outline_empty;
    public Texture button_large_outline_confirm;

    public Texture button_small;
    public Texture button_small_confirm;
    public Texture button_small_decline;
    public Texture button_small_save;
    public Texture button_small_delete;
    public Texture button_small_delete_confirm;

    public Texture arrow_left;
    public Texture arrow_left_double;
    public Texture arrow_right;
    public Texture arrow_right_double;
    public Texture arrow_up;
    public Texture arrow_down;

    public Texture inputfield;

    public Color textColor;

    public UITheme(String themeID, String themeFolderLoc){
        this.ID = themeID;

        this.resourceLocation = themeFolderLoc;
        if(!resourceLocation.endsWith("/")) resourceLocation += "/";

        textColor = Color.BLACK.cpy();
    }

    public UITheme setTextColor(Color textColor){
        this.textColor = textColor;
        return this;
    }

    public String getId(){
        return ID;
    }

    public void load(){
        this.background =  loadItem("background.png");

        this.button_large =  loadItem("button_large.png");
        this.button_large_outline_empty =  loadItem("button_large_outline_empty.png");
        this.button_large_outline_confirm =  loadItem("button_large_outline_confirm.png");

        this.button_small =  loadItem("button_small.png");
        this.button_small_confirm =  loadItem("button_small_confirm.png");
        this.button_small_decline =  loadItem("button_small_decline.png");
        this.button_small_save =  loadItem("button_small_save.png");
        this.button_small_delete =  loadItem("button_small_delete.png");
        this.button_small_delete_confirm =  loadItem("button_small_delete_confirm.png");

        this.arrow_left =  loadItem("arrow_left.png");
        this.arrow_left_double =  loadItem("arrow_left_double.png");
        this.arrow_right =  loadItem("arrow_right.png");
        this.arrow_right_double =  loadItem("arrow_right_double.png");
        this.arrow_up =  loadItem("arrow_up.png");
        this.arrow_down =  loadItem("arrow_down.png");

        this.inputfield =  loadItem("inputfield.png");
    }

    public void dispose(){
        this.background.dispose();

        this.button_large.dispose();
        this.button_large_outline_empty.dispose();
        this.button_large_outline_confirm.dispose();

        this.button_small.dispose();
        this.button_small_confirm.dispose();
        this.button_small_decline.dispose();
        this.button_small_delete.dispose();
        this.button_small_delete_confirm.dispose();

        this.arrow_left.dispose();
        this.arrow_left_double.dispose();
        this.arrow_right.dispose();
        this.arrow_right_double.dispose();
        this.arrow_up.dispose();
        this.arrow_down.dispose();
    }

    protected Texture loadItem(String itemName){
        return TextureManager.getTexture(resourceLocation + itemName);
    }
}
