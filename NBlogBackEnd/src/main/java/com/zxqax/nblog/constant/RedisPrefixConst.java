package com.zxqax.nblog.constant;

/**
 * Redis  常量
 * @author liulin
 */

public class RedisPrefixConst {
    /**
     * 验证码过期时间
     */
    public static final long CODE_EXPIRE_TIME = 15 * 60;

    /**
     * 存储密码的 hash
     */
    public static final String PWD = "pwd";

    /**
     * 存储用户的 hash
     */
    public static final String EXISTS_NAME = "exists_name";

    /**
     * 存储邮箱的 hash
     */
    public static final String EXISTS_EMAIL = "exists_email";

    /**
     * 存储文章
     */
    public static final String ARTICLE = "article";

    /**
     * 存储在线用户
     */
    public static final String ONLINE = "online";

    /**
     * 存储访问量
     */
    public static final String VIEWS = "views";
}
