package com.example.testai.fireway2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
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
    }
    

    
    /**
     * 给fireWayContainer添加50dp的marginTop
     */
    private fun addMarginToFireWayContainer() {
        val layoutParams = fireWayContainer.layoutParams as ConstraintLayout.LayoutParams
        val currentMarginTop = layoutParams.topMargin
        val newMarginTop = currentMarginTop + (50 * resources.displayMetrics.density).toInt()
        
        layoutParams.topMargin = newMarginTop
        fireWayContainer.layoutParams = layoutParams
    }
}