package com.wanmi.sbc.goods.info.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
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
import com.wanmi.sbc.goods.nacos.GoodsNacosConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 49包邮 业务信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/24 1:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class FreeDelivery49Service {




    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsNacosConfig goodsNacosConfig;

    public void init() {

    }

    /**
     * 更改49包邮 更改模版
     * @param spuIds
     */
    public void changeFreeDelivery49(List<String> spuIds) {
        GoodsListByIdsRequest goodsListByIdsRequest = new GoodsListByIdsRequest();
        goodsListByIdsRequest.setGoodsIds(spuIds);
        GoodsListByIdsResponse context = goodsQueryProvider.listByIds(goodsListByIdsRequest).getContext();
        if (CollectionUtils.isEmpty(context.getGoodsVOList())) {
            return;
        }
        Map<String, GoodsVO> spuId2GoodsVoMap = new HashMap<>();
        for (GoodsVO goodsVO : context.getGoodsVOList()) {
            spuId2GoodsVoMap.put(goodsVO.getGoodsId(), goodsVO);
        }

        GoodsInfoListByConditionRequest request = new GoodsInfoListByConditionRequest();
        request.setGoodsIds(spuIds);
        request.setAuditStatus(CheckStatus.CHECKED);
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setAddedFlags(Collections.singletonList(AddedFlag.YES.toValue()));
        GoodsInfoListByConditionResponse goodsInfoContext = goodsInfoQueryProvider.listByCondition(request).getContext();
        if (CollectionUtils.isEmpty(goodsInfoContext.getGoodsInfos())) {
            return;
        }

        Map<String, GoodsVO> updateSpuId2GoodsVoMap = new HashMap<>();
        for (GoodsInfoVO goodsInfo : goodsInfoContext.getGoodsInfos()) {
            GoodsVO goodsVO = spuId2GoodsVoMap.get(goodsInfo.getGoodsId());
            if (goodsVO == null) {
                continue;
            }
            if (updateSpuId2GoodsVoMap.get(goodsInfo.getGoodsId()) != null) {
                continue;
            }
            BigDecimal marketPrice = goodsInfo.getMarketPrice().multiply(new BigDecimal("0.96"));
            BigDecimal diffPrice = marketPrice.subtract(goodsInfo.getCostPrice());
            if (diffPrice.compareTo(new BigDecimal("5")) > 0) {
                updateSpuId2GoodsVoMap.put(goodsInfo.getGoodsId(), goodsVO);
            } else {
                //如果为指定模版id，则还原模版id
            }
        }

        //更新为指定模版
        for (Map.Entry<String, GoodsVO> stringGoodsVOEntry : updateSpuId2GoodsVoMap.entrySet()) {
            GoodsVO goodsVO = stringGoodsVOEntry.getValue();
            if (CollectionUtils.isNotEmpty(goodsNacosConfig.getUnFreeDelivery49s()) && goodsNacosConfig.getFreeDelivery49().contains(goodsVO.getFreightTempId().toString())) {
                continue;
            }
            //记录当前的模版id，

            //更新goods信息的模版id为指定的id
        }
    }

}
