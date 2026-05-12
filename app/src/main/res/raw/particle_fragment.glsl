precision mediump float;
varying float vAlpha;
uniform vec3 uColor;
void main() {
    vec2 coord = gl_PointCoord - vec2(0.5);
    float dist = length(coord);
    float alpha = smoothstep(0.5, 0.1, dist) * vAlpha;
    gl_FragColor = vec4(uColor, alpha);
}
