# TASK_IndependentPages

## 1. 任务列表

### Task 1: 创建 NotifyActivity
- **文件**: 
    - `app/src/main/java/com/example/fjnuserviceapp/ui/notify/NotifyActivity.java`
    - `app/src/main/res/layout/activity_notify.xml`
- **内容**: 实现 Activity 容器并加载 `NotifyFragment`。

### Task 2: 注册 Activity
- **文件**: `AndroidManifest.xml`
- **内容**: 注册 `NotifyActivity` 和 `MineActivity`。

### Task 3: 修改 MainActivity 跳转
- **文件**: `MainActivity.java`
- **内容**: 
    - `btnNotify` -> `startActivity(NotifyActivity)`
    - `btnMine` -> `startActivity(MineActivity)`
    - 清理旧代码（Fragment 切换）。

### Task 4: 验证
- **操作**: 运行应用，测试三个模块的跳转和返回。

## 2. 依赖关系
- Task 1 -> Task 2 -> Task 3 -> Task 4
