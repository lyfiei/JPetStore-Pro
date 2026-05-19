import request from './request'

/**
 * 获取个人信息
 */
export function getProfile() {
  return request({
    url: '/account/profile',
    method: 'get'
  })
}

/**
 * 更新个人信息
 */
export function updateProfile(data) {
  return request({
    url: '/account/profile',
    method: 'put',
    data
  })
}

/**
 * 修改密码
 */
export function changePassword(data) {
  return request({
    url: '/account/password',
    method: 'put',
    data
  })
}
