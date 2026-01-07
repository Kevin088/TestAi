package com.example.testai.mvi

/**
 * 登录意图 - 用户的所有操作
 */
sealed class LoginIntent {
    data class UpdateUsername(val username: String) : LoginIntent()
    data class UpdatePassword(val password: String) : LoginIntent()
    object Login : LoginIntent()
    object ClearError : LoginIntent()
}
