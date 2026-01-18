# TASK_StudyPage

## 1. 任务列表

### Task 1: 注册 Activity
- **文件**: `app/src/main/AndroidManifest.xml`
- **内容**: 添加 `LearningActivity` 的注册信息。
- **验收**: 编译通过，无 Manifest 错误。

### Task 2: 修改 MainActivity 逻辑
- **文件**: `app/src/main/java/com/example/fjnuserviceapp/MainActivity.java`
- **子任务**:
    1. 修改 `onCreate` 中的默认 Fragment 为 `LifeFragment`。
    2. 修改 `btn_study` 的点击事件，改为启动 `LearningActivity`。
- **验收**:
    - 启动应用默认显示“生活”页。
    - 点击“学习”按钮跳转到新页面。

### Task 3: 验证
- **操作**: 运行应用，测试跳转和返回。
- **验收**: 功能符合预期，无崩溃。

## 2. 依赖关系
- Task 1 -> Task 2 -> Task 3
