<script setup>
import { useFood } from "./stores/food";
const food = useFood();
</script>

<template>
  <nav>
    <b>FoodieHub</b>
    <RouterLink to="/">Menu</RouterLink>
    <RouterLink to="/cart">Cart ({{ food.count }})</RouterLink>
    <RouterLink to="/orders">My Orders</RouterLink>
  </nav>
  <main>
    <RouterView />
  </main>
</template>

<style>
* {
  box-sizing: border-box;
}
body {
  margin: 0;
  font-family: sans-serif;
  background: #fff7ed;
  color: #333;
}

nav {
  display: flex;
  gap: 20px;
  align-items: center;
  padding: 14px 24px;
  background: #ea580c;
}
nav b {
  color: #fff;
  font-size: 20px;
  margin-right: auto;
}
nav a {
  color: #fff;
  text-decoration: none;
}
nav a.router-link-exact-active {
  font-weight: bold;
  text-decoration: underline;
}

main {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 16px;
}
h2 {
  color: #c2410c;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}
.card,
.row {
  background: #fff;
  border-radius: 10px;
  padding: 16px;
  box-shadow: 0 1px 4px #0002;
}
.card h3 {
  margin: 0 0 8px;
}
.row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  gap: 10px;
}

.price {
  color: #ea580c;
  font-weight: bold;
  margin: 0 0 12px;
}
.muted {
  color: #888;
}
.total {
  text-align: right;
  font-size: 20px;
  margin: 16px 0;
}
.badge {
  background: #dcfce7;
  color: #166534;
  padding: 2px 10px;
  border-radius: 12px;
  font-size: 12px;
}

button {
  background: #ea580c;
  color: #fff;
  border: 0;
  border-radius: 6px;
  padding: 8px 12px;
  cursor: pointer;
}
button:hover {
  background: #c2410c;
}
button.light {
  background: #eee;
  color: #333;
}
button.light:hover {
  background: #ddd;
}
button.danger {
  background: #dc2626;
}
button.danger:hover {
  background: #b91c1c;
}
</style>
