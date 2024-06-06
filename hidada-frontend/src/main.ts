import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { createPinia } from "pinia";
import ArcoVue from "@arco-design/web-vue";
import "@arco-design/web-vue/dist/arco.css";
import "@/access";

const app = createApp(App);

createApp(App).use(router).use(ArcoVue).use(createPinia()).mount("#app");
