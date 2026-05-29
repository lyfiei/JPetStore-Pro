<script setup>
import { RouterView } from 'vue-router'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
</script>

<template>
  <div id="app">
    <Header />
    <main class="main-content">
      <RouterView />
    </main>
    <Footer />
  </div>
</template>

<style>
:root {
  --text: #3d4f3b;
  --text-h: #1a2e1a;
  --bg: #faf7f0;
  --surface: #ffffff;
  --surface-muted: #f5f0e6;
  --border: #e0d8c8;
  --accent: #3a7d5c;
  --accent-dark: #2d5f45;
  --accent-bg: rgba(58, 125, 92, 0.1);
  --orange: #e8913a;
  --orange-dark: #c57a2a;
  --shadow: rgba(22, 35, 18, 0.06) 0 14px 35px -8px, rgba(22, 35, 18, 0.03) 0 6px 16px -8px;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: var(--text);
  background: var(--bg);
  min-height: 100vh;
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: var(--bg);
}

.main-content {
  flex: 1;
  width: 100%;
}

/* Element Plus global overrides */
.el-button--primary {
  --el-button-bg-color: var(--accent);
  --el-button-border-color: var(--accent);
  --el-button-hover-bg-color: var(--accent-dark);
  --el-button-hover-border-color: var(--accent-dark);
  --el-button-active-bg-color: var(--accent-dark);
  --el-button-active-border-color: var(--accent-dark);
}

.el-button--warning {
  --el-button-bg-color: var(--orange);
  --el-button-border-color: var(--orange);
  --el-button-hover-bg-color: var(--orange-dark);
  --el-button-hover-border-color: var(--orange-dark);
}
</style>
