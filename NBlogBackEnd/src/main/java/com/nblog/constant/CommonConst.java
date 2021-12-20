package com.nblog.constant;

import java.io.File;

/**
 * 公共常量
 * @author liulin
 */

public final class CommonConst {

    /**
     * md 文件的存储根路径 /static/mds/
     */
    public static final String MDS_PATH= File.separator + "static" + File.separator+ "mds" + File.separator;

    /**
     * 图片 文件的存储根路径 /static/images/
     */
    public static final String IMAGE_PATH=File.separator + "static" + File.separator+ "images" + File.separator;

    /**
     * 头像 文件的存储根路径 /static/avatar/
     */
    public static final String AVATAR_PATH = File.separator + "static" + File.separator+ "avatar" + File.separator;

    /**
     * 作者搜索 高亮标签
     */
    public static final String PRE_AUTHOR_TAG = "<span style='color:#f47466'>";

    /**
     * 文章搜索 高亮标签
     */
    public static final String PRE_CONTENT_TAG = "<span style='color:#f47466'>";

    /**
     * 标题搜索 高亮标签
     */
    public static final String PRE_TITLE_TAG = "<span style='color:#f47466'>";

    /**
     * 混合搜索 高亮标签
     */
    public static final String PRE_MIXED_TAG = "<span style='color:#f47466'>";

    /**
     *  高亮标签 后缀
     */
    public static final String POST_TAG = "</span>";

    /**
     * JWT 验证头部标识
     */
    public static final String AUTH_HEADER_KEY = "Authorization";

    /**
     * token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";
}
