package com.fido.common.common_utils.customview.custom

/**
 * @author: FiDo
 * @date: 2025/3/14
 * @des:
 */
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.fido.common.R
import kotlin.math.sqrt

class MapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 地图图片
    private var mapBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bg)

    // 用于缩放和移动的矩阵
    private val matrix = Matrix()
    private val savedMatrix = Matrix()

    // 手势检测
    private var mode = NONE
    private val start = PointF()
    private val mid = PointF()
    private var oldDist = 1f

    companion object {
        private const val NONE = 0
        private const val DRAG = 1
        private const val ZOOM = 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制地图
        canvas.drawBitmap(mapBitmap, matrix, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                // 单点触控，开始拖拽
                savedMatrix.set(matrix)
                start.set(event.x, event.y)
                mode = DRAG
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                // 多点触控，开始缩放
                oldDist = spacing(event)
                if (oldDist > 10f) {
                    savedMatrix.set(matrix)
                    midPoint(mid, event)
                    mode = ZOOM
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mode == DRAG) {
                    // 拖拽地图
                    matrix.set(savedMatrix)
                    matrix.postTranslate(event.x - start.x, event.y - start.y)
                } else if (mode == ZOOM) {
                    // 缩放地图
                    val newDist = spacing(event)
                    if (newDist > 10f) {
                        matrix.set(savedMatrix)
                        val scale = newDist / oldDist
                        matrix.postScale(scale, scale, mid.x, mid.y)
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                // 结束手势
                mode = NONE
            }
        }
        // 重绘视图
        invalidate()
        return true
    }

    // 计算两点之间的距离
    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y)
    }

    // 计算两点的中点
    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }
}