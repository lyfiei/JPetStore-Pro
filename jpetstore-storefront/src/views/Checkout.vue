<template>
  <div class="checkout-page">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/cart' }">购物车</el-breadcrumb-item>
      <el-breadcrumb-item>订单确认</el-breadcrumb-item>
    </el-breadcrumb>

    <h1 class="page-title">订单确认</h1>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- 结算内容 -->
    <div v-else class="checkout-content">
      <el-row :gutter="20">
        <!-- 左侧：收货地址和支付信息 -->
        <el-col :xs="24" :md="16">
          <!-- 收货地址 -->
          <el-card class="address-section">
            <template #header>
              <div class="card-header">
                <h3>收货地址</h3>
                <el-button type="primary" link @click="showAddressDialog">
                  选择其他地址
                </el-button>
              </div>
            </template>
            
            <div v-if="selectedAddress" class="address-display">
              <div class="address-name">
                {{ selectedAddress.firstName }} {{ selectedAddress.lastName }}
                <el-tag v-if="selectedAddress.isDefault" type="success" size="small">默认</el-tag>
              </div>
              <p class="address-phone">{{ selectedAddress.phone }}</p>
              <p class="address-detail">
                {{ selectedAddress.address1 }} {{ selectedAddress.address2 }}
                {{ selectedAddress.city }}, {{ selectedAddress.state }} {{ selectedAddress.zip }}
                {{ selectedAddress.country }}
              </p>
            </div>
            <el-empty v-else description="请添加收货地址">
              <el-button type="primary" @click="showAddressDialog">添加地址</el-button>
            </el-empty>
          </el-card>

          <!-- 配送方式 -->
          <el-card class="shipping-section">
            <template #header>
              <h3>配送方式</h3>
            </template>
            
            <el-radio-group v-model="shippingMethod" class="shipping-options">
              <el-radio label="UPS" border>
                <div class="shipping-option">
                  <div class="option-name">标准配送</div>
                  <div class="option-desc">3-5个工作日</div>
                </div>
              </el-radio>
              <el-radio label="FedEx" border>
                <div class="shipping-option">
                  <div class="option-name">快速配送</div>
                  <div class="option-desc">1-2个工作日</div>
                </div>
              </el-radio>
            </el-radio-group>
          </el-card>

          <!-- 支付信息 -->
          <el-card class="payment-section">
            <template #header>
              <h3>支付信息</h3>
            </template>
            
            <el-form :model="paymentForm" :rules="paymentRules" ref="paymentFormRef" label-width="100px">
              <el-form-item label="卡类型" prop="cardType">
                <el-select v-model="paymentForm.cardType" placeholder="请选择卡类型" style="width: 100%">
                  <el-option label="Visa" value="Visa" />
                  <el-option label="MasterCard" value="MasterCard" />
                  <el-option label="American Express" value="Amex" />
                </el-select>
              </el-form-item>
              
              <el-form-item label="卡号" prop="creditCard">
                <el-input 
                  v-model="paymentForm.creditCard" 
                  placeholder="请输入卡号"
                  maxlength="16"
                />
              </el-form-item>
              
              <el-form-item label="有效期" prop="expiryDate">
                <el-input 
                  v-model="paymentForm.expiryDate" 
                  placeholder="MM/YYYY"
                  maxlength="7"
                />
              </el-form-item>
            </el-form>
          </el-card>

          <!-- 订单备注 -->
          <el-card class="notes-section">
            <template #header>
              <h3>订单备注（选填）</h3>
            </template>
            
            <el-input
              v-model="orderNotes"
              type="textarea"
              :rows="3"
              placeholder="如有特殊要求，请在此留言"
            />
          </el-card>
        </el-col>

        <!-- 右侧：订单摘要 -->
        <el-col :xs="24" :md="8">
          <el-card class="summary-card">
            <template #header>
              <h3>订单摘要</h3>
            </template>
            
            <!-- 商品列表 -->
            <div class="summary-items">
              <div 
                v-for="item in cartStore.cartItems" 
                :key="item.itemId"
                class="summary-item"
              >
                <div class="item-basic">
                  <span class="item-name">{{ item.productName }}</span>
                  <span class="item-quantity">x{{ item.quantity }}</span>
                </div>
                <div class="item-price">¥{{ (item.listPrice * item.quantity).toFixed(2) }}</div>
              </div>
            </div>

            <el-divider />

            <!-- 价格明细 -->
            <div class="summary-details">
              <div class="detail-row">
                <span class="label">商品总额:</span>
                <span class="value">¥{{ cartStore.cartTotal.toFixed(2) }}</span>
              </div>
              <div class="detail-row">
                <span class="label">运费:</span>
                <span class="value">¥{{ shippingFee.toFixed(2) }}</span>
              </div>
              <div class="detail-row total">
                <span class="label">应付总额:</span>
                <span class="value price">¥{{ (cartStore.cartTotal + shippingFee).toFixed(2) }}</span>
              </div>
            </div>

            <!-- 提交订单按钮 -->
            <el-button 
              type="primary" 
              size="large"
              class="submit-btn"
              :loading="submitting"
              :disabled="!canSubmit"
              @click="submitOrder"
            >
              提交订单
            </el-button>

            <p class="submit-hint">点击提交订单即表示您同意我们的服务条款</p>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 地址选择对话框 -->
    <el-dialog 
      v-model="addressDialogVisible" 
      title="选择收货地址"
      width="600px"
    >
      <div v-if="addresses.length === 0" class="empty-addresses">
        <el-empty description="暂无收货地址">
          <el-button type="primary" @click="addNewAddress">添加新地址</el-button>
        </el-empty>
      </div>
      <div v-else class="address-list">
        <div 
          v-for="address in addresses" 
          :key="address.addressId"
          class="address-option"
          :class="{ selected: selectedAddress?.addressId === address.addressId }"
          @click="selectAddress(address)"
        >
          <div class="address-header">
            <span class="name">{{ address.firstName }} {{ address.lastName }}</span>
            <span class="phone">{{ address.phone }}</span>
            <el-tag v-if="address.isDefault" type="success" size="small">默认</el-tag>
          </div>
          <div class="address-detail">
            {{ address.address1 }} {{ address.address2 }}, {{ address.city }}, {{ address.state }} {{ address.zip }}
          </div>
        </div>
        
        <el-button type="primary" plain style="width: 100%; margin-top: 16px" @click="addNewAddress">
          添加新地址
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCartStore } from '../stores/cart'
import { createOrder } from '../api/order'
import { getAddressList } from '../api/address'

