package com.wanmi.sbc.collectFactory.service;


import com.wanmi.sbc.collectFactory.AbstractCollect;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyContentRequest;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyContentDTO;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyTypeV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GoodsOrBookCollect extends AbstractCollect {

    @Autowired
    private TopicConfigProvider topicConfigProvider;

    @Override
    public Set<String> collectId(Integer topicStoreyId,Integer storeyType) {
        if(storeyType== TopicStoreyTypeV2.Goods.getId()||storeyType==TopicStoreyTypeV2.Books.getId()) {
            TopicStoreyContentRequest topicStoreyContentRequest = new TopicStoreyContentRequest();
            //获得主题id
            topicStoreyContentRequest.setStoreyId(topicStoreyId);
            //获得主题下商品skuList
            List<TopicStoreyContentDTO> collectTemp = topicConfigProvider.getContentByStoreyId(topicStoreyContentRequest);
            Set<String> skuList = collectTemp.stream().map(t -> t.getSkuId()).collect(Collectors.toSet());
            return skuList;
        }
        return new HashSet<>();
    }
}
