import router from "@/router";
import { useLoginUserStore } from "@/store/useStore";
import ACCESS_ENUM from "@/access/accessEnum";
import checkAccess from "@/access/checkAccess";

// 进入页面前进行权限校验
router.beforeEach(async (to, from, next) => {
  const loginUserStore = useLoginUserStore();
  let loginUser = loginUserStore.loginUser;
  // 如果之前没尝试过获取登陆信息
  if (!loginUser || !loginUser.userRole) {
    await loginUserStore.fetchLoginUser();
    loginUser = loginUserStore.loginUser;
  }

  const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN;
  // 当前页面需要的权限
  if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
    // 没登陆则跳转
    if (
      !loginUser ||
      !loginUser.userRole ||
      loginUser.userRole === ACCESS_ENUM.NOT_LOGIN
    ) {
      next(`/user/login?redirect=${to.fullPath}`);
    }
    // 如果已经登陆,判断条件是否足够
    if (!checkAccess(loginUser, needAccess)) {
      next("/noAuth");
      return;
    }
  }
  next();
});
