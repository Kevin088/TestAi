---
name: openspec-archive-change
description: 归档已完成的 OpenSpec change。适用于实现完成后，用户希望收尾并归档变更。
license: MIT
compatibility: 需要 openspec CLI。
metadata:
  author: openspec
  version: "1.0"
  generatedBy: "1.4.1"
---

归档已完成的 OpenSpec change。

**输入**：可选地指定 change 名称。如果省略，先尝试从对话上下文推断；如果模糊或存在歧义，必须提示用户选择可用 change。

**步骤**

1. **如果没有提供 change 名称，提示选择**

   运行：
   ```bash
   openspec list --json
   ```

   只展示 active changes，不展示已归档 change。若可用，显示每个 change 使用的 schema。

   **重要**：不要猜测或自动选择。始终让用户明确选择。

2. **检查 artifact 完成状态**

   ```bash
   openspec status --change "<name>" --json
   ```

   解析 JSON：
   - `schemaName`：使用的工作流
   - `planningHome`、`changeRoot`、`artifactPaths`、`actionContext`：路径和范围上下文
   - `artifacts`：artifact 列表及其状态

   如果 `actionContext.mode` 为 `workspace-planning`，说明当前切片不支持 workspace archive，停止。不要把 workspace change 移到 repo-local archive，也不要编辑关联仓库。

   **如果存在未完成 artifact：**
   - 展示未完成 artifact 警告。
   - 询问用户是否仍要继续。
   - 用户确认后再继续。

3. **检查任务完成状态**

   读取任务文件，通常是 `tasks.md`。

   统计：
   - `- [ ]`：未完成任务
   - `- [x]`：已完成任务

   **如果存在未完成任务：**
   - 展示未完成任务数量警告。
   - 询问用户是否仍要继续。
   - 用户确认后再继续。

   **如果没有任务文件：** 不展示任务相关警告，继续。

4. **评估 delta spec 同步状态**

   使用 status JSON 中的 `artifactPaths.specs.existingOutputPaths` 检查 delta specs。如果不存在 delta specs，跳过同步提示。

   **如果存在 delta specs：**
   - 将每个 delta spec 与主规格 `openspec/specs/<capability>/spec.md` 对比。
   - 判断将应用哪些变更：新增、修改、移除、重命名。
   - 在询问前展示合并摘要。

   **提示选项：**
   - 如果需要同步：`立即同步（推荐）`、`不同步直接归档`
   - 如果已同步：`立即归档`、`仍然同步一次`、`取消`

   如果用户选择同步，使用 openspec-sync-specs 的方式先完成同步，再继续归档。

5. **执行归档**

   确保归档目录存在：
   ```bash
   mkdir -p "<planningHome.changesDir>/archive"
   ```

   使用当前日期生成目标名称：`YYYY-MM-DD-<change-name>`。

   **如果目标已存在：**
   - 报错并建议重命名现有归档或使用其他日期/名称。

   **如果目标不存在：**
   ```bash
   mv "<changeRoot>" "<planningHome.changesDir>/archive/YYYY-MM-DD-<name>"
   ```

6. **展示摘要**

   摘要包括：
   - change 名称
   - 使用的 schema
   - 归档位置
   - specs 是否已同步
   - 是否存在未完成 artifact/任务的确认警告

**成功输出示例**

```markdown
## 归档完成

**Change:** <change-name>
**Schema:** <schema-name>
**归档到：** <planningHome.changesDir>/archive/YYYY-MM-DD-<name>/
**规格：** ✓ 已同步到主规格（或“无 delta specs”“已跳过同步”）

所有 artifact 已完成。所有任务已完成。
```

**护栏**

- 未提供 change 名称时，始终提示用户选择。
- 使用 `openspec status --json` 的 artifact graph 检查完成度。
- 警告不一定阻止归档，但必须告知并确认。
- 移动目录时保留 `.openspec.yaml`。
- 清楚总结实际发生了什么。
- 如果用户要求同步，使用 openspec-sync-specs 的方式处理。
- 如果存在 delta specs，始终先做同步评估并展示摘要。
