package com.zxqax.nblog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.zxqax.nblog.constant.CommonConst;
import com.zxqax.nblog.dao.ArticleDao;
import com.zxqax.nblog.dto.*;
import com.zxqax.nblog.entity.Article;
import com.zxqax.nblog.entity.Draft;
import com.zxqax.nblog.enums.SearchStrategyEnum;
import com.zxqax.nblog.service.ArticleService;
import com.zxqax.nblog.service.ESService;
import com.zxqax.nblog.service.RedisService;
import com.zxqax.nblog.service.UserService;
import com.zxqax.nblog.utils.ThreadLocalUtils;
import com.zxqax.nblog.utils.ThreadUtils;
import com.zxqax.nblog.vo.ArticlePageVO;
import com.zxqax.nblog.vo.ArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zxqax.nblog.constant.MQPrefixConst.ES_EXCHANGE;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    private final String SERVER_PATH="your_path"; // 文章保存根路径

    /**
     * Spring 自动注入
     */
    private final ArticleDao articleDao;

    private final UserService userService;

    private final RabbitTemplate rabbitTemplate;

    public ArticleServiceImpl(ArticleDao articleDao, UserService userService,ESService esService,RabbitTemplate rabbitTemplate){
        this.articleDao=articleDao;
        this.userService = userService;
        this.rabbitTemplate=rabbitTemplate;
    }

    /**
     * 首页展示 分页获取文章
     * @param page 页数
     * @param limit 每页的条数
     * @return Result 包含文章列表信息
     */
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
    public Result<?> getArticleDetailsByID(int id) {
        int current_user_id = userService.getCurrentUser().getId();

        // +1 阅读量
        articleDao.addview(current_user_id,id);

        ArticleDetailsDTO article=articleDao.getArticleDetailsByID(id);
        article.setId(id);

        if(current_user_id!=-1){
            ArticleAboutUserDTO dto = articleDao.getOthers(current_user_id,article.getAuthorId(),id);
            article.setIs_Favorites(dto.getIsFavorites());
            article.setIs_Attention(dto.getIsAttention());
            article.setIs_like(dto.getIsLike());
        }
        return Result.ok(article);
    }

    @Override
    public Result<?> addDraft(ArticleVO articleVO) {
        int current_user_id = userService.getCurrentUser().getId();
        Draft draft = new Draft();
        if(current_user_id != -1){
            draft.setContent(articleVO.getContent());
            draft.setTitle(articleVO.getTitle());
            draft.setTags(articleVO.getTags());
            if(articleVO.getScope().equals("公开")){
                draft.setScope(1);
            }else {
                draft.setScope((articleVO.getScope().equals("私密")?2:3));
            }
            draft.setUserId(current_user_id);
            articleDao.addDraft(draft);

            return Result.ok();
        }

        return Result.fail("未登录");
    }

    @Override
    public Result<?> addArticle(ArticleVO articleVO) {
        int current_user_id = userService.getCurrentUser().getId();
        if(current_user_id != -1){
            Article article =new Article();
            String content=articleVO.getContent();
            // userID
            article.setUserId(current_user_id);
            // title
            article.setArticleTitle(articleVO.getTitle());
            // desc
            article.setArticleDesc(content.substring(0,Math.min(200,content.length())));
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
            article.setImg(articleImg);

            // 插入数据库并获取主键
            articleDao.addArticle(article);

            // 异步线程操作 ES 和 磁盘
            article.setArticleContent(content);
            rabbitTemplate.convertAndSend(ES_EXCHANGE,"", JSON.toJSONString(article));

            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public Result<?> getAllFavorites() {
        int user = userService.getCurrentUser().getId();
        if(user != -1){
            List<FavoriteDTO> favorites = articleDao.getAllFavorites(user);
            return Result.ok(favorites);
        }else {
            return Result.fail();
        }
    }

    @Override
    public Result<?> getNotArchived() {
        int user = userService.getCurrentUser().getId();
        if(user != -1){
            List<ArticleDTO> archived = articleDao.getNotArchived(user);
            return Result.ok(archived);
        }else {
            return Result.fail();
        }
    }

    @Override
    public Result<?> getDirs() {
        int user = userService.getCurrentUser().getId();
        if(user != -1){
            List<DirDTO> archived = articleDao.getDirs(user);
            return Result.ok(archived);
        }else {
            return Result.fail();
        }
    }

    @Override
    public Result<?> getDirsDetails(int aid) {
        int user = userService.getCurrentUser().getId();
        if(user != -1){
            List<ArticleDTO> articleDTOList = articleDao.getDirsDetails(user,aid);
            return Result.ok(articleDTOList);
        }else {
            return Result.fail();
        }
    }

    @Override
    public Result<?> searchByTitleAndDir(String title, int did) {
        int user = userService.getCurrentUser().getId();
        if(user != -1){
            List<ArticleDTO> articleDTOList;
            if(did == -1){
                articleDTOList = articleDao.searchByTitle(user,title);
            }else{
                articleDTOList = articleDao.searchByTitleAndDir(user,title,did);
            }
            return Result.ok(articleDTOList);
        }else {
            return Result.fail();
        }
    }

    @Override
    public Result<?> deleteArticle(int aid) {
        articleDao.deleteArticle(aid);

        int scope = articleDao.getScopeByID(aid);

        // rabbitmq 异步处理文章删除
        if(scope == 1){
            Article article = new Article();
            article.setUserId(-1);
            article.setId(aid);
            rabbitTemplate.convertAndSend(ES_EXCHANGE,"",JSON.toJSONString(article));
        }


        return Result.ok();
    }

    @Override
    public Result<?> addDir(String dirName) {
        int user = userService.getCurrentUser().getId();
        if(user != -1){
            articleDao.addDir(user,dirName);
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @Override
    public Result<?> removeArticle(int aid, int did) {
        articleDao.removeArticle(aid,did);
        return Result.ok();
    }

    @Override
    public Result<?> getPrintDetails(int aid) {
        PrintDTO printDTO = articleDao.getPrintDetails(aid);
        return Result.ok(printDTO);
    }

    @Override
    public Result<?> getRePublishArticle(int aid) {
        ArticleDetailsDTO dto = articleDao.getRePublishArticle(aid);
        return Result.ok(dto);
    }

    @Override
    public Result<?> reEditorSubmit(ArticleVO articleVO) {
        int current_user_id = userService.getCurrentUser().getId();
        if(current_user_id != -1){
            Article article =new Article();
            // title
            article.setArticleTitle(articleVO.getTitle());

            String content=articleVO.getContent();

            // status
            if(articleVO.getScope().equals("公开")){
                article.setStatus(1);
            }else {
                article.setStatus((articleVO.getScope().equals("私密")?2:3));
            }

            article.setUserId(articleVO.getAid());

            // desc
            article.setArticleDesc(content.substring(0,Math.min(200,content.length())));
            // tags
            article.setArticleTags(articleVO.getTags());

            // img
            String pattern = "(!\\[.*?\\]\\(.*?\\))";
            // 创建 Pattern 对象
            Pattern r = Pattern.compile(pattern);

            // 创建 matcher 对象
            Matcher m = r.matcher(content);

            String refileDesc = SERVER_PATH + current_user_id + File.separator + articleVO.getDesc();
            File file=new File(refileDesc);

            String articleImg = "";
            if (m.find()) {
                String img = m.group(0);
                articleImg = img.substring(img.lastIndexOf('(')+1,img.lastIndexOf(')'));
            }
            article.setImg(articleImg);

            if(file.exists()){
                boolean result = file.delete();
                System.out.println("删除文件 " + articleVO.getDesc() + ": " +result);
            }

            File newFile=new File(refileDesc);

            // 写入文件
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(newFile));
                out.write(content);
                out.close();

                // 更新数据
                articleDao.reEditorSubmit(article);
                // 返回结果
                return Result.ok();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return Result.fail();
    }

    @Override
    public Result<?> getRecommendArticle() {
        List<DirDTO> articles = articleDao.getRecommendArticle();
        return Result.ok(articles);
    }

    @Override
    public Result<?> deleteDir(int did) {
        articleDao.updateDir(did);
        articleDao.deleteDir(did);
        return Result.ok();
    }

    @Override
    public Result<?> getAllDraft() {
        int user = userService.getCurrentUser().getId();
        if(user != -1){
            List<DraftDTO> drafts = articleDao.getAllDraft(user);
            return Result.ok(drafts);
        }else {
            return Result.fail();
        }
    }

    @Override
    public Result<?> deleteDraft(int did) {
        articleDao.deleteDraft(did);
        return Result.ok();
    }

    @Override
    public Result<?> getReDraftInfo(int aid) {
        Draft draft = articleDao.getReDraftInfo(aid);
        return Result.ok(draft);
    }

    @Override
    public Result<?> getEchartsData() {
        int user = userService.getCurrentUser().getId();
        if(user != -1){
            Calendar cal = Calendar.getInstance();
            List<Integer> monthCounts=new ArrayList<>();
            List<Integer> yearCounts=new ArrayList<>();

            int year = cal.get(Calendar.YEAR);
            for(int i=1;i<=12;i++){
                String month="";
                if(i<10){
                    month=year+"-0"+i;
                }else month=year+"-"+i;

                int count=articleDao.getEchartsDataByMonth(user,month);
                monthCounts.add(count);
            }

            for(int i=0;i<3;i++){
                int count=articleDao.getEchartsDataByYear(user,(year-i)+"");
                yearCounts.add(count);
            }

            Map<String,List<Integer>> map=new HashMap<>();
            map.put("monthCounts",monthCounts);
            map.put("yearCounts",yearCounts);
            return Result.ok(map);
        }else {
            return Result.fail();
        }
    }
}
