# 福师大校园生活服务 APP 登录注册功能实现 - AI 实现 Prompt

## 📋 文档信息

- **项目名称**：福师大校园生活服务 APP
- **功能模块**：登录注册功能
- **目标AI**：Gemini AI
- **实现范围**：Android 客户端（不含后端）
- **项目路径**：`c:\Users\33525\Desktop\FJNUserviceapp`
- **生成时间**：2026-01-20

---

## 🎯 任务概述

你是一位资深的 Android 开发工程师，拥有 10 年以上的移动应用开发经验，精通 Java 和 Kotlin，擅长 MVVM 架构设计、UI/UX 优化、性能调优和安全防护。现在你需要为福师大校园生活服务 APP 实现完整的登录注册功能。

### 项目背景

福师大校园生活服务 APP 是一款专为福建师范大学学生打造的校园生活服务应用，现有功能包括学习模块（待办清单、智能课表、成绩单、倒计时、正念冥想）、生活模块（失物招领、闲置交易）、导航模块（校园地图、地点搜索）、通知模块和个人中心。现有技术栈为 Java 11、MVVM 架构、Room Database、Retrofit + Gson、Glide、高德地图 SDK，UI 采用 Glassmorphism 设计和 3D 旋转五角星菜单。

当前项目缺少用户认证功能（登录、注册、Token 管理等），你需要基于以下提供的详细规范，实现完整的登录注册功能。

### 实现目标

你需要实现以下功能：
1. **用户注册**：手机号验证码注册（暂不实现第三方注册）
2. **用户登录**：手机号+密码登录、验证码登录
3. **找回密码**：通过手机号验证码重置密码
4. **密码管理**：修改密码、密码强度检测
5. **Token 管理**：Token 存储、刷新、过期处理
6. **自动登录**：应用启动时自动登录
7. **记住密码**：保存和填充登录凭证

### 技术要求

开发语言为 Java 11，项目使用 MVVM 架构（ViewModel + LiveData），使用 ViewBinding 进行视图绑定，使用 Room Database 进行本地数据存储，使用 Retrofit 2.9.0 + OkHttp 进行网络请求，使用 Gson 进行 JSON 解析，使用 EncryptedSharedPreferences 存储敏感数据，使用 AndroidX Lifecycle 组件管理生命周期。

---

## 📁 项目结构要求

### 需要创建的目录结构

在 `app/src/main/java/com/example/fjnuserviceapp/` 目录下创建以下结构：

```
auth/                              # 登录注册模块
├── data/
│   ├── model/                     # 数据模型
│   │   ├── User.java              # 用户实体类
│   │   ├── UserToken.java         # Token实体类
│   │   ├── VerificationCode.java  # 验证码实体类
│   │   ├── AuthRequest.java       # 请求基类
│   │   ├── LoginRequest.java      # 登录请求
│   │   ├── RegisterRequest.java   # 注册请求
│   │   ├── SendCodeRequest.java   # 发送验证码请求
│   │   ├── ResetPwdRequest.java   # 重置密码请求
│   │   ├── ChangePwdRequest.java  # 修改密码请求
│   │   ├── AuthResponse.java      # 认证响应
│   │   └── ApiResponse.java       # API统一响应
│   │
│   └── local/                     # 本地数据存储
│       ├── AuthDao.java           # 用户数据访问接口
│       ├── UserTokenDao.java      # Token数据访问接口
│       └── AuthDatabase.java      # 认证数据库
│
├── repository/                    # 数据仓库
│   ├── AuthRepository.java        # 认证数据仓库
│   └── UserRepository.java        # 用户数据仓库
│
├── network/                       # 网络相关
│   ├── AuthApi.java               # Retrofit API接口
│   ├── AuthInterceptor.java       # Token拦截器
│   ├── NetworkClient.java         # 网络客户端配置
│   └── AuthObserver.java          # 统一观察者
│
├── manager/                       # 管理器
│   ├── TokenManager.java          # Token管理器
│   ├── PreferenceManager.java     # 偏好设置管理器
│   └── SessionManager.java        # 会话管理器
│
├── viewmodel/                     # 视图模型
│   ├── AuthViewModel.java         # 认证视图模型（主）
│   ├── LoginViewModel.java        # 登录视图模型
│   ├── RegisterViewModel.java     # 注册视图模型
│   └── FindPwdViewModel.java      # 找回密码视图模型
│
└── ui/                            # UI层
    ├── AuthActivity.java          # 认证页面容器
    ├── WelcomeFragment.java       # 欢迎页面
    ├── LoginFragment.java         # 登录页面
    ├── RegisterFragment.java      # 注册页面
    ├── FindPwdFragment.java       # 找回密码页面
    └── adapter/                   # 适配器
        └── InputValidator.java    # 输入验证器
```

