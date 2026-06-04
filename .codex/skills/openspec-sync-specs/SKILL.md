---
name: openspec-sync-specs
description: 将 change 中的 delta specs 同步到主 specs。适用于用户希望根据 delta spec 更新主规格，但暂时不归档 change。
license: MIT
compatibility: 需要 openspec CLI。
metadata:
  author: openspec
  version: "1.0"
  generatedBy: "1.4.1"
---

将某个 change 中的 delta specs 同步到主 specs。

这是一个 **agent-driven** 操作：你需要读取 delta specs，并直接编辑主 specs 来应用变更。这样可以进行智能合并，例如只新增一个 scenario，而不是整段覆盖 requirement。

**输入**：可选地指定 change 名称。如果省略，先尝试从对话上下文推断；如果模糊或存在歧义，必须提示用户从可用 changes 中选择。

**步骤**

1. **如果没有提供 change 名称，提示选择**

   运行：
   ```bash
   openspec list --json
   ```

   展示包含 delta specs（即 `specs/` 目录下有内容）的 changes。

   **重要**：不要猜测或自动选择。始终让用户明确选择。

2. **解析 change 上下文**

   ```bash
   openspec status --change "<name>" --json
   ```

   如果 status 报告 `actionContext.mode: "workspace-planning"`，说明当前切片不支持 workspace spec sync，停止。不要退回到 repo-local 路径，也不要编辑关联仓库。

3. **查找 delta specs**

   使用 status JSON 中的 `artifactPaths.specs.existingOutputPaths` 作为 delta spec 文件列表。

   每个 delta spec 文件可能包含：
   - `## ADDED Requirements`：要新增的 requirements
   - `## MODIFIED Requirements`：要修改的 requirements
   - `## REMOVED Requirements`：要移除的 requirements
   - `## RENAMED Requirements`：要重命名的 requirements，使用 `FROM:`/`TO:` 格式

   如果没有找到 delta specs，告知用户并停止。

4. **逐个 delta spec 应用到主 specs**

   对 CLI 返回的每个 repo-local capability delta spec：

   a. **读取 delta spec**，理解预期变更。

   b. **读取主 spec**：`openspec/specs/<capability>/spec.md`。该文件可能尚不存在。

   c. **智能应用变更**：

   **ADDED Requirements：**
   - 如果主 spec 中不存在该 requirement，则新增。
   - 如果已存在，则更新为 delta 中的内容，视为隐式 MODIFIED。

   **MODIFIED Requirements：**
   - 在主 spec 中找到对应 requirement。
   - 应用变更，可能包括：
     - 新增 scenario，不必复制已有 scenario。
     - 修改已有 scenario。
     - 修改 requirement 描述。
   - 保留 delta 未提及的 scenario 和内容。

   **REMOVED Requirements：**
   - 从主 spec 中移除整个 requirement block。

   **RENAMED Requirements：**
   - 找到 `FROM` requirement，并重命名为 `TO`。

   d. **如果 capability 尚不存在，创建新的主 spec**
   - 创建 `openspec/specs/<capability>/spec.md`。
   - 添加简短 Purpose section，可标记为 TBD。
   - 添加 Requirements section，并写入 ADDED requirements。

5. **展示摘要**

   同步完成后总结：
   - 更新了哪些 capabilities
   - 做了哪些变更：新增、修改、移除、重命名 requirements

**Delta Spec 格式参考**

```markdown
## ADDED Requirements

### Requirement: 新功能
系统 SHALL 执行一项新的行为。

#### Scenario: 基础场景
- **WHEN** 用户执行 X
- **THEN** 系统执行 Y

## MODIFIED Requirements

### Requirement: 既有功能
#### Scenario: 新增场景
- **WHEN** 用户执行 A
- **THEN** 系统执行 B

## REMOVED Requirements

### Requirement: 废弃功能

## RENAMED Requirements

- FROM: `### Requirement: 旧名称`
- TO: `### Requirement: 新名称`
```

**关键原则：智能合并**

不同于机械合并，你可以应用 **部分更新**：
- 要新增 scenario，只需在 MODIFIED 下包含该 scenario，不必复制已有 scenario。
- delta 表达的是意图，不是整段替换。
- 使用判断力进行合理合并。

**成功输出示例**

```markdown
## Specs 已同步：<change-name>

已更新主 specs：

**<capability-1>**：
- 新增 requirement：“新功能”
- 修改 requirement：“既有功能”（新增 1 个 scenario）

**<capability-2>**：
- 创建新 spec 文件
- 新增 requirement：“另一个功能”

主 specs 已更新。该 change 仍保持 active，实施完成后再 archive。
```

**护栏**

- 编辑前同时读取 delta spec 和主 spec。
- 保留 delta 未提及的已有内容。
- 不清楚时询问用户。
- 进行变更时说明正在改什么。
- 操作应保持幂等：重复执行应得到相同结果。
