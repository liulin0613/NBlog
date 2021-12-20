package com.nblog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.nblog.dao.ArticleDao;
import com.nblog.dto.*;
import com.nblog.entity.Article;
import com.nblog.entity.Draft;
import com.nblog.service.ArticleService;
import com.nblog.service.RedisService;
import com.nblog.service.UserService;
import com.nblog.utils.StringUtils;
import com.nblog.vo.ArticlePageVO;
import com.nblog.vo.ArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.nblog.constant.CommonConst.MDS_PATH;
import static com.nblog.constant.MQPrefixConst.ES_EXCHANGE;
import static com.nblog.constant.RedisPrefixConst.*;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    private ArticleDao articleDao;
    private UserService userService;
    private RedisService redisService;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public ArticleServiceImpl(ArticleDao articleDao, UserService userService, RedisService redisService, RabbitTemplate rabbitTemplate) {
        this.articleDao = articleDao;
        this.userService = userService;
        this.redisService = redisService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Result<ArticlePageDTO> getArticlesByPagination(int page, int limit) {
        ArticlePageDTO pageDTO = new ArticlePageDTO();
        // 文章总量
        int number = articleDao.getTotalArticleCount();
        // 当前页文章详情
        List<ArticlePageVO> pageVO = articleDao.getArticlesByPagination(page*limit,limit);

        pageDTO.setArticles(pageVO);
        pageDTO.setCurrent_page(page+1);
        pageDTO.setTotal(number);

        return Result.ok(pageDTO);
    }

    @Override
    public Result<ArticleDetailsDTO> getArticleDetailsByID(int id) {
        // 获取当前登录用户 ID
        int current_user_id = userService.getCurrentUser().getId();

        // +1 阅读量 , 使用有序集合 sorted set 进行存储
        redisService.zIncr(Article_VIEW,id,1.0);

        // 获取与当前用户无关的信息
        ArticleDetailsDTO article=articleDao.getArticleDetailsByID(id);
        article.setId(id);

        // 获取 redis 中的统计信息
        // 总访问
        Double zScore = redisService.zScore(Article_VIEW, id);
        double v = zScore != null? zScore :0.;
        article.setAllView((int)v);

        // 总收藏
        Object o = redisService.hGet(Article_COLLECT, id + "");
        int c = o != null?Integer.parseInt(o.toString()):0;
        article.setAllCollect(c);

        // 总点赞
        Object o1 = redisService.hGet(Article_LIKE, id + "");
        int l = o1 != null?Integer.parseInt(o1.toString()):0;
        article.setAllLike(l);

        // 如果用户登录，则获取用户的关注信息
        if(current_user_id!=-1){
            // 是否关注作者
            article.setIs_Attention(redisService.sIsMember(USER_ATTENTION+current_user_id,article.getAuthorId()));
            // 是否收藏文章
            article.setIs_Collect(redisService.sIsMember(USER_COLLECT+current_user_id,id));
            // 是否给文章点赞
            article.setIs_like(redisService.sIsMember(USER_LIKE+current_user_id,id));
        }

        return Result.ok(article);
    }

    @Override
    public Result<Boolean> addDraft(ArticleVO articleVO) {
        // 获取用户 id
        int current_user_id = userService.getCurrentUser().getId();
        // 新建草稿
        Draft draft = new Draft();
        draft.setContent(articleVO.getContent());
        draft.setTitle(articleVO.getTitle());
        draft.setTags(articleVO.getTags());

        if(articleVO.getScope().equals("公开")){
            draft.setScope(1);
        }else {
            draft.setScope((articleVO.getScope().equals("私密")?2:3));
        }
        draft.setUserId(current_user_id);

        // 草稿添加到 mysql
        articleDao.addDraft(draft);

        return Result.ok(true);
    }

    @Override
    public Result<Boolean> addArticle(ArticleVO articleVO) {
        // 获取基本内容
        Article article =articleCopy(articleVO);

        int current_user_id = userService.getCurrentUser().getId();
        // userID
        article.setUserId(current_user_id);
        // img
        article.setImg(findImage(articleVO.getContent()));

        // 插入数据库并获取主键
        articleDao.addArticle(article);

        // rabbitMQ 消息队列操作 ES 和 磁盘
        article.setArticleContent(articleVO.getContent());
        rabbitTemplate.convertAndSend(ES_EXCHANGE,"", JSON.toJSONString(article));

        return Result.ok(true);
    }

    @Override
    public Result<Boolean> deleteArticle(int aid) {
        // MySQL 中置文章为删除标记位
        articleDao.deleteArticle(aid);

        // 获取文章的公开范围
        int scope = articleDao.getScopeByID(aid);

        // 如果是公开文章，则 rabbitmq 异步处理 ES 中文章删除
        if(scope == 1){
            Article article = new Article();
            article.setUserId(-1); // -1 表示删除消息标志位
            article.setId(aid);
            rabbitTemplate.convertAndSend(ES_EXCHANGE,"",JSON.toJSONString(article));
        }

        return Result.ok(true);
    }

    @Override
    public Result<PrintDTO> getPrintDetails(int aid) {
        // 从 Mysql 中获取打印信息
        PrintDTO printDTO = articleDao.getPrintDetails(aid);

        // 如果文件夹为 null 赋值为未归档
        if(StringUtils.isBlank(printDTO.getDir())){
            printDTO.setDir("未归档");
        }

        // 返回结果
        return Result.ok(printDTO);
    }

    @Override
    public Result<ArticleDetailsDTO> getRePublishArticle(int aid) {
        // 从 Mysql 中获取文章详情
        ArticleDetailsDTO dto = articleDao.getRePublishArticle(aid);

        // 返回结果
        return Result.ok(dto);
    }

    @Override
    public Result<Boolean> reEditorSubmit(ArticleVO articleVO) {
        // 获取基本内容
        Article article =articleCopy(articleVO);

        // 获取当前用户
        int current_user_id = userService.getCurrentUser().getId();

        // userID
        article.setUserId(current_user_id);

        // img
        article.setImg(findImage(articleVO.getContent()));

        // 删除源文件
        String refileDesc = MDS_PATH + current_user_id + File.separator + articleVO.getDesc();
        File file=new File(refileDesc);

        if(file.exists()){
            boolean result = file.delete();
            log.info("删除文件 " + articleVO.getDesc() + ": " +result);
        }

        // 创建新文件
        File newFile=new File(refileDesc);
        // 写入文件
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(newFile));
            out.write(articleVO.getContent());
            out.close();

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        // 更新 mysql 中文章数据
        articleDao.reEditorSubmit(article);

        // 返回结果
        return Result.ok(true);
    }

    @Override
    public Result<List<DirDTO>> getRecommendArticle() {
        // 从 redis 中获取文章
        Map<Object, Double> map = redisService.
                zReverseRangeWithScore(Article_VIEW, 0, 4);

        // 利用 类似于 pipeline 技术批量从 mysql 中获取文章详情数据
        List<Integer> aids = new ArrayList<>();
        for (Object o : map.keySet()) {
            aids.add(Integer.parseInt(o.toString()));
        }

        // 用 DirDTO 承载数据
        List<DirDTO> articles = articleDao.getRecommendArticle(aids);

        // 填充阅读量
        for (DirDTO article : articles) {
            Double score = map.get(article.getId());
            int view = (int)((double) score);

            article.setCount(view);
        }

        // 对 articles 降序进行排序
        articles.sort((o1, o2) -> o2.getCount() - o1.getCount());

        // 返回结果
        return Result.ok(articles);
    }

    @Override
    public Result<List<DraftDTO>> getAllDraft() {
        // 获取当前用户信息
        int user = userService.getCurrentUser().getId();

        // 从 mysql 中获取草稿信息
        List<DraftDTO> drafts = articleDao.getAllDraft(user);

        // 返回结果
        return Result.ok(drafts);
    }

    @Override
    public Result<Boolean> deleteDraft(int did) {
        // 从 Mysql 中删除草稿
        articleDao.deleteDraft(did);

        // 返回结果
        return Result.ok(true);
    }

    @Override
    public Result<Draft> getReDraftInfo(int did) {
        // 从 Mysql 中获取草稿信息
        Draft draft = articleDao.getReDraftInfo(did);

        // 返回结果
        return Result.ok(draft);
    }

    @Override
    public Result<Map<String,List<Integer>>> getEchartsData() {
        // 获取当前用户信息
        int user = userService.getCurrentUser().getId();

        Calendar cal = Calendar.getInstance();
        // 存储月份数据
        List<Integer> monthCounts=new ArrayList<>();
        // 存储年份数据
        List<Integer> yearCounts=new ArrayList<>();

        // 本年度，获取月度数据
        int year = cal.get(Calendar.YEAR);
        for(int i=1;i<=12;i++){
            String month="";
            if(i<10){
                month=year+"-0"+i;
            }else month=year+"-"+i;

            int count=articleDao.getEchartsDataByMonth(user,month);
            monthCounts.add(count);
        }

        // 获取近三年发表数据
        for(int i=0;i<3;i++){
            int count=articleDao.getEchartsDataByYear(user,(year-i)+"");
            yearCounts.add(count);
        }

        // 包装返回数据
        Map<String,List<Integer>> map=new HashMap<>();
        map.put("monthCounts",monthCounts);
        map.put("yearCounts",yearCounts);

        // 返回结果
        return Result.ok(map);
    }

    @Override
    public Result<List<ArticleDTO>> getPublishedByUser(int user) {
        // 从 MySQL 中获取
        List<ArticleDTO> articles=articleDao.getPublishedByUser(user);
        // 返回结果
        return Result.ok(articles);
    }

    /**
     * 从 ArticleVO 中复制内容到 Article
     * @param articleVO
     * @return
     */
    private Article articleCopy(ArticleVO articleVO){
        Article article =new Article();
        // title
        article.setArticleTitle(articleVO.getTitle());
        // desc
        article.setArticleDesc(articleVO.getContent().substring(0,Math.min(200,articleVO.getContent().length())));
        // content
        article.setArticleContent("");
        // tags
        article.setArticleTags(articleVO.getTags());
        // status
        if(articleVO.getScope().equals("公开")){
            article.setStatus(1);
        }else {
            article.setStatus((articleVO.getScope().equals("私密")?2:3));
        }
        return article;
    }

    /**
     * 从博文内容中提取第一张图片
     * @param content
     * @return
     */
    private String findImage(String content){
        // img
        String pattern = "(!\\[.*?\\]\\(.*?\\))";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 创建 matcher 对象
        Matcher m = r.matcher(content);

        String articleImg = "";
        if (m.find()) {
            String img = m.group(0);
            articleImg = img.substring(img.lastIndexOf('(')+1,img.lastIndexOf(')'));
        }

        return articleImg;
    }
}
