import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCart, addToCart, updateCartItem, removeCartItem, clearCart } from '../api/cart'

export const useCartStore = defineStore('cart', () => {
  // State
  const cartItems = ref([])
  const loading = ref(false)
  
  // Getters
  const cartCount = computed(() => {
    return cartItems.value.reduce((sum, item) => sum + item.quantity, 0)
  })
  
  const cartTotal = computed(() => {
    return cartItems.value.reduce((sum, item) => sum + (item.listPrice * item.quantity), 0)
  })
  
  // Actions
  async function fetchCart() {
    loading.value = true
    try {
      const res = await getCart()
      cartItems.value = res.data.cartItems || []
    } finally {
      loading.value = false
    }
  }
  
  async function addItemToCart(data) {
    const res = await addToCart(data)
    await fetchCart() // 刷新购物车
    return res
  }
  
  async function updateItem(itemId, quantity) {
    const res = await updateCartItem(itemId, quantity)
    await fetchCart()
    return res
  }
  
  async function removeItem(itemId) {
    const res = await removeCartItem(itemId)
    await fetchCart()
    return res
  }
  
  async function clearAll() {
    const res = await clearCart()
    cartItems.value = []
    return res
  }
  
  return {
    cartItems,
    loading,
    cartCount,
    cartTotal,
    fetchCart,
    addItemToCart,
    updateItem,
    removeItem,
    clearAll
  }
})
