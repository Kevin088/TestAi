package com.example.testai.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

class ConnectionLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 2f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val circlePaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val circleRadius = 20f
    private val circlePositions = mutableListOf<Point>()
    private val rectangleViews = mutableListOf<View>()

    init {
        setWillNotDraw(false)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val width = right - left
        val height = bottom - top

        // 设置四个圆圈的位置
        circlePositions.clear()
        circlePositions.add(Point(width / 4, height / 4))     // 左上
        circlePositions.add(Point(width * 3 / 4, height / 4)) // 右上
        circlePositions.add(Point(width / 4, height * 3 / 4)) // 左下
        circlePositions.add(Point(width * 3 / 4, height * 3 / 4)) // 右下

        // 创建并布局矩形ViewGroup
        if (rectangleViews.isEmpty()) {
            createRectangleViews()
        }

        // 布局矩形ViewGroup
        layoutRectangleViews()
    }

    private fun createRectangleViews() {
        // 创建6个矩形ViewGroup
        for (i in 0..5) {
            val rectangleView = View(context).apply {
                setBackgroundColor(Color.LTGRAY)
                layoutParams = LayoutParams(0, 70)
            }
            rectangleViews.add(rectangleView)
            addView(rectangleView)
        }
    }

    private fun layoutRectangleViews() {
        var index = 0
        // 从每个圆圈连接到其他圆圈
        for (i in 0..3) {
            for (j in (i + 1)..3) {
                if (index < rectangleViews.size) {
                    val start = circlePositions[i]
                    val end = circlePositions[j]
                    val rectangleView = rectangleViews[index]

                    // 计算矩形的位置和旋转角度
                    val dx = end.x - start.x
                    val dy = end.y - start.y
                    val length = Math.sqrt((dx * dx + dy * dy).toDouble()).toInt()
                    val angle = Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble())).toFloat()

                    // 设置矩形的宽度为两点之间的距离
                    rectangleView.layoutParams.width = length

                    // 设置矩形的位置
                    rectangleView.x = start.x.toFloat()
                    rectangleView.y = start.y.toFloat() - rectangleView.layoutParams.height / 2
                    rectangleView.rotation = angle

                    index++
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制圆圈
        for (position in circlePositions) {
            canvas.drawCircle(
                position.x.toFloat(),
                position.y.toFloat(),
                circleRadius,
                circlePaint
            )
        }
    }
}