package com.gadgeski.igniter.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class HexGridSystem {

    private val paint = Paint().apply {
        color = 0xFF00E5FF.toInt() // Cyan
        style = Paint.Style.STROKE
        strokeWidth = 2f // ~1dp
        isAntiAlias = true
    }

    private val path = Path()
    private val hexRadius = 150f // Size of hexagon

    fun resize(width: Int, height: Int) {
        path.reset()
        
        // Calculate grid layout
        // Horizontal distance between centers = 3/2 * radius
        // Vertical distance between centers = sqrt(3) * radius
        
        val horizDist = 1.5f * hexRadius
        val vertDist = sqrt(3f) * hexRadius
        
        val cols = (width / horizDist).toInt() + 2
        val rows = (height / vertDist).toInt() + 2

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                // Offset every odd column
                val xOffset = col * horizDist
                val yOffset = row * vertDist + if (col % 2 == 1) vertDist / 2f else 0f
                
                drawHexagon(xOffset, yOffset)
            }
        }
    }

    private fun drawHexagon(centerX: Float, centerY: Float) {
        // Draw one hexagon path at center
        for (i in 0 until 6) {
            val angle = Math.toRadians((60 * i).toDouble())
            val x = (centerX + hexRadius * cos(angle)).toFloat()
            val y = (centerY + hexRadius * sin(angle)).toFloat()
            
            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()
    }

    fun draw(canvas: Canvas) {
        // Pulse Effect
        val time = System.currentTimeMillis()
        val pulse = (sin(time / 1000.0) + 1) / 2 // 0.0 to 1.0
        val alpha = (30 + (pulse * 70)).toInt() // 30 to 100 alpha
        
        paint.alpha = alpha
        canvas.drawPath(path, paint)
    }
}
