<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-section">
        <h3> JPetStore</h3>
        <p>专业的宠物用品在线商城</p>
        <p>为您提供优质的宠物食品和用品</p>
      </div>
      
      <div class="footer-section">
        <h3>快速链接</h3>
        <ul>
          <li><router-link to="/">首页</router-link></li>
          <li><router-link to="/category/FISH">鱼类</router-link></li>
          <li><router-link to="/category/DOGS">犬类</router-link></li>
          <li><router-link to="/cart">购物车</router-link></li>
        </ul>
      </div>
      
      <div class="footer-section">
        <h3>联系我们</h3>
        <p>📧 service@jpetstore.com</p>
        <p>📞 400-123-4567</p>
      </div>
    </div>
    
    <div class="footer-bottom">
      <p>&copy; 2024 JPetStore. All Rights Reserved.</p>
    </div>
  </footer>
</template>

<script setup>
// 组件逻辑
</script>

<style scoped>
.footer {
  background: #2c3e50;
  color: #ecf0f1;
  padding: 30px 0 0;
  margin-top: 40px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 30px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
}

.footer-section h3 {
  color: #ffd04b;
  margin-bottom: 15px;
  font-size: 16px;
}

.footer-section p {
  margin: 8px 0;
  font-size: 14px;
  line-height: 1.6;
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section ul li {
  margin: 8px 0;
}

.footer-section ul li a {
  color: #ecf0f1;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-section ul li a:hover {
  color: #ffd04b;
}

.footer-bottom {
  background: #1a252f;
  text-align: center;
  padding: 15px;
}

.footer-bottom p {
  margin: 0;
  font-size: 14px;
  color: #95a5a6;
}
</style>
