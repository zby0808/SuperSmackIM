package com.example.administrator.testim.bean;

/**
 * Created by bangyong.zhang on 2018/6/4.
 */

public class LoginUserBean {


    /**
     * token : b57d8018-8c7c-4382-80e2-443190cfbbba
     * message : 此 token 应当以 x-auth-token 属性附在请求的 Header 中
     * userInfo : {"userId":"b040a562-84ed-4169-a320-6f64e1268b6e","userName":"王榜金","policeNo":"057050","orgId":62,"orgCode":"440300060100","orgName":"警务督察处政秘科","roleId":0}
     */

    private String token;
    private String message;
    private UserInfoBean userInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfoBean {
        /**
         * userId : b040a562-84ed-4169-a320-6f64e1268b6e
         * userName : 王榜金
         * policeNo : 057050
         * orgId : 62
         * orgCode : 440300060100
         * orgName : 警务督察处政秘科
         * roleId : 0
         */

        private String userId;
        private String userName;
        private String policeNo;
        private String orgId;
        private String orgCode;
        private String orgName;
        private int roleId;
        private int roleLev;
        private int rootDeptId;

        public int getRoleLev() {
            return roleLev;
        }

        public void setRoleLev(int roleLev) {
            this.roleLev = roleLev;
        }

        public int getRootDeptId() {
            return rootDeptId;
        }

        public void setRootDeptId(int rootDeptId) {
            this.rootDeptId = rootDeptId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPoliceNo() {
            return policeNo;
        }

        public void setPoliceNo(String policeNo) {
            this.policeNo = policeNo;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }
    }
}
