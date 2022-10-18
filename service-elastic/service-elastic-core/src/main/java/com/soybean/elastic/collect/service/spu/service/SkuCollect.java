package com.soybean.elastic.collect.service.spu.service;
import com.google.common.collect.Lists;

import com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum;
import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.soybean.elastic.spu.model.sub.SubCommentNew;
import com.soybean.elastic.spu.model.sub.SubLabelNew;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectCommentRelSpuDetailResp;
import com.wanmi.sbc.goods.bean.vo.CollectSkuVO;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: 采集商品标签信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/19 1:29 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SkuCollect extends AbstractSpuCollect {

    @Autowired
    private CollectSpuProvider collectSpuProvider;

    /**
     * 根据时间获取信息
     * @param lastCollectTime
     * @param now
     * @param fromId
     * @return
     */
    private List<CollectSkuVO> getSkuByTime(LocalDateTime lastCollectTime, LocalDateTime now, Integer fromId) {
        CollectSpuProviderReq req = new CollectSpuProviderReq();
        req.setPageSize(MAX_PAGE_SIZE);
        req.setFromId(fromId);
        req.setBeginTime(lastCollectTime);
        req.setEndTime(now);
        return collectSpuProvider.collectSkuIdByTime(req).getContext();
    }


    @Override
    public Set<String> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        List<CollectSkuVO> collectSkuVOList = this.getSkuByTime(lastCollectTime, now, 0);
        Set<String> spuIdSet = collectSkuVOList.stream().map(CollectSkuVO::getGoodsId).collect(Collectors.toSet());
        while (collectSkuVOList.size() >= MAX_PAGE_SIZE) {
            Integer tmpId = collectSkuVOList.get(collectSkuVOList.size() -1).getTmpId();
            collectSkuVOList = this.getSkuByTime(lastCollectTime, now, tmpId);
            spuIdSet.addAll(collectSkuVOList.stream().map(CollectSkuVO::getGoodsId).collect(Collectors.toSet()));
        }
        return spuIdSet;
    }


    @Override
    public <F> List<F> collect(List<F> list) {
        List<String> spuIdList = list.stream().map(t -> ((EsSpuNew) t).getSpuId()).collect(Collectors.toList());
        CollectSpuProviderReq req = new CollectSpuProviderReq();
        req.setSpuIds(spuIdList);
        List<CollectSkuVO> context = collectSpuProvider.collectSkuBySpuIds(req).getContext();
        if (CollectionUtils.isEmpty(context)) {
            return list;
        }

        Map<String, SearchSpuNewLabelCategoryEnum> spuId2Model49FreeDeliveryMap = new HashMap<>();
        for (CollectSkuVO skuVO : context) {
            if (spuId2Model49FreeDeliveryMap.get(skuVO.getGoodsId()) != null) {
                continue;
            }
            BigDecimal diffPrice = skuVO.getMarketPrice().subtract(skuVO.getCostPrice());
            if (diffPrice.compareTo(new BigDecimal(SearchSpuNewLabelCategoryEnum.FREE_DELIVERY.getThreshold().toString())) > 0) {
                spuId2Model49FreeDeliveryMap.put(skuVO.getGoodsId(), SearchSpuNewLabelCategoryEnum.FREE_DELIVERY);
            }
        }

        for (F f : list) {
            EsSpuNew esSpuNew = (EsSpuNew) f;
            SearchSpuNewLabelCategoryEnum spuNewLabelCategoryEnum  = spuId2Model49FreeDeliveryMap.get(esSpuNew.getSpuId());
            if (spuNewLabelCategoryEnum == null) {
                continue;
            }
            List<SubLabelNew> labels = esSpuNew.getLabels();
            if (CollectionUtils.isEmpty(labels)) {
                labels = new ArrayList<>();
            }
            SubLabelNew subLabelNew = new SubLabelNew();
            subLabelNew.setName(spuNewLabelCategoryEnum.getMessage());
            subLabelNew.setCategory(spuNewLabelCategoryEnum.getCode());
            labels.add(subLabelNew);
            esSpuNew.setLabels(labels);
        }
        return list;
    }
}
