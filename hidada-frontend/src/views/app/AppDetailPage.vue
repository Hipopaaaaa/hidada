<template>
  <div id="appDetailPage">
    <a-card>
      <a-row style="margin-bottom: 16px">
        <a-col class="content-wrapper" flex="auto">
          <h2>{{ data.appName }}</h2>
          <p>{{ data.appDesc }}</p>
          <p>应用类型：{{ APP_TYPE_MAP[data?.appType] }}</p>
          <p>评分策略：{{ APP_SCORING_STRATEGY_MAP[data?.scoringStrategy] }}</p>
          <p>
            <a-space>
              作者：
              <div :style="{ display: 'flex', alignItems: 'center' }">
                <a-avatar
                  :size="24"
                  :style="{ marginRight: '8px' }"
                  :image-url="data.user?.userAvatar"
                ></a-avatar>
                <a-typography-text
                  >{{ data.user?.userName ?? "无名" }}
                </a-typography-text>
              </div>
            </a-space>
          </p>
          <p>
            创建时间：{{ dayjs(data.createTime).format("YYYY-MM-DD HH:mm:ss") }}
          </p>
          <a-space size="medium">
            <a-button type="primary" :href="`/answer/do/${id}`"
              >开始答题</a-button
            >
            <a-button @click="doShare">分享应用</a-button>
            <a-button v-if="isMy" :href="`/add/question/${id}`"
              >设置题目
            </a-button>
            <a-button v-if="isMy" :href="`/add/scoring_result/${id}`"
              >设置评分
            </a-button>
            <a-button v-if="isMy" :href="`/add/app/${id}`">修改应用</a-button>
          </a-space>
        </a-col>
        <a-col flex="320px">
          <a-image width="100%" :src="data.appIcon" />
        </a-col>
      </a-row>
    </a-card>
    <ShareModal :link="shareLink" title="应用分享" ref="shareModelRef" />
  </div>
</template>

<script lang="ts" setup>
import { computed, ref, watchEffect } from "vue";
import API from "@/api";
import { Message } from "@arco-design/web-vue";
import { getAppVoByIdUsingGet } from "@/api/appController";
import { withDefaults, defineProps } from "vue";
import dayjs from "dayjs";
import { useLoginUserStore } from "@/store/useStore";
import { APP_SCORING_STRATEGY_MAP, APP_TYPE_MAP } from "../../constant/app";
import ShareModal from "@/components/ShareModal.vue";

interface Props {
  id: number;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => {
    return "";
  },
});

const loginUserStore = useLoginUserStore();
let loginUserId = loginUserStore.loginUser?.id;

// 是否为本人创建
const isMy = computed(() => {
  return loginUserId && loginUserId === data.value.userId;
});

const data = ref<API.AppVO>({});
/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getAppVoByIdUsingGet({
    id: props.id,
  });
  if (res.data.code === 0 && res.data.data) {
    data.value = res.data.data;
  } else {
    Message.error("获取数据失败，" + res.data.message);
  }
};
// 分享链接
const shareLink = `${window.location.protocol}//${window.location.host}/app/detail/${props.id}`;
// 分享弹窗的引用
const shareModelRef = ref();
// 分享
const doShare = (e: Event) => {
  if (shareModelRef.value) {
    shareModelRef.value.openModel();
  }
  // 阻止冒泡，防止跳转到详情页
  e.stopPropagation();
};

watchEffect(() => {
  loadData();
});
</script>
<style scoped>
#appDetailPage {
}

#appDetailPage.content-wrapper > * {
  margin-bottom: 24px;
}
</style>
