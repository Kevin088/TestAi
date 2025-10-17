package com.example.testai.petanim

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import java.io.File

/**
 * 仙宠动画流程管理类
 * 负责管理动画播放的优先级、资源下载和播放流程
 */
class PetAnimationManager private constructor(private val context: Context) {
    
    companion object {
        private const val TAG = "PetAnimationManager"
        
        @Volatile
        private var INSTANCE: PetAnimationManager? = null
        
        fun getInstance(context: Context): PetAnimationManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PetAnimationManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    // 当前播放状态
    private var currentPlayState = AnimationPlayState.IDLE
    
    // 当前播放的动画类型
    private var currentPlayingAnimation: PetAnimationType? = null
    
    // 协程作用域
    private val managerScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    /**
     * 获取资源下载器实例
     */
    private fun getResourceDownloader() = com.example.testai.petanim.PetAnimationDownloader()
    
    // 播放回调
    private var playCallback: AnimationPlayCallback? = null
    
    /**
     * 设置动画播放回调
     */
    fun setPlayCallback(callback: AnimationPlayCallback?) {
        this.playCallback = callback
    }
    
    /**
     * 播放动画
     * @param animationType 动画类型
     * @param resourceUrl 资源URL
     * @return 是否成功开始播放（优先级不足时直接舍弃）
     */
    fun playAnimation(animationType: PetAnimationType, resourceUrl: String): Boolean {
        Log.d(TAG, "请求播放动画: ${animationType.description}, 优先级: ${animationType.priority}")
        
        val animationResource = PetAnimationResource(
            animationType = animationType,
            resourceUrl = resourceUrl,
            localPath = getLocalPath(animationType, resourceUrl)
        )
        
        // 执行播放前置检查
        return performPrePlayCheck(animationResource)
    }
    
    /**
     * 播放前置检查
     */
    private fun performPrePlayCheck(animationResource: PetAnimationResource): Boolean {
        Log.d(TAG, "执行播放前置检查: ${animationResource.animationType.description}")
        
        // 1. 检查动画优先级
        if (!checkAnimationPriority(animationResource.animationType)) {
            Log.d(TAG, "动画优先级不足，直接舍弃动画: ${animationResource.animationType.description}")
            return false
        }
        
        // 2. 检查资源并播放
        managerScope.launch {
            checkAndDownloadResource(animationResource)
        }
        
        return true
    }
    
    /**
     * 检查动画优先级
     * @param newAnimationType 新的动画类型
     * @return 是否可以播放
     */
    private fun checkAnimationPriority(newAnimationType: PetAnimationType): Boolean {
        val currentAnimation = currentPlayingAnimation
        
        // 如果当前没有动画播放，直接允许
        if (currentAnimation == null || currentPlayState == AnimationPlayState.IDLE) {
            return true
        }
        
        // 比较优先级（数字越小优先级越高）
        val canInterrupt = newAnimationType.priority < currentAnimation.priority
        
        if (canInterrupt) {
            Log.d(TAG, "新动画优先级更高，打断当前动画: ${currentAnimation.description} -> ${newAnimationType.description}")
            interruptCurrentAnimation(newAnimationType)
        }
        
        return canInterrupt
    }
    
    /**
     * 打断当前动画
     */
    private fun interruptCurrentAnimation(newAnimationType: PetAnimationType) {
        val interruptedAnimation = currentPlayingAnimation
        if (interruptedAnimation != null) {
            playCallback?.onInterrupted(interruptedAnimation, newAnimationType)
        }
        
        stopCurrentAnimation()
    }
    
    /**
     * 检查并下载资源
     */
    private suspend fun checkAndDownloadResource(animationResource: PetAnimationResource) {
        // 检查本地资源是否存在
        val localFile = File(animationResource.localPath)
        
        if (localFile.exists() && localFile.length() > 0) {
            Log.d(TAG, "本地资源已存在: ${animationResource.localPath}")
            startPlayAnimation(animationResource.copy(isDownloaded = true))
        } else {
            Log.d(TAG, "开始下载资源: ${animationResource.resourceUrl}")
            downloadResource(animationResource)
        }
    }
    
    /**
     * 下载资源
     */
    private suspend fun downloadResource(animationResource: PetAnimationResource) {
        try {
            val success = getResourceDownloader().downloadResource(
                animationResource.resourceUrl,
                animationResource.localPath
            )
            
            if (success) {
                Log.d(TAG, "资源下载成功: ${animationResource.localPath}")
                startPlayAnimation(animationResource.copy(isDownloaded = true))
            } else {
                Log.e(TAG, "资源下载失败: ${animationResource.resourceUrl}")
                playCallback?.onError(animationResource.animationType, "资源下载失败")
            }
        } catch (e: Exception) {
            Log.e(TAG, "下载资源异常", e)
            playCallback?.onError(animationResource.animationType, "下载异常: ${e.message}")
        }
    }
    
    /**
     * 开始播放动画
     */
    private suspend fun startPlayAnimation(animationResource: PetAnimationResource) {
        if (!animationResource.isDownloaded) {
            Log.e(TAG, "资源未下载完成，无法播放")
            return
        }
        
        withContext(Dispatchers.Main) {
            currentPlayState = AnimationPlayState.PLAYING
            currentPlayingAnimation = animationResource.animationType
            
            Log.d(TAG, "开始播放动画: ${animationResource.animationType.description}")
            playCallback?.onStart(animationResource.animationType)
            
            // 模拟动画播放（实际项目中这里应该是真实的动画播放逻辑）
            simulateAnimationPlay(animationResource)
        }
    }
    
    /**
     * 模拟动画播放
     * 实际项目中应该替换为真实的动画播放逻辑
     */
    private fun simulateAnimationPlay(animationResource: PetAnimationResource) {
        managerScope.launch {
            // 模拟播放时长（实际应该根据动画资源的真实时长）
            val duration = when (animationResource.animationType.priority) {
                1, 2 -> 3000L // 弹窗和PK类动画较长
                3 -> 2000L    // 飘屏动画
                4 -> 1500L    // 位移动画
                5 -> 1000L    // 固定位置动画
                else -> 500L  // 默认动画
            }
            
            delay(duration)
            
            // 播放完成
            onAnimationComplete(animationResource.animationType)
        }
    }
    
    /**
     * 动画播放完成
     */
    private fun onAnimationComplete(animationType: PetAnimationType) {
        Log.d(TAG, "动画播放完成: ${animationType.description}")
        
        currentPlayState = AnimationPlayState.IDLE
        currentPlayingAnimation = null
        
        playCallback?.onComplete(animationType)
    }
    
    /**
     * 停止当前动画
     */
    fun stopCurrentAnimation() {
        if (currentPlayState == AnimationPlayState.PLAYING) {
            Log.d(TAG, "停止当前动画: ${currentPlayingAnimation?.description}")
            currentPlayState = AnimationPlayState.STOPPED
            currentPlayingAnimation = null
        }
    }
    
    /**
     * 暂停当前动画
     */
    fun pauseCurrentAnimation() {
        if (currentPlayState == AnimationPlayState.PLAYING) {
            Log.d(TAG, "暂停当前动画: ${currentPlayingAnimation?.description}")
            currentPlayState = AnimationPlayState.PAUSED
        }
    }
    
    /**
     * 恢复当前动画
     */
    fun resumeCurrentAnimation() {
        if (currentPlayState == AnimationPlayState.PAUSED) {
            Log.d(TAG, "恢复当前动画: ${currentPlayingAnimation?.description}")
            currentPlayState = AnimationPlayState.PLAYING
        }
    }
    
    /**
     * 获取当前播放状态
     */
    fun getCurrentPlayState(): AnimationPlayState = currentPlayState
    
    /**
     * 获取当前播放的动画类型
     */
    fun getCurrentPlayingAnimation(): PetAnimationType? = currentPlayingAnimation
    
    /**
     * 获取本地存储路径
     */
    private fun getLocalPath(animationType: PetAnimationType, resourceUrl: String): String {
        val fileName = "${animationType.typeId}_${resourceUrl.hashCode()}.anim"
        return File(context.cacheDir, "pet_animations/$fileName").absolutePath
    }
    
    /**
     * 清理资源
     */
    fun cleanup() {
        managerScope.cancel()
        currentPlayingAnimation = null
        currentPlayState = AnimationPlayState.IDLE
    }
}