# ALIGNMENT_NotificationsModule

## 1. 原始需求
- 重构通知模块 (Notifications Module)，使其 UI/UX 与学习模块 (Learning Module) **完全一致**。
- 复用学习模块的所有组件：Header, Cards, Colors, Fonts, Spacing, Animations, Bottom Navigation。
- 功能独立：展示通知列表。
- 包含 Mock JSON 数据。
- 提供完整的 XML 和 Java 代码。
- 确保与现有导航无缝集成。

## 2. 项目现状
- **Learning Module**: 
    - `StudyFragment` 作为容器，包含 Header, ViewPager2, TabLayout。
    - `ScheduleFragment` 和 `GradesFragment` 作为子页面。
    - UI 风格：Glassmorphism, Neon Colors, Particle Background.
- **Notifications Module (现有)**:
    - `NotifyFragment` 目前使用 `TabLayout` + `ViewPager2`，包含 `CollegeNotifyFragment`, `ContactListFragment`, `MessageCenterFragment`。
    - 需要确认现有布局是否符合新的 UI 规范。

## 3. 需求理解与决策
- **目标**: 将 `NotifyFragment` 的布局重构为与 `fragment_study.xml` 完全一致的结构。
- **Header**: 添加与 Study 模块相同的顶部 Header，显示 "Notification Center" 和当前状态。
- **Container**: 保留 `ViewPager2` 用于切换不同类型的通知。
- **Tabs**: 底部 `TabLayout` 样式需与 Study 模块一致 (底部导航风格)。
- **List Items**: 通知列表项需采用圆角毛玻璃卡片样式 (`bg_glass_card`)。
- **Data**: 创建 `Notification` 实体类和 Mock 数据。

## 4. 最终共识
- 重构 `fragment_notify.xml` 复用 `fragment_study.xml` 的结构。
- 创建 `NotificationAdapter` 使用统一的卡片样式。
- 更新 `NotifyFragment.java` 适配新布局。
- 创建 `NotificationMockData`。
