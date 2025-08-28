package com.example.testai.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.testai.R

/**
 * 竖直柔光渐变光柱View
 * 可用于PK/VS分割线、能量光效等场景
 * 支持自定义渐变色、宽度和柔光强度
 */
class VerticalGlowBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 默认渐变色，可根据需求自定义
    private val gradientColors = intArrayOf(
        0x00FFD6E7.toInt(), // 顶部透明
        0x66FFD6E7.toInt(), // 渐变色
        0xFFFFC1E3.toInt(), // 中心高亮
        0x66FFD6E7.toInt(), // 渐变色
        0x00FFD6E7.toInt()  // 底部透明
    )
    private val gradientPositions = floatArrayOf(0f, 0.2f, 0.5f, 0.8f, 1f)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        // 创建竖直方向的线性渐变
        val shader = LinearGradient(
            width / 2, 0f, width / 2, height,
            gradientColors, gradientPositions, Shader.TileMode.CLAMP
        )
        paint.shader = shader
        // 绘制竖直柔光光柱
        canvas.drawRoundRect(
            0f, 0f, width, height,
            width / 2, width / 2, paint
        )
    }
}