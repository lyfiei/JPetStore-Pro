<template>
  <div class="profile-page">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>个人中心</el-breadcrumb-item>
    </el-breadcrumb>

    <h1 class="page-title">个人中心</h1>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- 个人信息 -->
    <div v-else class="profile-content">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form 
            :model="profileForm" 
            :rules="rules" 
            ref="formRef" 
            label-width="100px"
            class="profile-form"
          >
            <el-form-item label="用户名">
              <el-input v-model="profileForm.username" disabled />
            </el-form-item>
            
            <el-form-item label="姓氏" prop="firstName">
              <el-input v-model="profileForm.firstName" placeholder="请输入姓氏" />
            </el-form-item>
            
            <el-form-item label="名字" prop="lastName">
              <el-input v-model="profileForm.lastName" placeholder="请输入名字" />
            </el-form-item>
            
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
            </el-form-item>

            <el-divider content-position="left">地址信息</el-divider>
            
            <el-form-item label="地址1" prop="address1">
              <el-input v-model="profileForm.address1" placeholder="请输入街道地址" />
            </el-form-item>
            
            <el-form-item label="地址2" prop="address2">
              <el-input v-model="profileForm.address2" placeholder="公寓、套房等（选填）" />
            </el-form-item>
            
            <el-form-item label="城市" prop="city">
              <el-input v-model="profileForm.city" placeholder="请输入城市" />
            </el-form-item>
            
            <el-form-item label="省份" prop="state">
              <el-input v-model="profileForm.state" placeholder="请输入省份" />
            </el-form-item>
            
            <el-form-item label="邮编" prop="zip">
              <el-input v-model="profileForm.zip" placeholder="请输入邮编" />
            </el-form-item>
            
            <el-form-item label="国家" prop="country">
              <el-input v-model="profileForm.country" placeholder="请输入国家" />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="updateProfile" :loading="updating">
                保存修改
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 修改密码 -->
        <el-tab-pane label="修改密码" name="password">
          <el-form 
            :model="passwordForm" 
            :rules="passwordRules" 
            ref="passwordFormRef" 
            label-width="100px"
            class="profile-form"
          >
            <el-form-item label="原密码" prop="oldPassword">
              <el-input 
                v-model="passwordForm.oldPassword" 
                type="password" 
                placeholder="请输入原密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item label="新密码" prop="newPassword">
              <el-input 
                v-model="passwordForm.newPassword" 
                type="password" 
                placeholder="请输入新密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input 
                v-model="passwordForm.confirmPassword" 
                type="password" 
                placeholder="请再次输入新密码"
                show-password
              />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="changePassword" :loading="changingPassword">
                修改密码
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 我的订单 -->
        <el-tab-pane label="我的订单" name="orders">
          <div class="orders-section">
            <div v-if="orders.length === 0" class="empty-orders">
              <el-empty description="暂无订单" />
            </div>
            <div v-else class="order-list">
              <div 
                v-for="order in orders" 
                :key="order.orderId"
                class="order-item"
                @click="viewOrderDetail(order.orderId)"
              >
                <div class="order-header">
                  <span class="order-id">订单号: {{ order.orderId }}</span>
                  <span class="order-date">{{ formatDate(order.orderDate) }}</span>
                </div>
                <div class="order-body">
                  <div class="order-info">
                    <span class="order-status" :class="getStatusClass(order.status)">
                      {{ getStatusText(order.status) }}
                    </span>
                    <span class="order-total">¥{{ order.totalPrice }}</span>
                  </div>
                  <el-button type="primary" link>查看详情</el-button>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 收货地址 -->
        <el-tab-pane label="收货地址" name="addresses">
          <div class="addresses-section">
            <el-button type="primary" @click="showAddAddressDialog">
              添加新地址
            </el-button>
            
            <div v-if="addresses.length === 0" class="empty-addresses">
              <el-empty description="暂无收货地址" />
            </div>
            <div v-else class="address-list">
              <div 
                v-for="address in addresses" 
                :key="address.addressId"
                class="address-item"
                :class="{ 'is-default': address.isDefault }"
              >
                <div class="address-header">
                  <span class="address-name">
                    {{ address.firstName }} {{ address.lastName }}
                  </span>
                  <span class="address-phone">{{ address.phone }}</span>
                  <el-tag v-if="address.isDefault" type="success" size="small">默认</el-tag>
                </div>
                <div class="address-detail">
                  {{ address.address1 }} {{ address.address2 }}
                  {{ address.city }}, {{ address.state }} {{ address.zip }}
                  {{ address.country }}
                </div>
                <div class="address-actions">
                  <el-button 
                    v-if="!address.isDefault" 
                    type="primary" 
                    link
                    @click="setDefaultAddress(address.addressId)"
                  >
                    设为默认
                  </el-button>
                  <el-button type="primary" link @click="editAddress(address)">
                    编辑
                  </el-button>
                  <el-button type="danger" link @click="deleteAddressItem(address.addressId)">
                    删除
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 添加/编辑地址对话框 -->
    <el-dialog 
      v-model="addressDialogVisible" 
      :title="editingAddress ? '编辑地址' : '添加地址'"
      width="600px"
    >
      <el-form 
        :model="addressForm" 
        :rules="addressRules" 
        ref="addressFormRef" 
        label-width="80px"
      >
        <el-form-item label="姓氏" prop="firstName">
          <el-input v-model="addressForm.firstName" placeholder="请输入姓氏" />
        </el-form-item>
        
        <el-form-item label="名字" prop="lastName">
          <el-input v-model="addressForm.lastName" placeholder="请输入名字" />
        </el-form-item>
        
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="addressForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        
        <el-form-item label="地址1" prop="address1">
          <el-input v-model="addressForm.address1" placeholder="请输入街道地址" />
        </el-form-item>
        
        <el-form-item label="地址2" prop="address2">
          <el-input v-model="addressForm.address2" placeholder="公寓、套房等（选填）" />
        </el-form-item>
        
        <el-form-item label="城市" prop="city">
          <el-input v-model="addressForm.city" placeholder="请输入城市" />
        </el-form-item>
        
        <el-form-item label="省份" prop="state">
          <el-input v-model="addressForm.state" placeholder="请输入省份" />
        </el-form-item>
        
        <el-form-item label="邮编" prop="zip">
          <el-input v-model="addressForm.zip" placeholder="请输入邮编" />
        </el-form-item>
        
        <el-form-item label="国家" prop="country">
          <el-input v-model="addressForm.country" placeholder="请输入国家" />
        </el-form-item>
        
        <el-form-item label="默认地址">
          <el-switch v-model="addressForm.isDefault" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="addressDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAddress" :loading="savingAddress">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProfile, updateProfile as updateProfileApi, changePassword as updatePassword } from '../api/account'
