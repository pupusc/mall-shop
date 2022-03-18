package com.wanmi.sbc.elastic.api.common;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.TerminalSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/11 2:48 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
public class CommonEsSearchCriteriaBuilder {


    /**
     * 查询es_goodsinfo 索引中的中公共部分
     * @return
     */
    public static BoolQueryBuilder getSkuCommonSearchCriterialBuilder(BaseRequest baseRequest) {
        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();
//        if (baseRequest.getGoodsChannelType() != null) {
//            boolQb.must(termQuery("goodsChannelTypeSet", baseRequest.getGoodsChannelType()));
//        }

        if (!CollectionUtils.isEmpty(baseRequest.getGoodsChannelTypeSet())) {
            if (baseRequest.getGoodsChannelTypeSet().size() == 1) {
                log.info("CommonEsSearchCriteriaBuilder getSkuCommonSearchCriterialBuilder single channelTypeSet");
                boolQb.must(termQuery("goodsChannelTypeList", baseRequest.getGoodsChannelTypeSet().get(0).toString()));
            } else {
                log.info("CommonEsSearchCriteriaBuilder getSkuCommonSearchCriterialBuilder two channelTypeSet");
                boolQb.must(termsQuery("goodsChannelTypeList", baseRequest.getGoodsChannelTypeSet()));
            }
        }
        return boolQb;
    }


    /**
     * 查询 es_goods 索引中的公共部分
     * @return
     */
    public static BoolQueryBuilder getSpuCommonSearchCriterialBuilder(BaseRequest baseRequest) {
        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();
//        if (baseRequest.getGoodsChannelType() != null) {
//            boolQb.must(termQuery("goodsChannelTypeSet", baseRequest.getGoodsChannelType()));
//        }

        if (!CollectionUtils.isEmpty(baseRequest.getGoodsChannelTypeSet())) {
            if (baseRequest.getGoodsChannelTypeSet().size() == 1) {
                log.info("CommonEsSearchCriteriaBuilder getSpuCommonSearchCriterialBuilder single channelTypeSet");
                boolQb.must(termQuery("goodsChannelTypeList", baseRequest.getGoodsChannelTypeSet().get(0).toString()));
            } else {
                log.info("CommonEsSearchCriteriaBuilder getSpuCommonSearchCriterialBuilder two channelTypeSet");

                boolQb.must(termsQuery("goodsChannelTypeList", baseRequest.getGoodsChannelTypeSet()));
            }

        }
        return boolQb;
    }
}
