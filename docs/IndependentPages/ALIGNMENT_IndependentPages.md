# ALIGNMENT_IndependentPages

## 1. 原始需求
用户要求：“通知模块和个人模块也是跳转到一个新的页面”。

## 2. 项目现状分析
- **MainActivity**: 目前包含“通知”和“我的”按钮，点击后在 Fragment 容器中切换。
- **Study 模块**: 刚刚已改造为跳转到独立的 `LearningActivity`。
- **Notify 模块**: 只有 `NotifyFragment`，没有独立的主 Activity。
- **Mine 模块**: 有 `MineActivity.java` 和布局，但未在 Manifest 中注册，且 `MainActivity` 并未使用它。

## 3. 需求理解与决策
- **目标**: 将“通知”和“我的”模块改为独立 Activity 跳转，与“学习”模块保持一致。
- **技术路径**:
    1.  **Notify**: 创建 `NotifyActivity`，作为 `NotifyFragment` 的容器。
    2.  **Mine**: 启用现有的 `MineActivity`，确保其逻辑正确加载 `MineFragment`。
    3.  **Manifest**: 注册这两个 Activity。
    4.  **MainActivity**: 修改点击事件为 `startActivity`。

## 4. 最终共识
- 创建 `NotifyActivity`。
- 注册 `NotifyActivity` 和 `MineActivity`。
- 修改 `MainActivity` 跳转逻辑。