import { getOrderList } from '../api/order'
import { getAddressList, addAddress, updateAddress, deleteAddress, setDefaultAddress as setDefault } from '../api/address'

const router = useRouter()

const loading = ref(false)
const updating = ref(false)
const changingPassword = ref(false)
const savingAddress = ref(false)
const activeTab = ref('basic')

// 个人信息表单
const profileForm = reactive({
  username: '',
  firstName: '',
  lastName: '',
  email: '',
  phone: '',
  address1: '',
  address2: '',
  city: '',
  state: '',
  zip: '',
  country: ''
})

const formRef = ref(null)

const rules = {
  firstName: [{ required: true, message: '请输入姓氏', trigger: 'blur' }],
  lastName: [{ required: true, message: '请输入名字', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }],
  address1: [{ required: true, message: '请输入街道地址', trigger: 'blur' }],
  city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
  state: [{ required: true, message: '请输入省份', trigger: 'blur' }],
  zip: [{ required: true, message: '请输入邮编', trigger: 'blur' }],
  country: [{ required: true, message: '请输入国家', trigger: 'blur' }]
}

// 密码表单
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordFormRef = ref(null)

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码至少6个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 订单列表
const orders = ref([])

// 地址列表
const addresses = ref([])

// 地址对话框
const addressDialogVisible = ref(false)
const editingAddress = ref(null)
const addressFormRef = ref(null)
const addressForm = reactive({
  firstName: '',
  lastName: '',
  phone: '',
  address1: '',
  address2: '',
  city: '',
  state: '',
  zip: '',
  country: '',
  isDefault: false
})

const addressRules = {
  firstName: [{ required: true, message: '请输入姓氏', trigger: 'blur' }],
  lastName: [{ required: true, message: '请输入名字', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  address1: [{ required: true, message: '请输入街道地址', trigger: 'blur' }],
  city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
  state: [{ required: true, message: '请输入省份', trigger: 'blur' }],
  zip: [{ required: true, message: '请输入邮编', trigger: 'blur' }],
  country: [{ required: true, message: '请输入国家', trigger: 'blur' }]
}

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

// 获取订单状态样式类
const getStatusClass = (status) => {
  const classMap = {
    'P': 'status-pending',
    'S': 'status-shipped',
    'C': 'status-completed',
    'X': 'status-cancelled'
  }
  return classMap[status] || ''
}

// 查看订单详情
const viewOrderDetail = (orderId) => {
  router.push(`/order/${orderId}`)
}

// 更新个人信息
const updateProfile = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      updating.value = true
      try {
        await updateProfileApi(profileForm)
        ElMessage.success('更新成功')
      } catch (error) {
        ElMessage.error('更新失败')
      } finally {
        updating.value = false
      }
    }
  })
}

