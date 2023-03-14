package com.wanmi.sbc.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
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

    @Autowired
    private TopicConfigProvider columnRepository;


    @Override
    public ReturnT<String> execute(String param) throws Exception {
        int topicStoreyId = 194;
        if(param != null && !param.equals("")){
            topicStoreyId = Integer.parseInt(param);
        }
        this.saveMixedComponentContent(topicStoreyId);
        return SUCCESS;
    }

    public void saveMixedComponentContent(int topicStoreyId) {
        //栏目信息
        TopicStoreyColumnQueryRequest request = new TopicStoreyColumnQueryRequest();
        request.setTopicStoreyId(topicStoreyId);
        request.setPublishState(0);
        request.setState(1);
        request.setParentId(0);
        List<TopicStoreyColumnDTO> list = columnRepository.listStoryColumnAll(request);
        //获取tab
        List<TopicStoreyColumnDTO> tabLists = new ArrayList<>();
        for(TopicStoreyColumnDTO dto:list){
            TopicStoreyColumnQueryRequest param = new TopicStoreyColumnQueryRequest();
            param.setTopicStoreyId(topicStoreyId);
            param.setParentId(dto.getId());
            List<TopicStoreyColumnDTO> tabList = columnRepository.listStoryColumnAll(param);
            if(!tabList.isEmpty()){
                for(TopicStoreyColumnDTO tab:tabList){
                    tabLists.add(tab);
                }
                dto.setChilidList(tabList);
            }
        }
        //存放关键词
        redisService.setString(RedisKeyUtil.MIXED_COMPONENT_TAB+"&test", JSON.toJSONString(list));
        //循环关键词取出对应的商品池
        List<TopicStoreyColumnDTO> pools = new ArrayList<>();
        for(TopicStoreyColumnDTO tab:tabLists){
            TopicStoreyColumnQueryRequest param = new TopicStoreyColumnQueryRequest();
            param.setTopicStoreyId(topicStoreyId);
            param.setParentId(tab.getId());
            List<TopicStoreyColumnDTO> poolList = columnRepository.listStoryColumnAll(param);
            if(!poolList.isEmpty()){
                for(TopicStoreyColumnDTO pool:poolList){
                    TopicStoreyColumnGoodsQueryRequest params = new TopicStoreyColumnGoodsQueryRequest();
                    params.setTopicStoreySearchId(pool.getId());
                    params.setTopicStoreyId(topicStoreyId);
                    List<TopicStoreyColumnGoodsDTO> goodsPools = columnRepository.listStoryColumnGoodsId(params);
                    //List<TopicStoreyColumnDTO> goodsPools = columnRepository.listStoryColumnAll(param);
                    //存放商品
                    redisListService.putAll(RedisKeyUtil.MIXED_COMPONENT+ tab.getId() + ":" + pool.getId()+"&test", goodsPools);
                    pools.add(pool);
                }
                tab.setChilidList(poolList);
            }
            //存放商品池
            redisService.setString(RedisKeyUtil.MIXED_COMPONENT + tab.getId() + ":keywords"+"&test", JSON.toJSONString(tab));
        }
    }
}