在 `app/src/main/res/` 目录下创建以下结构：

```
layout/
├── activity_auth.xml              # AuthActivity布局
├── fragment_welcome.xml           # 欢迎页面布局
├── fragment_login.xml             # 登录页面布局
├── fragment_register.xml          # 注册页面布局
├── fragment_find_pwd.xml          # 找回密码页面布局
└── item_password_strength.xml     # 密码强度指示器
```

### 需要修改的现有文件

你需要修改以下现有文件，在适当位置添加认证相关代码：

1. `app/src/main/java/com/example/fjnuserviceapp/db/AppDatabase.java` - 添加 UserDao 和 UserTokenDao
2. `app/src/main/java/com/example/fjnuserviceapp/MainActivity.java` - 添加登录状态检查
3. `app/src/main/java/com/example/fjnuserviceapp/api/ApiService.java` - 添加认证相关接口（如果需要）

---

## 🔧 实现步骤

### 步骤 1：数据模型层实现

#### 1.1 创建用户实体类（User.java）

文件路径：`app/src/main/java/com/example/fjnuserviceapp/auth/data/model/User.java`

创建 User 实体类，包含以下字段：
- `id` (long)：用户ID，主键自增
- `phone` (String)：手机号，唯一索引
- `email` (String)：邮箱，唯一索引
- `studentId` (String)：学号，唯一索引
- `passwordHash` (String)：加密后的密码
- `salt` (String)：加密盐值
- `nickname` (String)：昵称
- `realName` (String)：真实姓名
- `avatarUrl` (String)：头像URL
- `department` (String)：学院
- `major` (String)：专业
- `gender` (int)：性别，0未知1男2女
- `status` (int)：状态，0禁用1正常2待审核
- `createdAt` (long)：创建时间戳
- `updatedAt` (long)：更新时间戳
- `lastLoginAt` (long)：最后登录时间戳
- `loginCount` (int)：登录次数

需要包含：
- 无参构造方法
- 带参构造方法（name、studentId、department、major）
- 所有字段的 getter 和 setter 方法
- Room 注解（@Entity、@PrimaryKey、@ColumnInfo）

#### 1.2 创建 Token 实体类（UserToken.java）

文件路径：`app/src/main/java/com/example/fjnuserviceapp/auth/data/model/UserToken.java`

创建 UserToken 实体类，包含以下字段：
- `id` (long)：ID，主键自增
- `userId` (long)：用户ID，外键关联 users 表
- `token` (String)：访问令牌
- `refreshToken` (String)：刷新令牌
- `deviceType` (String)：设备类型
- `deviceToken` (String)：设备令牌
- `expiresAt` (long)：过期时间戳
- `createdAt` (long)：创建时间戳

需要包含：
- Room 注解（@Entity、@ForeignKey、@Index）
- 所有字段的 getter 和 setter 方法

#### 1.3 创建请求和响应类

创建以下请求类：

**LoginRequest.java**：
- `account` (String)：账号（手机号/学号/邮箱）
- `password` (String)：密码
- `deviceType` (String)：设备类型
- `deviceToken` (String)：设备令牌

**RegisterRequest.java**：
- `phone` (String)：手机号
- `code` (String)：验证码
- `password` (String)：密码
- `studentId` (String)：学号（可选）
- `email` (String)：邮箱（可选）
- `nickname` (String)：昵称

