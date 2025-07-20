package dLib.ui.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import dLib.util.bindings.texture.textureresource.ITextureSource;

public class UICommonResources implements ITextureSource {
    public static final Texture white_pixel = ImageMaster.loadImage("dLibResources/images/ui/common/white_pixel.png");
    public static final Texture transparent_pixel = ImageMaster.loadImage("dLibResources/images/ui/common/transparent_pixel.png");

    public static final Texture arrow_left = ImageMaster.loadImage("dLibResources/images/ui/common/arrow/left.png");
    public static final Texture arrow_right = ImageMaster.loadImage("dLibResources/images/ui/common/arrow/right.png");
    public static final Texture arrow_up = ImageMaster.loadImage("dLibResources/images/ui/common/arrow/up.png");
    public static final Texture arrow_down = ImageMaster.loadImage("dLibResources/images/ui/common/arrow/down.png");
    public static final Texture arrow_left_double = ImageMaster.loadImage("dLibResources/images/ui/common/arrow/left_double.png");
    public static final Texture arrow_right_double = ImageMaster.loadImage("dLibResources/images/ui/common/arrow/right_double.png");

    public static final Texture cancelButton = ImageMaster.loadImage("dLibResources/images/ui/common/CancelButton.png");
    public static final Texture confirmButton = ImageMaster.loadImage("dLibResources/images/ui/common/ConfirmButton.png");
    public static final Texture cancelButtonSmall = ImageMaster.loadImage("dLibResources/images/ui/common/CancelButtonSmall.png");
    public static final Texture confirmButtonSmall = ImageMaster.loadImage("dLibResources/images/ui/common/ConfirmButtonSmall.png");

    public static final Texture checkbox_checked = ImageMaster.loadImage("dLibResources/images/ui/common/checkbox_checked.png");
    public static final Texture checkbox_unchecked = ImageMaster.loadImage("dLibResources/images/ui/common/checkbox_unchecked.png");

    public static final Texture radiobutton_checked_checked = ImageMaster.loadImage("dLibResources/images/ui/common/radiobutton_checked.png");
    public static final Texture radiobutton_checked_unchecked = ImageMaster.loadImage("dLibResources/images/ui/common/radiobutton_unchecked.png");

    public static final NinePatch inputfield = new NinePatch(ImageMaster.loadImage("dLibResources/images/ui/common/inputfield.png"), 23, 23, 18, 18);

    public static final NinePatch button01_horizontal = new NinePatch(ImageMaster.loadImage("dLibResources/images/ui/common/button01/horizontal.png"), 41, 43, 21, 20);

    public static final NinePatch button02_square = new NinePatch(ImageMaster.loadImage("dLibResources/images/ui/common/button02/square.png"), 45, 45, 45, 45);

    public static final NinePatch button03_square = new NinePatch(ImageMaster.loadImage("dLibResources/images/ui/common/button03/square.png"), 53, 53, 53, 53);

    public static final Texture alignment_base = ImageMaster.loadImage("dLibResources/images/ui/common/alignment/base.png");
    public static final Texture alignment_leftbottom = ImageMaster.loadImage("dLibResources/images/ui/common/alignment/00.png");
    public static final Texture alignment_leftcenter = ImageMaster.loadImage("dLibResources/images/ui/common/alignment/01.png");
    public static final Texture alignment_lefttop = ImageMaster.loadImage("dLibResources/images/ui/common/alignment/02.png");
    public static final Texture alignment_centerbottom = ImageMaster.loadImage("dLibResources/images/ui/common/alignment/10.png");
    public static final Texture alignment_centercenter = ImageMaster.loadImage("dLibResources/images/ui/common/alignment/11.png");
    public static final Texture alignment_centertop = ImageMaster.loadImage("dLibResources/images/ui/common/alignment/12.png");
    public static final Texture alignment_rightbottom = ImageMaster.loadImage("dLibResources/images/ui/common/alignment/20.png");
    public static final Texture alignment_rightcenter = ImageMaster.loadImage("dLibResources/images/ui/common/alignment/21.png");
    public static final Texture alignment_righttop = ImageMaster.loadImage("dLibResources/images/ui/common/alignment/22.png");

