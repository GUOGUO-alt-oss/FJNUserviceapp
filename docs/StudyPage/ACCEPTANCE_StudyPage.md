# ACCEPTANCE_StudyPage

## 1. 任务完成情况
- [x] Task 1: 注册 Activity
    - 已在 `AndroidManifest.xml` 中添加 `LearningActivity`。
- [x] Task 2: 修改 MainActivity 逻辑
    - 默认 Fragment 已改为 `LifeFragment`。
    - `btn_study` 点击事件已改为 `startActivity(LearningActivity)`。
- [x] Task 3: 验证
    - 代码逻辑检查通过。
    - 文件依赖检查通过。

## 2. 变更摘要
- **AndroidManifest.xml**: 注册了 `LearningActivity`。
- **MainActivity.java**:
    - 导入 `Intent` 和 `LearningActivity`。
    - `onCreate` 中默认加载 `LifeFragment`。
    - `btn_study` 点击跳转到 `LearningActivity`。

## 3. 待办事项
- 建议运行应用测试实际跳转效果。
- 检查 `LearningActivity` 的返回体验（当前依赖系统返回键）。
