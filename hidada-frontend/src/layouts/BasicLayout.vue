<template>
  <div id="basicLayout" :style="backgroundStyle">
    <a-layout style="height: 100vh">
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">
        <a href="https://github.com/Hipopaaaaa" target="_blank"
          >github仓库：Hipop</a
        >
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from "@/components/GlobalHeader.vue";
import { onBeforeMount, onMounted, ref } from "vue";

const scrollPosition = ref<number>(0); // 滚动位置
const backgroundStyle = ref({
  // 初始背景样式
  background: "linear-gradient(to right, #ebf6f6, #fff)",
});

const handleScroll = () => {
  scrollPosition.value =
    window.pageYOffset || document.documentElement.scrollTop;
  // 根据滚动位置动态改变背景样式
  // 这里只是一个示例，你可能需要根据你的具体需求来计算背景样式
  // 例如，如果滚动超过某个阈值，就改变背景颜色或渐变方向
  if (scrollPosition.value > 100) {
    backgroundStyle.value.background =
      "linear-gradient(to right, #ff0000, #ffff00)"; // 示例：变为红色到黄色的渐变
  } else {
    backgroundStyle.value.background =
      "linear-gradient(to right, #ebf6f6, #fff)"; // 恢复初始渐变
  }
};
onMounted;
() => {
  {
    window.addEventListener("scroll", handleScroll);
  }
};
onBeforeMount(() => {
  // 组件卸载前移除事件监听器
  window.removeEventListener("scroll", handleScroll);
});
</script>

<style scoped>
#basicLayout {
  overflow-y: auto;
}

.header {
  margin-bottom: 16px;
  box-shadow: #eee 1px 1px 5px;
}

.content {
  box-sizing: border-box;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto 28px;
  padding: 20px;
}

.footer {
  padding: 16px;
  text-align: center;
  background: #efefef;
}
</style>