const router = useRouter()
const cartStore = useCartStore()

const loading = ref(false)
const submitting = ref(false)
const addresses = ref([])
const selectedAddress = ref(null)
const shippingMethod = ref('UPS')
const orderNotes = ref('')
const addressDialogVisible = ref(false)

// 支付表单
const paymentFormRef = ref(null)
const paymentForm = reactive({
  cardType: 'Visa',
  creditCard: '',
  expiryDate: ''
})

const paymentRules = {
  cardType: [{ required: true, message: '请选择卡类型', trigger: 'change' }],
  creditCard: [
    { required: true, message: '请输入卡号', trigger: 'blur' },
    { pattern: /^\d{16}$/, message: '请输入16位卡号', trigger: 'blur' }
  ],
  expiryDate: [
    { required: true, message: '请输入有效期', trigger: 'blur' },
    { pattern: /^(0[1-9]|1[0-2])\/\d{4}$/, message: '格式为MM/YYYY', trigger: 'blur' }
  ]
}

// 运费
const shippingFee = computed(() => {
  return shippingMethod.value === 'FedEx' ? 20.00 : 10.00
})

// 是否可以提交
const canSubmit = computed(() => {
  return selectedAddress.value && 
         paymentForm.creditCard && 
         paymentForm.expiryDate &&
         cartStore.cartItems.length > 0
})

// 显示地址选择对话框
const showAddressDialog = async () => {
  await loadAddresses()
  addressDialogVisible.value = true
}

// 选择地址
const selectAddress = (address) => {
  selectedAddress.value = address
  addressDialogVisible.value = false
}

// 添加新地址
const addNewAddress = () => {
  addressDialogVisible.value = false
  router.push('/profile?tab=addresses')
}

// 加载地址列表
const loadAddresses = async () => {
  try {
    const res = await getAddressList()
    addresses.value = res.data || []
    
    // 如果没有选中地址，默认选择第一个
    if (!selectedAddress.value && addresses.value.length > 0) {
      const defaultAddr = addresses.value.find(addr => addr.isDefault)
      selectedAddress.value = defaultAddr || addresses.value[0]
    }
  } catch (error) {
    console.error('加载地址失败:', error)
  }
}

