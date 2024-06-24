<template>
  <a-modal v-model:visible="visible" :footer="false">
    <template #title>
      {{ title }}
    </template>
    <h4 style="margin-top: 0">复制分享链接</h4>
    <a-typography-paragraph copyable>{{ link }}</a-typography-paragraph>
    <h4>手机扫码查看</h4>
    <img :src="codeImg" />
  </a-modal>
</template>

<script setup lang="ts">
import { withDefaults, defineProps, ref } from "vue";
// @ts-ignore
import QRCode from "qrcode";
import { Message } from "@arco-design/web-vue";
import { defineExpose } from "vue";

interface Props {
  // 分享链接
  link: string;
  // 弹窗标题
  title: string;
}

const props = withDefaults(defineProps<Props>(), {
  link: () => "https://www.baidu.com",
  title: () => "分享",
});
// 要展示的图片
const codeImg = ref();
QRCode.toDataURL(props.link)
  .then((url: any) => {
    codeImg.value = url;
  })
  .catch((err: any) => {
    Message.error("生成二维码失败，" + err.message);
  });
const visible = ref(false);
// 打开弹窗
const openModel = () => {
  visible.value = true;
};
// 关闭弹窗
const closeModel = () => {
  visible.value = false;
};
// 暴露方法给父组件
defineExpose({ openModel, closeModel });
</script>

<style scoped></style>
