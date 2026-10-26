<script setup>
import { useFood } from "../stores/food";
const food = useFood();
</script>

<template>
  <h2>Menu</h2>
  <div class="grid">
    <div class="card" v-for="item in food.menu" :key="item.id">
      <h3>{{ item.name }}</h3>
      <p class="price">₹{{ item.price }}</p>
      <button @click="food.add(item)">Add to cart</button>
    </div>
  </div>
</template>
