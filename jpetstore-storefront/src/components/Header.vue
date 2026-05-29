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
              <span class="user-name">{{ userInfo?.username || 'lyf' }}</span>
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
const isLogin = computed(() => !!localStorage.getItem('token'))
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
    router.push('/')
    location.reload()
  }
}

onMounted(() => {
  const updateUserInfo = () => {
    userInfo.value = JSON.parse(localStorage.getItem('userInfo') || '{}')
  }
  window.addEventListener('storage', updateUserInfo)
  loadCategories()
  return () => window.removeEventListener('storage', updateUserInfo)
})
</script>

<style scoped>
.header {
  background-color: #ffffff;
  border-bottom: 1px solid #e8e0d0;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px 20px 0;
}

.header-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

/* Brand */
.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  flex-shrink: 0;
}

.brand-logo-wrap {
  width: 42px;
  height: 42px;
  border-radius: 12px;
  background: linear-gradient(135deg, #e8f0e2, #d4e4c8);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.brand-logo {
  width: 30px;
  height: 30px;
  border-radius: 6px;
}

.brand-text {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.brand-text strong {
  font-size: 19px;
  letter-spacing: -0.2px;
  color: #1a3a22;
  font-weight: 700;
}

.brand-text span {
  font-size: 10px;
  color: #7a9a7c;
  letter-spacing: 0.08em;
}

/* Search */
.header-search {
  flex: 1;
  max-width: 440px;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 22px;
  background: #f5f2ea;
  border: 1.5px solid #e0d8c0;
  box-shadow: none;
  padding-left: 12px;
  transition: all 0.2s;
}

.search-input :deep(.el-input__wrapper:hover) {
  border-color: #c0b090;
  background: #f0ece0;
}

.search-input :deep(.el-input__wrapper.is-focus) {
  background: #fff;
  border-color: var(--accent);
  box-shadow: 0 0 0 3px var(--accent-bg);
}

.search-icon {
  color: #b0a890;
}

/* Actions */
.header-actions {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-shrink: 0;
}

.icon-btn {
  width: 38px;
  height: 38px;
  border: none !important;
  background: transparent !important;
  color: var(--text-h) !important;
  transition: background 0.2s, color 0.2s;
}

.icon-btn:hover {
  background: var(--surface-muted) !important;
  color: var(--accent) !important;
}

.cart-badge :deep(.el-badge__content) {
  background: var(--accent);
  border: 2px solid var(--bg);
}

.user-avatar-wrap {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 2px 10px 2px 2px;
  border-radius: 22px;
  transition: background 0.2s, box-shadow 0.2s;
  background: #f5f2ea;
  border: 1px solid #e8e0d0;
}

.user-avatar-wrap:hover {
  background: #ece6d8;
  border-color: #d0c8b0;
}

.user-avatar {
  background: var(--accent-bg) !important;
  color: var(--accent) !important;
  font-weight: 600;
  font-size: 14px;
}

.guest-avatar {
  cursor: pointer;
  background: #fff !important;
  color: var(--text) !important;
  border: 1.5px solid #d0c8b0;
}

.guest-avatar:hover {
  background: var(--accent-bg) !important;
  color: var(--accent) !important;
  border-color: var(--accent);
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-h);
}

/* Nav */
.header-nav {
  margin-top: 14px;
  padding-bottom: 2px;
}

.nav-list {
  list-style: none;
  display: flex;
  gap: 2px;
  padding: 0;
  margin: 0;
}

.nav-list > li {
  position: relative;
}

.nav-dropdown {
  overflow: visible;
}

.nav-list a,
.nav-dropdown-trigger {
  color: var(--text);
  font-weight: 500;
  font-size: 15px;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 10px 18px;
  border-radius: 10px;
  transition: all 0.2s;
  cursor: pointer;
}

.nav-list a:hover,
.nav-dropdown-trigger:hover {
  background: rgba(0,0,0,0.03);
  color: var(--text-h);
}

.nav-list a .el-icon {
  font-size: 15px;
}

.nav-list li.active > a {
  color: var(--accent);
  background: var(--accent-bg);
}

.chevron {
  font-size: 11px;
  transition: transform 0.25s;
}

.chevron.rotated {
  transform: rotate(180deg);
}

/* Dropdown panel */
.nav-dropdown-panel {
  position: absolute;
  top: 44px;
  left: 0;
  background: #fff;
  border-radius: 14px;
  padding: 10px 6px;
  min-width: 220px;
  box-shadow: 0 12px 36px rgba(0,0,0,0.1);
  z-index: 1000;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.nav-dropdown-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 14px;
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.15s;
}

.nav-dropdown-item:hover {
  background: #f5f2ea;
}

.dropdown-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  object-fit: cover;
  background: #f8f5ee;
  padding: 4px;
  flex-shrink: 0;
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
  font-weight: 300;
}

.dropdown-en {
  color: #999;
  font-size: 13px;
}

@media (max-width: 768px) {
  .header-top {
    flex-wrap: wrap;
    gap: 12px;
  }

  .header-search {
    order: 3;
    max-width: 100%;
    min-width: 100%;
  }

  .nav-list a,
  .nav-dropdown-trigger {
    padding: 8px 14px;
    font-size: 14px;
  }
}
</style>
