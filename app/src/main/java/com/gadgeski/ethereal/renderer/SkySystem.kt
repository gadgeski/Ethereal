package com.gadgeski.ethereal.renderer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.view.MotionEvent
import com.gadgeski.ethereal.R

class SkySystem(context: Context) {

    // 背景画像
    private var bgBitmap: Bitmap? = null

    // 画像を描画するための変換マトリクス（毎フレーム計算しないためのキャッシュ）
    private val bgMatrix = Matrix()
    private val paint = Paint().apply {
        isFilterBitmap = true // 拡大縮小時のジャギ（粗さ）を滑らかにする
    }

    init {
        // res/drawable/ethereal_bg.png を読み込む
        try {
            val options = BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
            bgBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ethereal_bg, options)
        } catch (e: Exception) {
            e.printStackTrace()
            // メモリ不足などで読み込めなかった場合のフォールバック処理
        }
    }

    /**
     * 画面サイズが変わった時に1回だけ呼ばれる。
     * ここで Center Crop (画面を埋め尽くしつつアスペクト比を維持) の計算をする。
     */
    fun updateSize(viewWidth: Int, viewHeight: Int) {
        val bitmap = bgBitmap ?: return

        val bWidth = bitmap.width.toFloat()
        val bHeight = bitmap.height.toFloat()
        val vWidth = viewWidth.toFloat()
        val vHeight = viewHeight.toFloat()

        // 修正ポイント1: Redundant initializer の解消
        // 初期値を入れず、if-elseで確実に値を設定するように変更
        val scale: Float
        val dx: Float
        val dy: Float

        // 縦と横、どちらの縮尺に合わせれば画面を埋め尽くせるか計算
        if (bWidth * vHeight > vWidth * bHeight) {
            scale = vHeight / bHeight
            dx = (vWidth - bWidth * scale) * 0.5f
            dy = 0f
        } else {
            scale = vWidth / bWidth
            dx = 0f
            dy = (vHeight - bHeight * scale) * 0.5f
        }

        // マトリクスに計算結果をセット
        bgMatrix.setScale(scale, scale)
        bgMatrix.postTranslate(dx, dy)
    }

    // 修正ポイント2: Parameter is never used の解消
    // 現状は実装待ちのため、アノテーションで警告を抑制して意図を明示
    @Suppress("UNUSED_PARAMETER")
    fun onTouch(event: MotionEvent) {
        // TODO: 将来的に「雲が避ける」処理などをここに追加
        // event.x, event.y を使ってインタラクションを実装する予定
    }

    // パララックス用のオフセット (0.0 - 1.0)
    private var xOffset: Float = 0f

    fun setParallax(offset: Float) {
        this.xOffset = offset
    }

    fun draw(canvas: Canvas) {
        val bitmap = bgBitmap ?: return

        canvas.save()
        // 修正ポイント: パララックス効果 (Factor 0.1)
        // 背景はゆっくり動く（遠景）
        // widthが確定していない場合は0fになるが、通常drawはupdateSize後なので問題ない
        val width = canvas.width.toFloat()
        val translationX = -xOffset * width * 0.1f
        canvas.translate(translationX, 0f)

        // 事前計算したマトリクスを使って高速に描画
        canvas.drawBitmap(bitmap, bgMatrix, paint)

        canvas.restore()
    }
}