# JPetStore 前后端分离重构 - 任务看板

## 📊 项目状态总览

**前端完成度**: 95% ✅  
**后端完成度**: 需要检查确认 ⚠️  
**联调测试**: 待进行 🔄

---

## ✅ 已完成工作清单

### 前端（100% 完成）

#### 页面组件（10/10）
- [x] Home.vue - 首页
- [x] Category.vue - 商品分类页
- [x] Product.vue - 商品详情页
- [x] Cart.vue - 购物车页
- [x] Checkout.vue - 订单结算页
- [x] Login.vue - 登录页
- [x] Register.vue - 注册页
- [x] Profile.vue - 个人中心
- [x] Search.vue - 搜索结果页
- [x] OrderDetail.vue - 订单详情页

#### API 模块（6/6）
- [x] api/auth.js - 认证 API
- [x] api/account.js - 账户 API
- [x] api/product.js - 商品 API
- [x] api/cart.js - 购物车 API
- [x] api/order.js - 订单 API
- [x] api/address.js - 地址 API

#### 公共组件（2/2）
- [x] Header.vue - 顶部导航
- [x] Footer.vue - 底部信息

#### 配置与状态管理
- [x] router/index.js - 路由配置（11个路由）
- [x] stores/cart.js - Pinia 购物车状态
- [x] api/request.js - Axios 封装与拦截器
- [x] vite.config.js - Vite 配置（含 @ 别名）
- [x] main.js - 应用入口

#### 文档
- [x] FRONTEND_README.md - 前端项目文档
- [x] QUICK_START.md - 快速启动指南
- [x] FRONTEND_COMPLETION_SUMMARY.md - 完成总结

---

### 后端（需要检查确认）

#### 已有文件（根据项目结构）
- [x] Controller 层（传统 MVC）
- [x] Service 层
- [x] Mapper 层
- [x] CORS 配置（CorsConfig.java）
- [x] API_DOCUMENTATION.md（RESTful API 文档）

#### 需要确认的文件
- [ ] CartApiController.java - RESTful API
- [ ] AuthApiController.java - 认证 API
- [ ] AccountApiController.java - 账户 API
- [ ] CatalogApiController.java - 商品 API
- [ ] OrderApiController.java - 订单 API
- [ ] Result.java - 统一响应格式
- [ ] GlobalExceptionHandler.java - 全局异常处理
- [ ] JWT 工具类与拦截器

---

## 🎯 5人团队分工明细

### 👤 成员 1：购物车模块（你）

#### 后端任务
- [ ] **检查现有代码**
  - 查看是否有 CartApiController.java
  - 如果没有，创建 RESTful API Controller
  
- [ ] **实现/验证以下接口**
  ```java
  GET    /api/v1/cart              // 查看购物车
  POST   /api/v1/cart/items        // 添加商品
  PUT    /api/v1/cart/items/{id}   // 更新数量
  DELETE /api/v1/cart/items/{id}   // 删除商品
  DELETE /api/v1/cart              // 清空购物车
  ```

- [ ] **确保返回统一格式**
  ```json
  {
    "code": 200,
    "message": "success",
    "data": { ... }
  }
  ```

#### 前端任务
- [x] views/Cart.vue - ✅ 已完成
- [x] api/cart.js - ✅ 已完成
- [x] stores/cart.js - ✅ 已完成
- [ ] **联调测试**
  - 测试添加商品到购物车
  - 测试修改数量
  - 测试删除商品
  - 测试清空购物车
  - 测试未登录状态提示

#### 验收标准
- [ ] 购物车增删改查功能正常
- [ ] 数据与后端同步
- [ ] 错误处理完善
- [ ] UI 交互流畅

---

### 👤 成员 2：账户/登录模块

#### 后端任务
- [ ] **创建 AuthApiController.java**
  ```java
  POST /api/v1/auth/login           // 登录
  POST /api/v1/auth/register        // 注册
  POST /api/v1/auth/logout          // 登出
  GET  /api/v1/auth/validate        // 验证用户名/邮箱
  POST /api/v1/auth/send-code       // 发送验证码
  ```

