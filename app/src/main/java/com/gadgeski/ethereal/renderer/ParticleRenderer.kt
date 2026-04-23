package com.gadgeski.ethereal.renderer

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.random.Random

class ParticleRenderer {

    private var programId = 0
    private var screenWidth = 0
    private var screenHeight = 0

    private val maxParticles = 200
    private val particles = ArrayList<GlitchParticle>(maxParticles)

    private lateinit var vertexBuffer: FloatBuffer

    private val vertexShaderSrc = """
        attribute vec2 aPosition;
        attribute float aAlpha;
        attribute float aSize;
        varying float vAlpha;
        void main() {
            vAlpha = aAlpha;
            gl_Position = vec4(aPosition, 0.0, 1.0);
            gl_PointSize = aSize;
        }
    """.trimIndent()

    private val fragmentShaderSrc = """
        precision mediump float;
        varying float vAlpha;
        uniform vec3 uColor;
        void main() {
            vec2 coord = gl_PointCoord - vec2(0.5);
            float dist = length(coord);
            float alpha = smoothstep(0.5, 0.1, dist) * vAlpha;
            gl_FragColor = vec4(uColor, alpha);
        }
    """.trimIndent()

    // グリッチパーティクルのデータクラス
    data class GlitchParticle(
        var x: Float,
        var y: Float,
        var vx: Float,
        var vy: Float,
        var life: Float,
        var maxLife: Float,
        var size: Float,
        val colorIndex: Int
    )

    // シアン・マゼンタ・ホワイトの3色
    private val colors = arrayOf(
        floatArrayOf(0f, 1f, 1f),
        floatArrayOf(1f, 0f, 1f),
        floatArrayOf(1f, 1f, 1f),
    )

    init {
        setupShader()
        setupBuffers()
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

    private fun setupBuffers() {
        // position(x,y) + alpha + size = 4 floats per particle
        vertexBuffer = ByteBuffer.allocateDirect(maxParticles * 4 * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
    }

    fun onSurfaceChanged(width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
    }

    fun ignite(screenX: Float, screenY: Float) {
        if (screenWidth <= 0 || screenHeight <= 0) return
        val count = Random.nextInt(5, 12)
        repeat(count) {
            if (particles.size >= maxParticles) return@repeat
            val angle = Random.nextFloat() * Math.PI.toFloat() * 2f
            val speed = Random.nextFloat() * 0.01f + 0.002f
            particles.add(
                GlitchParticle(
                    x = screenX / screenWidth * 2f - 1f,
                    y = -(screenY / screenHeight * 2f - 1f),
                    vx = kotlin.math.cos(angle) * speed,
                    vy = kotlin.math.sin(angle) * speed,
                    life = 1f,
                    maxLife = 1f,
                    size = Random.nextFloat() * 8f + 4f,
                    colorIndex = Random.nextInt(3)
                )
            )
        }
    }

    fun update(gravityX: Float, gravityY: Float, width: Int, height: Int) {
        screenWidth = width
        screenHeight = height

        val gx = gravityX * 0.0003f
        val gy = gravityY * 0.0003f

        val iterator = particles.iterator()
        while (iterator.hasNext()) {
            val p = iterator.next()
            p.vx += gx
            p.vy -= gy
            p.x += p.vx
            p.y += p.vy
            p.life -= 0.012f
            if (p.life <= 0f) iterator.remove()
        }

        // 自律的にランダム生成（散発的）
        if (particles.size < maxParticles && Random.nextFloat() < 0.15f) {
            spawnAmbientParticle()
        }
    }

    private fun spawnAmbientParticle() {
        val x = Random.nextFloat() * 2f - 1f
        val y = Random.nextFloat() * 2f - 1f
        val angle = Random.nextFloat() * Math.PI.toFloat() * 2f
        val speed = Random.nextFloat() * 0.004f + 0.001f
        particles.add(
            GlitchParticle(
                x = x,
                y = y,
                vx = kotlin.math.cos(angle) * speed,
                vy = kotlin.math.sin(angle) * speed,
                life = 1f,
                maxLife = 1f,
                size = Random.nextFloat() * 5f + 2f,
                colorIndex = Random.nextInt(3)
            )
        )
    }

    @Suppress("UNUSED_PARAMETER")
    fun draw(time: Float) {
        if (particles.isEmpty()) return

        GLES20.glUseProgram(programId)

        val posLoc = GLES20.glGetAttribLocation(programId, "aPosition")
        val alphaLoc = GLES20.glGetAttribLocation(programId, "aAlpha")
        val sizeLoc = GLES20.glGetAttribLocation(programId, "aSize")
        val colorLoc = GLES20.glGetUniformLocation(programId, "uColor")

        // 色ごとにバッチ描画
        colors.forEachIndexed { colorIndex, color ->
            val batch = particles.filter { it.colorIndex == colorIndex }
            if (batch.isEmpty()) return@forEachIndexed

            vertexBuffer.clear()
            batch.forEach { p ->
                vertexBuffer.put(p.x)
                vertexBuffer.put(p.y)
                vertexBuffer.put(p.life / p.maxLife)
                vertexBuffer.put(p.size)
            }
            vertexBuffer.position(0)

            val stride = 4 * 4 // 4 floats * 4 bytes

            GLES20.glEnableVertexAttribArray(posLoc)
            GLES20.glVertexAttribPointer(posLoc, 2, GLES20.GL_FLOAT, false, stride, vertexBuffer)

            vertexBuffer.position(2)
            GLES20.glEnableVertexAttribArray(alphaLoc)
            GLES20.glVertexAttribPointer(alphaLoc, 1, GLES20.GL_FLOAT, false, stride, vertexBuffer)

            vertexBuffer.position(3)
            GLES20.glEnableVertexAttribArray(sizeLoc)
            GLES20.glVertexAttribPointer(sizeLoc, 1, GLES20.GL_FLOAT, false, stride, vertexBuffer)

            GLES20.glUniform3f(colorLoc, color[0], color[1], color[2])
            GLES20.glDrawArrays(GLES20.GL_POINTS, 0, batch.size)
        }

        GLES20.glDisableVertexAttribArray(posLoc)
        GLES20.glDisableVertexAttribArray(alphaLoc)
        GLES20.glDisableVertexAttribArray(sizeLoc)
    }

    private fun compileShader(type: Int, src: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, src)
            GLES20.glCompileShader(shader)
        }
    }
}