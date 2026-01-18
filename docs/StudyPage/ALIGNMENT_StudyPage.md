# ALIGNMENT_StudyPage

## 1. 原始需求
用户要求：“学习界面要点主页的学习按钮然后跳转到一个新的页面”。

## 2. 项目现状分析
- **MainActivity**: 当前主页，包含 5 个功能按钮（学习、生活、导航、通知、我的）。
- **当前行为**: 点击“学习”按钮 (`btn_study`) 会在 `MainActivity` 的 `fragment_container` 中切换显示 `StudyFragment`。
- **StudyFragment**: 包含课程表和成绩单的 Fragment。
- **LearningActivity**: 这是一个现有的但未注册的 Activity，其布局 `activity_learning.xml` 包含一个容器，代码中已经实现了加载 `StudyFragment` 的逻辑。

## 3. 需求理解与决策
- **核心目标**: 将“学习”模块从 `MainActivity` 的 Fragment 切换模式改为跳转到一个独立的 Activity (`LearningActivity`)。
- **技术路径**:
    1.  在 `AndroidManifest.xml` 中注册 `LearningActivity`。
    2.  修改 `MainActivity.java`，将 `btn_study` 的点击事件从 `switchFragment` 改为 `startActivity`。
    3.  调整 `MainActivity` 的默认显示 Fragment（建议改为“生活”或保持为空但显示欢迎页，为避免开屏即重复，改为“生活”较合理，或者保留“学习”但作为独立入口）。
        - *决策*: 鉴于用户明确要求“跳转到新页面”，为了体现差异，`MainActivity` 不应再默认显示 `StudyFragment`。考虑到“生活”是另一个高频模块，将默认 Fragment 改为 `LifeFragment`。
- **边界条件**:
    - `LearningActivity` 应该复用现有的 `StudyFragment` 以保留课程表/成绩单功能。
    - 确保 `LearningActivity` 的返回逻辑正常（返回 `MainActivity`）。

## 4. 疑问澄清
- **Q**: `LearningActivity` 是否需要特殊的启动模式？
  - **A**: 默认即可。
- **Q**: `MainActivity` 默认显示什么？
  - **A**: 改为 `LifeFragment` 以避免逻辑冲突。

## 5. 最终共识
- 注册 `LearningActivity`。
- 修改 `MainActivity` 点击“学习”按钮的行为。
- 修改 `MainActivity` 默认 Fragment 为 `LifeFragment`。
