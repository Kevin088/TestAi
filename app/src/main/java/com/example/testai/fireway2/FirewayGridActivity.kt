package com.example.testai.fireway2

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.testai.R
import com.example.testai.fireway2.TrackLayoutManager

/**
 * 展示轨道布局的Activity
 */
class FirewayGridActivity : AppCompatActivity() {
    
    private lateinit var trackLayoutManager: TrackLayoutManager
    private lateinit var circleTopLeft: View
    private lateinit var circleTopRight: View
    private lateinit var circleBottomLeft: View
    private lateinit var circleBottomRight: View
    private lateinit var fireWayContainer: FrameLayout
    private lateinit var root: ConstraintLayout

    private lateinit var btnAddMargin: Button
    private lateinit var btnTestCircle0: Button
    private lateinit var btnTestCircle1: Button
    private lateinit var btnTestCircle2: Button
    private lateinit var btnTestCircle3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fireway_grid)
        
        initViews()
    }
    
    /**
     * 初始化视图
     */
    private fun initViews() {
        circleTopLeft = findViewById(R.id.circle_top_left)
        circleTopRight = findViewById(R.id.circle_top_right)
        circleBottomLeft = findViewById(R.id.circle_bottom_left)
        circleBottomRight = findViewById(R.id.circle_bottom_right)
        fireWayContainer = findViewById<FrameLayout>(R.id.fireway_container)
        root = findViewById(R.id.main_constraint_layout)
        // 创建轨道布局管理器实例
        trackLayoutManager = TrackLayoutManager(this, fireWayContainer)
        
        // 将circle视图传递给轨道布局管理器
        val circleViews = listOf(circleTopLeft, circleTopRight, circleBottomLeft, circleBottomRight)
        trackLayoutManager.setCircleViews(circleViews)
        
        // 延迟初始化，确保布局尺寸已确定
        fireWayContainer.post {
            trackLayoutManager.initialize()
        }
        
        // 初始化按钮
        btnAddMargin = findViewById(R.id.btn_add_margin)
        btnTestCircle0 = findViewById(R.id.btn_test_circle_0)
        btnTestCircle1 = findViewById(R.id.btn_test_circle_1)
        btnTestCircle2 = findViewById(R.id.btn_test_circle_2)
        btnTestCircle3 = findViewById(R.id.btn_test_circle_3)
        
        // 设置按钮点击监听器
        setupButtonListeners()
    }
    
    /**
     * 设置按钮点击监听器
     */
    private fun setupButtonListeners() {
        btnAddMargin.setOnClickListener {
            addMarginToFireWayContainer()
        }
        
        // 四个测试按钮的点击事件
        btnTestCircle0.setOnClickListener { testCircleClick(0) }
        btnTestCircle1.setOnClickListener { testCircleClick(1) }
        btnTestCircle2.setOnClickListener { testCircleClick(2) }
        btnTestCircle3.setOnClickListener { testCircleClick(3) }
    }
    

    
    /**
     * 测试圆形按钮点击事件
     * 
     * 调用 getAdjacentTrackViews 方法获取相邻轨道并显示结果
     * 
     * @param circleIndex 圆形索引
     */
    private fun testCircleClick(circleIndex: Int) {
        Log.d("FirewayGridActivity", "点击了圆形$circleIndex 按钮")
        
        // 调用 getAdjacentTrackViews 获取相邻轨道
        val adjacentTracks = trackLayoutManager.getAdjacentTrackViews(circleIndex)
        
        if (adjacentTracks.isNotEmpty()) {
            // 显示找到的轨道数量
            val message = "圆形$circleIndex 找到 ${adjacentTracks.size} 条相邻轨道"
            Log.d("FirewayGridActivity", message)
            // 为每个相邻轨道添加编号
            adjacentTracks.forEachIndexed { index, track ->
                Log.d("FirewayGridActivity", (track.tag as? TrackLayoutManager.TrackIndexInfo)?.toString() ?: "无索引信息")
            }
        }
    }

    
    /**
     * 给fireWayContainer添加50dp的marginTop
     */
    private fun addMarginToFireWayContainer() {
        val layoutParams = root.layoutParams as LinearLayout.LayoutParams
        val currentMarginTop = layoutParams.topMargin
        val newMarginTop = currentMarginTop + (50 * resources.displayMetrics.density).toInt()
        
        layoutParams.topMargin = newMarginTop
        fireWayContainer.layoutParams = layoutParams
    }
}