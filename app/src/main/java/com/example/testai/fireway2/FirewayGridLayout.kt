package com.example.testai.fireway2

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlin.math.cos
import kotlin.math.sin

/**
 * 自定义布局，管理四个圆角视图和六条连接它们的矩形轨道
 * 
 * 该布局可以自动在四个圆形视图之间创建连接轨道，形成一个完整的网格连接系统。
 * 轨道包括四条边框连接和两条对角线连接，总共六条轨道。
 * 
 * @param context Android上下文
 * @param attrs 属性集合，用于XML布局中的属性设置
 * @param defStyleAttr 默认样式属性
 */
class FirewayGridLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

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
        requestLayout()
    }
    
    /**
     * 布局回调方法
     * 
     * 当布局发生变化时被调用，确保在布局尺寸确定后进行初始化。
     * 只在第一次布局且尺寸大于0时执行初始化操作。
     * 
     * @param changed 布局是否发生变化
     * @param left 左边界
     * @param top 上边界
     * @param right 右边界
     * @param bottom 下边界
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        
        if (!isInitialized && width > 0 && height > 0) {
            isInitialized = true
            initializeLayout()
        }
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
            addView(trackView)
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
     * 计算圆形视图的默认位置
     * 
     * 当没有设置圆形视图时，在布局中心周围按90度间隔生成4个默认位置。
     * 位置按顺序为：右(0°)、下(90°)、左(180°)、上(270°)。
     * 
     * @param width 布局宽度
     * @param height 布局高度
     */
    private fun calculateCirclePositions(width: Int, height: Int) {
        val centerX = width / 2
        val centerY = height / 2
        val radius = minOf(width, height) / 3
        
        repeat(CIRCLE_COUNT) { i ->
            val angle = i * 90.0 * kotlin.math.PI / 180.0
            val x = centerX + (radius * cos(angle)).toInt()
            val y = centerY + (radius * sin(angle)).toInt()
            circlePositions.add(Point(x, y))
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
            circlePositions.clear()
            calculateCirclePositions(width, height)
            return
        }
        
        val tempPositions = mutableListOf<Point>()
        var completedCount = 0
        
        circleViews.forEach { view ->
            view.post {
                val location = IntArray(2)
                view.getLocationOnScreen(location)
                
                val myLocation = IntArray(2)
                this.getLocationOnScreen(myLocation)
                
                val relativeX = location[0] - myLocation[0] + view.width / 2
                val relativeY = location[1] - myLocation[1] + view.height / 2
                
                tempPositions.add(Point(relativeX, relativeY))
                completedCount++
                
                if (completedCount == circleViews.size) {
                    circlePositions.clear()
                    circlePositions.addAll(tempPositions)
                    post { layoutTrackViews() }
                }
            }
        }
    }
}