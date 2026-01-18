# DESIGN_StudyPage

## 1. 整体架构
- **MainActivity**: 作为应用入口和 Dashboard。
    - 交互：点击 `btn_study` -> 启动 `LearningActivity`。
    - 交互：点击其他按钮 -> 切换内部 Fragment。
- **LearningActivity**: 独立的学习模块容器。
    - 布局：`activity_learning.xml`。
    - 内容：加载 `StudyFragment`。
- **StudyFragment**: 具体的业务逻辑（课程表、成绩单）。

## 2. 模块设计
### AndroidManifest.xml
- 注册 `<activity android:name=".ui.study.LearningActivity" />`。

### MainActivity.java
- **onCreate**:
    - 默认 Fragment: 改为 `new LifeFragment()`。
    - `updateCenterStatus`: 初始状态改为 "生活"。
- **btn_study.onClick**:
    - 移除 `switchFragment(new StudyFragment())`。
    - 添加 `Intent intent = new Intent(MainActivity.this, LearningActivity.class); startActivity(intent);`。
    - 保留按钮动画反馈 `playPressFeedback(v)`。
    - 可能需要处理悬浮按钮的显示/隐藏状态（跳转后返回时状态需保持一致）。

### LearningActivity.java
- 保持现有逻辑：`setContentView` 后加载 `StudyFragment`。
- 确保有返回导航（可选，视 UI 设计而定，当前 XML 无 Toolbar，依赖系统返回键）。

## 3. 接口契约
- 无新增接口，复用现有 Fragment 和 Layout。

## 4. 异常处理
- 确保 `LearningActivity` 启动时 `StudyFragment` 能正确加载数据。
