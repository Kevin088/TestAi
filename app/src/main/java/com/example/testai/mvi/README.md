# MVI 架构登录示例

## MVI 架构说明

MVI (Model-View-Intent) 是一种单向数据流的架构模式。

1. 全称：Model - View - Intent
   * 模型 Model 代码UI的state , 包含UI需要展示的所有信息
   * View 视图
   * Intent 代表用户意图或系统事件 ， 通常用密封类表示 ，触发状态变化

2. 核心思想：
   * 数据只能单向流动，便于追踪和调试
   * 不可变状态，每次更新都创建新状态
   * 响应式编程，基于观察者模式，状态变化自动更新UI

3. 数据流向：
   * 用户操作->Intent->Model（状态更新）->View(ui刷新)
   




1. 初学者最容易犯的错误
❌ 局部更新 UI
❌ View 里修改 State
❌ State 不完整
❌ 没有 sealed class Intent
❌ 直接塞 LiveData + 叫 MVI







### 核心概念

1. **Intent (意图)** - `LoginIntent.kt`
   - 代表用户的所有操作和意图
   - 例如：输入用户名、输入密码、点击登录按钮

2. **Model (模型)** - `LoginViewModel.kt`
   - 处理Intent
   - 维护和更新ViewState
   - 执行业务逻辑

3. **ViewState (视图状态)** - `LoginViewState.kt`
   - 描述UI的完整状态
   - 是唯一的真实数据源

4. **View (视图)** - `LoginActivity.kt`
   - 发送Intent给ViewModel
   - 观察ViewState变化
   - 根据State渲染UI

### 数据流向

```
User Action → Intent → ViewModel → ViewState → View (UI Update)
```

### 使用示例

测试账号：
- 用户名：admin
- 密码：123456

### MVI 优势

- 单向数据流，易于理解和调试
- State不可变，避免状态混乱
- 易于测试
- 时间旅行调试（可以记录所有State变化）

### 文件说明

- `LoginIntent.kt` - 定义所有用户操作
- `LoginViewState.kt` - 定义UI状态
- `LoginViewModel.kt` - 处理业务逻辑
- `LoginActivity.kt` - UI层实现
- `activity_login.xml` - 布局文件
