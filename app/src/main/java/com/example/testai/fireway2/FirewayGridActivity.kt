package com.example.testai.fireway2

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.testai.R

/**
 * 展示FirewayGridLayout的Activity
 */
class FirewayGridActivity : AppCompatActivity() {
    
    private lateinit var firewayGridLayout: FirewayGridLayout
    private lateinit var circleTopLeft: View
    private lateinit var circleTopRight: View
    private lateinit var circleBottomLeft: View
    private lateinit var circleBottomRight: View
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fireway_grid)
        
        initViews()
    }
    
    /**
     * 初始化视图
     */
    private fun initViews() {
        firewayGridLayout = findViewById(R.id.fireway_grid_layout)
        circleTopLeft = findViewById(R.id.circle_top_left)
        circleTopRight = findViewById(R.id.circle_top_right)
        circleBottomLeft = findViewById(R.id.circle_bottom_left)
        circleBottomRight = findViewById(R.id.circle_bottom_right)
        
        // 为圆角视图设置标签，便于FirewayGridLayout识别
        circleTopLeft.tag = "circle"
        circleTopRight.tag = "circle"
        circleBottomLeft.tag = "circle"
        circleBottomRight.tag = "circle"
        
        // 将circle视图传递给FirewayGridLayout
        val circleViews = listOf(circleTopLeft, circleTopRight, circleBottomLeft, circleBottomRight)
        firewayGridLayout.setCircleViews(circleViews)
    }
}