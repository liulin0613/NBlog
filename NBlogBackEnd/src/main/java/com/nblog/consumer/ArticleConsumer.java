package com.nblog.consumer;

import com.alibaba.fastjson.JSON;
import com.nblog.dao.ArticleDao;
import com.nblog.dao.UserDao;
import com.nblog.dto.ArticleDTO;
import com.nblog.entity.Article;
import com.nblog.enums.SearchStrategyEnum;
import com.nblog.service.ESService;
import com.nblog.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.nblog.constant.CommonConst.MDS_PATH;
import static com.nblog.constant.MQPrefixConst.ES_QUEUE;

/**
 * @author liulin
 * 文章消费者
 */

@Component
@RabbitListener(queues = ES_QUEUE)
@Slf4j
public class ArticleConsumer {
    private final UserDao userDao;
    private final ArticleDao articleDao;
    private final ESService esService;

    /**
     * 构造器方式注入
     * @param userDao
     * @param articleDao
     * @param esService
     */
    @Autowired
    public ArticleConsumer(UserDao userDao,ArticleDao articleDao,ESService esService){
        this.userDao = userDao;
        this.articleDao=articleDao;
        this.esService=esService;
    }

    // 用 ThreadLocal 保证 SimpleDateFormat 线程安全
    private ThreadLocal<SimpleDateFormat> localSimpleDataFormat=
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    /**
     * rabbitmq article 消费者
     * @param art
     */
    @RabbitHandler
    public void process(String art){
        Article article = JSON.parseObject(art,Article.class);

        // 根据用户 id 来区分是添加文档还是删除文档
        if(article.getUserId()!=-1){
            // 添加文档
            addArticle(article);
        }else {
            // 删除文档
            esService.deleteDocument(SearchStrategyEnum.NBLOG.getDesc(),article.getId()+"");
        }

        // 文章已消费，打印日志
        log.info("消费文章：" + article.toString());
    }

    /**
     * 添加文章
     * 写入磁盘
     * @param article
     */
    private void addArticle(Article article){
        String content = article.getArticleContent();

        // 写入磁盘
        String base= MDS_PATH + article.getUserId() + File.separator;
        String fileName=article.getArticleTitle()+"_"+article.getId();
        // 格式化文件名
        fileName= CommonUtils.regFileName(fileName);
        File file=new File(base);
        if (!file.exists()){
            boolean success=file.mkdirs();
            String event="为用户 [ "+article.getUserId()+" ] 创建 md 文件夹 "+success;
            log.info(event);
        }

        // 写入文件
        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(base+ fileName+".md"));
            out.write(content);
            out.close();

            // 更新数据库中的文件路径
            articleDao.updateArticlePath(article.getId(),fileName+".md");

            // 公开文件 ,写入 ES
            if(article.getStatus() == 1){
                ArticleDTO esArticle = new ArticleDTO();
                esArticle.setAuthor(userDao.getNameByID(article.getUserId()));
                esArticle.setContent(content);

                Date date =new Date();
                SimpleDateFormat df = localSimpleDataFormat.get();
                String time = df.format(date);
                esArticle.setCreateTime(time);
                esArticle.setId(article.getId()+"");
                esArticle.setTitle(article.getArticleTitle());

                esService.addDocument(SearchStrategyEnum.NBLOG.getDesc(),esArticle);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
