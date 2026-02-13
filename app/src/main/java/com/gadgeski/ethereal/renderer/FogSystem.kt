package com.gadgeski.ethereal.renderer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import androidx.core.graphics.createBitmap
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation
import kotlin.random.Random

class FogSystem {

    companion object {
        // 定数は companion object に入れ、大文字スネークケースにするのが規約です
        private const val PARTICLE_COUNT = 12
    }

    private val fogParticles = mutableListOf<FogParticle>()
    private var cloudBitmap: Bitmap? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        // 1. "雲の素"となるぼやけた円の画像を生成する (256x256)
        val size = 256

        // KTX: createBitmap を使用 (Bitmap.createBitmap のラッパー)
        // インポート: androidx.core.graphics.createBitmap
        cloudBitmap = createBitmap(size, size, Bitmap.Config.ARGB_8888)

        // cloudBitmapはnull許容型ですが、createBitmap直後なので !! でアンラップしてCanvasに渡します
        val canvas = Canvas(cloudBitmap!!)

        val gradient = RadialGradient(
            size / 2f, size / 2f, size / 2f,
            Color.WHITE,
            Color.TRANSPARENT,
            Shader.TileMode.CLAMP
        )
        val cloudPaint = Paint().apply {
            shader = gradient
            isDither = true
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, cloudPaint)

        // 2. 霧のインスタンス生成
        // "Parameter 'i' is never used" を解消するため、repeat を使用
        repeat(PARTICLE_COUNT) {
            fogParticles.add(FogParticle())
        }
    }

    fun updateSize(width: Int, height: Int) {
        // 画面サイズに合わせて霧をランダム配置し直す
        fogParticles.forEach { p ->
            p.reset(width, height)
        }
    }

    fun update(width: Int, height: Int, touchX: Float, touchY: Float, isTouching: Boolean) {
        fogParticles.forEach { p ->
            // 1. Basic movement
            p.x += p.speed
            p.x += p.vx
            p.y += p.vy

            // 2. Repulsion logic
            if (isTouching) {
                val dx = p.x - touchX
                val dy = p.y - touchY
                val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                
                // Interaction radius (e.g. 300px)
                val radius = 300f 
                
                if (dist < radius && dist > 1f) {
                    val force = (1.0f - dist / radius) * 1.5f // Strength of repulsion
                    p.vx += (dx / dist) * force
                    p.vy += (dy / dist) * force
                }
            }

            // 3. Damping (friction)
            p.vx *= 0.95f
            p.vy *= 0.95f

            // 4. Looping
            val margin = 200f * p.scale
            if (p.x > width + margin) {
                p.x = -margin
                p.y = Random.nextFloat() * height
                // Reset velocity when respawning
                p.vx = 0f
                p.vy = 0f
            }
        }
    }

    fun draw(canvas: Canvas) {
        val bitmap = cloudBitmap ?: return

        fogParticles.forEach { p ->
            paint.alpha = p.alpha

            // KTX: withTranslation, withScale を使用
            // save() / restore() のブロック忘れを防げます
            canvas.withTranslation(p.x, p.y) {
                canvas.withScale(p.scale, p.scale) {
                    // 中心を基準に描画
                    canvas.drawBitmap(bitmap, -128f, -128f, paint)
                }
            }
        }
    }

    // 内部クラス：霧の粒ひとつのデータ
    private class FogParticle {
        var x: Float = 0f
        var y: Float = 0f
        var speed: Float = 0f
        var scale: Float = 1f
        var alpha: Int = 0
        
        // Velocity for physics integration
        var vx: Float = 0f
        var vy: Float = 0f

        // "Parameter 'w' is never used" を解消
        // X座標の初期化にも w を使うように変更
        fun reset(w: Int, h: Int) {
            x = Random.nextFloat() * w // 画面横幅のどこかにランダム配置
            y = Random.nextFloat() * h

            // 手前（大きく、速く）〜 奥（小さく、遅く）
            scale = Random.nextFloat() * 2.5f + 1.5f
            speed = Random.nextFloat() * 0.6f + 0.2f

            // 薄い霧
            alpha = Random.nextInt(10, 40)
            
            vx = 0f
            vy = 0f
        }
    }
}