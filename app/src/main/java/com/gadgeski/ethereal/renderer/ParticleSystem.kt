package com.gadgeski.ethereal.renderer

// Particleクラスのパッケージパスは環境に合わせて確認してください
import com.gadgeski.ethereal.model.Particle
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class ParticleSystem {

    companion object {
        // Ethereal (幽玄) なパラメータへ変更
        private const val POOL_SIZE = 500       // 多すぎるとクドいので少し減らす
        private const val BURST_COUNT = 15      // 爆発(50)ではなく、魔法の粉が舞うように(15)
        private const val SPEED_MIN = 1.0f      // ゆっくり
        private const val SPEED_MAX = 4.0f      // ふわっと

        // Ethereal カラーパレット (背景の空に溶け込む色)
        private const val COLOR_GLOW_WHITE = 0xFFFFFFFF.toInt() // 発光する白
        private const val COLOR_PALE_CYAN  = 0xCC88EEFF.toInt() // 半透明の淡いシアン
        private const val COLOR_DEEP_AQUA  = 0x9944FFCC.toInt() // 少し緑がかった水色
    }

    val particles = Array(POOL_SIZE) { Particle() }

    fun update(width: Int, height: Int) {
        particles.forEach { p ->
            if (p.isActive) {
                // 独自の物理演算（Particleクラスのupdateを使わずにここで制御してもOK）
                p.x += p.dx
                p.y += p.dy

                // 幻想的な空気抵抗と浮力（上に向かってゆっくり昇っていく）
                p.dx *= 0.95f // 横方向はすぐに減速
                p.dy -= 0.05f // 上方向への浮力（マイナスy方向）

                // 壁でのバウンドは廃止。画面外に出たら寿命を強制的に尽きさせる
                if (p.x < -50f || p.x > width + 50f || p.y < -50f || p.y > height + 50f) {
                    p.isActive = false
                }

                // ゆっくりフェードアウト (寿命の減りを遅く)
                if (p.life > 0) {
                    p.life -= 0.015f // 減衰速度。小さいほど長持ちする
                    if (p.life <= 0) p.isActive = false
                }
            }
        }
    }

    fun ignite(startX: Float, startY: Float) {
        var spawned = 0
        for (i in particles.indices) {
            if (!particles[i].isActive) {
                // 円形にふわっと広がる角度
                val angle = Random.nextDouble() * 2 * Math.PI
                val speed = Random.nextDouble(SPEED_MIN.toDouble(), SPEED_MAX.toDouble()).toFloat()

                val dx = (cos(angle) * speed).toFloat()
                val dy = (sin(angle) * speed).toFloat()

                // 色の抽選（白が一番レア、次いでアクア、シアン）
                val randColor = Random.nextDouble()
                val color = when {
                    randColor < 0.2 -> COLOR_GLOW_WHITE
                    randColor < 0.5 -> COLOR_DEEP_AQUA
                    else -> COLOR_PALE_CYAN
                }

                // 粒の大きさ（細かくて繊細な光）
                val strokeWidth = Random.nextDouble(2.0, 8.0).toFloat()

                // 寿命
                val life = Random.nextDouble(0.8, 1.2).toFloat()

                // Particleクラスのreset関数の引数仕様に合わせています
                particles[i].reset(startX, startY, dx, dy, color, life, strokeWidth)

                spawned++
                if (spawned >= BURST_COUNT) break
            }
        }
    }
}