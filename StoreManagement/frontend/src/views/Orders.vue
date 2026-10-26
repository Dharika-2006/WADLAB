<script setup>
import { useFood } from "../stores/food";
const food = useFood();
</script>

<template>
  <h2>My Orders</h2>
  <p class="muted" v-if="food.orders.length === 0">No orders yet.</p>

  <div
    class="card"
    style="margin-bottom: 12px"
    v-for="o in food.orders"
    :key="o.id"
  >
    <b>Order #{{ o.id }}</b>
    <span class="badge">{{ o.status }}</span>
    <ul>
      <li v-for="i in o.items" :key="i.id">{{ i.name }} x {{ i.qty }}</li>
    </ul>
    <p class="price">Total: ₹{{ o.total }}</p>
  </div>
</template>
