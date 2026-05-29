<template>
  <header class="header">
    <div class="header-main">
      <div class="header-top">
        <div class="brand" @click="goHome" role="button" aria-label="首页">
          <div class="brand-logo-wrap">
            <img class="brand-logo" src="/images/logo-parrot.svg" alt="JPetStore" />
          </div>
          <div class="brand-text">
            <strong>JPetStore</strong>
            <span>(DEMO)</span>
          </div>
        </div>

        <div class="header-search">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索宠物用品、零食、玩具..."
            class="search-input"
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon class="search-icon"><Search /></el-icon>
            </template>
          </el-input>
        </div>

        <div class="header-actions">
          <el-tooltip content="收藏" placement="bottom">
            <el-button class="icon-btn" circle @click="goToFavorite">
              <el-icon size="18"><Star /></el-icon>
            </el-button>
          </el-tooltip>

          <el-tooltip content="购物车" placement="bottom">
            <el-badge :value="cartStore.cartCount" :hidden="cartStore.cartCount === 0" class="cart-badge">
              <el-button class="icon-btn cart-btn" circle @click="goToCart">
                <el-icon size="18"><ShoppingCart /></el-icon>
              </el-button>
            </el-badge>
          </el-tooltip>

          <el-dropdown v-if="isLogin" trigger="click" class="user-dropdown">
            <span class="user-avatar-wrap">
              <el-avatar :size="34" class="user-avatar">
                {{ (userInfo?.username || 'U').charAt(0).toUpperCase() }}
              </el-avatar>
              <span class="user-name">{{ userInfo?.username || '用户' }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goToProfile">
                  <el-icon><User /></el-icon> 个人中心
                </el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>

          <el-avatar v-else :size="34" class="user-avatar guest-avatar" @click="goToLogin">
            <el-icon size="18"><User /></el-icon>
          </el-avatar>
        </div>
      </div>

      <nav class="header-nav">
        <ul class="nav-list">
          <li :class="{ active: activeMenu === '/' }">
            <router-link to="/">
              <el-icon><HomeFilled /></el-icon>
              首页
            </router-link>
          </li>
          <li
            :class="{ active: activeMenu.startsWith('/category') }"
            class="nav-dropdown"
            @mouseenter="openDropdown"
            @mouseleave="closeDropdown"
          >
            <span class="nav-dropdown-trigger" @click="toggleDropdown">
              商品分类
              <el-icon class="chevron" :class="{ rotated: showDropdown }"><ArrowDown /></el-icon>
            </span>
            <div
              v-if="showDropdown"
              class="nav-dropdown-panel"
              @mouseenter="cancelClose"
              @mouseleave="closeDropdown"
            >
                <div
                  v-for="cat in categories"
                  :key="cat.categoryId"
                  class="nav-dropdown-item"
                  @click.stop="goToCategory(cat.categoryId)"
                >
                  <img
                    :src="getCategoryIcon(cat.categoryId)"
                    :alt="cat.name"
                    class="dropdown-icon"
                    @error="e => e.target.src = '/images/logo-parrot.svg'"
                  />
                  <span class="dropdown-label">
                    <span class="dropdown-cn">{{ cat.name }}</span>
                    <span class="dropdown-sep">|</span>
                    <span class="dropdown-en">{{ getEnglishName(cat.categoryId) }}</span>
                  </span>
                </div>
              </div>
          </li>
          <li :class="{ active: activeMenu === '/help' }">
            <router-link to="/help">帮助</router-link>
          </li>
        </ul>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Search, ShoppingCart, User, ArrowDown, Star, HomeFilled } from '@element-plus/icons-vue'
import { useCartStore } from '../stores/cart'
import { logout as apiLogout } from '../api/auth'
import { getCategories } from '../api/product'
import { stripHtml } from '../utils/format'

const router = useRouter()
const route = useRoute()
const cartStore = useCartStore()

const categories = ref([])
const searchKeyword = ref('')
const showDropdown = ref(false)
let closeTimer = null
const isLogin = ref(!!localStorage.getItem('token'))
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

const activeMenu = computed(() => route.path)

const goHome = () => router.push('/')
const goToCart = () => router.push('/cart')
const goToLogin = () => router.push('/login')
const goToProfile = () => router.push('/profile')
const goToFavorite = () => {
  if (isLogin.value) {
    router.push('/profile')
  } else {
    router.push('/login')
  }
}

const openDropdown = () => {
  cancelClose()
  showDropdown.value = true
}

const closeDropdown = () => {
  closeTimer = setTimeout(() => {
    showDropdown.value = false
  }, 150)
}

const cancelClose = () => {
  if (closeTimer) {
    clearTimeout(closeTimer)
    closeTimer = null
  }
}

const toggleDropdown = () => {
  if (showDropdown.value) {
    showDropdown.value = false
  } else {
    openDropdown()
  }
}

const goToCategory = (categoryId) => {
  showDropdown.value = false
  router.push(`/category/${categoryId}`)
}

