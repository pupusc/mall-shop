package com.soybean.elastic.collect.service.booklistmodel.service;

import com.soybean.elastic.booklistmodel.model.EsBookListModel;
import com.soybean.elastic.booklistmodel.model.sub.EsBookListSubSpuNew;
import com.soybean.elastic.collect.service.booklistmodel.AbstractBookListModelCollect;
import com.wanmi.sbc.goods.api.provider.collect.CollectBookListModelProvider;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectBookListModelProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.collect.CollectBookListGoodsPublishResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 打包书单商品信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 10:44 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class BookListModelSpuCollect extends AbstractBookListModelCollect {


    @Autowired
    private CollectSpuProvider collectSpuProvider;

    @Autowired
    private CollectBookListModelProvider collectBookListModelProvider;

    /**
     * 获取商品对象信息
     * @param lastCollectTime
     * @param now
     * @return
     */
    private List<GoodsVO> getSpuByTime(LocalDateTime lastCollectTime, LocalDateTime now) {
        CollectSpuProviderReq request = new CollectSpuProviderReq();
        request.setBeginTime(lastCollectTime);
        request.setEndTime(now);
        request.setPageSize(MAX_PAGE_SIZE);
        return collectSpuProvider.collectSpuIdByTime(request).getContext();
    }

    /**
     * 获取书单id列表信息
     * @param goodsVOList
     * @return
     */
    private Set<Long> getBookListModelId(List<GoodsVO> goodsVOList) {
        Set<Long> bookListModelIdSet = new HashSet<>();
        if (CollectionUtils.isEmpty(goodsVOList)) {
            return bookListModelIdSet;
        }
        List<String> goodsIdList = goodsVOList.stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
        //根据商品id 获取对应的书单
        CollectBookListModelProviderReq req = new CollectBookListModelProviderReq();
        req.setSpuIds(goodsIdList);
        List<CollectBookListGoodsPublishResponse> context = collectBookListModelProvider.collectBookListGoodsPublishIdBySpuIds(req).getContext();
        for (CollectBookListGoodsPublishResponse collectBookListGoodsPublishParam : context) {
            bookListModelIdSet.add(collectBookListGoodsPublishParam.getBookListId().longValue());
        }
        return bookListModelIdSet;
    }

    /**
     * 聚合书单id列表
     * @param lastCollectTime
     * @param now
     * @return
     */
    private Set<Long> collectBookListModelId(LocalDateTime lastCollectTime, LocalDateTime now) {
        List<GoodsVO> goodsVOList = this.getSpuByTime(lastCollectTime, now);
        Set<Long> bookListModelIdSet = this.getBookListModelId(goodsVOList);
        while (goodsVOList.size() >= MAX_PAGE_SIZE) {
            LocalDateTime updateTime = goodsVOList.get(0).getUpdateTime();
            goodsVOList = this.getSpuByTime(updateTime, now);
            bookListModelIdSet.addAll(this.getBookListModelId(goodsVOList));
        }
        return bookListModelIdSet;
    }


    @Override
    public Set<Long> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {

        return this.collectBookListModelId(lastCollectTime, now);
    }

    @Override
    public <F> List<F> collect(List<F> list) {
        List<String> spuIdList = new ArrayList<>();
        for (F f : list) {
            EsBookListModel esBookListModel = (EsBookListModel) f;
            if (CollectionUtils.isEmpty(esBookListModel.getSpus())) {
                break;
            }
            for (EsBookListSubSpuNew esBookListSubSpuNew : esBookListModel.getSpus()) {
                spuIdList.add(esBookListSubSpuNew.getSpuId());
            }
        }
        if (CollectionUtils.isEmpty(spuIdList)) {
            return list;
        }
        CollectSpuProviderReq req = new CollectSpuProviderReq();
        req.setSpuIds(spuIdList);
        List<GoodsVO> context = collectSpuProvider.collectSpuBySpuIds(req).getContext();
        if (CollectionUtils.isEmpty(context)) {
            return list;
        }
        Map<String, GoodsVO> spuId2GoodsVoMap = context.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
        for (F f : list) {
            EsBookListModel esBookListModel = (EsBookListModel) f;
            if (CollectionUtils.isEmpty(esBookListModel.getSpus())) {
                break;
            }
            for (EsBookListSubSpuNew esBookListSubSpuNew : esBookListModel.getSpus()) {
                GoodsVO goodsVO = spuId2GoodsVoMap.get(esBookListSubSpuNew.getSpuId());
                if (goodsVO == null) {
                    continue;
                }
                esBookListSubSpuNew.setSpuName(goodsVO.getGoodsName());
            }
        }
        return list;
    }
}