**SendCodeRequest.java**：
- `phone` (String)：手机号
- `type` (int)：验证码类型，1注册2登录3找回密码

**ResetPwdRequest.java**：
- `phone` (String)：手机号
- `code` (String)：验证码
- `newPassword` (String)：新密码

**ChangePwdRequest.java**：
- `oldPassword` (String)：旧密码
- `newPassword` (String)：新密码

创建以下响应类：

**AuthResponse.java**：
- `user` (User)：用户信息
- `token` (String)：访问令牌
- `refreshToken` (String)：刷新令牌
- `expiresIn` (long)：有效期（秒）

**ApiResponse.java**：
- `code` (int)：状态码
- `message` (String)：消息
- `data` (T)：数据

### 步骤 2：数据库层实现

#### 2.1 创建 DAO 接口

**UserDao.java**：
- `User getUserByPhone(String phone)`：根据手机号查询
- `User getUserByStudentId(String studentId)`：根据学号查询
- `User getUserByEmail(String email)`：根据邮箱查询
- `User getUserById(long userId)`：根据ID查询
- `User getUserByAccount(String account)`：根据账号查询（手机号/学号/邮箱）
- `void insert(User user)`：插入用户
- `void update(User user)`：更新用户
- `void updateLoginInfo(long userId, long loginAt)`：更新登录信息
- `void updateStatus(long userId, int status)`：更新状态
- `void deleteById(long userId)`：删除用户

**UserTokenDao.java**：
- `UserToken getTokenByToken(String token)`：根据token查询
- `List<UserToken> getTokensByUserId(long userId)`：根据用户ID查询
- `UserToken getTokenByUserAndDevice(long userId, String deviceType)`：根据用户和设备查询
- `void insert(UserToken token)`：插入token
- `void update(UserToken token)`：更新token
- `void deleteByToken(String token)`：根据token删除
- `void deleteByUserId(long userId)`：根据用户ID删除
- `void deleteExpiredTokens(long currentTime)`：删除过期token

#### 2.2 创建认证数据库

**AuthDatabase.java**：
- 继承 RoomDatabase
- 声明 UserDao 和 UserTokenDao 的抽象方法
- 实现单例模式获取数据库实例
- 数据库版本为 8

在 AppDatabase.java 中添加认证相关的 DAO：
- `public abstract UserDao userDao()`
- `public abstract UserTokenDao userTokenDao()`

### 步骤 3：网络层实现

#### 3.1 创建 Retrofit API 接口

**AuthApi.java**：
- `POST("auth/login")` - 用户登录
- `POST("auth/register")` - 用户注册
- `POST("auth/send-code")` - 发送验证码
- `POST("auth/verify-code")` - 验证验证码
- `POST("auth/refresh-token")` - 刷新Token
- `POST("auth/reset-password")` - 重置密码
- `POST("auth/change-password")` - 修改密码
- `GET("auth/profile")` - 获取用户信息
- `POST("auth/logout")` - 退出登录

所有需要认证的接口添加 `@Header("Authorization") String token` 参数。

#### 3.2 创建 Token 拦截器

**AuthInterceptor.java**：
- 实现 Interceptor 接口
- 从 TokenManager 获取 token
- 添加 Authorization 请求头
- 处理 401 响应（Token过期）
- 触发 Token 刷新或跳转登录

#### 3.3 创建网络客户端

**NetworkClient.java**：
- 配置 OkHttpClient（超时时间、拦截器、日志等）
- 配置 Retrofit（baseURL、converter、client）
- 提供单例获取方法

### 步骤 4：管理器实现

#### 4.1 TokenManager

**TokenManager.java**：
- 使用 EncryptedSharedPreferences 存储
- `saveToken(String token)`：保存 access token
- `getToken()`：获取 access token
- `saveRefreshToken(String refreshToken)`：保存 refresh token
- `getRefreshToken()`：获取 refresh token
- `saveTokenExpiresAt(long expiresIn)`：保存过期时间
- `getTokenExpiresAt()`：获取过期时间
- `hasValidToken()`：检查 token 是否有效
- `isTokenExpiringSoon()`：检查 token 是否即将过期（5分钟内）
- `clearTokens()`：清除所有 token

