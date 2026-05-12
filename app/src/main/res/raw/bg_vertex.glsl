attribute vec2 aPosition;
attribute vec2 aTexCoord;
varying vec2 vTexCoord;
uniform float uXOffset;
void main() {
    vTexCoord = aTexCoord;
    gl_Position = vec4(aPosition.x + uXOffset * 0.05, aPosition.y, 0.0, 1.0);
}
