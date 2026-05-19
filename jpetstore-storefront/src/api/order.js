import request from './request'

/**
 * 创建订单
 */
export function createOrder(data) {
  return request({
    url: '/orders',
    method: 'post',
    data
  })
}

/**
 * 获取订单详情
 */
export function getOrderDetail(orderId) {
  return request({
    url: `/orders/${orderId}`,
    method: 'get'
  })
}

/**
 * 获取用户订单列表
 */
export function getOrderList(params) {
  return request({
    url: '/orders',
    method: 'get',
    params
  })
}
