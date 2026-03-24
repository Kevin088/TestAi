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
        // 轨道的固定宽度（像素）
        private const val TRACK_WIDTH = 50
    }

    // 圆形视图列表 - 存储所有需要连接的圆形视图
    private val circleViews = mutableListOf<View>()
    
    // 圆形视图位置列表 - 存储每个圆形视图相对于FrameLayout容器的中心坐标
    private val circlePositions = mutableListOf<Point>()
    
    // 轨道视图列表 - 存储所有连接圆形的轨道FrameLayout
    private val trackViews = mutableListOf<FrameLayout>()
    
    // 初始化状态标志 - 防止重复初始化
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
        // 清理现有轨道视图 - 移除旧的轨道并清空列表
        trackViews.forEach { fireWayRoot.removeView(it) }
        trackViews.clear()
        
        val circleCount = circleViews.size
        if (circleCount < 2) {
            return
        }
        
        // 动态生成所有圆形视图之间的连接
        // 使用双层循环确保每两个圆形之间都有一条轨道
        // 例如：3个圆形会生成3条轨道 (0-1, 0-2, 1-2)
        for (i in 0 until circleCount) {
            for (j in (i + 1) until circleCount) {
                val trackView = FrameLayout(context).apply {
                    // 设置轨道背景色为深灰色
                    setBackgroundColor(Color.DKGRAY)
                    visibility = View.VISIBLE
                    // 将起止圆形索引信息记录到轨道view的tag中，便于后续查询
                    tag = TrackIndexInfo(startIndex = i, endIndex = j)
                }
                
                // 在轨道上添加文字标签
                addTextToTracks(trackView)
                
                // 添加到轨道视图列表，保持顺序
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
        // 计算两个圆形之间的距离和角度
        val deltaX = end.x - start.x
        val deltaY = end.y - start.y
        val distance = kotlin.math.sqrt((deltaX * deltaX + deltaY * deltaY).toDouble()).toFloat()
        val angle = kotlin.math.atan2(deltaY.toDouble(), deltaX.toDouble()) * 180 / kotlin.math.PI
        
        Log.d("TrackLayoutManager", "轨道角度: $angle°, 距离: ${distance}px")
        
        // 确保轨道视图已添加到FrameLayout容器中
        if (trackView.parent == null) {
            fireWayRoot.addView(trackView)
        }
        
        // 使用post确保视图已经被添加到FrameLayout布局中，避免布局计算错误
        trackView.post {
            // 为FrameLayout设置轨道的宽度（距离）和高度（固定宽度）
            val layoutParams = FrameLayout.LayoutParams(
                distance.toInt(),
                TRACK_WIDTH
            )
            trackView.layoutParams = layoutParams
            
            // 计算两个圆形的中点坐标 - 这是轨道的理想中心位置
            val centerX = (start.x + end.x) / 2f
            val centerY = (start.y + end.y) / 2f
            
            // 计算轨道左上角位置
            // 由于轨道是以左上角为原点，需要向左上方偏移半个轨道尺寸
            val trackStartX = centerX - distance / 2f
            val trackStartY = centerY - TRACK_WIDTH / 2f
            
            // 在FrameLayout中直接设置视图的绝对位置
            trackView.x = trackStartX
            trackView.y = trackStartY
            
            // 设置旋转角度 - 使轨道指向目标圆形
            trackView.rotation = angle.toFloat()
            
            // 设置旋转中心点为轨道的几何中心
            // 这样旋转时轨道会围绕自身中心旋转，而不是围绕左上角
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
        
        // 使用数组确保位置按索引顺序添加，避免异步操作导致的顺序混乱
        val tempPositions = arrayOfNulls<Point>(circleViews.size)
        var completedCount = 0
        
        circleViews.forEachIndexed { index, view ->
            // 使用post确保视图已完成布局后再进行位置计算
            view.post {
                // 获取圆形视图在屏幕上的绝对位置（左上角坐标）
                val viewLocation = IntArray(2)
                view.getLocationOnScreen(viewLocation)
                
                // 获取FrameLayout容器在屏幕上的绝对位置（左上角坐标）
                val frameLocation = IntArray(2)
                fireWayRoot.getLocationOnScreen(frameLocation)
                
                // 计算圆形视图相对于FrameLayout容器的位置
                // 加上view.width/2 和 view.height/2 是为了获取圆形的中心坐标
                val relativeX = viewLocation[0] - frameLocation[0] + view.width / 2
                val relativeY = viewLocation[1] - frameLocation[1] + view.height / 2
                
                tempPositions[index] = Point(relativeX, relativeY)
                completedCount++
                
                // 当所有位置计算完成后，按顺序添加到circlePositions
                // 这样可以确保轨道布局时使用的位置数据是完整且有序的
                if (completedCount == circleViews.size) {
                    circlePositions.clear()
                    tempPositions.forEach { point ->
                        point?.let { circlePositions.add(it) }
                    }
                    
                    // 所有圆形位置确定后，开始布局轨道
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
                    // 当轨道的起始圆形等于指定索引时
                    indexInfo.startIndex == index -> {
                        // 设置scaleX为1，表示轨道方向正常
                        trackView.scaleX = 1f
                        // 特殊处理：当起始圆形为1时，对某些轨道进行Y轴翻转
                        if (indexInfo.startIndex == 1 && (indexInfo.endIndex == 2 || indexInfo.endIndex == 3)) {
                            trackView.scaleY = -1f
                        }
                        adjacentTracks.add(trackView)
                        Log.d("TrackLayoutManager", "找到相邻轨道: 圆形$index -> 圆形${indexInfo.endIndex}, scaleX=1")
                    }
                    // 当轨道的结束圆形等于指定索引时
                    indexInfo.endIndex == index -> {
                        // 设置scaleX为-1，表示轨道方向反向
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
        // 清除现有子视图，确保每次添加文字时容器是干净的
        parent.removeAllViews()

        // 创建TextView用于显示轨道标签
        val textView = TextView(context).apply {
            text = "中国人"
            textSize = 12f
            setTextColor(Color.WHITE)
            // 设置文字在TextView内部居中
            gravity = Gravity.CENTER
        }

        // 设置LayoutParams使文字在FrameLayout中居中显示
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            // 使用gravity属性让FrameLayout将TextView放在中心位置
            gravity = Gravity.CENTER
        }

        // 添加TextView到轨道视图中
        parent.addView(textView, layoutParams)
    }
    


}