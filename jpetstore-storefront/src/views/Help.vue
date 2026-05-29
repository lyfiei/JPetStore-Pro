<template>
  <div class="help-page">
    <section class="help-hero">
      <h1>帮助中心</h1>
      <p>有什么可以帮助您的？请查看下方常见问题或直接联系我们</p>
    </section>

    <section class="faq-section">
      <h2>常见问题</h2>
      <div class="faq-list">
        <div class="faq-item">
          <h3>
            <el-icon><QuestionFilled /></el-icon>
            如何注册账号？
          </h3>
          <p>点击页面右上角的用户图标进入登录页面，选择"还没有账号？立即注册"，填写用户名、密码及个人信息即可完成注册。新用户注册即送 100 元优惠券。</p>
        </div>
        <div class="faq-item">
          <h3>
            <el-icon><QuestionFilled /></el-icon>
            如何下单购买商品？
          </h3>
          <p>浏览商品分类或使用搜索功能找到心仪的商品，进入商品详情页选择规格和数量后点击"加入购物车"，在购物车中确认订单后进入结算页面完成支付即可。</p>
        </div>
        <div class="faq-item">
          <h3>
            <el-icon><QuestionFilled /></el-icon>
            支持哪些支付方式？
          </h3>
          <p>目前支持微信支付和支付宝支付，您可以在结算页面选择偏好的支付方式完成付款。</p>
        </div>
        <div class="faq-item">
          <h3>
            <el-icon><QuestionFilled /></el-icon>
            配送需要多长时间？
          </h3>
          <p>全国主要城市 48 小时内送达。订单满 199 元即可享受免费配送服务。具体送达时间以物流信息为准。</p>
        </div>
        <div class="faq-item">
          <h3>
            <el-icon><QuestionFilled /></el-icon>
            如何退换货？
          </h3>
          <p>支持 7 天无理由退换货。请在个人中心的订单页面申请退换货，我们的客服会在 24 小时内与您联系处理。</p>
        </div>
      </div>
    </section>

    <section class="help-contact">
      <h2>联系我们</h2>
      <div class="help-contact-grid">
        <div class="help-contact-card">
          <el-icon size="28"><Message /></el-icon>
          <h4>电子邮箱</h4>
          <p>service@jpetstore.com</p>
        </div>
        <div class="help-contact-card">
          <el-icon size="28"><Phone /></el-icon>
          <h4>客服电话</h4>
          <p>400-123-4567</p>
        </div>
        <div class="help-contact-card">
          <el-icon size="28"><Clock /></el-icon>
          <h4>服务时间</h4>
          <p>周一至周日 9:00 - 21:00</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { QuestionFilled, Message, Phone, Clock } from '@element-plus/icons-vue'
</script>

<style scoped>
.help-page {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
}

.help-hero {
  text-align: center;
  padding: 48px 0 32px;
}

.help-hero h1 {
  font-size: 36px;
  font-weight: 700;
  color: var(--text-h);
  margin: 0 0 12px;
}

.help-hero p {
  font-size: 16px;
  color: #8a9a7c;
}

.faq-section {
  margin-bottom: 40px;
}

.faq-section h2,
.help-contact h2 {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-h);
  margin: 0 0 20px;
}

.faq-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.faq-item {
  background: #fff;
  border-radius: 14px;
  padding: 22px 26px;
  border: 1px solid #ece6d8;
  transition: box-shadow 0.2s;
}

.faq-item:hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.05);
}

.faq-item h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-h);
  margin: 0 0 10px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.faq-item h3 .el-icon {
  color: var(--accent);
}

.faq-item p {
  font-size: 14px;
  color: #6d7d64;
  line-height: 1.7;
  margin: 0;
  padding-left: 26px;
}

.help-contact-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.help-contact-card {
  background: #fff;
  border-radius: 14px;
  padding: 28px 20px;
  text-align: center;
  border: 1px solid #ece6d8;
  transition: transform 0.2s, box-shadow 0.2s;
}

.help-contact-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0,0,0,0.06);
}

.help-contact-card .el-icon {
  color: var(--accent);
  margin-bottom: 12px;
}

.help-contact-card h4 {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-h);
  margin: 0 0 6px;
}

.help-contact-card p {
  font-size: 14px;
  color: #8a9a7c;
  margin: 0;
}

@media (max-width: 600px) {
  .help-contact-grid {
    grid-template-columns: 1fr;
  }
}
</style>
