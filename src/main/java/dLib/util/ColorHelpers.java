package dLib.util;

import com.badlogic.gdx.graphics.Color;

public class ColorHelpers {
    public static Color fromHSV(float hue, float saturation, float value) {
        float r, g, b;

        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        if (h == 0) {
            r = value;
            g = t;
            b = p;
        } else if (h == 1) {
            r = q;
            g = value;
            b = p;
        } else if (h == 2) {
            r = p;
            g = value;
            b = t;
        } else if (h == 3) {
            r = p;
            g = q;
            b = value;
        } else if (h == 4) {
            r = t;
            g = p;
            b = value;
        } else if (h <= 6) {
            r = value;
            g = p;
            b = q;
        } else {
            throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }

        return new Color(r, g, b, 1.0f);
    }

    public static Color fromHSL(float hue, float saturation, float lightness) {
        float r, g, b;

        if (saturation == 0) {
            r = g = b = lightness; // achromatic
        } else {
            float q = lightness < 0.5 ? lightness * (1 + saturation) : lightness + saturation - lightness * saturation;
            float p = 2 * lightness - q;
            r = hue2rgb(p, q, hue + 1/3f);
            g = hue2rgb(p, q, hue);
            b = hue2rgb(p, q, hue - 1/3f);
        }

        return new Color(r, g, b, 1.0f);
    }

    private static float hue2rgb(float p, float q, float t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1/6f) return p + (q - p) * 6 * t;
        if (t < 1/2f) return q;
        if (t < 2/3f) return p + (q - p) * (2/3f - t) * 6;
        return p;
    }
}
