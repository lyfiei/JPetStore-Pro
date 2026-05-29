<template>
  <div class="category-page">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>{{ categoryInfo?.name || '商品分类' }}</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 分类标题 -->
    <div class="category-header">
      <h1>{{ categoryInfo?.name }}</h1>
      <p class="category-description">{{ stripHtml(categoryInfo?.description) }}</p>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>

    <!-- 商品列表 -->
    <div v-else-if="products.length > 0" class="product-list">
      <div 
        v-for="product in products" 
        :key="product.productId"
        class="product-card"
        @click="viewProduct(product.productId)"
      >
        <div class="product-image">
          <img 
            :src="getProductImage(product)"
            :alt="product.name"
            @error="handleImageError"
          />
        </div>
        <div class="product-info">
          <h3 class="product-name">{{ product.name }}</h3>
          <p class="product-description">{{ stripHtml(product.description) }}</p>
          
          <!-- 商品规格 -->
          <div v-if="product.items && product.items.length > 0" class="product-items">
            <div 
              v-for="item in product.items" 
              :key="item.itemId"
              class="item-row"
            >
              <span class="item-name">{{ item.attribute1 || '标准版' }}</span>
              <span class="item-price">¥{{ item.listPrice }}</span>
              <span class="item-stock" :class="{ 'out-of-stock': item.stock === 0 }">
                {{ item.stock > 0 ? `库存: ${item.stock}` : '缺货' }}
              </span>
              <el-button
                class="add-cart-btn"
                size="small"
                :disabled="item.stock === 0"
                @click.stop="addToCart(item)"
              >
                <el-icon><ShoppingCart /></el-icon> 加入购物车
              </el-button>
            </div>
          </div>
          
          <el-button 
            v-else
            type="primary" 
            class="view-detail-btn"
            @click.stop="viewProduct(product.productId)"
          >
            查看详情
          </el-button>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty v-else description="暂无商品" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCategoryWithProducts, getCategoryProductsWithItems } from '../api/product'
import { ShoppingCart } from '@element-plus/icons-vue'
import { useCartStore } from '../stores/cart'
import { stripHtml, extractImageSrc } from '../utils/format'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

const loading = ref(false)
const categoryInfo = ref(null)
const products = ref([])

// 获取商品图片
const getProductImage = (product) => {
  const src = extractImageSrc(product.description)
  return src || '/images/splash.gif'
}

const handleImageError = (e) => {
  e.target.src = '/images/splash.gif'
}

// 查看商品详情
const viewProduct = (productId) => {
  router.push(`/product/${productId}`)
}

// 添加到购物车
const addToCart = async (item) => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  try {
    await cartStore.addItemToCart({
      itemId: item.itemId,
      quantity: 1
    })
    ElMessage.success('已添加到购物车')
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

// 加载分类数据
const loadCategoryData = async () => {
  const categoryId = route.params.categoryId
  if (!categoryId) return

  loading.value = true
  try {
    // 先尝试获取带规格项的数据
    const res = await getCategoryProductsWithItems(categoryId)
    if (res.data && res.data.length > 0) {
      products.value = res.data
      // 从第一个商品获取分类信息
      categoryInfo.value = {
        name: categoryId,
        description: `${categoryId} 类商品`
      }
    } else {
      // 如果没有规格项数据，获取基本分类数据
      const basicRes = await getCategoryWithProducts(categoryId)
      if (basicRes.data) {
        categoryInfo.value = basicRes.data.category
        products.value = basicRes.data.products || []
      }
    }
  } catch (error) {
    console.error('加载分类数据失败:', error)
    ElMessage.error('加载商品失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadCategoryData()
})

watch(() => route.params.categoryId, () => {
  loadCategoryData()
})
</script>

<style scoped>
.category-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.breadcrumb {
  margin-bottom: 20px;
}

.category-header {
  text-align: center;
  margin-bottom: 40px;
  padding: 30px 0;
  background: linear-gradient(135deg, var(--accent) 0%, var(--accent-dark) 100%);
  border-radius: 12px;
  color: white;
}

.category-header h1 {
  font-size: 36px;
  margin-bottom: 10px;
  font-weight: 600;
}

.category-description {
  font-size: 18px;
  opacity: 0.9;
}

.loading-container {
  padding: 40px;
  background: white;
  border-radius: 8px;
}

.product-list {
  display: grid;
  gap: 24px;
}

.product-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
  display: flex;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.product-image {
  width: 280px;
  height: 280px;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  flex: 1;
  padding: 24px;
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 12px;
}

.product-description {
  font-size: 14px;
  color: #7f8c8d;
  line-height: 1.6;
  margin-bottom: 20px;
}

.product-items {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.item-row {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
  transition: background 0.2s;
}

.item-row:hover {
  background: #e9ecef;
}

.item-name {
  min-width: 100px;
  font-weight: 500;
  color: #495057;
}

.item-price {
  font-size: 20px;
  font-weight: 600;
  color: var(--accent);
  min-width: 80px;
}

.item-stock {
  flex: 1;
  font-size: 13px;
  color: #27ae60;
}

.item-stock.out-of-stock {
  color: #e74c3c;
}

.view-detail-btn {
  align-self: flex-start;
  margin-top: auto;
}

.add-cart-btn {
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

@media (max-width: 768px) {
  .product-card {
    flex-direction: column;
  }

  .product-image {
    width: 100%;
    height: 200px;
  }

  .item-row {
    flex-wrap: wrap;
  }
}
</style>