#### 4.2 PreferenceManager

**PreferenceManager.java**：
- `saveRememberPassword(boolean remember)`：保存记住密码设置
- `isRememberPassword()`：是否记住密码
- `saveAutoLogin(boolean autoLogin)`：保存自动登录设置
- `isAutoLogin()`：是否自动登录
- `saveRememberedAccount(String account)`：保存记住的账号
- `getRememberedAccount()`：获取记住的账号
- `saveRememberedPassword(String password)`：保存记住的密码（加密）
- `getRememberedPassword()`：获取记住的密码
- `clearAuthState()`：清除认证状态

#### 4.3 SessionManager

**SessionManager.java**：
- `isLoggedIn()`：是否已登录
- `getCurrentUser()`：获取当前用户
- `saveSession(AuthResponse response)`：保存会话
- `clearSession()`：清除会话
- `updateUser(User user)`：更新用户信息

### 步骤 5：数据仓库实现

#### 5.1 AuthRepository

**AuthRepository.java**：
- `login(LoginRequest request)`：登录
- `register(RegisterRequest request)`：注册
- `sendCode(SendCodeRequest request)`：发送验证码
- `verifyCode(String phone, String code, int type)`：验证验证码
- `refreshToken(String refreshToken)`：刷新 token
- `resetPassword(ResetPwdRequest request)`：重置密码
- `changePassword(ChangePwdRequest request)`：修改密码
- `logout()`：退出登录
- `getUserProfile()`：获取用户信息

所有网络请求使用 Retrofit 的 Call 同步或异步执行。

### 步骤 6：视图模型实现

#### 6.1 AuthViewModel（主视图模型）

**AuthViewModel.java**：
- 状态管理（使用 LiveData）：
  - `authState` (AuthState)：认证状态（IDLE、LOADING、SUCCESS、ERROR）
  - `currentUser` (User)：当前用户
  - `errorMessage` (String)：错误消息
  - `countdown` (Integer)：验证码倒计时
  - `canResendCode` (Boolean)：是否可以重新发送
  - `passwordStrength` (PasswordStrength)：密码强度
- 方法：
  - `login(String account, String password)`：登录
  - `loginWithCode(String phone, String code)`：验证码登录
  - `register(RegisterRequest request)`：注册
  - `sendCode(String phone, int type)`：发送验证码
  - `resetPassword(ResetPwdRequest request)`：重置密码
  - `changePassword(String oldPwd, String newPwd)`：修改密码
  - `logout()`：退出登录
  - `checkAutoLogin()`：检查自动登录
  - `validatePassword(String password)`：验证密码强度

#### 6.2 枚举和状态类

**AuthState.java**：
```java
public enum AuthState {
    IDLE,      // 空闲
    LOADING,   // 加载中
    SUCCESS,   // 成功
    ERROR,     // 错误
    NEED_CAPTCHA,  // 需要验证码
    ACCOUNT_LOCKED  // 账号锁定
}
```

**PasswordStrength.java**：
```java
public class PasswordStrength {
    public enum Level {
        WEAK("弱", "#FF4D4F"),
        MEDIUM("中", "#FAAD14"),
        STRONG("强", "#52C41A"),
        VERY_STRONG("很强", "#0066FF");
    }
    
    private Level level;
    private String message;
    private int score;
    
    public static PasswordStrength check(String password) { ... }
}
```

### 步骤 7：UI 实现

#### 7.1 AuthActivity

**AuthActivity.java**：
- 作为登录、注册、找回密码页面的容器
- 使用 Fragment 切换管理页面
- 继承 BaseActivity<ActivityAuthBinding>
- 方法：
  - `switchToLogin()`：切换到登录页面
  - `switchToRegister()`：切换到注册页面
  - `switchToFindPwd()`：切换到找回密码页面
  - `goBack()`：返回上一页
  - `navigateToMain()`：跳转到首页

