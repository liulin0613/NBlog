package com.nblog.constant;

/**
 * Redis  常量
 * @author liulin
 */

public final class RedisPrefixConst {
    /**
     * 验证码过期时间
     */
    public static final long CODE_EXPIRE_TIME = 15 * 60;

    /**
     * 验证码前缀 String
     */
    public static final String CODE_PREFIX = "yzm_";

    /**
     * 文章访问量 sorted set
     */
    public static final String Article_VIEW = "article_view";

    /**
     * 文章收藏量 hash
     */
    public static final String Article_COLLECT = "article_collect";

    /**
     * 文章点赞量 hash
     */
    public static final String Article_LIKE = "article_like";

    /**
     * 存储用户的 hash
     */
    public static final String EXISTS_NAME = "exists_name";

    /**
     * 存储邮箱的 hash
     */
    public static final String EXISTS_EMAIL = "exists_email";

    /**
     * 存储在线用户 hash
     */
    public static final String ONLINE = "online";

    /**
     * 存储网页访问量 String
     */
    public static final String VIEWS = "views";

    /**
     * 用户收藏前缀 set
     */
    public static final String USER_COLLECT = "u_c_";

    /**
     * 用户点赞前缀 set
     */
    public static final String USER_LIKE = "u_l_";

    /**
     * 用户关注前缀 set
     */
    public static final String USER_ATTENTION = "u_t_";

    /**
     * 用户粉丝前缀 set
     */
    public static final String USER_FANS = "u_f_";
}
