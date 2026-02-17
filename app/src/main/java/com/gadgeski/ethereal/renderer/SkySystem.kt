package com.gadgeski.ethereal.renderer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import androidx.core.graphics.withTranslation
import com.gadgeski.ethereal.R
import kotlin.math.abs

class SkySystem(context: Context) {

    private val skyBitmap: Bitmap
    private val srcRect: Rect
    private val dstRect = Rect()
    // 警告が出ないよう、適切なフラグ設定
    private val paint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)

    private var screenWidth = 0
    private var screenHeight = 0

    private var xOffset: Float = 0f
    private val parallaxFactor = 0.3f
    private val scale = 1.3f

    // --- Aurora Shift (Smoothing Logic) ---
    // 生のセンサー値（目標値）
    private var targetGravityX: Float = 0f
    // 描画用に滑らかにした値（現在値）
    private var smoothedGravityX: Float = 0f
    // 追従係数 (0.05f = 毎フレーム5%ずつ目標に近づく)
    private val smoothingFactor = 0.05f

    init {
        val original = BitmapFactory.decodeResource(context.resources, R.drawable.ethereal_bg)
        skyBitmap = original
        srcRect = Rect(0, 0, original.width, original.height)
    }

    fun updateSize(width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
        // 描画先領域を更新（毎回newしない最適化）
        dstRect.set(0, 0, width, height)
    }

    fun setParallax(offset: Float) {
        xOffset = offset
    }

    @Suppress("UNUSED_PARAMETER")
    fun onTouch(event: MotionEvent) {
        // 将来拡張用
    }

    /** センサー重力X値を更新する (Aurora Shift 用) */
    fun updateGravity(gx: Float) {
        // ここでは「目標値」をセットするだけ
        this.targetGravityX = gx
    }

    fun draw(canvas: Canvas) {
        if (screenWidth == 0 || screenHeight == 0) return

        // 1. スムージング計算 (ここが滑らかさの肝です)
        smoothedGravityX += (targetGravityX - smoothedGravityX) * smoothingFactor

        val scaledWidth = screenWidth * scale
        val scaledHeight = screenHeight * scale
        val baseX = (screenWidth - scaledWidth) / 2f
        val baseY = (screenHeight - scaledHeight) / 2f
        val parallaxX = -xOffset * screenWidth * parallaxFactor

        // KTXのスコープ関数を使用して記述（美しい！）
        canvas.withTranslation(baseX + parallaxX, baseY) {
            // このブロック内の `this` は Canvas インスタンス
            scale(scale, scale)
            drawBitmap(skyBitmap, srcRect, dstRect, paint)

            // Aurora Shift: 傾きに応じた色オーバーレイ
            // smoothedGravityX を使用してチカチカを防止
            val intensity = (abs(smoothedGravityX) * 2.0f).coerceIn(0f, 1f)
            val alpha = (intensity * 80).toInt()

            if (alpha > 0) {
                val color = if (smoothedGravityX > 0) {
                    // 右傾き → Cyan
                    Color.argb(alpha, 0, 255, 255)
                } else {
                    // 左傾き → Magenta
                    Color.argb(alpha, 255, 0, 255)
                }
                drawColor(color)
            }
        }
    }
}