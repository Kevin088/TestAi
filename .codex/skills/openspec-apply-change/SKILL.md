---
name: openspec-apply-change
description: 实施 OpenSpec change 中的任务。适用于用户想开始实现、继续实现，或逐项推进 tasks。
license: MIT
compatibility: 需要 openspec CLI。
metadata:
  author: openspec
  version: "1.0"
  generatedBy: "1.4.1"
---

根据 OpenSpec change 的任务清单实施代码变更。

**输入**：可选地指定 change 名称。如果省略，先尝试从对话上下文推断；如果模糊或存在歧义，必须提示用户从可用 change 中选择。

**步骤**

1. **选择 change**

   如果用户提供了名称，直接使用。否则：
   - 从对话上下文推断用户提到的 change。
   - 如果只有一个 active change，可以自动选择。
   - 如果有歧义，运行 `openspec list --json` 获取可用 change，并让用户选择。

   始终说明：“使用 change: <name>”，并提示如何覆盖，例如 `/opsx:apply <other>`。

2. **检查状态，理解 schema**

   ```bash
   openspec status --change "<name>" --json
   ```

   解析 JSON：
   - `schemaName`：当前工作流，例如 `spec-driven`
   - `planningHome`、`changeRoot`、`actionContext`：规划范围和编辑约束
   - 哪个 artifact 包含任务，通常是 `tasks`

3. **获取 apply 指令**

   ```bash
   openspec instructions apply --change "<name>" --json
   ```

   该命令返回：
   - `contextFiles`：artifact ID 到具体文件路径数组的映射
   - 当前进度：总任务数、已完成数、剩余数
   - 带状态的任务列表
   - 根据当前状态生成的动态实施指令

   **状态处理：**
   - 如果 `state: "blocked"`：说明缺少产物，建议继续完善 change。
   - 如果 `state: "all_done"`：说明全部完成，建议 archive。
   - 否则进入实施。

   **工作区护栏：**
   如果 status JSON 中 `actionContext.mode` 为 `workspace-planning` 且 `allowedEditRoots` 为空，说明当前切片不支持全工作区 apply。将关联仓库和目录视为只读上下文，要求用户通过明确实施流程选择影响区域，并在编辑文件前停止。

4. **读取上下文文件**

   读取 apply 指令中 `contextFiles` 列出的所有具体文件路径。

   常见情况：
   - `spec-driven`：读取 proposal、specs、design、tasks。
   - 其他 schema：以 CLI 返回的 `contextFiles` 为准。

5. **展示当前进度**

   展示：
   - 使用的 schema
   - 进度：“N/M tasks complete”
   - 剩余任务概览
   - CLI 返回的动态指令

6. **实施任务，直到完成或阻塞**

   对每个未完成任务：
   - 说明正在处理哪个任务。
   - 做必要的代码变更。
   - 保持变更最小且聚焦。
   - 完成后立即在 tasks 文件中将 `- [ ]` 改为 `- [x]`。
   - 继续下一个任务。

   **以下情况暂停：**
   - 任务不清楚，需要澄清。
   - 实施中发现设计问题，需要建议更新 artifact。
   - 遇到错误或阻塞，需要用户指导。
   - 用户中断。

7. **完成或暂停时展示状态**

   展示：
   - 本次完成的任务
   - 总体进度：“N/M tasks complete”
   - 如果全部完成，建议 archive。
   - 如果暂停，说明原因并等待指导。

**实施过程输出示例**

```markdown
## 正在实施：<change-name>（schema: <schema-name>）

正在处理任务 3/7：<task description>
...
✓ 任务完成
```

**完成时输出示例**

```markdown
## 实施完成

**Change:** <change-name>
**Schema:** <schema-name>
**进度：** 7/7 tasks complete ✓

### 本次完成
- [x] 任务 1
- [x] 任务 2

所有任务已完成，可以 archive 该 change。
```

**暂停时输出示例**

```markdown
## 实施暂停

**Change:** <change-name>
**Schema:** <schema-name>
**进度：** 4/7 tasks complete

### 遇到的问题
<问题描述>

**选项：**
1. <选项 1>
2. <选项 2>
3. 其他方案
```

**护栏**

- 持续推进任务，直到完成或真正阻塞。
- 开始前始终读取 apply 指令中的上下文文件。
- 任务含糊时，先暂停询问再实现。
- 实施发现问题时，暂停并建议更新 artifact。
- 代码变更保持最小且限定在任务范围内。
- 任务完成后立即更新 checkbox。
- 遇到错误、阻塞或需求不清时暂停，不要猜。
- 使用 CLI 返回的 `contextFiles`，不要假设固定文件名。

**流式工作流集成**

该技能支持“围绕 change 执行动作”的模式：

- **可随时调用**：artifact 未全部完成但已有 tasks 时、部分实施后、或与其他动作交错时均可使用。
- **允许更新 artifact**：如果实施中发现设计问题，建议更新 artifact；不必被阶段边界锁死。
