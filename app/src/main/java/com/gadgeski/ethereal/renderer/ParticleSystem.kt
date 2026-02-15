package com.gadgeski.ethereal.renderer

import android.graphics.Canvas
import android.graphics.Paint
import com.gadgeski.ethereal.model.Particle
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class ParticleSystem {

    companion object {
        // Ethereal (幽玄) なパラメータ
        private const val POOL_SIZE = 2000
        // 一回のタップで出る量
        private const val BURST_COUNT = 40

        // 速度：初速を速くして「弾ける」感覚を強化
        private const val SPEED_MIN = 3.0f
        private const val SPEED_MAX = 18.0f

        // Ethereal カラーパレット
        private const val COLOR_GLOW_WHITE = 0xFFFFFFFF.toInt()
        private const val COLOR_NEON_CYAN  = 0xFF00FFFF.toInt()
        private const val COLOR_DEEP_BLUE  = 0xFF4400FF.toInt()
    }

    // 配列で事前確保
    val particles = Array(POOL_SIZE) { Particle() }

    // パララックス用オフセット (0.0 - 1.0)
    private var xOffset: Float = 0f

    // パーティクルの視差係数 (1.2 = 背景より手前にある演出)
    private val parallaxFactor = 1.2f

    // 描画用Paint
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    // 画面サイズ保持用
    private var currentWidth = 0f

    fun update(width: Int, height: Int) {
        currentWidth = width.toFloat()

        particles.forEach { p ->
            if (p.isActive) {
                // 物理演算
                p.x += p.dx
                p.y += p.dy

                // 強い空気抵抗（爆発した後にすぐ減速させる）
                p.dx *= 0.92f
                p.dy *= 0.92f

                // わずかな重力（または浮力）
                p.dy -= 0.02f // ゆっくり上昇

                // 画面外判定
                val margin = 500f
                if (p.x < -margin || p.x > width + margin || p.y < -margin || p.y > height + margin) {
                    p.isActive = false
                }

                // 寿命減衰
                if (p.life > 0) {
                    p.life -= 0.02f
                    if (p.life <= 0) p.isActive = false
                }
            }
        }
    }

    fun setParallax(offset: Float) {
        this.xOffset = offset
    }

    fun draw(canvas: Canvas) {
        val width = canvas.width.toFloat()

        for (p in particles) {
            if (!p.isActive) continue

            // アルファ値計算
            val alpha = (p.life.coerceIn(0f, 1f) * 255f).toInt()
            paint.color = p.color
            paint.alpha = alpha

            // サイズ計算
            val scale = if (p.life < 0.3f) p.life / 0.3f else 1.0f
            val radius = (p.strokeWidth * 0.5f * scale).coerceAtLeast(1f)

            // パララックス計算
            val visualX = p.x - (xOffset * width * parallaxFactor)

            canvas.drawCircle(visualX, p.y, radius, paint)
        }
    }

    fun ignite(touchX: Float, touchY: Float) {
        // 座標変換: 画面座標 -> ワールド座標
        val parallaxShift = xOffset * currentWidth * parallaxFactor
        val worldX = touchX + parallaxShift

        var spawnedCount = 0

        // 1. まず非アクティブなパーティクルを探して再利用
        for (i in particles.indices) {
            if (!particles[i].isActive) {
                spawnParticle(particles[i], worldX, touchY)
                spawnedCount++
                if (spawnedCount >= BURST_COUNT) return
            }
        }

        // 2. もし空きが足りず、まだ生成したい場合 (強制上書き)
        // ここに到達した時点で spawnedCount < BURST_COUNT は確定しているため
        // 条件分岐(if)は削除しました。
        val needed = BURST_COUNT - spawnedCount
        repeat(needed) {
            val randomIndex = Random.nextInt(POOL_SIZE)
            spawnParticle(particles[randomIndex], worldX, touchY)
        }
    }

    private fun spawnParticle(p: Particle, startX: Float, startY: Float) {
        val angle = Random.nextDouble() * 2 * Math.PI
        val speed = Random.nextDouble(SPEED_MIN.toDouble(), SPEED_MAX.toDouble()).toFloat()

        val dx = (cos(angle) * speed).toFloat()
        val dy = (sin(angle) * speed).toFloat()

        val randColor = Random.nextDouble()
        val color = when {
            randColor < 0.1 -> COLOR_GLOW_WHITE
            randColor < 0.4 -> COLOR_NEON_CYAN
            else -> COLOR_DEEP_BLUE
        }

        val strokeWidth = Random.nextDouble(3.0, 12.0).toFloat()
        val life = Random.nextDouble(0.6, 1.0).toFloat()

        p.reset(startX, startY, dx, dy, color, life, strokeWidth)
    }
}