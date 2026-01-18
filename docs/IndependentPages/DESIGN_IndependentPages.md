# DESIGN_IndependentPages

## 1. 整体架构
- **MainActivity**: 作为 Dashboard，点击按钮跳转到独立模块。
- **NotifyActivity**: 通知模块容器。
- **MineActivity**: 个人中心模块容器。

## 2. 模块设计

### NotifyActivity
- **Class**: `com.example.fjnuserviceapp.ui.notify.NotifyActivity`
- **Layout**: `activity_notify.xml` (FrameLayout 容器)
- **Logic**: 加载 `NotifyFragment`。

### MineActivity
- **Class**: `com.example.fjnuserviceapp.ui.mine.MineActivity` (已存在)
- **Layout**: `activity_mine.xml` (已存在)
- **Logic**: 加载 `MineFragment` (已存在)。

### AndroidManifest.xml
- 注册 `<activity android:name=".ui.notify.NotifyActivity" ... />`
- 注册 `<activity android:name=".ui.mine.MineActivity" ... />`

### MainActivity
- 修改 `btnNotify` 和 `btnMine` 的 `OnClickListener`。
- 移除相关的 Fragment 切换逻辑。
- `hideAllFloatButtons` 逻辑可能不再需要，因为已经跳出页面了。

## 3. 接口契约
- 复用现有 Fragment，无数据接口变更。

## 4. 异常处理
- 确保 Activity 启动时 Fragment 正确初始化。
