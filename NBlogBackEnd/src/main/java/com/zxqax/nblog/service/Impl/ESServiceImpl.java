package com.zxqax.nblog.service.Impl;

import com.alibaba.fastjson.JSON;
import com.zxqax.nblog.constant.CommonConst;
import com.zxqax.nblog.dto.ArticleDTO;
import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.enums.ArticleStatusEnum;
import com.zxqax.nblog.enums.SearchStrategyEnum;
import com.zxqax.nblog.service.ESService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zxqax.nblog.enums.StatusCodeEnum.VALID_ERROR;

@Service
@Slf4j
public class ESServiceImpl implements ESService {

    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @Autowired
    public ESServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.client = restHighLevelClient;
    }

    /**
     * 根据不同的类型执行不同的搜索策略
     * @param keyword 检索内容
     * @param type 检索类型
     */
    @Override
    public Result<?> search(String keyword, int type) {

        List<Map<String,Object>> res; // 存放搜索结果集

        if(type == SearchStrategyEnum.AUTHOR.getStatus()){  // 作者搜索
            res = searchByAuthor(keyword);
        }else if(type == SearchStrategyEnum.TITLE.getStatus()){  // 作者搜索
            res = searchByTitle(keyword);
        }else if(type == SearchStrategyEnum.CONTENT.getStatus()){   // 内容搜索
            res = searchByContent(keyword);
        }else if(type == SearchStrategyEnum.MIXED.getStatus()){  // 混合搜索
            res = searchByMixed(keyword);
        }else {
            return Result.fail(VALID_ERROR.getCode(),VALID_ERROR.getDesc());
        }

        return Result.ok(res);
    }

    /**
     * 增加文章
     * @param indexName 索引名
     * @param articleDto Document 信息
     */
    @Override
    public void addDocument(String indexName, ArticleDTO articleDto) {
        //创建请求
        IndexRequest request = new IndexRequest(indexName);

        // 规则 put /nblog/_doc/1
        request.id(articleDto.getId()+"");
        request.timeout(TimeValue.timeValueSeconds(3));

        // 将数据放入请求
        request.source(JSON.toJSONString(articleDto), XContentType.JSON);

        try {
            //客户端发送请求，获取响应结果
            IndexResponse index = client.index(request, RequestOptions.DEFAULT);
            log.info(index.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 删除文章
     * @param indexName 索引名
     * @param aid Document id
     */
    @Override
    public void deleteDocument(String indexName, String aid) {
        //创建请求
        DeleteRequest deleteRequest = new DeleteRequest(indexName, aid);
        DeleteResponse deleteResponse;
        try {

            // 删除 Document
            deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
            log.info(deleteResponse.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 通过作者搜索
     * @param keyword 搜索关键字
     * @return 搜索结果
     */
    private List<Map<String,Object>> searchByAuthor(String keyword){
        // 解析结果
        ArrayList<Map<String,Object>> res = new ArrayList<>();

        // 条件搜索
        SearchRequest searchRequest = new SearchRequest(SearchStrategyEnum.NBLOG.getDesc());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery(SearchStrategyEnum.AUTHOR.getDesc(), keyword))
                .mustNot(QueryBuilders.matchQuery(SearchStrategyEnum.SCOPE.getDesc(), ArticleStatusEnum.SECRET.getDesc()))
                .mustNot(QueryBuilders.matchQuery(SearchStrategyEnum.SCOPE.getDesc(), ArticleStatusEnum.VISIBLE_TO_FANS.getDesc()));

        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.timeout(new TimeValue(10, TimeUnit.SECONDS));

        sourceBuilder.size(20);

        // 高亮
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.requireFieldMatch(false); // 多个高亮显示
        highlightBuilder.field(SearchStrategyEnum.AUTHOR.getDesc());
        highlightBuilder.preTags(CommonConst.PRE_AUTHOR_TAG);
        highlightBuilder.postTags(CommonConst.POST_TAG);
        sourceBuilder.highlighter(highlightBuilder);

        // 执行搜索
        searchRequest.source(sourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            for (SearchHit hit : searchResponse.getHits().getHits()) {

                // 原来的结果
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                // 裁剪文章内容，减少网络传输速度
                String con = filterLength((String) hit.getSourceAsMap().get(SearchStrategyEnum.CONTENT.getDesc()));

                // 更新文章内容
                sourceAsMap.put(SearchStrategyEnum.CONTENT.getDesc(),con);

                // 解析高亮的 AUTHOR 字段
                String n_content = parseTheHighlightedFields(hit,SearchStrategyEnum.AUTHOR.getDesc());

                if(n_content.length()>1){
                    sourceAsMap.put(SearchStrategyEnum.AUTHOR.getDesc(), n_content);
                }

                res.add(sourceAsMap);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return res;
    }

    /**
     * 通过 标题 搜索
     * @param keyword 搜索关键字
     * @return 搜索结果
     */
    private List<Map<String, Object>> searchByTitle(String keyword) {
        // 解析结果
        ArrayList<Map<String,Object>> res = new ArrayList<>();

        // 条件搜索
        SearchRequest searchRequest = new SearchRequest(SearchStrategyEnum.NBLOG.getDesc());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery(SearchStrategyEnum.TITLE.getDesc(), keyword))
                .mustNot(QueryBuilders.matchQuery(SearchStrategyEnum.SCOPE.getDesc(), ArticleStatusEnum.SECRET.getDesc()))
                .mustNot(QueryBuilders.matchQuery(SearchStrategyEnum.SCOPE.getDesc(), ArticleStatusEnum.VISIBLE_TO_FANS.getDesc()));

        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.timeout(new TimeValue(10, TimeUnit.SECONDS));

        sourceBuilder.size(20);
        // 设置是否按查询匹配度排序
        sourceBuilder.explain(true);

        // 高亮
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.requireFieldMatch(false); // 多个高亮显示
        highlightBuilder.field(SearchStrategyEnum.TITLE.getDesc());
        highlightBuilder.preTags(CommonConst.PRE_TITLE_TAG);
        highlightBuilder.postTags(CommonConst.POST_TAG);
        sourceBuilder.highlighter(highlightBuilder);

        // 执行搜索
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            for (SearchHit hit : searchResponse.getHits().getHits()) {

                // 原来的结果
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                // 裁剪文章内容，减少网络传输速度
                String con = filterLength((String) hit.getSourceAsMap().get(SearchStrategyEnum.CONTENT.getDesc()));

                // 更新文章内容
                sourceAsMap.put(SearchStrategyEnum.CONTENT.getDesc(),con);

                // 解析高亮的 TITLE 字段
                String n_content = parseTheHighlightedFields(hit,SearchStrategyEnum.TITLE.getDesc());

                if(n_content.length()>1){
                    sourceAsMap.put(SearchStrategyEnum.TITLE.getDesc(), n_content);
                }

                res.add(sourceAsMap);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return res;
    }

    /**
     * 搜索 通过内容
     * @param keyword 搜索关键字
     * @return 搜索结果
     */
    private List<Map<String, Object>> searchByContent(String keyword) {
        // 解析结果
        ArrayList<Map<String,Object>> res = new ArrayList<>();

        List<String> list = new ArrayList<>();

        String[] s1 = keyword.split(" ");

        // 条件搜索
        SearchRequest searchRequest = new SearchRequest(SearchStrategyEnum.NBLOG.getDesc());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery(SearchStrategyEnum.CONTENT.getDesc(), keyword))
                .mustNot(QueryBuilders.matchQuery(SearchStrategyEnum.SCOPE.getDesc(), ArticleStatusEnum.SECRET.getDesc()))
                .mustNot(QueryBuilders.matchQuery(SearchStrategyEnum.SCOPE.getDesc(), ArticleStatusEnum.VISIBLE_TO_FANS.getDesc()));

        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.timeout(new TimeValue(10, TimeUnit.SECONDS));

        sourceBuilder.size(20);
        // 设置是否按查询匹配度排序
        sourceBuilder.explain(true);

        // 高亮
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field(SearchStrategyEnum.CONTENT.getDesc());
        highlightBuilder.preTags(CommonConst.PRE_CONTENT_TAG);
        highlightBuilder.postTags(CommonConst.POST_TAG);
        sourceBuilder.highlighter(highlightBuilder);

        // 执行搜索
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            for (SearchHit hit : searchResponse.getHits().getHits()) {

                list.clear();

                for (String s : s1) {
                    if(list.size() < 3){
                        list.add(s);
                    }
                }

                // 解析高亮的字段
                Map<String, HighlightField> fields = hit.getHighlightFields();
                HighlightField content = fields.get(SearchStrategyEnum.CONTENT.getDesc());
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();// 原来的结果

                if(content!=null){
                    Text[] fragments = content.getFragments();
                    StringBuilder n_content= new StringBuilder();

                    // 保证即使没有全匹配字段时也能返回搜索数据
                    StringBuilder replace = new StringBuilder();

                    for (Text text : fragments) {

                        // 最大限度保证多个关键词都被搜索到
                        if(list.size()>0){
                            for (String s : list) {

                                if(replace.length()<750){
                                    replace.append(text);
                                }

                                if (ifAllIncluded(s,text.toString())) {
                                    list.remove(s);
                                    n_content.append(text.toString());
                                    break;
                                }
                            }
                        }else {
                            if(n_content.length()>700)
                                break;
                            else {

                                if(replace.length()<400){
                                    replace.append(text);
                                }

                                if (ifAllIncluded(s1[0],text.toString())) {
                                    n_content.append(text.toString());
                                }
                            }
                        }
                    }

                    // 字段替换
                    if(n_content.toString().length()<300){
                        n_content = replace;
                    }

                    String filter_con =  filterContent(n_content.toString());
                    sourceAsMap.put(SearchStrategyEnum.CONTENT.getDesc(),filter_con);
                }
                res.add(sourceAsMap);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return res;
    }


    /**
     * 混合模式搜索
     * @param keyword keyword 搜索关键字
     * @return 搜索结果
     */
    private List<Map<String, Object>> searchByMixed(String keyword) {
        // 解析结果
        ArrayList<Map<String,Object>> res = new ArrayList<>();

        // 条件搜索
        SearchRequest searchRequest = new SearchRequest(SearchStrategyEnum.NBLOG.getDesc());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery(SearchStrategyEnum.AUTHOR.getDesc(), keyword))
                .should(QueryBuilders.matchQuery(SearchStrategyEnum.TITLE.getDesc(), keyword))
                .should(QueryBuilders.matchQuery(SearchStrategyEnum.CONTENT.getDesc(), keyword))
                .mustNot(QueryBuilders.matchQuery(SearchStrategyEnum.SCOPE.getDesc(), ArticleStatusEnum.SECRET.getDesc()))
                .mustNot(QueryBuilders.matchQuery(SearchStrategyEnum.SCOPE.getDesc(), ArticleStatusEnum.VISIBLE_TO_FANS.getDesc()));

        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.timeout(new TimeValue(10, TimeUnit.SECONDS));

        sourceBuilder.size(20);

        // 设置是否按查询匹配度排序
        sourceBuilder.explain(true);

        // 高亮
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.field(SearchStrategyEnum.AUTHOR.getDesc());
        highlightBuilder.field(SearchStrategyEnum.TITLE.getDesc());
        highlightBuilder.field(SearchStrategyEnum.CONTENT.getDesc());
        highlightBuilder.preTags(CommonConst.PRE_MIXED_TAG);
        highlightBuilder.postTags(CommonConst.POST_TAG);
        sourceBuilder.highlighter(highlightBuilder);

        // 执行搜索
        searchRequest.source(sourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            for (SearchHit hit : searchResponse.getHits().getHits()) {

                // 原来的结果
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();


                Map<String, HighlightField> fields = hit.getHighlightFields();

                // 解析高亮的 AUTHOR 字段
                String parse_author = parseTheHighlightedFields(hit,SearchStrategyEnum.AUTHOR.getDesc());

                if(parse_author.length()>1){
                    sourceAsMap.put(SearchStrategyEnum.AUTHOR.getDesc(), parse_author);
                }

                // 解析高亮的 TITLE 字段
                String parse_title = parseTheHighlightedFields(hit,SearchStrategyEnum.TITLE.getDesc());

                if(parse_title.length()>1){
                    sourceAsMap.put(SearchStrategyEnum.TITLE.getDesc(), parse_title);
                }


                // 解析高亮的 CONTENT 字段
                HighlightField content = fields.get(SearchStrategyEnum.CONTENT.getDesc());

                if(content!=null){
                    Text[] fragments = content.getFragments();
                    StringBuilder n_content= new StringBuilder();

                    for (Text text : fragments) {
                        if(n_content.length()<550){
                            n_content.append(text);
                        }else {
                            break;
                        }
                    }

                    String filter_con =  filterContent(n_content.toString());
                    sourceAsMap.put(SearchStrategyEnum.CONTENT.getDesc(),filter_con);
                }

                res.add(sourceAsMap);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        // 限制 CONTENT 的长度
        for (Map<String, Object> re : res) {
            String con = (String) re.get(SearchStrategyEnum.CONTENT.getDesc());
            if(!con.contains(CommonConst.PRE_CONTENT_TAG)){
                if(con.length()>420)
                    re.put(SearchStrategyEnum.CONTENT.getDesc(),con.substring(0,420));
            }
        }

        return res;
    }

    /**
     * 解析高亮字段
     * @param hit hit
     * @param desc 类型
     * @return String
     */
    private String parseTheHighlightedFields(SearchHit hit, String desc) {
        Map<String, HighlightField> fields = hit.getHighlightFields();

        HighlightField field = fields.get(desc);

        // 将原来的字段换为我们高亮的字段即可!
        if(field!=null){
            Text[] fragments = field.getFragments();
            StringBuilder n_content= new StringBuilder();

            for (Text text : fragments) {
                n_content.append(text);
            }

            return  n_content.toString();
        }

        return "";

    }

    /**
     * 判断是否全包含
     * @param toBeMatched 待匹配字符串
     * @param motherBoard 母串
     * @return boolean
     */
    private boolean ifAllIncluded(String toBeMatched,String motherBoard){
        for (int i = 0; i < toBeMatched.length(); i++) {
            if(motherBoard.indexOf(toBeMatched.charAt(i))==-1){
                return false;
            }
        }
        return true;
    }

    /**
     * 截取 内容 字段，加快网络传输速率
     * @param oldContent 旧的内容
     * @return 裁剪之后的内容
     */
    private String filterLength(String oldContent){
        if(oldContent.length() > 550){
            oldContent = oldContent.substring(0,550);
        }

        oldContent = filterContent(oldContent);

        if(oldContent.length() > 400){
            oldContent = oldContent.substring(0,400);
        }

        return oldContent;
    }


    /**
     * 去掉无用标签
     * @param oldContent 旧的内容
     * @return 裁剪之后的内容
     */
    private String filterContent(String oldContent){
        oldContent = oldContent.replace("hljs-","");
        oldContent = oldContent.replace("image.png","");
        oldContent = oldContent.replace("<font color=","");
        oldContent = oldContent.replace("</font>","");
        oldContent = oldContent.replace("<hr>","");
        oldContent = oldContent.replace("<br>","");
        oldContent = oldContent.replace("*","");

        return oldContent;
    }
}
