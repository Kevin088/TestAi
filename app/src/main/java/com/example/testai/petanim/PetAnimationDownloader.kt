package com.example.testai.petanim

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * 仙宠动画资源下载器
 * 负责下载动画资源文件
 */
class PetAnimationDownloader {
    
    companion object {
        private const val TAG = "PetAnimationDownloader"
        private const val CONNECT_TIMEOUT = 10000 // 10秒连接超时
        private const val READ_TIMEOUT = 30000    // 30秒读取超时
    }
    
    /**
     * 下载资源文件
     * @param resourceUrl 资源URL
     * @param localPath 本地存储路径
     * @return 是否下载成功
     */
    suspend fun downloadResource(resourceUrl: String, localPath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "开始下载资源: $resourceUrl -> $localPath")
                
                // 创建本地目录
                val localFile = File(localPath)
                localFile.parentFile?.mkdirs()
                
                // 建立网络连接
                val url = URL(resourceUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = CONNECT_TIMEOUT
                connection.readTimeout = READ_TIMEOUT
                connection.requestMethod = "GET"
                
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 下载文件
                    connection.inputStream.use { input ->
                        FileOutputStream(localFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    
                    Log.d(TAG, "资源下载成功: $localPath, 文件大小: ${localFile.length()} bytes")
                    true
                } else {
                    Log.e(TAG, "下载失败，响应码: $responseCode")
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "下载资源异常: $resourceUrl", e)
                false
            }
        }
    }
    
    /**
     * 检查本地文件是否存在且有效
     */
    fun isLocalResourceValid(localPath: String): Boolean {
        val file = File(localPath)
        return file.exists() && file.length() > 0
    }
    
    /**
     * 删除本地资源文件
     */
    fun deleteLocalResource(localPath: String): Boolean {
        return try {
            val file = File(localPath)
            if (file.exists()) {
                file.delete()
            } else {
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "删除本地资源失败: $localPath", e)
            false
        }
    }
    
    /**
     * 清理所有本地动画资源
     */
    fun clearAllLocalResources(cacheDir: File): Boolean {
        return try {
            val animationDir = File(cacheDir, "pet_animations")
            if (animationDir.exists() && animationDir.isDirectory) {
                animationDir.deleteRecursively()
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "清理本地资源失败", e)
            false
        }
    }
}