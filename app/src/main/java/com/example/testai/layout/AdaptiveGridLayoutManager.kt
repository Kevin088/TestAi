package com.example.testai.layout

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlin.math.max

/**
 * 自适应网格布局管理器
 * 根据数据源数量动态调整布局：
 * - 4个元素：2x2网格
 * - 3个元素：第一个占一半，其余两个均分
 * - 2个元素：均分两等分
 */
class AdaptiveGridLayoutManager : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler)
            return
        }

        detachAndScrapAttachedViews(recycler)

        val parentWidth = width - paddingLeft - paddingRight
        val parentHeight = height - paddingTop - paddingBottom

        when (itemCount) {
            2 -> layout2Items(recycler, parentWidth, parentHeight)
            3 -> layout3Items(recycler, parentWidth, parentHeight)
            4 -> layout4Items(recycler, parentWidth, parentHeight)
            else -> layoutDefault(recycler, parentWidth, parentHeight)
        }
    }

    /**
     * 2个元素布局：均分两等分
     */
    private fun layout2Items(recycler: RecyclerView.Recycler, parentWidth: Int, parentHeight: Int) {
        val itemWidth = parentWidth / 2
        val itemHeight = parentHeight

        for (i in 0 until 2) {
            val view = recycler.getViewForPosition(i)
            addView(view)
            measureChildWithMargins(view, 0, 0)

            val left = paddingLeft + i * itemWidth
            val top = paddingTop
            val right = left + itemWidth
            val bottom = top + itemHeight

            layoutDecorated(view, left, top, right, bottom)
        }
    }

    /**
     * 3个元素布局：第一个占一半，其余两个均分
     */
    private fun layout3Items(recycler: RecyclerView.Recycler, parentWidth: Int, parentHeight: Int) {
        val halfWidth = parentWidth / 2
        val halfHeight = parentHeight / 2

        // 第一个元素占左半边
        val firstView = recycler.getViewForPosition(0)
        addView(firstView)
        measureChildWithMargins(firstView, 0, 0)
        layoutDecorated(firstView, paddingLeft, paddingTop, paddingLeft + halfWidth, paddingTop + parentHeight)

        // 第二个元素占右上角
        val secondView = recycler.getViewForPosition(1)
        addView(secondView)
        measureChildWithMargins(secondView, 0, 0)
        layoutDecorated(secondView, paddingLeft + halfWidth, paddingTop, paddingLeft + parentWidth, paddingTop + halfHeight)

        // 第三个元素占右下角
        val thirdView = recycler.getViewForPosition(2)
        addView(thirdView)
        measureChildWithMargins(thirdView, 0, 0)
        layoutDecorated(thirdView, paddingLeft + halfWidth, paddingTop + halfHeight, paddingLeft + parentWidth, paddingTop + parentHeight)
    }

    /**
     * 4个元素布局：2x2网格
     */
    private fun layout4Items(recycler: RecyclerView.Recycler, parentWidth: Int, parentHeight: Int) {
        val itemWidth = parentWidth / 2
        val itemHeight = parentHeight / 2

        for (i in 0 until 4) {
            val view = recycler.getViewForPosition(i)
            addView(view)
            measureChildWithMargins(view, 0, 0)

            val row = i / 2
            val col = i % 2

            val left = paddingLeft + col * itemWidth
            val top = paddingTop + row * itemHeight
            val right = left + itemWidth
            val bottom = top + itemHeight

            layoutDecorated(view, left, top, right, bottom)
        }
    }

    /**
     * 默认布局：垂直线性布局
     */
    private fun layoutDefault(recycler: RecyclerView.Recycler, parentWidth: Int, parentHeight: Int) {
        val itemHeight = if (itemCount > 0) parentHeight / itemCount else parentHeight

        for (i in 0 until itemCount) {
            val view = recycler.getViewForPosition(i)
            addView(view)
            measureChildWithMargins(view, 0, 0)

            val left = paddingLeft
            val top = paddingTop + i * itemHeight
            val right = paddingLeft + parentWidth
            val bottom = top + itemHeight

            layoutDecorated(view, left, top, right, bottom)
        }
    }

    override fun canScrollVertically(): Boolean = false
    override fun canScrollHorizontally(): Boolean = false
}