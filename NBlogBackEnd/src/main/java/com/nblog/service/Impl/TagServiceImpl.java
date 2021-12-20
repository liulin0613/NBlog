package com.nblog.service.Impl;

import com.nblog.dao.TagDao;
import com.nblog.dto.Result;
import com.nblog.dto.TagArticleDTO;
import com.nblog.dto.TagDTO;
import com.nblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements TagService {
    private TagDao tagDao;

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Result<List<TagDTO>> getHotTags() {
        // 直接从 MySQL 中取值
        List<TagDTO> hotTags = getTagsFromMySQL(1);

        // 返回结果
        return Result.ok(hotTags);
    }

    @Override
    public Result<List<TagDTO>> getAllTags() {
        // 直接从 MySQL 中取值
        List<TagDTO> allTags = getTagsFromMySQL(0);

        // 返回结果
        return Result.ok(allTags);
    }

    @Override
    public Result<List<TagArticleDTO>> getTagArticleData(String tag) {
        // 直接从 MySQL 中取值
        List<TagArticleDTO> articles=tagDao.getTagArticleData(tag);

        // 返回结果
        return Result.ok(articles);
    }

    /**
     * 从 MySQL 中获取 标签
     * @param type 类型 0 全部 | 1，热点标签
     * @return TagDTO list [ tagName tagCount]
     */
    private List<TagDTO> getTagsFromMySQL(int type){

        // 获取 所有 Tags
        List<String> tagString = tagDao.getTags();

        // 对 Tags list 进行解析
        // 采用分拣存储的思想统计不同标题的次数
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

        // 分拣后将数据存储于 List<TwoAttributes> 中
        List<TagDTO> hotHags=new ArrayList<>();

        for (String s : map.keySet()) {
            hotHags.add(new TagDTO(s,map.get(s)));
        }

        // 对 List 降序进行排序
        hotHags.sort((o1, o2) -> Integer.parseInt(o2.getTagCount() + "") - Integer.parseInt(o1.getTagCount() + ""));

        if(type==1){
            // 取 Top_10 Tag
            if(hotHags.size()>10){
                hotHags=hotHags.subList(0,10);
            }
        }

        return hotHags;
    }
}
