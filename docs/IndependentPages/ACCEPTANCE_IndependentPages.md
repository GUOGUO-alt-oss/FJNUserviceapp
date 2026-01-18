# ACCEPTANCE_IndependentPages

## 1. 任务完成情况
- [x] Task 1: 创建 NotifyActivity
    - 创建了 `NotifyActivity.java` 和 `activity_notify.xml`。
    - 成功加载 `NotifyFragment`。
- [x] Task 2: 注册 Activity
    - 在 `AndroidManifest.xml` 中注册了 `NotifyActivity` 和 `MineActivity`。
- [x] Task 3: 修改 MainActivity 跳转
    - `btnNotify` 点击跳转 `NotifyActivity`。
    - `btnMine` 点击跳转 `MineActivity`。
    - 顶部头像点击也同步修改为跳转 `MineActivity`。

## 2. 变更摘要
- **新增文件**: `NotifyActivity.java`, `activity_notify.xml`
- **修改文件**: `AndroidManifest.xml`, `MainActivity.java`
- **复用资源**: 直接使用了现有的 `NotifyFragment` 和 `MineFragment`，未修改 Fragment 内部逻辑。

## 3. 待办事项
- 运行测试，确保 Activity 栈管理符合预期（按返回键回到主页）。
