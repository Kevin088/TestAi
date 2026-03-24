package com.example.testai.popwindow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testai.databinding.ActivityPopupDemoBinding

/**
 * PopupWindow演示Activity
 */
class PopupDemoActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityPopupDemoBinding
    private var popupWindow: GiftInfoPopupWindow? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopupDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupListeners()
    }
    
    private fun setupListeners() {
        // 点击信息图标显示PopupWindow
        binding.ivInfo.setOnClickListener {
            showPopup(it)
        }
        
        // 在按钮下方显示
        binding.btnShowBelow.setOnClickListener {
            showPopup(it)
        }
        
        // 在按钮上方显示
        binding.btnShowAbove.setOnClickListener {
            showPopupAbove(it)
        }
    }
    
    private fun showPopup(anchorView: android.view.View) {
        // 关闭之前的PopupWindow
        popupWindow?.dismiss()
        
        // 完整的文本内容（从服务器或其他地方获取）
        val fullText = "每一次抽取该扭蛋都会积攒能量。能量攒满后能够爆发威力,因此第300次抽该扭蛋,必得[限定礼物]哦!\n*300次后将重置能量条\n*中途抽到限定礼物,不会重置能量条"
        
        // 创建新的PopupWindow
        popupWindow = GiftInfoPopupWindow(this).apply {
            // 解析并设置内容
            parseAndSetContent(fullText)
            
            // 在锚点View下方显示
            showBelowView(anchorView)
        }
    }
    
    private fun showPopupAbove(anchorView: android.view.View) {
        // 关闭之前的PopupWindow
        popupWindow?.dismiss()
        
        // 完整的文本内容
        val fullText = "每一次抽取该扭蛋都会积攒能量。能量攒满后能够爆发威力,因此第300次抽该扭蛋,必得[限定礼物]哦!\n*300次后将重置能量条\n*中途抽到限定礼物,不会重置能量条"
        
        // 创建新的PopupWindow并在上方显示
        popupWindow = GiftInfoPopupWindow(this).apply {
            // 解析并设置内容
            parseAndSetContent(fullText)
            
            // 在锚点View上方显示
            showAboveView(anchorView)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        popupWindow?.dismiss()
    }
}
