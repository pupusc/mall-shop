package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wanmi.sbc.goods.api.provider.cyclebuy.CycleBuyQueryProvider;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyByGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuySendDateRuleRequest;
import com.wanmi.sbc.goods.bean.enums.DeliveryPlan;
import com.wanmi.sbc.goods.bean.enums.GiftGiveMethod;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.order.bean.dto.CycleBuyInfoDTO;
import com.wanmi.sbc.order.bean.dto.CycleBuySendDateRuleDTO;
import com.wanmi.sbc.order.request.ImmediateBuyRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.flashsale.request.RushToBuyFlashSaleGoodsRequest;
import com.wanmi.sbc.flashsale.service.FlashSaleService;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.intervalprice.GoodsIntervalPriceService;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.order.api.provider.trade.TradeItemProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.VerifyQueryProvider;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.TradeGetGoodsResponse;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.request.TradeItemConfirmRequest;
import com.wanmi.sbc.order.request.TradeItemRequest;
import com.wanmi.sbc.redis.RedisService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName ImmediatelyFlashSaleGoods
 * @Description 商品抢购，抢购资格mq消息消费处理逻辑
 * @Author lvzhenwei
 * @Date 2019/6/14 9:55
 **/
@Component
@EnableBinding(RushToBuyFlashSaleGoodsSink.class)
public class RushToBuyFlashSaleGoodsConsumerService {

    @Autowired
    private FlashSaleService flashSaleService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Resource
    private VerifyQueryProvider verifyQueryProvider;

    @Autowired
    private TradeItemProvider tradeItemProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Resource
    private GoodsIntervalPriceService goodsIntervalPriceService;

    @Resource
    private MarketingLevelPluginProvider marketingLevelPluginProvider;

    @Autowired
    private FlashSaleOrderCancelReturnStockMqService flashSaleOrderCancelReturnStockMqService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CommonUtil commonUtil;

