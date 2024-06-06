import axios from "axios";
import { Message } from "@arco-design/web-vue";

const myAxios = axios.create({
  baseURL: "http://localhost:8101",
  timeout: 10000,
  withCredentials: true,
});

myAxios.interceptors.request.use(
  function (config) {
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);
myAxios.interceptors.response.use(
  function (response) {
    const { data } = response;
    if (data.code === 40100) {
      // 不是获取用户信息的请求，并且用户目前不是已经在用户页面，则跳转到登陆页面
      if (
        !response.request.responseURL.includes("user/get/login") &&
        !window.location.pathname.includes("user/login")
      ) {
        window.location.href = `/user/login?redirect=${window.location.href}`;
      }
    }
    return response;
  },
  function (error) {
    return Promise.reject(error);
  }
);

export default myAxios;
