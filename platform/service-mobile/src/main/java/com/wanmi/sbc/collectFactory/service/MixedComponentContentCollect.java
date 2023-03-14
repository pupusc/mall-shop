package com.wanmi.sbc.collectFactory.service;


import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.collectFactory.AbstractCollect;
import com.wanmi.sbc.goodsPool.PoolFactory;
import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.redis.RedisListService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.ColumnContentQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.MixedComponentTabQueryRequest;
import com.wanmi.sbc.setting.bean.dto.*;
import com.wanmi.sbc.setting.bean.enums.MixedComponentLevel;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyTypeV2;
import com.wanmi.sbc.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MixedComponentContentCollect extends AbstractCollect {

    @Autowired
    private TopicConfigProvider topicConfigProvider;
    @Autowired
    private RedisService redisService;

    @Autowired
    private PoolFactory poolFactory;

    @Override
    public Set<String> collectId(Integer topicStoreyId,Integer storeyType) {
        if(storeyType== TopicStoreyTypeV2.MIXED.getId()) {
            MixedComponentTabQueryRequest request = new MixedComponentTabQueryRequest();
            request.setTopicStoreyId(topicStoreyId);
            request.setPublishState(0);
            request.setState(1);
            List<MixedComponentTabDto> mixedComponentTab = topicConfigProvider.listMixedComponentTab(request).getContext();
            // tab
            List<MixedComponentDto> mixedComponentDtos = mixedComponentTab.stream().filter(c -> MixedComponentLevel.ONE.toValue().equals(c.getLevel())).map(c -> {
                return new MixedComponentDto(c);
            }).collect(Collectors.toList());
            Set<String> idSet = new HashSet<>();
            for (MixedComponentDto mixedComponentDto : mixedComponentDtos) {
                Integer tabId = mixedComponentDto.getId();
                // 获取关键字
                List<KeyWordDto> keywords = new ArrayList<>();
                mixedComponentTab.stream().filter(c -> MixedComponentLevel.TWO.toValue().equals(c.getLevel()) && tabId.equals(c.getPId()))
                        .map(c -> {
                            return c.getKeywords();
                        }).collect(Collectors.toList())
                        .forEach(c -> {
                            c.forEach(s -> {
                                keywords.add(new KeyWordDto(s.getId(), s.getName()));
                            });
                        });
                // 获取规则
                List<String> rules = new ArrayList<>();
                mixedComponentTab.stream().filter(c -> MixedComponentLevel.THREE.toValue().equals(c.getLevel()) && tabId.equals(c.getPId()))
                        .map(c -> {
                            return c.getKeywords();
                        }).collect(Collectors.toList())
                        .forEach(c -> {
                            c.forEach(s -> {
                                rules.add(s.getName());
                            });
                        });
                //获取商品池
                List<MixedComponentTabDto> pools = mixedComponentTab.stream().filter(c -> MixedComponentLevel.FOUR.toValue().equals(c.getLevel()) && rules.contains(c.getDropName())).collect(Collectors.toList());
                for (KeyWordDto keyword : keywords) {
                    String keyWordId = keyword.getId();
                    String keyWord = keyword.getName();
                    List<GoodsPoolDto> goodsPoolDtos = new ArrayList<>();
                    for (MixedComponentTabDto pool : pools) {
                        Integer id = pool.getId();
                        ColumnContentQueryRequest columnContentQueryRequest = new ColumnContentQueryRequest();
                        columnContentQueryRequest.setTopicStoreySearchId(id);
                        request.setPublishState(0);
                        request.setState(1);
                        List<ColumnContentDTO> columnContent = topicConfigProvider.ListTopicStoreyColumnContent(columnContentQueryRequest).getContext();
                        PoolService poolService = poolFactory.getPoolService(pool.getBookType());
                        poolService.getGoodsPool(goodsPoolDtos, columnContent, pool, keyWord);
                    }
                    goodsPoolDtos.forEach(g -> {
                        g.getGoods().forEach(item -> {
                            idSet.add(item.getSkuId());
                        });
                    });
                }
            }
            return idSet;
        }
        return new HashSet<>();
    }
}
