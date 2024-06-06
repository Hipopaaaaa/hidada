<template>
  <div id="userLayout">
    <a-layout style="height: 100vh">
      <a-layout-header class="header">
        <a-space>
          <img class="logo" src="../assets/logo.png" />
          <div>hi答答 AI 智能答题平台</div>
        </a-space>
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
</script>

<style scoped>
#userLayout {
  text-align: center;
  background: url("https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png")
    0% 0% / 100% 100%;
}

.logo {
  width: 48px;
  height: 48px;
}

.header {
  margin-top: 16px;
  font-size: 21px;
}

.content {
  margin-bottom: 28px;
  padding: 20px;
}

.footer {
  padding: 16px;
  text-align: center;
  background: #efefef;
}
</style>
