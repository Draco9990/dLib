package dLib.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.nio.ByteBuffer;

public class TextureHelpers {
    public static Texture convertBufferedImageToTexture(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = bufferedImage.getRGB(x, y);
                pixmap.drawPixel(x, y, (argb << 8) | (argb >>> 24));
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }
}
