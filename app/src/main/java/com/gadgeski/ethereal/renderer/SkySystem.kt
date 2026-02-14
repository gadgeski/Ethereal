package com.gadgeski.ethereal.renderer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import com.gadgeski.ethereal.R

class SkySystem(context: Context) {

    private val skyBitmap: Bitmap
    private val paint = Paint(Paint.FILTER_BITMAP_FLAG)
    private var screenWidth = 0
    private var screenHeight = 0

    // パララックス用オフセット
    private var xOffset: Float = 0f
    // 背景の移動係数
    private val parallaxFactor = 0.3f
    // 拡大率
    private val scale = 1.2f

    init {
        val original = BitmapFactory.decodeResource(context.resources, R.drawable.ethereal_bg)
        skyBitmap = original
    }

    fun updateSize(width: Int, height: Int) {
        this.screenWidth = width
        this.screenHeight = height
    }

    fun setParallax(offset: Float) {
        this.xOffset = offset
    }

    // 将来的な拡張のために残すが、現在は未使用のため警告を抑制
    @Suppress("UNUSED_PARAMETER")
    fun onTouch(event: MotionEvent) {
        // 将来的に雲を動かすなどの拡張が可能
    }

    fun draw(canvas: Canvas) {
        if (screenWidth == 0 || screenHeight == 0) return

        val scaledWidth = screenWidth * scale
        val scaledHeight = screenHeight * scale

        val baseX = (screenWidth - scaledWidth) / 2f
        val baseY = (screenHeight - scaledHeight) / 2f

        val parallaxX = -xOffset * screenWidth * parallaxFactor

        // 標準APIを使用（KTX警告は無視または抑制）
        canvas.save()

        canvas.translate(baseX + parallaxX, baseY)
        canvas.scale(scale, scale)

        val srcRect = Rect(0, 0, skyBitmap.width, skyBitmap.height)
        val dstRect = Rect(0, 0, screenWidth, screenHeight)

        canvas.drawBitmap(skyBitmap, srcRect, dstRect, paint)

        canvas.restore()
    }
}