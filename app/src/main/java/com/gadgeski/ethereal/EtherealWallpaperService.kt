package com.gadgeski.ethereal

import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.gadgeski.ethereal.renderer.EtherealRenderer

class EtherealWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return EtherealEngine()
    }

    inner class EtherealEngine : Engine() {

        // レンダラーのインスタンス生成
        // ここで applicationContext を渡すことで "No value passed for parameter 'context'" を解消
        private val renderer = EtherealRenderer(applicationContext)

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            // タッチイベントを受け取る設定
            setTouchEventsEnabled(true)
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            // レンダラーに SurfaceHolder を渡す
            renderer.onSurfaceCreated(holder)
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            // レンダラーにサイズ変更を通知
            // (以前の setSurfaceSize の代わり)
            renderer.onSurfaceChanged(holder, width, height)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            // レンダラーに破棄を通知
            renderer.onSurfaceDestroyed()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            // 表示/非表示の状態を通知 (描画ループの開始/停止を制御)
            renderer.onVisibilityChanged(visible)
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
            // 視差効果のためのオフセット通知
            // これを呼ぶことでレンダラー側の "never used" 警告も解消します
            renderer.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset)
        }

        override fun onTouchEvent(event: MotionEvent?) {
            super.onTouchEvent(event)
            event?.let {
                // タッチイベントを通知
                // (以前の updateTouch の代わり)
                renderer.onTouchEvent(it)
            }
        }
    }
}