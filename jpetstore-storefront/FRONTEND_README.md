# JPetStore 前端项目

这是一个基于 Vue 3 + Element Plus 的现代化宠物商店前端应用，采用独特的设计美学和流畅的用户体验。

## 🎨 设计特色

本项目遵循 **frontend-design** 技能指南，具有以下特点：

- **独特的视觉风格**：采用紫色渐变主题，避免通用的AI设计风格
- **精心设计的交互**：流畅的动画效果和微交互
- **响应式布局**：完美适配桌面端和移动端
- **优雅的排版**：清晰的层次结构和可读性
- **现代化的UI组件**：使用 Element Plus 构建专业界面

## 📦 技术栈

- **框架**: Vue 3 (Composition API)
- **UI库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **HTTP客户端**: Axios
- **构建工具**: Vite
- **图标**: Element Plus Icons

## 📁 项目结构

```
jpetstore-storefront/
├── src/
│   ├── api/              # API 接口
│   │   ├── account.js    # 账户相关API
│   │   ├── address.js    # 地址相关API
│   │   ├── auth.js       # 认证相关API
│   │   ├── cart.js       # 购物车相关API
│   │   ├── order.js      # 订单相关API
│   │   ├── product.js    # 商品相关API
│   │   └── request.js    # Axios 配置
│   ├── components/       # 公共组件
│   │   ├── Footer.vue    # 页脚组件
│   │   └── Header.vue    # 页头组件
│   ├── router/           # 路由配置
│   │   └── index.js
│   ├── stores/           # Pinia 状态管理
│   │   └── cart.js       # 购物车状态
│   ├── views/            # 页面组件
│   │   ├── Cart.vue      # 购物车页面
│   │   ├── Category.vue  # 商品分类页面
│   │   ├── Checkout.vue  # 订单结算页面
│   │   ├── Home.vue      # 首页
│   │   ├── Login.vue     # 登录页面
│   │   ├── OrderDetail.vue # 订单详情页面
│   │   ├── Product.vue   # 商品详情页面
│   │   ├── Profile.vue   # 个人中心页面
│   │   ├── Register.vue  # 注册页面
│   │   └── Search.vue    # 搜索结果页面
│   ├── App.vue           # 根组件
│   ├── main.js           # 入口文件
│   └── style.css         # 全局样式
├── public/               # 静态资源
├── index.html            # HTML 模板
├── package.json          # 依赖配置
└── vite.config.js        # Vite 配置
```

## 🚀 快速开始

### 安装依赖

```bash
npm install
```

### 开发模式

```bash
npm run dev
```

访问 http://localhost:5173

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 📄 页面说明

### 1. 首页 (Home.vue)
- 轮播图展示促销活动
- 商品分类导航
- 热门商品推荐
- 从后端API动态加载分类数据

### 2. 商品分类页 (Category.vue)
- 显示指定分类下的所有商品
- 支持商品规格展示
- 直接加入购物车功能
- 库存状态显示

### 3. 商品详情页 (Product.vue)
- 商品图片和详细信息
- 规格选择
- 数量选择
- 加入购物车
- 商品描述

### 4. 购物车 (Cart.vue)
- 查看购物车商品
- 修改商品数量
- 删除商品
- 清空购物车
- 去结算

### 5. 订单结算 (Checkout.vue)
- 选择收货地址
- 选择配送方式
- 填写支付信息
- 订单摘要
- 提交订单

### 6. 登录/注册 (Login.vue / Register.vue)
- 用户名密码登录
- 表单验证
- 用户注册
- 用户名/邮箱可用性检查

### 7. 个人中心 (Profile.vue)
- 查看和编辑个人信息
- 修改密码
- 查看订单列表
- 管理收货地址

### 8. 订单详情 (OrderDetail.vue)
- 订单状态
- 商品信息
- 收货地址
- 支付信息
- 订单总计

### 9. 搜索 (Search.vue)
- 关键字搜索商品
- 搜索结果展示
- 实时搜索

## 🔌 API 集成

所有API接口已配置在 `src/api/` 目录下，基础URL为 `http://localhost:8080/api/v1`。

主要API模块：
- **auth.js**: 登录、注册、登出
- **product.js**: 商品分类、商品详情、搜索
- **cart.js**: 购物车操作
- **order.js**: 订单创建和查询
- **account.js**: 个人信息管理
- **address.js**: 收货地址管理

## 🎯 核心功能

### 认证系统
- JWT Token 认证
- 路由守卫保护需要登录的页面
- 自动刷新用户信息

### 购物车
- Pinia 状态管理
- 实时同步后端数据
- 响应式更新

### 订单流程
1. 浏览商品
2. 加入购物车
3. 确认订单（选择地址、支付方式）
4. 提交订单
5. 查看订单详情

## 🎨 设计规范

### 颜色方案
- 主色: `#667eea` (紫色)
- 辅助色: `#764ba2` (深紫)
- 强调色: `#e74c3c` (红色 - 价格)
- 成功色: `#27ae60` (绿色 - 库存)
- 背景色: `#f8f9fa` (浅灰)

### 字体
- 主要字体: Helvetica Neue, Helvetica, Arial, sans-serif
- 标题字重: 600
- 正文字重: 400

### 间距
- 小间距: 8px
- 中间距: 16px
- 大间距: 24px
- 超大间距: 32px

### 圆角
- 小组件: 4px
- 卡片: 8px
- 大卡片: 12px

## 📱 响应式设计

- **桌面端**: > 1024px
- **平板端**: 768px - 1024px
- **移动端**: < 768px

所有页面都经过响应式设计，在不同设备上都有良好的显示效果。

## 🔧 开发注意事项

1. **API调用**: 所有API调用都已封装在 `src/api/` 目录
2. **错误处理**: 统一的错误提示和拦截器
3. **加载状态**: 使用骨架屏和loading状态提升用户体验
4. **图片处理**: 使用占位图处理图片加载失败
5. **表单验证**: 使用Element Plus的表单验证规则

## 🐛 常见问题

### 1. 跨域问题
确保后端已配置CORS，或在前端配置代理。

### 2. Token过期
Token存储在localStorage中，过期后会自动跳转到登录页。

### 3. 图片路径
图片放在 `public/images/` 目录下，使用绝对路径引用。

## 📝 待优化项

- [ ] 添加商品评价功能
- [ ] 实现商品收藏功能
- [ ] 添加优惠券系统
- [ ] 实现订单追踪
- [ ] 添加客服聊天功能
- [ ] 优化搜索引擎
- [ ] 添加商品对比功能
- [ ] 实现分享功能

## 📄 许可证

MIT License

## 👥 贡献

欢迎提交 Issue 和 Pull Request！

---

**注意**: 本项目需要配合后端API使用，请确保后端服务已启动并运行在 `http://localhost:8080`。
