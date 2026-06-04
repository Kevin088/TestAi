package com.example.testai.ai_ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testai.R

/**
 * 订单确认页（仿图实现）
 */
class OrderConfirmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirm)

        findViewById<android.view.View>(R.id.ivBack).setOnClickListener {
            finish()
        }
    }
}
