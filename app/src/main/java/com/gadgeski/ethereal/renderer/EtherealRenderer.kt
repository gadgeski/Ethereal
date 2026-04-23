package com.gadgeski.ethereal.renderer

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class EtherealGLRenderer(private val context: Context) : GLSurfaceView.Renderer {

    private var screenWidth = 0
    private var screenHeight = 0

    private var touchX = 0f
    private var touchY = 0f
    private var isTouching = false

    private var gravityX = 0f
    private var gravityY = 0f

    private var xOffset = 0f

    private var startTime = 0L

    private lateinit var backgroundRenderer: BackgroundRenderer
    private lateinit var glitchRenderer: GlitchRenderer
    private lateinit var particleRenderer: ParticleRenderer

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        startTime = System.currentTimeMillis()

        backgroundRenderer = BackgroundRenderer(context)
        glitchRenderer = GlitchRenderer()
        particleRenderer = ParticleRenderer()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        screenWidth = width
        screenHeight = height

        backgroundRenderer.onSurfaceChanged(width, height)
        glitchRenderer.onSurfaceChanged(width, height)
        particleRenderer.onSurfaceChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        val elapsed = (System.currentTimeMillis() - startTime) / 1000f

        // 1) 背景テクスチャ + スキャンライン
        backgroundRenderer.draw(elapsed, xOffset)

        // 2) グリッチオーバーレイ
        glitchRenderer.draw(elapsed, touchX, touchY, isTouching)

        // 3) パーティクル
        particleRenderer.update(gravityX, gravityY, screenWidth, screenHeight)
        particleRenderer.draw(elapsed)
    }

    fun onVisibilityChanged(visible: Boolean) {
        // GLSurfaceViewのonPause/onResumeで制御するため現状は空
    }

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
                particleRenderer.ignite(x, y)
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
}