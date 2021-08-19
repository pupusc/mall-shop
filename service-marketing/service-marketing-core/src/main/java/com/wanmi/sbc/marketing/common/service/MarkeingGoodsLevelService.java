package com.wanmi.sbc.marketing.common.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleInProgressRequest;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleInProgressRequest;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.MarketingPluginService;
import com.wanmi.sbc.marketing.api.response.market.MarketingGoodsForXsiteResponse;
import com.wanmi.sbc.marketing.bean.enums.MarketingLevelType;
import com.wanmi.sbc.marketing.distribution.service.DistributionSettingService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 营销过滤服务
 */
@Service
public class MarkeingGoodsLevelService {

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MarketingPluginService marketingPluginService;

    @Autowired
    private DistributionSettingService distributionSettingService;

    @Autowired
    private FlashSaleGoodsQueryProvider flashSaleGoodsQueryProvider;

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    /**
     * 魔方组件根据活动类型过滤
     *
     * @param goodsInfoIds
     * @param marketingLevelType
     * @return
     */
    public BaseResponse<MarketingGoodsForXsiteResponse> dealByMarketingLevel(List<String> goodsInfoIds, MarketingLevelType marketingLevelType) {
        MarketingGoodsForXsiteResponse marketingGoodsForXsiteResponse = new MarketingGoodsForXsiteResponse();
        List<GoodsInfoVO> skus = goodsInfoQueryProvider.listByIds(
                GoodsInfoListByIdsRequest.builder().goodsInfoIds(goodsInfoIds).build()).getContext().getGoodsInfos();
        Map<String, GoodsInfoVO> skuMap = skus.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, i -> i));

        //过滤的起始等级
        int level = marketingLevelType.toValue() - 1;
        if (level >= NumberUtils.INTEGER_ZERO) {
            MarketingLevelType filterType = MarketingLevelType.fromValue(level);

            // 积分价>预约预售>秒杀>分销>企业价>拼团
            // 按优先级倒序
            switch (filterType) {
                case ENTER_PRISE_PRICE:
                    goodsInfoIds = filterEnterPrisePrice(goodsInfoIds, skuMap);
                case DISTRIBUTION:
                    goodsInfoIds = filterDistribution(goodsInfoIds, skuMap);
                case FLASH_SALE:
                    goodsInfoIds = filterFlashSale(goodsInfoIds);
                case APPOINTMENT_OR_BOOKING:
                    goodsInfoIds = filterAppointmentAndBooking(goodsInfoIds);
                case POINTS_PRICE:
                    goodsInfoIds = filterPointsPrice(goodsInfoIds, skuMap);
            }
        }

        marketingGoodsForXsiteResponse.setGoodsInfoIds(goodsInfoIds);
        return BaseResponse.success(marketingGoodsForXsiteResponse);
    }

    /**
     * 过滤预约、预售商品
     *
     * @param goodsInfoIds
     * @return
     */
    public List<String> filterAppointmentAndBooking(List<String> goodsInfoIds) {
        if(CollectionUtils.isEmpty(goodsInfoIds)) {
            return new ArrayList<>();
        }
        // 根据id查询预约活动
        AppointmentSaleInProgressRequest appointmentRequest = AppointmentSaleInProgressRequest.builder().goodsInfoIdList(goodsInfoIds).build();
        List<AppointmentSaleVO> appointmentSaleVOList = appointmentSaleQueryProvider.inProgressAppointmentSaleInfoByGoodsInfoIdList(appointmentRequest)
                .getContext().getAppointmentSaleVOList();
        if (CollectionUtils.isNotEmpty(appointmentSaleVOList)) {
            // 聚合出活动中的skuId
            List<String> appointmentGoodsInfoIds = appointmentSaleVOList.stream()
                    .map(vo -> vo.getAppointmentSaleGood().getGoodsInfoId()).collect(Collectors.toList());
            goodsInfoIds = goodsInfoIds.stream().filter(id -> !appointmentGoodsInfoIds.contains(id)).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(goodsInfoIds)) return new ArrayList<>();
        }

        // 根据id查询预售活动
        BookingSaleInProgressRequest bookingRequest = BookingSaleInProgressRequest.builder().goodsInfoIdList(goodsInfoIds).build();
        List<BookingSaleVO> bookingSaleVOList = bookingSaleQueryProvider.inProgressBookingSaleInfoByGoodsInfoIdList(bookingRequest)
                .getContext().getBookingSaleVOList();

        if (CollectionUtils.isNotEmpty(bookingSaleVOList)) {
            LocalDateTime now = LocalDateTime.now();
            // 定金预售需在定金支付时间内才算在活动中
            // 聚合出活动中的skuId
            List<String> bookingGoodsInfoIds =bookingSaleVOList.stream().filter(vo -> !(vo.getBookingType().equals(NumberUtils.INTEGER_ONE)
                    && (vo.getHandSelStartTime().isAfter(now) || vo.getHandSelEndTime().isBefore(now))))
                    .map(vo -> vo.getBookingSaleGoods().getGoodsInfoId()).collect(Collectors.toList());
            goodsInfoIds = goodsInfoIds.stream().filter(id -> !bookingGoodsInfoIds.contains(id)).collect(Collectors.toList());
        }

        return goodsInfoIds;
    }

    /**
     * 过滤秒杀商品
     *
     * @param goodsInfoIds
     * @return
     */
    public List<String> filterFlashSale(List<String> goodsInfoIds) {
        if(CollectionUtils.isEmpty(goodsInfoIds)) {
            return new ArrayList<>();
        }
        // 查询进行中的秒杀商品
        List<FlashSaleGoodsVO> flashSaleGoodsVOList = flashSaleGoodsQueryProvider
                .list(FlashSaleGoodsListRequest.builder().goodsinfoIds(goodsInfoIds).queryDataType(1).build()).getContext().getFlashSaleGoodsVOList();
        if (CollectionUtils.isNotEmpty(flashSaleGoodsVOList)) {
            List<String> flashGoodsInfoIds = flashSaleGoodsVOList.stream()
                    .map(FlashSaleGoodsVO::getGoodsInfoId).collect(Collectors.toList());
            goodsInfoIds = goodsInfoIds.stream().filter(id -> !flashGoodsInfoIds.contains(id)).collect(Collectors.toList());
        }

        return goodsInfoIds;
    }

    /**
     * 过滤积分价商品
     *
     * @param goodsInfoIds
     * @return
     */
    public List<String> filterPointsPrice(List<String> goodsInfoIds, Map<String, GoodsInfoVO> skuMap) {
        List<String> newIds = goodsInfoIds.stream().filter(id -> {
            GoodsInfoVO goodsInfoVO = skuMap.get(id);
            if (Objects.isNull(goodsInfoIds) || goodsInfoVO.getBuyPoint() != NumberUtils.LONG_ZERO) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        return newIds;
    }

    /**
     * 过滤分销
     *
     * @param goodsInfoIds
     * @return
     */
    public List<String> filterDistribution(List<String> goodsInfoIds, Map<String, GoodsInfoVO> skuMap) {
        // 获取设置开关
        DefaultFlag bossOpenFlag = distributionSettingService.querySimSetting().getDistributionSettingSimVO().getOpenFlag();
        List<String> newIds = goodsInfoIds.stream().filter(id -> {
            GoodsInfoVO goodsInfoVO = skuMap.get(id);
            DefaultFlag storeOpenFlag = distributionSettingService.queryStoreSetting(goodsInfoVO.getStoreId().toString()).getOpenFlag();
            //平台分销、店铺分销开关、商品分销审核状态
            if(Objects.isNull(goodsInfoIds) || DefaultFlag.YES.equals(bossOpenFlag) && DefaultFlag.YES.equals(storeOpenFlag)
                    && DistributionGoodsAudit.CHECKED.equals(goodsInfoVO.getDistributionGoodsAudit())) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());

        return newIds;
    }

    /**
     * 过滤企业价
     * @param goodsInfoIds
     * @param skuMap
     * @return
     */
    public List<String> filterEnterPrisePrice(List<String> goodsInfoIds, Map<String, GoodsInfoVO> skuMap){
        List<String> newIds = goodsInfoIds.stream().filter(id -> {
            GoodsInfoVO goodsInfoVO = skuMap.get(id);
            if (Objects.isNull(goodsInfoIds) || EnterpriseAuditState.CHECKED.equals(goodsInfoVO.getEnterPriseAuditState())) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        return newIds;
    }
}
