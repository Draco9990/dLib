#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_hueShift; // Hue shift in radians

// Function to convert RGB to HSV
vec3 rgbToHsv(vec3 c) {
    float cMax = max(c.r, max(c.g, c.b));
    float cMin = min(c.r, min(c.g, c.b));
    float delta = cMax - cMin;

    float h = 0.0;
    if (delta > 0.0) {
        if (cMax == c.r) {
            h = mod((c.g - c.b) / delta, 6.0);
        } else if (cMax == c.g) {
            h = (c.b - c.r) / delta + 2.0;
        } else {
            h = (c.r - c.g) / delta + 4.0;
        }
        h /= 6.0; // Normalize to [0, 1]
    }

    float s = cMax == 0.0 ? 0.0 : delta / cMax;
    float v = cMax;

    return vec3(h, s, v);
}

// Function to convert HSV back to RGB
vec3 hsvToRgb(vec3 c) {
    float h = c.x * 6.0; // Expand h to [0, 6]
    float s = c.y;
    float v = c.z;

    float c1 = v * s;
    float x = c1 * (1.0 - abs(mod(h, 2.0) - 1.0));
    float m = v - c1;

    vec3 rgb;
    if (h < 1.0) {
        rgb = vec3(c1, x, 0.0);
    } else if (h < 2.0) {
        rgb = vec3(x, c1, 0.0);
    } else if (h < 3.0) {
        rgb = vec3(0.0, c1, x);
    } else if (h < 4.0) {
        rgb = vec3(0.0, x, c1);
    } else if (h < 5.0) {
        rgb = vec3(x, 0.0, c1);
    } else {
        rgb = vec3(c1, 0.0, x);
    }

    return rgb + m;
}

void main()
{
    vec4 texColor = texture2D(u_texture, v_texCoords);
    vec3 hsv = rgbToHsv(texColor.rgb);
    hsv.x = mod(hsv.x + u_hueShift / (2.0 * 3.14159265), 1.0); // Adjust hue and wrap around
    vec3 shiftedRgb = hsvToRgb(hsv);

    gl_FragColor = v_color * vec4(shiftedRgb, texColor.a);
}
