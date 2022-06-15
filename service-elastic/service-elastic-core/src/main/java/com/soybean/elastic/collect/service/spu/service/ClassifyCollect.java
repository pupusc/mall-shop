package com.soybean.elastic.collect.service.spu.service;
import com.google.common.collect.Lists;

import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.soybean.elastic.spu.model.sub.SubClassifyNew;
import com.wanmi.sbc.goods.api.provider.collect.CollectClassifyProvider;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectClassifyProviderReq;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.goods.api.response.collect.CollectClassifyRelSpuDetailResp;
import com.wanmi.sbc.goods.api.response.collect.CollectClassifyRelSpuResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
 * Description: 采集店铺分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/13 3:30 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class ClassifyCollect extends AbstractSpuCollect {

    @Autowired
    private CollectClassifyProvider collectClassifyProvider;

    /**
     * 采集商品id信息
     * @param beginTime
     * @param endTime
     * @return
     */
    private List<CollectClassifyRelSpuResp> collectSpuId(LocalDateTime beginTime, LocalDateTime endTime, Integer fromId) {
        CollectClassifyProviderReq req = new CollectClassifyProviderReq();
        req.setFromId(fromId);
        req.setPageSize(MAX_PAGE_SIZE);
        req.setBeginTime(beginTime);
        req.setEndTime(endTime);
        return collectClassifyProvider.collectClassifySpuIdByTime(req).getContext();
    }

    /**
     * 采集商品id信息
     * @param beginTime
     * @param endTime
     * @return
     */
    private List<CollectClassifyRelSpuResp> collectSpuIdRel(LocalDateTime beginTime, LocalDateTime endTime, Integer fromId) {
        CollectClassifyProviderReq req = new CollectClassifyProviderReq();
        req.setFromId(fromId);
        req.setPageSize(MAX_PAGE_SIZE);
        req.setBeginTime(beginTime);
        req.setEndTime(endTime);
        return collectClassifyProvider.collectClassifySpuIdRelByTime(req).getContext();
    }

    @Override
    public Set<String> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        Set<String> spuIdSet = new HashSet<>();
        List<CollectClassifyRelSpuResp> collectClassifyRelSpuRespList = this.collectSpuId(lastCollectTime, now, 0);
        for (CollectClassifyRelSpuResp collectClassifyRelSpuRespParam : collectClassifyRelSpuRespList) {
            spuIdSet.add(collectClassifyRelSpuRespParam.getSpuId());
        }
        while (collectClassifyRelSpuRespList.size() >= MAX_PAGE_SIZE) {
            Integer maxClassifyId = collectClassifyRelSpuRespList.get(collectClassifyRelSpuRespList.size() - 1).getClassifyId();
            collectClassifyRelSpuRespList = this.collectSpuId(lastCollectTime, now, maxClassifyId);
            for (CollectClassifyRelSpuResp collectClassifyRelSpuRespParam : collectClassifyRelSpuRespList) {
                spuIdSet.add(collectClassifyRelSpuRespParam.getSpuId());
            }
        }

        //此处重复率比较高，所以再获取关系表里面的更新数据，减少查询次数
        List<CollectClassifyRelSpuResp> collectClassifyRelSpuByRelRespList = this.collectSpuIdRel(lastCollectTime, now, 0);
        for (CollectClassifyRelSpuResp collectClassifyRelSpuResp : collectClassifyRelSpuByRelRespList) {
            spuIdSet.add(collectClassifyRelSpuResp.getSpuId());
        }

        while (collectClassifyRelSpuByRelRespList.size() >= MAX_PAGE_SIZE) {
            Integer maxClassifySpuRelId = collectClassifyRelSpuByRelRespList.get(collectClassifyRelSpuByRelRespList.size() - 1).getClassifySpuRelId();
            collectClassifyRelSpuByRelRespList = this.collectSpuIdRel(lastCollectTime, now, maxClassifySpuRelId);
            for (CollectClassifyRelSpuResp collectClassifyRelSpuResp : collectClassifyRelSpuByRelRespList) {
                spuIdSet.add(collectClassifyRelSpuResp.getSpuId());
            }
        }

        return spuIdSet;
    }

    @Override
    public <F> List<F> collect(List<F> list) {
        List<String> spuIdList = list.stream().map(t -> ((EsSpuNew) t).getSpuId()).collect(Collectors.toList());
        //获取classify信息
        CollectClassifyProviderReq req = new CollectClassifyProviderReq();
        req.setSpuIds(spuIdList);
        List<CollectClassifyRelSpuDetailResp> context = collectClassifyProvider.collectClassifyBySpuIds(req).getContext();
        if (CollectionUtils.isEmpty(context)) {
            return list;
        }
        Map<String, CollectClassifyRelSpuDetailResp> spuId2CollectClassifyRelSpuMap =
                context.stream().collect(Collectors.toMap(CollectClassifyRelSpuDetailResp::getSpuId, Function.identity(), (k1, k2) -> k1));
        for (F f : list) {
            EsSpuNew esSpuNew = (EsSpuNew) f;
            CollectClassifyRelSpuDetailResp collectClassifyRelSpuDetailResp = spuId2CollectClassifyRelSpuMap.get(esSpuNew.getSpuId());
            if (collectClassifyRelSpuDetailResp == null) {
                continue;
            }
            SubClassifyNew subClassifyNew = new SubClassifyNew();
            subClassifyNew.setFClassifyId(collectClassifyRelSpuDetailResp.getFClassifyId());
            subClassifyNew.setFClassifyName(collectClassifyRelSpuDetailResp.getFClasssifyName());
            subClassifyNew.setClassifyId(collectClassifyRelSpuDetailResp.getClassifyId());
            subClassifyNew.setClassifyName(collectClassifyRelSpuDetailResp.getClassifyName());
            esSpuNew.setClassify(subClassifyNew);
        }
        return list;
    }
}
