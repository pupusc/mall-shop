package com.wanmi.sbc.task;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.goodsPool.PoolFactory;
import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.redis.RedisListService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.bean.dto.*;
import com.wanmi.sbc.setting.bean.enums.MixedComponentLevel;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.RedisKeyUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@JobHandler(value = "mixedComponentContentJobHandler")
@Component
@EnableBinding
@Slf4j
public class MixedComponentContentJobHandler extends IJobHandler {

    @Autowired
    private TopicConfigProvider topicConfigProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisListService redisListService;


    @Autowired
    private PoolFactory poolFactory;



    @Override
    public ReturnT<String> execute(String param) throws Exception {
        int topicStoreyId=0;
        if(param != null && !param.equals("")){
            topicStoreyId = Integer.parseInt(param);
        }
        this.saveMixedComponentContent(topicStoreyId);
        return SUCCESS;
    }

    public void saveMixedComponentContent(int topicStoreyId) {
        try {
            //栏目信息
            MixedComponentTabQueryRequest request = new MixedComponentTabQueryRequest();
            request.setTopicStoreyId(topicStoreyId);
            request.setPublishState(0);
            request.setState(1);
            List<MixedComponentTabDto> mixedComponentTab = topicConfigProvider.listMixedComponentTab(request).getContext();
            //存redis
            //redisService.setString(RedisKeyUtil.MIXED_COMPONENT + "details", JSON.toJSONString(mixedComponentTab));
            // tab
            List<MixedComponentDto> mixedComponentDtos = mixedComponentTab.stream().filter(c -> MixedComponentLevel.ONE.toValue().equals(c.getLevel())).map(c -> {
                return new MixedComponentDto(c);
            }).collect(Collectors.toList());
            redisService.setString(RedisKeyUtil.MIXED_COMPONENT_TAB+topicStoreyId+":tab", JSON.toJSONString(mixedComponentDtos));
            for (MixedComponentDto mixedComponentDto : mixedComponentDtos) {
                Integer tabId = mixedComponentDto.getId();
                // 获取关键字
                List<KeyWordDto> keywords = mixedComponentTab.stream().filter(c -> MixedComponentLevel.TWO.toValue().equals(c.getLevel()) && tabId.equals(c.getPId()))
                        .map(c -> {
                            return new KeyWordDto(String.valueOf(c.getId()), c.getName());
                        }).collect(Collectors.toList());
                if(keywords == null || keywords.size() == 0) {
                    //获取商品池
                    List<MixedComponentTabDto> pools = mixedComponentTab.stream().filter(c -> MixedComponentLevel.THREE.toValue().equals(c.getLevel()) && String.valueOf(tabId).equals(String.valueOf(c.getPId()))).collect(Collectors.toList());
                    List<GoodsPoolDto> goodsPoolDtos = new ArrayList<>();
                    for (MixedComponentTabDto pool : pools) {
                        Integer id = pool.getId();
                        ColumnContentQueryRequest columnContentQueryRequest = new ColumnContentQueryRequest();
                        columnContentQueryRequest.setTopicStoreySearchId(id);
                        request.setPublishState(0);
                        request.setState(1);
                        List<ColumnContentDTO> columnContent = topicConfigProvider.ListTopicStoreyColumnContent(columnContentQueryRequest).getContext();
                        PoolService poolService = poolFactory.getPoolService(pool.getBookType());
                        poolService.getGoodsPool(goodsPoolDtos, columnContent, pool, null);
                    }
                    //排序
                    List<GoodsPoolDto> goodsPools = goodsPoolDtos.stream().sorted(Comparator.comparing(GoodsPoolDto::getSorting)
                                    .thenComparing(Comparator.comparing(GoodsPoolDto::getType).reversed()))
                            .collect(Collectors.toList());
                    //存redis
                    redisListService.putAll(RedisKeyUtil.MIXED_COMPONENT+topicStoreyId+":" + tabId + ":all", goodsPools);
                } else {
                    for (KeyWordDto keyword : keywords) {
                        String keyWordId = keyword.getId();
                        String keyWord = keyword.getName();
                        //获取商品池
                        List<MixedComponentTabDto> pools = mixedComponentTab.stream().filter(c -> MixedComponentLevel.THREE.toValue().equals(c.getLevel()) && keyWordId.equals(String.valueOf(c.getPId()))).collect(Collectors.toList());
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
                        //排序
                        List<GoodsPoolDto> goodsPools = goodsPoolDtos.stream().sorted(Comparator.comparing(GoodsPoolDto::getSorting)
                                        .thenComparing(Comparator.comparing(GoodsPoolDto::getType).reversed()))
                                .collect(Collectors.toList());
                        //存redis
                        redisListService.putAll(RedisKeyUtil.MIXED_COMPONENT+topicStoreyId+":" + tabId + ":" + keyWordId, goodsPools);
                    }
                    redisService.setString(RedisKeyUtil.MIXED_COMPONENT+topicStoreyId+":" + tabId + ":keywords", JSON.toJSONString(keywords));
                }
            }
        }catch (Exception e){
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(),DateUtil.FMT_TIME_1),
                    "saveMixedComponentContent",
                    Objects.isNull(topicStoreyId)?"":JSON.toJSONString(topicStoreyId),
                    e);
        }

    }
}
