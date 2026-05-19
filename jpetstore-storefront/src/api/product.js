import request from './request'

/**
 * 获取所有分类
 */
export function getCategories() {
  return request({
    url: '/categories',
    method: 'get'
  })
}

/**
 * 获取分类详情及商品列表
 */
export function getCategoryWithProducts(categoryId) {
  return request({
    url: `/categories/${categoryId}`,
    method: 'get'
  })
}

/**
 * 获取商品详情
 */
export function getProductDetail(productId) {
  return request({
    url: `/products/${productId}`,
    method: 'get'
  })
}

/**
 * 获取规格项详情
 */
export function getItemDetail(itemId) {
  return request({
    url: `/items/${itemId}`,
    method: 'get'
  })
}

/**
 * 搜索商品
 */
export function searchProducts(keyword) {
  return request({
    url: '/products/search',
    method: 'get',
    params: { keyword }
  })
}

/**
 * 商品自动补全
 */
export function autoComplete(keyword) {
  return request({
    url: '/products/autocomplete',
    method: 'get',
    params: { keyword }
  })
}

/**
 * 获取分类下商品及规格项
 */
export function getCategoryProductsWithItems(categoryId) {
  return request({
    url: `/categories/${categoryId}/products-with-items`,
    method: 'get'
  })
}
