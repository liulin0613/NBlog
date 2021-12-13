package com.zxqax.nblog.dto;

import com.zxqax.nblog.vo.ArticlePageVO;
import lombok.Data;

import java.util.List;

/**
 * 文章分页信息
 * @author liulin
 */

@Data
public class ArticlePageDTO {
    /**
     * 文章总数
     */
    private int total;

    /**
     * 当前分页页码
     */
    private int current_page;


    /**
     * 当前分页文章
     */
    private List<ArticlePageVO> articles;
}
