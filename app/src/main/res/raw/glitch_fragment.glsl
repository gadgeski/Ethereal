precision mediump float;
varying vec2 vTexCoord;
uniform float uTime;
uniform float uTouchX;
uniform float uTouchY;
uniform float uIsTouching;
uniform float uGlitchIntensity;
uniform float uScanProgress;

float rand(vec2 co) {
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec2 uv = vTexCoord;

    // 散発的なノイズブロック
    float trigger = step(0.95, rand(vec2(floor(uTime * 2.0), 1.0)));
    float blockNoise = trigger * rand(vec2(
        floor(uv.x * 30.0),
        floor(uv.y * 50.0 + uTime * 5.0)
    ));
    float block = step(0.85, blockNoise) * 0.15 * uGlitchIntensity;

    // タッチ時の波紋グリッチ
    float touchEffect = 0.0;
    if (uIsTouching > 0.5) {
        float dx = uv.x - uTouchX;
        float dy = uv.y - uTouchY;
        float dist = sqrt(dx * dx + dy * dy);
        touchEffect = sin(dist * 40.0 - uTime * 10.0) * 0.05
            * uGlitchIntensity
            * (1.0 - smoothstep(0.0, 0.3, dist));
    }

    // 水平ラインのズレ
    float lineGlitch = trigger * step(0.9,
        rand(vec2(floor(uv.y * 40.0), floor(uTime * 4.0)))
    ) * 0.04 * uGlitchIntensity;

    float glitchAlpha = clamp(block + abs(touchEffect) + lineGlitch, 0.0, 0.3);

    // グリッチカラー: シアン・マゼンタ・ホワイト
    float colorSeed = rand(vec2(floor(uTime * 3.0), floor(uv.y * 20.0)));
    vec3 glitchColor;
    if (colorSeed < 0.33) {
        glitchColor = vec3(0.0, 1.0, 1.0);
    } else if (colorSeed < 0.66) {
        glitchColor = vec3(1.0, 0.0, 1.0);
    } else {
        glitchColor = vec3(1.0, 1.0, 1.0);
    }

    // 充電時のスキャンスイープ
    float scanAlpha = 0.0;
    if (uScanProgress >= 0.0 && uScanProgress <= 1.0) {
        float e = 1.0 - pow(1.0 - uScanProgress, 2.0);
        float bandY = 1.0 - e;
        float d = 1.0 - abs(uv.y - bandY) / 0.08;
        float fade = sin(3.14159 * uScanProgress);
        d = max(d, 0.0);
        scanAlpha = d * d * 0.3 * fade;
    }
    vec3 scanColor = vec3(0.71, 0.92, 1.0);

    // 合成
    vec3 finalColor = mix(glitchColor, scanColor, scanAlpha > glitchAlpha ? 1.0 : 0.0);
    float finalAlpha = clamp(glitchAlpha + scanAlpha, 0.0, 0.4);

    gl_FragColor = vec4(finalColor, finalAlpha);
}