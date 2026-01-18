# TASK_NotificationsModule

## 1. 任务列表

### Task 1: 数据模型与 Mock 数据
- [ ] 创建 `Notification.java`。
- [ ] 创建 `NotificationMockData.java`。

### Task 2: 布局重构
- [ ] 重写 `fragment_notify.xml` (复用 `fragment_study.xml` 结构)。
- [ ] 创建 `item_notification_card.xml` (通用通知列表项，复用卡片样式)。

### Task 3: 适配器与列表实现
- [ ] 创建 `NotificationAdapter.java`。
- [ ] 更新 `CollegeNotifyFragment` 使用新 Adapter。
- [ ] 更新 `ContactListFragment` 使用新 Adapter。
- [ ] 更新 `MessageCenterFragment` 使用新 Adapter。

### Task 4: 主容器逻辑更新
- [ ] 更新 `NotifyFragment.java` 适配新的 Header 和 TabLayout 逻辑。
- [ ] 确保 `ParticleView` 和动画效果生效。

## 2. 依赖关系
- Task 1 -> Task 2 -> Task 3 -> Task 4
