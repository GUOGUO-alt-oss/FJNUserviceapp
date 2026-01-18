# 首页交互与动效设计（工程可落地）

## 目标气质
- 清晰、克制、高级
- 工程理性、可持续迭代
- Notion 的清晰与 Apple 的高级质感

## 核心动画：五角星按钮出场
- 结构：五个主功能按钮采用五角布局（0°/72°/144°/216°/288°）
- 出场路径：自屏幕底部外沿垂直升起
- 动效维度：位移 + 透明度 + 缩放 + 轻微旋转
- 层次：100ms 递进延迟，形成波浪式层次
- 工程落地：
  - 动效封装：AnimationUtils.createRiseFromBottomAnimation
    - 代码位置：[AnimationUtils.java](file:///f:/android%20ex/ex3/FJNUserviceapp/app/src/main/java/com/example/fjnuserviceapp/utils/AnimationUtils.java)
  - 调度入口：MainActivity.startButtonAnimations
    - 代码位置：[MainActivity.java](file:///f:/android%20ex/ex3/FJNUserviceapp/app/src/main/java/com/example/fjnuserviceapp/MainActivity.java)

## 设计选择：为什么「简单可靠」而非复杂路径
- 可靠性优先：基本属性动画组合，规避复杂曲线的数值误差与设备差异
- 稳定性能：计算量与重绘压力低，适配广泛机型与帧率
- 视觉克制：与 Notion/Apple 的简洁克制一致，避免炫技喧宾夺主
- 维护成本：清晰 API 与松耦合封装，便于持续迭代和参数调优

## 交互反馈与过渡原则
- 点击反馈
  - 即时触感：Haptic KEYBOARD_TAP
  - 视觉反馈：80ms 压低至 0.94，120ms 回弹至 1.0
  - 工程实现：MainActivity.playPressFeedback
- 页面过渡
  - 统一淡入淡出：setCustomAnimations(fade_in, fade_out)
  - 保持上下文稳定，不引入复杂转场导致认知跳跃

## 哪些该动
- 首屏入场的功能入口按钮：以渐进节奏建立层次和关注
- 背景色渐变：超低频率呼吸式变化，营造高级感与氛围
- 按钮点击短促反馈：即时确认与愉悦微动

## 哪些必须不动
- 核心内容区在同级切换时避免位移与大尺度缩放
- 导航与状态元素位置稳定，不随内容变化抖动
- 文本与数据列表不做花哨转场，保持信息可读与操作效率

## 参数建议
- 入场时长：1500ms
- 递进延迟：100ms
- 透明度：0 → 1
- 缩放：0.5 → 1.0
- 轻微旋转：-12° → 0°
- 背景渐变周期：10s，往返循环

## 工程要点
- 动效封装与复用：统一工具类封装，外部仅给定时长与延迟
- 可测试性：编译验证与低耦合结构，便于 A/B 调参
- 渐进增强：旧设备保证基本位移/透明度，支持设备增加旋转与模糊质感

## 验收清单
- 五按钮自底部旋转升起，层次感明确
- 点击反馈与触感即时、节奏克制
- Fragment 过渡统一淡入淡出，内容区稳定
- 背景渐变低频且不影响可读性

## 代码参考
- 动画工具：[AnimationUtils.java](file:///f:/android%20ex/ex3/FJNUserviceapp/app/src/main/java/com/example/fjnuserviceapp/utils/AnimationUtils.java)
- 首页入口：[MainActivity.java](file:///f:/android%20ex/ex3/FJNUserviceapp/app/src/main/java/com/example/fjnuserviceapp/MainActivity.java)

