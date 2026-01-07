package com.example.testai.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 登录ViewModel - 处理Intent并更新State
 */
class LoginViewModel : ViewModel() {
    
    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState> = _viewState.asStateFlow()
    
    /**
     * 处理用户意图
     */
    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UpdateUsername -> updateUsername(intent.username)
            is LoginIntent.UpdatePassword -> updatePassword(intent.password)
            is LoginIntent.Login -> login()
            is LoginIntent.ClearError -> clearError()
        }
    }
    
    private fun updateUsername(username: String) {
        _viewState.value = _viewState.value.copy(username = username)
    }
    
    private fun updatePassword(password: String) {
        _viewState.value = _viewState.value.copy(password = password)
    }
    
    private fun login() {
        val currentState = _viewState.value
        
        // 验证输入
        if (currentState.username.isBlank()) {
            _viewState.value = currentState.copy(error = "用户名不能为空")
            return
        }
        
        if (currentState.password.isBlank()) {
            _viewState.value = currentState.copy(error = "密码不能为空")
            return
        }
        
        // 开始登录
        viewModelScope.launch {
            _viewState.value = currentState.copy(isLoading = true, error = null)
            
            // 模拟网络请求
            delay(2000)
            
            // 模拟登录逻辑
            if (currentState.username == "admin" && currentState.password == "123456") {
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    isLoginSuccess = true
                )
            } else {
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    error = "用户名或密码错误"
                )
            }
        }
    }
    
    private fun clearError() {
        _viewState.value = _viewState.value.copy(error = null)
    }
}