#### 7.2 WelcomeFragment

**WelcomeFragment.java**：
- 显示欢迎页面
- 提供"手机快捷登录"、"学号登录"、"注册账号"、"忘记密码"入口
- 根据是否已登录自动跳转

布局要求：
- 背景渐变效果
- 应用 Logo
- 欢迎文字
- 两个主要按钮（手机快捷登录、学号登录）
- 底部链接（注册账号、忘记密码）

#### 7.3 LoginFragment

**LoginFragment.java**：
- 账号输入框（支持手机号/学号/邮箱）
- 密码输入框（支持显示/隐藏切换）
- 记住密码开关
- 自动登录开关
- 登录按钮
- 忘记密码链接
- 注册账号链接
- 第三方登录占位（暂不实现）

布局要求：
- Logo 区域
- 卡片式输入区域
- 输入验证实时反馈
- Loading 状态
- 错误信息显示

#### 7.4 RegisterFragment

**RegisterFragment.java**：
- 手机号输入框（11位中国手机号格式验证）
- 验证码输入框（6位数字）
- 获取验证码按钮（60秒倒计时）
- 密码输入框（显示密码强度指示）
- 确认密码输入框
- 用户协议勾选框
- 注册按钮
- 已有账号链接

布局要求：
- 步骤指示器（可选）
- 输入框实时验证
- 密码强度进度条/指示器
- 倒计时按钮

#### 7.5 FindPwdFragment

**FindPwdFragment.java**：
- 使用步骤式界面
- 步骤1：输入手机号
- 步骤2：输入验证码
- 步骤3：重置密码（输入新密码和确认密码）

布局要求：
- 步骤指示器
- 各步骤输入区域
- 上一步/下一步按钮
- 完成按钮

### 步骤 8：工具类实现

#### 8.1 InputValidator

**InputValidator.java**：
- `isValidPhone(String phone)`：验证手机号格式
- `isValidEmail(String email)`：验证邮箱格式
- `isValidStudentId(String studentId)`：验证学号格式（10位数字）
- `isValidPassword(String password)`：验证密码格式（至少8位，包含大小写字母和数字）
- `isValidCode(String code)`：验证验证码格式（6位数字）
- `isValidNickname(String nickname)`：验证昵称格式（2-20位）

#### 8.2 PasswordUtils

**PasswordUtils.java**：
- `generateSalt()`：生成随机盐
- `encryptPassword(String password, String salt)`：加密密码
- `verifyPassword(String password, String storedPassword)`：验证密码
- `checkStrength(String password)`：检查密码强度

### 步骤 9：修改 MainActivity

在 MainActivity 的 onCreate 方法中添加登录状态检查：

```java
// 检查是否已登录
if (sessionManager.isLoggedIn()) {
    // 已登录，直接进入首页
    navigateToHome();
} else {
    // 未登录，进入认证页面
    navigateToAuth();
}
```

添加跳转方法：
- `navigateToAuth()`：跳转到 AuthActivity
- `navigateToHome()`：跳转到首页

---

## 📐 代码规范与要求

### 命名规范

类名使用大驼峰命名法，如 `LoginFragment`、`AuthViewModel`。方法名和变量名使用小驼峰命名法，如 `sendCode`、`userProfile`。常量名使用全大写加下划线，如 `DEFAULT_TIMEOUT`、`TOKEN_EXPIRY`。包名使用小写，如 `com.example.fjnuserviceapp.auth`。

### 代码格式

所有代码必须使用 UTF-8 编码。缩进使用 4 个空格。行宽不超过 120 个字符。大括号使用 K&R 风格（在同一行开始）。

### 注释要求

每个类必须有类注释，说明类的功能和用途。每个公开方法必须有方法注释，说明方法功能、参数和返回值。复杂的业务逻辑需要添加行内注释。XML 布局文件不需要注释，但需要保持整洁。

