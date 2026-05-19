# JPetStore RESTful API 文档

> **版本**: v1.0  
> **基础路径**: `/api/v1`  
> **协议**: HTTP/HTTPS  
> **数据格式**: JSON  

---

## 📋 目录

- [1. 认证模块 (Authentication)](#1-认证模块)
- [2. 商品目录模块 (Catalog)](#2-商品目录模块)
- [3. 购物车模块 (Cart)](#3-购物车模块)
- [4. 订单模块 (Order)](#4-订单模块)
- [5. 账户管理模块 (Account)](#5-账户管理模块)
- [6. 地址管理模块 (Address)](#6-地址管理模块)
- [通用响应格式](#通用响应格式)
- [错误码说明](#错误码说明)
- [认证方式](#认证方式)

---

## 通用响应格式

所有 API 接口统一返回以下 JSON 格式：

### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 失败响应
```json
{
  "code": 400,
  "message": "错误描述信息",
  "data": null
}
```

### 分页响应
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [...],
    "total": 100,
    "page": 1,
    "pageSize": 10
  }
}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（需要登录） |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 认证方式

大部分接口需要在请求头中携带 Token：

```
Authorization: Bearer <token>
```

---

## 1. 认证模块

### 1.1 用户登录

**接口**: `POST /api/v1/auth/login`

**描述**: 使用用户名和密码登录

**请求体**:
```json
{
  "username": "j2ee",
  "password": "j2ee"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "username": "j2ee",
      "firstName": "ABC",
      "lastName": "XYX",
      "email": "yourname@yourdomain.com"
    }
  }
}
```

---

### 1.2 邮箱验证码登录

**接口**: `POST /api/v1/auth/login/email`

**描述**: 使用邮箱和验证码登录

**请求体**:
```json
{
  "email": "user@example.com",
  "code": "123456"
}
```

**响应**: 同 1.1

---

### 1.3 发送邮箱验证码

**接口**: `POST /api/v1/auth/send-code`

**描述**: 向指定邮箱发送验证码

**请求体**:
```json
{
  "email": "user@example.com"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "验证码已发送",
  "data": null
}
```

---

### 1.4 用户登出

**接口**: `POST /api/v1/auth/logout`

**描述**: 退出登录，使 Token 失效

**请求头**: 需要 Authorization

**响应**:
```json
{
  "code": 200,
  "message": "登出成功",
  "data": null
}
```

---

### 1.5 用户注册

**接口**: `POST /api/v1/auth/register`

**描述**: 注册新用户

**请求体**:
```json
{
  "username": "newuser",
  "password": "password123",
  "confirmPassword": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "address1": "123 Main St",
  "city": "New York",
  "state": "NY",
  "zip": "10001",
  "country": "USA"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": null
}
```

---

### 1.6 验证用户名/邮箱可用性

**接口**: `GET /api/v1/auth/validate`

**描述**: AJAX 验证用户名或邮箱是否已被注册

**查询参数**:
- `username` (可选): 要验证的用户名
- `email` (可选): 要验证的邮箱

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "available": true,
    "type": "username"
  }
}
```

---

## 2. 商品目录模块

### 2.1 获取所有分类

**接口**: `GET /api/v1/categories`

**描述**: 获取所有商品分类列表

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "categoryId": "FISH",
      "name": "Fish",
      "description": "Fish Products"
    },
    {
      "categoryId": "DOGS",
      "name": "Dogs",
      "description": "Dog Products"
    }
  ]
}
```

---

### 2.2 获取分类详情及商品列表

**接口**: `GET /api/v1/categories/{categoryId}`

**描述**: 获取指定分类的详细信息和该分类下的所有商品

**路径参数**:
- `categoryId`: 分类ID

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "category": {
      "categoryId": "FISH",
      "name": "Fish",
      "description": "Fish Products"
    },
    "products": [
      {
        "productId": "FI-SW-01",
        "name": "Angelfish",
        "description": "Salt Water fish from Australia",
        "categoryId": "FISH"
      }
    ]
  }
}
```

---

### 2.3 获取商品详情

**接口**: `GET /api/v1/products/{productId}`

**描述**: 获取指定商品的详细信息和所有规格项

**路径参数**:
- `productId`: 商品ID

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "product": {
      "productId": "FI-SW-01",
      "name": "Angelfish",
      "description": "Salt Water fish from Australia",
      "categoryId": "FISH"
    },
    "items": [
      {
        "itemId": "EST-1",
        "productId": "FI-SW-01",
        "listPrice": 16.50,
        "unitCost": 10.00,
        "supplierId": 1,
        "status": "P",
        "attribute1": "Large",
        "stock": 100
      }
    ]
  }
}
```

---

### 2.4 获取商品规格项详情

**接口**: `GET /api/v1/items/{itemId}`

**描述**: 获取指定规格项的详细信息

**路径参数**:
- `itemId`: 规格项ID

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "itemId": "EST-1",
    "productId": "FI-SW-01",
    "listPrice": 16.50,
    "unitCost": 10.00,
    "supplierId": 1,
    "status": "P",
    "attribute1": "Large",
    "stock": 100,
    "product": {
      "productId": "FI-SW-01",
      "name": "Angelfish"
    }
  }
}
```

---

### 2.5 搜索商品

**接口**: `GET /api/v1/products/search`

**描述**: 根据关键字搜索商品

**查询参数**:
- `keyword` (必填): 搜索关键字

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "productId": "FI-SW-01",
      "name": "Angelfish",
      "description": "Salt Water fish from Australia"
    }
  ]
}
```

---

### 2.6 商品自动补全

**接口**: `GET /api/v1/products/autocomplete`

**描述**: 输入提示，用于搜索框自动补全

**查询参数**:
- `keyword` (必填): 输入的关键字

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "productId": "FI-SW-01",
      "name": "Angelfish"
    },
    {
      "productId": "FI-SW-02",
      "name": "Tiger Shark"
    }
  ]
}
```

---

### 2.7 获取分类下商品及规格（含库存）

**接口**: `GET /api/v1/categories/{categoryId}/products-with-items`

**描述**: 获取指定分类下的所有商品及其规格项信息（用于前端展示）

**路径参数**:
- `categoryId`: 分类ID

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "productId": "FI-SW-01",
      "name": "Angelfish",
      "items": [
        {
          "itemId": "EST-1",
          "listPrice": 16.50,
          "stock": 100
        }
      ]
    }
  ]
}
```