const getCategoryIcon = (categoryId) => {
  const map = {
    'FISH': '/images/fish-category.jpg',
    'DOGS': '/images/dog-category.jpg',
    'CATS': '/images/cat-category.jpg',
    'REPTILES': '/images/reptile-category.jpg',
    'BIRDS': '/images/bird-category.jpg'
  }
  return map[categoryId] || '/images/logo-parrot.svg'
}

const getEnglishName = (categoryId) => {
  const map = {
    'FISH': 'Fish',
    'DOGS': 'Dogs',
    'CATS': 'Cats',
    'REPTILES': 'Reptiles',
    'BIRDS': 'Birds'
  }
  return map[categoryId] || categoryId
}

const loadCategories = async () => {
  try {
    const res = await getCategories()
    if (res.data) {
      categories.value = res.data
    }
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

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
    isLogin.value = false
    userInfo.value = {}
    router.push('/')
  }
}

onMounted(() => {
  const updateUserInfo = () => {
    const token = localStorage.getItem('token')
    isLogin.value = !!token
    userInfo.value = JSON.parse(localStorage.getItem('userInfo') || '{}')
  }

  window.addEventListener('storage', updateUserInfo)

  loadCategories()

  return () => window.removeEventListener('storage', updateUserInfo)
})
</script>

<style scoped>
.header {
  background: var(--surface);
  border-bottom: 1px solid var(--border);
  box-shadow: var(--shadow);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-main {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.header-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  padding: 16px 0;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: opacity 0.2s;
}

.brand:hover {
  opacity: 0.85;
}

.brand-logo-wrap {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.brand-logo {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.brand-text {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.brand-text strong {
  font-size: 18px;
  color: var(--text-h);
  font-weight: 700;
}

.brand-text span {
  font-size: 11px;
  color: #999;
  letter-spacing: 0.5px;
}

.header-search {
  flex: 1;
  max-width: 480px;
}

.search-input {
  width: 100%;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 20px;
  padding: 8px 16px;
  box-shadow: 0 0 0 1px var(--border) inset;
  transition: all 0.2s;
}

.search-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--accent) inset;
}

.search-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--accent) inset;
}

.search-icon {
  color: #999;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.icon-btn {
  border: none;
  background: transparent;
  color: var(--text);
  transition: all 0.2s;
}

.icon-btn:hover {
  background: var(--accent-bg);
  color: var(--accent);
}

.cart-badge {
  display: inline-flex;
}

.cart-btn {
  position: relative;
}

.user-avatar-wrap {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.user-name {
  font-size: 14px;
  color: var(--text);
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.guest-avatar {
  cursor: pointer;
  transition: all 0.2s;
}

.guest-avatar:hover {
  background: var(--accent-bg);
}

.user-dropdown {
  cursor: pointer;
}

.header-nav {
  border-top: 1px solid var(--border);
}

.nav-list {
  display: flex;
  align-items: center;
  gap: 32px;
  list-style: none;
  padding: 0;
  margin: 0;
  min-height: 44px;
}

.nav-list li {
  position: relative;
}

.nav-list li a,
.nav-list li .nav-dropdown-trigger {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px;
  color: var(--text);
  text-decoration: none;
  font-size: 14px;
  border-radius: 6px;
  transition: all 0.2s;
  cursor: pointer;
  user-select: none;
}

.nav-list li a:hover,
.nav-list li .nav-dropdown-trigger:hover {
  background: var(--accent-bg);
  color: var(--accent);
}

.nav-list li.active a {
  background: var(--accent);
  color: #fff;
}

.chevron {
  transition: transform 0.2s;
  font-size: 12px;
}

.chevron.rotated {
  transform: rotate(180deg);
}

.nav-dropdown {
  position: relative;
}

.nav-dropdown-panel {
  position: absolute;
  top: 100%;
  left: 0;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 8px;
  box-shadow: var(--shadow);
  padding: 8px;
  min-width: 200px;
  z-index: 200;
  margin-top: 8px;
}

.nav-dropdown-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
}

.nav-dropdown-item:hover {
  background: var(--accent-bg);
}

.dropdown-icon {
  width: 32px;
  height: 32px;
  object-fit: cover;
  border-radius: 4px;
}

.dropdown-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.dropdown-cn {
  color: var(--text-h);
  font-weight: 500;
}

.dropdown-sep {
  color: #ccc;
}

.dropdown-en {
  color: #999;
  font-size: 12px;
}

@media (max-width: 768px) {
  .header-top {
    flex-wrap: wrap;
    gap: 12px;
  }

  .header-search {
    order: 3;
    flex-basis: 100%;
    max-width: 100%;
  }

  .brand-text {
    display: none;
  }

  .nav-list {
    gap: 8px;
    overflow-x: auto;
    padding-bottom: 4px;
  }

  .nav-list li a,
  .nav-list li .nav-dropdown-trigger {
    padding: 8px 12px;
    font-size: 13px;
    white-space: nowrap;
  }

  .user-name {
    display: none;
  }
}
</style>
