<template>
  <div id="answerResultPage">
    <a-card>
      <a-row style="margin-bottom: 16px">
        <a-col class="content-wrapper" flex="auto">
          <h2>{{ data.resultName }}</h2>
          <p>结果描述：{{ data.resultDesc }}</p>
          <p>结果 id：{{ data.resultId }}</p>
          <p>应用 id：{{ data.appId }}</p>
          <p v-if="APP_TYPE_ENUM.SCORE === data.appType">
            结果得分：{{ data.resultScore }}
          </p>
          <p>我的答案: {{ data.choices }}</p>
          <p>应用类型：{{ APP_TYPE_MAP[data?.appType] }}</p>
          <p>评分策略：{{ APP_SCORING_STRATEGY_MAP[data?.scoringStrategy] }}</p>
          <p>
            <a-space>
              答题人：
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
            答题时间：{{ dayjs(data.createTime).format("YYYY-MM-DD HH:mm:ss") }}
          </p>
          <a-space size="medium">
            <a-button type="primary" :href="`/answer/do/${data.appId}`"
              >去答题</a-button
            >
            <a-button>分享结果</a-button>
          </a-space>
        </a-col>
        <a-col flex="320px">
          <a-image width="100%" :src="data.resultPicture" />
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, watchEffect } from "vue";
import API from "@/api";
import { Message } from "@arco-design/web-vue";
import { getUserAnswerVoByIdUsingGet } from "@/api/userAnswerController";
import { withDefaults, defineProps } from "vue";
import dayjs from "dayjs";
import { useLoginUserStore } from "@/store/useStore";
import {
  APP_SCORING_STRATEGY_MAP,
  APP_TYPE_ENUM,
  APP_TYPE_MAP,
} from "../../constant/app";

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

const data = ref<API.UserAnswerVO>({});
/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getUserAnswerVoByIdUsingGet({
    id: props.id,
  });
  if (res.data.code === 0 && res.data.data) {
    data.value = res.data.data;
  } else {
    Message.error("获取数据失败，" + res.data.message);
  }
};

watchEffect(() => {
  loadData();
});
</script>
<style scoped>
#answerResultPage {
}

#answerResultPage.content-wrapper > * {
  margin-bottom: 24px;
}
</style>
