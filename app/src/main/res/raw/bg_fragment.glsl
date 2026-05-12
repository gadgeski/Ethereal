precision mediump float;
varying vec2 vTexCoord;
uniform sampler2D uTexture;
uniform float uTime;
uniform float uGlitchIntensity;
uniform float uScanlineStrength;

float rand(vec2 co) {
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec2 uv = vTexCoord;

    // スキャンライン
    float scanline = sin(uv.y * 800.0) * uScanlineStrength;

    // 散発的グリッチ
    float glitchTrigger = step(0.92, rand(vec2(floor(uTime * 3.0), 0.0)));
    float glitchStrength = glitchTrigger * 0.03 * uGlitchIntensity;
    float glitchBand = step(
        rand(vec2(floor(uv.y * 20.0), floor(uTime * 3.0))), 0.3
    );
    uv.x += glitchStrength * glitchBand * rand(vec2(uv.y, uTime));

    // RGBシフト
    float shift = glitchTrigger * 0.008 * uGlitchIntensity;
    float r = texture2D(uTexture, vec2(uv.x + shift, uv.y)).r;
    float g = texture2D(uTexture, uv).g;
    float b = texture2D(uTexture, vec2(uv.x - shift, uv.y)).b;

    vec4 color = vec4(r + scanline, g + scanline, b + scanline, 1.0);
    gl_FragColor = color;
}
