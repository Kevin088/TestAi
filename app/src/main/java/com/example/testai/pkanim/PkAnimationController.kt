package com.example.testai.pkanim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * PK卡片动画控制器
 * 实现4人→3人的三种动画场景
 */
class PkAnimationController {

    companion object {
        // 动画时长规范
        private const val FADE_OUT_DURATION = 600L    // 淘汰动画时长
        private const val LAYOUT_ADJUST_DURATION = 500L  // 布局调整动画时长
        
        // 动画插值器
        private val EASE_IN_OUT = AccelerateDecelerateInterpolator()
    }

    /**
     * 场景一：第2个卡片离开（右侧退出）
     */
    fun animateCard2Leave(
        card1: View, card2: View, card3: View, card4: View,
        onAnimationEnd: () -> Unit = {}
    ) {
        val animatorSet = AnimatorSet()
        
        // 第2个卡片的退出动画：向右移动 + 缩小 + 淡出
        val card2TranslateX = ObjectAnimator.ofFloat(card2, "translationX", 0f, 200f)
        val card2ScaleX = ObjectAnimator.ofFloat(card2, "scaleX", 1f, 0.8f)
        val card2ScaleY = ObjectAnimator.ofFloat(card2, "scaleY", 1f, 0.8f)
        val card2Alpha = ObjectAnimator.ofFloat(card2, "alpha", 1f, 0f)
        
        val card2Exit = AnimatorSet().apply {
            playTogether(card2TranslateX, card2ScaleX, card2ScaleY, card2Alpha)
            duration = FADE_OUT_DURATION
            interpolator = EASE_IN_OUT
        }
        
        // 剩余卡片的重新布局动画
        val card3MoveUp = ObjectAnimator.ofFloat(card3, "translationY", 0f, -70f)
        val card4MoveLeft = ObjectAnimator.ofFloat(card4, "translationX", 0f, -100f)
        val card4MoveUp = ObjectAnimator.ofFloat(card4, "translationY", 0f, -70f)
        
        val layoutAdjust = AnimatorSet().apply {
            playTogether(card3MoveUp, card4MoveLeft, card4MoveUp)
            duration = LAYOUT_ADJUST_DURATION
            interpolator = EASE_IN_OUT
            startDelay = FADE_OUT_DURATION - 200L // 提前开始布局调整
        }
        
        animatorSet.playTogether(card2Exit, layoutAdjust)
        animatorSet.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                card2.visibility = View.GONE
                onAnimationEnd()
            }
        })
        
        animatorSet.start()
    }

    /**
     * 场景二：第3个卡片离开（向下退出）
     */
    fun animateCard3Leave(
        card1: View, card2: View, card3: View, card4: View,
        onAnimationEnd: () -> Unit = {}
    ) {
        val animatorSet = AnimatorSet()
        
        // 第3个卡片的退出动画：向下移动 + 缩小 + 淡出
        val card3TranslateY = ObjectAnimator.ofFloat(card3, "translationY", 0f, 150f)
        val card3ScaleX = ObjectAnimator.ofFloat(card3, "scaleX", 1f, 0.7f)
        val card3ScaleY = ObjectAnimator.ofFloat(card3, "scaleY", 1f, 0.7f)
        val card3Alpha = ObjectAnimator.ofFloat(card3, "alpha", 1f, 0f)
        
        val card3Exit = AnimatorSet().apply {
            playTogether(card3TranslateY, card3ScaleX, card3ScaleY, card3Alpha)
            duration = FADE_OUT_DURATION
            interpolator = EASE_IN_OUT
        }
        
        // 剩余卡片的重新布局动画：a、b、d 靠拢居中
        val card2MoveDown = ObjectAnimator.ofFloat(card2, "translationY", 0f, 35f)
        val card4MoveLeft = ObjectAnimator.ofFloat(card4, "translationX", 0f, -100f)
        
        val layoutAdjust = AnimatorSet().apply {
            playTogether(card2MoveDown, card4MoveLeft)
            duration = LAYOUT_ADJUST_DURATION
            interpolator = EASE_IN_OUT
            startDelay = FADE_OUT_DURATION - 200L
        }
        
        animatorSet.playTogether(card3Exit, layoutAdjust)
        animatorSet.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                card3.visibility = View.GONE
                onAnimationEnd()
            }
        })
        
        animatorSet.start()
    }

    /**
     * 场景三：第4个卡片离开（右侧退出）
     */
    fun animateCard4Leave(
        card1: View, card2: View, card3: View, card4: View,
        onAnimationEnd: () -> Unit = {}
    ) {
        val animatorSet = AnimatorSet()
        
        // 第4个卡片的退出动画：向右滑出 + 淡出
        val card4TranslateX = ObjectAnimator.ofFloat(card4, "translationX", 0f, 250f)
        val card4Alpha = ObjectAnimator.ofFloat(card4, "alpha", 1f, 0f)
        
        val card4Exit = AnimatorSet().apply {
            playTogether(card4TranslateX, card4Alpha)
            duration = FADE_OUT_DURATION
            interpolator = EASE_IN_OUT
        }
        
        // 剩余卡片向右微调居中：a、b、c 整体居中
        val centerOffset = 50f
        val card1MoveRight = ObjectAnimator.ofFloat(card1, "translationX", 0f, centerOffset)
        val card2MoveRight = ObjectAnimator.ofFloat(card2, "translationX", 0f, centerOffset)
        val card3MoveRight = ObjectAnimator.ofFloat(card3, "translationX", 0f, centerOffset)
        
        val layoutAdjust = AnimatorSet().apply {
            playTogether(card1MoveRight, card2MoveRight, card3MoveRight)
            duration = LAYOUT_ADJUST_DURATION
            interpolator = EASE_IN_OUT
            startDelay = FADE_OUT_DURATION - 200L
        }
        
        animatorSet.playTogether(card4Exit, layoutAdjust)
        animatorSet.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                card4.visibility = View.GONE
                onAnimationEnd()
            }
        })
        
        animatorSet.start()
    }

    /**
     * 重置所有卡片到初始状态
     */
    fun resetAllCards(card1: View, card2: View, card3: View, card4: View) {
        val cards = arrayOf(card1, card2, card3, card4)
        
        cards.forEach { card ->
            card.visibility = View.VISIBLE
            card.translationX = 0f
            card.translationY = 0f
            card.scaleX = 1f
            card.scaleY = 1f
            card.alpha = 1f
        }
    }

    /**
     * 创建弹性缩放动画（卡片点击反馈）
     */
    fun createBounceAnimation(view: View): AnimatorSet {
        val scaleDown = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f)
            )
            duration = 100L
        }
        
        val scaleUp = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 1f)
            )
            duration = 100L
        }
        
        return AnimatorSet().apply {
            playSequentially(scaleDown, scaleUp)
        }
    }
}