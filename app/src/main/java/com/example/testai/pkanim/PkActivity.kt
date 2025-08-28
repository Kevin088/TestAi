package com.example.testai.pkanim

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.testai.R

/**
 * PK界面Activity
 * 展示四人PK卡片布局，使用自定义PkCardView组件
 * 支持4人→3人的动画效果
 */
class PkActivity : AppCompatActivity() {

    // 四个PK卡片视图引用
    private lateinit var cardUser1: PkCardView
    private lateinit var cardUser2: PkCardView
    private lateinit var cardUser3: PkCardView
    private lateinit var cardUser4: PkCardView
    
    // 测试按钮引用
    private lateinit var btnTestCard2: Button
    private lateinit var btnTestCard3: Button
    private lateinit var btnTestCard4: Button
    private lateinit var btnReset: Button
    
    // 动画控制器
    private lateinit var animationController: PkAnimationController
    
    // 用户数据
    private lateinit var users: List<PkUser>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_pk_four_cards)
        
        initViews()
        initAnimationController()
        initData()
        bindData()
        setupCardClickListeners()
        setupTestButtons()
    }

    /**
     * 初始化视图引用
     */
    private fun initViews() {
        cardUser1 = findViewById(R.id.card_user_1)
        cardUser2 = findViewById(R.id.card_user_2)
        cardUser3 = findViewById(R.id.card_user_3)
        cardUser4 = findViewById(R.id.card_user_4)
        
        btnTestCard2 = findViewById(R.id.btn_test_card2)
        btnTestCard3 = findViewById(R.id.btn_test_card3)
        btnTestCard4 = findViewById(R.id.btn_test_card4)
        btnReset = findViewById(R.id.btn_reset)
    }

    /**
     * 初始化动画控制器
     */
    private fun initAnimationController() {
        animationController = PkAnimationController()
    }

    /**
     * 设置测试按钮事件
     */
    private fun setupTestButtons() {
        // 第2个离开按钮
        btnTestCard2.setOnClickListener {
            animationController.animateCard2Leave(
                cardUser1, cardUser2, cardUser3, cardUser4
            ) {
                showToast("场景一：第2个卡片已离开")
            }
        }
        
        // 第3个离开按钮
        btnTestCard3.setOnClickListener {
            animationController.animateCard3Leave(
                cardUser1, cardUser2, cardUser3, cardUser4
            ) {
                showToast("场景二：第3个卡片已离开")
            }
        }
        
        // 第4个离开按钮
        btnTestCard4.setOnClickListener {
            animationController.animateCard4Leave(
                cardUser1, cardUser2, cardUser3, cardUser4
            ) {
                showToast("场景三：第4个卡片已离开")
            }
        }
        
        // 重置按钮
        btnReset.setOnClickListener {
            resetAllCards()
        }
    }
    /**
     * 设置卡片点击事件，触发不同的动画场景
     */
    private fun setupCardClickListeners() {
        // 点击卡片，显示点击反馈动画
        cardUser1.setOnClickListener {
            animationController.createBounceAnimation(cardUser1).start()
            showToast("点击了第1个卡片")
        }
        
        cardUser2.setOnClickListener {
            animationController.createBounceAnimation(cardUser2).start()
            showToast("点击了第2个卡片")
        }
        
        cardUser3.setOnClickListener {
            animationController.createBounceAnimation(cardUser3).start()
            showToast("点击了第3个卡片")
        }
        
        cardUser4.setOnClickListener {
            animationController.createBounceAnimation(cardUser4).start()
            showToast("点击了第4个卡片")
        }
    }

    /**
     * 初始化测试数据
     */
    private fun initData() {
        users = PkUser.createTestUsers()
    }

    /**
     * 绑定数据到视图
     */
    private fun bindData() {
        val cards = arrayOf(cardUser1, cardUser2, cardUser3, cardUser4)
        
        users.forEachIndexed { index, user ->
            if (index < cards.size) {
                cards[index].bindUser(user)
            }
        }
    }

    /**
     * 切换到简化模式（只显示头像和昵称）
     */
    fun switchToSimplifiedMode() {
        cardUser1.showSimplified()
        cardUser2.showSimplified()
        cardUser3.showSimplified()
        cardUser4.showSimplified()
    }

    /**
     * 切换到完整模式（显示所有信息）
     */
    fun switchToCompleteMode() {
        cardUser1.showComplete()
        cardUser2.showComplete()
        cardUser3.showComplete()
        cardUser4.showComplete()
    }

    /**
     * 设置指定位置卡片的选中状态
     */
    fun setCardSelected(position: Int, selected: Boolean) {
        val cards = arrayOf(cardUser1, cardUser2, cardUser3, cardUser4)
        if (position in cards.indices) {
            cards[position].setSelected(selected)
        }
    }

    /**
     * 重置所有卡片到初始状态
     */
    private fun resetAllCards() {
        animationController.resetAllCards(cardUser1, cardUser2, cardUser3, cardUser4)
        showToast("已重置所有卡片")
    }

    /**
     * 显示提示消息
     */
    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}