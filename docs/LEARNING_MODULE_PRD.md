# 学习模块 (Learning Module) 产品需求文档 (PRD)

## 1. 产品定位与目标 (Product Positioning)
-   **定位**：高校智能学习系统 (Smart Learning System)
-   **风格**：Notion (清晰克制) + Apple School (高级感) + Enterprise (工程理性)
-   **核心价值**：为大学生提供专业、高效、高颜值的学业管理工具，支持简历展示与项目实战。

## 2. 信息架构 (Information Architecture)
-   **入口**：首页 -> 学习 (Study) 按钮 -> 独立学习模块页
-   **层级**：
    -   **Top Header**: 学期/周次/日期状态栏
    -   **Body**: 核心功能区（课表/成绩）
    -   **Bottom Nav**: 视图切换（课表 Schedule | 成绩 Grades）

## 3. 核心功能模块 (Core Modules)

### 3.1 智能课表 (Smart Schedule)
-   **数据模型**: 7x12 网格 (周一至周日, 1-12节)
-   **UI 布局**:
    -   左侧固定节次轴 (Time Axis)
    -   顶部固定周次轴 (Day Axis)
    -   支持横向/纵向滚动
    -   **当前周** 高亮显示
-   **课程卡片 (Course Card)**:
    -   圆角 (12dp), 毛玻璃/半透明背景
    -   莫兰迪色系区分课程
    -   显示: 课程名 (Bold), 教室 & 教师 (Small)
    -   交互: 点击查看详情, 长按编辑
-   **AI 导入流程**: PDF/图片 -> OCR -> NLP 解析 -> 确认 -> 生成

### 3.2 成绩管理 (Grades Management)
-   **功能**: 学期切换, GPA 展示, 成绩列表, 趋势分析
-   **可视化**: 折线图展示 GPA 变化趋势 (MPAndroidChart)
-   **预警**: 挂科风险提示 (Visual Alert)

### 3.3 个人中心 (Personal Center)
-   **信息编辑**: 头像, 昵称, 学号
-   **我的收藏**: 常用课程, 重要通知

## 4. UI/UX 规范 (Guidelines)
-   **色彩**:
    -   Background: 深空星尘 (Deep Space Stardust) - `#0F2027` -> `#203A43` -> `#2C5364`
    -   Accents: 霓虹青 (`#00F2FE`), 霓虹紫 (`#4FACFE`)
    -   Text: White / White Alpha
-   **字体**: 系统默认无衬线字体, 标题加粗
-   **动效**: 页面切换淡入淡出, 列表加载瀑布流, 按钮点击脉冲

## 5. 技术栈 (Tech Stack)
-   **Language**: Java
-   **Architecture**: MVVM (ViewModel + LiveData)
-   **Database**: Room (SQLite)
-   **Network**: Retrofit (预留)
-   **Charts**: MPAndroidChart
-   **UI Components**: Material Components, ConstraintLayout, CardView
