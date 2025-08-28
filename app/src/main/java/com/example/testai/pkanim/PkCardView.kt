package com.example.testai.pkanim

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.testai.R

/**
 * PK卡片自定义View组件
 * 封装单个PK用户卡片的UI展示和数据绑定逻辑
 */
class PkCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    // UI组件引用
    private val avatarImageView: ImageView
    private val voiceImageView: ImageView
    private val usernameTextView: TextView
    private val scoreTextView: TextView
    private val comboTextView: TextView
    private val scoreContainer: LinearLayout
    private val comboContainer: LinearLayout

    init {
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.view_pk_card, this, true)
        
        // 初始化UI组件引用
        avatarImageView = findViewById(R.id.iv_avatar)
        voiceImageView = findViewById(R.id.iv_voice)
        usernameTextView = findViewById(R.id.tv_username)
        scoreTextView = findViewById(R.id.tv_score)
        comboTextView = findViewById(R.id.tv_combo)
        scoreContainer = findViewById(R.id.ll_score)
        comboContainer = findViewById(R.id.ll_combo)
    }

    /**
     * 绑定PK用户数据到卡片
     */
    fun bindUser(user: PkUser) {
        // 设置用户名
        usernameTextView.text = user.name
        
        // 设置分数（格式化显示）
        scoreTextView.text = formatScore(user.score)
        
        // 设置连胜次数
        comboTextView.text = "x${user.comboCount}"
        
        // 设置语音图标可见性
        voiceImageView.visibility = if (user.hasVoice) VISIBLE else GONE
        
        // 设置头像（这里使用默认头像，实际项目中可以加载网络图片）
        avatarImageView.setImageResource(R.drawable.default_avatar)
        
        // 根据在线状态调整透明度
        alpha = if (user.isOnline) 1.0f else 0.6f
    }

    /**
     * 显示简化版卡片（只显示头像和昵称）
     */
    fun showSimplified() {
        scoreContainer.visibility = GONE
        comboContainer.visibility = GONE
        voiceImageView.visibility = GONE
    }

    /**
     * 显示完整版卡片
     */
    fun showComplete() {
        scoreContainer.visibility = VISIBLE
        comboContainer.visibility = VISIBLE
    }

    /**
     * 格式化分数显示
     */
    private fun formatScore(score: Long): String {
        return when {
            score >= 1000000 -> String.format("%.1fM", score / 1000000.0)
            score >= 1000 -> String.format("%.1fK", score / 1000.0)
            else -> score.toString()
        }
    }

    /**
     * 设置卡片选中状态
     */
    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        // 可以在这里添加选中状态的视觉效果
        alpha = if (selected) 1.0f else 0.8f
    }

    /**
     * 获取头像ImageView（用于设置头像图片）
     */
    fun getAvatarImageView(): ImageView = avatarImageView
}