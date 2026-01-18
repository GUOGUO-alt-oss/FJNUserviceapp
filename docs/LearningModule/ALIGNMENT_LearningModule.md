# ALIGNMENT_LearningModule

## 1. 原始需求
- 实现学习模块，包含 **课程表 (Schedule)** 和 **成绩单 (Grades)**。
- **课程表**:
    - 7x8 网格布局 (周一至周日, 1-8 节)。
    - 支持周次切换。
    - 支持 PDF/图片导入 (模拟 OCR + AI)。
    - UI 需与主页一致 (Glassmorphism, Morandi Colors)。
    - 课程卡片交互: 点击详情, 长按编辑。
- **成绩单**:
    - 显示 GPA, 趋势图 (折线图)。
    - 列表展示各学期成绩。
- **要求**: 提供完整布局 XML, Java 代码, Mock JSON 数据, OCR 伪代码。

## 2. 项目现状
- 现有 `ScheduleFragment` 使用简单的 RelativeLayout 定位。
- 现有 `GradesFragment` 为空壳。
- 缺乏 MPAndroidChart 库，需手动实现简单图表或仅展示列表。
- 已有 Gson 库。

## 3. 需求理解与决策
- **UI 风格**: 严格遵循主页的“霓虹+毛玻璃”风格，但课程卡片采用“低饱和度莫兰迪色”以区分。
- **图表实现**: 由于没有图表库，将实现一个轻量级的 `SimpleTrendChartView` (自定义 View) 来绘制 GPA 折线图。
- **OCR 模拟**: 在菜单栏添加“导入课表”按钮，点击后模拟文件选择和加载过程。
- **数据源**: 使用本地 Mock JSON 数据。

## 4. 最终共识
- 重构 `ScheduleFragment` 及其布局。
- 实现 `GradesFragment` 及其布局。
- 创建 `Course` 和 `Grade` 数据模型。
- 实现自定义图表 View。
- 编写 Mock 数据生成器。
