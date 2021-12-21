package com.nblog.consumer;

import com.alibaba.fastjson.JSON;
import com.nblog.dao.ArticleDao;
import com.nblog.dao.UserDao;
import com.nblog.dto.ArticleDTO;
import com.nblog.entity.Article;
import com.nblog.enums.SearchStrategyEnum;
import com.nblog.service.ESService;
import com.nblog.service.RedisService;
import com.nblog.utils.CommonUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
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
import static com.nblog.constant.RedisPrefixConst.ARTICLE_MESSAGE;
import static com.nblog.constant.RedisPrefixConst.CONSUMER_ARTICLE_MESSAGE;

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
    private final RedisService redisService;

    /**
     * 构造器方式注入
     * @param userDao
     * @param articleDao
     * @param esService
     */
    @Autowired
    public ArticleConsumer(UserDao userDao,ArticleDao articleDao,ESService esService,RedisService redisService){
        this.userDao = userDao;
        this.articleDao=articleDao;
        this.esService=esService;
        this.redisService =redisService;
    }

    // 用 ThreadLocal 保证 SimpleDateFormat 线程安全
    private ThreadLocal<SimpleDateFormat> localSimpleDataFormat=
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    /**
     * rabbitmq article 消费者
     */
    @RabbitHandler
    public void process(String msg, Channel channel, Message message){
        // 获取消息 UUID
        String uuid = (String) message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");

        // 幂等性，防止重复消费
        if(redisService.sIsMember(CONSUMER_ARTICLE_MESSAGE,uuid)){
            // 待消费的文章
            Article article = JSON.parseObject(msg,Article.class);

            // 根据用户 id 来区分是添加文档还是删除文档
            if(article.getUserId()!=-1){
                // 添加文档
                addArticle(article);
            }else {
                // 删除文档
                esService.deleteDocument(SearchStrategyEnum.NBLOG.getDesc(),article.getId()+"");
            }

            // 从 Redis 中删除待消费
            redisService.sRemove(CONSUMER_ARTICLE_MESSAGE,uuid);
            // 从 Redis 中删除该条消息
            redisService.hDel(ARTICLE_MESSAGE,uuid);
        }

        try {
            // 手动执行 ack
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            // 文章已消费，打印日志
            log.info("【消费者】： 成功消费消息 " + uuid);

        } catch (IOException e) {
            //消费者处理出了问题，需要告诉队列信息消费失败
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                        false, true);
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }

            System.err.println("【消费者】： 消费消息 "+ uuid +"失败");
        }
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
