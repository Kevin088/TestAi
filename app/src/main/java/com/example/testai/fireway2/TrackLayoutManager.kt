package com.example.testai.fireway2

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.view.View
import android.widget.FrameLayout
import kotlin.math.cos
import kotlin.math.sin

/**
 * 轨道管理类，管理四个圆形视图和六条连接它们的矩形轨道
 * 
 * 该管理类可以自动在四个圆形视图之间创建连接轨道，形成一个完整的网格连接系统。
 * 轨道包括四条边框连接和两条对角线连接，总共六条轨道。
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
        /** 轨道总数量 - 4条边框 + 2条对角线 */
        private const val TRACK_COUNT = 6
        /** 圆形视图数量 */
        private const val CIRCLE_COUNT = 4
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
     * 该方法用于设置需要连接的圆形视图，设置后会自动重新布局并创建连接轨道。
     * 
     * @param circles 圆形视图列表，通常包含4个视图
     */
    fun setCircleViews(circles: List<View>) {
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
     * 创建6个FrameLayout作为连接轨道，设置深灰色背景、阴影和透明度。
     * 如果轨道视图已存在则跳过创建。
     */
    private fun createTrackViews() {
        if (trackViews.isNotEmpty()) return
        
        repeat(TRACK_COUNT) {
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
     * 创建6条连接：4条边框连接 + 2条对角线连接。
     */
    private fun layoutTrackViews() {
        if (circlePositions.size < CIRCLE_COUNT || trackViews.size < TRACK_COUNT) {
            return
        }
        
        val connections = listOf(
            0 to 1, // 左上-右上
            1 to 3, // 右上-右下
            3 to 2, // 右下-左下
            2 to 0, // 左下-左上
            0 to 3, // 左上-右下
            1 to 2  // 右上-左下
        )
        
        connections.forEachIndexed { index, (startIdx, endIdx) ->
            layoutTrack(
                trackViews[index],
                circlePositions[startIdx],
                circlePositions[endIdx]
            )
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
     */
    private fun layoutCircleViews() {
        if (circleViews.isEmpty()) {
            return
        }
        
        val tempPositions = mutableListOf<Point>()
        var completedCount = 0
        
        circleViews.forEach { view ->
            view.post {
                // 优化：直接计算相对于fireWayRoot的坐标，避免屏幕坐标转换
                val relativeX = view.x.toInt() + view.width / 2
                val relativeY = view.y.toInt() + view.height / 2
                
                tempPositions.add(Point(relativeX, relativeY))
                completedCount++
                
                if (completedCount == circleViews.size) {
                    circlePositions.clear()
                    circlePositions.addAll(tempPositions)
                    fireWayRoot.post { layoutTrackViews() }
                }
            }
        }
    }
}