package com.wanmi.sbc.goods.info.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByConditionResponse;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsFreightHistory;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsFreightHistoryRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.nacos.GoodsNacosConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description: 49包邮 业务信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/24 1:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class FreeDelivery49Service {




    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private GoodsNacosConfig goodsNacosConfig;

    @Autowired
    private GoodsFreightHistoryRepository goodsFreightHistoryRepository;


    /**
     * 更改49包邮 更改模版
     * @param spuIds
     */
    public void changeFreeDelivery49(List<String> spuIds) {
        log.info("FreeDelivery49Service changeFreeDelivery49 spuIds {}", JSON.toJSONString(spuIds));
        List<Goods> goodsList = goodsService.listByGoodsIds(spuIds);
        if (CollectionUtils.isEmpty(goodsList)) {
            return;
        }
//        Map<String, Goods> spuId2GoodsMap = new HashMap<>();
//        for (Goods goods : goodsList) {
//            spuId2GoodsMap.put(goods.getGoodsId(), goods);
//        }

        GoodsInfoQueryRequest param = new GoodsInfoQueryRequest();
        param.setGoodsIds(spuIds);
        param.setAuditStatus(CheckStatus.CHECKED);
        param.setDelFlag(DeleteFlag.NO.toValue());
        param.setAddedFlag(AddedFlag.YES.toValue());
        List<GoodsInfo> goodsInfoList = goodsInfoService.findByParams(param);
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            log.info("FreeDelivery49Service changeFreeDelivery49 goodsInfoList isEmpty spuIds {} ", JSON.toJSONString(goodsInfoList));
            return;
        }

        log.info("FreeDelivery49Service changeFreeDelivery49 goodsNacosConfig {} ", JSON.toJSONString(goodsNacosConfig));

        Map<String, Goods> directUpdateGoodsFreightTmpIdMap = new HashMap<>();
        Map<String, Goods> calcUpdateGoodsFreightTmpIdMap = new HashMap<>();

        for (Goods goods : goodsList) {
            if (CollectionUtils.isNotEmpty(goodsNacosConfig.getUnFreeDelivery49())
                    && goodsNacosConfig.getUnFreeDelivery49().contains(goods.getFreightTempId().toString())) {
                directUpdateGoodsFreightTmpIdMap.put(goods.getGoodsId(), goods);
            } else {
                calcUpdateGoodsFreightTmpIdMap.put(goods.getGoodsId(), goods);
            }
        }

        Map<String, Goods> updateSpuId2GoodsMap = new HashMap<>();
        List<GoodsFreightHistory> updateResetFreightGoodsIdList = new ArrayList<>();
        for (GoodsInfo goodsInfo : goodsInfoList) {
            //非计算的模版直接返回
            Goods goods = calcUpdateGoodsFreightTmpIdMap.get(goodsInfo.getGoodsId());
            if (goods == null) {
                continue;
            }
            if (updateSpuId2GoodsMap.get(goodsInfo.getGoodsId()) != null) {
                continue;
            }
            BigDecimal marketPrice = goodsInfo.getMarketPrice().multiply(new BigDecimal("0.96"));
            BigDecimal diffPrice = marketPrice.subtract(goodsInfo.getCostPrice());
            log.info("FreeDelivery49Service changeFreeDelivery49 skuId: {} marketPrice:{} 96MarketPrice:{} costPrice:{} diffPrice:{} ",
                    goodsInfo.getGoodsInfoId(), goodsInfo.getMarketPrice(), marketPrice, goodsInfo.getCostPrice(), diffPrice);
            if (diffPrice.compareTo(new BigDecimal("5")) > 0) {
                updateSpuId2GoodsMap.put(goodsInfo.getGoodsId(), goods);
            } else {
                //如果存在历史模版，则还原模版id
                GoodsFreightHistory  goodsFreightHistoryParam = new GoodsFreightHistory();
                goodsFreightHistoryParam.setGoodsId(goods.getGoodsId());
                goodsFreightHistoryParam.setDelFlag(DeleteFlag.NO.toValue());
                List<GoodsFreightHistory> goodsFreightHistories = goodsFreightHistoryRepository.findAll(Example.of(goodsFreightHistoryParam));
                if (!CollectionUtils.isEmpty(goodsFreightHistories)) {
                    GoodsFreightHistory goodsFreightHistory = goodsFreightHistories.get(0);
                    updateResetFreightGoodsIdList.add(goodsFreightHistory);
                }
            }
        }

        String freeDelivery49 = goodsNacosConfig.getFreeDelivery49();

        //更新为指定模版
        List<String> updateFreightGoodsIdList = new ArrayList<>();
        List<GoodsFreightHistory> goodsFreightHistoryList = new ArrayList<>();
        for (Map.Entry<String, Goods> stringGoodsEntry : updateSpuId2GoodsMap.entrySet()) {
            Goods goods = stringGoodsEntry.getValue();
            if (goods.getFreightTempId() == null) {
                continue;
            }

            //记录当前的模版id，
            if (!Objects.equals(freeDelivery49, goods.getFreightTempId().toString())) {
                GoodsFreightHistory  goodsFreightHistoryParam = new GoodsFreightHistory();
                goodsFreightHistoryParam.setGoodsId(goods.getGoodsId());
                goodsFreightHistoryParam.setDelFlag(DeleteFlag.NO.toValue());
                List<GoodsFreightHistory> goodsFreightHistories = goodsFreightHistoryRepository.findAll(Example.of(goodsFreightHistoryParam));
                GoodsFreightHistory goodsFreightHistory = null;
                if (!CollectionUtils.isEmpty(goodsFreightHistories)) {
                    goodsFreightHistory = goodsFreightHistories.get(0);
                }

                if (goodsFreightHistory == null) {
                    goodsFreightHistory = new GoodsFreightHistory();
                    goodsFreightHistory.setHisFreightId(goods.getFreightTempId().intValue());
                    goodsFreightHistory.setCreateTime(LocalDateTime.now());
                }
                goodsFreightHistory.setGoodsId(goods.getGoodsId());
                goodsFreightHistory.setDelFlag(DeleteFlag.NO.toValue());
                goodsFreightHistory.setUpdateTime(LocalDateTime.now());
                goodsFreightHistoryList.add(goodsFreightHistory);
            }
            //更新goods信息的模版id为指定的id
            updateFreightGoodsIdList.add(goods.getGoodsId());
        }

        log.info("FreeDelivery49Service changeFreeDelivery49 直接使用模版不做任何处理的商品列表是 {}", directUpdateGoodsFreightTmpIdMap.keySet());
        
        log.info("FreeDelivery49Service changeFreeDelivery49 save goodsFreightHistoryList {} ", JSON.toJSONString(goodsFreightHistoryList));
        if (!CollectionUtils.isEmpty(goodsFreightHistoryList)) {
            goodsFreightHistoryRepository.saveAll(goodsFreightHistoryList);
        }

        log.info("FreeDelivery49Service changeFreeDelivery49 save updateResetFreightGoodsIdList {} ", JSON.toJSONString(updateResetFreightGoodsIdList));
        if (!CollectionUtils.isEmpty(updateResetFreightGoodsIdList)) {
            for (GoodsFreightHistory goodsFreightHistory : updateResetFreightGoodsIdList) {
                if (directUpdateGoodsFreightTmpIdMap.get(goodsFreightHistory.getGoodsId()) != null) {
                    continue;
                }
                goodsService.updateFreight(goodsFreightHistory.getHisFreightId().longValue(), Collections.singletonList(goodsFreightHistory.getGoodsId()));
            }
        }

        log.info("FreeDelivery49Service changeFreeDelivery49 goodsId {} freightId {}", JSON.toJSONString(updateFreightGoodsIdList), freeDelivery49);
        if (!CollectionUtils.isEmpty(updateFreightGoodsIdList)) {
            goodsService.updateFreight(Long.valueOf(freeDelivery49), updateFreightGoodsIdList);
        }


    }

}
