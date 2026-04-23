package com.gadgeski.ethereal.renderer

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class GlitchRenderer {

    private var programId = 0

    private lateinit var vertexBuffer: FloatBuffer
    private lateinit var texCoordBuffer: FloatBuffer

    private val vertices = floatArrayOf(
        -1f,  1f,
        -1f, -1f,
        1f,  1f,
        1f, -1f,
    )

    private val texCoords = floatArrayOf(
        0f, 0f,
        0f, 1f,
        1f, 0f,
        1f, 1f,
    )

    private val vertexShaderSrc = """
        attribute vec2 aPosition;
        attribute vec2 aTexCoord;
        varying vec2 vTexCoord;
        void main() {
            vTexCoord = aTexCoord;
            gl_Position = vec4(aPosition, 0.0, 1.0);
        }
    """.trimIndent()

    private val fragmentShaderSrc = """
        precision mediump float;
        varying vec2 vTexCoord;
        uniform float uTime;
        uniform float uTouchX;
        uniform float uTouchY;
        uniform float uIsTouching;

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
            float block = step(0.85, blockNoise) * 0.15;

            // タッチ時の波紋グリッチ
            float touchEffect = 0.0;
            if (uIsTouching > 0.5) {
                float dx = uv.x - uTouchX;
                float dy = uv.y - uTouchY;
                float dist = sqrt(dx * dx + dy * dy);
                touchEffect = sin(dist * 40.0 - uTime * 10.0) * 0.05
                    * (1.0 - smoothstep(0.0, 0.3, dist));
            }

            // 水平ラインのズレ
            float lineGlitch = trigger * step(0.9,
                rand(vec2(floor(uv.y * 40.0), floor(uTime * 4.0)))
            ) * 0.04;

            float glitchAlpha = clamp(block + abs(touchEffect) + lineGlitch, 0.0, 0.3);

            // グリッチカラー: シアン・マゼンタ・ホワイト
            float colorSeed = rand(vec2(floor(uTime * 3.0), floor(uv.y * 20.0)));
            vec3 glitchColor;
            if (colorSeed < 0.33) {
                glitchColor = vec3(0.0, 1.0, 1.0); // シアン
            } else if (colorSeed < 0.66) {
                glitchColor = vec3(1.0, 0.0, 1.0); // マゼンタ
            } else {
                glitchColor = vec3(1.0, 1.0, 1.0); // ホワイト
            }

            gl_FragColor = vec4(glitchColor, glitchAlpha);
        }
    """.trimIndent()

    init {
        setupBuffers()
        setupShader()
    }

    private fun setupBuffers() {
        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply { put(vertices); position(0) }

        texCoordBuffer = ByteBuffer.allocateDirect(texCoords.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply { put(texCoords); position(0) }
    }

    private fun setupShader() {
        val vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderSrc)
        val fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSrc)

        programId = GLES20.glCreateProgram().also { program ->
            GLES20.glAttachShader(program, vertexShader)
            GLES20.glAttachShader(program, fragmentShader)
            GLES20.glLinkProgram(program)
        }
    }

    fun onSurfaceChanged(width: Int, height: Int) {}

    fun draw(time: Float, touchX: Float, touchY: Float, isTouching: Boolean) {
        GLES20.glUseProgram(programId)

        val posLoc = GLES20.glGetAttribLocation(programId, "aPosition")
        val texLoc = GLES20.glGetAttribLocation(programId, "aTexCoord")
        val timeLoc = GLES20.glGetUniformLocation(programId, "uTime")
        val touchXLoc = GLES20.glGetUniformLocation(programId, "uTouchX")
        val touchYLoc = GLES20.glGetUniformLocation(programId, "uTouchY")
        val isTouchingLoc = GLES20.glGetUniformLocation(programId, "uIsTouching")

        GLES20.glEnableVertexAttribArray(posLoc)
        GLES20.glVertexAttribPointer(posLoc, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        GLES20.glEnableVertexAttribArray(texLoc)
        GLES20.glVertexAttribPointer(texLoc, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer)

        GLES20.glUniform1f(timeLoc, time)
        GLES20.glUniform1f(touchXLoc, touchX / 1080f)
        GLES20.glUniform1f(touchYLoc, touchY / 2400f)
        GLES20.glUniform1f(isTouchingLoc, if (isTouching) 1f else 0f)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(posLoc)
        GLES20.glDisableVertexAttribArray(texLoc)
    }

    private fun compileShader(type: Int, src: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, src)
            GLES20.glCompileShader(shader)
        }
    }
}