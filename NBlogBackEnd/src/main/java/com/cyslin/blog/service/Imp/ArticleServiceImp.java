package com.cyslin.blog.service.Imp;

import com.cyslin.blog.bean.Article;
import com.cyslin.blog.bean.Msg;
import com.cyslin.blog.mapper.ArticleMapper;
import com.cyslin.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Comparator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ArticleServiceImp implements ArticleService {
    @Autowired
    ArticleMapper mapper;

    @Override
    public Msg addArticle(Article article) {
        Date date=new Date();
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String time=df.format(date);
        try {
            mapper.addArticle(article.getATitle(),article.getATags(),article.getAScope(),
                    article.getAContent(),time, article.getAAuthor());
            return Msg.success();
        }catch (Exception e){
            return Msg.fail().add("error",e.getMessage());
        }
    }

    @Override
    public List<Article> getArticleData(int page, int limit,String name) {
        return mapper.getArticleData(page*limit,limit,name);
    }

    @Override
    public Article getDetailsById(int id) {
        return mapper.getDetailsById(id);
    }

    @Override
    public List<Map.Entry<String, Integer>> getHotTags() {
        List<String> tagString=mapper.getHotTags();
        Map<String, Integer> map = new HashMap<>();
        for (String s : tagString) {
            String[] tags=s.split(",");
            for (String tag : tags) {
                if(!map.containsKey(tag)){
                    map.put(tag,1);
                }else{
                    map.put(tag, map.get(tag)+1);
                }
            }
        }

        //自定义比较器
        Comparator<Map.Entry<String, Integer>> valCmp = (o1, o2) -> o2.getValue()-o1.getValue();

        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet()); //传入maps实体
        list.sort(valCmp);

        if(list.size()<=10){
            return list;
        }else {
            return list.subList(0,10);
        }
    }

    @Override
    public Msg uploadimg(String  file) {
        String base = File.separator + "usr" + File.separator + "images" + File.separator + "nblog" + File.separator + "markdown" + File.separator;
//        String base="D:\\";
        String uuid = UUID.randomUUID().toString().replaceAll("-","").substring(0,6);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd"); //生成日期格式
        String datePrefix = dateFormat.format(new Date()); //生成当前日期作为前缀

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(base+ datePrefix +uuid+".txt"));
            out.write(file);
            out.close();
            return Msg.success().add("url",datePrefix+""+uuid);
        } catch (IOException e) {
            return Msg.fail().add("error","上传失败");
        }
    }

    @Override
    public Msg getBase64ByUrl(String[] name,String time) {
        Map<String,String> urls=new HashMap<>();
        String base = File.separator + "usr" + File.separator + "images" + File.separator + "nblog" + File.separator + "markdown" + File.separator;
//        String base="D:\\";
        for (String s : name) {
            File file = new File(base+""+s+".txt");

            System.out.println(file.exists());
            BufferedReader reader = null;
            StringBuilder bf=new StringBuilder();
            try {
                if(file.exists()){
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    while ((tempString = reader.readLine()) != null) {
                        bf.append(tempString);
                    }
                    reader.close();
                    String aurl=bf.toString();
                    urls.put(s,aurl);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }

        String[] names=new String[urls.size()];
        String[] url=new String[urls.size()];
        int i=0;
        for (String s : urls.keySet()) {
            names[i]=s;
            url[i]=urls.get(s);
            i++;
        }

        return Msg.success().add("names",names).add("url",url);
    }

    @Override
    public Msg delImg(String path) {
        String base = File.separator + "usr" + File.separator + "images" + File.separator + "nblog" + File.separator + "markdown" + File.separator;
//        String base="D:\\";
        File file = new File(base+path+".txt");
        if(file.exists()){
            System.out.println(file.delete());
        }
        return Msg.success();
    }

    @Override
    public int getArticleLen() {
        return mapper.getArticleLen();
    }

    @Override
    public Msg getAllTitles() {
        List<Article> titlesAndIds= mapper.getAllTitles();
        Collections.shuffle(titlesAndIds);
        List<String> titles=new ArrayList<>();
        List<Integer> ids=new ArrayList<>();

        for (Article titlesAndId : titlesAndIds) {
            titles.add(titlesAndId.getATitle());
            ids.add(titlesAndId.getAId());
        }
        return Msg.success().add("titles",titles).add("ids",ids);
    }

    @Override
    public Msg getPublished(String name) {
        List<Article> published=mapper.getPublished(name);
        return Msg.success().add("articles",published);
    }

    @Override
    public Msg deleteArticle(int aid) {
        mapper.deleteArticle(aid);
        return Msg.success();
    }

    @Override
    public Msg getArticleInfo(int aid) {
        Article article=mapper.getArticleInfo(aid);
        return Msg.success().add("article",article);
    }

    @Override
    public Msg getEchartsData(String name) {
        Calendar cal = Calendar.getInstance();
        List<Integer> monthCounts=new ArrayList<>();
        List<Integer> yearCounts=new ArrayList<>();

        int year = cal.get(Calendar.YEAR);
        for(int i=1;i<=12;i++){
            String month="";
            if(i<10){
                month=year+"-0"+i;
            }else month=year+"-"+i;

            int count=mapper.getEchartsDataByMonth(name,month);
            monthCounts.add(count);
        }

        for(int i=0;i<3;i++){
            int count=mapper.getEchartsDataByYear(name,(year-i)+"");
            yearCounts.add(count);
        }

        return Msg.success().add("monthCounts",monthCounts).add("yearCounts",yearCounts);
    }

    @Override
    public Msg getAllTags() {
        List<String> alltags=mapper.getHotTags();
        Map<String, Integer> tagMap = new HashMap<>();
        for (String s : alltags) {
            String[] tags=s.split(",");
            for (String tag : tags) {
                if(tag.length()>0){
                    if(!tagMap.containsKey(tag)){
                        tagMap.put(tag,1);
                    }else{
                        tagMap.put(tag, tagMap.get(tag)+1);
                    }
                }
            }
        }

        List<String> tagname=new ArrayList<>();
        List<Integer> tagcount=new ArrayList<>();
        for (String s : tagMap.keySet()) {
            tagname.add(s);
            tagcount.add(tagMap.get(s));
        }
        return Msg.success().add("tagname",tagname).add("tagcount",tagcount);
    }

    @Override
    public Msg getTagArticleData(String tagname,String name) {
        List<Article> articles=mapper.getTagArticleData(tagname,name);
        return Msg.success().add("articles",articles);
    }

    @Override
    public Msg addfavorites(int aid, String name) {
        int uid=mapper.getIdByname(name);
        Date date=new Date();
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String time=df.format(date);

        mapper.addfavorites(aid,uid,time);
        return Msg.success();
    }

    @Override
    public Msg hasfavorites(int aid, String name) {
        int uid=mapper.getIdByname(name);
        int count = mapper.hasfavorites(aid,uid);
        return Msg.success().add("count",count);
    }

    @Override
    public Msg deletefavorites(int aid, String name) {
        int uid=mapper.getIdByname(name);
        mapper.deletefavorites(aid,uid);
        return Msg.success();
    }

    @Override
    public Msg getAllfavorites(String name) {
        int uid=mapper.getIdByname(name);
        List<Article> articles=mapper.getAllfavorites(uid);
        return Msg.success().add("article",articles);
    }

    @Override
    public Msg addDraft(Article article) {
        Date date=new Date();
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String time=df.format(date);
        try {
            mapper.addDraft(article.getATitle(),article.getATags(),article.getAScope(),
                    article.getAContent(),time, article.getAAuthor());

            return Msg.success();
        }catch (Exception e){
            return Msg.fail().add("error",e.getMessage());
        }
    }

    @Override
    public Msg getAllDraft(String name) {
        List<Article> drafts=mapper.getAllDraft(name);
        return Msg.success().add("drafts",drafts);
    }

    @Override
    public Msg deleteDraft(int did) {
        mapper.deleteDraft(did);
        return Msg.success();
    }

    @Override
    public Msg getDraftInfo(int did) {
        Article draft=mapper.getDraftInfo(did);
        return Msg.success().add("draft",draft);
    }
}