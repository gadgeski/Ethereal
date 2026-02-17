package com.gadgeski.ethereal

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.gadgeski.ethereal.renderer.EtherealRenderer

class EtherealWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return EtherealEngine()
    }

    inner class EtherealEngine : Engine(), SensorEventListener {

        // レンダラーのインスタンス生成
        private val renderer = EtherealRenderer(applicationContext)

        // センサー管理
        private var sensorManager: SensorManager? = null
        private var accelerometer: Sensor? = null

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            setTouchEventsEnabled(true)

            // SensorManager を取得し加速度センサーを保持
            sensorManager = getSystemService(SENSOR_SERVICE) as? SensorManager
            accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            renderer.onSurfaceCreated(holder)
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            renderer.onSurfaceChanged(holder, width, height)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            renderer.onSurfaceDestroyed()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            renderer.onVisibilityChanged(visible)

            // バッテリー節約: 表示中のみセンサーリスナーを登録
            if (visible) {
                accelerometer?.let {
                    sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
                }
            } else {
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
            renderer.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset)
        }

        override fun onTouchEvent(event: MotionEvent?) {
            super.onTouchEvent(event)
            event?.let { renderer.onTouchEvent(it) }
        }

        // --- SensorEventListener ---

        override fun onSensorChanged(event: SensorEvent?) {
            event ?: return
            // Canvas座標系に変換: 右=+X, 下=+Y
            // Sensor X は左側が下のとき正 → 反転して渡す
            // Sensor Y は下側が下のとき正 → そのまま渡す
            val gx = -event.values[0]
            val gy = event.values[1]
            renderer.updateGravity(gx, gy)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // 精度変更時の処理は不要
        }
    }
}