package com.example.testai.fireway2

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout

class SimpleTrackTestActivity : Activity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 创建根布局
        val rootLayout = FrameLayout(this).apply {
            setBackgroundColor(Color.WHITE)
        }
        
        // 创建四个圆形视图
        val circles = listOf(
            createCircleView(100, 100),
            createCircleView(700, 100),
            createCircleView(100, 1500),
            createCircleView(700, 1500)
        )
        
        circles.forEach { rootLayout.addView(it) }
        
        // 创建轨道视图
        val tracks = listOf(
            createTrackView(100, 100, 700, 100), // 顶部水平轨道
            createTrackView(700, 100, 700, 1500), // 右侧垂直轨道
            createTrackView(700, 1500, 100, 1500), // 底部水平轨道
            createTrackView(100, 1500, 100, 100), // 左侧垂直轨道
            createTrackView(100, 100, 700, 1500), // 对角线1
            createTrackView(700, 100, 100, 1500)  // 对角线2
        )
        
        tracks.forEach { rootLayout.addView(it) }
        
        setContentView(rootLayout)
        
        Log.d("SimpleTrackTest", "Created ${circles.size} circles and ${tracks.size} tracks")
    }
    
    private fun createCircleView(x: Int, y: Int): View {
        return View(this).apply {
            setBackgroundColor(Color.BLUE)
            layoutParams = FrameLayout.LayoutParams(100, 100).apply {
                leftMargin = x - 50
                topMargin = y - 50
            }
        }
    }
    
    private fun createTrackView(startX: Int, startY: Int, endX: Int, endY: Int): View {
        val centerX = (startX + endX) / 2
        val centerY = (startY + endY) / 2
        val length = kotlin.math.sqrt(((endX - startX) * (endX - startX) + (endY - startY) * (endY - startY)).toDouble()).toInt()
        val angle = kotlin.math.atan2((endY - startY).toDouble(), (endX - startX).toDouble()) * 180 / kotlin.math.PI
        
        return View(this).apply {
            setBackgroundColor(Color.RED)
            alpha = 0.8f
            layoutParams = FrameLayout.LayoutParams(length, 60).apply {
                leftMargin = centerX - length / 2
                topMargin = centerY - 30
            }
            rotation = angle.toFloat()
            elevation = 10f
        }
    }
}