package com.gadgeski.ethereal

import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import android.service.wallpaper.WallpaperService
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.gadgeski.ethereal.opengl.EglHelper
import com.gadgeski.ethereal.renderer.EtherealGLRenderer
import com.gadgeski.ethereal.settings.SettingsActivity
import com.gadgeski.ethereal.settings.WallpaperTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class EtherealWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return EtherealEngine()
    }

    inner class EtherealEngine : Engine(), SensorEventListener {

        private val renderer = EtherealGLRenderer(applicationContext)
        private val eglHelper = EglHelper()

        private val glExecutor = Executors.newSingleThreadExecutor()
        private val glDispatcher = glExecutor.asCoroutineDispatcher()
        private val scope = CoroutineScope(SupervisorJob() + glDispatcher)

        private var drawJob: Job? = null

        @Volatile
        private var surfaceReady = false

        @Volatile
        private var engineVisible = false

        private var sensorManager: SensorManager? = null
        private var accelerometer: Sensor? = null

        private lateinit var prefs: SharedPreferences
        private val prefsListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == SettingsActivity.KEY_SELECTED_THEME) {
                    val themeName = sharedPreferences.getString(
                        SettingsActivity.KEY_SELECTED_THEME,
                        WallpaperTheme.GLITCH_SUNSET.name
                    )
                    val theme = WallpaperTheme.fromName(themeName)

                    scope.launch {
                        if (surfaceReady && eglHelper.isReady) {
                            renderer.setTheme(theme)
                        }
                    }
                }
            }

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            setTouchEventsEnabled(true)

            prefs = applicationContext.getSharedPreferences(SettingsActivity.PREFS_NAME, MODE_PRIVATE)
            prefs.registerOnSharedPreferenceChangeListener(prefsListener)

            sensorManager = getSystemService(SENSOR_SERVICE) as? SensorManager
            accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }

        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
            scope.launch {
                if (holder == null) return@launch

                val success = eglHelper.init(holder)
                if (!success) return@launch

                renderer.onSurfaceCreated(null, null)

                val themeName = prefs.getString(
                    SettingsActivity.KEY_SELECTED_THEME,
                    WallpaperTheme.GLITCH_SUNSET.name
                )
                renderer.setTheme(WallpaperTheme.fromName(themeName))

                val size = holder.surfaceFrame
                renderer.onSurfaceChanged(null, size.width(), size.height())

                surfaceReady = true

                if (engineVisible) {
                    startDrawingLoop()
                }
            }
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder?,
            format: Int,
            width: Int,
            height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
            scope.launch {
                if (surfaceReady && eglHelper.isReady) {
                    renderer.onSurfaceChanged(null, width, height)
                }
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            super.onSurfaceDestroyed(holder)
            surfaceReady = false
            stopDrawingLoop()

            runBlocking(glDispatcher) {
                renderer.release()
                eglHelper.destroySurface()
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            engineVisible = visible

            if (visible) {
                sensorManager?.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_GAME
                )
                if (surfaceReady && eglHelper.isReady) {
                    startDrawingLoop()
                }
            } else {
                sensorManager?.unregisterListener(this)
                stopDrawingLoop()
            }
        }

        override fun onOffsetsChanged(
            xOffset: Float,
            yOffset: Float,
            xOffsetStep: Float,
            yOffsetStep: Float,
            xPixelOffset: Int,
            yPixelOffset: Int
        ) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset)
            scope.launch {
                renderer.onOffsetsChanged(xOffset, yOffset)
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            prefs.unregisterOnSharedPreferenceChangeListener(prefsListener)
            sensorManager?.unregisterListener(this)

            stopDrawingLoop()
            surfaceReady = false

            runBlocking(glDispatcher) {
                renderer.release()
                eglHelper.release()
            }

            scope.cancel()
            glExecutor.shutdown()
        }

        override fun onTouchEvent(event: MotionEvent?) {
            super.onTouchEvent(event)
            event?.let { e ->
                val eventCopy = MotionEvent.obtain(e)
                scope.launch {
                    renderer.onTouchEvent(eventCopy)
                    eventCopy.recycle()
                }
            }
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type != Sensor.TYPE_ACCELEROMETER) return
            val gx = -event.values[0]
            val gy = event.values[1]
            scope.launch {
                renderer.updateGravity(gx, gy)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        private fun startDrawingLoop() {
            if (drawJob?.isActive == true) return

            drawJob = scope.launch {
                while (isActive) {
                    val frameStartMs = SystemClock.elapsedRealtime()

                    if (engineVisible && surfaceReady && eglHelper.isReady) {
                        try {
                            renderer.onDrawFrame(null)
                            eglHelper.swapBuffers()
                        } catch (e: Exception) {
                            Log.e("EtherealWallpaperService", "Error in drawFrame", e)
                        }
                    }

                    val frameElapsedMs = SystemClock.elapsedRealtime() - frameStartMs
                    val sleepMs = (16L - frameElapsedMs).coerceAtLeast(0L)
                    delay(sleepMs)
                }
            }
        }

        private fun stopDrawingLoop() {
            drawJob?.cancel()
            drawJob = null
        }
    }
}