---

## 3. 购物车模块

### 3.1 查看购物车

**接口**: `GET /api/v1/cart`

**描述**: 获取当前用户的购物车内容

**请求头**: 需要 Authorization

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "cartItems": [
      {
        "itemId": "EST-1",
        "productId": "FI-SW-01",
        "productName": "Angelfish",
        "quantity": 2,
        "listPrice": 16.50,
        "total": 33.00,
        "inStock": true
      }
    ],
    "subTotal": 33.00
  }
}
```

---

### 3.2 添加商品到购物车

**接口**: `POST /api/v1/cart/items`

**描述**: 将指定规格的商品添加到购物车

**请求头**: 需要 Authorization

**请求体**:
```json
{
  "itemId": "EST-1",
  "quantity": 1
}
```

**响应**:
```json
{
  "code": 200,
  "message": "添加成功",
  "data": {
    "subTotal": 16.50
  }
}
```

---

### 3.3 更新购物车商品数量

**接口**: `PUT /api/v1/cart/items/{itemId}`

**描述**: 更新购物车中指定商品的数量

**请求头**: 需要 Authorization

**路径参数**:
- `itemId`: 规格项ID

**请求体**:
```json
{
  "quantity": 3
}
```

**响应**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "itemTotal": 49.50,
    "subTotal": 49.50
  }
}
```

---

### 3.4 删除购物车商品

**接口**: `DELETE /api/v1/cart/items/{itemId}`

**描述**: 从购物车中删除指定商品

**请求头**: 需要 Authorization

**路径参数**:
- `itemId`: 规格项ID

