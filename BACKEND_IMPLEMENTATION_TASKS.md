# JPetStore 后端 RESTful API 实现任务清单

## 🚨 当前状态

**API 设计**: ✅ 已完成（API_DOCUMENTATION.md）  
**实际实现**: ❌ 大部分未完成  
**现有代码**: ⚠️ 有骨架但业务逻辑未实现

---

## 🎯 核心问题

1. **CartApiController** - 业务逻辑全是 TODO
2. **Auth/Account Controller** - 是传统 MVC，不是 RESTful
3. **缺少多个 Controller** - Catalog、Order、Address
4. **缺少基础设施** - Result、异常处理、JWT

---

## 📝 详细任务清单

### 🔧 P0: 基础设施（成员 5 - 优先级最高）

#### 1. 创建统一响应格式类
**文件**: `src/main/java/com/csu/jpetstore/common/Result.java`

```java
package com.csu.jpetstore.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    
    // 成功响应
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }
    
    public static <T> Result<T> success() {
        return success(null);
    }
    
    // 失败响应
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
    
    public static <T> Result<T> error(String message) {
        return error(400, message);
    }
}
```

**任务**:
- [ ] 创建 Result.java
- [ ] 添加 Lombok 依赖（如果还没有）
- [ ] 测试序列化是否正常

---

#### 2. 创建全局异常处理器
**文件**: `src/main/java/com/csu/jpetstore/config/GlobalExceptionHandler.java`

```java
package com.csu.jpetstore.config;

import com.csu.jpetstore.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        return Result.error(500, "服务器内部错误: " + e.getMessage());
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return Result.error(400, e.getMessage());
    }
}
```

**任务**:
- [ ] 创建 GlobalExceptionHandler.java
- [ ] 测试异常是否被正确捕获
- [ ] 确保返回 JSON 格式

---

#### 3. 实现 JWT Token 认证

**3.1 添加 JWT 依赖**
在 `pom.xml` 中添加：
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

**3.2 创建 JWT 工具类**
**文件**: `src/main/java/com/csu/jpetstore/util/JwtUtil.java`

```java
package com.csu.jpetstore.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24小时
    
    // 生成 Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }
    
    // 验证 Token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    
    // 从 Token 获取用户名
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
```

**3.3 创建 JWT 拦截器**
**文件**: `src/main/java/com/csu/jpetstore/config/JwtInterceptor.java`

```java
package com.csu.jpetstore.config;

import com.csu.jpetstore.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // OPTIONS 请求直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        String token = request.getHeader("Authorization");
        
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                request.setAttribute("username", username);
                return true;
            }
        }
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
```

**3.4 配置拦截器**
**文件**: `src/main/java/com/csu/jpetstore/config/WebMvcConfig.java`

```java
package com.csu.jpetstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Autowired
    private JwtInterceptor jwtInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns(
                    "/api/v1/auth/login",
                    "/api/v1/auth/register",
                    "/api/v1/auth/send-code",
                    "/api/v1/auth/validate"
                );
    }
}
```

**任务**:
- [ ] 添加 JWT 依赖到 pom.xml
- [ ] 创建 JwtUtil.java
- [ ] 创建 JwtInterceptor.java
- [ ] 创建 WebMvcConfig.java
- [ ] 测试 Token 生成和验证

---

### 👤 成员 2: 认证模块重构

#### 4. 创建 AuthApiController（RESTful）
**文件**: `src/main/java/com/csu/jpetstore/controller/api/AuthApiController.java`

需要实现的接口：
```java
POST   /api/v1/auth/login           // 登录
POST   /api/v1/auth/register        // 注册
POST   /api/v1/auth/logout          // 登出
GET    /api/v1/auth/validate        // 验证用户名/邮箱
POST   /api/v1/auth/send-code       // 发送验证码
```

**关键改动**:
- [ ] 使用 `@RestController` 替代 `@Controller`
- [ ] 返回 `Result<T>` 统一格式
- [ ] 登录成功后生成 JWT Token
- [ ] 使用 `@RequestBody` 接收 JSON 参数
- [ ] 移除 Session 相关代码

**示例代码结构**:
```java
@RestController
@RequestMapping("/api/v1/auth")
public class AuthApiController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest request) {
        // 1. 验证用户名密码
        // 2. 生成 JWT Token
        // 3. 返回 Token 和用户信息
    }
}
```

**任务**:
- [ ] 创建 AuthApiController.java
- [ ] 实现登录接口（返回 JWT Token）
- [ ] 实现注册接口
- [ ] 实现登出接口（前端删除 Token 即可）
- [ ] 实现验证接口
- [ ] 实现发送验证码接口
- [ ] 测试所有接口

---

#### 5. 创建 AccountApiController（RESTful）
**文件**: `src/main/java/com/csu/jpetstore/controller/api/AccountApiController.java`

需要实现的接口：
```java
GET    /api/v1/account/profile      // 获取个人信息
PUT    /api/v1/account/profile      // 更新个人信息
PUT    /api/v1/account/password     // 修改密码
```

**关键改动**:
- [ ] 从 JWT Token 中获取用户名
- [ ] 返回 `Result<T>` 格式
- [ ] 使用 `@RequestBody` 接收参数

**任务**:
- [ ] 创建 AccountApiController.java
- [ ] 实现获取个人信息接口
- [ ] 实现更新个人信息接口
- [ ] 实现修改密码接口
- [ ] 测试所有接口

---

### 🛒 成员 1: 购物车模块完善

#### 6. 完善 CartApiController
**文件**: `src/main/java/com/csu/jpetstore/controller/api/CartApiController.java`（已存在，需重写）

当前问题：
- ❌ 所有业务逻辑都是 TODO
- ❌ 使用 Session 存储购物车
- ❌ 参数接收方式错误

