# ACCEPTANCE_LearningModule

## 1. 任务完成情况
- [x] **数据模型**: 创建了 `Course` 和 `Grade` 类，包含必要的字段（颜色、周次、学分等）。
- [x] **Mock 数据**: `MockDataGenerator` 可生成真实的测试数据。
- [x] **课程表 (Schedule)**:
    - 实现了 7x8 网格布局。
    - 支持周次切换（点击标题）。
    - 实现了模拟 OCR 导入流程。
    - UI 采用毛玻璃和莫兰迪色系。
- [x] **成绩单 (Grades)**:
    - 实现了 `SimpleTrendChartView` 绘制 GPA 趋势。
    - 头部展示总 GPA。
    - 列表展示详细成绩。
- [x] **UI 一致性**: 遵循了主页的设计语言。

## 2. 核心文件变更
- **Models**: `Course.java`, `Grade.java`
- **Utils**: `MockDataGenerator.java`
- **Fragments**: `ScheduleFragment.java`, `GradesFragment.java`
- **Layouts**: `fragment_schedule.xml`, `fragment_grades.xml`, `item_grade.xml`
- **Widgets**: `SimpleTrendChartView.java`
- **Resources**: `colors.xml`, `themes.xml` (添加样式)

## 3. 验证步骤
1. 打开应用，点击“学习”按钮。
2. 默认显示“课程表”。点击顶部“第 5 周”可切换周次，观察课程变化。
3. 点击右上角“+”号，选择“导入 PDF/图片”，观察模拟 OCR 过程。
4. 切换到“成绩单” Tab。
5. 查看顶部的 GPA 汇总和趋势图。
6. 滑动查看底部的成绩列表。

## 4. 后续建议
- 对接真实后端 API 替换 Mock 数据。
- 引入真实 OCR SDK (如 TextIn 或 Google ML Kit)。
- 优化 `SimpleTrendChartView`，增加坐标轴文字和触摸交互。
