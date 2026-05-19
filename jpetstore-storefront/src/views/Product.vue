<template>
  <div class="product-page">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: `/category/${product?.categoryId}` }">
        {{ product?.categoryId }}
      </el-breadcrumb-item>
      <el-breadcrumb-item>{{ product?.name }}</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- 商品详情 -->
    <div v-else-if="product" class="product-detail">
      <div class="product-main">
        <!-- 商品图片 -->
        <div class="product-image-section">
          <div class="main-image">
            <img 
              :src="currentImage" 
              :alt="product.name"
              @error="handleImageError"
            />
          </div>
        </div>

        <!-- 商品信息 -->
        <div class="product-info-section">
          <h1 class="product-title">{{ product.name }}</h1>
          <p class="product-description">{{ product.description }}</p>

          <!-- 规格选择 -->
          <div v-if="items.length > 0" class="specifications">
            <h3>选择规格</h3>
            <div class="item-list">
              <div 
                v-for="item in items" 
                :key="item.itemId"
                class="item-option"
                :class="{ selected: selectedItem?.itemId === item.itemId }"
                @click="selectItem(item)"
              >
                <div class="item-header">
                  <span class="item-name">{{ item.attribute1 || '标准版' }}</span>
                  <span class="item-price">¥{{ item.listPrice }}</span>
                </div>
                <div class="item-footer">
                  <span class="item-stock" :class="{ 'out-of-stock': item.stock === 0 }">
                    {{ item.stock > 0 ? `库存: ${item.stock}` : '缺货' }}
                  </span>
                </div>
              </div>
            </div>
          </div>

          <!-- 数量选择 -->
          <div v-if="selectedItem" class="quantity-selector">
            <label>数量：</label>
            <el-input-number 
              v-model="quantity" 
              :min="1" 
              :max="selectedItem.stock"
              size="large"
            />
          </div>

          <!-- 操作按钮 -->
          <div class="action-buttons">
            <el-button 
              type="primary" 
              size="large"
              :disabled="!selectedItem || selectedItem.stock === 0"
              @click="addToCart"
            >
              <el-icon><ShoppingCart /></el-icon>
              加入购物车
            </el-button>
          </div>

          <!-- 其他信息 -->
          <div class="additional-info">
            <el-divider />
            <div class="info-row">
              <span class="label">商品编号：</span>
              <span class="value">{{ product.productId }}</span>
            </div>
            <div class="info-row">
              <span class="label">分类：</span>
              <span class="value">{{ product.categoryId }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 商品详情描述 -->
      <div class="product-details-section">
        <h2>商品详情</h2>
        <div class="details-content">
          <p>{{ product.description }}</p>
          <p>我们提供优质的宠物用品，确保您的宠物得到最好的照顾。</p>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty v-else description="商品不存在" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ShoppingCart } from '@element-plus/icons-vue'
import { getProductDetail } from '../api/product'
import { useCartStore } from '../stores/cart'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

const loading = ref(false)
const product = ref(null)
const items = ref([])
const selectedItem = ref(null)
const quantity = ref(1)

// 当前显示的图片
const currentImage = computed(() => {
  if (!product.value) return '/images/placeholder.png'
  const imageMap = {
    'FI-SW-01': '/images/fish1.jpg',
    'FI-SW-02': '/images/fish2.jpg',
    'K9-DL-01': '/images/dog1.jpg',
  }
  return imageMap[product.value.productId] || '/images/placeholder.png'
})

const handleImageError = (e) => {
  e.target.src = '/images/placeholder.png'
}

// 选择规格项
const selectItem = (item) => {
  selectedItem.value = item
  quantity.value = 1
}

// 添加到购物车
const addToCart = async () => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  if (!selectedItem.value) {
    ElMessage.warning('请选择规格')
    return
  }

  try {
    await cartStore.addItemToCart({
      itemId: selectedItem.value.itemId,
      quantity: quantity.value
    })
    ElMessage.success('已添加到购物车')
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

// 加载商品数据
const loadProductData = async () => {
  const productId = route.params.productId
  if (!productId) return

  loading.value = true
  try {
    const res = await getProductDetail(productId)
    if (res.data) {
      product.value = res.data.product
      items.value = res.data.items || []
      
      // 默认选择第一个有库存的规格
      if (items.value.length > 0) {
        const availableItem = items.value.find(item => item.stock > 0)
        if (availableItem) {
          selectedItem.value = availableItem
        } else {
          selectedItem.value = items.value[0]
        }
      }
    }
  } catch (error) {
    console.error('加载商品数据失败:', error)
    ElMessage.error('加载商品失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadProductData()
})
</script>

<style scoped>
.product-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.breadcrumb {
  margin-bottom: 24px;
}

.loading-container {
  padding: 40px;
  background: white;
  border-radius: 12px;
}

.product-detail {
  background: white;
  border-radius: 12px;
  overflow: hidden;
}

.product-main {
  display: grid;
  grid-template-columns: 500px 1fr;
  gap: 40px;
  padding: 40px;
}

.product-image-section {
  position: sticky;
  top: 20px;
  height: fit-content;
}

.main-image {
  width: 100%;
  aspect-ratio: 1;
  background: #f8f9fa;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.main-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info-section {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.product-title {
  font-size: 32px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
}

.product-description {
  font-size: 16px;
  color: #7f8c8d;
  line-height: 1.8;
}

.specifications h3 {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 16px;
}

.item-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.item-option {
  padding: 16px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.item-option:hover {
  border-color: #667eea;
  background: #f8f9ff;
}

.item-option.selected {
  border-color: #667eea;
  background: #f0f2ff;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.item-name {
  font-weight: 500;
  color: #495057;
}

.item-price {
  font-size: 20px;
  font-weight: 600;
  color: #e74c3c;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-stock {
  font-size: 13px;
  color: #27ae60;
}

.item-stock.out-of-stock {
  color: #e74c3c;
}

.quantity-selector {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 0;
}

.quantity-selector label {
  font-weight: 500;
  color: #495057;
}

.action-buttons {
  padding-top: 16px;
}

.action-buttons .el-button {
  width: 100%;
  height: 50px;
  font-size: 16px;
}

.additional-info {
  margin-top: auto;
}

.info-row {
  display: flex;
  gap: 12px;
  padding: 8px 0;
  font-size: 14px;
}

.info-row .label {
  color: #7f8c8d;
  min-width: 80px;
}

.info-row .value {
  color: #2c3e50;
}

.product-details-section {
  padding: 40px;
  border-top: 1px solid #e9ecef;
  background: #f8f9fa;
}

.product-details-section h2 {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 20px;
}

.details-content {
  line-height: 1.8;
  color: #495057;
}

.details-content p {
  margin-bottom: 12px;
}

@media (max-width: 1024px) {
  .product-main {
    grid-template-columns: 1fr;
    padding: 24px;
  }

  .product-image-section {
    position: static;
  }
}

@media (max-width: 768px) {
  .product-title {
    font-size: 24px;
  }

  .product-details-section {
    padding: 24px;
  }
}
</style>
