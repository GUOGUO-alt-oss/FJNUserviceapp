# TASK_UI_Optimization

## 1. 任务列表

### Task 1: 资源准备
- [ ] 创建 `bg_button_main_gradient.xml` (生活按钮)。
- [ ] 创建 `bg_button_sub_glass.xml` (辅助按钮)。
- [ ] 创建 `bg_header_notification.xml` (通知卡片)。
- [ ] 更新 `colors.xml` 添加所需渐变色。

### Task 2: 布局重构 (activity_main.xml)
- [ ] 移除旧的 `card_center_status` 和 `iv_center_logo`。
- [ ] 将 `btn_life` 移至中心，调整尺寸为 130dp。
- [ ] 重新排列其余 4 个按钮 (`btn_study`, `btn_nav`, `btn_notify`, `btn_mine`) 环绕中心。
- [ ] 优化顶部 Header 区域，应用新的通知卡片样式。

### Task 3: 代码适配 (MainActivity.java)
- [ ] 更新按钮初始化逻辑。
- [ ] 调整入场动画：中心按钮改为 Scale/Alpha 动画，四周按钮保持升起或改为飞入。
- [ ] 移除旧的 `updateCenterStatus` 逻辑。

### Task 4: 验证
- [ ] 检查层级关系（主按钮最上层）。
- [ ] 检查点击交互和波纹效果。
- [ ] 检查不同屏幕适配。

## 2. 依赖关系
- Task 1 -> Task 2 -> Task 3 -> Task 4