- [ ] **实现 JWT Token 认证**
  - 创建 JwtUtil.java（生成/验证 Token）
  - 创建 JwtInterceptor.java（拦截器）
  - 配置拦截器到 WebMvcConfigurer

- [ ] **创建 AccountApiController.java**
  ```java
  GET  /api/v1/account/profile      // 获取个人信息
  PUT  /api/v1/account/profile      // 更新个人信息
  PUT  /api/v1/account/password     // 修改密码
  ```

#### 前端任务
- [x] views/Register.vue - ✅ 已完成
- [x] views/Profile.vue - ✅ 已完成
- [x] views/Login.vue - ✅ 已完成
- [x] 路由守卫已配置（requiresAuth）
- [ ] **联调测试**
  - 测试用户注册
  - 测试用户登录
  - 测试 Token 存储
  - 测试路由守卫
  - 测试个人信息获取/更新
  - 测试密码修改

#### 验收标准
- [ ] 登录注册功能正常
- [ ] JWT Token 正确生成和验证
- [ ] 受保护路由需要登录
- [ ] 个人信息 CRUD 正常

---

### 👤 成员 3：订单模块

#### 后端任务
- [ ] **创建 OrderApiController.java**
  ```java
  POST /api/v1/orders               // 创建订单
  GET  /api/v1/orders/{orderId}     // 订单详情
  GET  /api/v1/orders               // 订单列表（分页）
  ```

- [ ] **完善 OrderService**
  - 从购物车创建订单
  - 扣减库存
  - 计算总价
  - 订单状态管理

#### 前端任务
- [x] views/Checkout.vue - ✅ 订单确认页已完成
- [x] views/OrderDetail.vue - ✅ 订单详情已完成
- [ ] **完善订单列表**
  - 选项1：在 Profile.vue 中已实现
  - 选项2：创建独立的 views/OrderList.vue
  
- [x] api/order.js - ✅ 已完成
- [ ] **联调测试**
  - 测试订单创建流程
  - 测试订单详情查看
  - 测试订单列表查询
  - 测试分页功能

#### 验收标准
- [ ] 订单创建成功并清空购物车
- [ ] 订单详情显示完整
- [ ] 订单列表支持分页
- [ ] 订单状态正确显示

---

### 👤 成员 4：商品目录模块

#### 后端任务
- [ ] **创建 CatalogApiController.java**
  ```java
  GET /api/v1/categories                    // 获取所有分类
  GET /api/v1/categories/{id}               // 分类详情
  GET /api/v1/categories/{id}/products-with-items  // 分类+商品+规格
  GET /api/v1/products/{productId}          // 商品详情
  GET /api/v1/items/{itemId}                // 规格项详情
  GET /api/v1/products/search               // 搜索商品
  GET /api/v1/products/autocomplete         // 自动补全
  ```

#### 前端任务
- [x] views/Category.vue - ✅ 已完成
- [x] views/Product.vue - ✅ 已完成
- [x] views/Search.vue - ✅ 已完成
- [x] api/product.js - ✅ 已完成
- [ ] **联调测试**
  - 测试分类列表加载
  - 测试商品详情展示
  - 测试规格选择
  - 测试搜索功能
  - 测试自动补全（如果实现）

#### 验收标准
- [ ] 分类和商品数据正确加载
- [ ] 商品详情完整展示
- [ ] 搜索功能正常
- [ ] 图片加载有占位符

---

### 👤 成员 5：公共配置 & 支持

#### 后端任务
- [ ] **创建 Result.java**（如果不存在）
  ```java
  public class Result<T> {
      private Integer code;
      private String message;
      private T data;
      
      // 静态方法：success(), error()
  }
  ```

- [ ] **创建 GlobalExceptionHandler.java**
  ```java
  @RestControllerAdvice
  public class GlobalExceptionHandler {
      @ExceptionHandler(Exception.class)
      public Result<?> handleException(Exception e) { ... }
  }
  ```

- [ ] **JWT 工具类配置**
  - JwtUtil.java - Token 生成/验证
  - JwtInterceptor.java - 请求拦截
  - 配置到 WebMvcConfigurer

