attribute vec2 aPosition;
attribute float aAlpha;
attribute float aSize;
varying float vAlpha;
void main() {
    vAlpha = aAlpha;
    gl_Position = vec4(aPosition, 0.0, 1.0);
    gl_PointSize = aSize;
}
