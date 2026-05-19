import request from './request'

/**
 * 获取地址列表
 */
export function getAddressList() {
  return request({
    url: '/addresses',
    method: 'get'
  })
}

/**
 * 添加地址
 */
export function addAddress(data) {
  return request({
    url: '/addresses',
    method: 'post',
    data
  })
}

/**
 * 更新地址
 */
export function updateAddress(addressId, data) {
  return request({
    url: `/addresses/${addressId}`,
    method: 'put',
    data
  })
}

/**
 * 删除地址
 */
export function deleteAddress(addressId) {
  return request({
    url: `/addresses/${addressId}`,
    method: 'delete'
  })
}

/**
 * 设置默认地址
 */
export function setDefaultAddress(addressId) {
  return request({
    url: `/addresses/${addressId}/default`,
    method: 'put'
  })
}