- [ ] **统一所有 Controller 返回格式**
  - 确保都使用 Result 类
  - 确保 HTTP 状态码正确

#### 前端任务
- [x] components/Footer.vue - ✅ 已完成
- [x] components/Header.vue - ✅ 已完成
- [ ] **Vite 代理配置**（可选）
  ```javascript
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
  ```

- [ ] **创建 ProductCard 组件**（可选优化）
  - 提取商品卡片为可复用组件
  - 在首页、分类页、搜索页中使用

- [x] API 文档 - ✅ API_DOCUMENTATION.md 已存在
- [ ] **编写接口对接文档**
  - 前端如何调用每个接口
  - 参数说明
  - 返回值说明

#### 验收标准
- [ ] 统一响应格式正常工作
- [ ] 全局异常捕获正常
- [ ] JWT 拦截器正确验证 Token
- [ ] 跨域配置正确
- [ ] 文档完整清晰

---

## 🔄 联调测试计划

### 第一阶段：基础功能测试
1. [ ] 用户注册
2. [ ] 用户登录
3. [ ] Token 验证
4. [ ] 浏览商品分类
5. [ ] 查看商品详情

### 第二阶段：核心业务流程
1. [ ] 添加商品到购物车
2. [ ] 修改购物车数量
3. [ ] 结算下单
4. [ ] 查看订单详情
5. [ ] 查看订单列表

### 第三阶段：辅助功能
1. [ ] 搜索商品
2. [ ] 编辑个人资料
3. [ ] 管理收货地址
4. [ ] 修改密码
5. [ ] 退出登录

---

## 📅 建议时间安排

| 阶段 | 时间 | 任务 |
|------|------|------|
| Day 1 | 上午 | 后端 API 检查与补充 |
| Day 1 | 下午 | 前端联调测试（成员1-4） |
| Day 2 | 上午 | 公共配置完善（成员5） |
| Day 2 | 下午 | 全面测试与 Bug 修复 |
| Day 3 | 全天 | 优化与文档完善 |

---

## 🎯 下一步行动

### 立即执行（优先级 P0）
1. **成员 5**：检查并创建 Result.java 和 GlobalExceptionHandler.java
2. **成员 2**：实现 JWT Token 认证机制
3. **所有人**：检查各自的 Controller 是否存在，是否符合 RESTful 规范

### 短期任务（优先级 P1）
1. **所有人**：联调测试各自模块
2. **成员 5**：配置 JWT 拦截器
3. **成员 1-4**：修复发现的 Bug

### 中期任务（优先级 P2）
1. **所有人**：优化用户体验
2. **成员 5**：完善文档
3. **团队**：集成测试

---

## 📝 注意事项

### 后端开发注意
1. 所有 Controller 必须放在 `controller.api` 包下
2. 使用 `@RestController` 而非 `@Controller`
3. 返回 `Result<T>` 统一格式
4. 使用 `@RequestBody` 接收 JSON 参数
5. 需要登录的接口添加 Token 验证

### 前端开发注意
1. 所有 API 调用使用 try-catch 包裹
2. 统一的错误提示（ElMessage）
3. Loading 状态管理
4. 表单验证规则完善
5. 响应式布局测试

### 协作注意
1. 前后端接口字段名保持一致
2. 及时沟通接口变更
3. 使用 API_DOCUMENTATION.md 作为契约
4. Git 提交信息清晰
5. 定期同步进度

---

## ✅ 完成检查清单

### 后端
- [ ] 所有 RESTful API 已实现
- [ ] JWT 认证正常工作
- [ ] 统一响应格式
- [ ] 全局异常处理
- [ ] CORS 配置正确
- [ ] 数据库操作正常

### 前端
- [ ] 所有页面可访问
- [ ] API 调用正常
- [ ] 路由守卫生效
- [ ] 状态管理正确
- [ ] UI 交互流畅
- [ ] 响应式布局正常

### 整体
- [ ] 完整购物流程走通
- [ ] 无严重 Bug
- [ ] 性能可接受
- [ ] 文档完整
- [ ] 代码规范

---

**最后更新**: 2026-05-19  
**项目状态**: 前端完成 95%，后端待确认，联调待进行
