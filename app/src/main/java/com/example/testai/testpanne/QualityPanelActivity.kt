package com.example.testai.testpanne

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testai.R
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * 清晰度选择面板演示Activity
 * 点击"高清"按钮弹出底部半弹层选择清晰度
 */
class QualityPanelActivity : AppCompatActivity() {

    private lateinit var btnQuality: Button
    private var currentQuality = "超清"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quality_panel)

        initViews()
        setupListeners()
    }

    /**
     * 初始化视图
     */
    private fun initViews() {
        btnQuality = findViewById(R.id.btn_quality)
        btnQuality.text = currentQuality
    }

    /**
     * 设置监听器
     */
    private fun setupListeners() {
        btnQuality.setOnClickListener {
            showQualityBottomSheet()
        }
    }

    /**
     * 显示清晰度选择底部弹窗
     */
    private fun showQualityBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_quality, null)
        bottomSheetDialog.setContentView(view)

        // 获取清晰度选项
        val qualityBluray = view.findViewById<TextView>(R.id.quality_bluray)
        val qualityHd = view.findViewById<TextView>(R.id.quality_hd)

        // 根据当前选中的清晰度设置选中状态
        updateQualitySelection(qualityBluray, qualityHd)

        // 设置点击事件
        qualityBluray.setOnClickListener {
            currentQuality = "超清"
            btnQuality.text = currentQuality
            bottomSheetDialog.dismiss()
        }

        qualityHd.setOnClickListener {
            currentQuality = "高清"
            btnQuality.text = currentQuality
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    /**
     * 更新清晰度选项的选中状态
     */
    private fun updateQualitySelection(qualityBluray: TextView, qualityHd: TextView) {
        when (currentQuality) {
            "超清" -> {
                qualityBluray.setTextColor(getColor(android.R.color.black))
                qualityHd.setTextColor(getColor(android.R.color.darker_gray))
            }
            "高清" -> {
                qualityBluray.setTextColor(getColor(android.R.color.darker_gray))
                qualityHd.setTextColor(getColor(android.R.color.holo_red_light))
            }
        }
    }
}

