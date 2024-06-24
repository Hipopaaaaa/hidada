<template>
  <div id="addAppPage">
    <h2 style="margin-bottom: 16px">设置题目</h2>
    <a-form
      :model="questionContent"
      label-align="left"
      auto-label-width
      @submit="handleSubmit"
    >
      <a-form-item label="应用 id">
        {{ appId }}
      </a-form-item>
      <a-form-item label="题目列表" :content-flex="false" :merge-props="false">
        <a-space size="medium" style="margin-bottom: 16px">
          <a-button @click="addQuestion(questionContent.length)"
            >底部添加题目
          </a-button>
          <AiGenerateQuestionDrawer
            :appId="appId"
            :onSuccess="onAiGenerateSuccess"
            :onSSESuccess="onAiGenerateSuccessSSE"
            :onSSEStart="onSSEStart"
            :onSSEClose="onSSEClose"
          />
        </a-space>
        <!--遍历每道一题目-->
        <a-list
          :grid-props="{ gutter: [20, 20], sm: 24, md: 24, lg: 24, xl: 12 }"
          :bordered="false"
          :data="questionContent"
        >
          <template #item="{ item, index }">
            <a-card>
              <a-space size="large">
                <h3>题目 {{ index + 1 }}</h3>
                <a-button size="small" @click="addQuestion(index + 1)"
                  >添加题目
                </a-button>
                <a-button
                  size="small"
                  status="danger"
                  @click="deleteQuestion(index)"
                  >删除题目
                </a-button>
              </a-space>
              <a-form-item
                field="posts.post1"
                :label="`题目 ${index + 1} 标题`"
              >
                <a-input v-model="item.title" placeholder="请输入标题..." />
              </a-form-item>
              <!--题目选项-->
              <a-space size="large">
                <h4>题目 {{ index + 1 }} 选项列表</h4>
                <a-button
                  size="small"
                  @click="
                    addQuestionOption(
                      item,
                      item.options ? item.options.length : 0
                    )
                  "
                  >底部添加选项
                </a-button>
              </a-space>
              <a-form-item
                v-for="(option, optionIndex) in item.options"
                :key="optionIndex"
                :label="`选项${optionIndex + 1}`"
                :content-flex="false"
                :merge-props="false"
              >
                <a-form-item label="选项 key">
                  <a-input
                    v-model="option.key"
                    placeholder="请输入选项key..."
                  />
                </a-form-item>
                <a-form-item label="选项 值">
                  <a-input
                    v-model="option.value"
                    placeholder="请输入选项值..."
                  />
                </a-form-item>
                <a-form-item
                  label="选项 结果"
                  v-if="app.appType === APP_TYPE_ENUM.TEST"
                >
                  <a-input
                    v-model="option.result"
                    placeholder="请输入选项结果..."
                  />
                </a-form-item>
                <a-form-item
                  label="选项 得分"
                  v-if="app.appType === APP_TYPE_ENUM.SCORE"
                >
                  <a-input
                    v-model="option.score"
                    placeholder="请输入选项得分..."
                  />
                </a-form-item>
                <a-space size="large">
                  <a-button
                    size="mini"
                    @click="addQuestionOption(item, optionIndex + 1)"
                    >添加选项
                  </a-button>
                  <a-button
                    size="mini"
                    status="danger"
                    @click="deleteQuestionOption(item, optionIndex)"
                    >删除选项
                  </a-button>
                </a-space>
              </a-form-item>
              <!--题目选项结尾-->
            </a-card>
          </template>
        </a-list>
        <!--        <a-card v-for="(question, index) in questionContent" :key="index">-->
        <!--          <h3>题目 {{ index + 1 }}</h3>-->
        <!--          <a-space size="large">-->
        <!--            <a-button size="small" @click="addQuestion(index + 1)"-->
        <!--              >添加题目-->
        <!--            </a-button>-->
        <!--            <a-button-->
        <!--              size="small"-->
        <!--              status="danger"-->
        <!--              @click="deleteQuestion(index)"-->
        <!--              >删除题目-->
        <!--            </a-button>-->
        <!--          </a-space>-->
        <!--          <a-form-item field="posts.post1" :label="`题目 ${index + 1} 标题`">-->
        <!--            <a-input v-model="question.title" placeholder="请输入标题..." />-->
        <!--          </a-form-item>-->
        <!--          &lt;!&ndash;题目选项&ndash;&gt;-->
        <!--          <a-space size="large">-->
        <!--            <h4>题目 {{ index + 1 }} 选项列表</h4>-->
        <!--            <a-button-->
        <!--              size="small"-->
        <!--              @click="-->
        <!--                addQuestionOption(-->
        <!--                  question,-->
        <!--                  question.options ? question.options.length : 0-->
        <!--                )-->
        <!--              "-->
        <!--              >底部添加选项-->
        <!--            </a-button>-->
        <!--          </a-space>-->
        <!--          <a-form-item-->
        <!--            v-for="(option, optionIndex) in question.options"-->
        <!--            :key="optionIndex"-->
        <!--            :label="`选项${optionIndex + 1}`"-->
        <!--            :content-flex="false"-->
        <!--            :merge-props="false"-->
        <!--          >-->
        <!--            <a-form-item label="选项 key">-->
        <!--              <a-input v-model="option.key" placeholder="请输入选项key..." />-->
        <!--            </a-form-item>-->
        <!--            <a-form-item label="选项 值">-->
        <!--              <a-input v-model="option.value" placeholder="请输入选项值..." />-->
        <!--            </a-form-item>-->
        <!--            <a-form-item-->
        <!--              label="选项 结果"-->
        <!--              v-if="app.appType === APP_TYPE_ENUM.TEST"-->
        <!--            >-->
        <!--              <a-input-->
        <!--                v-model="option.result"-->
        <!--                placeholder="请输入选项结果..."-->
        <!--              />-->
        <!--            </a-form-item>-->
        <!--            <a-form-item-->
        <!--              label="选项 得分"-->
        <!--              v-if="app.appType === APP_TYPE_ENUM.SCORE"-->
        <!--            >-->
        <!--              <a-input v-model="option.score" placeholder="请输入选项得分..." />-->
        <!--            </a-form-item>-->
        <!--            <a-space size="large">-->
        <!--              <a-button-->
        <!--                size="mini"-->
        <!--                @click="addQuestionOption(question, optionIndex + 1)"-->
        <!--                >添加选项-->
        <!--              </a-button>-->
        <!--              <a-button-->
        <!--                size="mini"-->
        <!--                status="danger"-->
        <!--                @click="deleteQuestionOption(question, optionIndex)"-->
        <!--                >删除选项-->
        <!--              </a-button>-->
        <!--            </a-space>-->
        <!--          </a-form-item>-->
        <!--          &lt;!&ndash;题目选项结尾&ndash;&gt;-->
        <!--        </a-card>-->
      </a-form-item>

      <a-form-item>
        <a-button type="primary" style="width: 120px" html-type="submit"
          >提交
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import API from "@/api";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import { getAppVoByIdUsingGet } from "@/api/appController";
import { APP_TYPE_ENUM } from "@/constant/app";
import PictureUploader from "@/components/PictureUploader.vue";

