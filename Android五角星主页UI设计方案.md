# Android 五角星主页 UI 设计方案

## 1. 设计概述

本方案旨在打造一款集 **Notion 的清晰克制**、**Apple 的高级感** 与 **企业级系统的稳定理性** 于一体的校园生活服务 APP 主页。核心采用五角星布局，通过精确的角度映射与层次化的动效，构建独特的导航体验。

## 2. 核心布局 (Core Layout)

### 2.1 整体结构
- **容器**：`ConstraintLayout` 全屏覆盖
- **背景**：动态流体渐变（Fluid Gradient）或低饱和度模糊背景
- **导航结构**：五角星（Pentagram）分布，半径动态适配屏幕宽度（建议 `140dp` - `160dp`）
- **左上角信息区**：日期与天气胶囊（Date & Weather Capsule）

### 2.2 左上角日期/天气 (Date & Weather)
- **位置**：屏幕左上角，`marginTop="48dp"`, `marginStart="24dp"` (避开状态栏和圆角)。
- **样式**：胶囊形 (`Stadium Shape`) 或 圆角矩形 (`CornerRadius="16dp"`)。
- **内容**：
  - **日期**：`MM月dd日` + `周x` (例如 "06月18日 周五")。
  - **天气**：图标 (Icon) + 温度 (例如 "26°C")。
- **视觉**：半透明毛玻璃 (`Glassmorphism`)，白色 60% 透明度背景，轻微阴影。

### 2.3 按钮与角度映射
| 功能 | 角度 (Top=0°) | 图标 (Iconify/Material) | 颜色建议 (Hex) | 语义 |
| :--- | :--- | :--- | :--- | :--- |
| **学习 (Study)** | **0°** (顶部) | `ic_school` / `ic_book` | `#4A90E2` (学识蓝) | 核心学业数据 |
| **生活 (Life)** | **72°** (右中) | `ic_restaurant` / `ic_local_cafe` | `#F5A623` (活力橙) | 食堂、服务 |
| **导航 (Nav)** | **144°** (右下) | `ic_map` / `ic_navigation` | `#2ECC71` (探索绿) | 地图、路线 |
| **通知 (Notify)** | **216°** (左下) | `ic_notifications` / `ic_bell` | `#E74C3C` (提醒红) | 消息、公告 |
| **我的 (Mine)** | **288°** (左中) | `ic_person` / `ic_account_circle` | `#9B59B6` (个性紫) | 个人中心 |

### 2.3 中心状态卡片 (Center Hub)
- **位置**：屏幕正中央，五角星几何中心。
- **尺寸**：圆形或圆角矩形，直径/边长约 `120dp`，确保不与周围按钮重叠。
- **内容**：
  - **默认态**：显示今日日期、天气或“福师大”Logo。
  - **交互态**：点击周围按钮时，中心卡片显示对应模块的摘要（如：点击“学习” -> 显示“下节课：高等数学”）。
- **组件**：`CardView` + `ConstraintLayout`，高斯模糊背景 (`glass-morphism`)。

## 3. 动效与交互 (Motion & Interaction)

### 3.1 首页加载 (Entrance)
1.  **骨架屏 (Shimmer)**：(可选) 按钮位置显示灰色占位符闪烁。
2.  **左上角信息区**：
    -   **路径**：自左侧滑入 (TranslationX -50dp -> 0)。
    -   **属性**：透明度 (0% -> 100%)。
    -   **时序**：最先启动 (Delay 0ms)。
3.  **出场动画**：
    -   **路径**：自屏幕底部垂直升起至五角星顶点位置。
    -   **属性**：位移 (TranslationY) + 透明度 (0% -> 100%) + 缩放 (50% -> 100%)。
    -   **时序**：按顺时针或优先级顺序依次延迟启动 (Delay 100ms)，形成波浪感。

### 3.2 按钮点击 (Tap Feedback)
-   **触感**：`HapticFeedbackConstants.KEYBOARD_TAP`
-   **视觉**：
    -   **按下**：轻微放大 (Scale 1.0 -> 1.1) 或 缩小 (1.0 -> 0.95) 以示确认。*建议采用 1.0 -> 1.1 -> 1.0 的弹性脉冲效果，增强活跃感。*
    -   **波纹**：`RippleEffect` (白色或主色半透明)。
-   **联动**：点击按钮时，中心卡片内容进行 **淡入淡出 (Crossfade)** 切换。

### 3.3 背景氛围 (Ambience)
-   使用 `ObjectAnimator` 对背景 View 的 `backgroundColor` 进行无限循环的 `ArgbEvaluator` 渐变。
-   建议色值：低饱和度的蓝灰、淡紫、浅青，避免高饱和度导致视觉疲劳。

### 3.4 持续微动效 (Continuous Micro-interactions)
-   **中心呼吸**：中心卡片执行 `Alpha 0.8 <-> 1.0` + `Scale 0.98 <-> 1.02` 的循环呼吸动画 (Duration 2000ms)，模拟生命感。
-   **天气浮动**：左上角天气图标执行 `TranslationY 0 -> -10 -> 0` 的上下漂浮动画 (Duration 3000ms)，增加灵动感。

### 3.5 交互升级 (Interaction Upgrade)
-   **长按菜单**：长按五角星按钮触发 `HapticFeedbackConstants.LONG_PRESS`，并在中心卡片显示该模块的快捷子功能（如“查看课表”）。

## 4. 配色方案 (Color Palette)

-   **背景色 (Background)**：
    -   Start: `#F5F7FA` (冷灰白)
    -   Mid: `#E4E7EB` (金属白)
    -   End: `#D0D5DB` (深灰白)
    -   *暗黑模式下自动适配为深空灰系列。*
-   **主色 (Primary)**：`#007AFF` (Apple Blue) - 用于强调和选中态。
-   **文字 (Text)**：`#1D1D1F` (Notion Black) - 清晰易读。
-   **卡片背景**：`#FFFFFF` (90% Opacity) - 磨砂玻璃质感。

## 5. 组件选择 (Component Selection)

-   **按钮**：`com.google.android.material.button.MaterialButton`
    -   Style: `Widget.MaterialComponents.Button.TextButton` (配合自定义背景实现圆形)
    -   Icon Gravity: `textStart` (Centered)
-   **布局**：`androidx.constraintlayout.widget.ConstraintLayout`
    -   利用 `layout_constraintCircle` 属性实现精确的角度布局。
-   **动画**：原生 `android.animation` 包 (`ObjectAnimator`, `AnimatorSet`)，保证零依赖高性能。

## 6. 开发实施计划

1.  **布局更新**：
    -   在 `activity_main.xml` 中增加中心卡片 `CardView`。
    -   **新增**：在 `activity_main.xml` 中增加左上角日期/天气 `CardView`。
2.  **样式定制**：定义卡片的圆角背景和阴影。
3.  **交互逻辑**：
    -   在 `MainActivity.java` 中监听按钮点击，触发中心卡片内容更新动画。
    -   **新增**：在 `MainActivity.java` 中获取当前日期并显示。
4.  **动画微调**：
    -   更新 `AnimationUtils` 支持 1.0 -> 1.1 -> 1.0 的弹性缩放。
    -   **新增**：实现左上角卡片的滑入动画。
