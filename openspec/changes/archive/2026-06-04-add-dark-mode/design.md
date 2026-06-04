## 背景

该项目是单模块 Android 应用，同时使用 Jetpack Compose、AppCompat Activity、XML 布局、drawable 资源和自定义 View。Compose 侧已经有 `TestAiTheme`，可以通过 `isSystemInDarkTheme()` 跟随系统；但 Android 资源主题当前继承自 `Theme.AppCompat.Light.NoActionBar`，并且不少 XML 布局/drawable 使用了固定浅色样式。

因此暗黑模式需要同时覆盖两套 UI：使用 `MaterialTheme` 的 Compose 页面，以及依赖 Android 资源、AppCompat 夜间模式和自定义绘制代码的 View 页面。

## 目标 / 非目标

**目标：**

- 提供应用级浅色、暗色、跟随系统三种主题行为。
- 新安装和已有安装默认都使用跟随系统。
- 让 XML 布局、drawable 资源、弹窗/底部面板表面和自定义 View 在两种主题下都清晰可读。
- 持久化用户显式选择的主题，并足够早地应用，避免 Activity 首屏闪现错误主题。
- 验证代表性页面在浅色和暗色模式下的表现。

**非目标：**

- 不重新设计页面布局，也不改变暗黑模式之外的功能行为。
- 不引入新的设计系统依赖。
- 不支持单页面独立主题覆盖。
- 不修改启动图标或刻意保持主题无关的装饰性素材。

## 技术决策

1. 使用 AppCompat 夜间模式作为 View 体系 UI 的运行时开关。

   `AppCompatDelegate.setDefaultNightMode()` 可以在 AppCompat Activity 中统一应用 `MODE_NIGHT_FOLLOW_SYSTEM`、`MODE_NIGHT_NO` 和 `MODE_NIGHT_YES`。备选方案是逐页面手动重建 Activity 并覆盖资源，但这种方式更难保持一致，也更容易在新增页面时遗漏。

2. 将主题偏好存为轻量的应用级设置。

   持久化一个简单的枚举式值：`system`、`light` 或 `dark`。在应用启动或最早的公共初始化路径中应用该值，确保 Activity inflate 布局前主题已经生效。备选方案是只跟随系统主题，但这无法满足希望显式覆盖主题的用户。

3. 将颜色选择迁移到主题感知资源。

   将白色背景、深色文字等浅色专用值替换成语义化资源名或主题属性，并在 `values-night` 中定义对应覆盖值。这样可以让 XML 布局和 drawable 更易维护。备选方案是在每个布局里直接替换 `@color`，但这会延续硬编码模式，让未来页面更脆弱。

4. 让 Compose 主题绑定同一个最终生效模式。

   Compose 的 `TestAiTheme` 继续使用 Material color scheme，但当用户存在显式覆盖时，`darkTheme` 入参必须反映最终生效模式。备选方案是 Compose 侧独立使用 `isSystemInDarkTheme()` 判断，但当用户显式选择浅色或暗色时，这会和 AppCompat 行为不一致。

5. 将自定义 View 作为主题消费者处理。

   自定义 View 和 adapter 中通过代码设置颜色时，如果颜色影响可读性，应从主题属性或资源 ID 解析，而不是使用固定的 `Color.WHITE`、`Color.BLACK` 或原始十六进制值。若游戏/演示类装饰色在暗色下仍满足对比度，可以保持固定。

## 风险 / 取舍

- 部分硬编码颜色是装饰色而不是语义色 -> 逐项检查，只迁移表示表面、文字、控件、分割线或交互状态的颜色。
- Activity 启动后才应用夜间模式可能造成可见重建或闪屏 -> 在首次布局 inflate 前应用持久化主题模式。
- Compose 和 XML 如果使用不同模式来源可能产生漂移 -> 集中维护主题偏好映射，并用同一份存储设置驱动 AppCompat 和 Compose。
- 演示页面和弹窗中的视觉回归容易隐藏 -> 覆盖主页面、网格页、订单确认页、弹窗、底部面板和自定义 View 示例的代表性人工或截图检查。
