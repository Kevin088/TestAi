package com.example.testai.model

/**
 * 网格项数据模型
 * @param id 唯一标识
 * @param title 标题
 * @param content 内容
 * @param backgroundColor 背景颜色
 */
data class GridItem(
    val id: Int,
    val title: String,
    val content: String,
    val backgroundColor: Int
)