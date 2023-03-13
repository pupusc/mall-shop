package com.wanmi.sbc.collectFactory.service;


import com.alibaba.fastjson.JSONObject;
import com.soybean.marketing.api.resp.NormalActivitySkuResp;
import com.wanmi.sbc.collectFactory.AbstractCollect;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsQueryProvider;
import com.wanmi.sbc.goods.api.response.index.NormalModuleSkuResp;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyTypeV2;
import com.wanmi.sbc.topic.response.NewBookPointResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NewBookPointCollect extends AbstractCollect {

    @Autowired
    private PointsGoodsQueryProvider pointsGoodsQueryProvider;

    @Override
    public Set<String> collectId(Integer topicStoreyId,Integer storeyType) {
        if(storeyType==(TopicStoreyTypeV2.NEWBOOK.getId())) {
            List<NormalModuleSkuResp> context = pointsGoodsQueryProvider.getReturnPointGoods(new BaseQueryRequest()).getContext();
            //获取商品积分
            Set<String> skuIdList = new HashSet<>();
            context.stream().forEach(normalModuleSkuResp -> {
                NewBookPointResponse newBookPointResponse = new NewBookPointResponse();
                BeanUtils.copyProperties(normalModuleSkuResp, newBookPointResponse);
                skuIdList.add(newBookPointResponse.getSkuId());
            });
            return skuIdList;
        }
        return new HashSet<>();
    }
}