**响应**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": {
    "subTotal": 0.00
  }
}
```

---

### 3.5 清空购物车

**接口**: `DELETE /api/v1/cart`

**描述**: 清空当前用户的所有购物车商品

**请求头**: 需要 Authorization

**响应**:
```json
{
  "code": 200,
  "message": "购物车已清空",
  "data": null
}
```

---

## 4. 订单模块

### 4.1 创建订单（结算）

**接口**: `POST /api/v1/orders`

**描述**: 根据购物车内容创建新订单

**请求头**: 需要 Authorization

**请求体**:
```json
{
  "billToFirstName": "John",
  "billToLastName": "Doe",
  "billAddress1": "123 Main St",
  "billAddress2": "Apt 4B",
  "billCity": "New York",
  "billState": "NY",
  "billZip": "10001",
  "billCountry": "USA",
  "shipToFirstName": "John",
  "shipToLastName": "Doe",
  "shipAddress1": "123 Main St",
  "shipAddress2": "Apt 4B",
  "shipCity": "New York",
  "shipState": "NY",
  "shipZip": "10001",
  "shipCountry": "USA",
  "creditCard": "4111111111111111",
  "expiryDate": "12/2025",
  "cardType": "Visa",
  "courier": "UPS"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": {
    "orderId": 1001,
    "orderDate": "2024-01-15T10:30:00Z",
    "totalPrice": 165.00,
    "status": "P"
  }
}
```

---

### 4.2 获取订单详情

**接口**: `GET /api/v1/orders/{orderId}`

**描述**: 获取指定订单的详细信息

**请求头**: 需要 Authorization

**路径参数**:
- `orderId`: 订单ID

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "orderId": 1001,
    "userId": "j2ee",
    "orderDate": "2024-01-15T10:30:00Z",
    "totalPrice": 165.00,
    "status": "P",
    "billAddress": {
      "firstName": "John",
      "lastName": "Doe",
      "address1": "123 Main St",
      "city": "New York",
      "state": "NY",
      "zip": "10001",
      "country": "USA"
    },
    "lineItems": [
      {
        "lineItemId": 1,
        "itemId": "EST-1",
        "quantity": 10,
        "unitPrice": 16.50,
        "total": 165.00
      }
    ]
  }
}
```

---

### 4.3 获取用户订单列表

**接口**: `GET /api/v1/orders`

**描述**: 获取当前用户的所有订单列表

**请求头**: 需要 Authorization

**查询参数**:
- `page` (可选): 页码，默认 1
- `pageSize` (可选): 每页数量，默认 10

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "orderId": 1001,
        "orderDate": "2024-01-15T10:30:00Z",
        "totalPrice": 165.00,
        "status": "P"
      }
    ],
    "total": 5,
    "page": 1,
    "pageSize": 10
  }
}
```

---

## 5. 账户管理模块

### 5.1 获取个人信息

**接口**: `GET /api/v1/account/profile`

**描述**: 获取当前登录用户的个人信息

**请求头**: 需要 Authorization

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "username": "j2ee",
    "firstName": "ABC",
    "lastName": "XYX",
    "email": "yourname@yourdomain.com",
    "phone": "555-555-5555",
    "address1": "901 San Antonio Road",
    "address2": "MS UCUP02-206",
    "city": "Palo Alto",
    "state": "CA",
    "zip": "94303",
    "country": "USA",
    "languagePreference": "english",
    "favouriteCategoryId": "DOGS",
    "listOption": true,
    "bannerOption": true
  }
}
```

---

### 5.2 更新个人信息

**接口**: `PUT /api/v1/account/profile`

**描述**: 更新当前用户的个人信息

**请求头**: 需要 Authorization

**请求体**:
```json
{
  "firstName": "ABC",
  "lastName": "XYX",
  "email": "newemail@example.com",
  "phone": "555-555-5555",
  "address1": "901 San Antonio Road",
  "address2": "MS UCUP02-206",
  "city": "Palo Alto",
  "state": "CA",
  "zip": "94303",
  "country": "USA",
  "languagePreference": "english",
  "favouriteCategoryId": "DOGS",
  "listOption": true,
  "bannerOption": true
}
```

