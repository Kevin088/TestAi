package com.example.testai.pkanim

/**
 * PK用户数据模型
 */
data class PkUser(
    val id: Int,
    val name: String,
    val avatar: String, // 头像URL或资源ID
    val score: Long,
    val comboCount: Int = 0,
    val isOnline: Boolean = true,
    val hasVoice: Boolean = false, // 是否开启语音
    val position: Int = 0 // 在PK中的位置
) {
    companion object {
        // 创建测试数据
        fun createTestUsers(): List<PkUser> {
            return listOf(
                PkUser(
                    id = 1,
                    name = "K站桃天使",
                    avatar = "avatar_1",
                    score = 234567,
                    comboCount = 6,
                    hasVoice = true,
                    position = 1
                ),
                PkUser(
                    id = 2,
                    name = "初夏的夏夏～",
                    avatar = "avatar_2",
                    score = 100560,
                    comboCount = 3,
                    hasVoice = true,
                    position = 2
                ),
                PkUser(
                    id = 3,
                    name = "可爱的兔兔～",
                    avatar = "avatar_3",
                    score = 100560,
                    comboCount = 3,
                    hasVoice = true,
                    position = 3
                ),
                PkUser(
                    id = 4,
                    name = "火辣辣猫猫～",
                    avatar = "avatar_4",
                    score = 100560,
                    comboCount = 3,
                    hasVoice = true,
                    position = 4
                )
            )
        }
    }
}