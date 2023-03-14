package com.wanmi.sbc.collectFactory;

import com.alibaba.fastjson.JSONArray;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.index.RefreshConfig;
import com.wanmi.sbc.index.V2tabConfigResponse;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyDTO;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyTypeV2;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CollectSkuIdFactory {

    @Autowired
    private Map<String,AbstractCollect> collectMap;

    @Autowired
    private RefreshConfig refreshConfig;

    @Autowired
    private TopicConfigProvider topicConfigProvider;

    public Set<String> collectId(){
        List<V2tabConfigResponse> list = JSONArray.parseArray(refreshConfig.getV2tabConfig(), V2tabConfigResponse.class);
        Set<String> idSet=new HashSet<>();
        if(list != null && list.size() > 0){
            V2tabConfigResponse response = list.get(0);
            String topicKey = response.getParamsId();

            TopicQueryRequest request = new TopicQueryRequest();
            request.setTopicKey(topicKey);

            BaseResponse<TopicActivityVO> activityVO =  topicConfigProvider.detail(request);
            List<TopicStoreyDTO> tpList = activityVO.getContext().getStoreyList();

            for(int i=0;i<tpList.size();i++){
                for (String key : collectMap.keySet()) {
                    TopicStoreyDTO storeyDTO = tpList.get(i);

                    int topic_store_id = storeyDTO.getId();
                    int storeyType = storeyDTO.getStoreyType();
                    AbstractCollect idCollect = collectMap.get(key);
                    Set<String> skuIdTmpSet = idCollect.collectId(topic_store_id,storeyType);
                    idSet.addAll(skuIdTmpSet);
                }

            }

        }
        return idSet;
    }
}
