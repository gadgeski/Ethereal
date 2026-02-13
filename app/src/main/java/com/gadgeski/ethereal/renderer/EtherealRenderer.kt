package com.gadgeski.ethereal.renderer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.gadgeski.ethereal.renderer.SkySystem
import com.gadgeski.ethereal.renderer.ParticleSystem
import com.gadgeski.ethereal.renderer.FogSystem

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

    // 光の粒子のシステム
    private val particleSystem = ParticleSystem()

    // パーティクル用のPaint
    private val particlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        // 光が発光しているように見せるため、白〜水色系にするのがオススメ
        // ここではパーティクル個別の色を使う設定にします
    }

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

    // 修正ポイント: パララックス実装待ちのため警告を抑制
    @Suppress("UNUSED_PARAMETER")
    fun onOffsetsChanged(xOffset: Float, yOffset: Float, xOffsetStep: Float, yOffsetStep: Float, xPixelOffset: Int, yPixelOffset: Int) {
        // ホーム画面のスクロールに合わせて視差効果（パララックス）を入れる場合はここに記述
        // 将来的に skySystem.setParallax(xOffset) のように使う予定
    }

    fun onTouchEvent(event: MotionEvent) {
        val x = event.x
        val y = event.y

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
                // 2. 霧（FogSystem）の更新と描画 (NEW)
                // -------------------------------------------------
                fogSystem.update(screenWidth, screenHeight)
                fogSystem.draw(canvas)

                // -------------------------------------------------
                // 3. パーティクルの更新と描画
                // -------------------------------------------------
                // 物理演算の更新
                particleSystem.update(screenWidth, screenHeight)

                // 描画ループ
                for (p in particleSystem.particles) {
                    if (!p.isActive) continue

                    // 寿命(0.0~1.0)をアルファ値(0~255)に変換
                    val alpha = (p.life.coerceIn(0f, 1f) * 255f).toInt()

                    particlePaint.color = p.color
                    particlePaint.alpha = alpha

                    // strokeWidth を粒子の半径として使用
                    val radius = (p.strokeWidth * 0.5f).coerceAtLeast(1f)

                    canvas.drawCircle(p.x, p.y, radius, particlePaint)
                }
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