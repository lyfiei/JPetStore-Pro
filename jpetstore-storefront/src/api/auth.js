import request from './request'

/**
 * 用户登录
 */
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

/**
 * 邮箱验证码登录
 */
export function loginByEmail(data) {
  return request({
    url: '/auth/login/email',
    method: 'post',
    data
  })
}

/**
 * 发送邮箱验证码
 */
export function sendCode(email) {
  return request({
    url: '/auth/send-code',
    method: 'post',
    data: { email }
  })
}

/**
 * 用户登出
 */
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

/**
 * 用户注册
 */
export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

/**
 * 验证用户名/邮箱可用性
 */
export function validateUsername(username) {
  return request({
    url: '/auth/validate',
    method: 'get',
    params: { username }
  })
}

export function validateEmail(email) {
  return request({
    url: '/auth/validate',
    method: 'get',
    params: { email }
  })
}
