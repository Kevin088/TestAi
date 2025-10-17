package com.example.testai.petanim

import android.content.Context
import android.util.Log

/**
 * 仙宠动画使用示例
 * 展示如何使用PetAnimationManager进行动画播放
 */
class PetAnimationExample(private val context: Context) {
    
    private val animationManager = PetAnimationManager.getInstance(context)
    
    init {
        // 设置动画播放回调
        animationManager.setPlayCallback(object : AnimationPlayCallback {
            override fun onStart(animationType: PetAnimationType) {
                Log.d("PetAnimExample", "动画开始播放: ${animationType.description}")
            }
            
            override fun onComplete(animationType: PetAnimationType) {
                Log.d("PetAnimExample", "动画播放完成: ${animationType.description}")
            }
            
            override fun onError(animationType: PetAnimationType, error: String) {
                Log.e("PetAnimExample", "动画播放错误: ${animationType.description}, 错误: $error")
            }
            
            override fun onInterrupted(animationType: PetAnimationType, interruptedBy: PetAnimationType) {
                Log.d("PetAnimExample", "动画被打断: ${animationType.description} -> ${interruptedBy.description}")
            }
        })
    }
    
    /**
     * 播放小礼物动画
     */
    fun playSmallGiftAnimation() {
        val success = animationManager.playAnimation(
            PetAnimationType.SMALL_GIFT,
            "https://example.com/animations/small_gift.anim"
        )
        Log.d("PetAnimExample", "请求播放小礼物动画: $success")
    }
    
    /**
     * 播放PK攻击动画
     */
    fun playPkAttackAnimation() {
        val success = animationManager.playAnimation(
            PetAnimationType.PK_ATTACK,
            "https://example.com/animations/pk_attack.anim"
        )
        Log.d("PetAnimExample", "请求播放PK攻击动画: $success")
    }
    
    /**
     * 播放弹窗飘屏动画（最高优先级）
     */
    fun playPopupFloatAnimation() {
        val success = animationManager.playAnimation(
            PetAnimationType.POPUP_FLOAT,
            "https://example.com/animations/popup_float.anim"
        )
        Log.d("PetAnimExample", "请求播放弹窗飘屏动画: $success")
    }
    
    /**
     * 播放中大礼物动画
     */
    fun playMediumLargeGiftAnimation() {
        val success = animationManager.playAnimation(
            PetAnimationType.MEDIUM_LARGE_GIFT,
            "https://example.com/animations/medium_large_gift.anim"
        )
        Log.d("PetAnimExample", "请求播放中大礼物动画: $success")
    }
    
