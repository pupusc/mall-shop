package com.wanmi.sbc.task;


import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.GoodsIdsByRankListIdsRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.RankGoodsPublishResponse;
import com.wanmi.sbc.goods.bean.dto.MarketingLabelNewDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goodsPool.PoolFactory;
import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.redis.RedisListService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.RankRedisListResponse;
import com.wanmi.sbc.setting.api.request.RankRequest;
import com.wanmi.sbc.setting.api.request.RankStoreyRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.ColumnContentQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.MixedComponentTabQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.api.response.RankPageResponse;
import com.wanmi.sbc.setting.bean.dto.*;
import com.wanmi.sbc.setting.bean.enums.MixedComponentLevel;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyTypeV2;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.topic.response.TopicResponse;
import com.wanmi.sbc.topic.response.TopicStoreyResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.RedisKeyUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisListService redisListService;

    @Autowired
    private PoolFactory poolFactory;


    @Override
    public ReturnT<String> execute(String param) throws Exception {
        this.saveMixedComponentContent();
        return SUCCESS;
    }

    public void saveMixedComponentContent() {
        //栏目信息
        Integer topicStoreyId = 194;
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
        redisService.setString(RedisKeyUtil.MIXED_COMPONENT_TAB, JSON.toJSONString(mixedComponentDtos));
        for (MixedComponentDto mixedComponentDto : mixedComponentDtos) {
            Integer tabId = mixedComponentDto.getId();
            // 获取所有关键字
            List<KeyWordDto> keywords = new ArrayList<>();
            mixedComponentTab.stream().filter(c -> MixedComponentLevel.TWO.toValue().equals(c.getLevel()) && tabId.equals(c.getPId()))
                    .map(c -> {return c.getKeywords();}).collect(Collectors.toList())
                    .forEach(c -> {c.forEach(s -> {keywords.add(new KeyWordDto(s.getId(), s.getName()));});});
            // 获取规则
            List<String> rules = new ArrayList<>();
            mixedComponentTab.stream().filter(c -> MixedComponentLevel.THREE.toValue().equals(c.getLevel()) && tabId.equals(c.getPId()))
                    .map(c -> {return c.getKeywords();}).collect(Collectors.toList())
                    .forEach(c -> {c.forEach(s -> {rules.add(s.getName());});});
            //获取商品池
            List<MixedComponentTabDto> pools = mixedComponentTab.stream().filter(c -> MixedComponentLevel.FOUR.toValue().equals(c.getLevel()) && rules.contains(c.getDropName())).collect(Collectors.toList());
            //前台透出关键词
            List<KeyWordDto> showKeyword = new ArrayList<>();
            for (KeyWordDto keyword : keywords) {
                String keyWordId = keyword.getId();
                String keyWord = keyword.getName();
                List<GoodsPoolDto> goodsPoolDtos = new ArrayList<>();
                for (MixedComponentTabDto pool : pools) {
                    Integer id = pool.getId();
                    ColumnContentQueryRequest columnContentQueryRequest = new ColumnContentQueryRequest();
                    columnContentQueryRequest.setTopicStoreySearchId(id);
                    List<ColumnContentDTO> columnContent = topicConfigProvider.ListTopicStoreyColumnContent(columnContentQueryRequest).getContext();
                    PoolService poolService = poolFactory.getPoolService(pool.getBookType());
                    poolService.getGoodsPool(goodsPoolDtos, columnContent, pool, keyWord);
                }
                //排序
                List<GoodsPoolDto> goodsPools = goodsPoolDtos.stream().sorted(Comparator.comparing(GoodsPoolDto::getSorting)
                                .thenComparing(Comparator.comparing(GoodsPoolDto::getType).reversed()))
                        .collect(Collectors.toList());
                if(goodsPools.size() != 0 && goodsPools != null) {showKeyword.add(new KeyWordDto(keyWordId, keyWord));}
                //存redis
                redisListService.putAll(RedisKeyUtil.MIXED_COMPONENT+ tabId + ":" + keyWordId, goodsPools);
            }
            redisService.setString(RedisKeyUtil.MIXED_COMPONENT + tabId + ":keywords", JSON.toJSONString(showKeyword));
        }
    }
}
