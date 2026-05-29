<template>
  <div class="order-detail-page">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/profile' }">个人中心</el-breadcrumb-item>
      <el-breadcrumb-item>订单详情</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- 订单详情 -->
    <div v-else-if="order" class="order-content">
      <!-- 订单状态卡片 -->
      <el-card class="status-card">
        <div class="status-header">
          <div class="status-info">
            <h2>订单号: {{ order.orderId }}</h2>
            <p class="order-date">下单时间: {{ formatDate(order.orderDate) }}</p>
          </div>
          <el-tag 
            :type="getStatusType(order.status)" 
            size="large"
            class="status-tag"
          >
            {{ getStatusText(order.status) }}
          </el-tag>
        </div>
      </el-card>

      <!-- 订单商品 -->
      <el-card class="items-card">
        <template #header>
          <h3>商品信息</h3>
        </template>
        
        <div class="order-items">
          <div 
            v-for="item in order.lineItems" 
            :key="item.lineItemId"
            class="order-item"
          >
            <div class="item-image">
              <img 
                :src="getItemImage(item.itemId)" 
                alt="商品图片"
                @error="handleImageError"
              />
            </div>
            <div class="item-info">
              <h4 class="item-name">{{ item.productName || '商品' }}</h4>
              <p class="item-id">商品编号: {{ item.itemId }}</p>
            </div>
            <div class="item-details">
              <div class="item-price">
                <span class="label">单价:</span>
                <span class="value">¥{{ item.unitPrice }}</span>
              </div>
              <div class="item-quantity">
                <span class="label">数量:</span>
                <span class="value">{{ item.quantity }}</span>
              </div>
              <div class="item-total">
                <span class="label">小计:</span>
                <span class="value price">¥{{ item.total }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 订单信息 -->
      <el-row :gutter="20">
        <!-- 收货地址 -->
        <el-col :xs="24" :sm="12">
          <el-card class="info-card">
            <template #header>
              <h3>收货地址</h3>
            </template>
            
            <div class="address-info">
              <p><strong>收货人:</strong> {{ order.shipToFirstName }} {{ order.shipToLastName }}</p>
              <p><strong>电话:</strong> {{ order.shipPhone || '未提供' }}</p>
              <p><strong>地址:</strong> {{ formatAddress(order) }}</p>
            </div>
          </el-card>
        </el-col>

        <!-- 账单地址 -->
        <el-col :xs="24" :sm="12">
          <el-card class="info-card">
            <template #header>
              <h3>账单地址</h3>
            </template>
            
            <div class="address-info">
              <p><strong>姓名:</strong> {{ order.billToFirstName }} {{ order.billToLastName }}</p>
              <p><strong>地址:</strong> {{ formatBillAddress(order) }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 支付信息 -->
      <el-card class="payment-card">
        <template #header>
          <h3>支付信息</h3>
        </template>
        
        <div class="payment-info">
          <div class="payment-row">
            <span class="label">支付方式:</span>
            <span class="value">{{ order.cardType || '信用卡' }}</span>
          </div>
          <div class="payment-row">
            <span class="label">卡号:</span>
            <span class="value">{{ maskCardNumber(order.creditCard) }}</span>
          </div>
          <div class="payment-row">
            <span class="label">配送方式:</span>
            <span class="value">{{ order.courier || '标准配送' }}</span>
          </div>
        </div>
      </el-card>

      <!-- 订单总计 -->
      <el-card class="total-card">
        <div class="total-summary">
          <div class="total-row">
            <span class="label">商品总数:</span>
            <span class="value">{{ getTotalQuantity(order.lineItems) }} 件</span>
          </div>
          <div class="total-row grand-total">
            <span class="label">订单总计:</span>
            <span class="value price">¥{{ order.totalPrice }}</span>
          </div>
        </div>
      </el-card>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button @click="goBack">返回</el-button>
        <el-button type="primary" @click="contactSupport">联系客服</el-button>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty v-else description="订单不存在" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getOrderDetail } from '../api/order'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const order = ref(null)

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

// 获取订单状态文本
const getStatusText = (status) => {
  const statusMap = {
    'P': '待处理',
    'S': '已发货',
    'C': '已完成',
    'X': '已取消'
  }
  return statusMap[status] || status
}

// 获取订单状态类型
const getStatusType = (status) => {
  const typeMap = {
    'P': 'warning',
    'S': 'primary',
    'C': 'success',
    'X': 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取商品图片
const getItemImage = (itemId) => {
  return '/images/placeholder.png'
}

const handleImageError = (e) => {
  e.target.src = '/images/placeholder.png'
}

// 格式化收货地址
const formatAddress = (order) => {
  const parts = [
    order.shipAddress1,
    order.shipAddress2,
    order.shipCity,
    order.shipState,
    order.shipZip,
    order.shipCountry
  ]
  return parts.filter(p => p).join(', ')
}

// 格式化账单地址
const formatBillAddress = (order) => {
  const parts = [
    order.billAddress1,
    order.billAddress2,
    order.billCity,
    order.billState,
    order.billZip,
    order.billCountry
  ]
  return parts.filter(p => p).join(', ')
}

// 隐藏卡号
const maskCardNumber = (cardNumber) => {
  if (!cardNumber) return '未提供'
  return `**** **** **** ${cardNumber.slice(-4)}`
}

// 获取商品总数量
const getTotalQuantity = (lineItems) => {
  if (!lineItems) return 0
  return lineItems.reduce((sum, item) => sum + item.quantity, 0)
}

// 返回
const goBack = () => {
  router.back()
}

// 联系客服
const contactSupport = () => {
  ElMessage.info('客服功能开发中...')
}

// 加载订单详情
const loadOrderDetail = async () => {
  const orderId = route.params.orderId
  if (!orderId) return

  loading.value = true
  try {
    const res = await getOrderDetail(orderId)
    order.value = res.data
  } catch (error) {
    console.error('加载订单详情失败:', error)
    ElMessage.error('加载订单详情失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadOrderDetail()
})
</script>

<style scoped>
.order-detail-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.breadcrumb {
  margin-bottom: 24px;
}

.loading-container {
  padding: 40px;
  background: white;
  border-radius: 12px;
}

.order-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.status-card {
  background: linear-gradient(135deg, var(--accent) 0%, var(--accent-dark) 100%);
  color: white;
}

.status-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.status-info h2 {
  font-size: 24px;
  margin: 0 0 8px 0;
}

.order-date {
  margin: 0;
  opacity: 0.9;
}

.status-tag {
  font-size: 16px;
  padding: 8px 16px;
}

.items-card,
.info-card,
.payment-card,
.total-card {
  border-radius: 12px;
}

.items-card h3,
.info-card h3,
.payment-card h3 {
  margin: 0;
  font-size: 18px;
  color: #2c3e50;
}

.order-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-item {
  display: grid;
  grid-template-columns: 80px 1fr auto;
  gap: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
}

.item-image {
  width: 80px;
  height: 80px;
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: #2c3e50;
}

.item-id {
  margin: 0;
  font-size: 13px;
  color: #7f8c8d;
}

.item-details {
  display: flex;
  gap: 24px;
  align-items: center;
}

.item-price,
.item-quantity,
.item-total {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.item-price .label,
.item-quantity .label,
.item-total .label {
  font-size: 12px;
  color: #7f8c8d;
}

.item-price .value,
.item-quantity .value {
  font-size: 14px;
  color: #495057;
}

.item-total .value.price {
  font-size: 18px;
  font-weight: 600;
  color: var(--accent);
}

.address-info p {
  margin: 8px 0;
  line-height: 1.6;
}

.payment-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.payment-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
}

.payment-row .label {
  color: #7f8c8d;
}

.payment-row .value {
  color: #2c3e50;
  font-weight: 500;
}

.total-summary {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.total-row {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  font-size: 16px;
}

.total-row.grand-total {
  border-top: 2px solid #e9ecef;
  padding-top: 20px;
  font-size: 20px;
}

.total-row .label {
  color: #7f8c8d;
}

.total-row .value {
  font-weight: 500;
  color: #2c3e50;
}

.total-row .value.price {
  font-size: 28px;
  font-weight: 600;
  color: var(--accent);
}

.action-buttons {
  display: flex;
  gap: 12px;
  justify-content: center;
  padding: 20px 0;
}

@media (max-width: 768px) {
  .status-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }

  .order-item {
    grid-template-columns: 1fr;
  }

  .item-details {
    justify-content: space-between;
  }

  .action-buttons {
    flex-direction: column;
  }
}
</style>
