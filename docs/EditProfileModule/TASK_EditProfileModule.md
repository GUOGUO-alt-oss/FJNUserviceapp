# TASK_EditProfileModule

## 1. 任务列表

### Task 1: 基础设施更新
- [ ] 在 `UserProfile` 中添加 `phone` 字段（如果需要）。
- [ ] 在 `ProfileViewModel` 中实现 `updateUser` 方法。
- [ ] 创建输入框样式的 Drawable (`bg_glass_input.xml`)。

### Task 2: 布局重构
- [ ] 重写 `fragment_profile.xml`：
  - 添加粒子背景。
  - 设计头像编辑区。
  - 设计表单输入区。
  - 添加保存按钮。

### Task 3: 逻辑实现
- [ ] 更新 `ProfileFragment.java`：
  - 绑定 UI 控件。
  - 实现头像点击（模拟）。
  - 实现保存逻辑。
  - 处理返回导航。

### Task 4: 验证
- [ ] 运行应用，进入“编辑资料”。
- [ ] 修改信息并保存。
- [ ] 验证主页是否实时更新。

## 2. 依赖关系
- Task 1 -> Task 2 -> Task 3 -> Task 4
