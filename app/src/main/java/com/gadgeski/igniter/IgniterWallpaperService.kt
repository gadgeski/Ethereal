package com.gadgeski.igniter

import android.service.wallpaper.WallpaperService
import android.util.Log
import android.view.SurfaceHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IgniterWallpaperService : WallpaperService() {

    companion object {
        private const val TAG = "IgniterWallpaperService"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Service created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Service destroyed")
    }

    override fun onCreateEngine(): Engine {
        Log.d(TAG, "onCreateEngine: Creating new engine")
        return IgniterEngine()
    }

    inner class IgniterEngine : Engine() {

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            Log.d(TAG, "Engine.onCreate: Engine created")
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            Log.d(TAG, "Engine.onVisibilityChanged: visible=$visible")
            if (visible) {
                draw()
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            Log.d(TAG, "Engine.onDestroy: Engine destroyed")
        }
        
        private fun draw() {
            // Placeholder for drawing logic
            // Ideally we would want to draw black screen here as requested
            val holder = surfaceHolder
            val canvas = holder.lockCanvas()
            if (canvas != null) {
                try {
                    canvas.drawColor(android.graphics.Color.BLACK)
                } finally {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }
}
