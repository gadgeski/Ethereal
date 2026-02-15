package com.gadgeski.ethereal.renderer

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.SurfaceHolder

class EtherealRenderer(private val context: Context) {

    private var surfaceHolder: SurfaceHolder? = null
    private var isVisible = false

    private val handler = Handler(Looper.getMainLooper())

    private var screenWidth = 0
    private var screenHeight = 0

    // 背景（空）のシステム
    private lateinit var skySystem: SkySystem

    // 霧のシステム
    private val fogSystem = FogSystem()

    // パーティクルのシステム
    private val particleSystem = ParticleSystem()

    // Touch state
    private var touchX = 0f
    private var touchY = 0f
    private var isTouching = false

    private val drawRunner = object : Runnable {
        override fun run() {
            draw()
            if (isVisible) {
                handler.postDelayed(this, FRAME_DELAY_MS)
            }
        }
    }

    fun onSurfaceCreated(holder: SurfaceHolder) {
        surfaceHolder = holder
        if (!::skySystem.isInitialized) {
            skySystem = SkySystem(context)
        }
    }

    fun onSurfaceChanged(holder: SurfaceHolder, width: Int, height: Int) {
        surfaceHolder = holder
        screenWidth = width
        screenHeight = height

        if (!::skySystem.isInitialized) {
            skySystem = SkySystem(context)
        }

        skySystem.updateSize(width, height)
        fogSystem.updateSize(width, height)

        draw()
    }

    fun onSurfaceDestroyed() {
        stop()
        surfaceHolder = null
    }

    fun onVisibilityChanged(visible: Boolean) {
        isVisible = visible
        if (visible) start() else stop()
    }

    fun updateTouch(x: Float, y: Float, touching: Boolean) {
        touchX = x
        touchY = y
        isTouching = touching
    }

    @Suppress("UNUSED_PARAMETER")
    fun onOffsetsChanged(
        xOffset: Float,
        yOffset: Float,
        xOffsetStep: Float,
        yOffsetStep: Float,
        xPixelOffset: Int,
        yPixelOffset: Int
    ) {
        if (!::skySystem.isInitialized) return

        skySystem.setParallax(xOffset)
        fogSystem.setParallax(xOffset)
        particleSystem.setParallax(xOffset)
    }

    fun onTouchEvent(event: MotionEvent) {
        if (!::skySystem.isInitialized) return

        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> updateTouch(x, y, false)
            else -> updateTouch(x, y, true)
        }

        skySystem.onTouch(event)

        if (event.actionMasked == MotionEvent.ACTION_DOWN || event.actionMasked == MotionEvent.ACTION_MOVE) {
            particleSystem.ignite(x, y)
        }
    }

    private fun start() {
        handler.removeCallbacks(drawRunner)
        handler.post(drawRunner)
    }

    private fun stop() {
        handler.removeCallbacks(drawRunner)
    }

    private fun draw() {
        val holder = surfaceHolder ?: return
        if (!::skySystem.isInitialized) return
        if (screenWidth <= 0 || screenHeight <= 0) return

        var canvas: Canvas? = null
        try {
            canvas = holder.lockCanvas()
            if (canvas != null) {
                // 1) 背景
                skySystem.draw(canvas)

                // 2) 霧
                fogSystem.update(screenWidth, screenHeight, touchX, touchY, isTouching)
                fogSystem.draw(canvas)

                // 3) パーティクル
                particleSystem.update(screenWidth, screenHeight)
                particleSystem.draw(canvas)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (canvas != null) {
                try {
                    holder.unlockCanvasAndPost(canvas)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private companion object {
        private const val FRAME_DELAY_MS = 16L
    }
}
