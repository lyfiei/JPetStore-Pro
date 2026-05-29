<template>
  <div class="home-page">
    <!-- Hero Banner -->
    <section class="hero-section">
      <div class="hero-carousel">
        <div
          v-for="(slide, idx) in banners"
          :key="slide.id"
          class="hero-slide"
          :class="{ active: currentSlide === idx }"
        >
          <div class="hero-card">
            <div class="hero-copy">
              <span class="hero-tag">{{ slide.tag }}</span>
              <h1 class="hero-title">{{ slide.title }}</h1>
              <p class="hero-desc">{{ stripHtml(slide.description) }}</p>
              <button class="hero-cta" @click="goToCategory(slide.categoryId)">
                {{ slide.ctaText }}
                <el-icon><ArrowRight /></el-icon>
              </button>
            </div>
            <div class="hero-visual">
              <div class="hero-deco-circle hero-deco-lg"></div>
              <div class="hero-deco-circle hero-deco-md"></div>
              <div class="hero-photo-wrap">
                <img :src="slide.image" :alt="slide.title" class="hero-photo" />
              </div>
            </div>
          </div>
        </div>

        <button class="carousel-arrow carousel-prev" @click="prevSlide">
          <el-icon><ArrowLeft /></el-icon>
        </button>
        <button class="carousel-arrow carousel-next" @click="nextSlide">
          <el-icon><ArrowRight /></el-icon>
        </button>

        <div class="carousel-dots">
          <span
            v-for="(s, i) in banners"
            :key="i"
            class="carousel-dot"
            :class="{ active: i === currentSlide }"
            @click="currentSlide = i"
          ></span>
        </div>
      </div>
    </section>

    <!-- Category Cards -->
    <section class="category-section">
      <div class="section-head">
        <h2 class="section-title">多样宠物用品，一站式购齐</h2>
      </div>

      <div class="category-grid">
        <div
          v-for="cat in categories"
          :key="cat.categoryId"
          class="category-card"
          @click="goToCategory(cat.categoryId)"
        >
          <div class="category-card-img">
            <img :src="getCategoryImage(cat.categoryId)" :alt="cat.name" />
          </div>
          <div class="category-card-body">
            <h3 class="category-card-title">
              <span class="cn">{{ cat.name }}</span>
              <span class="sep">|</span>
              <span class="en">{{ getEnglishName(cat.categoryId) }}</span>
            </h3>
            <p class="category-card-desc">{{ stripHtml(cat.description) }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Customer Service Widget -->
    <div class="support-widget" @click="goToContact">
      <el-icon size="20"><ChatDotRound /></el-icon>
      <div class="support-text">
        <span class="support-brand">JPetStore 客服</span>
        <span class="support-sub">在线咨询</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowRight, ArrowLeft, ChatDotRound } from '@element-plus/icons-vue'
import { getCategories } from '../api/product'
import { stripHtml } from '../utils/format'

const router = useRouter()
const currentSlide = ref(0)
let autoplayTimer = null

const banners = [
  {
    id: 1,
    tag: '新会员福利',
    title: '注册即送 100 元优惠券',
    description: '精选宠物用品与护理用品，温暖陪伴每一天。注册即享新人专属好礼！',
    ctaText: '立即选购',
    categoryId: 'DOGS',
    image: '/images/hero-banner.jpg'
  },
  {
    id: 2,
    tag: '爆款推荐',
    title: '萌宠鱼类精选',
    description: '缤纷热带鱼、金鱼与海水鱼，为你的水族箱增添生机与色彩。',
    ctaText: '去鱼类专区',
    categoryId: 'FISH',
    image: '/images/fish-category.jpg'
  },
  {
    id: 3,
    tag: '品质保障',
    title: '猫咪用品专区',
    description: '精选猫粮、猫砂与猫玩具，给猫咪最贴心的呵护。',
    ctaText: '浏览猫咪用品',
    categoryId: 'CATS',
    image: '/images/cat-category.jpg'
  }
]

const categories = ref([])

const prevSlide = () => {
  currentSlide.value = (currentSlide.value - 1 + banners.length) % banners.length
  resetAutoplay()
}

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % banners.length
  resetAutoplay()
}

const startAutoplay = () => {
  stopAutoplay()
  autoplayTimer = setInterval(() => {
    currentSlide.value = (currentSlide.value + 1) % banners.length
  }, 4000)
}

const stopAutoplay = () => {
  if (autoplayTimer) {
    clearInterval(autoplayTimer)
    autoplayTimer = null
  }
}

const resetAutoplay = () => {
  stopAutoplay()
  startAutoplay()
}

const goToCategory = (categoryId) => {
  router.push(`/category/${categoryId}`)
}

const goToContact = () => {
  router.push('/help')
}

const getCategoryImage = (categoryId) => {
  const map = {
    'BIRDS': '/images/bird-category.jpg',
    'CATS': '/images/cat-category.jpg',
    'DOGS': '/images/dog-category.jpg',
    'FISH': '/images/fish-category.jpg',
    'REPTILES': '/images/reptile-category.jpg',
  }
  return map[categoryId] || '/images/hero-banner.jpg'
}

const getEnglishName = (categoryId) => {
  const map = {
    'FISH': 'Fish',
    'DOGS': 'Dogs',
    'CATS': 'Cats',
    'REPTILES': 'Reptiles',
    'BIRDS': 'Birds',
  }
  return map[categoryId] || categoryId
}

onMounted(async () => {
  startAutoplay()
  try {
    const res = await getCategories()
    if (res.data) {
      categories.value = res.data
    }
  } catch (e) {
    console.error('加载分类失败:', e)
  }
})

onBeforeUnmount(() => {
  stopAutoplay()
})
</script>

<style scoped>
.home-page {
  position: relative;
}

