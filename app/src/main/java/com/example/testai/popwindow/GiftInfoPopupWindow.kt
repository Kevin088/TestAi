package com.example.testai.popwindow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.example.testai.databinding.PopupGiftInfoBinding

/**
 * 礼物信息提示PopupWindow
 * 用于显示扭蛋抽取规则说明
 */
class GiftInfoPopupWindow(context: Context) : PopupWindow() {
    
    private val binding: PopupGiftInfoBinding
    
    init {
        binding = PopupGiftInfoBinding.inflate(LayoutInflater.from(context))
        
        // 设置PopupWindow属性
        contentView = binding.root
        width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        
        // 设置点击外部可关闭
        isOutsideTouchable = true
        isFocusable = true
        
        // 设置背景，使点击外部关闭生效
        setBackgroundDrawable(null)
    }
    
    /**
     * 设置完整内容，自动解析带*的字符串
     * 格式: "主要描述\n*规则1\n*规则2"
     */
    fun setContent(content: String) {
        val lines = content.split("\n")
        val mainDesc = StringBuilder()
        val rules = mutableListOf<String>()
        
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("*")) {
                // 去掉开头的*和空格
                rules.add(trimmed.substring(1).trim())
            } else if (trimmed.isNotEmpty()) {
                mainDesc.append(trimmed).append("\n")
            }
        }
        
        // 设置主要描述
        binding.tvMainDescription.text = mainDesc.toString().trim()
        
        // 设置规则
        if (rules.size > 0) {
            binding.tvRule1.text = rules[0]
        }
        if (rules.size > 1) {
            binding.tvRule2.text = rules[1]
        }
    }
    
    /**
     * 设置主要描述文本
     */
    fun setMainDescription(text: String) {
        binding.tvMainDescription.text = text
    }
    
    /**
     * 设置规则1文本
     */
    fun setRule1(text: String) {
        binding.tvRule1.text = text
    }
    
    /**
     * 设置规则2文本
     */
    fun setRule2(text: String) {
        binding.tvRule2.text = text
    }
    
    /**
     * 解析完整的文本内容
     * 格式: "主描述\n*规则1\n*规则2"
     * 
     * @param fullText 完整的文本内容
     */
    fun parseAndSetContent(fullText: String) {
        // 找到第一个 \n* 的位置
        val firstRuleIndex = fullText.indexOf("\n*")
        
        if (firstRuleIndex == -1) {
            // 没有找到规则，全部作为主描述
            setMainDescription(fullText)
            return
        }
        
        // 截取主描述（第一个\n*之前的内容）
        val mainDescription = fullText.substring(0, firstRuleIndex).trim()
        setMainDescription(mainDescription)
        
        // 截取规则部分（第一个\n*之后的内容）
        val rulesText = fullText.substring(firstRuleIndex).trim()
        
        // 按 \n* 分割规则
        val rules = rulesText.split("\n*")
            .filter { it.isNotBlank() }
            .map { it.trim().removePrefix("*").trim() }
        
        // 设置规则文本
        if (rules.isNotEmpty()) {
            setRule1(rules[0])
        }
        if (rules.size > 1) {
            setRule2(rules[1])
        }
    }
    
    /**
     * 在指定View下方显示
     */
    fun showBelowView(anchorView: View) {
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        
        // 在锚点View下方显示，带一点偏移
        showAtLocation(
            anchorView,
            android.view.Gravity.NO_GRAVITY,
            location[0],
            location[1] + anchorView.height + 10
        )
    }
    
    /**
     * 在指定View上方显示
     */
    fun showAboveView(anchorView: View) {
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        
        // 先测量PopupWindow的高度
        contentView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        val popupHeight = contentView.measuredHeight
        
        // 在锚点View上方显示
        showAtLocation(
            anchorView,
            android.view.Gravity.NO_GRAVITY,
            location[0],
            location[1] - popupHeight - 10
        )
    }
}
