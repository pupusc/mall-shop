package com.soybean.elastic.booklistmodel.service;


import com.soybean.elastic.collect.factory.AbstractCollectFactory;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.setting.api.constant.SearchWeightConstant;
import com.wanmi.sbc.setting.api.provider.weight.SearchWeightProvider;
import com.wanmi.sbc.setting.api.response.weight.SearchWeightResp;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/2 4:26 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class EsBookListModelService {


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SearchWeightProvider searchWeightProvider;


    public void listEsBookListModel(String key){
        float defaultBoost = 0f;
        List<SearchWeightResp> context = searchWeightProvider.list(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_KEY).getContext();
        Map<String, Float> searchWeightMap = new HashMap<>();
        for (SearchWeightResp searchWeightResp : context) {
            if (StringUtils.isEmpty(searchWeightResp.getWeightValue())) {
                continue;
            }
            searchWeightMap.put(searchWeightResp.getWeightKey(), Float.parseFloat(searchWeightResp.getWeightValue()));
        }
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(AbstractCollectFactory.INDEX_ES_BOOK_LIST_MODEL);
        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();
        boolQb.should(matchQuery("bookListName", key)).
                boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_BOOK_LIST_NAME, defaultBoost));
        boolQb.should(matchQuery("spuName", key)).
                boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_SPU_NAME, defaultBoost));



    }
}