/* ── Hero Banner ── */
.hero-section {
  margin-bottom: 48px;
  padding: 0 20px;
}

.hero-carousel {
  position: relative;
  border-radius: 24px;
  overflow: hidden;
  background: linear-gradient(135deg, #faf7f0 0%, #f3efe4 40%, #ece4d2 100%);
}

.hero-slide {
  position: absolute;
  inset: 0;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.5s ease;
}

.hero-slide.active {
  position: relative;
  opacity: 1;
  pointer-events: auto;
}

.hero-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 48px 80px 40px 56px;
  gap: 32px;
  min-height: 340px;
}

.hero-copy {
  max-width: 440px;
  z-index: 2;
}

.hero-tag {
  display: inline-block;
  background: rgba(255,255,255,0.6);
  color: var(--accent-dark);
  border-radius: 999px;
  padding: 5px 16px;
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 16px;
}

.hero-title {
  font-size: 34px;
  font-weight: 700;
  line-height: 1.25;
  color: #1a2e1a;
  margin: 0 0 14px;
  letter-spacing: -0.3px;
}

.hero-desc {
  font-size: 15px;
  color: #7a8a70;
  line-height: 1.8;
  margin: 0 0 28px;
}

.hero-cta {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 13px 28px;
  border: none;
  border-radius: 24px;
  background: #e8dcc0;
  color: #4a3a1a;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s;
  font-family: inherit;
}

.hero-cta:hover {
  background: #d8c8a0;
  transform: translateY(-1px);
}

.hero-visual {
  flex-shrink: 0;
  position: relative;
  width: 260px;
  height: 260px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.hero-deco-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255,255,255,0.55);
}

.hero-deco-lg {
  width: 220px;
  height: 220px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.hero-deco-md {
  width: 130px;
  height: 130px;
  top: 15px;
  right: -10px;
}

.hero-photo-wrap {
  position: relative;
  z-index: 1;
  width: 190px;
  height: 190px;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 0 12px 36px rgba(0,0,0,0.1);
}

.hero-photo {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* Carousel controls */
.carousel-arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: none;
  background: #fff;
  color: #555;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  z-index: 5;
}

.carousel-arrow:hover {
  background: var(--accent);
  color: #fff;
}

.carousel-prev { left: 16px; }
.carousel-next { right: 16px; }

.carousel-dots {
  position: absolute;
  bottom: 16px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 8px;
  z-index: 5;
}

.carousel-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: rgba(0,0,0,0.12);
  cursor: pointer;
  transition: all 0.3s;
}

.carousel-dot.active {
  background: var(--accent);
  width: 22px;
  border-radius: 4px;
}

/* ── Category Section ── */
.category-section {
  margin-bottom: 48px;
  padding: 0 20px;
}

.section-head {
  margin-bottom: 22px;
}

.section-title {
  font-size: 28px;
  font-weight: 700;
  color: #1a2e1a;
  margin: 0;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.category-card {
  display: flex;
  align-items: center;
  gap: 20px;
  background: #fff;
  border-radius: 18px;
  padding: 20px 24px;
  cursor: pointer;
  transition: transform 0.25s, box-shadow 0.25s;
  box-shadow: 0 4px 16px rgba(0,0,0,0.03);
  border: 1px solid #ece6d8;
}

.category-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 28px rgba(0,0,0,0.07);
  border-color: rgba(58,125,92,0.15);
}

.category-card-img {
  width: 80px;
  height: 80px;
  border-radius: 14px;
  background: #f0ece0;
  flex-shrink: 0;
  overflow: hidden;
}

.category-card-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.category-card-body { flex: 1; min-width: 0; }

.category-card-title {
  font-size: 17px;
  font-weight: 700;
  color: #1a2e1a;
  margin: 0 0 6px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.category-card-title .cn { white-space: nowrap; }
.category-card-title .sep { color: #d0c8b8; font-weight: 300; }
.category-card-title .en { color: #999; font-weight: 400; font-size: 14px; }

.category-card-desc {
  font-size: 13px;
  color: #8a9a7c;
  margin: 0;
  line-height: 1.5;
}

/* ── Support Widget ── */
.support-widget {
  position: fixed;
  right: 20px;
  bottom: 28px;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 20px;
  background: linear-gradient(135deg, #3d5f4a, #4a6b53);
  color: #fff;
  border-radius: 26px;
  box-shadow: 0 10px 32px rgba(58,95,70,0.3);
  cursor: pointer;
  z-index: 20;
  transition: transform 0.25s, box-shadow 0.25s;
}

.support-widget:hover {
  transform: translateY(-2px);
  box-shadow: 0 14px 40px rgba(58,95,70,0.4);
}

.support-text { display: flex; flex-direction: column; line-height: 1.3; }
.support-brand { font-size: 14px; font-weight: 700; }
.support-sub { font-size: 12px; color: rgba(255,255,255,0.78); }

/* ── Responsive ── */
@media (max-width: 900px) {
  .hero-card {
    flex-direction: column;
    text-align: center;
    padding: 36px 24px 30px;
    gap: 20px;
  }

  .hero-title { font-size: 26px; }
  .hero-visual { width: 180px; height: 180px; }
  .hero-photo-wrap { width: 140px; height: 140px; }
  .hero-deco-lg { width: 160px; height: 160px; }
  .hero-deco-md { width: 100px; height: 100px; }

  .category-grid { grid-template-columns: 1fr; }
  .section-title { font-size: 24px; }
}

@media (max-width: 600px) {
  .support-widget {
    right: 10px; bottom: 10px;
    padding: 10px 16px;
    border-radius: 22px;
  }
  .support-brand { font-size: 12px; }
  .support-sub { font-size: 11px; }
}
</style>
