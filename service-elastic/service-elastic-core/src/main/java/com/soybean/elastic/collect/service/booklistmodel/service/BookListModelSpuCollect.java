package com.soybean.elastic.collect.service.booklistmodel.service;

import com.soybean.elastic.booklistmodel.model.EsBookListModel;
import com.soybean.elastic.booklistmodel.model.sub.EsBookListSubSpuNew;
import com.soybean.elastic.collect.service.booklistmodel.AbstractBookListModelCollect;
import com.wanmi.sbc.goods.api.provider.collect.CollectBookListModelProvider;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectBookListModelProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuImageProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectBookListGoodsPublishResponse;
import com.wanmi.sbc.goods.api.response.collect.CollectSpuImageResp;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
     * 采集商品图片信息
     * @param beginTime
     * @param endTime
     * @param fromId
     * @return
     */
    private List<CollectSpuImageResp> collectSpuImageRespsByTime (LocalDateTime beginTime, LocalDateTime endTime, Integer fromId) {
        CollectSpuImageProviderReq collectSpuImageProviderReq = new CollectSpuImageProviderReq();
        collectSpuImageProviderReq.setPageSize(MAX_PAGE_SIZE);
        collectSpuImageProviderReq.setFromId(fromId);
        collectSpuImageProviderReq.setBeginTime(beginTime);
        collectSpuImageProviderReq.setEndTime(endTime);
        return collectSpuProvider.collectSpuIdImageByTime(collectSpuImageProviderReq).getContext();
    }

    /**
     * 获取商品对象信息
     * @param lastCollectTime
     * @param now
     * @return
     */
    private List<CollectSpuVO> getSpuByTime(LocalDateTime lastCollectTime, LocalDateTime now, Integer fromId) {
        CollectSpuProviderReq request = new CollectSpuProviderReq();
        request.setBeginTime(lastCollectTime);
        request.setEndTime(now);
        request.setPageSize(MAX_PAGE_SIZE);
        request.setFromId(fromId);
        return collectSpuProvider.collectSpuIdByTime(request).getContext();
    }

    /**
     * 获取书单id列表信息
     * @param collectSpuIdList
     * @return
     */
    private Set<Long> getBookListModelId(List<String> collectSpuIdList) {
        Set<Long> bookListModelIdSet = new HashSet<>();
        if (CollectionUtils.isEmpty(collectSpuIdList)) {
            return bookListModelIdSet;
        }
//        List<String> goodsIdList = collectSpuVOList.stream().map(CollectSpuVO::getGoodsId).collect(Collectors.toList());
        //根据商品id 获取对应的书单
        CollectBookListModelProviderReq req = new CollectBookListModelProviderReq();
        req.setSpuIds(collectSpuIdList);
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
        List<CollectSpuVO> collectSpuVOList = this.getSpuByTime(lastCollectTime, now, 0);
        Set<Long> bookListModelIdSet = this.getBookListModelId(collectSpuVOList.stream().map(CollectSpuVO::getGoodsId).collect(Collectors.toList()));
        while (collectSpuVOList.size() >= MAX_PAGE_SIZE) {
            Integer tmpId = collectSpuVOList.get(collectSpuVOList.size() - 1).getTmpId();
            collectSpuVOList = this.getSpuByTime(lastCollectTime, now, tmpId);
            bookListModelIdSet.addAll(this.getBookListModelId(collectSpuVOList.stream().map(CollectSpuVO::getGoodsId).collect(Collectors.toList())));
        }

        //图片对应书单更新
        List<CollectSpuImageResp> collectSpuImageList = this.collectSpuImageRespsByTime(lastCollectTime, now, 0);
        bookListModelIdSet.addAll(this.getBookListModelId(collectSpuImageList.stream().map(CollectSpuImageResp::getSpuId).collect(Collectors.toList())));
        while (collectSpuImageList.size() >= MAX_PAGE_SIZE) {
            Integer tmpId = collectSpuImageList.get(collectSpuImageList.size() - 1).getTmpId();
            collectSpuImageList = this.collectSpuImageRespsByTime(lastCollectTime, now, tmpId);
            bookListModelIdSet.addAll(this.getBookListModelId(collectSpuVOList.stream().map(CollectSpuVO::getGoodsId).collect(Collectors.toList())));
        }
        return bookListModelIdSet;
    }


    @Override
    public Set<Long> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {

        return this.collectBookListModelId(lastCollectTime, now);
    }

    @Override
    public <F> List<F> collect(List<F> list) {

        for (F f : list) {
            EsBookListModel esBookListModel = (EsBookListModel) f;
            if (CollectionUtils.isEmpty(esBookListModel.getSpus())) {
                break;
            }

            List<String> spuIdList = new ArrayList<>();
            for (EsBookListSubSpuNew esBookListSubSpuNew : esBookListModel.getSpus()) {
                spuIdList.add(esBookListSubSpuNew.getSpuId());
            }

            Map<String, CollectSpuVO> spuId2CollectSpuVoMap = new HashMap<>();
            int cycleCount = spuIdList.size() / MAX_PAGE_SIZE;
            int remainder = spuIdList.size() % MAX_PAGE_SIZE;
            cycleCount += remainder > 0 ? 1 : 0;

            for (int i = 0; i<cycleCount; i++) {
                spuId2CollectSpuVoMap.putAll(this.mapGoodsVo(spuIdList.subList(i * MAX_PAGE_SIZE, Math.min((i + 1) * MAX_PAGE_SIZE, spuIdList.size()))));
            }

            if (spuId2CollectSpuVoMap.size() > 0) {
                for (EsBookListSubSpuNew esBookListSubSpuNew : esBookListModel.getSpus()) {
                    CollectSpuVO collectSpuVO = spuId2CollectSpuVoMap.get(esBookListSubSpuNew.getSpuId());
                    if (collectSpuVO == null) {
                        continue;
                    }
                    esBookListSubSpuNew.setSpuName(collectSpuVO.getGoodsName());
                    esBookListSubSpuNew.setChannelTypes(collectSpuVO.getGoodsChannelTypes());
                    esBookListSubSpuNew.setUnBackgroundPic(collectSpuVO.getGoodsUnBackImg());
                    esBookListSubSpuNew.setPic(collectSpuVO.getMinSalePriceImg());
                }
//                spuIdSet.clear(); //辅助GC
//                spuId2GoodsVoMap.clear(); //辅助GC
            }
        }
        return list;
    }


    private Map<String, CollectSpuVO> mapGoodsVo(List<String> spuIdList) {
        CollectSpuProviderReq req = new CollectSpuProviderReq();
        req.setSpuIds(spuIdList);
        List<CollectSpuVO> context = collectSpuProvider.collectSpuBySpuIds(req).getContext();
        if (CollectionUtils.isEmpty(context)) {
            return new HashMap<>();
        }
        return context.stream().collect(Collectors.toMap(CollectSpuVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
    }
}
