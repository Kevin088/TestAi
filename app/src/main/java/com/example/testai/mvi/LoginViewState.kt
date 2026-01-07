package com.example.testai.mvi

/**
 * 登录视图状态 - UI的所有状态
 */
data class LoginViewState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccess: Boolean = false
)
