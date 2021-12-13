package com.zxqax.nblog;

import com.zxqax.nblog.dao.ArticleDao;
import com.zxqax.nblog.dto.ArticleDTO;
import com.zxqax.nblog.enums.SearchStrategyEnum;
import com.zxqax.nblog.service.ESService;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class NblogApplicationTests {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    ESService esService;

//    @Test
    public void getAllArticle() throws IOException, InterruptedException {
//        List<ArticleDTO> articles=articleDao.getAllArticle();
////
//        for (ArticleDTO article : articles) {
//            File file=new File("D:\\mds\\"+article.getContent());
//            if(file.exists()){
//                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//                StringBuilder res = new StringBuilder();
//                String line;
//
//                while ((line=bufferedReader.readLine())!=null){
//                    res.append(line);
//                }
//                article.setContent(res.toString());
//            }
////
//            System.out.println(article.getId());
//            System.out.println(article.getAuthor());
//            System.out.println(article.getTitle());
//            System.out.println(article.getCreateTime());
//            System.out.println(article.getContent());
//            System.out.println("----------");
////
//            esService.addDocument(SearchStrategyEnum.NBLOG.getDesc(),article);
//            System.out.println(article.getId() + "  -> success !");
//            Thread.sleep(500);
//        }
    }

//    @Test
    public void getArticleImages() throws IOException {
//        List<ArticleDTO> articles=articleDao.getAllArticle();
//        for (ArticleDTO article : articles) {
//            File file=new File("D:\\mds\\"+article.getContent());
//            if(file.exists()){
//                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//                StringBuilder res = new StringBuilder();
//                String line;
//
//                while ((line=bufferedReader.readLine())!=null){
//                    res.append(line);
//                }
//
//                String pattern = "(!\\[.*?\\]\\(.*?\\))";
//                // 创建 Pattern 对象
//                Pattern r = Pattern.compile(pattern);
//
//                // 现在创建 matcher 对象
//                Matcher m = r.matcher(res.toString());
//
//
//                String articleImg = "";
//                if (m.find()) {
//                    String img = m.group(0);
//                    articleImg = img.substring(img.lastIndexOf('(')+1,img.lastIndexOf(')'));
//                }
//
//                articleDao.updateImg(Integer.parseInt(article.getId()),articleImg);
//
//                System.out.println(article.getId() + " IMG --> " + articleImg + "--> finished");
//            }
//        }
    }
}
