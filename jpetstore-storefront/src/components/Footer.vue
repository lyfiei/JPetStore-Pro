<template>
  <footer class="footer">
    <div class="footer-main">
      <div class="footer-col footer-brand">
        <div class="footer-logo-row">
          <img src="/images/logo-parrot.svg" alt="JPetStore" class="footer-logo-img" />
          <h3>JPetStore</h3>
        </div>
        <p class="footer-tagline">专业的宠物用品在线商城，为您的爱宠提供优质食品、玩具与护理用品。</p>
        <div class="footer-social">
          <span class="social-dot"></span>
          <span class="social-dot"></span>
          <span class="social-dot"></span>
          <span class="social-dot"></span>
        </div>
      </div>

      <div class="footer-col">
        <h4>快速链接</h4>
        <ul>
          <li><router-link to="/">首页</router-link></li>
          <li v-for="cat in categories" :key="cat.categoryId">
            <router-link :to="`/category/${cat.categoryId}`">{{ cat.name }}</router-link>
          </li>
          <li><router-link to="/cart">购物车</router-link></li>
          <li><router-link to="/help">帮助中心</router-link></li>
        </ul>
      </div>

      <div class="footer-col">
        <h4>宠物分类</h4>
        <ul>
          <li><router-link to="/category/BIRDS">鸟类 Birds</router-link></li>
          <li><router-link to="/category/CATS">猫咪 Cats</router-link></li>
          <li><router-link to="/category/DOGS">狗狗 Dogs</router-link></li>
          <li><router-link to="/category/FISH">鱼类 Fish</router-link></li>
          <li><router-link to="/category/REPTILES">爬虫 Reptiles</router-link></li>
        </ul>
      </div>

      <div class="footer-col footer-contact">
        <h4>联系我们</h4>
        <ul>
          <li>
            <el-icon><Message /></el-icon>
            <span>service@jpetstore.com</span>
          </li>
          <li>
            <el-icon><Phone /></el-icon>
            <span>400-123-4567</span>
          </li>
          <li>
            <el-icon><Clock /></el-icon>
            <span>周一至周日 9:00 - 21:00</span>
          </li>
          <li>
            <el-icon><Location /></el-icon>
            <span>湖南省长沙市岳麓区</span>
          </li>
        </ul>
      </div>
    </div>

    <div class="footer-bottom">
      <div class="footer-bottom-inner">
        <p>&copy; 2024 JPetStore. All Rights Reserved.</p>
        <div class="footer-links">
          <a href="#">隐私政策</a>
          <span class="divider">|</span>
          <a href="#">服务条款</a>
          <span class="divider">|</span>
          <a href="#">Cookie 设置</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Message, Phone, Clock, Location } from '@element-plus/icons-vue'
import { getCategories } from '../api/product'

const categories = ref([])

onMounted(async () => {
  try {
    const res = await getCategories()
    if (res.data) categories.value = res.data
  } catch (e) { console.error('加载分类失败:', e) }
})
</script>

<style scoped>
.footer {
  background: #1a2e1a;
  color: rgba(255,255,255,0.6);
  margin-top: 60px;
}

.footer-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 48px 20px 32px;
  display: grid;
  grid-template-columns: 1.5fr 1fr 1fr 1.2fr;
  gap: 40px;
}

.footer-brand h3 {
  color: #fff;
  font-size: 22px;
  margin: 0;
  font-weight: 700;
}

.footer-logo-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 14px;
}

.footer-logo-img {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  background: rgba(255,255,255,0.1);
  padding: 6px;
}

.footer-tagline {
  font-size: 13px;
  line-height: 1.7;
  margin: 0 0 18px;
}

.footer-social {
  display: flex;
  gap: 8px;
}

.social-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: rgba(255,255,255,0.2);
  transition: background 0.2s;
}

.social-dot:hover { background: #8fc0a3; }

.footer-col h4 {
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  margin: 0 0 16px;
  letter-spacing: 0.04em;
  text-transform: uppercase;
}

.footer-col ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-col li {
  margin: 9px 0;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.footer-col li .el-icon {
  color: #8fc0a3;
  font-size: 15px;
  flex-shrink: 0;
}

.footer-col a {
  color: rgba(255,255,255,0.55);
  text-decoration: none;
  transition: color 0.2s;
}

.footer-col a:hover {
  color: #fff;
}

.footer-bottom {
  border-top: 1px solid rgba(255,255,255,0.08);
}

.footer-bottom-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.footer-bottom p {
  margin: 0;
  font-size: 12px;
  color: rgba(255,255,255,0.3);
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 10px;
}

.footer-links a {
  color: rgba(255,255,255,0.35);
  text-decoration: none;
  font-size: 12px;
  transition: color 0.2s;
}

.footer-links a:hover { color: rgba(255,255,255,0.65); }
.footer-links .divider { color: rgba(255,255,255,0.15); font-size: 10px; }

@media (max-width: 768px) {
  .footer-main {
    grid-template-columns: 1fr 1fr;
    gap: 28px;
  }
  .footer-bottom-inner { flex-direction: column; text-align: center; }
}

@media (max-width: 480px) {
  .footer-main { grid-template-columns: 1fr; }
}
</style>
