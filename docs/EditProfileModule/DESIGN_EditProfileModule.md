# DESIGN_EditProfileModule

## 1. 整体架构
- **View**: `ProfileFragment` (改造为编辑页)
  - 负责 UI 展示、输入验证、头像点击事件、保存按钮点击事件。
- **ViewModel**: `ProfileViewModel`
  - 负责与 Repository/DAO 交互，更新数据库。
- **Model**: `UserProfile`
  - 实体类，无需修改。

## 2. UI 设计
- **背景**: 复用 `ParticleView` + `bg_mine_gradient`。
- **Header**: 带有“编辑资料”标题的玻璃态 Header，左侧返回箭头。
- **头像区域**:
  - 居中显示大头像。
  - 右下角添加“相机”小图标提示可点击修改。
  - 点击弹出图片选择（模拟）。
- **表单区域**:
  - 使用 `LinearLayout` 垂直排列。
  - 每个输入项包含：Label (TextSecondary) + InputField (Glass Card Background)。
  - 字段：姓名、个性签名、学院（只读）、专业（只读）、手机号（新增）。
- **操作栏**:
  - 底部悬浮或固定“保存修改”按钮 (Neon Gradient)。

## 3. 交互逻辑
1. 进入页面，加载当前用户信息填入输入框。
2. 用户修改内容。
3. 点击“保存”：
   - 验证输入（如姓名不能为空）。
   - 调用 ViewModel 更新数据库。
   - 提示“保存成功”。
   - 返回上一页（`popBackStack`）。
4. `MineFragment` 由于观察了 `LiveData`，会自动更新显示。

## 4. 接口契约
- `ProfileViewModel.updateUser(UserProfile profile)`

## 5. 异常处理
- 输入为空提示。
- 数据库写入失败提示（虽然 Room 主线程操作会报错，需确保在 IO 线程）。