// 修改密码
const changePassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      changingPassword.value = true
      try {
        await updatePassword(passwordForm)
        ElMessage.success('密码修改成功')
        passwordForm.oldPassword = ''
        passwordForm.newPassword = ''
        passwordForm.confirmPassword = ''
      } catch (error) {
        ElMessage.error('密码修改失败')
      } finally {
        changingPassword.value = false
      }
    }
  })
}

// 显示添加地址对话框
const showAddAddressDialog = () => {
  editingAddress.value = null
  Object.assign(addressForm, {
    firstName: '',
    lastName: '',
    phone: '',
    address1: '',
    address2: '',
    city: '',
    state: '',
    zip: '',
    country: '',
    isDefault: false
  })
  addressDialogVisible.value = true
}

// 编辑地址
const editAddress = (address) => {
  editingAddress.value = address
  Object.assign(addressForm, address)
  addressDialogVisible.value = true
}

// 保存地址
const saveAddress = async () => {
  if (!addressFormRef.value) return
  
  await addressFormRef.value.validate(async (valid) => {
    if (valid) {
      savingAddress.value = true
      try {
        if (editingAddress.value) {
          await updateAddress(editingAddress.value.addressId, addressForm)
          ElMessage.success('地址更新成功')
        } else {
          await addAddress(addressForm)
          ElMessage.success('地址添加成功')
        }
        addressDialogVisible.value = false
        await loadAddresses()
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        savingAddress.value = false
      }
    }
  })
}

// 删除地址
const deleteAddressItem = async (addressId) => {
  try {
    await ElMessageBox.confirm('确定要删除这个地址吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteAddress(addressId)
    ElMessage.success('删除成功')
    await loadAddresses()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 设置默认地址
const setDefaultAddress = async (addressId) => {
  try {
    await setDefault(addressId)
    ElMessage.success('设置成功')
    await loadAddresses()
  } catch (error) {
    ElMessage.error('设置失败')
  }
}

// 加载个人信息
const loadProfile = async () => {
  loading.value = true
  try {
    const res = await getProfile()
    Object.assign(profileForm, res.data)
  } catch (error) {
    console.error('加载个人信息失败:', error)
  } finally {
    loading.value = false
  }
}

// 加载订单列表
const loadOrders = async () => {
  try {
    const res = await getOrderList({ page: 1, pageSize: 10 })
    orders.value = res.data.list || []
  } catch (error) {
    console.error('加载订单列表失败:', error)
  }
}

// 加载地址列表
const loadAddresses = async () => {
  try {
    const res = await getAddressList()
    addresses.value = res.data || []
  } catch (error) {
    console.error('加载地址列表失败:', error)
  }
}

onMounted(() => {
  loadProfile()
  loadOrders()
  loadAddresses()
})
</script>

<style scoped>
.profile-page {
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

.profile-content {
  background: white;
  border-radius: 12px;
  overflow: hidden;
}

.profile-form {
  max-width: 600px;
  padding: 20px;
}

.orders-section,
.addresses-section {
  padding: 20px;
}

.empty-orders,
.empty-addresses {
  padding: 40px 0;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-item {
  padding: 16px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.order-item:hover {
  border-color: #667eea;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.1);
}

.order-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 14px;
}

.order-id {
  font-weight: 500;
  color: #2c3e50;
}

.order-date {
  color: #7f8c8d;
}

.order-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-info {
  display: flex;
  gap: 20px;
  align-items: center;
}

.order-status {
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 13px;
  font-weight: 500;
}

.status-pending {
  background: #fff3cd;
  color: #856404;
}

.status-shipped {
  background: #cce5ff;
  color: #004085;
}

.status-completed {
  background: #d4edda;
  color: #155724;
}

.status-cancelled {
  background: #f8d7da;
  color: #721c24;
}

.order-total {
  font-size: 18px;
  font-weight: 600;
  color: #e74c3c;
}

.address-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
  margin-top: 20px;
}

.address-item {
  padding: 16px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  transition: all 0.2s;
}

.address-item.is-default {
  border-color: #27ae60;
  background: #f0fff4;
}

.address-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.address-name {
  font-weight: 600;
  color: #2c3e50;
}

.address-phone {
  color: #7f8c8d;
  font-size: 14px;
}

.address-detail {
  font-size: 14px;
  color: #495057;
  line-height: 1.6;
  margin-bottom: 12px;
}

.address-actions {
  display: flex;
  gap: 8px;
}

@media (max-width: 768px) {
  .address-list {
    grid-template-columns: 1fr;
  }
}
</style>
