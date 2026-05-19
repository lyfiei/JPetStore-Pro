<template>
  <div class="home-page">
    <!-- 轮播图区域 -->
    <section class="banner">
      <el-carousel height="400px" indicator-position="outside">
        <el-carousel-item v-for="item in banners" :key="item.id">
          <div class="banner-item" :style="{ background: item.color }">
            <h2>{{ item.title }}</h2>
            <p>{{ item.description }}</p>
            <el-button type="warning" size="large" @click="goToCategory(item.categoryId)">
              立即选购
            </el-button>
          </div>
        </el-carousel-item>
      </el-carousel>
    </section>

    <!-- 商品分类 -->
    <section class="categories">
      <h2>商品分类</h2>
      <div class="category-grid">
        <div 
          v-for="category in categories" 
          :key="category.categoryId"
          class="category-card"
          @click="goToCategory(category.categoryId)"
        >
          <div class="category-icon">{{ category.icon }}</div>
          <h3>{{ category.name }}</h3>
          <p>{{ category.description }}</p>
        </div>
      </div>
    </section>

    <!-- 热门商品 -->
    <section class="featured-products">
      <h2>热门商品</h2>
      <div class="product-grid">
        <div v-for="product in featuredProducts" :key="product.productId" class="product-card">
          <div class="product-image">
            <img :src="product.imageUrl || '/placeholder.png'" :alt="product.name" />
          </div>
          <div class="product-info">
            <h3>{{ product.name }}</h3>
            <p class="price">¥{{ product.listPrice }}</p>
            <el-button type="primary" @click="viewProduct(product.productId)">
              查看详情
            </el-button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCategories } from '../api/product'

const router = useRouter()

// 轮播图数据
const banners = ref([
  { id: 1, title: '宠物食品大促', description: '全场宠物食品 8 折起', color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', categoryId: 'FOOD' },
  { id: 2, title: '新会员专享', description: '注册即送 100 元优惠券', color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)', categoryId: 'DOGS' },
  { id: 3, title: '猫咪专场', description: '精选猫咪用品', color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)', categoryId: 'CATS' }
])

// 商品分类数据
const categories = ref([])

// 热门商品（Mock 数据）
const featuredProducts = ref([
  { productId: 'FI-SW-01', name: '天使鱼', listPrice: 16.50, imageUrl: '' },
  { productId: 'FI-SW-02', name: '虎鲨', listPrice: 20.00, imageUrl: '' },
  { productId: 'K9-DL-01', name: '达尔马提亚斑点狗', listPrice: 183.00, imageUrl: '' },
  { productId: 'K9-PO-02', name: '贵宾犬', listPrice: 150.00, imageUrl: '' }
])

// 跳转到分类页
const goToCategory = (categoryId) => {
  router.push(`/category/${categoryId}`)
}

// 查看商品详情
const viewProduct = (productId) => {
  router.push(`/product/${productId}`)
}

// 加载分类数据（从后端 API）
onMounted(async () => {
  try {
    const res = await getCategories()
    if (res.data) {
      // 为每个分类添加图标
      const iconMap = {
        'FISH': '🐟',
        'DOGS': '🐕',
        'CATS': '🐈',
        'REPTILES': '🦎',
        'BIRDS': '🐦'
      }
      
      categories.value = res.data.map(cat => ({
        ...cat,
        icon: iconMap[cat.categoryId] || '🛍️'
      }))
    }
  } catch (error) {
    console.error('加载分类失败:', error)
  }
})
</script>

<style scoped>
.home-page {
  padding: 0;
}

.banner {
  margin-bottom: 40px;
}

.banner-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  height: 400px;
  text-align: center;
}

.banner-item h2 {
  font-size: 48px;
  margin-bottom: 20px;
}

.banner-item p {
  font-size: 24px;
  margin-bottom: 30px;
}

.categories {
  margin-bottom: 40px;
}

.categories h2,
.featured-products h2 {
  text-align: center;
  font-size: 32px;
  margin-bottom: 30px;
  color: #2c3e50;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.category-card {
  background: white;
  border-radius: 8px;
  padding: 30px 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.category-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

.category-card h3 {
  font-size: 20px;
  margin-bottom: 10px;
  color: #2c3e50;
}

.category-card p {
  font-size: 14px;
  color: #7f8c8d;
}

.featured-products {
  margin-bottom: 40px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.product-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.product-image {
  height: 200px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.product-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 20px;
  text-align: center;
}

.product-info h3 {
  font-size: 18px;
  margin-bottom: 10px;
  color: #2c3e50;
}

.product-info .price {
  font-size: 24px;
  color: #e74c3c;
  font-weight: bold;
  margin-bottom: 15px;
}
</style>
