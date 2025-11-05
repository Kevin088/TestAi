# TestAi

一个 Android 应用项目，使用 Kotlin 和 Jetpack Compose 开发，包含多种动画效果和布局组件。

## 📱 项目简介

TestAi 是一个测试 Android 应用，展示了多种动画效果和自定义布局组件的实现，包括宠物动画、PK对战动画、自适应网格布局等。

## 🛠 技术栈

- **开发语言**: Kotlin
- **UI 框架**: Jetpack Compose
- **构建工具**: Gradle (Kotlin DSL)
- **最低 SDK**: API 24 (Android 7.0)
- **目标 SDK**: API 35 (Android 15)
- **编译 SDK**: API 35

## ✨ 主要功能模块

### 🎮 动画系统
- **Pet 动画**: 宠物动画管理器，支持动画下载和播放
- **PK 动画**: PK 对战动画控制器，支持卡片视图和用户交互

### 📐 布局组件
- **自适应网格布局**: AdaptiveGridLayoutManager 和 AdaptiveGridAdapter
- **Fireway 网格**: FirewayGridActivity 和 TrackLayoutManager
- **连接布局**: ConnectionLayout 自定义组件

### 🎨 UI 组件
- **发光条**: VerticalGlowBarView 和预览界面
- **主题系统**: 完整的 Material 3 主题配置

## 📁 项目结构

```
app/src/main/java/com/example/testai/
├── adapter/                 # 数据适配器
│   └── AdaptiveGridAdapter.kt
├── fireway2/                # Fireway 网格功能
│   ├── FirewayGridActivity.kt
│   └── TrackLayoutManager.kt
├── layout/                  # 布局管理器
│   └── AdaptiveGridLayoutManager.kt
├── model/                   # 数据模型
│   └── GridItem.kt
├── petanim/                 # 宠物动画系统
│   ├── PetAnimationDownloader.kt
│   ├── PetAnimationExample.kt
│   ├── PetAnimationManager.kt
│   ├── PetAnimationModels.kt
│   └── PetAnimationType.kt
├── pkanim/                  # PK 动画系统
│   ├── PkActivity.kt
│   ├── PkAnimationController.kt
│   ├── PkCardView.kt
│   └── PkUser.kt
├── ui/theme/                # UI 主题配置
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
├── view/                    # 自定义视图
│   ├── ConnectionLayout.kt
│   └── VerticalGlowBarView.kt
├── MainActivity.kt          # 主活动
├── MainActivity2.kt         # 第二个主活动
└── Utils.kt                 # 工具类
```

## 🚀 构建和运行

### 环境要求
- Android Studio Arctic Fox 或更高版本
- JDK 17
- Android SDK API 35

### 运行步骤
1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd TestAi
   ```

2. **打开项目**
   - 使用 Android Studio 打开项目根目录

3. **同步依赖**
   - Android Studio 会自动同步 Gradle 依赖
   - 或者手动执行: `./gradlew sync`

4. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击运行按钮或执行 `./gradlew installDebug`

## 📚 依赖库

项目使用的主要依赖库：

```kotlin
// Jetpack Compose
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.ui)
implementation(libs.androidx.ui.graphics)
implementation(libs.androidx.material3)

// AndroidX 核心库
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.lifecycle.runtime.ktx)
implementation(libs.androidx.activity.compose)

// 布局和兼容性
implementation(libs.androidx.constraintlayout)
implementation(libs.androidx.appcompat)
implementation(libs.material)
```

## 🧪 测试

项目包含单元测试和仪器化测试：

- **单元测试**: `app/src/test/java/`
- **仪器化测试**: `app/src/androidTest/java/`

运行测试：
```bash
# 运行所有测试
./gradlew test
./gradlew connectedAndroidTest
```

## 📋 开发说明

### 代码风格
- 遵循 Kotlin 官方代码风格指南
- 使用 Compose 最佳实践
- 保持代码简洁和可读性

### 贡献指南
1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

[请添加许可证信息]

---

**注意**: 此项目为测试和学习目的开发，包含多个动画和布局的示例实现。