import { withDefaults, defineProps } from "vue";
import {
  addQuestionUsingPost,
  editQuestionUsingPost,
  listQuestionVoByPageUsingPost,
} from "@/api/questionController";
import AiGenerateQuestionDrawer from "@/views/add/components/AiGenerateQuestionDrawer.vue";

interface Props {
  appId: string;
}

const props = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  },
});
const router = useRouter();

// 题目内容结构
const questionContent = ref<API.QuestionContentDTO[]>([]);
// 添加题目
const addQuestion = (index: number) => {
  questionContent.value.splice(index, 0, {
    title: "",
    options: [],
  });
};
// 删除题目
const deleteQuestion = (index: number) => {
  questionContent.value.splice(index, 1);
};
// 添加题目选项
const addQuestionOption = (question: API.QuestionContentDTO, index: number) => {
  if (!question.options) {
    question.options = [];
  }
  question.options.splice(index, 0, {
    key: "",
    value: "",
  });
};
// 删除题目选项
const deleteQuestionOption = (
  question: API.QuestionContentDTO,
  index: number
) => {
  if (!question.options) {
    question.options = [];
  }
  question.options.splice(index, 1);
};

const app = ref<API.AppVO>({});

const oldQuestion = ref<API.QuestionVO>();

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.appId) {
    return;
  }
  // 加载题目选项
  const res = await listQuestionVoByPageUsingPost({
    appId: props.appId as any,
    current: 1,
    pageSize: 1,
    sortField: "createTime",
    sortOrder: "descend",
  });
  if (res.data.code === 0 && res.data.data?.records) {
    oldQuestion.value = res.data.data.records[0];
    if (oldQuestion.value) {
      questionContent.value = oldQuestion.value.questionContent ?? [];
    }
  } else {
    Message.error("获取数据失败，" + res.data.message);
  }
  // 加载题目
  const res2 = await getAppVoByIdUsingGet({
    id: props.appId as any,
  });
  if (res2.data.code === 0 && res2.data.data) {
    app.value = res2.data.data;
  } else {
    Message.error("获取数据失败，" + res2.data.message);
  }
};
watchEffect(() => {
  loadData();
});

/**
 * 提交
 */
const handleSubmit = async () => {
  if (!props.appId || !questionContent.value) {
    return;
  }
  let res: any;
  if (oldQuestion.value?.id) {
    res = await editQuestionUsingPost({
      id: oldQuestion.value.id,
      questionContent: questionContent.value,
    });
  } else {
    res = await addQuestionUsingPost({
      appId: props.appId as any,
      questionContent: questionContent.value,
    });
  }
  if (res.data.code === 0) {
    Message.success("操作成功,即将跳转到应用详情页");
    setTimeout(() => {
      router.push(`/app/detail/${props.appId}`);
    }, 3000);
  } else {
    Message.error("操作失败，" + res.data.message);
  }
};
/**
 * AI 生成题目成功后执行
 */
const onAiGenerateSuccess = (result: API.QuestionContentDTO[]) => {
  Message.success(`AI 生成题目成功，生成${result.length}道题目`);
  questionContent.value = [...questionContent.value, ...result];
};
/**
 * AI 生成题目成功后执行(SSE)
 */
const onAiGenerateSuccessSSE = (result: API.QuestionContentDTO) => {
  questionContent.value = [...questionContent.value, result];
};
const onSSEStart=()=>{
  Message.success("开始生成")
}
const onSSEClose=()=>{
  Message.success("生成完毕")
}
</script>
