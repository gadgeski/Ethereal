package com.gadgeski.ethereal.renderer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import androidx.core.graphics.withTranslation
import com.gadgeski.ethereal.R

class SkySystem(context: Context) {

    private val skyBitmap: Bitmap
    private val srcRect: Rect
    private val dstRect = Rect()
    private val paint = Paint(Paint.FILTER_BITMAP_FLAG)

    private var screenWidth = 0
    private var screenHeight = 0

    private var xOffset: Float = 0f
    private val parallaxFactor = 0.3f
    private val scale = 1.3f

    init {
        val original = BitmapFactory.decodeResource(context.resources, R.drawable.ethereal_bg)
        skyBitmap = original
        srcRect = Rect(0, 0, original.width, original.height)
    }

    fun updateSize(width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
        dstRect.set(0, 0, width, height)
    }

    // ← EtherealRenderer から呼ばれる想定API
    fun setParallax(offset: Float) {
        xOffset = offset
    }

    // ← EtherealRenderer から呼ばれる想定API
    @Suppress("UNUSED_PARAMETER")
    fun onTouch(event: MotionEvent) {
        // 将来拡張用
    }

    fun draw(canvas: Canvas) {
        if (screenWidth == 0 || screenHeight == 0) return

        val scaledWidth = screenWidth * scale
        val scaledHeight = screenHeight * scale

        val baseX = (screenWidth - scaledWidth) / 2f
        val baseY = (screenHeight - scaledHeight) / 2f
        val parallaxX = -xOffset * screenWidth * parallaxFactor

        canvas.withTranslation(baseX + parallaxX, baseY) {
            scale(scale, scale)
            drawBitmap(skyBitmap, srcRect, dstRect, paint)
        }
    }
}