### 错误处理

网络错误：显示"网络连接异常，请检查网络"提示。服务器错误：显示服务器返回的错误消息。验证错误：在输入框下方显示具体错误信息。未知错误：显示"操作失败，请稍后重试"。

### 状态管理

加载状态：显示 Loading 动画，禁用按钮。成功状态：显示成功提示，跳转页面。错误状态：显示错误信息，恢复按钮可点击。空状态：显示空状态提示和操作按钮。

---

## 🔒 安全要求

### 数据存储

敏感数据（Token、密码）必须使用 EncryptedSharedPreferences 存储。密码本地存储必须加密（使用 PBKDF2 或 BCrypt）。不将敏感信息写入日志。

### 网络安全

所有 API 调用必须使用 HTTPS。不在 URL 中传递敏感参数。实现 Certificate Pinning（可选）。

### 输入验证

所有用户输入必须进行验证。防止 SQL 注入（使用 Room 参数化查询）。防止 XSS 攻击（输入转义）。

### 密码安全

密码必须加密存储（bcrypt 或 PBKDF2）。密码强度检测，至少 8 位，包含大小写字母和数字。限制密码尝试次数，连续失败 5 次显示验证码，连续失败 10 次锁定账号 30 分钟。

---

## 🧪 测试要求

### 单元测试

测试 InputValidator 的各种验证逻辑。测试 PasswordUtils 的加密和验证逻辑。测试 TokenManager 的 Token 管理逻辑。

### 集成测试

测试登录流程（成功和失败）。测试注册流程（成功和失败）。测试找回密码流程。测试 Token 刷新流程。

### UI 测试

测试各种输入验证。测试按钮状态切换。测试页面跳转。

---

## ⚠️ 实现注意事项

### 1. 后端 API 暂不可用

当前后端 API 尚未实现，在网络请求失败时需要：
- 显示友好的错误提示
- 提供重试选项
- 保存本地数据进行后续处理

### 2. 验证码发送

由于没有短信服务，验证码发送功能：
- 在控制台打印验证码（开发阶段）
- 实现倒计时逻辑
- 验证逻辑在本地进行（开发阶段）

### 3. 第三方登录

第三方登录（微信、QQ）暂不实现，在 UI 上显示占位即可。

### 4. 数据兼容性

数据库版本从 7 升级到 8，使用 Migration。保留原有数据（如有）。测试升级过程。

### 5. 向后兼容

新功能不能影响现有功能。保持原有 UI 风格一致。使用现有的主题和样式。

---

## 📝 实现优先级

### 第一优先级（必须实现）

用户手机号注册流程，包括手机号输入、验证码发送和验证、密码输入和注册、创建账户和自动登录。用户手机号+密码登录流程，包括账号密码输入、验证和登录、Token 保存和自动登录。找回密码流程，包括手机号验证、验证码验证和新密码设置。Token 管理和自动登录功能，包括 Token 存储、刷新和过期处理、应用启动自动登录。密码强度检测和输入验证功能。

### 第二优先级（应该实现）

记住密码功能。登录状态管理。错误提示优化。加载状态优化。

### 第三优先级（可选实现）

验证码登录。登录失败次数限制。账号锁定机制。

---

## 🎨 UI/UX 要求

### 设计风格

严格遵循项目现有设计语言，确保完全同步：

1. **Glassmorphism（毛玻璃拟态）**：
   - 半透明磨砂质感，使用 `#26FFFFFF` 至 `#4DFFFFFF` 半透明白色
   - 柔和阴影效果（`#1A000000`，10dp-20dp 模糊半径）
   - 1dp 宽半透明边框（`#33FFFFFF`）
   - 半透明背景叠加效果
   - 使用 `BlurUtils`（项目现有工具类）实现真实毛玻璃效果

2. **3D 视觉效果**：
   - 与项目 3D 旋转五角星菜单呼应，添加轻微 3D 倾斜效果
   - 卡片元素添加轻微 3D 变换，增强深度感
   - 深度阴影和立体层次感

