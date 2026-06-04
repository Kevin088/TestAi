---
name: openspec-propose
description: 一次性创建 OpenSpec 变更提案及其全部规划产物。适用于用户快速描述想构建的内容，并希望生成可实施的 proposal、design、specs、tasks。
license: MIT
compatibility: 需要 openspec CLI。
metadata:
  author: openspec
  version: "1.0"
  generatedBy: "1.4.1"
---

创建新的 OpenSpec 变更，并一次性生成实施前需要的规划产物。

我会创建一个 change，并生成：
- `proposal.md`：说明做什么、为什么做
- `design.md`：说明如何实现
- `specs/**/*.md`：定义能力和行为规格
- `tasks.md`：拆解实施步骤

准备开始实现时，运行 `/opsx:apply`。

---

**输入**：用户请求中应包含一个 kebab-case 的变更名称，或描述想构建/修复的内容。

**步骤**

1. **如果输入不清楚，先询问要做什么**

   使用开放式提问询问：
   > “你想处理什么变更？请描述要构建或修复的内容。”

   根据描述推导 kebab-case 名称，例如“添加用户认证”可推导为 `add-user-auth`。

   **重要**：在理解用户要构建什么之前，不要继续。

2. **创建变更目录**

   ```bash
   openspec new change "<name>"
   ```

   该命令会在 CLI 结合 `.openspec.yaml` 解析出的 planning home 中创建变更脚手架。

3. **获取产物构建顺序**

   ```bash
   openspec status --change "<name>" --json
   ```

   解析 JSON：
   - `applyRequires`：实施前必须完成的 artifact ID 数组，例如 `["tasks"]`
   - `artifacts`：所有 artifact 的状态和依赖
   - `planningHome`、`changeRoot`、`artifactPaths`、`actionContext`：路径、范围和约束上下文。使用这些字段，不要假设固定路径。

4. **按依赖顺序创建产物，直到达到可实施状态**

   按 artifact 依赖顺序循环处理，优先处理没有未完成依赖的 artifact。

   对每个状态为 `ready` 的 artifact：
   - 获取写作说明：
     ```bash
     openspec instructions <artifact-id> --change "<name>" --json
     ```
   - 指令 JSON 通常包含：
     - `context`：项目背景和约束，只供你参考，不要复制到产物中
     - `rules`：artifact 专属规则，只供你参考，不要复制到产物中
     - `template`：输出文件应使用的结构
     - `instruction`：该 artifact 类型的具体写作指导
     - `resolvedOutputPath`：输出文件的解析后路径或模式
     - `dependencies`：需要读取的已完成依赖产物
   - 读取所有已完成依赖文件作为上下文。
   - 按 `template` 结构创建产物，写入 `resolvedOutputPath`。
   - 遵守 `context` 和 `rules`，但不要把这些内部约束原样写进文件。
   - 简短汇报进度，例如：“已创建 <artifact-id>”。

   每创建一个 artifact 后，重新运行：
   ```bash
   openspec status --change "<name>" --json
   ```

   当 `applyRequires` 中所有 artifact 在 `artifacts` 里均为 `done` 时停止。

   如果 artifact 需要用户输入且上下文确实不清楚，先询问用户，再继续。

5. **展示最终状态**

   ```bash
   openspec status --change "<name>"
   ```

**输出**

完成全部 artifact 后，总结：
- 变更名称和位置
- 已创建的 artifact 及简要说明
- 当前状态：“所有产物已创建，已准备好实施。”
- 提示：“运行 `/opsx:apply`，或让我开始实现任务。”

**产物创建准则**

- 按 `openspec instructions` 返回的 `instruction` 字段创建每类 artifact。
- schema 决定文件内容和结构，必须遵守。
- 创建新 artifact 前，读取其依赖产物。
- 使用 `template` 的结构并填充内容。
- **重要**：`context`、`rules` 是给你的约束，不是输出内容。
  - 不要把 `<context>`、`<rules>`、`<project_context>` 块写入 artifact。
  - 它们只用于指导你写作。

**护栏**

- 创建 schema 的 `apply.requires` 所要求的全部实施前产物。
- 写下一个产物前，始终读取依赖产物。
- 关键上下文不清楚时可以询问，但默认应作合理判断推进。
- 如果同名 change 已存在，询问用户是继续使用还是创建新名称。
- 写完每个 artifact 后，确认文件确实存在。
