package com.soybean.elastic.collect.service.spu.service;
import com.alibaba.fastjson.JSON;
import com.soybean.elastic.api.enums.SearchSpuNewCategoryEnum;
import com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum;
import com.soybean.elastic.spu.model.sub.SubAnchorRecomNew;

import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.soybean.elastic.spu.model.sub.SubBookNew;
import com.soybean.elastic.spu.model.sub.SubLabelNew;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.enums.AnchorPushEnum;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuPropProvider;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuProvider;
import com.wanmi.sbc.goods.api.provider.nacos.GoodsNacosConfigProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuPropProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectSpuPropResp;
import com.wanmi.sbc.goods.api.response.nacos.GoodsNacosConfigResp;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 采集商品信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 2:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class SpuCollect extends AbstractSpuCollect {

    @Autowired
    private CollectSpuProvider collectSpuProvider;

//    @Autowired
//    private CollectSpuPropProvider collectSpuPropProvider;
    @Autowired
    private GoodsNacosConfigProvider goodsNacosConfigProvider;

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
        request.setFromId(fromId);
        request.setPageSize(MAX_PAGE_SIZE);
        return collectSpuProvider.collectSpuIdByTime(request).getContext();
    }


    /**
     * 获取商品对象信息
     * @param lastCollectTime
     * @param now
     * @return
     */
    private List<CollectSpuPropResp> getSpuPropByTime(LocalDateTime lastCollectTime, LocalDateTime now, Integer fromId) {
        CollectSpuPropProviderReq request = new CollectSpuPropProviderReq();
        request.setBeginTime(lastCollectTime);
        request.setEndTime(now);
        request.setFromId(fromId);
        request.setPageSize(MAX_PAGE_SIZE);
        return collectSpuPropProvider.collectSpuIdPropByTime(request).getContext();
    }

    @Override
    public Set<String> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        List<CollectSpuVO> collectSpuVOList = this.getSpuByTime(lastCollectTime, now, 0);
        Set<String> spuIdSet = collectSpuVOList.stream().map(CollectSpuVO::getGoodsId).collect(Collectors.toSet());
        while (collectSpuVOList.size() >= MAX_PAGE_SIZE) {
            Integer tmpId = collectSpuVOList.get(collectSpuVOList.size() -1).getTmpId();
            collectSpuVOList = this.getSpuByTime(lastCollectTime, now, tmpId);
            spuIdSet.addAll(collectSpuVOList.stream().map(CollectSpuVO::getGoodsId).collect(Collectors.toSet()));
        }

        //获取商品属性信息
        List<CollectSpuPropResp> collectSpuPropRespList = this.getSpuPropByTime(lastCollectTime, now, 0);
        spuIdSet.addAll(collectSpuPropRespList.stream().map(CollectSpuPropResp::getSpuId).collect(Collectors.toSet()));
        while (collectSpuPropRespList.size() >= MAX_PAGE_SIZE) {
            Integer tmpId = collectSpuPropRespList.get(collectSpuPropRespList.size() -1).getTmpId();
            collectSpuPropRespList = this.getSpuPropByTime(lastCollectTime, now, tmpId);
            spuIdSet.addAll(collectSpuPropRespList.stream().map(CollectSpuPropResp::getSpuId).collect(Collectors.toSet()));
        }
        return spuIdSet;
    }



    @Override
    public <T> List<T> collect(List<T> list) {
        List<String> spuIdList = list.stream().map(t -> ((EsSpuNew) t).getSpuId()).collect(Collectors.toList());
        CollectSpuProviderReq req = new CollectSpuProviderReq();
        req.setSpuIds(spuIdList);
        List<CollectSpuVO> context = collectSpuProvider.collectSpuBySpuIds(req).getContext();
        if (CollectionUtils.isEmpty(context)) {
//            整体作废
            for (T t : list) {
                EsSpuNew esSpuNew = (EsSpuNew) t;
                //商品直接作废
                esSpuNew.setDelFlag(DeleteFlag.YES.toValue());
                esSpuNew.setAddedFlag(AddedFlag.NO.toValue());
                esSpuNew.setAuditStatus(Integer.parseInt(AuditStatus.NOT_PASS.toValue()));
            }
            return list;
        }
        //获取49包邮配置信息
        GoodsNacosConfigResp goodsNacosConfigResp = goodsNacosConfigProvider.getNacosConfig().getContext();
        log.info("SpuCollect collect goodsNacosConfigResp {}", JSON.toJSONString(goodsNacosConfigResp));

        Map<String, CollectSpuVO> spuId2CollectGoodsVoMap =
                context.stream().collect(Collectors.toMap(CollectSpuVO::getGoodsId, Function.identity(), (k1, k2) -> k1));

        CollectSpuPropProviderReq collectSpuPropProviderReq = new CollectSpuPropProviderReq();
        collectSpuPropProviderReq.setSpuIds(spuIdList);
        List<CollectSpuPropResp> collectSpuPropRespList = collectSpuPropProvider.collectSpuPropBySpuIds(collectSpuPropProviderReq).getContext();

        Map<String, CollectSpuPropResp> spuId2CollectSpuPropMap =
                collectSpuPropRespList.stream().collect(Collectors.toMap(CollectSpuPropResp::getSpuId, Function.identity(), (k1, k2) -> k1));
        for (T t : list) {
            EsSpuNew esSpuNew = (EsSpuNew) t;
            CollectSpuVO collectSpuVO = spuId2CollectGoodsVoMap.get(esSpuNew.getSpuId());
            if (collectSpuVO == null) {
                //商品直接作废
                esSpuNew.setDelFlag(DeleteFlag.YES.toValue());
                esSpuNew.setAddedFlag(AddedFlag.NO.toValue());
                esSpuNew.setAuditStatus(Integer.parseInt(AuditStatus.NOT_PASS.toValue()));
                continue;
            }

            esSpuNew.setSpuId(collectSpuVO.getGoodsId());
            esSpuNew.setSpuName(collectSpuVO.getGoodsName());
            esSpuNew.setSpuSubName(collectSpuVO.getGoodsSubtitle());
            esSpuNew.setSpuCategory(SearchSpuNewCategoryEnum.SPU.getCode());
            esSpuNew.setChannelTypes(collectSpuVO.getGoodsChannelTypes());
            esSpuNew.setUnBackgroundPic(collectSpuVO.getGoodsUnBackImg());
            esSpuNew.setPic(collectSpuVO.getMinSalePriceImg());
            if (!CollectionUtils.isEmpty(collectSpuVO.getAnchorPushs())) {
                List<SubAnchorRecomNew> subAnchorRecomNewList = new ArrayList<>();
                for (String s : collectSpuVO.getAnchorPushs()) {
                    AnchorPushEnum anchorPushEnum = AnchorPushEnum.getByCode(s);
                    if (anchorPushEnum == null) {
                        continue;
                    }
                    SubAnchorRecomNew subAnchorRecomNew = new SubAnchorRecomNew();
                    subAnchorRecomNew.setRecomId(Integer.valueOf(anchorPushEnum.getCode()));
                    subAnchorRecomNew.setRecomName(anchorPushEnum.getMessage());
                    subAnchorRecomNewList.add(subAnchorRecomNew);
                }
                esSpuNew.setAnchorRecoms(subAnchorRecomNewList);
            }

            //49包邮标签信息
            log.info("SpuCollect collect spuId:{} freightTempId is {}", collectSpuVO.getGoodsId(), collectSpuVO.getFreightTempId());
            if (goodsNacosConfigResp != null && collectSpuVO.getFreightTempId() != null && Objects.equals(collectSpuVO.getFreightTempId().toString(), goodsNacosConfigResp.getFreeDelivery49())) {
                SearchSpuNewLabelCategoryEnum freeDelivery49 = SearchSpuNewLabelCategoryEnum.FREE_DELIVERY_49;
                List<SubLabelNew> labels = esSpuNew.getLabels();
                if (CollectionUtils.isEmpty(labels)) {
                    labels = new ArrayList<>();
                }
                SubLabelNew subLabelNew = new SubLabelNew();
                subLabelNew.setLabelName(freeDelivery49.getMessage());
                subLabelNew.setCategory(freeDelivery49.getCode());
                labels.add(subLabelNew);
                esSpuNew.setLabels(labels);
            }


            esSpuNew.setSalesNum(collectSpuVO.getGoodsSalesNum());
            esSpuNew.setSalesPrice(collectSpuVO.getMiniSalesPrice());
            esSpuNew.setCpsSpecial(collectSpuVO.getCpsSpecial());
            esSpuNew.setAddedFlag(collectSpuVO.getAddedFlag());
            esSpuNew.setAddedTime(collectSpuVO.getAddedTime());
            esSpuNew.setCreateTime(collectSpuVO.getCreateTime());
            esSpuNew.setDelFlag(collectSpuVO.getDelFlag().toValue());
            esSpuNew.setAuditStatus(collectSpuVO.getAuditStatus().toValue());

            //book信息
            CollectSpuPropResp collectSpuPropResp = spuId2CollectSpuPropMap.get(esSpuNew.getSpuId());
            if (collectSpuPropResp != null) {
                esSpuNew.setSpuCategory(SearchSpuNewCategoryEnum.BOOK.getCode());
                SubBookNew subBookNew = new SubBookNew();
                subBookNew.setIsbn(collectSpuPropResp.getIsbn());
//                subBookNew.setFixPrice(collectSpuPropResp.getFixPrice().doubleValue());
                subBookNew.setScore(collectSpuPropResp.getScore());
                esSpuNew.setBook(subBookNew);
            }
        }
        return list;
    }
}
