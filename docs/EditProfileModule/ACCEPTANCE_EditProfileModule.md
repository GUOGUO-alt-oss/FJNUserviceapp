# ACCEPTANCE_EditProfileModule

## 1. 任务完成情况
- [x] **数据模型**: 更新 `UserProfile` 添加了 `phone` 字段。
- [x] **ViewModel**: 在 `ProfileViewModel` 中实现了 `updateUser` 方法，支持 Room 数据库更新。
- [x] **UI 设计**: 
  - 重写了 `fragment_profile.xml`，将其从只读详情页转变为编辑表单页。
  - 保持了与主页一致的“粒子背景 + 毛玻璃卡片 + 霓虹配色”风格。
  - 创建了 `bg_glass_input.xml` 用于输入框样式。
- [x] **交互逻辑**:
  - `ProfileFragment` 现在支持读取当前用户数据填充表单。
  - 实现了“保存”按钮逻辑，验证输入并更新数据库。
  - 模拟了头像点击事件。

## 2. 核心文件变更
- **Model**: `UserProfile.java`
- **ViewModel**: `ProfileViewModel.java`
- **Layout**: `fragment_profile.xml`
- **Fragment**: `ProfileFragment.java`
- **Drawable**: `bg_glass_input.xml`

## 3. 验证步骤
1. 打开应用，进入“我的”页面。
2. 点击“编辑资料”按钮。
3. 验证页面是否显示当前的姓名、签名等信息，且不可编辑项（学号、专业）是否置灰。
4. 修改姓名和签名，点击“保存”。
5. 验证是否提示“保存成功”并返回主页。
6. 验证主页上的姓名和签名是否已实时更新。

## 4. 后续优化
- 实现真实的图片选择（调用系统相册）并保存路径。
- 增加更详细的输入验证规则（如手机号格式）。
