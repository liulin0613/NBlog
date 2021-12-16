package com.zxqax.nblog.consumer;

import com.alibaba.fastjson.JSON;
import com.zxqax.nblog.dao.ArticleDao;
import com.zxqax.nblog.dao.UserDao;
import com.zxqax.nblog.dto.ArticleDTO;
import com.zxqax.nblog.entity.Article;
import com.zxqax.nblog.enums.SearchStrategyEnum;
import com.zxqax.nblog.service.ESService;
import com.zxqax.nblog.utils.FileUtils;
import com.zxqax.nblog.utils.ThreadLocalUtils;
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

import static com.zxqax.nblog.constant.MQPrefixConst.ES_QUEUE;

@Component
@RabbitListener(queues = ES_QUEUE)
@Slf4j
public class ArticleConsumer {

    private final String SERVER_PATH="your_path"; // 文章保存根路径

    @Autowired
    private UserDao userDao;

    @Autowired
    ArticleDao articleDao;

    @Autowired
    ESService esService;

    @RabbitHandler
    public void process(String art){
        Article article = JSON.parseObject(art,Article.class);

        if(article.getUserId()!=-1){
            addArticle(article);
        }else {
            esService.deleteDocument(SearchStrategyEnum.NBLOG.getDesc(),article.getId()+"");
        }

        log.info("消费文章：" + article.toString());
    }

    private void addArticle(Article article){
        String content = article.getArticleContent();

        // 写入磁盘
        String base= SERVER_PATH + article.getUserId() + File.separator;
        String fileName=article.getArticleTitle()+"_"+article.getId();
        // 格式化文件名
        fileName= FileUtils.regName(fileName);
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

            // 写入 ES
            if(article.getStatus() == 1){
                ArticleDTO esArticle = new ArticleDTO();
                esArticle.setAuthor(userDao.getNameByID(article.getUserId()));
                esArticle.setContent(content);

                Date date =new Date();
                SimpleDateFormat df = ThreadLocalUtils.localSimpleDataFormat.get();
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
