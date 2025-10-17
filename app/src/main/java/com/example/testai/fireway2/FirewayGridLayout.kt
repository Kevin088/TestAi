package com.example.testai.fireway2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import kotlin.math.cos
import kotlin.math.sin

/**
 * 自定义布局，管理四个圆角视图和六条连接它们的矩形轨道
 */
class FirewayGridLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        Log.d("FirewayGrid", "FirewayGridLayout initialized")
    }

    // 存储四个圆角视图
    private val circleViews = mutableListOf<View>()
    
    // 存储四个圆角视图的中心点位置
    private val circlePositions = mutableListOf<Point>()
    
    // 存储六个矩形轨道视图
    private val trackViews = mutableListOf<FrameLayout>()
    
    // 存储从Activity传递的circle视图引用
    /**
     * 设置circle视图列表
     * @param circles 从Activity传递过来的circle视图列表
     */
    fun setCircleViews(circles: List<View>) {
        circleViews.clear()
        circleViews.addAll(circles)
        Log.d("FirewayGrid", "Received ${circles.size} circle views from Activity")
        
        // 触发重新布局
        requestLayout()
    }

    private var isInitialized = false
    
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        
        // 只在第一次布局时初始化
        if (!isInitialized && width > 0 && height > 0) {
            isInitialized = true
            
            // 获取circle视图的位置
            layoutCircleViews()
            
            // 创建轨道视图
            createTrackViews()
            
            // 如果已经有位置信息，立即布局轨道
            if (circlePositions.isNotEmpty()) {
                layoutTrackViews()
            }
            
            Log.d("FirewayGrid", "Layout completed")
        }
    }

    private val trackHeight = 40 // 轨道高度
    
    private fun createTrackViews() {
        // 如果已经创建过轨道视图，直接返回
        if (trackViews.isNotEmpty()) {
            Log.d("FirewayGrid", "Track views already created, skipping")
            return
        }
        
        Log.d("FirewayGrid", "Creating ${6} track views")
        // 创建6个矩形轨道ViewGroup
        // 轨道连接：左上-右上，左上-左下，左上-右下，右上-左下，右上-右下，左下-右下
        for (i in 0..5) {
            val trackView = FrameLayout(context).apply {
                // 设置轨道颜色为深灰色
                setBackgroundColor(Color.DKGRAY)
                // 设置更高的层级，确保轨道显示在圆球上面
                elevation = 10f
                // 设置透明度
                alpha = 0.8f
                // 设置可见性
                visibility = View.VISIBLE
            }
            
            trackViews.add(trackView)
            addView(trackView)
            
            Log.d("FirewayGrid", "Track view $i created and added with dark gray color")
        }
    }

    private fun layoutTrackViews() {
        Log.d("FirewayGrid", "Layout tracks: circles=${circlePositions.size}, tracks=${trackViews.size}")
        
        // 确保有足够的圆圈位置和轨道视图
        if (circlePositions.size < 4 || trackViews.size < 6) {
            Log.w("FirewayGrid", "Not enough positions or tracks to layout")
            return
        }
        
        // 定义连接关系：每对圆圈的索引
        // 圆圈位置：0=左上, 1=右上, 2=左下, 3=右下
        val connections = listOf(
            Pair(0, 1), // 左上-右上（顶边）
            Pair(1, 3), // 右上-右下（右边）
            Pair(3, 2), // 右下-左下（底边）
            Pair(2, 0), // 左下-左上（左边）
            Pair(0, 3), // 左上-右下（对角线1）
            Pair(1, 2)  // 右上-左下（对角线2）
        )
        
        // 布局每条轨道
        for (i in connections.indices) {
            val (startIdx, endIdx) = connections[i]
            val start = circlePositions[startIdx]
            val end = circlePositions[endIdx]
            val trackView = trackViews[i]
            
            // 使用简化的布局方法
            layoutTrackSimple(trackView, start, end, i)
        }
    }
    
    private fun layoutTrackSimple(trackView: FrameLayout, start: Point, end: Point, trackIndex: Int) {
        // 计算轨道的中心位置
        val centerX = (start.x + end.x) / 2
        val centerY = (start.y + end.y) / 2
        
        // 计算轨道长度和角度
        val dx = end.x - start.x
        val dy = end.y - start.y
        val length = Math.sqrt((dx * dx + dy * dy).toDouble()).toInt()
        val angle = Math.atan2(dy.toDouble(), dx.toDouble()) * 180 / Math.PI
        
        // 轨道尺寸 - 使用实际计算的距离
        val trackWidth = 20  // 轨道宽度
        val trackLength = length  // 使用实际距离，确保连接到圆圈
        
        // 计算轨道位置
        val left = centerX - trackLength / 2
        val top = centerY - trackWidth / 2
        val right = left + trackLength
        val bottom = top + trackWidth
        
        // 直接使用计算的位置，不进行边界限制以保持轨道的正确尺寸
        trackView.layout(left, top, right, bottom)
        
        // 设置轨道的旋转角度
        trackView.rotation = angle.toFloat()
        
        // 设置旋转中心为轨道视图的中心
        trackView.pivotX = (right - left) / 2.0f
        trackView.pivotY = (bottom - top) / 2.0f
        
        trackView.visibility = View.VISIBLE
        trackView.alpha = 0.8f
        trackView.elevation = 4f
        
        Log.d("FirewayGrid", "Track $trackIndex: center=($centerX,$centerY), angle=${angle.toInt()}°, layout=($left,$top,$right,$bottom)")
    }

    /**
     * 查找四个圆角视图
     */

    

    
    /**
     * 获取四个圆角视图的实际位置
     */
    private fun calculateCirclePositions(width: Int, height: Int) {
        // 使用默认的圆形排列
        val centerX = width / 2
        val centerY = height / 2
        val radius = minOf(width, height) / 3
        
        for (i in 0 until 4) {
            val angle = i * 90.0 * Math.PI / 180.0
            val x = centerX + (radius * cos(angle)).toInt()
            val y = centerY + (radius * sin(angle)).toInt()
            circlePositions.add(Point(x, y))
        }
        
        Log.d("FirewayGrid", "Generated ${circlePositions.size} default circle positions")
    }
    
    private fun layoutCircleViews() {
        // 如果没有circle视图，使用默认位置
        if (circleViews.isEmpty()) {
            Log.w("FirewayGrid", "No circle views available, using default positions")
            circlePositions.clear()
            calculateCirclePositions(width, height)
            return
        }
        
        // 创建临时列表来收集位置
        val tempPositions = mutableListOf<Point>()
        var completedCount = 0
        
        // 获取每个circle视图的实际位置
        for (view in circleViews) {
            // 使用post确保视图已经完成布局
            view.post {
                // 获取视图相对于父容器的位置
                val location = IntArray(2)
                view.getLocationOnScreen(location)
                
                // 获取FirewayGridLayout的位置
                val myLocation = IntArray(2)
                this.getLocationOnScreen(myLocation)
                
                // 计算相对于FirewayGridLayout的位置
                val relativeX = location[0] - myLocation[0] + view.width / 2
                val relativeY = location[1] - myLocation[1] + view.height / 2
                
                tempPositions.add(Point(relativeX, relativeY))
                completedCount++
                Log.d("FirewayGrid", "Circle position: ($relativeX, $relativeY)")
                
                // 当所有位置都获取完成后，更新circlePositions并重新布局轨道
                if (completedCount == circleViews.size) {
                    circlePositions.clear()
                    circlePositions.addAll(tempPositions)
                    post {
                        layoutTrackViews()
                    }
                }
            }
        }
        
        Log.d("FirewayGrid", "Started updating ${circleViews.size} circle positions")
    }
}