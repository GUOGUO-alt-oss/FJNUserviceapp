# 福师大校园生活服务 APP 登录注册功能实现计划

## 📋 目录

1. [需求分析](#1-需求分析)
2. [技术架构设计](#2-技术架构设计)
3. [数据库设计](#3-数据库设计)
4. [API接口设计](#4-api接口设计)
5. [UI/UX设计](#5-uiux设计)
6. [核心功能实现](#6-核心功能实现)
7. [安全策略](#7-安全策略)
8. [测试计划](#8-测试计划)
9. [部署与发布](#9-部署与发布)
10. [项目里程碑与时间规划](#10-项目里程碑与时间规划)
11. [风险评估与应对策略](#11-风险评估与应对策略)
12. [预算与资源需求](#12-预算与资源需求)

---

## 1. 需求分析

### 1.1 功能需求

#### 1.1.1 用户注册功能
- **手机号注册**：用户使用手机号接收验证码进行注册
- **学号注册**：用户使用学号和学校邮箱进行注册
- **第三方注册**：支持微信、QQ等第三方账号授权注册

#### 1.1.2 用户登录功能
- **手机号登录**：用户使用手机号和密码登录
- **学号登录**：用户使用学号和密码登录
- **验证码登录**：用户使用手机号和验证码登录
- **第三方登录**：支持微信、QQ等第三方账号授权登录
- **自动登录**：用户登录后，下次打开应用自动登录
- **记住密码**：用户可选择记住密码

#### 1.1.3 密码管理功能
- **修改密码**：用户登录后可修改密码
- **找回密码**：用户可通过手机号或邮箱找回密码
- **密码强度检测**：实时检测密码强度

#### 1.1.4 用户认证功能
- **Token管理**：实现Token的获取、刷新和过期处理
- **会话管理**：管理用户登录状态，支持多设备登录
- **权限控制**：基于用户角色的权限控制

### 1.2 非功能需求

#### 1.2.1 性能需求
- 登录响应时间：< 2秒
- 注册响应时间：< 3秒（包含验证码发送）
- 并发用户数：支持 1000+ 用户同时登录

#### 1.2.2 安全需求
- 密码加密存储：使用BCrypt加密
- 传输加密：全程HTTPS
- 防暴力破解：限制登录尝试次数
- 防SQL注入：使用参数化查询
- 防XSS攻击：输入校验和转义

#### 1.2.3 可用性需求
- 支持断网状态下的本地缓存
- 支持低版本Android系统（API 26+）
- 支持屏幕旋转等配置变更
- 提供友好的错误提示

#### 1.2.4 兼容性需求
- 兼容Android 8.0至Android 15
- 适配主流屏幕尺寸和分辨率
- 支持深色模式
- 支持无障碍访问

### 1.3 用户场景分析

#### 1.3.1 新用户注册场景
1. 新用户打开应用，进入欢迎页面
2. 用户点击"注册"按钮，进入注册页面
3. 用户选择注册方式（手机号/学号/第三方）
4. 用户填写注册信息，提交注册申请
5. 系统发送验证码，用户输入验证码
6. 系统验证通过，创建用户账户
7. 系统自动登录，跳转到首页

#### 1.3.2 老用户登录场景
1. 老用户打开应用，进入欢迎页面
2. 系统检测到已登录用户，自动登录或显示登录页面
3. 用户选择登录方式（手机号/学号/验证码/第三方）
4. 用户填写登录信息，提交登录申请
5. 系统验证身份，生成Token
6. 系统跳转到首页，显示用户信息

#### 1.3.3 密码找回场景
1. 用户在登录页面点击"忘记密码"
2. 用户选择找回方式（手机号/邮箱）
3. 用户填写身份信息
4. 系统发送验证码/重置链接
5. 用户重置密码
6. 用户使用新密码登录

## 2. 技术架构设计

### 2.1 整体架构

```
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                    UI Layer                                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────────────┐  │
│  │  AuthActivity│  │  LoginFrag  │  │ RegisterFrag│  │  FindPwdFragment            │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────────────────────┘
                                        │ ViewBinding
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                  ViewModel Layer                                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────────────┐  │
│  │ AuthViewModel│  │ LoginViewModel│ │ RegisterVM  │  │  FindPwdViewModel          │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────────────────────┘
                                        │ LiveData / Coroutines
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                 Repository Layer                                        │
│  ┌─────────────┐  ┌─────────────┐  ┌───────────────────────────┐  ┌───────────────┐  │
│  │AuthRepository│  │UserRepository│  │  TokenManager             │  │ PreferenceMgr│  │
│  └─────────────┘  └─────────────┘  └───────────────────────────┘  └───────────────┘  │
└─────────────────────────────────────────────────────────────────────────────────────────┘
                                        │ Retrofit / Room
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                   Data Layer                                            │
│  ┌─────────────┐  ┌─────────────┐  ┌───────────────────────────┐  ┌───────────────┐  │
│  │   Server    │  │  Room DB    │  │  SharedPreferences        │  │  SecureStorage│  │
│  └─────────────┘  └─────────────┘  └───────────────────────────┘  └───────────────┘  │
└─────────────────────────────────────────────────────────────────────────────────────────┘
```

### 2.2 核心组件设计

#### 2.2.1 AuthActivity（认证活动）
- **职责**：管理登录、注册、找回密码等认证相关页面
- **导航**：支持页面间的跳转和返回
- **状态管理**：管理认证流程的状态

#### 2.2.2 AuthViewModel（认证视图模型）
- **职责**：处理认证相关的业务逻辑
- **数据**：管理用户信息、登录状态、错误信息等
- **方法**：login()、register()、logout()、resetPassword()等

#### 2.2.3 AuthRepository（认证仓库）
- **职责**：封装认证相关的数据访问
- **数据源**：网络API、本地数据库、缓存
- **方法**：login()、register()、getUserProfile()、updateUserProfile()等

#### 2.2.4 TokenManager（Token管理器）
- **职责**：管理用户认证Token
- **功能**：Token存储、Token刷新、Token过期处理
- **存储**：使用加密的SharedPreferences或EncryptedSharedPreferences

### 2.3 技术选型

| 技术 | 用途 | 版本/规格 |
|------|------|----------|
| 开发语言 | Java（可逐步迁移到Kotlin） | 11 |
| 网络框架 | Retrofit + OkHttp | Retrofit 2.9.0+ |
| JSON解析 | Gson | 2.10.1+ |
| 数据库 | Room | 2.5.2+ |
| 生命周期 | Lifecycle + ViewModel + LiveData | 2.6.2+ |
| 异步编程 | Coroutines（可选）或RxJava | 1.7.3+ |
| 加密库 | AES + BCrypt | - |
| 验证码 | 短信验证码或邮箱验证码 | - |
| 第三方SDK | 微信登录、QQ登录 | 最新版本 |

## 3. 数据库设计

### 3.1 用户表设计

#### 3.1.1 用户基本信息表（users）
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    phone VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE,
    student_id VARCHAR(20) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(64) NOT NULL,
    nickname VARCHAR(50),
    real_name VARCHAR(50),
    avatar_url VARCHAR(255),
    department VARCHAR(100),
    major VARCHAR(100),
    gender TINYINT DEFAULT 0,
    status TINYINT DEFAULT 1,  -- 0:禁用, 1:正常, 2:待审核
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login_at DATETIME,
    login_count INTEGER DEFAULT 0
);
```

#### 3.1.2 用户Token表（user_tokens）
```sql
CREATE TABLE user_tokens (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    token VARCHAR(255) UNIQUE NOT NULL,
    refresh_token VARCHAR(255) UNIQUE NOT NULL,
    device_type VARCHAR(20),
    device_token VARCHAR(255),
    expires_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE INDEX idx_user_tokens_token ON user_tokens(token);
CREATE INDEX idx_user_tokens_user_id ON user_tokens(user_id);
```

#### 3.1.3 验证码记录表（verification_codes）
```sql
CREATE TABLE verification_codes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    code VARCHAR(10) NOT NULL,
    type TINYINT NOT NULL,  -- 1:注册, 2:登录, 3:找回密码
    status TINYINT DEFAULT 0,  -- 0:未使用, 1:已使用, 2:已过期
    expires_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_verification_codes_phone ON verification_codes(phone);
CREATE INDEX idx_verification_codes_code ON verification_codes(code);
```

### 3.2 Room实体类设计

#### 3.2.1 User实体类
```java
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    @ColumnInfo(index = true)
    private String phone;
    
    @ColumnInfo(index = true)
    private String email;
    
    @ColumnInfo(index = true)
    private String studentId;
    
    private String passwordHash;
    private String salt;
    private String nickname;
    private String realName;
    private String avatarUrl;
    private String department;
    private String major;
    private int gender;  // 0:未知, 1:男, 2:女
    private int status;  // 0:禁用, 1:正常, 2:待审核
    private long createdAt;
    private long updatedAt;
    private long lastLoginAt;
    private int loginCount;
    
    // 构造方法、Getter和Setter
}
```

#### 3.2.2 UserToken实体类
```java
@Entity(tableName = "user_tokens",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = CASCADE
        ),
        indices = {
                @Index("token"),
                @Index("userId")
        })
public class UserToken {
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private long userId;
    private String token;
    private String refreshToken;
    private String deviceType;
    private String deviceToken;
    private long expiresAt;
    private long createdAt;
    
    // 构造方法、Getter和Setter
}
```

### 3.3 DAO接口设计

#### 3.3.1 UserDao接口
```java
@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    User getUserByPhone(String phone);
    
    @Query("SELECT * FROM users WHERE student_id = :studentId LIMIT 1")
    User getUserByStudentId(String studentId);
    
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);
    
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    User getUserById(long userId);
    
    @Query("SELECT * FROM users WHERE (phone = :account OR student_id = :account OR email = :account) LIMIT 1")
    User getUserByAccount(String account);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);
    
    @Update
    void update(User user);
    
    @Query("UPDATE users SET last_login_at = :loginAt, login_count = login_count + 1 WHERE id = :userId")
    void updateLoginInfo(long userId, long loginAt);
    
    @Query("UPDATE users SET status = :status WHERE id = :userId")
    void updateStatus(long userId, int status);
    
    @Query("DELETE FROM users WHERE id = :userId")
    void deleteById(long userId);
}
```

#### 3.3.2 UserTokenDao接口
```java
@Dao
public interface UserTokenDao {
    @Query("SELECT * FROM user_tokens WHERE token = :token LIMIT 1")
    UserToken getTokenByToken(String token);
    
    @Query("SELECT * FROM user_tokens WHERE user_id = :userId")
    List<UserToken> getTokensByUserId(long userId);
    
    @Query("SELECT * FROM user_tokens WHERE user_id = :userId AND device_type = :deviceType LIMIT 1")
    UserToken getTokenByUserAndDevice(long userId, String deviceType);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(UserToken token);
    
    @Update
    void update(UserToken token);
    
    @Query("DELETE FROM user_tokens WHERE token = :token")
    void deleteByToken(String token);
    
    @Query("DELETE FROM user_tokens WHERE user_id = :userId")
    void deleteByUserId(long userId);
    
    @Query("DELETE FROM user_tokens WHERE expires_at < :currentTime")
    void deleteExpiredTokens(long currentTime);
}
```

### 3.4 数据库升级策略

#### 3.4.1 数据库版本升级
- 当前版本：7
- 新版本：8
- 升级方式：使用Migration进行数据迁移

#### 3.4.2 Migration实现
```java
static final Migration MIGRATION_7_8 = new Migration(7, 8) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
        // 创建新表
        database.execSQL("CREATE TABLE users (...)");
        database.execSQL("CREATE TABLE user_tokens (...)");
        database.execSQL("CREATE TABLE verification_codes (...)");
        
        // 迁移旧数据（如果有）
        // ...
    }
};
```

## 4. API接口设计

### 4.1 接口规范

#### 4.1.1 基础信息
- 基础URL：https://api.fjnuservice.com/v1/
- 认证方式：Bearer Token
- 请求格式：JSON
- 响应格式：JSON

#### 4.1.2 统一响应格式
```json
{
    "code": 200,
    "message": "success",
    "data": {
        // 响应数据
    },
    "timestamp": 1704067200000,
    "requestId": "xxx"
}
```

#### 4.1.3 错误码定义
| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未认证或Token过期 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 422 | 业务逻辑错误 |
| 500 | 服务器内部错误 |

### 4.2 接口列表

#### 4.2.1 用户注册接口
**接口路径**：POST /auth/register

**请求参数**：
```json
{
    "phone": "13800138000",
    "code": "123456",
    "password": "password123",
    "studentId": "20210001",
    "email": "student@fjn.edu.cn",
    "nickname": "用户名",
    "department": "计算机科学与技术学院",
    "major": "计算机科学与技术"
}
```

**响应参数**：
```json
{
    "code": 200,
    "message": "注册成功",
    "data": {
        "user": {
            "id": 1,
            "phone": "13800138000",
            "nickname": "用户名",
            "avatarUrl": null
        },
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "expiresIn": 86400
    }
}
```

#### 4.2.2 用户登录接口
**接口路径**：POST /auth/login

**请求参数**：
```json
{
    "account": "13800138000",
    "password": "password123",
    "deviceType": "android",
    "deviceToken": "xxx"
}
```

**响应参数**：
```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "user": {
            "id": 1,
            "phone": "13800138000",
            "nickname": "用户名",
            "avatarUrl": "https://...",
            "studentId": "20210001"
        },
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "expiresIn": 86400
    }
}
```

#### 4.2.3 发送验证码接口
**接口路径**：POST /auth/send-code

**请求参数**：
```json
{
    "phone": "13800138000",
    "type": 1  // 1:注册, 2:登录, 3:找回密码
}
```

**响应参数**：
```json
{
    "code": 200,
    "message": "验证码发送成功",
    "data": {
        "expiresIn": 300  // 验证码有效期（秒）
    }
}
```

#### 4.2.4 刷新Token接口
**接口路径**：POST /auth/refresh-token

**请求参数**：
```json
{
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**响应参数**：
```json
{
    "code": 200,
    "message": "Token刷新成功",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "expiresIn": 86400
    }
}
```

#### 4.2.5 找回密码接口
**接口路径**：POST /auth/reset-password

**请求参数**：
```json
{
    "phone": "13800138000",
    "code": "123456",
    "newPassword": "newpassword123"
}
```

**响应参数**：
```json
{
    "code": 200,
    "message": "密码重置成功"
}
```

#### 4.2.6 修改密码接口
**接口路径**：POST /auth/change-password

**请求参数**：
```json
{
    "oldPassword": "oldpassword123",
    "newPassword": "newpassword123"
}
```

**响应参数**：
```json
{
    "code": 200,
    "message": "密码修改成功"
}
```

#### 4.2.7 获取用户信息接口
**接口路径**：GET /user/profile

**请求头**：
```
Authorization: Bearer <token>
```

**响应参数**：
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "user": {
            "id": 1,
            "phone": "13800138000",
            "nickname": "用户名",
            "avatarUrl": "https://...",
            "studentId": "20210001",
            "department": "计算机科学与技术学院",
            "major": "计算机科学与技术",
            "gender": 1,
            "createdAt": "2024-01-01 00:00:00"
        }
    }
}
```

#### 4.2.8 退出登录接口
**接口路径**：POST /auth/logout

**请求头**：
```
Authorization: Bearer <token>
```

**请求参数**：
```json
{
    "deviceType": "android"
}
```

**响应参数**：
```json
{
    "code": 200,
    "message": "退出成功"
}
```

### 4.3 Retrofit接口定义

```java
public interface AuthApi {
    // 用户注册
    @POST("auth/register")
    Call<BaseResponse<AuthData>> register(@Body RegisterRequest request);
    
    // 用户登录
    @POST("auth/login")
    Call<BaseResponse<AuthData>> login(@Body LoginRequest request);
    
    // 发送验证码
    @POST("auth/send-code")
    Call<BaseResponse<CodeResponse>> sendCode(@Body SendCodeRequest request);
    
    // 验证验证码
    @POST("auth/verify-code")
    Call<BaseResponse<Void>> verifyCode(@Body VerifyCodeRequest request);
    
    // 刷新Token
    @POST("auth/refresh-token")
    Call<BaseResponse<TokenResponse>> refreshToken(@Body RefreshTokenRequest request);
    
    // 找回密码
    @POST("auth/reset-password")
    Call<BaseResponse<Void>> resetPassword(@Body ResetPasswordRequest request);
    
    // 修改密码
    @POST("auth/change-password")
    Call<BaseResponse<Void>> changePassword(@Header("Authorization") String token, @Body ChangePasswordRequest request);
    
    // 获取用户信息
    @GET("user/profile")
    Call<BaseResponse<UserResponse>> getUserProfile(@Header("Authorization") String token);
    
    // 更新用户信息
    @PUT("user/profile")
    Call<BaseResponse<UserResponse>> updateUserProfile(@Header("Authorization") String token, @Body UpdateProfileRequest request);
    
    // 退出登录
    @POST("auth/logout")
    Call<BaseResponse<Void>> logout(@Header("Authorization") String token, @Body LogoutRequest request);
}
```

## 5. UI/UX设计

### 5.1 页面结构

```
AuthActivity (认证页面容器)
├── WelcomeFragment (欢迎页面)
│   ├── Logo和欢迎语（带浮动/旋转动画）
│   ├── 项目标志性3D元素或粒子背景
│   ├── "手机快捷登录"按钮（Glassmorphism效果）
│   ├── "学号登录"按钮（Glassmorphism效果）
│   ├── "注册账号"链接
│   └── "忘记密码"链接
│
├── LoginFragment (登录页面)
│   ├── 标题栏（Glassmorphism效果，返回按钮+标题）
│   ├── Logo区域（带轻微3D效果）
│   ├── 账号输入框（Glassmorphism效果）
│   ├── 密码输入框（Glassmorphism效果，可切换显示/隐藏）
│   ├── 记住密码开关（自定义样式，项目配色）
│   ├── 自动登录开关（自定义样式，项目配色）
│   ├── "登录"按钮（渐变效果，悬停动画）
│   ├── "忘记密码"链接
│   ├── 第三方登录（Glassmorphism效果，微信、QQ）
│   └── 注册账号链接
│
├── RegisterFragment (注册页面)
│   ├── 标题栏（Glassmorphism效果，返回按钮+标题）
│   ├── 手机号输入框（Glassmorphism效果）
│   ├── 验证码输入框（Glassmorphism效果）
│   ├── "获取验证码"按钮（倒计时动画，Glassmorphism效果）
│   ├── 密码输入框（Glassmorphism效果，动态强度指示）
│   ├── 确认密码输入框（Glassmorphism效果）
│   ├── 学号输入框（可选，Glassmorphism效果）
│   ├── 邮箱输入框（可选，Glassmorphism效果）
│   ├── 用户协议勾选框
│   ├── "注册"按钮（渐变效果，悬停动画）
│   └── 已有账号，去登录
│
├── FindPwdFragment (找回密码页面)
│   ├── 标题栏（Glassmorphism效果，返回按钮+标题）
│   ├── 步骤指示器（动态填充动画）
│   ├── 步骤1：输入手机号/邮箱（Glassmorphism效果）
│   ├── 步骤2：输入验证码（Glassmorphism效果）
│   ├── 步骤3：重置密码（Glassmorphism效果，动态强度指示）
│   └── "完成"按钮（渐变效果，悬停动画）
│
└── ProfileSetupFragment (完善资料页面 - 注册后)
    ├── 标题栏（Glassmorphism效果，跳过+标题）
    ├── 头像选择区域（带轻微3D效果）
    ├── 昵称输入框（Glassmorphism效果）
    ├── 性别选择（自定义样式，项目配色）
    ├── 学院选择（Glassmorphism效果）
    ├── 专业选择（Glassmorphism效果）
    ├── "完成"按钮（渐变效果，悬停动画）
    └── "跳过"按钮（Glassmorphism效果）
```

### 5.2 设计规范

#### 5.2.1 颜色规范
| 元素 | 颜色/渐变 | 说明 |
|------|----------|------|
| 主色调渐变 | `#0066FF` 至 `#00CCFF` | 主要按钮、标题文字、渐变背景 |
| 辅助色渐变 | `#6600FF` 至 `#CC00FF` | 次要按钮、装饰元素 |
| 强调色 | `#00F2FE` | 高亮状态、边框、动画效果、涟漪效果 |
| Glassmorphism背景 | `#26FFFFFF` 至 `#4DFFFFFF` | 卡片、输入框、按钮背景 |
| Glassmorphism边框 | `#33FFFFFF` | 卡片、输入框、按钮边框 |
| 阴影颜色 | `#1A000000` | 所有Glassmorphism元素 |
| 背景色 | 渐变背景 | 页面背景，与项目主页一致 |
| 文字主色 | `#1A1A1A` | 主要文字 |
| 文字次色 | `#666666` | 次要文字 |
| 文字提示色 | `#999999` | 提示文字 |
| 错误色 | `#FF4D4F` | 错误提示、密码强度弱 |
| 警告色 | `#FAAD14` | 警告提示、密码强度中 |
| 成功色 | `#52C41A` | 成功提示、密码强度强 |
| 很强色 | `#0066FF` | 密码强度很强 |

#### 5.2.2 字体规范
- 标题文字：24sp，粗体，渐变文字效果
- 主要文字：16sp，常规
- 次要文字：14sp，常规
- 提示文字：12sp，常规
- 按钮文字：16sp，中等，渐变文字效果（主要按钮）

#### 5.2.3 间距规范
- 页面边距：20dp
- 卡片内边距：20dp
- 元素间距：16dp
- 按钮高度：56dp（主要按钮），48dp（次要按钮）
- 输入框高度：56dp

#### 5.2.4 圆角规范
- 小圆角：4dp（标签、小按钮）
- 中圆角：8dp（输入框、开关）
- 大圆角：12dp（主要按钮、卡片）
- 全圆角：24dp（圆形按钮、头像、验证码按钮）

### 5.3 交互设计

#### 5.3.1 输入验证
- **实时验证**：输入时实时验证格式
- **失焦验证**：失去焦点时验证
- **提交验证**：点击按钮时验证全部
- **错误提示**：输入框下方显示错误信息，带抖动+变色动画

#### 5.3.2 按钮状态
- **默认状态**：正常显示
- **加载状态**：显示自定义Loading动画（粒子旋转），禁止点击
- **禁用状态**：灰色显示，禁止点击
- **成功状态**：显示成功图标
- **失败状态**：显示错误信息，恢复可点击
- **悬停效果**：轻微上浮、阴影增强、呼吸光晕效果
- **涟漪效果**：使用项目强调色 `#00F2FE`

#### 5.3.3 页面转场
- **页面进入**：底部滑入动画（300ms，AccelerateDecelerateInterpolator）
- **页面退出**：底部滑出动画（300ms，AccelerateDecelerateInterpolator）
- **错误提示**：抖动动画 + Toast/Snackbar
- **加载状态**：骨架屏或自定义Loading动画（粒子旋转）
- **入场动画**：与项目主页一致的弹射动画或淡入效果

#### 5.3.4 手势支持
- **点击**：触发按钮点击事件
- **长按**：显示提示信息
- **滑动**：页面滑动切换
- **双击**：快速操作（如隐藏/显示密码）

#### 5.3.5 3D效果融入
- 登录注册页面添加轻微3D倾斜效果，与项目3D旋转五角星菜单呼应
- 卡片元素添加轻微3D变换，增强深度感
- Logo区域添加轻微3D效果
- 头像选择区域添加轻微3D效果

### 5.4 Glassmorphism实现规范

#### 5.4.1 核心实现
```xml
<!-- bg_auth_glass_card.xml -->
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#26FFFFFF" />
    <corners android:radius="12dp" />
    <stroke
        android:width="1dp"
        android:color="#33FFFFFF" />
</shape>

<!-- 布局中应用 -->
<androidx.cardview.widget.CardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@drawable/bg_auth_glass_card"
    android:elevation="10dp"
    android:padding="20dp"
    app:cardCornerRadius="12dp">
    <!-- 内容 -->
</androidx.cardview.widget.CardView>
```

#### 5.4.2 实现要点
- 使用 `BlurUtils`（项目现有工具类）实现真实毛玻璃效果
- 背景叠加半透明颜色（`#26FFFFFF` 至 `#4DFFFFFF`）
- 添加柔和阴影（`#1A000000`，10dp-20dp 模糊半径）
- 添加1dp宽半透明边框（`#33FFFFFF`）
- 配合适当的圆角（12dp 用于卡片，8dp 用于输入框）

### 5.5 动画效果规范

| 动画类型 | 时长 | 缓动函数 | 应用场景 |
|----------|------|----------|----------|
| 页面转场 | 300ms | AccelerateDecelerateInterpolator | 页面切换 |
| 按钮反馈 | 200ms | OvershootInterpolator | 按钮点击 |
| 输入框焦点 | 300ms | FastOutSlowInInterpolator | 输入框焦点变化 |
| 卡片浮动 | 3000ms | LinearInterpolator | 表单卡片 |
| 加载动画 | 1000ms | LinearInterpolator | 加载状态 |
| 密码强度指示 | 300ms | FastOutSlowInInterpolator | 密码强度变化 |
| 验证码倒计时 | 1000ms | LinearInterpolator | 验证码发送按钮 |
| 卡片3D变换 | 500ms | AccelerateDecelerateInterpolator | 页面加载 |

### 5.6 深色模式支持

- 完整的深色主题支持
- 自适应系统设置
- 使用 `values-night` 目录存放深色模式资源
- 深色模式优化配色：
  - Glassmorphism背景：`#1A1A1A99` 至 `#2A2A2A99`
  - 边框颜色：`#44444499`
  - 阴影颜色：`#00000099`
- 支持手动切换主题

### 5.7 关键交互流程

#### 5.7.1 登录流程
```
用户打开APP
    ↓
检查登录状态（本地Token）
    ↓
有有效Token → 自动登录 → 跳转首页
    ↓
无有效Token
    ↓
显示欢迎页面
    ↓
用户点击登录
    ↓
显示登录页面
    ↓
用户输入账号密码
    ↓
实时验证输入格式
    ↓
用户点击登录
    ↓
显示Loading
    ↓
调用登录API
    ↓
登录成功 → 保存Token → 跳转首页
    ↓
登录失败 → 显示错误信息 → 恢复可点击
```

#### 5.4.2 注册流程
```
用户点击注册
    ↓
显示注册页面
    ↓
用户输入手机号
    ↓
用户点击获取验证码
    ↓
发送验证码请求
    ↓
发送成功 → 开始倒计时
    ↓
用户输入验证码
    ↓
用户输入密码
    ↓
实时检测密码强度
    ↓
用户点击注册
    ↓
显示Loading
    ↓
调用注册API
    ↓
注册成功 → 自动登录 → 跳转完善资料/首页
    ↓
注册失败 → 显示错误信息 → 恢复可点击
```

#### 5.4.3 找回密码流程
```
用户点击忘记密码
    ↓
显示找回密码页面（步骤1）
    ↓
用户输入手机号
    ↓
用户点击下一步
    ↓
显示找回密码页面（步骤2）
    ↓
用户输入验证码
    ↓
用户点击下一步
    ↓
显示找回密码页面（步骤3）
    ↓
用户输入新密码
    ↓
实时检测密码强度
    ↓
用户点击确认
    ↓
调用重置密码API
    ↓
重置成功 → 跳转登录页面
```

## 6. 核心功能实现

### 6.1 用户注册实现

#### 6.1.1 注册流程
```java
public class RegisterViewModel extends ViewModel {
    private MutableLiveData<RegisterState> state = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    // 发送验证码
    public void sendCode(String phone, int type) {
        state.setValue(RegisterState.LOADING);
        
        authRepository.sendCode(phone, type)
                .subscribe(new BaseObserver<CodeResponse>() {
                    @Override
                    public void onSuccess(CodeResponse data) {
                        state.setValue(RegisterState.CODE_SENT);
                        startCountdown();
                    }
                    
                    @Override
                    public void onError(String message) {
                        errorMessage.setValue(message);
                        state.setValue(RegisterState.IDLE);
                    }
                });
    }
    
    // 注册
    public void register(RegisterRequest request) {
        state.setValue(RegisterState.LOADING);
        
        authRepository.register(request)
                .subscribe(new BaseObserver<AuthData>() {
                    @Override
                    public void onSuccess(AuthData data) {
                        // 保存Token
                        tokenManager.saveToken(data.getToken());
                        tokenManager.saveRefreshToken(data.getRefreshToken());
                        
                        // 更新登录状态
                        state.setValue(RegisterState.SUCCESS);
                    }
                    
                    @Override
                    public void onError(String message) {
                        errorMessage.setValue(message);
                        state.setValue(RegisterState.IDLE);
                    }
                });
    }
    
    // 验证码倒计时
    private void startCountdown() {
        final int TOTAL_SECONDS = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(i -> TOTAL_SECONDS - i)
                .take(TOTAL_SECONDS + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(seconds -> {
                    countdown.setValue(seconds);
                    if (seconds == 0) {
                        canResend.setValue(true);
                    }
                });
    }
}
```

#### 6.1.2 密码强度检测
```java
public class PasswordStrengthChecker {
    
    public enum Strength {
        WEAK(0, "弱", Color.parseColor("#FF4D4F")),
        MEDIUM(1, "中", Color.parseColor("#FAAD14")),
        STRONG(2, "强", Color.parseColor("#52C41A")),
        VERY_STRONG(3, "很强", Color.parseColor("#0066FF"));
        
        private final int level;
        private final String text;
        private final int color;
        
        Strength(int level, String text, int color) {
            this.level = level;
            this.text = text;
            this.color = color;
        }
    }
    
    public static Strength check(String password) {
        int score = 0;
        
        // 长度检测
        if (password.length() >= 8) score += 1;
        if (password.length() >= 12) score += 1;
        
        // 复杂度检测
        if (password.matches(".*[A-Z].*")) score += 1;  // 大写字母
        if (password.matches(".*[a-z].*")) score += 1;  // 小写字母
        if (password.matches(".*\\d.*")) score += 1;    // 数字
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) score += 1;  // 特殊字符
        
        // 等级划分
        if (score <= 2) return Strength.WEAK;
        if (score <= 4) return Strength.MEDIUM;
        if (score <= 5) return Strength.STRONG;
        return Strength.VERY_STRONG;
    }
    
    public static String getRequirements() {
        return "密码要求：\n" +
                "• 长度至少8位\n" +
                "• 包含大写字母（A-Z）\n" +
                "• 包含小写字母（a-z）\n" +
                "• 包含数字（0-9）\n" +
                "• 包含特殊字符（!@#$%^&*等）";
    }
}
```

### 6.2 用户登录实现

#### 6.2.1 登录逻辑
```java
public class LoginViewModel extends ViewModel {
    private MutableLiveData<LoginState> state = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    // 登录
    public void login(String account, String password, String deviceType, String deviceToken) {
        if (!validateInput(account, password)) {
            return;
        }
        
        state.setValue(LoginState.LOADING);
        
        authRepository.login(account, password, deviceType, deviceToken)
                .subscribe(new BaseObserver<AuthData>() {
                    @Override
                    public void onSuccess(AuthData data) {
                        // 保存Token
                        tokenManager.saveToken(data.getToken());
                        tokenManager.saveRefreshToken(data.getRefreshToken());
                        tokenManager.saveTokenExpiresAt(data.getExpiresIn());
                        
                        // 保存用户信息
                        user.setValue(data.getUser());
                        
                        // 记住密码
                        if (rememberPassword.getValue()) {
                            preferencesManager.saveAccount(account, password);
                        } else {
                            preferencesManager.clearPassword();
                        }
                        
                        // 自动登录
                        preferencesManager.setAutoLogin(autoLogin.getValue());
                        
                        state.setValue(LoginState.SUCCESS);
                    }
                    
                    @Override
                    public void onError(String message) {
                        errorMessage.setValue(message);
                        state.setValue(LoginState.IDLE);
                        
                        // 处理登录失败次数
                        handleLoginFailure(account);
                    }
                });
    }
    
    // 输入验证
    private boolean validateInput(String account, String password) {
        if (TextUtils.isEmpty(account)) {
            errorMessage.setValue("请输入账号");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            errorMessage.setValue("请输入密码");
            return false;
        }
        if (password.length() < 6) {
            errorMessage.setValue("密码长度至少6位");
            return false;
        }
        return true;
    }
    
    // 处理登录失败
    private void handleLoginFailure(String account) {
        int failCount = loginFailCountMap.getOrDefault(account, 0) + 1;
        loginFailCountMap.put(account, failCount);
        
        // 连续失败5次，显示验证码
        if (failCount >= 5) {
            state.setValue(LoginState.NEED_CAPTCHA);
        }
        
        // 连续失败10次，锁定账号
        if (failCount >= 10) {
            state.setValue(LoginState.ACCOUNT_LOCKED);
        }
    }
}
```

#### 6.2.2 自动登录
```java
public class AutoLoginManager {
    
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_REFRESH_TOKEN = "key_refresh_token";
    private static final String KEY_TOKEN_EXPIRES = "key_token_expires";
    private static final String KEY_USER_ID = "key_user_id";
    
    private final Context context;
    private final TokenManager tokenManager;
    private final AuthRepository authRepository;
    
    public AutoLoginManager(Context context, TokenManager tokenManager, AuthRepository authRepository) {
        this.context = context.getApplicationContext();
        this.tokenManager = tokenManager;
        this.authRepository = authRepository;
    }
    
    // 检查是否需要自动登录
    public boolean shouldAutoLogin() {
        return preferencesManager.getAutoLogin() && tokenManager.hasValidToken();
    }
    
    // 自动登录
    public void autoLogin(Callback<User> callback) {
        if (!tokenManager.hasValidToken()) {
            callback.onFailure(new Exception("无有效Token"));
            return;
        }
        
        // Token即将过期，刷新Token
        if (tokenManager.isTokenExpiringSoon()) {
            refreshTokenAndLogin(callback);
            return;
        }
        
        // Token有效，获取用户信息
        authRepository.getUserProfile()
                .subscribe(new BaseObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        callback.onSuccess(user);
                    }
                    
                    @Override
                    public void onError(String message) {
                        // Token可能过期，尝试刷新
                        refreshTokenAndLogin(callback);
                    }
                });
    }
    
    // 刷新Token并登录
    private void refreshTokenAndLogin(Callback<User> callback) {
        String refreshToken = tokenManager.getRefreshToken();
        if (TextUtils.isEmpty(refreshToken)) {
            callback.onFailure(new Exception("无Refresh Token"));
            return;
        }
        
        authRepository.refreshToken(refreshToken)
                .subscribe(new BaseObserver<TokenResponse>() {
                    @Override
                    public void onSuccess(TokenResponse data) {
                        tokenManager.saveToken(data.getToken());
                        tokenManager.saveRefreshToken(data.getRefreshToken());
                        tokenManager.saveTokenExpiresAt(data.getExpiresIn());
                        
                        // 重新获取用户信息
                        autoLogin(callback);
                    }
                    
                    @Override
                    public void onError(String message) {
                        // 刷新失败，需要重新登录
                        callback.onFailure(new Exception(message));
                    }
                });
    }
    
    // 退出登录
    public void logout() {
        authRepository.logout()
                .subscribe(new BaseObserver<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        clearLoginState();
                    }
                    
                    @Override
                    public void onError(String message) {
                        // 即使失败也清除本地状态
                        clearLoginState();
                    }
                });
    }
    
    // 清除登录状态
    private void clearLoginState() {
        tokenManager.clearTokens();
        preferencesManager.clearLoginState();
    }
}
```

### 6.3 Token管理实现

#### 6.3.1 TokenManager
```java
public class TokenManager {
    
    private static final String KEY_ACCESS_TOKEN = "key_access_token";
    private static final String KEY_REFRESH_TOKEN = "key_refresh_token";
    private static final String KEY_TOKEN_EXPIRES = "key_token_expires";
    
    private final SharedPreferences securePrefs;
    private final Gson gson;
    
    public TokenManager(Context context) {
        this.securePrefs = EncryptedSharedPreferences.create(
                context,
                "token_prefs",
                MasterKey.DEFAULT_MASTER_KEY,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
        this.gson = new Gson();
    }
    
    // 保存Access Token
    public void saveToken(String token) {
        securePrefs.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }
    
    // 获取Access Token
    public String getToken() {
        return securePrefs.getString(KEY_ACCESS_TOKEN, null);
    }
    
    // 保存Refresh Token
    public void saveRefreshToken(String refreshToken) {
        securePrefs.edit().putString(KEY_REFRESH_TOKEN, refreshToken).apply();
    }
    
    // 获取Refresh Token
    public String getRefreshToken() {
        return securePrefs.getString(KEY_REFRESH_TOKEN, null);
    }
    
    // 保存Token过期时间
    public void saveTokenExpiresAt(long expiresIn) {
        long expiresAt = System.currentTimeMillis() + expiresIn * 1000;
        securePrefs.edit().putLong(KEY_TOKEN_EXPIRES, expiresAt).apply();
    }
    
    // 获取Token过期时间
    public long getTokenExpiresAt() {
        return securePrefs.getLong(KEY_TOKEN_EXPIRES, 0);
    }
    
    // 检查Token是否有效
    public boolean hasValidToken() {
        String token = getToken();
        long expiresAt = getTokenExpiresAt();
        return !TextUtils.isEmpty(token) && expiresAt > System.currentTimeMillis();
    }
    
    // 检查Token是否即将过期（5分钟内）
    public boolean isTokenExpiringSoon() {
        long expiresAt = getTokenExpiresAt();
        long fiveMinutesLater = System.currentTimeMillis() + 5 * 60 * 1000;
        return expiresAt > 0 && expiresAt < fiveMinutesLater;
    }
    
    // 清除所有Token
    public void clearTokens() {
        securePrefs.edit()
                .remove(KEY_ACCESS_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
                .remove(KEY_TOKEN_EXPIRES)
                .apply();
    }
}
```

#### 6.3.2 认证拦截器
```java
public class AuthInterceptor implements Interceptor {
    
    private final TokenManager tokenManager;
    
    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        
        // 不需要认证的接口
        if (isPublicApi(originalRequest)) {
            return chain.proceed(originalRequest);
        }
        
        // 获取当前Token
        String token = tokenManager.getToken();
        if (TextUtils.isEmpty(token)) {
            // 无Token，跳转到登录页面
            return handleNoToken(chain);
        }
        
        // 检查Token是否即将过期
        if (tokenManager.isTokenExpiringSoon()) {
            // 异步刷新Token
            refreshTokenSync();
        }
        
        // 添加认证头
        Request authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
        
        Response response = chain.proceed(authenticatedRequest);
        
        // 处理401错误（Token过期）
        if (response.code() == 401) {
            return handleUnauthorized(chain, originalRequest);
        }
        
        return response;
    }
    
    private boolean isPublicApi(Request request) {
        String path = request.url().encodedPath();
        return path.contains("/auth/login") ||
               path.contains("/auth/register") ||
               path.contains("/auth/send-code") ||
               path.contains("/auth/refresh-token");
    }
    
    private Response handleNoToken(Chain chain) {
        // 发送广播，触发跳转登录页面
        Intent intent = new Intent("ACTION_NEED_LOGIN");
        intent.setPackage(App.getContext().getPackageName());
        App.getContext().sendBroadcast(intent);
        
        // 返回错误
        return new Response.Builder()
                .code(401)
                .message("需要登录")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .build();
    }
    
    private Response handleUnauthorized(Chain chain, Request originalRequest) {
        // 尝试刷新Token
        String refreshToken = tokenManager.getRefreshToken();
        if (!TextUtils.isEmpty(refreshToken)) {
            boolean refreshed = refreshTokenSync();
            if (refreshed) {
                // 重试原请求
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + tokenManager.getToken())
                        .build();
                return chain.proceed(newRequest);
            }
        }
        
        // 刷新失败，清除登录状态
        tokenManager.clearTokens();
        
        // 发送广播
        Intent intent = new Intent("ACTION_TOKEN_EXPIRED");
        intent.setPackage(App.getContext().getPackageName());
        App.getContext().sendBroadcast(intent);
        
        return new Response.Builder()
                .code(401)
                .message("登录已过期")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .build();
    }
    
    private boolean refreshTokenSync() {
        // 同步刷新Token（实际项目中建议异步处理）
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] result = {false};
        
        AuthApi api = RetrofitManager.getInstance().create(AuthApi.class);
        api.refreshToken(new RefreshTokenRequest(tokenManager.getRefreshToken()))
                .enqueue(new Callback<BaseResponse<TokenResponse>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<TokenResponse>> call,
                                         Response<BaseResponse<TokenResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            TokenResponse data = response.body().getData();
                            tokenManager.saveToken(data.getToken());
                            tokenManager.saveRefreshToken(data.getRefreshToken());
                            tokenManager.saveTokenExpiresAt(data.getExpiresIn());
                            result[0] = true;
                        }
                        latch.countDown();
                    }
                    
                    @Override
                    public void onFailure(Call<BaseResponse<TokenResponse>> call, Throwable t) {
                        latch.countDown();
                    }
                });
        
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return result[0];
    }
}
```

### 6.4 密码加密实现

#### 6.4.1 密码加密工具
```java
public class PasswordUtils {
    
    private static final int SALT_LENGTH = 16;
    private static final int HASH_ITERATIONS = 10000;
    private static final int HASH_KEY_LENGTH = 256;
    
    /**
     * 生成随机盐
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.encodeToString(salt, Base64.NO_WRAP);
    }
    
    /**
     * 加密密码
     * 使用BCrypt或PBKDF2WithHmacSHA256
     */
    public static String encryptPassword(String password, String salt) {
        try {
            // 使用PBKDF2WithHmacSHA256
            KeySpec spec = new PBEKeySpec(password.toCharArray(), 
                                          Base64.decode(salt, Base64.NO_WRAP), 
                                          HASH_ITERATIONS, 
                                          HASH_KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            
            // 格式：salt:hash
            return salt + ":" + Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (Exception e) {
            throw new RuntimeException("密码加密失败", e);
        }
    }
    
    /**
     * 验证密码
     */
    public static boolean verifyPassword(String password, String storedPassword) {
        try {
            String[] parts = storedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            String salt = parts[0];
            String storedHash = parts[1];
            
            String computedHash = encryptPassword(password, salt);
            return computedHash.equals(storedPassword);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 检查密码强度
     */
    public static PasswordStrength checkStrength(String password) {
        int score = 0;
        List<String> feedback = new ArrayList<>();
        
        // 长度检测
        if (password.length() < 8) {
            feedback.add("密码长度至少8位");
        } else {
            score += 2;
        }
        
        if (password.length() >= 12) {
            score += 1;
        }
        
        // 大写字母
        if (password.matches(".*[A-Z].*")) {
            score += 1;
        } else {
            feedback.add("包含大写字母");
        }
        
        // 小写字母
        if (password.matches(".*[a-z].*")) {
            score += 1;
        } else {
            feedback.add("包含小写字母");
        }
        
        // 数字
        if (password.matches(".*\\d.*")) {
            score += 1;
        } else {
            feedback.add("包含数字");
        }
        
        // 特殊字符
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            score += 1;
        } else {
            feedback.add("包含特殊字符");
        }
        
        // 常见密码检测
        if (isCommonPassword(password)) {
            score = Math.max(0, score - 2);
            feedback.add("密码过于常见");
        }
        
        // 重复字符检测
        if (hasRepeatedChars(password)) {
            score -= 1;
            feedback.add("避免连续重复字符");
        }
        
        return new PasswordStrength(score, feedback);
    }
    
    private static boolean isCommonPassword(String password) {
        Set<String> commonPasswords = new HashSet<>(Arrays.asList(
                "password", "123456", "12345678", "qwerty", "abc123",
                "password123", "admin123", "letmein", "welcome", "monkey"
        ));
        return commonPasswords.contains(password.toLowerCase());
    }
    
    private static boolean hasRepeatedChars(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) && 
                password.charAt(i) == password.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }
}
```

## 7. 安全策略

### 7.1 数据加密

#### 7.1.1 传输加密
- 全程使用HTTPS
- 使用TLS 1.2+
- 证书绑定（Certificate Pinning）
- 敏感数据传输加密

#### 7.1.2 存储加密
- Token存储：使用EncryptedSharedPreferences
- 用户数据存储：Room数据库加密（SQLCipher）
- 本地缓存加密：AES-256加密

### 7.2 防护措施

#### 7.2.1 防暴力破解
- 登录失败次数限制：5次
- 验证码发送频率限制：60秒/次
- IP黑名单机制
- 图形验证码（连续失败）

#### 7.2.2 防SQL注入
- 使用参数化查询
- 输入校验和过滤
- ORM框架（Room）

#### 7.2.3 防XSS攻击
- 输出编码
- 输入校验
- Content Security Policy

#### 7.2.4 防重放攻击
- 请求签名
- 时间戳验证
- Nonce机制

### 7.3 安全审计

#### 7.3.1 日志记录
- 登录日志
- 操作日志
- 错误日志
- 安全事件日志

#### 7.3.2 监控告警
- 异常登录监控
- 频繁失败监控
- 敏感操作监控

## 8. 测试计划

### 8.1 单元测试

#### 8.1.1 测试范围
- 密码加密与验证
- Token管理
- 输入验证
- 业务逻辑

#### 8.1.2 测试用例
```java
public class PasswordUtilsTest {
    
    @Test
    public void testEncryptPassword() {
        String password = "Test123456!";
        String salt = PasswordUtils.generateSalt();
        String encrypted = PasswordUtils.encryptPassword(password, salt);
        
        assertNotNull(encrypted);
        assertTrue(encrypted.contains(":"));
    }
    
    @Test
    public void testVerifyPassword() {
        String password = "Test123456!";
        String salt = PasswordUtils.generateSalt();
        String encrypted = PasswordUtils.encryptPassword(password, salt);
        
        assertTrue(PasswordUtils.verifyPassword(password, encrypted));
        assertFalse(PasswordUtils.verifyPassword("WrongPassword", encrypted));
    }
    
    @Test
    public void testPasswordStrength() {
        assertEquals(Strength.VERY_STRONG, 
            PasswordUtils.checkStrength("Test123456!@#"));
        
        assertEquals(Strength.WEAK, 
            PasswordUtils.checkStrength("123456"));
        
        assertEquals(Strength.MEDIUM, 
            PasswordUtils.checkStrength("Test123"));
    }
}
```

### 8.2 集成测试

#### 8.2.1 API测试
- 正常流程测试
- 异常流程测试
- 并发测试
- 压力测试

#### 8.2.2 场景测试
- 注册流程测试
- 登录流程测试
- 找回密码流程测试
- Token刷新测试

### 8.3 UI测试

#### 8.3.1 Espresso测试
```java
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    
    @Rule
    public ActivityTestRule<LoginActivity> activityRule = 
            new ActivityTestRule<>(LoginActivity.class);
    
    @Test
    public void testLoginWithEmptyAccount() {
        onView(withId(R.id.btn_login)).perform(click());
        onView(withText("请输入账号")).check(matches(isDisplayed()));
    }
    
    @Test
    public void testLoginWithEmptyPassword() {
        onView(withId(R.id.et_account)).perform(typeText("13800138000"));
        onView(withId(R.id.btn_login)).perform(click());
        onView(withText("请输入密码")).check(matches(isDisplayed()));
    }
    
    @Test
    public void testLoginSuccess() {
        onView(withId(R.id.et_account)).perform(typeText("13800138000"));
        onView(withId(R.id.et_password)).perform(typeText("Test123456!"));
        onView(withId(R.id.btn_login)).perform(click());
        
        // 验证跳转到首页
        intended(hasComponent(HomeActivity.class));
    }
}
```

### 8.4 安全测试

#### 8.4.1 测试内容
- 渗透测试
- 漏洞扫描
- 代码审计
- 安全配置检查

## 9. 部署与发布

### 9.1 后端部署

#### 9.1.1 服务器要求
- CPU：4核+
- 内存：8GB+
- 硬盘：100GB+ SSD
- 带宽：10Mbps+

#### 9.1.2 部署步骤
1. 安装运行环境（Java、MySQL、Redis等）
2. 部署后端服务
3. 配置HTTPS证书
4. 配置监控告警
5. 性能调优

### 9.2 Android发布

#### 9.2.1 发布流程
1. 代码审查
2. 测试验证
3. 构建APK/AAB
4. 混淆签名
5. 应用商店提交

#### 9.2.2 版本规划
- v1.0：基础登录注册功能
- v1.1：第三方登录、安全增强
- v1.2：功能优化、性能提升

## 10. 项目里程碑与时间规划

### 10.1 阶段规划

| 阶段 | 时间 | 主要任务 | 交付物 |
|------|------|----------|--------|
| 第一阶段 | 第1-2周 | 数据库设计、API接口设计 | 数据库设计文档、API文档 |
| 第二阶段 | 第3-4周 | 后端开发、API实现 | 后端服务、API测试 |
| 第三阶段 | 第5-6周 | Android端开发、UI实现 | 登录注册功能 |
| 第四阶段 | 第7周 | 测试修复、Bug修复 | 稳定版本 |
| 第五阶段 | 第8周 | 部署上线、应用商店发布 | 正式版本 |

### 10.2 详细计划

#### 10.2.1 第一周：设计阶段
- **周一-周二**：需求分析、数据库设计
- **周三-周四**：API接口设计、文档编写
- **周五**：设计评审、技术选型确认

#### 10.2.2 第二周：后端开发（1）
- **周一-周二**：搭建后端框架、数据库实现
- **周三-周四**：注册、登录接口开发
- **周五**：验证码、Token接口开发

#### 10.2.3 第三周：后端开发（2）
- **周一-周二**：找回密码、修改密码接口
- **周三-周四**：用户信息接口、安全加固
- **周五**：后端测试、性能优化

#### 10.2.4 第四周：Android端开发（1）
- **周一-周二**：项目架构搭建、基础组件开发
- **周三-周四**：登录页面UI实现、ViewModel开发
- **周五**：注册页面UI实现

#### 10.2.5 第五周：Android端开发（2）
- **周一-周二**：找回密码页面、第三方登录
- **周三-周四**：Token管理、安全模块
- **周五**：集成测试、Bug修复

#### 10.2.6 第六周：完善与优化
- **周一-周三**：UI优化、性能优化
- **周四-周五**：全面测试、Bug修复

#### 10.2.3 第七周：测试与修复
- **周一-周三**：全面测试
- **周四-周五**：Bug修复、性能调优

#### 10.2.4 第八周：部署与发布
- **周一-周三**：后端部署、配置
- **周四**：Android应用打包、签名
- **周五**：应用商店提交、发布

## 11. 风险评估与应对策略

### 11.1 技术风险

| 风险 | 影响 | 可能性 | 应对策略 |
|------|------|--------|----------|
| 后端服务不稳定 | 用户无法登录 | 中 | 增加容错机制、本地缓存 |
| 网络请求失败 | 登录失败 | 中 | 重试机制、离线支持 |
| 安全漏洞 | 用户数据泄露 | 低 | 安全审计、渗透测试 |
| 性能瓶颈 | 响应慢 | 中 | 性能优化、负载均衡 |

### 11.2 运营风险

| 风险 | 影响 | 可能性 | 应对策略 |
|------|------|--------|----------|
| 用户接受度低 | 使用率低 | 中 | 用户调研、体验优化 |
| 竞争对手 | 用户流失 | 中 | 功能创新、体验提升 |
| 政策变化 | 功能受限 | 低 | 合规审查、政策跟踪 |

### 11.3 项目风险

| 风险 | 影响 | 可能性 | 应对策略 |
|------|------|--------|----------|
| 开发延期 | 上线延迟 | 中 | 进度管控、资源调配 |
| 需求变更 | 返工 | 中 | 需求管理、灵活架构 |
| 人员变动 | 开发受阻 | 低 | 知识共享、文档完善 |

## 12. 预算与资源需求

### 12.1 人力资源需求

| 角色 | 数量 | 投入时间 | 职责 |
|------|------|----------|------|
| 后端开发工程师 | 1 | 4周 | 后端开发、API实现 |
| Android开发工程师 | 1-2 | 4周 | Android端开发 |
| UI设计师 | 1 | 1周 | UI设计、切图 |
| 测试工程师 | 1 | 2周 | 功能测试、安全测试 |
| 产品经理 | 1 | 1周 | 需求分析、项目管理 |

### 12.2 技术资源需求

| 资源 | 数量 | 用途 | 预算 |
|------|------|------|------|
| 服务器 | 1台 | 后端服务部署 | 5,000元/年 |
| 数据库 | 1个 | 用户数据存储 | 3,000元/年 |
| 短信服务 | 按量 | 验证码发送 | 1,000元/年 |
| HTTPS证书 | 1个 | 安全传输 | 1,000元/年 |
| 第三方SDK | - | 微信登录等 | 免费 |

### 12.3 总预算

| 项目 | 预算 |
|------|------|
| 人力资源 | 80,000-120,000元 |
| 服务器和云服务 | 10,000元/年 |
| 短信服务 | 5,000元/年 |
| 其他 | 5,000元 |
| **总计** | **100,000-140,000元** |

## 📈 总结

本计划详细阐述了福师大校园生活服务APP登录注册功能的实现方案，涵盖了需求分析、技术架构、数据库设计、API接口、UI/UX设计、核心功能实现、安全策略、测试计划、部署发布、项目里程碑、风险评估和预算需求等各个方面。

通过本计划的实施，将实现以下目标：

1. **完善的用户认证体系**：支持手机号、学号、第三方等多种登录方式
2. **安全的用户数据保护**：采用加密存储、传输加密、防护机制等多重安全措施
3. **良好的用户体验**：流畅的交互设计、友好的错误提示、快速的响应速度
4. **可扩展的架构设计**：模块化设计、支持功能扩展和技术升级

本计划具有可操作性和灵活性，可根据实际情况进行调整和优化。通过严格的进度管控和质量保证，确保项目按时交付，达到预期效果。