3. **现代化配色**：
   - 主色调：蓝色渐变（`#0066FF` 至 `#00CCFF`）
   - 辅助色：紫色渐变（`#6600FF` 至 `#CC00FF`）
   - 强调色：青色（`#00F2FE`）用于高亮和动画效果
   - 渐变背景和渐变文字效果

4. **动态效果**：
   - 流畅的动画过渡
   - 呼吸光晕效果
   - 入场弹射动画
   - 粒子视差滚动
   - 卡片浮动动画（3000ms 周期）

5. **深色模式支持**：
   - 完整的深色主题
   - 自适应系统设置
   - 深色模式优化配色
   - 使用 `values-night` 目录存放深色模式资源

### 页面布局

- **页面边距**：20dp
- **卡片内边距**：20dp
- **输入框高度**：56dp
- **按钮高度**：56dp（主要按钮），48dp（次要按钮）
- **圆角规范**：
  - 小圆角：4dp（标签、小按钮）
  - 中圆角：8dp（输入框、开关）
  - 大圆角：12dp（主要按钮、卡片）
  - 全圆角：24dp（圆形按钮、头像）

### 交互设计

1. **输入实时验证**：
   - 输入时实时验证格式
   - 失焦验证
   - 提交验证
   - 错误提示（抖动+变色动画）

2. **按钮状态**：
   - 默认状态：正常显示
   - 加载状态：显示自定义 Loading 动画（粒子旋转）
   - 禁用状态：灰色显示，禁止点击
   - 成功状态：显示成功图标
   - 失败状态：显示错误信息，恢复可点击
   - 悬停效果：轻微上浮、阴影增强
   - 涟漪效果：使用项目强调色

3. **页面转场**：
   - 页面进入：底部滑入动画（300ms，AccelerateDecelerateInterpolator）
   - 页面退出：底部滑出动画（300ms，AccelerateDecelerateInterpolator）
   - 错误提示：抖动动画 + Toast/Snackbar
   - 加载状态：骨架屏或 Loading 动画

4. **输入框交互**：
   - 焦点时边框高亮（强调色 `#00F2FE`）
   - 密码显示/隐藏按钮使用项目强调色
   - 半透明背景、柔和阴影、细边框
   - 内阴影效果增强深度感

5. **手势支持**：
   - 点击：触发按钮点击事件
   - 长按：显示提示信息
   - 滑动：页面滑动切换
   - 双击：快速操作（如隐藏/显示密码）

---

## 📋 检查清单

在实现完成后，检查以下项目：

### 功能检查
- [ ] 用户可以注册新账号
- [ ] 用户可以登录
- [ ] 用户可以找回密码
- [ ] Token 自动刷新
- [ ] 自动登录功能正常
- [ ] 记住密码功能正常
- [ ] 密码强度检测正常
- [ ] 输入验证正常

### 代码检查
- [ ] 代码格式符合规范
- [ ] 必要的注释已添加
- [ ] 没有编译错误
- [ ] 没有警告
- [ ] 命名规范一致

### 测试检查
- [ ] 单元测试已编写
- [ ] 集成测试已执行
- [ ] UI 测试已执行
- [ ] 边界情况已测试

### 安全检查
- [ ] 敏感数据已加密
- [ ] 输入已验证
- [ ] 没有硬编码密钥
- [ ] 日志不包含敏感信息

---

## 📞 联系方式与支持

如有问题，请参考：
- 现有项目的代码风格和结构
- Android 官方文档：https://developer.android.com/
- Retrofit 文档：https://square.github.io/retrofit/
- Room 文档：https://developer.android.com/topic/libraries/room
- ViewModel 文档：https://developer.android.com/topic/libraries/architecture/viewmodel

---

## ✅ 开始实现

现在你可以开始实现了。请按照以上步骤和规范，逐步完成登录注册功能的实现。在实现过程中，请：
1. 保持代码风格与现有项目一致
2. 确保代码质量高、注释完整
3. 注意安全性和性能
4. 完成后进行充分测试

祝你实现顺利！