    /**
     * 测试优先级打断
     * 先播放低优先级动画，再播放高优先级动画，观察打断效果
     */
    fun testPriorityInterruption() {
        // 先播放低优先级的常规动画
        animationManager.playAnimation(
            PetAnimationType.NORMAL_STYLE,
            "https://example.com/animations/normal_style.anim"
        )
        
        // 延迟一段时间后播放高优先级的PK攻击动画
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            animationManager.playAnimation(
                PetAnimationType.PK_ATTACK,
                "https://example.com/animations/pk_attack.anim"
            )
        }, 1000)
    }
    
    /**
     * 获取当前播放状态
     */
    fun getCurrentStatus(): String {
        val state = animationManager.getCurrentPlayState()
        val currentAnim = animationManager.getCurrentPlayingAnimation()
        return "状态: $state, 当前动画: ${currentAnim?.description ?: "无"}"
    }
    
    /**
     * 停止当前动画
     */
    fun stopCurrentAnimation() {
        animationManager.stopCurrentAnimation()
        Log.d("PetAnimExample", "已停止当前动画")
    }
    
    /**
     * 暂停当前动画
     */
    fun pauseCurrentAnimation() {
        animationManager.pauseCurrentAnimation()
        Log.d("PetAnimExample", "已暂停当前动画")
    }
    
    /**
     * 恢复当前动画
     */
    fun resumeCurrentAnimation() {
        animationManager.resumeCurrentAnimation()
        Log.d("PetAnimExample", "已恢复当前动画")
    }
    
    /**
     * 清理资源
     */
    fun cleanup() {
        animationManager.cleanup()
        Log.d("PetAnimExample", "已清理动画管理器资源")
    }
    
    /**
     * 演示如何对比两个PetAnimationType对象
     */
    fun demonstrateAnimationTypeComparison() {
        Log.d("PetAnimExample", "=== 动画类型对比演示 ===")
        
        val type1 = PetAnimationType.PK_ATTACK
        val type2 = PetAnimationType.PK_VICTORY 
        val type3 = PetAnimationType.PK_ATTACK
        val type4 = PetAnimationType.SMALL_GIFT
        
        // 1. 基本相等性对比
        Log.d("PetAnimExample", "1. 基本相等性对比:")
        Log.d("PetAnimExample", "PK_ATTACK == PK_VICTORY: ${type1 == type2}")
        Log.d("PetAnimExample", "PK_ATTACK == PK_ATTACK: ${type1 == type3}")
        Log.d("PetAnimExample", "PK_ATTACK.isEqualTo(PK_ATTACK): ${type1.isEqualTo(type3)}")
        
        // 2. 使用静态方法对比
        Log.d("PetAnimExample", "\n2. 使用静态方法对比:")
        Log.d("PetAnimExample", "areEqual(PK_ATTACK, PK_VICTORY): ${PetAnimationType.areEqual(type1, type2)}")
        Log.d("PetAnimExample", "areEqual(PK_ATTACK, PK_ATTACK): ${PetAnimationType.areEqual(type1, type3)}")
        
        // 3. 根据typeId对比
        Log.d("PetAnimExample", "\n3. 根据typeId对比:")
        Log.d("PetAnimExample", "PK_ATTACK.isEqualTo(typeId=8): ${type1.isEqualTo(8)}")
        Log.d("PetAnimExample", "PK_ATTACK.isEqualTo(typeId=10): ${type1.isEqualTo(10)}")
        
        // 4. 根据描述对比
        Log.d("PetAnimExample", "\n4. 根据描述对比:")
        Log.d("PetAnimExample", "PK_ATTACK.isEqualTo('PK Attack'): ${type1.isEqualTo("PK Attack")}")
        Log.d("PetAnimExample", "PK_ATTACK.isEqualTo('PK Victory'): ${type1.isEqualTo("PK Victory")}")
        
        // 5. 优先级对比
        Log.d("PetAnimExample", "\n5. 优先级对比:")
        Log.d("PetAnimExample", "PK_ATTACK priority: ${type1.priority}, SMALL_GIFT priority: ${type4.priority}")
        Log.d("PetAnimExample", "PK_ATTACK.hasHigherPriorityThan(SMALL_GIFT): ${type1.hasHigherPriorityThan(type4)}")
        Log.d("PetAnimExample", "SMALL_GIFT.hasLowerPriorityThan(PK_ATTACK): ${type4.hasLowerPriorityThan(type1)}")
        Log.d("PetAnimExample", "PK_ATTACK.hasSamePriorityAs(PK_VICTORY): ${type1.hasSamePriorityAs(type2)}")
        
        // 6. 使用静态方法对比优先级
        Log.d("PetAnimExample", "\n6. 使用静态方法对比优先级:")
        val priorityComparison = PetAnimationType.comparePriority(type1, type4)
        Log.d("PetAnimExample", "comparePriority(PK_ATTACK, SMALL_GIFT): $priorityComparison") // 应该返回-1，表示PK_ATTACK优先级更高
        Log.d("PetAnimExample", "hasHigherPriority(PK_ATTACK, SMALL_GIFT): ${PetAnimationType.hasHigherPriority(type1, type4)}")
        
        // 7. null值处理
        Log.d("PetAnimExample", "\n7. null值处理:")
        val nullType: PetAnimationType? = null
        Log.d("PetAnimExample", "areEqual(PK_ATTACK, null): ${PetAnimationType.areEqual(type1, nullType)}")
        Log.d("PetAnimExample", "areEqual(null, null): ${PetAnimationType.areEqual(nullType, nullType)}")
        
        Log.d("PetAnimExample", "=== 动画类型对比演示结束 ===")
    }
}