package com.auth.server.security.dingding;

/**
 * Created by 李雷 on 2018/7/20.
 */
public class DingUrlConstant {

    public static final String DING_BASE_URL="https://oapi.dingtalk.com";

    public static final String DING_PERSISTENT_CODE_OF_SNS_URI="/sns/get_persistent_code";

    public static final String DING_TOKEN_OF_SNS_URI="/sns/gettoken";

    public static final String DING_SNS_TOKEN_OF_SNS_URI=DING_BASE_URL+"/sns/get_sns_token";

    public static final String DING_USER_INFO_OF_SNS_URI=DING_BASE_URL+"/sns/getuserinfo";

    public static final String DING_TOKEN_OF_COM_URI="/gettoken";

    public static final String DING_GET_USERID_URI="/user/getUseridByUnionid";

    public static final String DING_GET_USERINFO_URI="/user/get";
}
