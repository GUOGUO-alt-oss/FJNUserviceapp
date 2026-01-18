# DESIGN_UI_Optimization

## 1. 布局重构 (Layout)
- **ConstraintLayout**: 继续使用。
- **Center**:
  - `MaterialButton` ID: `btn_life` (原 `card_center_status` / `iv_center_logo` 位置)。
  - Size: 130dp x 130dp.
  - Style: Gradient Background, Elevation 16dp.
- **Surrounding (Ring)**:
  - `btn_study` (Top-Left), `btn_nav` (Top-Right), `btn_notify` (Bottom-Right), `btn_mine` (Bottom-Left).
  - Size: 85dp x 85dp.
  - Radius: 140dp (增加半径以避免拥挤).
  - Style: Glassmorphism, Lower Saturation.

## 2. 视觉样式 (Visuals)
- **Colors**:
  - Main Button (Life): Orange/Gold Gradient (`#FFD700` -> `#FF8C00`).
  - Sub Buttons: Semi-transparent white (`#1AFFFFFF`) with colored icons.
- **Header**:
  - Welcome Text: 22sp, Bold, White.
  - Notification Card: Rounded rect background (`#33000000`), Icon + Text.

## 3. 资源文件
- `drawable/bg_button_main_gradient.xml`: 主按钮背景。
- `drawable/bg_button_sub_glass.xml`: 辅助按钮背景。
- `drawable/bg_header_notification.xml`: 顶部通知栏背景。

## 4. 交互逻辑
- **MainActivity.java**:
  - 更新按钮引用（部分按钮位置改变）。
  - `initGlassButtons()` 需适配不同类型的按钮样式。
  - `startButtonAnimations()` 需调整中心按钮的动画（可能是缩放淡入，而非底部升起）。

## 5. 兼容性
- 确保 `ConstraintLayout` 的 `layout_constraintCircle` 属性在各版本正常工作。
