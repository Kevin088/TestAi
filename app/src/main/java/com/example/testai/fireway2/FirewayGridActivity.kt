package com.example.testai.fireway2

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.testai.R

/**
 * 展示轨道布局的Activity
 */
class FirewayGridActivity : AppCompatActivity() {
    
    private lateinit var trackLayoutManager: TrackLayoutManager
    private lateinit var circleTopLeft: View
    private lateinit var circleTopRight: View
    private lateinit var circleBottomLeft: View
    private lateinit var circleBottomRight: View
    private lateinit var fireWayRoot: ConstraintLayout

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
        fireWayRoot = findViewById<ConstraintLayout>(R.id.main_constraint_layout)

        // 创建轨道布局管理器实例
        trackLayoutManager = TrackLayoutManager(this, fireWayRoot)
        
        // 将circle视图传递给轨道布局管理器
        val circleViews = listOf(circleTopLeft, circleTopRight, circleBottomLeft, circleBottomRight)
        trackLayoutManager.setCircleViews(circleViews)
        
        // 延迟初始化，确保布局尺寸已确定
        fireWayRoot.post {
            trackLayoutManager.initialize()
        }
    }
}