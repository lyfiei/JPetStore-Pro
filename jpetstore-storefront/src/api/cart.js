import request from './request'

/**
 * 获取购物车
 */
export function getCart() {
  return request({
    url: '/cart',
    method: 'get'
  })
}

/**
 * 添加商品到购物车
 */
export function addToCart(data) {
  return request({
    url: '/cart/items',
    method: 'post',
    data
  })
}

/**
 * 更新购物车商品数量
 */
export function updateCartItem(itemId, quantity) {
  return request({
    url: `/cart/items/${itemId}`,
    method: 'put',
    data: { quantity }
  })
}

/**
 * 删除购物车商品
 */
export function removeCartItem(itemId) {
  return request({
    url: `/cart/items/${itemId}`,
    method: 'delete'
  })
}

/**
 * 清空购物车
 */
export function clearCart() {
  return request({
    url: '/cart',
    method: 'delete'
  })
}
