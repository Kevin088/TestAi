package com.example.testai.petanim

/**
 * 动画场景类型常量
 */
object AnimationSceneType {
    /** 弹窗飘屏 */
    const val POPUP_FLOAT = 1
    
    /** 常规样式动画 */
    const val NORMAL_STYLE = 2
    
    /** 送小礼物 */
    const val SMALL_GIFT = 3
    
    /** 送中、大礼物 */
    const val MEDIUM_LARGE_GIFT = 4
    
    /** 开通贵族 */
    const val OPEN_NOBLE = 5
    
    /** 我要上墙 */
    const val WALL_UP = 6
    
    /** 进场欢迎 */
    const val ENTER_WELCOME = 7
    
    /** PK攻击 */
    const val PK_ATTACK = 8
    
    /** PK攻击弹道 */
    const val PK_TRAJECTORY = 9
    
    /** PK胜利 */
    const val PK_VICTORY = 10
    
    /** PK失败 */
    const val PK_FAIL = 11
    
    /** 飘屏 */
    const val FLOAT_SCREEN = 12
    
    /** 加守护团 */
    const val ADD_GUARD = 13
    
    /** 小时榜 */
    const val HOURLY_RANK = 14
}

/**
 * 动画资源数据类
 */
data class PetAnimationResource(
    val animationType: PetAnimationType,
    val resourceUrl: String,
    val localPath: String = "",
    val isDownloaded: Boolean = false,
    val duration: Long = 0L
)

/**
 * 动画播放状态
 */
enum class AnimationPlayState {
    IDLE,       // 空闲状态
    PLAYING,    // 播放中
    PAUSED,     // 暂停
    STOPPED     // 停止
}

/**
 * 动画播放结果回调
 */
interface AnimationPlayCallback {
    fun onStart(animationType: PetAnimationType)
    fun onComplete(animationType: PetAnimationType)
    fun onError(animationType: PetAnimationType, error: String)
    fun onInterrupted(animationType: PetAnimationType, interruptedBy: PetAnimationType)
}