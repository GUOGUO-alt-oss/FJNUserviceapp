# TASK_LearningModule

## 1. 任务列表

### Task 1: 基础资源与数据模型
- [ ] 更新 `colors.xml` 添加莫兰迪色系。
- [ ] 创建 `Course.java`, `Grade.java`。
- [ ] 创建 `MockDataGenerator.java` (含 JSON 数据)。

### Task 2: 课程表实现 (Schedule)
- [ ] 修改 `fragment_schedule.xml` (完善布局结构)。
- [ ] 更新 `ScheduleFragment.java`:
    - 实现动态添加课程卡片逻辑。
    - 实现周次切换逻辑。
    - 实现模拟 OCR 导入流程。

### Task 3: 成绩单实现 (Grades)
- [ ] 创建 `SimpleTrendChartView.java` (自定义折线图)。
- [ ] 创建 `item_grade.xml` (成绩列表项)。
- [ ] 修改 `fragment_grades.xml`。
- [ ] 实现 `GradesFragment.java` 和 `GradeAdapter.java`。

### Task 4: 整合与验证
- [ ] 确保 `StudyFragment` (LearningActivity) 正确加载这两个 Fragment。
- [ ] 运行测试。

## 2. 依赖关系
- Task 1 -> Task 2 & Task 3 -> Task 4
