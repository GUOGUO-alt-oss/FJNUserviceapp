# DESIGN_NotificationsModule

## 1. 整体架构
- **UI Structure**:
    - **Container**: `NotifyFragment` (复用 `StudyFragment` 结构)。
    - **Background**: `ParticleView` (全屏粒子背景)。
    - **Header**: 顶部 Glassmorphism Header (标题 + 状态)。
    - **Navigation**: 底部 `TabLayout` (学院通知, 私信, 消息中心)。
    - **Content**: `ViewPager2` 承载三个子 Fragment。

## 2. 子模块设计
- **Sub-Fragments**:
    - `CollegeNotifyFragment`: 学院通知列表。
    - `ContactListFragment`: 私信/联系人列表。
    - `MessageCenterFragment`: 系统消息列表。
- **List Item Design**:
    - 使用 `CardView` + `bg_glass_card` 背景。
    - 标题 (Bold White), 内容 (Secondary), 时间 (Neon/Secondary)。
    - 点击跳转详情。

## 3. 数据模型
- `Notification` 类:
    - id, title, content, time, type (system, college, chat), sender, isRead.
- `NotificationMockData`: 生成各类测试数据。

## 4. 组件复用
- **Layout**: `fragment_notify.xml` <- `fragment_study.xml`
- **Background**: `ParticleView`
- **Colors**: `neon_cyan`, `neon_purple`, `text_secondary`, `glass_bg`.
- **Drawable**: `bg_glass_card.xml`.

## 5. 接口契约
- 保持 `NotifyFragment` 对外的接口不变，仅修改内部 UI 实现。
