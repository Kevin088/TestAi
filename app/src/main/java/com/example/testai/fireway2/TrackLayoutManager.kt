package com.example.testai.fireway2

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.view.View
import android.widget.FrameLayout
import kotlin.math.cos
import kotlin.math.sin

/**
 * 轨道管理类，管理2-4个圆形视图和它们之间的连接轨道
 * 
 * 该管理类可以自动在圆形视图之间创建连接轨道，形成完整的网格连接系统。
 * 支持2个、3个或4个圆形视图的动态连接。
 * 轨道视图将添加到指定的根视图中，减少布局层级。
 * 
 * @param context Android上下文
 * @param fireWayRoot 根视图，轨道视图将添加到此视图中
 */
class TrackLayoutManager(
    private val context: Context,
    private val fireWayRoot: androidx.constraintlayout.widget.ConstraintLayout
) {

    companion object {
        /** 轨道宽度（像素） */
        private const val TRACK_WIDTH = 50
    }

    /** 存储圆形视图的列表 */
    private val circleViews = mutableListOf<View>()
    /** 存储圆形视图中心位置的列表 */
    private val circlePositions = mutableListOf<Point>()
    /** 存储轨道视图的列表 */
    private val trackViews = mutableListOf<FrameLayout>()
    /** 标记是否已完成初始化 */
    private var isInitialized = false

    /**
     * 设置圆形视图列表
     * 
     * 该方法用于设置需要连接的圆形视图，支持2-4个视图，设置后会自动重新布局并创建连接轨道。
     * 
     * @param circles 圆形视图列表，支持2-4个视图
     */
    fun setCircleViews(circles: List<View>) {
        require(circles.size in 2..4) { "圆形视图数量必须在2-4个之间" }
        circleViews.clear()
        circleViews.addAll(circles)
        initializeLayout()
    }
    
    /**
     * 手动初始化轨道布局
     * 
     * 当根视图尺寸确定后调用此方法来初始化轨道布局。
     * 只在第一次调用且根视图尺寸大于0时执行初始化操作。
     */
    fun initialize() {
        if (!isInitialized && fireWayRoot.width > 0 && fireWayRoot.height > 0) {
            isInitialized = true
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
        if (circleCount < 2) return
        
        // 计算需要的轨道数量：n*(n-1)/2
        val trackCount = circleCount * (circleCount - 1) / 2
        
        repeat(trackCount) {
            val trackView = FrameLayout(context).apply {
                setBackgroundColor(Color.DKGRAY)
                visibility = View.VISIBLE
            }
            
            trackViews.add(trackView)
            fireWayRoot.addView(trackView)
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
     * 布局单个轨道
     * 
     * 计算两点之间的距离、角度，并设置轨道视图的位置、大小和旋转。
     * 轨道会以两点连线的中心为中心点进行旋转。
     * 
     * @param trackView 要布局的轨道视图
     * @param start 起始点坐标
     * @param end 结束点坐标
     */
    private fun layoutTrack(trackView: FrameLayout, start: Point, end: Point) {
        val centerX = (start.x + end.x) / 2
        val centerY = (start.y + end.y) / 2
        
        val dx = end.x - start.x
        val dy = end.y - start.y
        val length = kotlin.math.sqrt((dx * dx + dy * dy).toDouble()).toInt()
        val angle = kotlin.math.atan2(dy.toDouble(), dx.toDouble()) * 180 / kotlin.math.PI
        
        val left = centerX - length / 2
        val top = centerY - TRACK_WIDTH / 2
        val right = left + length
        val bottom = top + TRACK_WIDTH
        
        trackView.apply {
            layout(left, top, right, bottom)
            rotation = angle.toFloat()
            pivotX = length / 2.0f
            pivotY = TRACK_WIDTH / 2.0f
            visibility = View.VISIBLE
        }
    }

    
    /**
     * 布局圆形视图并获取其位置
     * 
     * 如果没有圆形视图，则使用默认位置；
     * 如果有圆形视图，则通过post方法异步获取每个视图的实际屏幕位置，
     * 转换为相对于当前布局的坐标，并在所有位置获取完成后触发轨道布局。
     * 使用数组确保位置按circleViews中view的顺序添加。
     */
    private fun layoutCircleViews() {
        if (circleViews.isEmpty()) {
            return
        }
        
        val tempPositions = arrayOfNulls<Point>(circleViews.size)
        var completedCount = 0
        
        circleViews.forEachIndexed { index, view ->
            view.post {
                // 优化：直接计算相对于fireWayRoot的坐标，避免屏幕坐标转换
                val relativeX = view.x.toInt() + view.width / 2
                val relativeY = view.y.toInt() + view.height / 2
                
                tempPositions[index] = Point(relativeX, relativeY)
                completedCount++
                
                if (completedCount == circleViews.size) {
                    circlePositions.clear()
                    // 按数组顺序添加，确保与circleViews顺序一致
                    tempPositions.forEach { point ->
                        point?.let { circlePositions.add(it) }
                    }
                    fireWayRoot.post { layoutTrackViews() }
                }
            }
        }
    }

    /**
     * 获取所有轨道视图
     * 
     * @return 轨道视图列表的只读副本
     */
    fun getTrackViews(): List<FrameLayout> {
        return trackViews.toList()
    }

    /**
     * 获取与指定圆形视图相邻的轨道视图
     * 
     * 根据圆形视图的位置索引，返回所有与该圆形视图连接的轨道视图。
     * 圆形视图索引定义：左上=0, 右上=1, 左下=2, 右下=3
     * 
     * @param circleIndex 圆形视图的位置索引 (0-3)
     * @return 与该圆形视图相邻的轨道视图列表
     * @throws IllegalArgumentException 如果索引超出范围
     */
    fun getAdjacentTrackViews(circleIndex: Int): List<FrameLayout> {
        require(circleIndex in 0 until circleViews.size) { 
            "圆形视图索引超出范围: $circleIndex, 当前圆形视图数量: ${circleViews.size}" 
        }
        
        val adjacentTracks = mutableListOf<FrameLayout>()
        val circleCount = circleViews.size
        
        // 根据当前的轨道连接逻辑，找到与指定圆形视图相邻的轨道
        var trackIndex = 0
        for (i in 0 until circleCount) {
            for (j in (i + 1) until circleCount) {
                // 如果当前轨道连接包含指定的圆形视图索引
                if (i == circleIndex || j == circleIndex) {
                    if (trackIndex < trackViews.size) {
                        adjacentTracks.add(trackViews[trackIndex])
                    }
                }
                trackIndex++
            }
        }
        
        return adjacentTracks
    }
}