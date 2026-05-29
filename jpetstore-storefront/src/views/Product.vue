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
        <div class="product-gallery">
          <div class="main-image">
            <img :src="currentImage" :alt="product.name" @error="handleImageError" />
          </div>
          <div class="thumbs">
            <img v-for="img in gallery" :key="img" :src="img" @click="currentImage = img" class="thumb" />
          </div>
        </div>

        <div class="product-meta">
          <h1 class="product-title">{{ product.name }}</h1>
          <p class="product-description">{{ stripHtml(product.description) }}</p>

          <div v-if="items.length > 0" class="specifications">
            <h4>规格</h4>
            <div class="spec-grid">
              <div v-for="item in items" :key="item.itemId" class="spec-card" :class="{selected: selectedItem?.itemId===item.itemId}" @click="selectItem(item)">
                <div class="spec-name">{{ item.attribute1 || '标准版' }}</div>
                <div class="spec-price">¥{{ item.listPrice }}</div>
                <div class="spec-stock" :class="{ 'out': item.stock===0 }">{{ item.stock>0?`库存 ${item.stock}`:'缺货' }}</div>
              </div>
            </div>
          </div>

          <div class="purchase">
            <el-input-number v-model="quantity" :min="1" :max="selectedItem?.stock || 1" size="large" />
            <el-button
              class="add-cart-btn"
              type="primary"
              size="large"
              :disabled="!selectedItem || selectedItem.stock === 0"
              @click="addToCart"
            >
              <el-icon size="18"><ShoppingCart /></el-icon> 加入购物车
            </el-button>
          </div>

          <div class="product-info-extra">
            <el-divider />
            <div>商品编号：{{ product.productId }}</div>
            <div>分类：{{ product.categoryId }}</div>
          </div>
        </div>
      </div>

      <div class="product-details-section">
        <h2>商品详情</h2>
        <div class="details-content">
          <p v-html="product.description"></p>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty v-else description="商品不存在" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ShoppingCart } from '@element-plus/icons-vue'
import { getProductDetail } from '../api/product'
import { useCartStore } from '../stores/cart'
import { stripHtml, extractImageSrc } from '../utils/format'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

const loading = ref(false)
const product = ref(null)
const items = ref([])
const selectedItem = ref(null)
const quantity = ref(1)

// 图片画廊与当前图片
const gallery = ref([])
const currentImage = ref('/images/splash.gif')

const handleImageError = (e) => {
  e.target.src = '/images/splash.gif'
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

      // 从数据库 description 提取商品图片
      const mainImg = extractImageSrc(product.value.description) || '/images/splash.gif'
      gallery.value = [mainImg]
      currentImage.value = mainImg

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

watch(() => route.params.productId, () => {
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
.product-meta {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.product-gallery {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.product-gallery .main-image {
  width: 100%;
  border-radius: 12px;
  overflow: hidden;
}

.product-gallery .thumbs {
  display: flex;
  gap: 8px;
}

.product-gallery .thumb {
  width: 72px;
  height: 72px;
  object-fit: cover;
  border-radius: 8px;
  cursor: pointer;
  border: 2px solid transparent;
}

.product-gallery .thumb:hover {
  border-color: var(--accent);
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
.specifications h4 {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 16px;
}
.spec-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: 12px;
}

.spec-card {
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #e9ecef;
  cursor: pointer;
  transition: all .18s;
  text-align: center;
}

.spec-card.selected {
  border-color: var(--accent);
  box-shadow: 0 6px 18px rgba(58,125,92,0.15);
}

.spec-name { font-weight: 600; color: #2c3e50; }
.spec-price { color: var(--accent); font-weight: 700; margin-top: 8px; }
.spec-stock { font-size: 13px; color: #27ae60; margin-top: 6px; }
.spec-stock.out { color: #e74c3c; }

.purchase {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 0;
}

.add-cart-btn {
  flex: 1;
  height: 50px !important;
  font-size: 16px !important;
  font-weight: 600 !important;
  background: #2d7a4f !important;
  border-color: #2d7a4f !important;
  color: #fff !important;
}

.add-cart-btn.is-disabled,
.add-cart-btn:disabled {
  background: #c0c8b8 !important;
  border-color: #c0c8b8 !important;
  color: rgba(255,255,255,0.7) !important;
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
