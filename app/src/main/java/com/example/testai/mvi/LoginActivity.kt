package com.example.testai.mvi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.testai.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

/**
 * 登录Activity - MVI架构示例
 * 
 * MVI架构说明：
 * - Intent: 用户的所有操作（输入用户名、输入密码、点击登录等）
 * - Model: ViewModel处理Intent并更新State
 * - View: Activity观察State变化并更新UI
 */
class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initViewModel()
        observeViewState()
        setupListeners()
    }
    
    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }
    
    /**
     * 观察ViewState变化并更新UI
     */
    private fun observeViewState() {
        lifecycleScope.launch {
            viewModel.viewState.collect { state ->
                render(state)
            }
        }
    }
    
    /**
     * 根据State渲染UI
     */
    private fun render(state: LoginViewState) {
        binding.apply {
            // 更新加载状态
            progressBar.isVisible = state.isLoading
            btnLogin.isEnabled = !state.isLoading
            
            // 显示错误信息
            state.error?.let { error ->
                Toast.makeText(this@LoginActivity, error, Toast.LENGTH_SHORT).show()
                viewModel.handleIntent(LoginIntent.ClearError)
            }
            
            // 登录成功
            if (state.isLoginSuccess) {
                Toast.makeText(this@LoginActivity, "登录成功！", Toast.LENGTH_SHORT).show()
                // 这里可以跳转到主页面
            }
        }
    }
    
    /**
     * 设置监听器，将用户操作转换为Intent
     */
    private fun setupListeners() {
        binding.apply {
            // 用户名输入
            etUsername.addTextChangedListener { text ->
                viewModel.handleIntent(LoginIntent.UpdateUsername(text.toString()))
            }
            
            // 密码输入
            etPassword.addTextChangedListener { text ->
                viewModel.handleIntent(LoginIntent.UpdatePassword(text.toString()))
            }
            
            // 登录按钮
            btnLogin.setOnClickListener {
                viewModel.handleIntent(LoginIntent.Login)
            }
        }
    }
}
