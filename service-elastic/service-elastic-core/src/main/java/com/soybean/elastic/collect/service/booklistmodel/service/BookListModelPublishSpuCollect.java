package com.soybean.elastic.collect.service.booklistmodel.service;

import com.soybean.elastic.booklistmodel.model.EsBookListModel;
import com.soybean.elastic.booklistmodel.model.sub.EsBookListSubSpuNew;
import com.soybean.elastic.collect.service.booklistmodel.AbstractBookListModelCollect;
import com.wanmi.sbc.goods.api.provider.collect.CollectBookListModelProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectBookListModelProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectBookListModelProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectBookListGoodsPublishResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: 打包书单商品信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 10:44 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class BookListModelPublishSpuCollect extends AbstractBookListModelCollect {

    @Autowired
    private CollectBookListModelProvider collectBookListModelProvider;

    private List<CollectBookListGoodsPublishResponse> listCollectBookListGoodsPublish(LocalDateTime beginTime, LocalDateTime endtime) {
        //获取商品列表
        CollectBookListModelProviderReq request = new CollectBookListModelProviderReq();
        request.setBeginTime(beginTime);
        request.setEndTime(endtime);
        request.setPageSize(MAX_PAGE_SIZE);
        return collectBookListModelProvider.collectBookListGoodsPublishId(request).getContext();
    }

    @Override
    public Set<Long> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        Set<Long> booklistModelIdSet = new HashSet<>();
        List<CollectBookListGoodsPublishResponse> collectBookListGoodsPublishResponseList = this.listCollectBookListGoodsPublish(lastCollectTime, now);
        for (CollectBookListGoodsPublishResponse collectBookListGoodsPublishResponse : collectBookListGoodsPublishResponseList) {
            booklistModelIdSet.add(collectBookListGoodsPublishResponse.getBookListId().longValue());
        }
        //可能没有获取完，再取一次
        while (collectBookListGoodsPublishResponseList.size() >= MAX_PAGE_SIZE) {
            LocalDateTime updateTime = collectBookListGoodsPublishResponseList.get(collectBookListGoodsPublishResponseList.size() - 1).getUpdateTime();
            collectBookListGoodsPublishResponseList = this.listCollectBookListGoodsPublish(updateTime, now);
            for (CollectBookListGoodsPublishResponse collectBookListGoodsPublishResponse : collectBookListGoodsPublishResponseList) {
                booklistModelIdSet.add(collectBookListGoodsPublishResponse.getBookListId().longValue());
            }
        }
        return booklistModelIdSet;
    }

    @Override
    public <F> List<F> collect(List<F> list) {
        List<Integer> bookListModelIdList = list.stream().map(ex -> ((EsBookListModel)ex).getBookListId().intValue()).collect(Collectors.toList());

        //根据商品id获取商品信息
        CollectBookListModelProviderReq collectBookListModelProviderRequest = new CollectBookListModelProviderReq();
        collectBookListModelProviderRequest.setBookListModelIds(bookListModelIdList);
        List<CollectBookListGoodsPublishResponse> context =
                collectBookListModelProvider.collectBookListGoodsPublishIdByBookListIds(collectBookListModelProviderRequest).getContext();
        Map<Integer, List<CollectBookListGoodsPublishResponse>> bookListModelId2spuIdMap =
                context.stream().collect(Collectors.groupingBy(CollectBookListGoodsPublishResponse::getBookListId));
        for (F f : list) {
            EsBookListModel esBookListModel = (EsBookListModel) f;
            List<CollectBookListGoodsPublishResponse> collectBookListGoodsPublishResponseList = bookListModelId2spuIdMap.get(esBookListModel.getBookListId().intValue());
            if (CollectionUtils.isEmpty(collectBookListGoodsPublishResponseList)) {
                continue;
            }
            List<EsBookListSubSpuNew> subSpuNews = new ArrayList<>();
            for (CollectBookListGoodsPublishResponse collectBookListGoodsPublishParam : collectBookListGoodsPublishResponseList) {
                EsBookListSubSpuNew esBookListSubSpuNew = new EsBookListSubSpuNew();
                esBookListSubSpuNew.setSpuId(collectBookListGoodsPublishParam.getSpuId());
                esBookListSubSpuNew.setSortNum(collectBookListGoodsPublishParam.getOrderNum());
                subSpuNews.add(esBookListSubSpuNew);
            }
            esBookListModel.setSpus(subSpuNews);
            esBookListModel.setSpuNum(collectBookListGoodsPublishResponseList.size());
        }
        return list;
    }
}
