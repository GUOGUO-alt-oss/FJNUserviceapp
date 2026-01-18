# DESIGN_LearningModule

## 1. 整体架构
- **Data Layer**:
    - `Course`: 实体类 (id, name, teacher, room, day, start, end, color).
    - `Grade`: 实体类 (term, courseName, score, credit, gpa).
    - `MockDataGenerator`: 生成静态 JSON 数据并解析为对象列表。
- **UI Layer**:
    - `ScheduleFragment`:
        - 顶部: 周次选择器 (Spinner 或 HorizontalScrollView)。
        - 中部: `ScrollView` + `RelativeLayout` (作为网格容器)。
        - 底部/悬浮: 导入按钮。
    - `GradesFragment`:
        - 头部: GPA 总览卡片 + `SimpleTrendChartView`。
        - 列表: `RecyclerView` 展示详细成绩。
    - `SimpleTrendChartView`: 自定义 View，绘制简单的折线图。

## 2. 核心组件设计
### 课程表 (Schedule)
- **布局**: 使用 `RelativeLayout` 作为容器，根据 (day, startNode) 计算坐标 (leftMargin, topMargin) 和高度。
- **卡片样式**: 圆角矩形，背景色使用莫兰迪色系 (半透明)，白色文字。
- **周次切换**: 简单的左右箭头或下拉菜单，过滤显示当前周课程。

### 成绩单 (Grades)
- **布局**: `NestedScrollView` 包含 Header 和 `RecyclerView`。
- **图表**: 自定义 View，输入 `List<Float>` (GPA 数据)，绘制折线和点。

### OCR 导入流程 (模拟)
1. 点击“导入”。
2. 弹出 Dialog 模拟“选择图片/PDF”。
3. 显示 Loading 动画 ("AI 识别中...")。
4. 解析 Mock 数据并刷新界面。

## 3. UI 规范
- **Colors**:
    - Morandi Blue: #5B7C99
    - Morandi Pink: #D8B4B8
    - Morandi Green: #8FA395
    - Glass BG: #1AFFFFFF
- **Typography**: 遵循现有 styles。

## 4. 接口契约
- `Course` 数据结构需支持跨周设置 (startWeek, endWeek)。
- `Grade` 数据需包含学期信息以便分组。
