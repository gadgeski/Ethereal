package com.gadgeski.ethereal.renderer

import android.opengl.GLES20
import android.opengl.GLUtils
import com.gadgeski.ethereal.settings.WallpaperTheme
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class BackgroundRenderer {

    private var programId = 0
    private var textureId = 0

    private var screenWidth = 0
    private var screenHeight = 0

    private var glitchIntensity = 0.6f
    private var scanlineStrength = 0.04f

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
        uniform float uXOffset;
        void main() {
            vTexCoord = aTexCoord;
            gl_Position = vec4(aPosition.x + uXOffset * 0.05, aPosition.y, 0.0, 1.0);
        }
    """.trimIndent()

    private val fragmentShaderSrc = """
        precision mediump float;
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

    fun setTheme(theme: WallpaperTheme, bitmap: android.graphics.Bitmap) {
        glitchIntensity = theme.glitchIntensity
        scanlineStrength = theme.scanlineStrength
        loadTexture(bitmap)
    }

    private fun loadTexture(bitmap: android.graphics.Bitmap) {
        if (textureId != 0) {
            GLES20.glDeleteTextures(1, intArrayOf(textureId), 0)
            textureId = 0
        }

        val ids = IntArray(1)
        GLES20.glGenTextures(1, ids, 0)
        textureId = ids[0]
        android.util.Log.d("Ethereal", "loadTexture: textureId=$textureId")

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
    }

    fun onSurfaceChanged(width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
    }

    fun draw(time: Float, xOffset: Float) {
        if (textureId == 0) return

        GLES20.glUseProgram(programId)

        val posLoc = GLES20.glGetAttribLocation(programId, "aPosition")
        val texLoc = GLES20.glGetAttribLocation(programId, "aTexCoord")
        val timeLoc = GLES20.glGetUniformLocation(programId, "uTime")
        val xOffsetLoc = GLES20.glGetUniformLocation(programId, "uXOffset")
        val textureLoc = GLES20.glGetUniformLocation(programId, "uTexture")
        val glitchLoc = GLES20.glGetUniformLocation(programId, "uGlitchIntensity")
        val scanlineLoc = GLES20.glGetUniformLocation(programId, "uScanlineStrength")

        GLES20.glEnableVertexAttribArray(posLoc)
        GLES20.glVertexAttribPointer(posLoc, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        GLES20.glEnableVertexAttribArray(texLoc)
        GLES20.glVertexAttribPointer(texLoc, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer)

        GLES20.glUniform1f(timeLoc, time)
        GLES20.glUniform1f(xOffsetLoc, xOffset)
        GLES20.glUniform1f(glitchLoc, glitchIntensity)
        GLES20.glUniform1f(scanlineLoc, scanlineStrength)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(textureLoc, 0)

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