    @StreamListener(RushToBuyFlashSaleGoodsSink.INPUT)
    public void rushToBuyFlashSaleGoodsConsumer(String message) {
        RushToBuyFlashSaleGoodsRequest request = JSONObject.parseObject(message, RushToBuyFlashSaleGoodsRequest.class);
        RLock rLock = null;
        //判断抢购秒杀商品条件
        try {
            flashSaleService.judgeBuyFalshSaleGoodsCondition(request);
            //扣减库存（加锁，保证高并发下库存是正确的）
            rLock = redissonClient.getFairLock(RedisKeyConstant.FLASH_SALE_GOODS_INFO_STOCK + request.getFlashSaleGoodsId());
            try {
                rLock.lock();
                //获取redis里填实的sku库存，redis如果是null暂时通过，由commit里再进行验证
                Long skuStock = null;
                Object stockInRedis = redisTemplate.opsForValue().get(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX.concat(String.valueOf(request.getFlashSaleGoodsId())));
                if (Objects.nonNull(stockInRedis)) {
                    skuStock = NumberUtils.toLong(Objects.toString(stockInRedis));
                }

                //查询现在库存
                FlashSaleGoodsVO flashSaleGoodsVO = flashSaleService.getFlashSaleGoodsInfoForRedis(request);
                if (Objects.nonNull(flashSaleGoodsVO)
                        && flashSaleGoodsVO.getStock() >= request.getFlashSaleGoodsNum()
                        && (skuStock == null || skuStock >= request.getFlashSaleGoodsNum())) {
                    //生成订单快照
                    generateSnapshot(request);
                    //扣减库存,更新原来库存
                    flashSaleGoodsVO.setStock(flashSaleGoodsVO.getStock() - request.getFlashSaleGoodsNum());
                    String flashSaleGoodsInfoKey = RedisKeyConstant.FLASH_SALE_GOODS_INFO + request.getFlashSaleGoodsId();
                    redisService.setObj(flashSaleGoodsInfoKey, flashSaleGoodsVO, Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
                    try {
                        //该秒杀商品对应会员抢购次数（用于该次抢购商品下单是使用）
                        String flashSaleNumInfo = redisService.getString(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS +
                                request.getCustomerId() + request.getFlashSaleGoodsId() + "flashSaleNum");
                        //如果存在则更新值，不存在设置初始值
                        if (StringUtils.isNotBlank(flashSaleNumInfo)) {
                            //设置对应秒杀商品下单数缓存为两小时
                            redisService.setObj(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS + request.getCustomerId() +
                                            request.getFlashSaleGoodsId() + "flashSaleNum",
                                    Integer.valueOf(flashSaleNumInfo)+1, Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
                            request.setFlashSaleNum(Integer.valueOf(flashSaleNumInfo)+1);
                        } else {
                            //设置对应秒杀商品下单数缓存为两小时
                            redisService.setObj(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS + request.getCustomerId() +
                                            request.getFlashSaleGoodsId() + "flashSaleNum",
                                    1, Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
                            request.setFlashSaleNum(1);
                        }
                        //设置抢购资格redis缓存(时效为5分钟)
                        redisService.setObj(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS + request.getCustomerId() + request.getFlashSaleGoodsId(),
                                request, Constants.FLASH_SALE_GOODS_QUALIFICATIONS_VALIDITY_PERIOD * 60);
                    } catch (Exception e) {
                        String flashSaleNumInfo = redisService.getString(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS +
                                request.getCustomerId() + request.getFlashSaleGoodsId() + "flashSaleNum");
                        redisService.setObj(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS + request.getCustomerId() +
                                        request.getFlashSaleGoodsId() + "flashSaleNum",
                                Integer.valueOf(flashSaleNumInfo)-1, Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
                        request.setFlashSaleNum(Integer.valueOf(flashSaleNumInfo)-1);
                        //扣库存成功以后，如果设置抢购资格出错，将商品对应的库存在加回去
                        flashSaleGoodsVO.setStock(flashSaleGoodsVO.getStock() + request.getFlashSaleGoodsNum());
                        redisService.setObj(flashSaleGoodsInfoKey, flashSaleGoodsVO, Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
                        redisService.setString(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS_FAILED+ request.getCustomerId() + request.getFlashSaleGoodsId(),"Failed",Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
                        e.printStackTrace();
                    }
                    try {
                        //RabbitMQ设置延迟队列在抢购资格超过五分钟自动消费该消息，并将改资格的库存还回到redis缓存库存
                        flashSaleOrderCancelReturnStockMqService.flashSaleOrderCancelReturnStock(JSONObject.toJSONString(request));
                    } catch (Exception e) {
                        //删除资格
                        redisService.delete(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS + request.getCustomerId() + request.getFlashSaleGoodsId());
                        //将商品对应的库存在加回去
                        flashSaleGoodsVO.setStock(flashSaleGoodsVO.getStock() + request.getFlashSaleGoodsNum());
                        redisService.setObj(flashSaleGoodsInfoKey, flashSaleGoodsVO, Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
                        redisService.setString(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS_FAILED+ request.getCustomerId() + request.getFlashSaleGoodsId(),"Failed",Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                redisService.setString(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS_FAILED+ request.getCustomerId() + request.getFlashSaleGoodsId(),"Failed",Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
                e.printStackTrace();
            } finally {
                if(Objects.nonNull(rLock)){
                    rLock.unlock();
                }
            }
        } catch (Exception e) {
            redisService.setString(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS_FAILED+ request.getCustomerId() + request.getFlashSaleGoodsId(),"Failed",Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
            e.printStackTrace();
        }finally {
            if(Objects.nonNull(rLock)){
                rLock.unlock();
            }
        }
    }

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 生成抢购订单快照
     * @Date 15:02 2019/6/17
     * @Param [request]
     **/
    @GlobalTransactional
    public BaseResponse generateSnapshot(RushToBuyFlashSaleGoodsRequest request) {
        //获取抢购商品信息
        FlashSaleGoodsVO flashSaleGoodsVO = flashSaleService.getFlashSaleGoodsInfoForRedis(request);
        //设置抢购订单快照数据信息
        TradeItemRequest tradeItemRequest = new TradeItemRequest();
        tradeItemRequest.setNum(request.getFlashSaleGoodsNum());
        tradeItemRequest.setSkuId(flashSaleGoodsVO.getGoodsInfoId());
        tradeItemRequest.setPrice(flashSaleGoodsVO.getPrice());
        tradeItemRequest.setIsFlashSaleGoods(true);
        tradeItemRequest.setFlashSaleGoodsId(flashSaleGoodsVO.getId());

        List<TradeItemRequest> tradeItemConfirmRequests = new ArrayList<>();
        tradeItemConfirmRequests.add(tradeItemRequest);

        List<TradeMarketingDTO> tradeMarketingList = new ArrayList<>();
        TradeItemConfirmRequest confirmRequest = new TradeItemConfirmRequest();
        confirmRequest.setTradeItems(tradeItemConfirmRequests);
        confirmRequest.setTradeMarketingList(tradeMarketingList);
        confirmRequest.setForceConfirm(false);
        String customerId = request.getCustomerId();
        List<TradeItemDTO> tradeItems = confirmRequest.getTradeItems().stream().map(
                o -> TradeItemDTO.builder().skuId(o.getSkuId()).num(o.getNum()).price(o.getPrice())
                        .isFlashSaleGoods(o.getIsFlashSaleGoods()).flashSaleGoodsId(o.getFlashSaleGoodsId()).build()
        ).collect(Collectors.toList());
        List<String> skuIds =
                confirmRequest.getTradeItems().stream().map(TradeItemRequest::getSkuId).collect(Collectors.toList());
        //验证用户
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (customerId)).getContext();
        GoodsInfoResponse response = getGoodsResponse(skuIds, customer);
        //商品验证
        verifyQueryProvider.verifyGoods(new VerifyGoodsRequest(tradeItems, Collections.emptyList(),
                KsBeanUtil.convert(response, TradeGoodsInfoPageDTO.class), null, false));
        verifyQueryProvider.verifyStore(new VerifyStoreRequest(response.getGoodsInfos().stream().map
                (GoodsInfoVO::getStoreId).collect(Collectors.toList())));
        //营销活动校验
        verifyQueryProvider.verifyTradeMarketing(new VerifyTradeMarketingRequest(confirmRequest.getTradeMarketingList
                (), Collections.emptyList(), tradeItems, customerId, confirmRequest.isForceConfirm()));
        return tradeItemProvider.snapshot(TradeItemSnapshotRequest.builder().customerId(customerId).tradeItems
                (tradeItems)
                .tradeMarketingList(confirmRequest.getTradeMarketingList())
                .skuList(KsBeanUtil.convertList(response.getGoodsInfos(), GoodsInfoDTO.class))
                .snapshotType(Constants.FLASH_SALE_GOODS_ORDER_TYPE)
                .terminalToken(request.getTerminalToken()).build());
    }

    /**
     * 获取订单商品详情,包含区间价，会员级别价
     */
    private GoodsInfoResponse getGoodsResponse(List<String> skuIds, CustomerVO customer) {
        TradeGetGoodsResponse response =
                tradeQueryProvider.getGoods(TradeGetGoodsRequest.builder().skuIds(skuIds).build()).getContext();
        //计算区间价
        GoodsIntervalPriceByCustomerIdResponse priceResponse = goodsIntervalPriceService.getGoodsIntervalPriceVOList
                (response.getGoodsInfos(), customer.getCustomerId());
        response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
        response.setGoodsInfos(priceResponse.getGoodsInfoVOList());
        //获取客户的等级
        if (StringUtils.isNotBlank(customer.getCustomerId())) {
            //计算会员价
            response.setGoodsInfos(
                    marketingLevelPluginProvider.goodsListFilter(MarketingLevelGoodsListFilterRequest.builder()
                            .goodsInfos(KsBeanUtil.convert(response.getGoodsInfos(), GoodsInfoDTO.class))
                            .customerDTO(KsBeanUtil.convert(customer, CustomerDTO.class)).build())
                            .getContext().getGoodsInfoVOList());
        }
        return GoodsInfoResponse.builder().goodsInfos(response.getGoodsInfos())
                .goodses(response.getGoodses())
                .goodsIntervalPrices(response.getGoodsIntervalPrices())
                .build();
    }
}
