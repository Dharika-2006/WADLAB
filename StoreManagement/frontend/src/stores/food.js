import { defineStore } from "pinia";
import { ref, computed } from "vue";

export const useFood = defineStore("food", () => {
  // ---- state ----
  const menu = ref([
    { id: 1, name: "Margherita Pizza", price: 250 },
    { id: 2, name: "Veg Burger", price: 120 },
    { id: 3, name: "Chicken Biryani", price: 220 },
    { id: 4, name: "Pasta", price: 180 },
    { id: 5, name: "French Fries", price: 90 },
  ]);
  const cart = ref([]);
  const orders = ref([]);

  // ---- getters (computed) ----
  const count = computed(() => cart.value.reduce((s, i) => s + i.qty, 0));
  const total = computed(() =>
    cart.value.reduce((s, i) => s + i.price * i.qty, 0),
  );

  // ---- actions ----
  function add(item) {
    const found = cart.value.find((c) => c.id === item.id);
    if (found) found.qty++;
    else cart.value.push({ ...item, qty: 1 });
  }
  function change(id, d) {
    // d = +1 or -1
    const item = cart.value.find((c) => c.id === id);
    item.qty += d;
    if (item.qty <= 0) remove(id);
  }
  function remove(id) {
    cart.value = cart.value.filter((c) => c.id !== id);
  }
  function placeOrder() {
    if (!cart.value.length) return;
    orders.value.push({
      id: orders.value.length + 1,
      items: cart.value,
      total: total.value,
      status: "Placed",
    });
    cart.value = [];
  }

  return { menu, cart, orders, count, total, add, change, remove, placeOrder };
});
