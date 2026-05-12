package com.gadgeski.ethereal.renderer

import android.content.Context
import android.opengl.GLES20
import android.view.MotionEvent
import com.gadgeski.ethereal.R
import com.gadgeski.ethereal.opengl.ShaderHelper
import com.gadgeski.ethereal.opengl.TextureHelper
import com.gadgeski.ethereal.settings.WallpaperTheme
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.random.Random
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class EtherealGLRenderer(private val context: Context) {

    private var screenWidth = 0
    private var screenHeight = 0

    private var touchX = 0f
    private var touchY = 0f
    private var isTouching = false

    private var gravityX = 0f
    private var gravityY = 0f
    private var xOffset = 0f

    private var startTime = 0L
    private var currentTheme = WallpaperTheme.GLITCH_SUNSET

    private var bgProgram = 0
    private var glitchProgram = 0
    private var particleProgram = 0
    private var bgTextureId = 0

    private val quadVertices = floatArrayOf(
        -1f,  1f,
        -1f, -1f,
        1f,  1f,
        1f, -1f,
    )

    private val quadTexCoords = floatArrayOf(
        0f, 0f,
        0f, 1f,
        1f, 0f,
        1f, 1f,
    )

    private val quadBuffer = ByteBuffer.allocateDirect(quadVertices.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer().apply { put(quadVertices); position(0) }

    private val texCoordBuffer = ByteBuffer.allocateDirect(quadTexCoords.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer().apply { put(quadTexCoords); position(0) }

    // Particle
    private val maxParticles = 200
    private val particles = ArrayList<GlitchParticle>(maxParticles)
    private val particleBuffer = ByteBuffer.allocateDirect(maxParticles * 4 * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()

    private val particleColors = arrayOf(
        floatArrayOf(0f, 1f, 1f),
        floatArrayOf(1f, 0f, 1f),
        floatArrayOf(1f, 1f, 1f),
    )

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

    @Suppress("UNUSED_PARAMETER")
    fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        startTime = System.currentTimeMillis()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        screenWidth = width
        screenHeight = height
    }

    @Suppress("UNUSED_PARAMETER")
    fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        val elapsed = (System.currentTimeMillis() - startTime) / 1000f

        drawBackground(elapsed)
        drawGlitch(elapsed)
        
        updateParticles()
        drawParticles(elapsed)
    }

    fun setTheme(theme: WallpaperTheme) {
        currentTheme = theme
        
        release()

        bgProgram = ShaderHelper.buildProgram(context, R.raw.bg_vertex, R.raw.bg_fragment)
        glitchProgram = ShaderHelper.buildProgram(context, R.raw.glitch_vertex, R.raw.glitch_fragment)
        particleProgram = ShaderHelper.buildProgram(context, R.raw.particle_vertex, R.raw.particle_fragment)

        bgTextureId = TextureHelper.loadTexture(context, theme.backgroundDrawableRes)

        particles.clear()
    }

    // -- Background Drawing --
    private fun drawBackground(time: Float) {
        if (bgProgram == 0 || bgTextureId == 0) return

        GLES20.glUseProgram(bgProgram)

        val posLoc = GLES20.glGetAttribLocation(bgProgram, "aPosition")
        val texLoc = GLES20.glGetAttribLocation(bgProgram, "aTexCoord")
        val timeLoc = GLES20.glGetUniformLocation(bgProgram, "uTime")
        val xOffsetLoc = GLES20.glGetUniformLocation(bgProgram, "uXOffset")
        val textureLoc = GLES20.glGetUniformLocation(bgProgram, "uTexture")
        val glitchLoc = GLES20.glGetUniformLocation(bgProgram, "uGlitchIntensity")
        val scanlineLoc = GLES20.glGetUniformLocation(bgProgram, "uScanlineStrength")

        quadBuffer.position(0)
        GLES20.glEnableVertexAttribArray(posLoc)
        GLES20.glVertexAttribPointer(posLoc, 2, GLES20.GL_FLOAT, false, 0, quadBuffer)

        texCoordBuffer.position(0)
        GLES20.glEnableVertexAttribArray(texLoc)
        GLES20.glVertexAttribPointer(texLoc, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer)

        GLES20.glUniform1f(timeLoc, time)
        GLES20.glUniform1f(xOffsetLoc, xOffset)
        GLES20.glUniform1f(glitchLoc, currentTheme.glitchIntensity)
        GLES20.glUniform1f(scanlineLoc, currentTheme.scanlineStrength)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, bgTextureId)
        GLES20.glUniform1i(textureLoc, 0)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(posLoc)
        GLES20.glDisableVertexAttribArray(texLoc)
    }

    // -- Glitch Overlay Drawing --
    private fun drawGlitch(time: Float) {
        if (glitchProgram == 0) return

        GLES20.glUseProgram(glitchProgram)

        val posLoc = GLES20.glGetAttribLocation(glitchProgram, "aPosition")
        val texLoc = GLES20.glGetAttribLocation(glitchProgram, "aTexCoord")
        val timeLoc = GLES20.glGetUniformLocation(glitchProgram, "uTime")
        val touchXLoc = GLES20.glGetUniformLocation(glitchProgram, "uTouchX")
        val touchYLoc = GLES20.glGetUniformLocation(glitchProgram, "uTouchY")
        val isTouchingLoc = GLES20.glGetUniformLocation(glitchProgram, "uIsTouching")
        val glitchLoc = GLES20.glGetUniformLocation(glitchProgram, "uGlitchIntensity")

        quadBuffer.position(0)
        GLES20.glEnableVertexAttribArray(posLoc)
        GLES20.glVertexAttribPointer(posLoc, 2, GLES20.GL_FLOAT, false, 0, quadBuffer)

        texCoordBuffer.position(0)
        GLES20.glEnableVertexAttribArray(texLoc)
        GLES20.glVertexAttribPointer(texLoc, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer)

        GLES20.glUniform1f(timeLoc, time)
        val sW = if(screenWidth > 0) screenWidth.toFloat() else 1f
        val sH = if(screenHeight > 0) screenHeight.toFloat() else 1f
        GLES20.glUniform1f(touchXLoc, touchX / sW)
        GLES20.glUniform1f(touchYLoc, touchY / sH)
        GLES20.glUniform1f(isTouchingLoc, if (isTouching) 1f else 0f)
        GLES20.glUniform1f(glitchLoc, currentTheme.glitchIntensity)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(posLoc)
        GLES20.glDisableVertexAttribArray(texLoc)
    }

    // -- Particles logic --
    fun ignite(screenX: Float, screenY: Float) {
        if (screenWidth <= 0 || screenHeight <= 0) return
        val count = (Random.nextInt(5, 12) * currentTheme.particleDensity).toInt().coerceAtLeast(1)
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

    private fun updateParticles() {
        if (screenWidth <= 0 || screenHeight <= 0) return
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

        val spawnChance = 0.05f + currentTheme.particleDensity * 0.15f
        if (particles.size < maxParticles && Random.nextFloat() < spawnChance) {
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
    private fun drawParticles(time: Float) {
        if (particleProgram == 0 || particles.isEmpty()) return

        GLES20.glUseProgram(particleProgram)

        val posLoc = GLES20.glGetAttribLocation(particleProgram, "aPosition")
        val alphaLoc = GLES20.glGetAttribLocation(particleProgram, "aAlpha")
        val sizeLoc = GLES20.glGetAttribLocation(particleProgram, "aSize")
        val colorLoc = GLES20.glGetUniformLocation(particleProgram, "uColor")

        particleColors.forEachIndexed { colorIndex, color ->
            val batch = particles.filter { it.colorIndex == colorIndex }
            if (batch.isEmpty()) return@forEachIndexed

            particleBuffer.clear()
            batch.forEach { p ->
                particleBuffer.put(p.x)
                particleBuffer.put(p.y)
                particleBuffer.put(p.life / p.maxLife)
                particleBuffer.put(p.size)
            }
            particleBuffer.position(0)

            val stride = 4 * 4

            particleBuffer.position(0)
            GLES20.glEnableVertexAttribArray(posLoc)
            GLES20.glVertexAttribPointer(posLoc, 2, GLES20.GL_FLOAT, false, stride, particleBuffer)

            particleBuffer.position(2)
            GLES20.glEnableVertexAttribArray(alphaLoc)
            GLES20.glVertexAttribPointer(alphaLoc, 1, GLES20.GL_FLOAT, false, stride, particleBuffer)

            particleBuffer.position(3)
            GLES20.glEnableVertexAttribArray(sizeLoc)
            GLES20.glVertexAttribPointer(sizeLoc, 1, GLES20.GL_FLOAT, false, stride, particleBuffer)

            GLES20.glUniform3f(colorLoc, color[0], color[1], color[2])
            GLES20.glDrawArrays(GLES20.GL_POINTS, 0, batch.size)
        }

        GLES20.glDisableVertexAttribArray(posLoc)
        GLES20.glDisableVertexAttribArray(alphaLoc)
        GLES20.glDisableVertexAttribArray(sizeLoc)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onVisibilityChanged(visible: Boolean) {}

    @Suppress("UNUSED_PARAMETER")
    fun onOffsetsChanged(xOffset: Float, yOffset: Float) {
        this.xOffset = xOffset
    }

    fun onTouchEvent(event: MotionEvent) {
        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                touchX = x
                touchY = y
                isTouching = true
                ignite(x, y)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isTouching = false
            }
        }
    }

    fun updateGravity(gx: Float, gy: Float) {
        gravityX = gx
        gravityY = gy
    }

    fun release() {
        TextureHelper.deleteTexture(bgTextureId)
        bgTextureId = 0
        if (bgProgram != 0) { GLES20.glDeleteProgram(bgProgram); bgProgram = 0 }
        if (glitchProgram != 0) { GLES20.glDeleteProgram(glitchProgram); glitchProgram = 0 }
        if (particleProgram != 0) { GLES20.glDeleteProgram(particleProgram); particleProgram = 0 }
    }
}