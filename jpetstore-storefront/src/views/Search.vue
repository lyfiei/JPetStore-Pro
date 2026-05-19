<template>
  <div class="search-page">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>搜索结果</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 搜索框 -->
    <div class="search-header">
      <h1>搜索结果</h1>
      <div class="search-box">
        <el-input
          v-model="keyword"
          placeholder="搜索商品..."
          size="large"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
          </template>
        </el-input>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>

    <!-- 搜索结果 -->
    <div v-else-if="products.length > 0" class="search-results">
      <p class="result-count">找到 {{ products.length }} 个相关商品</p>
      
      <div class="product-list">
        <div 
          v-for="product in products" 
          :key="product.productId"
          class="product-card"
          @click="viewProduct(product.productId)"
        >
          <div class="product-image">
            <img 
              :src="getProductImage(product.productId)" 
              :alt="product.name"
              @error="handleImageError"
            />
          </div>
          <div class="product-info">
            <h3 class="product-name">{{ product.name }}</h3>
            <p class="product-description">{{ product.description }}</p>
            <el-button type="primary" @click.stop="viewProduct(product.productId)">
              查看详情
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty v-else description="没有找到相关商品" class="empty-result">
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </el-empty>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { searchProducts } from '../api/product'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const keyword = ref(route.query.keyword || '')
const products = ref([])

// 获取商品图片
const getProductImage = (productId) => {
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

// 返回首页
const goHome = () => {
  router.push('/')
}

// 查看商品详情
const viewProduct = (productId) => {
  router.push(`/product/${productId}`)
}

// 执行搜索
const handleSearch = () => {
  if (!keyword.value.trim()) {
    ElMessage.warning('请输入搜索关键字')
    return
  }
  
  // 更新URL
  router.push({ path: '/search', query: { keyword: keyword.value } })
  loadSearchResults()
}

// 加载搜索结果
const loadSearchResults = async () => {
  const searchKeyword = route.query.keyword
  if (!searchKeyword) return

  keyword.value = searchKeyword
  loading.value = true
  
  try {
    const res = await searchProducts(searchKeyword)
    products.value = res.data || []
  } catch (error) {
    console.error('搜索失败:', error)
    ElMessage.error('搜索失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadSearchResults()
})
</script>

<style scoped>
.search-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.breadcrumb {
  margin-bottom: 24px;
}

.search-header {
  margin-bottom: 32px;
}

.search-header h1 {
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 20px;
}

.search-box {
  max-width: 600px;
}

.loading-container {
  padding: 40px;
  background: white;
  border-radius: 12px;
}

.search-results {
  margin-top: 20px;
}

.result-count {
  font-size: 16px;
  color: #7f8c8d;
  margin-bottom: 24px;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.product-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.product-image {
  height: 200px;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 20px;
}

.product-name {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 8px;
}

.product-description {
  font-size: 14px;
  color: #7f8c8d;
  line-height: 1.6;
  margin-bottom: 16px;
  height: 44px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.empty-result {
  padding: 80px 20px;
  background: white;
  border-radius: 12px;
}

@media (max-width: 768px) {
  .product-list {
    grid-template-columns: 1fr;
  }
}
</style>
