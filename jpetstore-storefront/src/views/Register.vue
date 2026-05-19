<template>
  <div class="register-page">
    <el-card class="register-card">
      <template #header>
        <h2>用户注册</h2>
      </template>
      
      <el-form 
        :model="registerForm" 
        :rules="rules" 
        ref="formRef" 
        label-width="100px"
      >
        <!-- 账号信息 -->
        <el-divider content-position="left">账号信息</el-divider>
        
        <el-form-item label="用户名" prop="username">
          <el-input 
            v-model="registerForm.username" 
            placeholder="请输入用户名"
            @blur="validateUsername"
          />
          <div v-if="usernameStatus" class="validation-hint" :class="usernameStatus.type">
            {{ usernameStatus.message }}
          </div>
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="registerForm.password" 
            type="password" 
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            v-model="registerForm.confirmPassword" 
            type="password" 
            placeholder="请再次输入密码"
            show-password
          />
        </el-form-item>

        <!-- 个人信息 -->
        <el-divider content-position="left">个人信息</el-divider>
        
        <el-form-item label="姓氏" prop="firstName">
          <el-input v-model="registerForm.firstName" placeholder="请输入姓氏" />
        </el-form-item>
        
        <el-form-item label="名字" prop="lastName">
          <el-input v-model="registerForm.lastName" placeholder="请输入名字" />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input 
            v-model="registerForm.email" 
            placeholder="请输入邮箱"
            @blur="validateEmail"
          />
          <div v-if="emailStatus" class="validation-hint" :class="emailStatus.type">
            {{ emailStatus.message }}
          </div>
        </el-form-item>
        
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入手机号" />
        </el-form-item>

        <!-- 地址信息 -->
        <el-divider content-position="left">地址信息</el-divider>
        
        <el-form-item label="地址1" prop="address1">
          <el-input v-model="registerForm.address1" placeholder="请输入街道地址" />
        </el-form-item>
        
        <el-form-item label="地址2" prop="address2">
          <el-input v-model="registerForm.address2" placeholder="公寓、套房等（选填）" />
        </el-form-item>
        
        <el-form-item label="城市" prop="city">
          <el-input v-model="registerForm.city" placeholder="请输入城市" />
        </el-form-item>
        
        <el-form-item label="省份" prop="state">
          <el-input v-model="registerForm.state" placeholder="请输入省份" />
        </el-form-item>
        
        <el-form-item label="邮编" prop="zip">
          <el-input v-model="registerForm.zip" placeholder="请输入邮编" />
        </el-form-item>
        
        <el-form-item label="国家" prop="country">
          <el-input v-model="registerForm.country" placeholder="请输入国家" />
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleRegister" 
            :loading="loading" 
            style="width: 100%"
          >
            注册
          </el-button>
        </el-form-item>
        
        <el-form-item>
          <div class="form-footer">
            <router-link to="/login">已有账号？立即登录</router-link>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register, validateUsername as checkUsername, validateEmail as checkEmail } from '../api/auth'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const usernameStatus = ref(null)
const emailStatus = ref(null)

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
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

// 自定义验证器：确认密码
const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  firstName: [
    { required: true, message: '请输入姓氏', trigger: 'blur' }
  ],
  lastName: [
    { required: true, message: '请输入名字', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }
  ],
  address1: [
    { required: true, message: '请输入街道地址', trigger: 'blur' }
  ],
  city: [
    { required: true, message: '请输入城市', trigger: 'blur' }
  ],
  state: [
    { required: true, message: '请输入省份', trigger: 'blur' }
  ],
  zip: [
    { required: true, message: '请输入邮编', trigger: 'blur' }
  ],
  country: [
    { required: true, message: '请输入国家', trigger: 'blur' }
  ]
}

// 验证用户名
const validateUsername = async () => {
  if (!registerForm.username) return
  
  try {
    const res = await checkUsername(registerForm.username)
    if (res.data.available) {
      usernameStatus.value = { type: 'success', message: '用户名可用' }
    } else {
      usernameStatus.value = { type: 'error', message: '用户名已被使用' }
    }
  } catch (error) {
    console.error('验证用户名失败:', error)
  }
}

// 验证邮箱
const validateEmail = async () => {
  if (!registerForm.email) return
  
  try {
    const res = await checkEmail(registerForm.email)
    if (res.data.available) {
      emailStatus.value = { type: 'success', message: '邮箱可用' }
    } else {
      emailStatus.value = { type: 'error', message: '邮箱已被注册' }
    }
  } catch (error) {
    console.error('验证邮箱失败:', error)
  }
}

// 处理注册
const handleRegister = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      // 检查用户名和邮箱是否可用
      if (usernameStatus.value?.type === 'error') {
        ElMessage.error('用户名已被使用')
        return
      }
      if (emailStatus.value?.type === 'error') {
        ElMessage.error('邮箱已被注册')
        return
      }

      loading.value = true
      try {
        await register(registerForm)
        ElMessage.success('注册成功')
        router.push('/login')
      } catch (error) {
        ElMessage.error('注册失败')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 200px);
  padding: 40px 20px;
}

.register-card {
  width: 100%;
  max-width: 700px;
}

.register-card h2 {
  text-align: center;
  margin: 0;
  color: #2c3e50;
}

.validation-hint {
  font-size: 12px;
  margin-top: 4px;
}

.validation-hint.success {
  color: #27ae60;
}

.validation-hint.error {
  color: #e74c3c;
}

.form-footer {
  text-align: center;
  width: 100%;
}

.form-footer a {
  color: #409eff;
  text-decoration: none;
}

.form-footer a:hover {
  text-decoration: underline;
}

:deep(.el-divider__text) {
  font-weight: 600;
  color: #2c3e50;
}
</style>
