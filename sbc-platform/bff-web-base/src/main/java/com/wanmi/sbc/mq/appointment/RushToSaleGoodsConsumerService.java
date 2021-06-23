package com.wanmi.sbc.mq.appointment;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.appointmentsale.service.AppointmentSaleService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.RushToAppointmentSaleGoodsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
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
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName RushToAppointmentSaleGoodsConsumerService
 * @Description 商品预约，抢购数量同步
 * @Author zxd
 * @Date 2020/05/25 10:26
 **/
@Component
@EnableBinding(RushToSaleGoodsSink.class)
@Slf4j
public class RushToSaleGoodsConsumerService {

    @Autowired
    private AppointmentSaleService appointmentSaleService;


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private AppointmentSaleGoodsProvider appointmentSaleGoodsProvider;
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
    private RedisService redisService;


    @StreamListener(RushToSaleGoodsSink.INPUT)
    public void rushToSaleGoodsConsumer(String message) {
        RushToAppointmentSaleGoodsRequest request = JSONObject.parseObject(message, RushToAppointmentSaleGoodsRequest.class);
        RLock rLock = null;
        //判断预约商品条件
        try {
            appointmentSaleService.judgeAppointmentSaleGoodsCondition(request);
            //（加锁，保证高并发下库存是正确的）扣减库存
            rLock = redissonClient.getFairLock(RedisKeyConstant.RUSH_SALE_GOODS_INFO_COUNT + request.getAppointmentSaleId() + ":" + request.getSkuId());
            try {
                rLock.lock();
                //获取现有抢购商品信息
                AppointmentSaleVO appointmentSaleVO = appointmentSaleService.getAppointmentSaleGoodsInfoForRedis(request);
                if (Objects.nonNull(appointmentSaleVO) && appointmentSaleVO.getStock() >= request.getNum()) {
                    // 生成订单快照
                    generateSnapshot(request);
                    // 扣减库存，更新原来库存
                    appointmentSaleVO.setStock(appointmentSaleVO.getStock() - request.getNum());
                    String appointmentSaleGoodsInfoKey = RedisKeyConstant.APPOINTMENT_SALE_GOODS_INFO + request.getAppointmentSaleId() + ":" + request.getSkuId();
                    long minutes = Duration.between(appointmentSaleVO.getSnapUpEndTime(), LocalDateTime.now()).toMinutes();
                    redisService.setObj(appointmentSaleGoodsInfoKey, appointmentSaleVO, Math.abs(minutes) * 60);
                    try {
                        //设置抢购资格redis缓存(时效为5分钟)
                        redisService.setObj(RedisKeyConstant.APPOINTMENT_SALE_GOODS_QUALIFICATIONS + request.getCustomerId()
                                        + ":" + request.getAppointmentSaleId() + ":" + request.getSkuId(),
                                request, Constants.APPOINTMENT_SALE_GOODS_QUALIFICATIONS_VALIDITY_PERIOD * 60);
                    } catch (Exception e) {
                        //扣库存成功以后，如果设置抢购资格出错，将商品对应的库存在加回去
                        appointmentSaleVO.setStock(appointmentSaleVO.getStock() + request.getNum());
                        redisService.setObj(appointmentSaleGoodsInfoKey, appointmentSaleVO, Math.abs(minutes) * 60);
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(Objects.nonNull(rLock)){
                    rLock.unlock();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(Objects.nonNull(rLock)){
                rLock.unlock();
            }
        }
    }


    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author zhangxiaodong
     * @Description 生成抢购订单快照
     * @Date 2020/05/25
     * @Param [request]
     **/
    @GlobalTransactional
    public BaseResponse generateSnapshot(RushToAppointmentSaleGoodsRequest request) {
        //获取抢购商品信息
        AppointmentSaleVO appointmentSaleVO = appointmentSaleService.getAppointmentSaleGoodsInfoForRedis(request);
        //设置抢购订单快照数据信息
        TradeItemRequest tradeItemRequest = new TradeItemRequest();
        tradeItemRequest.setNum(request.getNum());
        GoodsInfoVO goodsInfoVO = appointmentSaleVO.getAppointmentSaleGood().getGoodsInfoVO();
        tradeItemRequest.setSkuId(goodsInfoVO.getGoodsInfoId());
        tradeItemRequest.setPrice(Objects.isNull(appointmentSaleVO.getAppointmentSaleGood().getPrice()) ?
                goodsInfoVO.getMarketPrice() : appointmentSaleVO.getAppointmentSaleGood().getPrice());
        tradeItemRequest.setIsFlashSaleGoods(false);
        tradeItemRequest.setAppointmentSaleId(appointmentSaleVO.getId());
        tradeItemRequest.setIsAppointmentSaleGoods(true);

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
                        .isFlashSaleGoods(o.getIsFlashSaleGoods()).flashSaleGoodsId(o.getFlashSaleGoodsId())
                        .isAppointmentSaleGoods(o.getIsAppointmentSaleGoods()).appointmentSaleId(o.getAppointmentSaleId()).build()
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
                .snapshotType(Constants.APPOINTMENT_SALE_GOODS_ORDER_TYPE).build());
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
