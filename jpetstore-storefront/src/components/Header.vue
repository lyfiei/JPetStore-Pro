<template>
  <header class="header">
    <div class="header-container">
      <div class="logo" @click="goHome">
        <h1>🐾 JPetStore</h1>
      </div>
      
      <nav class="nav">
        <el-menu
          :default-active="activeMenu"
          mode="horizontal"
          :router="true"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
        >
          <el-menu-item index="/">首页</el-menu-item>
          <el-sub-menu index="category">
            <template #title>商品分类</template>
            <el-menu-item index="/category/FISH">鱼类</el-menu-item>
            <el-menu-item index="/category/DOGS">犬类</el-menu-item>
            <el-menu-item index="/category/CATS">猫类</el-menu-item>
            <el-menu-item index="/category/REPTILES">爬行类</el-menu-item>
            <el-menu-item index="/category/BIRDS">鸟类</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </nav>
      
      <div class="header-actions">
        <!-- 搜索框 -->
        <el-input
          v-model="searchKeyword"
          placeholder="搜索商品..."
          class="search-input"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
        
        <!-- 购物车 -->
        <el-badge :value="cartStore.cartCount" class="cart-badge">
          <el-button type="primary" @click="goToCart">
            <el-icon><ShoppingCart /></el-icon>
            购物车
          </el-button>
        </el-badge>
        
        <!-- 用户菜单 -->
        <el-dropdown v-if="isLogin" trigger="click">
          <span class="user-info">
            <el-icon><User /></el-icon>
            {{ userInfo?.username || userInfo?.firstName }}
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="goToProfile">个人中心</el-dropdown-item>
              <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        
        <el-button v-else type="primary" @click="goToLogin">
          <el-icon><User /></el-icon>
          登录/注册
        </el-button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Search, ShoppingCart, User, ArrowDown } from '@element-plus/icons-vue'
import { useCartStore } from '../stores/cart'
import { logout as apiLogout } from '../api/auth'

const router = useRouter()
const route = useRoute()
const cartStore = useCartStore()

const searchKeyword = ref('')
const isLogin = computed(() => !!localStorage.getItem('token'))
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

const activeMenu = computed(() => route.path)

const goHome = () => router.push('/')
const goToCart = () => router.push('/cart')
const goToLogin = () => router.push('/login')
const goToProfile = () => router.push('/profile')

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/search', query: { keyword: searchKeyword.value } })
    searchKeyword.value = ''
  }
}

const handleLogout = async () => {
  try {
    await apiLogout()
  } catch (error) {
    console.error('登出失败:', error)
  } finally {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.push('/')
    location.reload()
  }
}

// 监听用户信息变化
onMounted(() => {
  const updateUserInfo = () => {
    userInfo.value = JSON.parse(localStorage.getItem('userInfo') || '{}')
  }
  
  window.addEventListener('storage', updateUserInfo)
  return () => window.removeEventListener('storage', updateUserInfo)
})
</script>

<style scoped>
.header {
  background: #545c64;
  color: white;
  padding: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  gap: 20px;
}

.logo {
  cursor: pointer;
  white-space: nowrap;
}

.logo h1 {
  font-size: 24px;
  font-weight: bold;
  color: #ffd04b;
}

.nav {
  flex: 1;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.search-input {
  width: 250px;
}

.cart-badge {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  color: white;
  padding: 8px 15px;
  border-radius: 4px;
  transition: background 0.3s;
}

.user-info:hover {
  background: rgba(255, 255, 255, 0.1);
}
</style>
