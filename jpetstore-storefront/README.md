# JPetStore 前台商城 - 前端项目

> 基于 Vue 3 + Vite + Element Plus 的现代化电商前端项目

## 📦 技术栈

- **框架**: Vue 3 (Composition API)
- **构建工具**: Vite
- **UI 组件库**: Element Plus
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **HTTP 客户端**: Axios

## 🚀 快速开始

### 1. 安装依赖

```bash
cd jpetstore-storefront
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

访问: http://localhost:5173

### 3. 构建生产版本

```bash
npm run build
```

## 📁 项目结构

```
jpetstore-storefront/
├── src/
│   ├── api/                    # API 接口
│   │   ├── request.js         # Axios 封装
│   │   ├── product.js         # 商品相关 API
│   │   ├── cart.js            # 购物车 API
│   │   ├── auth.js            # 认证 API
│   │   └── account.js         # 账户 API
│   ├── components/            # 公共组件
│   │   ├── Header.vue         # 头部导航
│   │   └── Footer.vue         # 底部信息
│   ├── views/                 # 页面组件
│   │   ├── Home.vue           # 首页
│   │   ├── Category.vue       # 分类页
│   │   ├── Product.vue        # 商品详情
│   │   ├── Cart.vue           # 购物车
│   │   ├── Login.vue          # 登录
│   │   ├── Register.vue       # 注册
│   │   └── Profile.vue        # 个人中心
│   ├── router/                # 路由配置
│   │   ── index.js
│   ├── stores/                # 状态管理
│   │   ── cart.js            # 购物车状态
│   ├── App.vue                # 根组件
│   └── main.js                # 入口文件
├── index.html
├── package.json
└── vite.config.js
```

## 🔧 配置说明

### API 地址配置

在 `src/api/request.js` 中修改后端 API 地址：

```javascript
const request = axios.create({
  baseURL: 'http://localhost:8080/api/v1', // 修改为你的后端地址
  timeout: 10000
})
```

### 跨域配置（开发环境）

在 `vite.config.js` 中添加代理配置：

```javascript
export default defineConfig({
  // ...
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

## 📝 开发指南

### 添加新页面

1. 在 `views/` 下创建页面组件
2. 在 `router/index.js` 中添加路由配置
3. 在导航菜单中添加链接

### 添加新 API

1. 在 `api/` 下创建 API 文件
2. 使用 `request` 实例调用接口
3. 在组件中引入并使用

### 状态管理

使用 Pinia 管理全局状态：

```javascript
import { useCartStore } from '../stores/cart'

const cartStore = useCartStore()
cartStore.fetchCart()
```

## 🎨 UI 组件使用

本项目使用 Element Plus 组件库：

```vue
<template>
  <el-button type="primary">按钮</el-button>
  <el-input v-model="value" placeholder="请输入"></el-input>
  <el-table :data="tableData">
    <!-- ... -->
  </el-table>
</template>
```

详细文档: https://element-plus.org/

## 🔐 认证机制

### Token 存储

- Token 存储在 `localStorage`
- 请求自动携带 Authorization header
- 401 错误自动跳转登录页

### 路由守卫

需要登录的页面在路由配置中添加：

```javascript
{
  path: '/profile',
  meta: { requiresAuth: true }
}
```

## 🌐 后端 API 要求

后端需要提供 RESTful API，遵循以下规范：

### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 认证接口

- `POST /api/v1/auth/login` - 登录
- `POST /api/v1/auth/register` - 注册
- `POST /api/v1/auth/logout` - 登出

### 商品接口

- `GET /api/v1/categories` - 获取分类列表
- `GET /api/v1/products/{id}` - 获取商品详情
- `GET /api/v1/products/search?keyword=xxx` - 搜索商品

### 购物车接口

- `GET /api/v1/cart` - 获取购物车
- `POST /api/v1/cart/items` - 添加商品
- `PUT /api/v1/cart/items/{id}` - 更新数量
- `DELETE /api/v1/cart/items/{id}` - 删除商品

详细 API 文档请参考项目根目录的 `API_DOCUMENTATION.md`

## 🚀 部署

### 开发环境

```bash
npm run dev
```

### 生产环境

```bash
# 构建
npm run build

# 部署 dist 目录到 Nginx/Apache
```

### Nginx 配置示例

```nginx
server {
    listen 80;
    server_name yourdomain.com;
    
    root /var/www/jpetstore-frontend/dist;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # API 代理
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

##  已完成功能

- ✅ 项目基础配置
- ✅ 路由配置
- ✅ API 封装（Axios）
- ✅ 状态管理（Pinia）
- ✅ Header 组件
- ✅ Footer 组件
- ⏳ 首页
- ⏳ 商品列表页
- ⏳ 商品详情页
- ⏳ 购物车
- ⏳ 登录/注册
-  个人中心

## 🔨 待完成

1. 实现所有页面组件
2. 对接后端 API
3. 完善错误处理
4. 添加加载动画
5. 响应式布局优化
6. 性能优化
7. 单元测试

##  开发建议

1. **先开发页面结构**，再对接 API
2. **使用 Mock 数据**进行前端开发
3. **与后端同步开发**，定义好 API 接口
4. **注意跨域问题**，开发时使用代理
5. **统一错误处理**，提升用户体验

## 📚 参考文档

- [Vue 3 官方文档](https://cn.vuejs.org/)
- [Vite 官方文档](https://cn.vitejs.dev/)
- [Element Plus 文档](https://element-plus.org/zh-CN/)
- [Vue Router 文档](https://router.vuejs.org/zh/)
- [Pinia 文档](https://pinia.vuejs.org/zh/)
- [Axios 文档](https://axios-http.com/)

## 🤝 协作开发

### 前后端协作流程

1. 确定 API 接口（参考 API_DOCUMENTATION.md）
2. 前端使用 Mock 数据开发
3. 后端实现 API 接口
4. 前端对接真实 API
5. 联调测试

## ⚠️ 注意事项

1. 确保后端 CORS 配置正确
2. Token 过期需要重新登录
3. 图片路径需要使用完整 URL
4. 购物车需要登录后才能使用

---

**开发愉快！** 
