<script setup>
import { useRouter } from "vue-router";
import { useFood } from "../stores/food";

const food = useFood();
const router = useRouter();

function order() {
  food.placeOrder();
  router.push("/orders");
}
</script>

<template>
  <h2>Cart</h2>
  <p class="muted" v-if="food.cart.length === 0">Your cart is empty.</p>

  <div class="row" v-for="c in food.cart" :key="c.id">
    <span>{{ c.name }} - ₹{{ c.price }} x {{ c.qty }}</span>
    <span>
      <button class="light" @click="food.change(c.id, -1)">-</button>
      <button class="light" @click="food.change(c.id, 1)">+</button>
      <button class="danger" @click="food.remove(c.id)">Remove</button>
    </span>
  </div>

  <div v-if="food.cart.length">
    <p class="total">
      Total: <b>₹{{ food.total }}</b>
    </p>
    <button @click="order">Place order</button>
  </div>
</template>
