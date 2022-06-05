package com.soybean.elastic.booklistmodel.service;


import com.soybean.elastic.collect.factory.AbstractCollectFactory;
import com.wanmi.sbc.common.util.EsConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

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



    public void listEsBookListModel(String key){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(AbstractCollectFactory.INDEX_ES_BOOK_LIST_MODEL);
        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();
        boolQb.should(matchQuery("bookListName", key)).boost(1f);
        boolQb.should(matchQuery("bookListName", key)).boost(1f);



    }
}
