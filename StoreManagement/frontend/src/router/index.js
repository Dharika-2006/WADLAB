import { createRouter, createWebHistory } from "vue-router";
import Menu from "../views/Menu.vue";
import Cart from "../views/Cart.vue";
import Orders from "../views/Orders.vue";

export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/", component: Menu },
    { path: "/cart", component: Cart },
    { path: "/orders", component: Orders },
  ],
});