    public static final Texture scrollbar_horizontal_left = ImageMaster.loadImage("dLibResources/images/ui/common/scrollbar/horizontal_left.png");
    public static final Texture scrollbar_horizontal_mid = ImageMaster.loadImage("dLibResources/images/ui/common/scrollbar/horizontal_mid.png");
    public static final Texture scrollbar_horizontal_right = ImageMaster.loadImage("dLibResources/images/ui/common/scrollbar/horizontal_right.png");
    public static final Texture scrollbar_horizontal_train = ImageMaster.loadImage("dLibResources/images/ui/common/scrollbar/horizontal_train.png");
    public static final Texture scrollbar_vertical_top = ImageMaster.loadImage("dLibResources/images/ui/common/scrollbar/vertical_top.png");
    public static final Texture scrollbar_vertical_mid = ImageMaster.loadImage("dLibResources/images/ui/common/scrollbar/vertical_mid.png");
    public static final Texture scrollbar_vertical_bottom = ImageMaster.loadImage("dLibResources/images/ui/common/scrollbar/vertical_bottom.png");
    public static final Texture scrollbar_vertical_train = ImageMaster.loadImage("dLibResources/images/ui/common/scrollbar/vertical_train.png");

    public static final Texture background_medium = ImageMaster.loadImage("dLibResources/images/ui/common/bgs/medium.png");

    public static final Texture xButton = ImageMaster.loadImage("dLibResources/images/ui/common/xButton.png");
    public static final Texture deleteButton = ImageMaster.loadImage("dLibResources/images/ui/common/DeleteButton.png");
    public static final Texture settingsButton = ImageMaster.loadImage("dLibResources/images/ui/common/settingsButton.png");

    public static final Texture discordButton = ImageMaster.loadImage("dLibResources/images/ui/common/discordButton.png");
    public static final Texture patreonButton = ImageMaster.loadImage("dLibResources/images/ui/common/patreonButton.png");
    public static final Texture steamButton = ImageMaster.loadImage("dLibResources/images/ui/common/steamButton.png");

    public static final Texture lightnessBar = ImageMaster.loadImage("dLibResources/images/ui/common/color/lightnessBar.png");
    public static final Texture lightnessBarOverlay = ImageMaster.loadImage("dLibResources/images/ui/common/color/lightnessBarOverlay.png");
    public static final Texture alphaBar = ImageMaster.loadImage("dLibResources/images/ui/common/color/alphaBar.png");

    public static final NinePatch dropZoneOptionBg = new NinePatch(ImageMaster.loadImage("dLibResources/images/ui/common/DropZoneOptionBg.png"), 50, 50, 50, 50);
    public static final Texture dropZoneBg = ImageMaster.loadImage("dLibResources/images/ui/common/DropZoneBg.png");

    public static final NinePatch bg01 = new NinePatch(ImageMaster.loadImage("dLibResources/images/ui/common/bgs/bg01.png"), 408, 456, 97, 153);

    public static final NinePatch bg02_background = new NinePatch(ImageMaster.loadImage("dLibResources/images/ui/common/bgs/bg02_background.png"), 26, 26, 26, 26);
    public static final NinePatch bg02_inner = new NinePatch(ImageMaster.loadImage("dLibResources/images/ui/common/bgs/bg02_inner.png"), 26, 26, 26, 26);

    public static final NinePatch bg03 = new NinePatch(ImageMaster.loadImage("dLibResources/images/ui/common/bgs/bg03.png"), 40, 68, 33, 67);

    public static final Texture errorIcon = ImageMaster.loadImage("dLibResources/images/ui/common/errorIcon.png");

    public static final NinePatch tooltipBg = new NinePatch(ImageMaster.loadImage("dLibResources/images/ui/common/tooltipBg.png"), 18, 17, 17, 18);

    public static final Texture inputfield_color = ImageMaster.loadImage("dLibResources/images/ui/common/inputfield/color.png");

    public static final Texture color_fill = ImageMaster.loadImage("dLibResources/images/ui/common/magiccolor/Fill.png");
    public static final Texture color_outline = ImageMaster.loadImage("dLibResources/images/ui/common/magiccolor/Outline.png");
    public static final Texture color_outline_selected = ImageMaster.loadImage("dLibResources/images/ui/common/magiccolor/Outline_selected.png");

    public static final NinePatch advancedDebugOverlay = new NinePatch(ImageMaster.loadImage("dLibResources/images/ui/common/AdvancedElementHitbox.png"), 50, 50, 50, 50);

    public static final Texture energyIconUniversal = ImageMaster.loadImage("dLibResources/images/ui/common/EnergyIconUniversal.png");
}
