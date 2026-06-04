package com.example.testai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testai.fireway2.FirewayGridActivity
import com.example.testai.mvi.LoginActivity
import com.example.testai.pkanim.PkActivity
import com.example.testai.testpanne.QualityPanelActivity
import com.example.testai.theme.ThemeMode
import com.example.testai.theme.ThemePreferences
import com.example.testai.ui.theme.TestAiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val selectedThemeMode = remember {
                mutableStateOf(ThemePreferences.getThemeMode(context))
            }

            TestAiTheme(darkTheme = selectedThemeMode.value.isDark(context)) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        selectedThemeMode = selectedThemeMode.value,
                        onThemeModeSelected = { mode ->
                            selectedThemeMode.value = mode
                            ThemePreferences.setThemeMode(context, mode)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    selectedThemeMode: ThemeMode = ThemeMode.SYSTEM,
    onThemeModeSelected: (ThemeMode) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TestAi 演示项目",
            modifier = Modifier.padding(bottom = 32.dp)
        )

        ThemeModeSelector(
            selectedMode = selectedThemeMode,
            onModeSelected = onThemeModeSelected
        )
        
        Button(
            onClick = {
                val intent = Intent(context, PkActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("PK四人卡片布局")
        }

        Button(
            onClick = {
                val intent = Intent(context, GridActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("自适应RecyclerView布局")
        }
        
        Button(
            onClick = {
                val intent = Intent(context, GlowBarPreviewActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("柔光光柱预览")
        }
        
        Button(
            onClick = {
                val intent = Intent(context, MainActivity2::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("AI服务测试")
        }
        
        Button(
            onClick = {
                val intent = Intent(context, FirewayGridActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("网格+对角线布局")
        }
        
        Button(
            onClick = {
                val intent = Intent(context, QualityPanelActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("清晰度选择半弹层")
        }
        
        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("MVI登录示例")
        }

    }
}

@Composable
private fun ThemeModeSelector(
    selectedMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Text("主题模式")
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            ThemeMode.entries.forEach { mode ->
                FilterChip(
                    selected = selectedMode == mode,
                    onClick = { onModeSelected(mode) },
                    label = {
                        Text(
                            when (mode) {
                                ThemeMode.SYSTEM -> "跟随系统"
                                ThemeMode.LIGHT -> "浅色"
                                ThemeMode.DARK -> "暗色"
                            }
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TestAiTheme {
        MainScreen()
    }
}
