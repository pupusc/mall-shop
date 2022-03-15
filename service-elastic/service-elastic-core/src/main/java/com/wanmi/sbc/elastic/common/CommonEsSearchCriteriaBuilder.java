package com.wanmi.sbc.elastic.common;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/11 2:48 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class CommonEsSearchCriteriaBuilder {


    /**
     * 查询es_goodsinfo 索引中的中公共部分
     * @return
     */
    public static BoolQueryBuilder getSkuCommonSearchCriterialBuilder(CommonEsSearchCriteriaRequest request) {
        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();

        return boolQb;
    }


    /**
     * 查询 es_goods 索引中的公共部分
     * @return
     */
    public static BoolQueryBuilder getSpuCommonSearchCriterialBuilder(CommonEsSearchCriteriaRequest request) {
        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();

        return boolQb;
    }
}
