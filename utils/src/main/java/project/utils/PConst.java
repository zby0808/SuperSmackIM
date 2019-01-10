package project.utils;

import java.nio.charset.Charset;

/**
 * 提供一些常量
 * Created by zby on 2016/12/9.
 */

public class PConst {

    public static final String DEFAULT_ENC = "utf-8";

    public static Charset UTF_8 = Charset.forName("utf-8");

    /**
     * SharedPreference默认读取参数
     */
    public static final String SP_CONFIG = "pac_config";

    /**
     * 默认保存账户Key
     */
    public static final String LOCAL_ACCOUNT = "LOCAL_ACCOUNT";

    /**
     * 默认保存密码Key
     */
    public static final String LOCAL_PWD = "LOCAL_PWD";

}