需要重写的接口：
```java
GET    /api/v1/cart                 // 查看购物车
POST   /api/v1/cart/items           // 添加商品
PUT    /api/v1/cart/items/{itemId}  // 更新数量
DELETE /api/v1/cart/items/{itemId}  // 删除商品
DELETE /api/v1/cart                 // 清空购物车
```

**关键改动**:
- [ ] 从 JWT Token 获取用户名
- [ ] 调用 CartService 的真正方法
- [ ] 使用 `@RequestBody` 接收 JSON
- [ ] 返回 `Result<T>` 格式

**示例**:
```java
@PostMapping("/items")
public Result<Void> addItem(
        @RequestBody AddCartItemRequest request,
        HttpServletRequest httpRequest) {
    
    String username = (String) httpRequest.getAttribute("username");
    cartService.addCartItem(username, request.getItemId(), request.getQuantity());
    return Result.success();
}
```

**任务**:
- [ ] 检查 CartService 是否有完整的方法实现
- [ ] 重写所有接口方法
- [ ] 实现真正的业务逻辑
- [ ] 测试增删改查功能

---

### 📦 成员 4: 商品目录模块

#### 7. 创建 CatalogApiController
**文件**: `src/main/java/com/csu/jpetstore/controller/api/CatalogApiController.java`

需要实现的接口：
```java
GET    /api/v1/categories                           // 获取所有分类
GET    /api/v1/categories/{categoryId}              // 分类详情+商品列表
GET    /api/v1/categories/{id}/products-with-items  // 分类+商品+规格
GET    /api/v1/products/{productId}                 // 商品详情
GET    /api/v1/items/{itemId}                       // 规格项详情
GET    /api/v1/products/search                      // 搜索商品
GET    /api/v1/products/autocomplete                // 自动补全
```

**任务**:
- [ ] 创建 CatalogApiController.java
- [ ] 实现所有查询接口
- [ ] 确保返回格式统一
- [ ] 测试所有接口

---

### 📋 成员 3: 订单模块

#### 8. 创建 OrderApiController
**文件**: `src/main/java/com/csu/jpetstore/controller/api/OrderApiController.java`

需要实现的接口：
```java
POST   /api/v1/orders               // 创建订单
GET    /api/v1/orders/{orderId}     // 订单详情
GET    /api/v1/orders               // 订单列表（分页）
```

**任务**:
- [ ] 检查 OrderService 是否有完整实现
- [ ] 创建 OrderApiController.java
- [ ] 实现创建订单接口
- [ ] 实现订单详情接口
- [ ] 实现订单列表接口（支持分页）
- [ ] 测试所有接口

---

#### 9. 创建 AddressApiController
**文件**: `src/main/java/com/csu/jpetstore/controller/api/AddressApiController.java`

需要实现的接口：
```java
GET    /api/v1/addresses                    // 获取地址列表
POST   /api/v1/addresses                    // 添加地址
PUT    /api/v1/addresses/{addressId}        // 更新地址
DELETE /api/v1/addresses/{addressId}        // 删除地址
PUT    /api/v1/addresses/{id}/default       // 设置默认地址
```

**任务**:
- [ ] 检查 AddressService 是否有完整实现
- [ ] 创建 AddressApiController.java
- [ ] 实现所有地址管理接口
- [ ] 测试所有接口

---

## 🔄 实施步骤建议

### 第一阶段：基础设施（1天）
1. 成员 5 创建 Result.java
2. 成员 5 创建 GlobalExceptionHandler.java
3. 成员 5 实现 JWT 工具类和拦截器
4. 测试基础设施是否正常工作

### 第二阶段：核心 API（2天）
1. 成员 2 重构 AuthApiController（登录注册）
2. 成员 1 完善 CartApiController（购物车）
3. 成员 4 创建 CatalogApiController（商品）
4. 联调测试基础功能

### 第三阶段：业务 API（1天）
1. 成员 3 创建 OrderApiController（订单）
2. 成员 3 创建 AddressApiController（地址）
3. 成员 2 创建 AccountApiController（账户）
4. 全面联调测试

### 第四阶段：优化与文档（1天）
1. 修复 Bug
2. 完善错误处理
3. 更新 API 文档
4. 性能优化

---

## ✅ 验收标准

### 代码质量
- [ ] 所有 Controller 使用 `@RestController`
- [ ] 所有接口返回 `Result<T>` 格式
- [ ] 使用 JWT Token 认证（不用 Session）
- [ ] 使用 `@RequestBody` 接收 JSON 参数
- [ ] 统一的异常处理

### 功能完整性
- [ ] 所有 API 接口已实现
- [ ] 业务逻辑正确
- [ ] 数据库操作正常
- [ ] 无 TODO 注释

### 测试
- [ ] 每个接口都能正常调用
- [ ] 返回数据格式正确
- [ ] 错误处理完善
- [ ] 前后端联调通过

---

## 📊 进度跟踪

| 模块 | 负责人 | 状态 | 进度 |
|------|--------|------|------|
| 基础设施 | 成员 5 | ⏳ 待开始 | 0% |
| 认证模块 | 成员 2 | ⏳ 待开始 | 0% |
| 账户模块 | 成员 2 | ⏳ 待开始 | 0% |
| 购物车模块 | 成员 1 | ⏳ 待开始 | 0% |
| 商品模块 | 成员 4 | ⏳ 待开始 | 0% |
| 订单模块 | 成员 3 | ⏳ 待开始 | 0% |
| 地址模块 | 成员 3 | ⏳ 待开始 | 0% |

**总体进度**: 0%

---

**创建时间**: 2026-05-19  
**最后更新**: 2026-05-19