// 提交订单
const submitOrder = async () => {
  if (!paymentFormRef.value) return
  
  await paymentFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await ElMessageBox.confirm('确认提交订单吗？', '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        submitting.value = true
        
        // 构建订单数据
        const orderData = {
          billToFirstName: selectedAddress.value.firstName,
          billToLastName: selectedAddress.value.lastName,
          billAddress1: selectedAddress.value.address1,
          billAddress2: selectedAddress.value.address2,
          billCity: selectedAddress.value.city,
          billState: selectedAddress.value.state,
          billZip: selectedAddress.value.zip,
          billCountry: selectedAddress.value.country,
          shipToFirstName: selectedAddress.value.firstName,
          shipToLastName: selectedAddress.value.lastName,
          shipAddress1: selectedAddress.value.address1,
          shipAddress2: selectedAddress.value.address2,
          shipCity: selectedAddress.value.city,
          shipState: selectedAddress.value.state,
          shipZip: selectedAddress.value.zip,
          shipCountry: selectedAddress.value.country,
          creditCard: paymentForm.creditCard,
          expiryDate: paymentForm.expiryDate,
          cardType: paymentForm.cardType,
          courier: shippingMethod.value
        }
        
        const res = await createOrder(orderData)
        
        ElMessage.success('订单提交成功')
        
        // 清空购物车
        await cartStore.clearAll()
        
        // 跳转到订单详情页
        router.push(`/order/${res.data.orderId}`)
      } catch (error) {
        if (error !== 'cancel') {
          console.error('提交订单失败:', error)
          ElMessage.error('提交订单失败')
        }
      } finally {
        submitting.value = false
      }
    }
  })
}

onMounted(async () => {
  // 检查是否有购物车商品
  if (cartStore.cartItems.length === 0) {
    ElMessage.warning('购物车是空的')
    router.push('/cart')
    return
  }
  
  loading.value = true
  await loadAddresses()
  loading.value = false
})
</script>

<style scoped>
.checkout-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.breadcrumb {
  margin-bottom: 24px;
}

.page-title {
  font-size: 32px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 32px;
}

.loading-container {
  padding: 40px;
  background: white;
  border-radius: 12px;
}

.checkout-content {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  color: #2c3e50;
}

.address-section,
.shipping-section,
.payment-section,
.notes-section,
.summary-card {
  margin-bottom: 20px;
  border-radius: 12px;
}

.address-display {
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
}

.address-name {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 8px;
}

.address-phone {
  color: #7f8c8d;
  margin: 4px 0;
}

.address-detail {
  color: #495057;
  line-height: 1.6;
  margin: 4px 0;
}

.shipping-options {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.shipping-option {
  padding: 8px 0;
}

.option-name {
  font-weight: 500;
  color: #2c3e50;
}

.option-desc {
  font-size: 13px;
  color: #7f8c8d;
  margin-top: 4px;
}

.summary-items {
  max-height: 300px;
  overflow-y: auto;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.item-basic {
  flex: 1;
  display: flex;
  gap: 8px;
}

.item-name {
  font-size: 14px;
  color: #2c3e50;
}

.item-quantity {
  color: #7f8c8d;
}

.item-price {
  font-weight: 500;
  color: #e74c3c;
}

.summary-details {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin: 16px 0;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
}

.detail-row .label {
  color: #7f8c8d;
}

.detail-row .value {
  color: #2c3e50;
  font-weight: 500;
}

.detail-row.total {
  font-size: 18px;
  padding-top: 12px;
  border-top: 2px solid #e9ecef;
}

.detail-row.total .value.price {
  font-size: 24px;
  font-weight: 600;
  color: #e74c3c;
}

.submit-btn {
  width: 100%;
  height: 50px;
  font-size: 16px;
  margin-top: 16px;
}

.submit-hint {
  text-align: center;
  font-size: 12px;
  color: #7f8c8d;
  margin-top: 12px;
}

.empty-addresses {
  padding: 40px 0;
}

.address-list {
  max-height: 400px;
  overflow-y: auto;
}

.address-option {
  padding: 16px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.address-option:hover {
  border-color: #667eea;
  background: #f8f9ff;
}

.address-option.selected {
  border-color: #667eea;
  background: #f0f2ff;
}

.address-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.address-header .name {
  font-weight: 600;
  color: #2c3e50;
}

.address-header .phone {
  color: #7f8c8d;
  font-size: 14px;
}

@media (max-width: 768px) {
  .checkout-content {
    display: flex;
    flex-direction: column;
  }
}
</style>
