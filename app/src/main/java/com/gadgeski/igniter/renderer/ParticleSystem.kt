package com.gadgeski.igniter.renderer

import android.graphics.Color
import com.gadgeski.igniter.model.Particle
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class ParticleSystem {

    companion object {
        // Limit Break Parameters
        private const val POOL_SIZE = 1000
        private const val EXPLOSION_COUNT = 50
        private const val SPEED_MIN = 15f
        private const val SPEED_MAX = 30f // Super fast
        
        // Colors from Design Rules
        private const val COLOR_CYAN = 0xFF00E5FF.toInt()
        private const val COLOR_MAGENTA = 0xFFD500F9.toInt()
        private const val COLOR_WHITE = 0xFFFFFFFF.toInt()
    }

    // Fixed pool of particles
    val particles = Array(POOL_SIZE) { Particle() }

    fun update(width: Int, height: Int) {
        particles.forEach { p ->
            if (p.isActive) {
                p.update()
                
                // Wall Bounce Logic
                var bounced = false
                if (p.x <= 0 || p.x >= width) {
                    p.dx = -p.dx
                    bounced = true
                }
                if (p.y <= 0 || p.y >= height) {
                    p.dy = -p.dy
                    bounced = true
                }

                if (bounced) {
                    // Randomly switch color on bounce (White stays White -> or maybe switch to standard?)
                    // Let's keep White special, but standard particles switch.
                    if (p.color != COLOR_WHITE) {
                        p.color = if (Random.nextBoolean()) COLOR_CYAN else COLOR_MAGENTA
                    }
                }
                
                // Slower decay for longer trails
                if (p.life > 0) {
                     p.life -= 0.005f
                     if (p.life <= 0) p.isActive = false
                }
            }
        }
    }

    fun ignite(startX: Float, startY: Float) {
        var spawned = 0
        // Find inactive particles to respawn
        for (i in particles.indices) {
            if (!particles[i].isActive) {
                val angle = Random.nextDouble() * 2 * Math.PI
                val speed = Random.nextDouble(SPEED_MIN.toDouble(), SPEED_MAX.toDouble()).toFloat()
                
                val dx = (cos(angle) * speed).toFloat()
                val dy = (sin(angle) * speed).toFloat()
                
                // Rare Spark (5%)
                val isRare = Random.nextDouble() < 0.05
                val color = if (isRare) COLOR_WHITE else (if (Random.nextBoolean()) COLOR_CYAN else COLOR_MAGENTA)
                
                // Randomized Stroke Width (1dp ~ 4dp roughly)
                val strokeWidth = Random.nextDouble(3.0, 12.0).toFloat()
                
                // Slightly randomized life
                val life = Random.nextDouble(0.8, 1.0).toFloat()

                particles[i].reset(startX, startY, dx, dy, color, life, strokeWidth)
                
                spawned++
                if (spawned >= EXPLOSION_COUNT) break
            }
        }
    }
}
