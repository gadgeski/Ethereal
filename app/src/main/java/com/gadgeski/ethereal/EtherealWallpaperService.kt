package com.gadgeski.ethereal

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.opengl.GLSurfaceView
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.gadgeski.ethereal.renderer.EtherealGLRenderer

class EtherealWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return EtherealEngine()
    }

    inner class EtherealEngine : Engine(), SensorEventListener {

        private val renderer = EtherealGLRenderer(applicationContext)
        private var glSurfaceView: WallpaperGLSurfaceView? = null

        private var sensorManager: SensorManager? = null
        private var accelerometer: Sensor? = null

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            setTouchEventsEnabled(true)

            glSurfaceView = WallpaperGLSurfaceView(applicationContext).also { view ->
                view.setEGLContextClientVersion(2)
                view.setRenderer(renderer)
                view.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
            }

            sensorManager = getSystemService(SENSOR_SERVICE) as? SensorManager
            accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            glSurfaceView?.onPause()
            glSurfaceView = null
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            renderer.onVisibilityChanged(visible)

            if (visible) {
                glSurfaceView?.onResume()
                accelerometer?.let {
                    sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
                }
            } else {
                glSurfaceView?.onPause()
                sensorManager?.unregisterListener(this)
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
            renderer.onOffsetsChanged(xOffset, yOffset)
        }

        override fun onTouchEvent(event: MotionEvent?) {
            super.onTouchEvent(event)
            event?.let { renderer.onTouchEvent(it) }
        }

        override fun onSensorChanged(event: SensorEvent?) {
            event ?: return
            val gx = -event.values[0]
            val gy = event.values[1]
            renderer.updateGravity(gx, gy)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        // WallpaperServiceのSurfaceHolderをGLSurfaceViewに橋渡しするクラス
        inner class WallpaperGLSurfaceView(
            context: android.content.Context
        ) : GLSurfaceView(context) {
            override fun getHolder(): SurfaceHolder = surfaceHolder
        }
    }
}