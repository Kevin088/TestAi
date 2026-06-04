package com.example.testai.model

/**
 * 网格列表中的单项数据模型。
 *
 * 该模型用于承载网格卡片展示所需的核心字段，通常会被 `RecyclerView` 的
 * `Adapter` 直接消费并绑定到 item 视图。
 *
 * @property id 数据唯一标识，常用于 `DiffUtil` 或点击事件回传。
 * @property title 网格项主标题文本。
 * @property content 网格项说明/描述文本。
 * @property backgroundColor 网格项背景颜色值（`ColorInt`）。
 */
data class GridItem(
    val id: Int,
    val title: String,
    val content: String,
    val backgroundColor: Int
)

