import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 检查用户权限
 * @param loginUser
 * @param needAccess
 * @return 有无权限
 */
const checkAccess = (
  loginUser: API.LoginUserVO,
  needAccess = ACCESS_ENUM.NOT_LOGIN
) => {
  const loginUserAccess = loginUser?.userRole ?? ACCESS_ENUM.NOT_LOGIN;
  if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
    return true;
  }
  // 如果用户要登陆才能访问
  if (needAccess === ACCESS_ENUM.USER) {
    if (loginUserAccess === ACCESS_ENUM.NOT_LOGIN) {
      return false;
    }
  }
  // 如果管理员才能访问
  if (needAccess === ACCESS_ENUM.ADMIN) {
    if (loginUserAccess !== ACCESS_ENUM.ADMIN) {
      return false;
    }
  }
  return true;
};

export default checkAccess;
