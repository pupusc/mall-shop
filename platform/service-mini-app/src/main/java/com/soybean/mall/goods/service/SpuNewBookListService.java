package com.soybean.mall.goods.service;
import com.google.common.collect.Lists;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.enums.SearchBookListSortTypeEnum;

import com.soybean.elastic.api.provider.booklistmodel.EsBookListModelProvider;
import com.soybean.elastic.api.req.EsBookListQueryProviderReq;
import com.soybean.elastic.api.resp.EsBookListModelResp;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 商品书单信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/10 2:35 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SpuNewBookListService {

    @Autowired
    private EsBookListModelProvider esBookListModelProvider;

    public void test(List<String> spuIdList, int pageSize) {
        if (CollectionUtils.isEmpty(spuIdList) || pageSize <= 0) {
            return;
        }
        Map<String, EsBookListModelResp> spuId2BookListMap = new HashMap<>();
        EsBookListQueryProviderReq req = new EsBookListQueryProviderReq();
        req.setSpuIds(spuIdList);
        req.setBooklistSortType(SearchBookListSortTypeEnum.HAS_TOP_UPDATE_TIME);
        req.setPageSize(pageSize);
        CommonPageResp<List<EsBookListModelResp>> context = esBookListModelProvider.listEsBookListModel(req).getContext();
        if (context.getTotal() <= 0L) {
            return;
        }
        for (EsBookListModelResp esBookListModelResp : context.getContent()) {

        }
    }
}