**响应**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

---

### 5.3 修改密码

**接口**: `PUT /api/v1/account/password`

**描述**: 修改当前用户密码

**请求头**: 需要 Authorization

**请求体**:
```json
{
  "oldPassword": "oldpass",
  "newPassword": "newpass",
  "confirmPassword": "newpass"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "密码修改成功",
  "data": null
}
```

---

## 6. 地址管理模块

### 6.1 获取地址列表

**接口**: `GET /api/v1/addresses`

**描述**: 获取当前用户的所有收货地址

**请求头**: 需要 Authorization

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "addressId": 1,
      "firstName": "John",
      "lastName": "Doe",
      "phone": "1234567890",
      "address1": "123 Main St",
      "address2": "Apt 4B",
      "city": "New York",
      "state": "NY",
      "zip": "10001",
      "country": "USA",
      "isDefault": true
    }
  ]
}
```

---

### 6.2 添加地址

**接口**: `POST /api/v1/addresses`

**描述**: 添加新的收货地址

**请求头**: 需要 Authorization

**请求体**:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "phone": "1234567890",
  "address1": "123 Main St",
  "address2": "Apt 4B",
  "city": "New York",
  "state": "NY",
  "zip": "10001",
  "country": "USA",
  "isDefault": false
}
```

**响应**:
```json
{
  "code": 200,
  "message": "地址添加成功",
  "data": {
    "addressId": 2
  }
}
```

---

### 6.3 更新地址

**接口**: `PUT /api/v1/addresses/{addressId}`

**描述**: 更新指定地址信息

**请求头**: 需要 Authorization

**路径参数**:
- `addressId`: 地址ID

**请求体**: 同 6.2

**响应**:
```json
{
  "code": 200,
  "message": "地址更新成功",
  "data": null
}
```

---

### 6.4 删除地址

**接口**: `DELETE /api/v1/addresses/{addressId}`

**描述**: 删除指定地址

**请求头**: 需要 Authorization

**路径参数**:
- `addressId`: 地址ID

**响应**:
```json
{
  "code": 200,
  "message": "地址删除成功",
  "data": null
}
```

---

### 6.5 设置默认地址

**接口**: `PUT /api/v1/addresses/{addressId}/default`

**描述**: 将指定地址设置为默认地址

**请求头**: 需要 Authorization

**路径参数**:
- `addressId`: 地址ID

**响应**:
```json
{
  "code": 200,
  "message": "设置成功",
  "data": null
}
```

---

## 附录

### A. 数据字典

#### 订单状态 (status)
- `P`: Pending (待处理)
- `S`: Shipped (已发货)
- `C`: Completed (已完成)
- `X`: Cancelled (已取消)

#### 商品状态 (item.status)
- `P`: Available (可售)
- `O`: Out of Stock (缺货)

---

### B. RESTful 设计规范说明

本项目 API 遵循以下 RESTful 设计原则：

1. **资源命名**: 使用名词复数形式表示资源集合
   - ✅ `/api/v1/products` 
   - ❌ `/api/v1/getProducts`

2. **HTTP 方法语义**:
   - `GET`: 获取资源（安全、幂等）
   - `POST`: 创建资源
   - `PUT`: 完整更新资源
   - `PATCH`: 部分更新资源
   - `DELETE`: 删除资源

3. **版本控制**: URL 中包含版本号 `/api/v1/`

4. **统一响应格式**: 所有接口返回统一的 JSON 结构

5. **状态码使用**:
   - 2xx: 成功
   - 4xx: 客户端错误
   - 5xx: 服务器错误

6. **分页支持**: 列表接口支持 `page` 和 `pageSize` 参数

---

### C. 后续优化建议

1. **添加 JWT Token 认证机制**
2. **实现 API 限流**
3. **添加 API 文档自动生成工具（Swagger/OpenAPI）**
4. **实现 GraphQL 接口作为补充**
5. **添加 WebSocket 支持实时通知**

---

**文档版本**: v1.0  
**最后更新**: 2024-01-15  
**维护者**: JPetStore Team
