<template>
  <div class="cart-page">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>购物车</el-breadcrumb-item>
    </el-breadcrumb>

    <h1 class="page-title">购物车</h1>

    <!-- 加载状态 -->
    <div v-if="cartStore.loading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>

    <!-- 空购物车 -->
    <el-empty 
      v-else-if="cartStore.cartItems.length === 0" 
      description="购物车是空的"
      class="empty-cart"
    >
      <el-button type="primary" @click="goShopping">去购物</el-button>
    </el-empty>

    <!-- 购物车内容 -->
    <div v-else class="cart-content">
      <div class="cart-items">
        <div 
          v-for="item in cartStore.cartItems" 
          :key="item.itemId"
          class="cart-item"
        >
          <!-- 商品图片 -->
          <div class="item-image">
            <img 
              :src="getItemImage(item.productId)" 
              :alt="item.productName"
              @error="handleImageError"
            />
          </div>

          <!-- 商品信息 -->
          <div class="item-info">
            <h3 class="item-name">{{ item.productName }}</h3>
            <p class="item-id">商品编号: {{ item.itemId }}</p>
            
            <!-- 库存状态 -->
            <div class="stock-status" :class="{ 'out-of-stock': !item.inStock }">
              {{ item.inStock ? '有货' : '缺货' }}
            </div>
          </div>

          <!-- 单价 -->
          <div class="item-price">
            <span class="price-label">单价：</span>
            <span class="price-value">¥{{ item.listPrice }}</span>
          </div>

          <!-- 数量控制 -->
          <div class="item-quantity">
            <label>数量：</label>
            <el-input-number 
              v-model="item.quantity" 
              :min="1" 
              :max="99"
              size="default"
              @change="updateQuantity(item)"
            />
          </div>

          <!-- 小计 -->
          <div class="item-subtotal">
            <span class="subtotal-label">小计：</span>
            <span class="subtotal-value">¥{{ (item.listPrice * item.quantity).toFixed(2) }}</span>
          </div>

          <!-- 删除按钮 -->
          <el-button 
            type="danger" 
            link
            @click="removeItem(item.itemId)"
          >
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
        </div>
      </div>

      <!-- 购物车结算 -->
      <div class="cart-summary">
        <div class="summary-row">
          <span class="summary-label">商品总数：</span>
          <span class="summary-value">{{ cartStore.cartCount }} 件</span>
        </div>
        <div class="summary-row total">
          <span class="summary-label">合计：</span>
          <span class="summary-value price">¥{{ cartStore.cartTotal.toFixed(2) }}</span>
        </div>
        
        <div class="summary-actions">
          <el-button @click="clearCart">清空购物车</el-button>
          <el-button type="primary" size="large" @click="checkout">
            去结算
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import { useCartStore } from '../stores/cart'

const router = useRouter()
const cartStore = useCartStore()

// 获取商品图片
const getItemImage = (productId) => {
  const imageMap = {
    'FI-SW-01': '/images/fish1.jpg',
    'FI-SW-02': '/images/fish2.jpg',
    'K9-DL-01': '/images/dog1.jpg',
  }
  return imageMap[productId] || '/images/placeholder.png'
}

const handleImageError = (e) => {
  e.target.src = '/images/placeholder.png'
}

// 去购物
const goShopping = () => {
  router.push('/')
}

// 更新数量
const updateQuantity = async (item) => {
  try {
    await cartStore.updateItem(item.itemId, item.quantity)
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

// 删除商品
const removeItem = async (itemId) => {
  try {
    await ElMessageBox.confirm('确定要删除这个商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await cartStore.removeItem(itemId)
    ElMessage.success('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 清空购物车
const clearCart = async () => {
  try {
    await ElMessageBox.confirm('确定要清空购物车吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await cartStore.clearAll()
    ElMessage.success('购物车已清空')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

// 去结算
const checkout = () => {
  router.push('/checkout')
}

// 加载购物车数据
onMounted(async () => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  await cartStore.fetchCart()
})
</script>

<style scoped>
.cart-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.breadcrumb {
  margin-bottom: 24px;
}

.page-title {
  font-size: 32px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 32px;
}

.loading-container {
  padding: 40px;
  background: white;
  border-radius: 12px;
}

.empty-cart {
  padding: 80px 20px;
  background: white;
  border-radius: 12px;
}

.cart-content {
  display: grid;
  grid-template-columns: 1fr 380px;
  gap: 24px;
}

.cart-items {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.cart-item {
  display: grid;
  grid-template-columns: 100px 1fr 120px 140px 120px auto;
  gap: 20px;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid #e9ecef;
}

.cart-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 100px;
  height: 100px;
  background: #f8f9fa;
  border-radius: 8px;
  overflow: hidden;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.item-name {
  font-size: 18px;
  font-weight: 500;
  color: #2c3e50;
  margin: 0;
}

.item-id {
  font-size: 13px;
  color: #7f8c8d;
  margin: 0;
}

.stock-status {
  font-size: 13px;
  color: #27ae60;
  font-weight: 500;
}

.stock-status.out-of-stock {
  color: #e74c3c;
}

.item-price,
.item-subtotal {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.price-label,
.subtotal-label {
  font-size: 13px;
  color: #7f8c8d;
}

.price-value,
.subtotal-value {
  font-size: 18px;
  font-weight: 600;
  color: #e74c3c;
}

.item-quantity {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.item-quantity label {
  font-size: 13px;
  color: #7f8c8d;
}

.cart-summary {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  height: fit-content;
  position: sticky;
  top: 20px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  font-size: 16px;
}

.summary-row.total {
  border-top: 2px solid #e9ecef;
  margin-top: 12px;
  padding-top: 20px;
  font-size: 18px;
}

.summary-label {
  color: #7f8c8d;
}

.summary-value {
  font-weight: 500;
  color: #2c3e50;
}

.summary-value.price {
  font-size: 24px;
  font-weight: 600;
  color: #e74c3c;
}

.summary-actions {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.summary-actions .el-button {
  flex: 1;
}

@media (max-width: 1024px) {
  .cart-content {
    grid-template-columns: 1fr;
  }

  .cart-summary {
    position: static;
  }
}

@media (max-width: 768px) {
  .cart-item {
    grid-template-columns: 80px 1fr;
    gap: 12px;
  }

  .item-price,
  .item-quantity,
  .item-subtotal {
    grid-column: 1 / -1;
  }

  .item-image {
    width: 80px;
    height: 80px;
  }
}
</style>
