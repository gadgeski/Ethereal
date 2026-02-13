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

    // 描画ループ用のハンドラ
    private val handler = Handler(Looper.getMainLooper())

    // 画面サイズ
    private var screenWidth = 0
    private var screenHeight = 0

    // --- 各システムの定義 ---

    // 背景（空）のシステム
    private lateinit var skySystem: SkySystem

    // 霧のシステム（NEW）
    private val fogSystem = FogSystem()

    // パーティクルのシステム
    private val particleSystem = ParticleSystem()

    // 描画ループ
    private val drawRunner = object : Runnable {
        override fun run() {
            draw()
            if (isVisible) {
                // 60FPSを目指す (1000ms / 60 ≈ 16ms)
                handler.postDelayed(this, 16)
            }
        }
    }

    fun onSurfaceCreated(holder: SurfaceHolder) {
        this.surfaceHolder = holder

        // SkySystemの初期化
        skySystem = SkySystem(context)

        // ParticleSystemの初期化（もし必要なら）
        // particleSystem.init()
    }

    fun onSurfaceChanged(holder: SurfaceHolder, width: Int, height: Int) {
        this.surfaceHolder = holder
        this.screenWidth = width
        this.screenHeight = height

        // 画面サイズが変わったらシステムに通知
        skySystem.updateSize(width, height)
        fogSystem.updateSize(width, height) // 霧の再配置
        // particleSystem.updateSize(width, height) // 必要であれば実装

        draw() // サイズ変更時に一度強制描画
    }

    fun onSurfaceDestroyed() {
        stop()
        this.surfaceHolder = null
    }

    fun onVisibilityChanged(visible: Boolean) {
        this.isVisible = visible
        if (visible) {
            start()
        } else {
            stop()
        }
    }

    // Touch state
    private var touchX = 0f
    private var touchY = 0f
    private var isTouching = false

    fun updateTouch(x: Float, y: Float, isTouching: Boolean) {
        this.touchX = x
        this.touchY = y
        this.isTouching = isTouching
    }

    // 修正ポイント: パララックス実装
    @Suppress("UNUSED_PARAMETER")
    fun onOffsetsChanged(xOffset: Float, yOffset: Float, xOffsetStep: Float, yOffsetStep: Float, xPixelOffset: Int, yPixelOffset: Int) {
        // ホーム画面のスクロールに合わせて視差効果（パララックス）を入れる
        skySystem.setParallax(xOffset)
        fogSystem.setParallax(xOffset)
        particleSystem.setParallax(xOffset)
    }

    fun onTouchEvent(event: MotionEvent) {
        val x = event.x
        val y = event.y

        // Update touch state for continuous interaction
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            updateTouch(x, y, false)
        } else {
            updateTouch(x, y, true)
        }

        // 空（雲）へのタッチ伝播
        skySystem.onTouch(event)

        // パーティクルの生成（タップまたはドラッグ中）
        if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
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
        var canvas: Canvas? = null

        try {
            canvas = holder.lockCanvas()
            if (canvas != null) {
                // -------------------------------------------------
                // 1. 背景（SkySystem）の描画
                // -------------------------------------------------
                // 画像を描画するので、drawRectでの塗りつぶしは不要
                skySystem.draw(canvas)

                // -------------------------------------------------
                // 2. 霧（FogSystem）の更新と描画
                // -------------------------------------------------
                fogSystem.update(screenWidth, screenHeight, touchX, touchY, isTouching)
                fogSystem.draw(canvas)

                // -------------------------------------------------
                // 3. パーティクルの更新と描画
                // -------------------------------------------------
                // 物理演算の更新
                particleSystem.update(screenWidth, screenHeight)

                // 描画 (ParticleSystemに移譲)
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
}