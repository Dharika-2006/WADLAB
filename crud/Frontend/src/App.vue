<template>
  <h1>ShopEase</h1>

  <button @click="page = 'shop'">Shop</button>
  <button @click="page = 'cart'">Cart ({{ cart.length }})</button>
  <button @click="page = 'orders'">Orders</button>
  <button @click="page = 'account'">Account</button>
  <p v-if="msg"><b>{{ msg }}</b></p>

  <!-- SHOP -->
  <div v-if="page === 'shop'">
    <h2>{{ editId ? 'Edit' : 'Add' }} product</h2>
    <form @submit.prevent="saveProduct">
      <input v-model="form.name" placeholder="Name" required>
      <input v-model.number="form.price" type="number" placeholder="Price" required>
      <input v-model.number="form.quantity" type="number" placeholder="Qty" required>
      <button>{{ editId ? 'Save' : 'Add' }}</button>
    </form>

    <h2>Products</h2>
    <div v-for="p in products" :key="p.id">
      {{ p.name }} | ₹{{ p.price }} | stock: {{ p.quantity }}
      <button @click="addToCart(p)">+ Cart</button>
      <button @click="edit(p)">Edit</button>
      <button @click="deleteProduct(p.id)">Delete</button>
    </div>
  </div>

  <!-- CART -->
  <div v-if="page === 'cart'">
    <h2>Cart</h2>
    <div v-for="c in cart" :key="c.id">
      {{ c.productName }} x {{ c.quantity }}
      <button @click="run(() => api('/cart/' + c.id, 'DELETE'))">Remove</button>
    </div>
    <button v-if="cart.length" @click="placeOrder">Place order</button>
  </div>

  <!-- ORDERS -->
  <div v-if="page === 'orders'">
    <h2>Orders</h2>
    <div v-for="o in orders" :key="o.id">
      {{ o.productName }} x {{ o.quantity }} | {{ o.status }}
      <button v-if="o.status !== 'Cancelled'" @click="run(() => api('/orders/' + o.id, 'DELETE'), 'Order cancelled')">
        Cancel
      </button>
    </div>
  </div>

  <!-- ACCOUNT -->
  <div v-if="page === 'account'">
    <h2>Account</h2>
    <input v-model="user.username" placeholder="Username">
    <input v-model="user.password" type="password" placeholder="Password">
    <button @click="run(() => api('/users/login', 'POST', user))">Login</button>
    <button @click="run(() => api('/users/register', 'POST', user), 'Registered')">Register</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const API = 'http://localhost:8080/api'

const page = ref('shop')
const msg = ref('')
const products = ref([])
const cart = ref([])
const orders = ref([])
const editId = ref(null)
const form = ref({ name: '', price: 0, quantity: 1 })
const user = ref({ username: '', password: '' })

// one helper for every backend call
async function api(path, method = 'GET', body) {
  const res = await fetch(API + path, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: body ? JSON.stringify(body) : undefined
  })
  const text = await res.text()
  let data = text
  try { data = JSON.parse(text) } catch (e) {}
  if (!res.ok) throw new Error(data.message || data.error || res.status)
  return data
}

async function load() {
  products.value = await api('/products')
  cart.value = await api('/cart')
  orders.value = await api('/orders')
}

// runs an action, then reloads everything and shows the result
async function run(action, okText) {
  try {
    const result = await action()
    msg.value = okText || (typeof result === 'string' ? result : '')
    await load()
  } catch (e) {
    msg.value = 'Error: ' + e.message
  }
}

function saveProduct() {
  const path = editId.value ? '/products/' + editId.value : '/products'
  run(async () => {
    await api(path, editId.value ? 'PUT' : 'POST', form.value)
    form.value = { name: '', price: 0, quantity: 1 }
    editId.value = null
  }, 'Saved')
}

function edit(p) {
  form.value = { name: p.name, price: p.price, quantity: p.quantity }
  editId.value = p.id
}

function deleteProduct(id) {
  run(() => api('/products/' + id, 'DELETE'), 'Deleted')
}

function addToCart(p) {
  run(() => api('/cart', 'POST', { productName: p.name, quantity: 1 }), 'Added to cart')
}

function placeOrder() {
  run(async () => {
    for (const c of cart.value) {
      await api('/orders', 'POST', { productName: c.productName, quantity: c.quantity })
    }
    await api('/cart', 'DELETE')
    page.value = 'orders'
  }, 'Order placed')
}

onMounted(() => run(() => {}))
</script>