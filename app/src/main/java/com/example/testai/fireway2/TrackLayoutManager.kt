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
     * @param circles 圆形视图列表
     */
    fun setCircleViews(circles: List<View>) {
        if (circles.size !in 2..4) {
            Log.e("TrackLayoutManager", "圆形视图数量必须在2-4之间，当前数量: ${circles.size}")
            return
        }
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
     * 释放资源
     * 
     * 完全清理和释放所有相关资源，包括视图、数据和状态重置。
     * 调用此方法后，需要重新调用 setCircleViews 和 initialize 才能正常使用。
     */
    fun release() {
        Log.d("TrackLayoutManager", "开始释放资源...")
        
        // 移除所有轨道视图
        trackViews.forEach { trackView ->
            try {
                // 清除轨道视图的所有子视图
                trackView.removeAllViews()
                // 从父容器中移除轨道视图
                if (trackView.parent != null) {
                    fireWayRoot.removeView(trackView)
                }
            } catch (e: Exception) {
                Log.w("TrackLayoutManager", "移除轨道视图时发生异常: ${e.message}")
            }
        }
        
        // 清空所有数据集合
        trackViews.clear()
        circleViews.clear()
        circlePositions.clear()
        
        // 重置初始化状态
        isInitialized = false
        
        Log.d("TrackLayoutManager", "资源释放完成")
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
    /**
     * 轨道索引信息数据类
     * 
     * 用于存储轨道的起始和结束圆形索引信息
     * 
     * @param startIndex 起始圆形索引
     * @param endIndex 结束圆形索引
     */
    data class TrackIndexInfo(
        val startIndex: Int,
        val endIndex: Int
    )
    
    private fun createTrackViews() {
        // 清理现有轨道视图
        trackViews.forEach { fireWayRoot.removeView(it) }
        trackViews.clear()
        
        val circleCount = circleViews.size
        if (circleCount < 2) {
            return
        }
        
        // 动态生成所有圆形视图之间的连接，并记录索引信息
        for (i in 0 until circleCount) {
            for (j in (i + 1) until circleCount) {
                val trackView = FrameLayout(context).apply {
                    setBackgroundColor(Color.DKGRAY)
                    visibility = View.VISIBLE
                    // 将起止圆形索引信息记录到轨道view的tag中
                    tag = TrackIndexInfo(startIndex = i, endIndex = j)
                }
                
                // 添加文字到轨道
                addTextToTracks(trackView)
                
                // 添加到轨道视图列表
                trackViews.add(trackView)
                
                // 记录日志便于调试
                Log.d("TrackLayoutManager", "创建轨道: 圆形$i -> 圆形$j")
            }
        }
        
        Log.d("TrackLayoutManager", "总共创建了 ${trackViews.size} 条轨道")
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
     * 优化版本：围绕轨道中心点进行旋转，提供更自然的旋转效果
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
        
        Log.d("TrackLayoutManager", "轨道角度: $angle°, 距离: ${distance}px")
        
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
            
            // 计算轨道中心点坐标
            val centerX = (start.x + end.x) / 2f
            val centerY = (start.y + end.y) / 2f
            
            // 计算轨道左上角位置，使轨道中心对准计算出的中心点
            val trackStartX = centerX - distance / 2f
            val trackStartY = centerY - TRACK_WIDTH / 2f
            
            // 在FrameLayout中直接设置视图的绝对位置
            trackView.x = trackStartX
            trackView.y = trackStartY
            
            // 设置旋转角度
            trackView.rotation = angle.toFloat()
            
            // 设置旋转中心点为轨道的几何中心，确保围绕轨道中心旋转
            trackView.pivotX = distance / 2f
            trackView.pivotY = (TRACK_WIDTH / 2).toFloat()
            Log.d("TrackLayoutManager", "轨道布局完成 - 中心点: ($centerX, $centerY), 旋转中心: (${distance/2f}, ${TRACK_WIDTH/2f})")
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
    /**
     * 获取某个圆形相邻的轨道视图view集合
     * 
     * 根据圆形位置索引获取与其相邻的所有轨道视图，并设置相应的缩放效果。
     * 当轨道的startIndex等于传入的index时，设置scaleX为1；
     * 当轨道的endIndex等于传入的index时，设置scaleX为-1。
     * 
     * @param index 圆形位置下标
     * @return 相邻的轨道视图集合
     */
    fun getAdjacentTrackViews(index: Int): List<FrameLayout> {
        val adjacentTracks = mutableListOf<FrameLayout>()
        
        if (index < 0 || index >= circleViews.size) {
            Log.w("TrackLayoutManager", "无效的圆形索引: $index")
            return adjacentTracks
        }
        
        // 遍历所有轨道视图，查找与指定圆形相邻的轨道
        trackViews.forEach { trackView ->
            val indexInfo = trackView.tag as? TrackIndexInfo
            if (indexInfo != null) {
                when {
                    indexInfo.startIndex == index -> {
                        // 当startIndex等于index时，设置scaleX为1
                        trackView.scaleX = 1f
                        if (indexInfo.startIndex == 1 && (indexInfo.endIndex == 2 || indexInfo.endIndex == 3)) {
                            trackView.scaleY = -1f
                        }
                        adjacentTracks.add(trackView)
                        Log.d("TrackLayoutManager", "找到相邻轨道: 圆形$index -> 圆形${indexInfo.endIndex}, scaleX=1")
                    }
                    indexInfo.endIndex == index -> {
                        // 当endIndex等于index时，设置scaleX为-1
                        trackView.scaleX = -1f
                        adjacentTracks.add(trackView)
                        Log.d("TrackLayoutManager", "找到相邻轨道: 圆形${indexInfo.startIndex} -> 圆形$index, scaleX=-1")
                    }
                }
            }
        }
        Log.d("TrackLayoutManager", "圆形$index 共有 ${adjacentTracks.size} 条相邻轨道")
        return adjacentTracks
    }
    
    fun addTextToTracks(parent: FrameLayout) {
        // 清除现有子视图
        parent.removeAllViews()

        // 创建TextView
        val textView = TextView(context).apply {
            text = "中国人"
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