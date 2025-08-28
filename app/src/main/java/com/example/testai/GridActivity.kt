package com.example.testai

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.testai.adapter.AdaptiveGridAdapter
import com.example.testai.layout.AdaptiveGridLayoutManager
import com.example.testai.model.GridItem

/**
 * 自适应网格布局Activity
 * 演示RecyclerView根据数据源数量动态调整布局
 */
class GridActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdaptiveGridAdapter
    private lateinit var layoutManager: AdaptiveGridLayoutManager

    private lateinit var btn2Items: Button
    private lateinit var btn3Items: Button
    private lateinit var btn4Items: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)

        initViews()
        setupRecyclerView()
        setupClickListeners()
        
        // 默认显示2个元素
        load2Items()
    }

    /**
     * 初始化视图
     */
    private fun initViews() {
        recyclerView = findViewById(R.id.recycler_view)
        btn2Items = findViewById(R.id.btn_2_items)
        btn3Items = findViewById(R.id.btn_3_items)
        btn4Items = findViewById(R.id.btn_4_items)
    }

    /**
     * 设置RecyclerView
     */
    private fun setupRecyclerView() {
        layoutManager = AdaptiveGridLayoutManager()
        adapter = AdaptiveGridAdapter { item ->
            // 点击事件处理
            Toast.makeText(this, "点击了: ${item.title}", Toast.LENGTH_SHORT).show()
        }
        
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    /**
     * 设置按钮点击事件
     */
    private fun setupClickListeners() {
        btn2Items.setOnClickListener {
            load2Items()
        }
        
        btn3Items.setOnClickListener {
            load3Items()
        }
        
        btn4Items.setOnClickListener {
            load4Items()
        }
    }

    /**
     * 加载2个元素
     */
    private fun load2Items() {
        val items = listOf(
            GridItem(
                id = 1,
                title = "元素 1",
                content = "这是第一个元素\n均分布局",
                backgroundColor = Color.parseColor("#FF6B6B")
            ),
            GridItem(
                id = 2,
                title = "元素 2",
                content = "这是第二个元素\n均分布局",
                backgroundColor = Color.parseColor("#4ECDC4")
            )
        )
        adapter.updateItems(items)
    }

    /**
     * 加载3个元素
     */
    private fun load3Items() {
        val items = listOf(
            GridItem(
                id = 1,
                title = "主要元素",
                content = "占据左半边\n重要内容展示",
                backgroundColor = Color.parseColor("#FF6B6B")
            ),
            GridItem(
                id = 2,
                title = "元素 2",
                content = "右上角\n次要内容",
                backgroundColor = Color.parseColor("#4ECDC4")
            ),
            GridItem(
                id = 3,
                title = "元素 3",
                content = "右下角\n次要内容",
                backgroundColor = Color.parseColor("#45B7D1")
            )
        )
        adapter.updateItems(items)
    }

    /**
     * 加载4个元素
     */
    private fun load4Items() {
        val items = listOf(
            GridItem(
                id = 1,
                title = "元素 1",
                content = "左上角\n2x2网格",
                backgroundColor = Color.parseColor("#FF6B6B")
            ),
            GridItem(
                id = 2,
                title = "元素 2",
                content = "右上角\n2x2网格",
                backgroundColor = Color.parseColor("#4ECDC4")
            ),
            GridItem(
                id = 3,
                title = "元素 3",
                content = "左下角\n2x2网格",
                backgroundColor = Color.parseColor("#45B7D1")
            ),
            GridItem(
                id = 4,
                title = "元素 4",
                content = "右下角\n2x2网格",
                backgroundColor = Color.parseColor("#96CEB4")
            )
        )
        adapter.updateItems(items)
    }
}