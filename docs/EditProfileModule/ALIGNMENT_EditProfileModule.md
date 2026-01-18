# ALIGNMENT_EditProfileModule

## 1. 原始需求
- 新增“编辑个人资料”功能 (Edit Profile)。
- 可编辑项：昵称、头像、个性签名、联系方式（可选）。
- UI 要求：与个人主页风格完全一致（毛玻璃、圆角、字体、动画、Header）。
- 技术要求：
  - XML 布局。
  - Kotlin/Java 代码（打开页面、保存到本地存储/Room、实时更新主页）。
  - 提供测试数据。
  - 代码清晰、易维护。

## 2. 项目现状
- **入口**：`MineFragment` 中已有“编辑资料”按钮，但跳转的是只读的 `ProfileFragment`。
- **数据**：`UserProfile` 实体类已存在，包含 name, studentId, department, major, signature, avatarUrl。
- **ViewModel**：`ProfileViewModel` 已存在，但缺少更新数据的方法。
- **布局**：`fragment_profile.xml` 目前是只读展示样式。
- **风格**：主页使用 `bg_glass_card`、`ParticleView`、霓虹配色。

## 3. 需求理解与决策
- **方案选择**：直接改造现有的 `ProfileFragment`，将其升级为“编辑模式”，或者新建一个 `EditProfileFragment`。
  - *决策*：鉴于 `ProfileFragment` 目前功能过于简单且是只读的，直接将其改造为可编辑的 `ProfileEditFragment` 是更优选择，或者在 `ProfileFragment` 中增加编辑/保存状态切换。
  - *修正*：为了保持代码职责单一，保留 `ProfileFragment` 作为只读详情页（如需），新建 `ProfileEditFragment` 专门处理编辑逻辑。但根据用户描述，目前的 `ProfileFragment` 点击进去就是为了编辑，因此直接将 `ProfileFragment` **重构为编辑页**是最符合用户预期的（点击“编辑资料”->进入“编辑页”）。
- **数据存储**：使用 `Room` 数据库 (`UserDao`) 进行持久化。
- **UI 设计**：
  - 复用 `fragment_mine.xml` 的背景和 Header 风格。
  - 将 `TextView` 替换为 `EditText`（带毛玻璃背景输入框）。
  - 底部增加“保存”按钮。

## 4. 最终共识
- 重构 `ProfileFragment` 为真正的编辑页。
- 修改 `fragment_profile.xml` 为编辑表单布局。
- 在 `ProfileViewModel` 中添加 `updateUserProfile` 方法。
- 完善 `UserProfile` 实体类（确认字段完整性）。
