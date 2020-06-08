package com.example.dbl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dbl.entity.EsNoteBook;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: liuhuan
 * @Description: es 工具
 * @date: 2020/5/20
 */
@Service
@Slf4j
public class ESUtil {

    private String INDEX = "bang_test";

    @Autowired
    private RestHighLevelClient esClient;


    public List<JSONObject> queryByKeyword(String keyword){

        try {
            SearchRequest searchRequest = new SearchRequest(INDEX);
            // 1.matchQuery：会将搜索词分词，再与目标查询字段进行匹配，若分词中的任意一个词与目标字段匹配上，则可查询到。
            // ①operator  or 表示 只要有一个词在文档中出现则就符合条件，and表示每个词都在文档中出现则才符合条件。
            // ②spring开发框架 如果被分为三个词：spring、开发、框架 设置"minimum_should_match": "80%"表示，三个词在文档的匹配占比为80%，即3*0.8=2.4，向上取整得2，表示至少有两个词在文档中要匹配成功。
            // minimum_should_match 和 operator 是and关系
            MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("noteContent", keyword).operator(Operator.OR);
            // 2.termQuery：不会对搜索词进行分词处理，而是作为一个整体与目标字段进行匹配，若完全匹配，则可查询到。
            // TermQueryBuilder queryBuilder = QueryBuilders.termQuery("noteContent", keyword);
            // 3.multiMatchQuery 多字段查询 field("noteBookName", 10) 将"noteBookName" 权重提高10倍
            // MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, "noteContent", "noteBookName", "bookAuthor").field("noteBookName", 10);
            // 4.布尔查询对应于Lucene的BooleanQuery查询，实现将多个查询组合起来。三个参数：
            // must：文档必须匹配must所包括的查询条件，相当于 “AND”
            // should：文档应该匹配should所包括的查询条件其中的一个或多个，相当于 “OR”
            // must_not：文档不能匹配must_not所包括的该查询条件，相当于“NOT”
            // BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("noteContent", keyword)).should(QueryBuilders.matchQuery("notebookName", keyword)).mustNot(QueryBuilders.termQuery("noteId", 1));

            // 5.Filter 过滤器
            // ①过虑是针对搜索的结果进行过虑，过虑器主要判断的是文档是否匹配，不去计算和判断文档的匹配度得分，所以过虑器性能比查询要高，且方便缓存，推荐尽量使用过虑器去实现查询或者过虑器和查询共同使用。
            // searchRequest.source().postFilter 后置过滤器 在搜索结果的基础上进行过滤
            // queryBuilder.filter(QueryBuilders.termQuery("noteContent", keyword));
            // 6.sort
            // searchRequest.source().sort("noteId",SortOrder.DESC)
            // fetchSource source源字段过滤 第一个参数包含，第二个参数不包含，会同时生效 像下面的实例会返回一个空对象
            // searchRequest.source().fetchSource(new String[]{"noteContent"}, new String[]{"noteContent"}).from(0).size(50).query(queryBuilder);
            // 7.HighlightBuilder
            // fragment_size 字段高亮显示的片段的字符长度大小，default: 100
            // number_of_fragment 最多返回片段数，default: 5
            // 8.RequestOptions
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags("<highlight>");
            highlightBuilder.postTags("</highlight>");
            highlightBuilder.field("noteContent").fragmentSize(2).numOfFragments(5);
            searchRequest.source().highlighter(highlightBuilder).sort("noteId",SortOrder.DESC).fetchSource(new String[]{}, new String[]{"noteCreatorId"}).from(0).size(50).query(queryBuilder);
            System.out.println("fragmentSize: " + searchRequest.source().highlighter().fragmentSize());
            SearchResponse search = esClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = search.getHits().getHits();
            List<JSONObject> list = new ArrayList<>();
            for (SearchHit hit : hits) {
                JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(hit.getSourceAsMap()));
                for (Map.Entry<String, HighlightField> entry : hit.getHighlightFields().entrySet()) {
                    HighlightField value = entry.getValue();
                    int i = 0;
                    for (Text text : value.getFragments()) {
                        i++;
                        System.out.println(i + ":  " +text.toString());
                    }
                    jsonObject.put("noteContent1", value.getFragments()[0].toString());
                }
                list.add(jsonObject);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insertES(EsNoteBook esNoteBook) throws Exception{
        IndexRequest indexRequest = new IndexRequest(INDEX);
        JSONObject object = JSONObject.parseObject(JSON.toJSONString(esNoteBook));
        indexRequest.id(esNoteBook.getNoteId().toString()).source(object);
        IndexResponse indexResponse = esClient.index(indexRequest, RequestOptions.DEFAULT);
        RestStatus status = indexResponse.status();
        log.info("笔记新增状态: " + status.getStatus());
        return true;
    }

    public boolean deleteES(Long noteId) throws Exception{
        DeleteRequest deleteRequest = new DeleteRequest(INDEX);
        deleteRequest.id(noteId.toString());
        //DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(INDEX);
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("noteId", noteId);
        // deleteByQueryRequest.setQuery(termQueryBuilder);
        DeleteResponse deleteResponse = esClient.delete(deleteRequest, RequestOptions.DEFAULT);
        RestStatus status = deleteResponse.status();

        log.info("笔记删除状态: " + status.getStatus());
        return true;
    }

    public boolean updateES(Long noteId,String noteContent) throws Exception{
        // DeleteRequest deleteRequest = new DeleteRequest();
        // deleteRequest.id(noteId.toString());
        UpdateRequest updateRequest = new UpdateRequest(INDEX,noteId.toString());
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
        builder.field("noteContent", noteContent).endObject();
        updateRequest.doc(builder);
        UpdateResponse updateResponse = esClient.update(updateRequest, RequestOptions.DEFAULT);
        log.info("笔记更新状态: " + updateResponse.status().getStatus());
        return true;
    }
}
