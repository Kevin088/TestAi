# Cursor 2.0.54 使用指南

> 本指南帮助你快速掌握 Cursor AI 编辑器的核心功能和使用技巧

## 📚 目录

- [核心功能](#核心功能)
- [快捷键操作](#快捷键操作)
- [代码编辑方式](#代码编辑方式)
- [上下文引用](#上下文引用)
- [实用技巧](#实用技巧)
- [项目理解功能](#项目理解功能)
- [代码生成](#代码生成)
- [调试辅助](#调试辅助)
- [最佳实践](#最佳实践)
- [实际应用场景](#实际应用场景)
- [高级功能](#高级功能)

---

## 🎯 核心功能

### AI 对话模式

#### Ask 模式（只读）
- 📖 **用途**：询问问题、查询代码、理解项目
- 🔒 **特点**：不会修改文件，只提供建议
- 💡 **适用场景**：探索和学习代码库

#### Agent 模式（可编辑）
- ✏️ **用途**：直接修改文件、运行命令
- ⚡ **特点**：完整的编辑权限
- 🚀 **适用场景**：实际开发和代码实现

### 模式切换
```
左下角切换按钮：Ask ⇄ Agent
```

---

## ⌨️ 快捷键操作

### 核心快捷键

| 快捷键 | macOS | Windows/Linux | 功能 |
|--------|-------|---------------|------|
| 内联编辑 | `Cmd + K` | `Ctrl + K` | 在代码中直接编辑 |
| 侧边栏聊天 | `Cmd + L` | `Ctrl + L` | 打开 AI 聊天面板 |
| Composer | `Cmd + I` | `Ctrl + I` | 多文件编辑模式 |
| 新建会话 | `Cmd + Shift + L` | `Ctrl + Shift + L` | 开始新的对话 |
| 接受建议 | `Tab` | `Tab` | 接受 AI 建议 |
| 拒绝建议 | `Esc` | `Esc` | 取消 AI 建议 |

### 导航快捷键

| 快捷键 | macOS | Windows/Linux | 功能 |
|--------|-------|---------------|------|
| 文件搜索 | `Cmd + P` | `Ctrl + P` | 快速打开文件 |
| 符号搜索 | `Cmd + T` | `Ctrl + T` | 搜索函数、类等 |
| 全局搜索 | `Cmd + Shift + F` | `Ctrl + Shift + F` | 全局文本搜索 |

---

## ✏️ 代码编辑方式

### 1. 内联编辑（Cmd/Ctrl + K）

最快速的代码修改方式：

```
步骤：
1. 选中需要修改的代码
2. 按 Cmd+K (macOS) 或 Ctrl+K (Windows)
3. 输入修改需求，例如：
   - "添加空值检查"
   - "转换为 Kotlin 协程"
   - "优化这个循环"
4. AI 生成修改建议
5. 按 Tab 接受或 Esc 拒绝
```

**示例**：
```kotlin
// 选中以下代码，按 Cmd+K，输入"添加异常处理"
fun loadData() {
    val result = api.fetchData()
    display(result)
}
```

### 2. 侧边栏聊天（Cmd/Ctrl + L）

适合讨论和多步骤操作：

```
优势：
✓ 可以进行多轮对话
✓ AI 会自动读取相关文件
✓ 可以引用多个文件和符号
✓ 保留对话历史
```

**使用场景**：
- 询问代码问题
- 讨论实现方案
- 分析错误和 Bug
- 学习项目架构

### 3. Composer 模式（Cmd/Ctrl + I）

多文件编辑的强大工具：

```
特点：
✓ 同时编辑多个文件
✓ 跨文件重构
✓ 大型功能开发
✓ 架构调整
```

**使用场景**：
- 重构整个模块
- 添加新功能（需要修改多个文件）
- 统一代码风格
- 批量更新 API

---

## 🔗 上下文引用

### @ 符号引用系统

Cursor 提供强大的上下文引用功能，通过 `@` 符号可以精确指定 AI 关注的内容。

#### 文件引用

```bash
@filename.kt              # 引用特定文件
@app/MainActivity.kt      # 引用路径下的文件
@*.gradle.kts             # 引用所有匹配的文件
```

**示例**：
```
"@MainActivity.kt 这个 Activity 的生命周期是如何管理的？"
```

#### 文件夹引用

```bash
@folder/                  # 引用整个文件夹
@app/src/main/java/       # 引用特定路径
```

**示例**：
```
"@petanim/ 这个包里的动画系统是如何工作的？"
```

#### 代码库引用

```bash
@codebase                 # 搜索整个代码库
```

**示例**：
```
"@codebase 找到所有使用 RecyclerView 的地方"
```

#### 网络搜索

```bash
@web                      # 搜索最新的网络信息
```

**示例**：
```
"@web Jetpack Compose 1.5 有哪些新特性？"
```

#### 文档引用

```bash
@docs                     # 引用官方文档
```

**示例**：
```
"@docs Kotlin 协程的最佳实践"
```

#### Git 信息

```bash
@git                      # 引用 Git 历史和状态
```

**示例**：
```
"@git 最近的提交改变了什么？"
```

### 自动上下文

Cursor 会智能分析：
- ✅ 当前打开的文件
- ✅ 最近编辑的文件
- ✅ 相关的依赖文件
- ✅ 项目结构和配置

---

## 💡 实用技巧

### 技巧 1：精确提问

**❌ 不好的问题**：
```
"优化这个"
"修复 bug"
"改进代码"
```

**✅ 好的问题**：
```
"将这个回调改为 Kotlin 协程，并添加超时处理"
"修复列表滚动时的内存泄漏问题"
"重构这个函数，使用策略模式替代 if-else"
```

### 技巧 2：分步骤操作

对于复杂任务，分解为小步骤：

```
步骤 1: "分析当前的架构问题"
步骤 2: "提供重构方案"
步骤 3: "先重构 ViewModel"
步骤 4: "然后更新 UI 层"
步骤 5: "最后添加测试"
```

### 技巧 3：使用具体示例

```
❌ "添加一个列表"
✅ "添加一个用户列表，每个条目显示头像、姓名和邮箱，
    支持点击跳转到详情页"
```

### 技巧 4：指定技术栈

```
"使用 Jetpack Compose 创建一个登录界面"
"用 Retrofit + Coroutines 实现网络请求"
"使用 Room 数据库存储用户信息"
```

### 技巧 5：参考现有代码

```
"参考 @PetAnimationManager.kt 的实现方式，
 创建一个新的 GiftAnimationManager"
```

---

## 🔍 项目理解功能

### 架构分析

```bash
# 提问示例
"这个项目的整体架构是什么？"
"使用了哪些设计模式？"
"各个模块之间是如何交互的？"
```

### 代码理解

```bash
# 理解特定代码
"@TrackLayoutManager.kt 这个自定义 LayoutManager 的原理是什么？"
"解释一下 PkAnimationController 的工作流程"
"ConnectionLayout 是如何实现的？"
```

### 依赖分析

```bash
# 分析依赖关系
"MainActivity 依赖了哪些类？"
"哪些文件使用了 AdaptiveGridAdapter？"
"找到所有调用 Utils.kt 的地方"
```

### 代码搜索

```bash
# 功能搜索
"@codebase 找到所有网络请求的代码"
"@codebase 搜索所有 TODO 注释"
"@codebase 哪里处理了用户点击事件？"
```

---

## 🚀 代码生成

### 从注释生成代码

**方法 1：TODO 注释**
```kotlin
// TODO: 创建一个显示加载状态的 Composable 函数
// 应该支持加载中、成功和错误三种状态

// 选中注释，Cmd+K，输入"实现这个功能"
```

**方法 2：详细描述**
```kotlin
/**
 * 需求：创建一个用户信息卡片
 * - 显示头像（圆形）
 * - 显示姓名和简介
 * - 支持点击展开详情
 * - 使用 Material 3 风格
 */

// 选中注释，Cmd+K，输入"实现这个 Composable"
```

### 从需求生成代码

**示例 1：创建 ViewModel**
```
"创建一个 UserViewModel，包含以下功能：
1. 使用 StateFlow 管理用户列表
2. 提供加载用户数据的方法（使用协程）
3. 支持搜索和过滤
4. 处理加载状态（Loading, Success, Error）
5. 添加适当的错误处理"
```

**示例 2：创建 UI 组件**
```
"使用 Jetpack Compose 创建一个自定义的搜索框：
- 带有搜索图标
- 支持清空按钮
- 实时搜索回调
- 可配置占位符文本
- Material 3 样式"
```

### 样板代码生成

```bash
# Activity/Fragment
"创建一个新的 Activity，使用 ViewBinding"

# Adapter
"创建一个 RecyclerView Adapter，显示字符串列表"

# 数据类
"创建一个 User 数据类，包含 id, name, email, avatar"
```

---

## 🐛 调试辅助

### 分析错误

**崩溃日志分析**：
```
"分析这个崩溃日志，找出原因：
[粘贴崩溃堆栈]"
```

**编译错误**：
```
"这个编译错误是什么意思，如何修复？
[粘贴错误信息]"
```

### 性能优化

```bash
# 性能问题
"@MainActivity.kt 这个界面为什么会卡顿？"
"优化这个列表的渲染性能"
"减少这个函数的内存占用"
```

### 代码审查

```bash
# 质量检查
"这段代码有什么潜在问题？"
"检查是否有内存泄漏风险"
"这里的异常处理是否完善？"
"是否符合 Kotlin 最佳实践？"
```

### 单元测试

```bash
# 生成测试
"为这个函数生成 JUnit 测试用例"
"创建 ViewModel 的单元测试"
"添加边界条件测试"
```

---

## ✅ 最佳实践

### 推荐做法

#### 1. 明确描述需求
```
✅ "创建一个支持下拉刷新的用户列表，使用 SwipeRefreshLayout，
    列表项显示头像、姓名和在线状态，点击跳转到详情页"

❌ "创建一个列表"
```

#### 2. 提供足够的上下文
```
✅ "@UserRepository.kt @UserViewModel.kt 
    基于现有的架构，添加用户搜索功能"

❌ "添加搜索"
```

#### 3. 指定代码风格
```
✅ "使用 Kotlin 协程、LiveData 和 MVVM 模式实现数据加载，
    遵循 Clean Architecture 原则"

❌ "实现数据加载"
```

#### 4. 迭代改进
```
第一次: "创建基础的登录界面"
第二次: "添加输入验证"
第三次: "添加加载动画"
第四次: "添加错误提示"
```

#### 5. 验证和测试
```
生成代码后：
1. 仔细审查代码逻辑
2. 运行测试
3. 检查边界条件
4. 确保符合项目规范
```

### 避免的做法

#### ❌ 1. 过于模糊
```
❌ "优化一下"
❌ "改进性能"
❌ "修复问题"
```

#### ❌ 2. 一次性要求太多
```
❌ "创建完整的电商系统，包括用户、商品、订单、支付..."
```

#### ❌ 3. 不提供上下文
```
❌ "添加这个功能" （没有说明是什么功能）
❌ "修改这里" （没有说明如何修改）
```

#### ❌ 4. 盲目接受建议
```
❌ 不检查就应用所有 AI 建议
❌ 不理解代码就直接使用
```

---

## 🎬 实际应用场景

### 场景 1：快速原型开发

**需求**：创建一个新闻列表页面

**步骤**：
```
1. 新建文件: "创建 NewsListActivity.kt"

2. 创建数据类:
   "创建 NewsItem 数据类，包含 id, title, content, imageUrl, publishTime"

3. 创建列表:
   "创建一个 RecyclerView，显示新闻列表，每个条目显示图片和标题"

4. 添加点击事件:
   "添加点击事件，跳转到新闻详情页，传递新闻 ID"

5. 添加加载状态:
   "添加加载动画和错误处理"
```

### 场景 2：代码重构

**需求**：将 Activity 重构为 MVVM 架构

**步骤**：
```
1. 分析现有代码:
   "@MainActivity.kt 分析当前的架构问题"

2. 创建 ViewModel:
   "创建 MainViewModel，将业务逻辑从 Activity 中移出"

3. 更新 Activity:
   "重构 MainActivity，使用 ViewModel 和 LiveData"

4. 添加数据层:
   "创建 Repository 层，分离数据访问逻辑"

5. 添加测试:
   "为 ViewModel 添加单元测试"
```

### 场景 3：添加新功能

**需求**：给列表添加搜索和排序

**步骤**：
```
1. 添加搜索框:
   "在工具栏添加搜索框，实时过滤列表"

2. 实现搜索逻辑:
   "在 ViewModel 中实现搜索功能，使用 Flow 操作符"

3. 添加排序菜单:
   "添加菜单，支持按时间、名称排序"

4. 保存状态:
   "使用 SavedStateHandle 保存搜索和排序状态"
```

### 场景 4：修复 Bug

**需求**：解决列表滚动卡顿

**步骤**：
```
1. 分析问题:
   "@AdaptiveGridAdapter.kt 这个 Adapter 为什么会导致卡顿？"

2. 查看性能:
   "检查 onBindViewHolder 是否有耗时操作"

3. 优化方案:
   "使用 DiffUtil 优化列表更新"

4. 图片加载:
   "使用 Glide 优化图片加载，添加缓存"

5. 验证修复:
   "添加性能监控代码"
```

### 场景 5：学习理解

**需求**：理解复杂的自定义组件

**步骤**：
```
1. 整体理解:
   "@TrackLayoutManager.kt 这个类的作用是什么？"

2. 核心逻辑:
   "解释 onLayoutChildren 方法的实现原理"

3. 关键算法:
   "scrollVerticallyBy 是如何处理滚动的？"

4. 使用示例:
   "给一个使用 TrackLayoutManager 的示例代码"

5. 优缺点:
   "这个实现有什么优点和潜在问题？"
```

### 场景 6：文档编写

**需求**：为代码添加文档

**步骤**：
```
1. 类文档:
   "为 PetAnimationManager 添加详细的 KDoc 注释"

2. 方法文档:
   "为所有公开方法添加参数说明和返回值说明"

3. 使用示例:
   "在类注释中添加使用示例代码"

4. README:
   "生成这个模块的 README.md"
```

---

## 🔧 高级功能

### 1. 自定义提示词

在设置中配置常用的提示词模板：

```
设置 → AI Rules → Custom Instructions

示例：
"在生成代码时，始终：
- 使用 Kotlin 协程而不是回调
- 添加适当的空值检查
- 包含错误处理
- 使用 Material 3 组件
- 添加 KDoc 注释"
```

### 2. Rules for AI

在项目根目录创建 `.cursorrules` 文件：

```markdown
# Android 项目编码规范

## 语言和框架
- 使用 Kotlin 作为主要开发语言
- UI 优先使用 Jetpack Compose
- 遵循 Material Design 3 规范

## 架构
- 使用 MVVM 架构模式
- 遵循 Clean Architecture 原则
- 使用 Repository 模式处理数据

## 代码风格
- 遵循 Kotlin 官方代码规范
- 使用有意义的变量名
- 函数保持简短（不超过 50 行）
- 适当添加注释

## 最佳实践
- 使用 Kotlin 协程处理异步操作
- 使用 StateFlow/LiveData 管理状态
- 资源字符串放在 strings.xml
- 颜色定义在 colors.xml
- 尺寸定义在 dimens.xml

## 错误处理
- 所有网络请求必须有错误处理
- 使用 Result 或 sealed class 封装结果
- 向用户显示友好的错误信息

## 测试
- 重要逻辑必须有单元测试
- 使用 JUnit 5 和 MockK
- 测试覆盖率 > 80%

## 安全
- 敏感数据使用加密存储
- API 密钥不要硬编码
- 使用 ProGuard/R8 混淆代码

## 性能
- 避免主线程执行耗时操作
- 合理使用缓存
- 图片使用适当的压缩
- 列表使用 DiffUtil
```

### 3. 工作区设置

```json
// .vscode/settings.json 或 Cursor 设置
{
  "cursor.ai.model": "gpt-4",
  "cursor.ai.maxTokens": 4000,
  "cursor.ai.temperature": 0.7,
  "cursor.ai.autoSuggest": true
}
```

### 4. 批量操作

**场景**：统一更新导入语句

```
"@codebase 将所有文件中的 
'import com.example.old.*' 
替换为 
'import com.example.new.*'"
```

### 5. 代码迁移

**场景**：从 Java 迁移到 Kotlin

```
1. "@OldActivity.java 将这个 Java 文件转换为 Kotlin"
2. "优化转换后的代码，使用 Kotlin 特性"
3. "添加空值安全检查"
4. "使用 Kotlin 协程替换 AsyncTask"
```

### 6. 多语言支持

```
"为这个界面添加多语言支持：
- 提取所有硬编码的字符串到 strings.xml
- 创建英文和中文的资源文件"
```

---

## 📊 针对 TestAi 项目的具体应用

基于你的 Android 项目，这些场景特别有用：

### 理解现有代码

```bash
# 动画系统
"@petanim/ 宠物动画系统的架构是怎样的？"
"@PkAnimationController.kt PK 动画的实现原理"

# 布局管理
"@TrackLayoutManager.kt 这个自定义布局管理器如何工作？"
"@AdaptiveGridLayoutManager.kt 自适应网格的实现逻辑"

# 自定义视图
"@VerticalGlowBarView.kt 发光条效果是如何实现的？"
"@ConnectionLayout.kt 连接布局的绘制原理"
```

### 添加功能

```bash
# 新增动画类型
"参考 @PetAnimationManager.kt，创建一个新的礼物动画管理器"

# 扩展布局功能
"基于 @AdaptiveGridAdapter.kt，添加拖拽排序功能"

# UI 增强
"为 @GlowBarPreviewActivity.kt 添加实时预览功能"
```

### 优化改进

```bash
# 性能优化
"优化 @FirewayGridActivity.kt 的渲染性能"
"减少 @PkActivity.kt 的内存占用"

# 代码重构
"重构 @MainActivity.kt，使用 MVVM 架构"
"将 @Utils.kt 按功能拆分为多个工具类"
```

### 添加文档

```bash
# 模块文档
"为 petanim 包生成详细的使用文档"
"为 pkanim 包添加 API 说明"

# 代码注释
"为 @PetAnimationModels.kt 添加详细的 KDoc"
"为所有公开 API 添加使用示例"
```

---

## 🎓 学习资源

### 官方资源
- [Cursor 官方文档](https://cursor.sh/docs)
- [Cursor Changelog](https://cursor.sh/changelog)
- [Cursor 社区论坛](https://forum.cursor.sh)

### 视频教程
- YouTube 搜索 "Cursor AI Tutorial"
- 查找 "Cursor vs GitHub Copilot" 对比视频

### 技巧博客
- Medium 上的 Cursor 使用技巧
- Dev.to 上的实战案例

---

## 💬 常见问题 (FAQ)

### Q: Cursor 是否需要联网？
A: 是的，Cursor 需要联网与 AI 模型通信。

### Q: 数据安全吗？
A: Cursor 遵循严格的隐私政策，不会存储你的代码。建议查看官方隐私政策。

### Q: 支持哪些编程语言？
A: 支持几乎所有主流编程语言，包括 Kotlin, Java, Python, JavaScript, TypeScript 等。

### Q: 如何取消 AI 建议？
A: 按 `Esc` 键即可取消当前的 AI 建议。

### Q: 可以离线使用吗？
A: 不可以，Cursor 的 AI 功能需要联网。

### Q: 如何提高 AI 回答的准确性？
A: 
1. 提供清晰明确的问题
2. 使用 @ 引用提供充足上下文
3. 分步骤处理复杂任务
4. 提供具体的技术栈要求

---

## 🚀 快速开始清单

- [ ] 熟悉快捷键：`Cmd+K`, `Cmd+L`, `Cmd+I`
- [ ] 学会使用 @ 引用系统
- [ ] 尝试 Ask 和 Agent 两种模式
- [ ] 创建项目的 `.cursorrules` 文件
- [ ] 练习精确描述需求
- [ ] 使用 Composer 进行多文件编辑
- [ ] 探索代码理解功能
- [ ] 尝试代码生成和重构
- [ ] 利用 AI 进行调试和优化

---

## 📝 总结

Cursor 2.0.54 是一个强大的 AI 编程助手，掌握以下核心要点：

1. **模式选择**：根据任务选择 Ask 或 Agent 模式
2. **快捷键**：熟练使用 `Cmd+K`, `Cmd+L`, `Cmd+I`
3. **上下文**：使用 `@` 提供精确的上下文
4. **清晰表达**：明确、具体地描述需求
5. **迭代改进**：分步骤完成复杂任务
6. **验证审查**：始终检查 AI 生成的代码

**记住**：Cursor 是助手，不是替代品。最好的使用方式是人机协作，发挥各自优势！

---

*最后更新：2025-11-05*
*适用版本：Cursor 2.0.54*


