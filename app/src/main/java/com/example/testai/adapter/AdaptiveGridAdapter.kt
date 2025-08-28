package com.example.testai.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testai.R
import com.example.testai.model.GridItem

/**
 * 自适应网格适配器
 */
class AdaptiveGridAdapter(
    private var items: List<GridItem> = emptyList(),
    private val onItemClick: ((GridItem) -> Unit)? = null
) : RecyclerView.Adapter<AdaptiveGridAdapter.GridViewHolder>() {

    /**
     * ViewHolder类
     */
    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.tv_title)
        val contentText: TextView = itemView.findViewById(R.id.tv_content)
        val container: View = itemView.findViewById(R.id.container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grid, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = items[position]
        
        holder.titleText.text = item.title
        holder.contentText.text = item.content
        holder.container.setBackgroundColor(item.backgroundColor)
        
        // 设置点击事件
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = items.size

    /**
     * 更新数据
     */
    fun updateItems(newItems: List<GridItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    /**
     * 添加单个项目
     */
    fun addItem(item: GridItem) {
        val mutableItems = items.toMutableList()
        mutableItems.add(item)
        items = mutableItems
        notifyItemInserted(items.size - 1)
    }

    /**
     * 移除项目
     */
    fun removeItem(position: Int) {
        if (position >= 0 && position < items.size) {
            val mutableItems = items.toMutableList()
            mutableItems.removeAt(position)
            items = mutableItems
            notifyItemRemoved(position)
        }
    }

    /**
     * 清空所有项目
     */
    fun clearItems() {
        val oldSize = items.size
        items = emptyList()
        notifyItemRangeRemoved(0, oldSize)
    }
}