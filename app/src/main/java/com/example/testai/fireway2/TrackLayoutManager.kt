package com.example.testai.fireway2

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import kotlin.math.cos
import kotlin.math.sin

/**
 * 轨道布局管理器
 * 
 * 负责管理圆形视图之间的轨道连接，支持2-4个圆形视图的动态布局。
 * 提供轨道视图的创建、布局和管理功能。
 * 
 * @param context Android上下文
 * @param fireWayRoot FrameLayout根布局容器，用于承载轨道视图
 */
class TrackLayoutManager(
    private val context: Context,
    private val fireWayRoot: FrameLayout
) {

    companion object {
        private const val TRACK_WIDTH = 50
    }

    // 圆形视图列表
    private val circleViews = mutableListOf<View>()
    // 圆形视图位置列表
    private val circlePositions = mutableListOf<Point>()
    // 轨道视图列表
    private val trackViews = mutableListOf<FrameLayout>()
    // 初始化状态标志
    private var isInitialized = false

    /**
     * 设置圆形视图
     * 
     * @param circles 圆形视图列表，支持2-4个视图
     * @throws IllegalArgumentException 如果视图数量不在2-4范围内
     */
    fun setCircleViews(circles: List<View>) {
        require(circles.size in 2..4) { "圆形视图数量必须在2-4之间，当前数量: ${circles.size}" }
        circleViews.clear()
        circleViews.addAll(circles)
        isInitialized = false
    }
    
    /**
     * 初始化轨道布局
     * 
     * 在视图布局完成后调用，开始创建和布局轨道视图。
     */
    fun initialize() {
        if (!isInitialized && circleViews.isNotEmpty()) {
            initializeLayout()
        }
    }

    /**
     * 清理轨道视图
     * 
     * 从根视图中移除所有轨道视图并清空相关数据。
     */
    fun cleanup() {
        trackViews.forEach { trackView ->
            fireWayRoot.removeView(trackView)
        }
        trackViews.clear()
        circleViews.clear()
        circlePositions.clear()
        isInitialized = false
    }

    /**
     * 初始化布局
     * 
     * 创建轨道视图并开始布局圆形视图的位置计算。
     * 该方法在布局尺寸确定后被调用一次。
     */
    private fun initializeLayout() {
        createTrackViews()
        layoutCircleViews()
    }

    /**
     * 创建轨道视图
     * 
     * 根据圆形视图数量动态创建FrameLayout作为连接轨道，设置深灰色背景。
     * 轨道数量 = n*(n-1)/2，其中n为圆形视图数量。
     */
    private fun createTrackViews() {
        // 清理现有轨道视图
        trackViews.forEach { fireWayRoot.removeView(it) }
        trackViews.clear()
        
        val circleCount = circleViews.size
        if (circleCount < 2) {
            return
        }
        
        // 计算需要的轨道数量：n*(n-1)/2
        val trackCount = circleCount * (circleCount - 1) / 2
        
        repeat(trackCount) {
            val trackView = FrameLayout(context).apply {
                setBackgroundColor(Color.DKGRAY)
                visibility = View.VISIBLE
            }
            addTextToTracks(trackView)
            trackViews.add(trackView)
            // 不在这里添加到父容器，在layoutTrack方法中添加
        }
    }

    /**
     * 布局轨道视图
     * 
     * 根据圆形视图的位置计算并设置每条轨道的位置、大小和旋转角度。
     * 动态创建所有圆形视图之间的连接。
     */
    private fun layoutTrackViews() {
        val circleCount = circlePositions.size
        if (circleCount < 2 || trackViews.isEmpty()) {
            return
        }
        
        // 动态生成所有圆形视图之间的连接
        var trackIndex = 0
        for (i in 0 until circleCount) {
            for (j in (i + 1) until circleCount) {
                if (trackIndex < trackViews.size) {
                    layoutTrack(
                        trackViews[trackIndex],
                        circlePositions[i],
                        circlePositions[j]
                    )
                    trackIndex++
                }
            }
        }
    }
    
    /**
     * 布局单条轨道
     * 
     * 根据起始和结束点计算轨道的位置、大小和旋转角度。
     * 针对FrameLayout容器进行了优化，使用绝对定位确保轨道准确连接圆形视图。
     * 
     * @param trackView 要布局的轨道视图
     * @param start 起始点坐标
     * @param end 结束点坐标
     */
    private fun layoutTrack(trackView: FrameLayout, start: Point, end: Point) {
        val deltaX = end.x - start.x
        val deltaY = end.y - start.y
        val distance = kotlin.math.sqrt((deltaX * deltaX + deltaY * deltaY).toDouble()).toFloat()
        val angle = kotlin.math.atan2(deltaY.toDouble(), deltaX.toDouble()) * 180 / kotlin.math.PI
        Log.e("ssssss","angle:$angle")
        // 确保轨道视图已添加到FrameLayout容器中
        if (trackView.parent == null) {
            fireWayRoot.addView(trackView)
        }
        
        // 使用post确保视图已经被添加到FrameLayout布局中
        trackView.post {
            // 为FrameLayout设置轨道的宽度和高度
            val layoutParams = FrameLayout.LayoutParams(
                distance.toInt(),
                TRACK_WIDTH
            )
            trackView.layoutParams = layoutParams
            
            // 计算轨道的起始位置（左上角），在FrameLayout中使用绝对定位
            val trackStartX = start.x.toFloat()
            val trackStartY = start.y.toFloat() - TRACK_WIDTH / 2
            
            // 在FrameLayout中直接设置视图的绝对位置
            trackView.x = trackStartX
            trackView.y = trackStartY
            
            // 设置旋转角度
            trackView.rotation = angle.toFloat()
            
            // 设置旋转中心点为轨道的起始点中心，确保围绕连接点旋转
            trackView.pivotX = 0f
            trackView.pivotY = (TRACK_WIDTH / 2).toFloat()

            if (angle < 180 && angle > 90) {
                trackView.scaleY = -1f
            }
        }
    }

    
    /**
     * 布局圆形视图
     * 
     * 异步计算每个圆形视图的相对位置并存储到circlePositions中。
     * 使用view.post确保视图已完成布局后再进行位置计算。
     */
    private fun layoutCircleViews() {
        circlePositions.clear()
        
        if (circleViews.isEmpty()) return
        
        // 使用数组确保位置按索引顺序添加
        val tempPositions = arrayOfNulls<Point>(circleViews.size)
        var completedCount = 0
        
        circleViews.forEachIndexed { index, view ->
            view.post {
                // 获取圆形视图在屏幕上的绝对位置
                val viewLocation = IntArray(2)
                view.getLocationOnScreen(viewLocation)
                
                // 获取FrameLayout容器在屏幕上的绝对位置
                val frameLocation = IntArray(2)
                fireWayRoot.getLocationOnScreen(frameLocation)
                
                // 计算圆形视图相对于FrameLayout容器的位置
                val relativeX = viewLocation[0] - frameLocation[0] + view.width / 2
                val relativeY = viewLocation[1] - frameLocation[1] + view.height / 2
                
                tempPositions[index] = Point(relativeX, relativeY)
                completedCount++
                
                // 当所有位置计算完成后，按顺序添加到circlePositions
                if (completedCount == circleViews.size) {
                    circlePositions.clear()
                    tempPositions.forEach { point ->
                        point?.let { circlePositions.add(it) }
                    }
                    
                    fireWayRoot.post {
                        layoutTrackViews()
                        isInitialized = true
                    }
                }
            }
        }
    }


    /**
     * 添加文字到轨道视图
     * 
     * 为指定的轨道视图添加居中显示的"12345"文字。
     * 
     * @param parent 要添加文字的轨道视图
     */
    fun addTextToTracks(parent: FrameLayout) {
        // 清除现有子视图
        parent.removeAllViews()

        // 创建TextView
        val textView = TextView(context).apply {
            text = "12345"
            textSize = 12f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        // 设置LayoutParams使文字居中
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }

        // 添加TextView到轨道视图中
        parent.addView(textView, layoutParams)
    }
    

}