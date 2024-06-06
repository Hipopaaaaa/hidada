<template>
  <div id="adminUserPage">
    <a-form
      :model="formSearchParams"
      :style="{ marginBottom: '20px' }"
      label-align="left"
      layout="inline"
      auto-label-width
      @submit="doSearch"
    >
      <a-form-item field="userName" label="用户名">
        <a-input
          v-model="formSearchParams.userName"
          placeholder="请输入用户名..."
        />
      </a-form-item>
      <a-form-item field="userProfile" label="用户简介">
        <a-input
          v-model="formSearchParams.userProfile"
          placeholder="请输入用户简介..."
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" style="width: 100px" html-type="submit"
          >搜索
        </a-button>
      </a-form-item>
    </a-form>

    <a-table
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
      }"
      @page-change="pageChange"
    >
      <template #userAvatar="{ record }">
        <a-image width="64" :src="record.userAvatar" />
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button status="danger" @click="doDelete(record)">删除</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, watchEffect } from "vue";
import {
  deleteUserUsingPost,
  listUserByPageUsingPost,
} from "@/api/userController";
import API from "@/api";
import { Message } from "@arco-design/web-vue";

const formSearchParams = ref<API.UserQueryRequest>({});

const initSearchParams = {
  current: 1,
  pageSize: 10,
};

const searchParams = ref<API.UserQueryRequest>({
  ...initSearchParams,
});

const dataList = ref<API.User[]>([]);
const total = ref<number>(0);
/**
 * 加载数据
 */
const loadData = async () => {
  const res = await listUserByPageUsingPost(searchParams.value);
  if (res.data.code === 0) {
    dataList.value = res.data.data?.records || [];
    total.value = res.data.data?.total || 0;
  } else {
    Message.error("获取数据失败，" + res.data.message);
  }
};

const pageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

const doDelete = async (record: API.User) => {
  if (!record.id) {
    return;
  }
  const res = await deleteUserUsingPost({ id: record.id });
  if (res.data.code === 0) {
    loadData();
  } else {
    Message.error("删除失败，" + res.data.message);
  }
};

const doSearch = () => {
  searchParams.value = { ...initSearchParams, ...formSearchParams.value };
};

watchEffect(() => {
  loadData();
});

// 表格列配置
const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "账号",
    dataIndex: "userAccount",
  },
  {
    title: "用户名",
    dataIndex: "userName",
  },
  {
    title: "用户头像",
    dataIndex: "userAvatar",
    slotName: "userAvatar",
  },
  {
    title: "用户简介",
    dataIndex: "userProfile",
  },
  {
    title: "权限",
    dataIndex: "userRole",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    slotName: "createTime",
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
    slotName: "updateTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];
</script>
