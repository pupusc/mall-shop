package com.wanmi.sbc.task;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soybean.marketing.api.provider.activity.NormalActivityPointSkuProvider;
import com.soybean.marketing.api.resp.NormalActivitySkuResp;
import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.GoodsIdsByRankListIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.RankGoodsPublishResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfosRedisResponse;
import com.wanmi.sbc.goods.api.response.goods.NewBookPointRedisResponse;
import com.wanmi.sbc.goods.api.response.index.NormalModuleSkuResp;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.dto.MarketingLabelNewDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.redis.RedisListService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.RankRedisListResponse;
import com.wanmi.sbc.setting.api.request.RankRequest;
import com.wanmi.sbc.setting.api.request.RankRequestListResponse;
import com.wanmi.sbc.topic.response.NewBookPointResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.RedisKeyUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@JobHandler(value = "newBookPointJobHandler")
@Component
@EnableBinding
@Slf4j
public class NewBookPointJobHandler extends IJobHandler {

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
    private PointsGoodsQueryProvider pointsGoodsQueryProvider;

    @Autowired
    private NormalActivityPointSkuProvider normalActivityPointSkuProvider;

    @Autowired
    private RedisListService redisListService;

    @Autowired
    private RedisService redisService;


    @Override
    public ReturnT<String> execute(String param) throws Exception {
        //获取首页榜单
        RankRedisListResponse rank = new RankRedisListResponse();
//        rank.setRankRequestList(this.rank(param));
        GoodsInfosRedisResponse goodsInfoVOS = this.newBookPoint(new BaseQueryRequest());
//        redisService.setString(RedisKeyUtil.HOME_RANK, JSON.toJSONString(rank));
//        redisListService.putAll(RedisKeyUtil.NEW_BOOK_POINT,goodsInfoVOS);
        if(redisService.hasKey(RedisKeyUtil.NEW_BOOK_POINT+param)){
            redisService.delete(RedisKeyUtil.NEW_BOOK_POINT+param);
        }
        redisService.setString(RedisKeyUtil.NEW_BOOK_POINT+param,JSON.toJSONString(goodsInfoVOS));
        return SUCCESS;
    }

    public GoodsInfosRedisResponse newBookPoint(BaseQueryRequest baseQueryRequest) {

        try {
            List<NewBookPointResponse> newBookPointResponseList = new ArrayList<>();


            List<NormalModuleSkuResp> context = pointsGoodsQueryProvider.getReturnPointGoods(baseQueryRequest).getContext();

            List<NormalActivitySkuResp> ponitByActivity = normalActivityPointSkuProvider.getPonitByActivity();

            Map<String, NormalActivitySkuResp> goodsPointMap = ponitByActivity.stream()
                    .filter(normalActivitySkuResp -> normalActivitySkuResp.getNum() != 0)
                    .collect(Collectors.toMap(NormalActivitySkuResp::getSkuId, Function.identity()));

            //获取商品积分
            List<String> skuIdList = new ArrayList<>();
            context.stream().forEach(normalModuleSkuResp -> {
                NewBookPointResponse newBookPointResponse = new NewBookPointResponse();
                BeanUtils.copyProperties(normalModuleSkuResp, newBookPointResponse);
                if (null != goodsPointMap.get(normalModuleSkuResp.getSkuId()) && null != goodsPointMap.get(normalModuleSkuResp.getSkuId()).getNum()) {
                    newBookPointResponse.setNum(goodsPointMap.get(normalModuleSkuResp.getSkuId()).getNum());
                }
                String old_json = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID" + ":" + normalModuleSkuResp.getSkuId());
                if(null!=old_json) {
                    Map labelMap = JSONObject.parseObject(old_json, Map.class);
                    newBookPointResponse.setLabelMap(labelMap);
                }
                skuIdList.add(newBookPointResponse.getSkuId());
                newBookPointResponseList.add(newBookPointResponse);
            });

            //获取商品信息
            GoodsInfoViewByIdsRequest goodsInfoByIdRequest = new GoodsInfoViewByIdsRequest();
            goodsInfoByIdRequest.setDeleteFlag(DeleteFlag.NO);
            goodsInfoByIdRequest.setGoodsInfoIds(skuIdList);
            goodsInfoByIdRequest.setIsHavSpecText(1);
            List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listViewByIds(goodsInfoByIdRequest).getContext().getGoodsInfos();
            GoodsInfosRedisResponse response=new GoodsInfosRedisResponse();
            response.setGoodsInfoVOList(goodsInfos);
            response.setNewBookPointResponseList(KsBeanUtil.convert(newBookPointResponseList, NewBookPointRedisResponse.class));
            return response;
        }catch (Exception e){
            return null;
        }
    }
}
