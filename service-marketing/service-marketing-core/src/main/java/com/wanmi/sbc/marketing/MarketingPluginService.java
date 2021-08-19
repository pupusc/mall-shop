package com.wanmi.sbc.marketing;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SpringContextHolder;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelMapByCustomerIdAndStoreIdsRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelMapGetResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.response.info.GoodsInfoListByGoodsInfoResponse;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.enums.MarketingStatus;
import com.wanmi.sbc.marketing.common.request.MarketingRequest;
import com.wanmi.sbc.marketing.common.response.MarketingResponse;
import com.wanmi.sbc.marketing.common.service.MarketingService;
import com.wanmi.sbc.marketing.common.service.MarketingCommonService;
import com.wanmi.sbc.marketing.plugin.IGoodsDetailPlugin;
import com.wanmi.sbc.marketing.plugin.IGoodsListPlugin;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 主插件服务
 * Created by dyt on 2016/12/2.
 */
@Data
@Slf4j
public class MarketingPluginService {

    /**
     * 商品列表插件集
     */
    private List<String> goodsListPlugins;


    /**
     * 秒杀plugin
     */
    private String flashSalePlugin = "flashSalePlugin";

    /**
     * 优惠券plugin
     */
    private String couponPlugin = "couponPlugin";


    /**
     * 商品详情插件集
     */
    private List<String> goodsDetailPlugins;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private MarketingService marketingService;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private MarketingCommonService marketingCommonService;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    /**
     * 商品列表处理
     *
     * @param goodsInfos 商品数据
     * @param request    参数
     * @throws SbcRuntimeException
     */
    public GoodsInfoListByGoodsInfoResponse goodsListFilter(List<GoodsInfoVO> goodsInfos, MarketingPluginRequest request) throws SbcRuntimeException {
        if (CollectionUtils.isEmpty(goodsListPlugins) || CollectionUtils.isEmpty(goodsInfos)) {
            return new GoodsInfoListByGoodsInfoResponse();
        }
        //设定等级
        request.setLevelMap(this.getCustomerLevels(goodsInfos, KsBeanUtil.convert(request.getCustomer(), CustomerVO.class)));
        //设定营销
        if (!request.getCommitFlag()) {
            request.setMarketingMap(getMarketing(goodsInfos, request.getLevelMap(), KsBeanUtil.convert(request.getCustomer(), CustomerDTO.class)));
        }
        for (String plugin : goodsListPlugins) {
            if (Objects.nonNull(request.getIsFlashSaleMarketing()) && request.getIsFlashSaleMarketing()) {
                if (flashSalePlugin.equals(plugin)) {
                    continue;
                }
            }
            Object pluginObj = SpringContextHolder.getBean(plugin);
            if (pluginObj instanceof IGoodsListPlugin) {
                long start = System.currentTimeMillis();
                ((IGoodsListPlugin) pluginObj).goodsListFilter(goodsInfos, request);
                long end = System.currentTimeMillis();
                log.debug("商品列表处理【".concat(plugin).concat("】执行时间:").concat(String.valueOf((end - start))).concat("毫秒"));
            } else {
                log.error("商品列表处理【".concat(plugin).concat("】没有实现IGoodsListPlugin接口"));
            }
        }
        return new GoodsInfoListByGoodsInfoResponse(goodsInfos);
    }

    /**
     * 商品详情处理
     *
     * @param detailResponse 商品详情数据
     * @param request        参数
     * @throws SbcRuntimeException
     */
    public GoodsInfoDetailByGoodsInfoResponse goodsDetailFilter(GoodsInfoDetailByGoodsInfoResponse detailResponse, MarketingPluginRequest request) throws SbcRuntimeException {
        if (CollectionUtils.isEmpty(goodsDetailPlugins) || Objects.isNull(detailResponse.getGoodsInfo())) {
            return null;
        }
        //设定等级
        request.setLevelMap(this.getCustomerLevels(Collections.singletonList(detailResponse.getGoodsInfo()), request.getCustomer()));
        //设定营销
        request.setMarketingMap(getMarketing(Collections.singletonList(detailResponse.getGoodsInfo()), request.getLevelMap(), KsBeanUtil.convert(request.getCustomer(), CustomerDTO.class)));
        for (String plugin : goodsDetailPlugins) {
            Object pluginObj = SpringContextHolder.getBean(plugin);
            if (pluginObj instanceof IGoodsDetailPlugin) {
                long start = System.currentTimeMillis();
                ((IGoodsDetailPlugin) pluginObj).goodsDetailFilter(detailResponse, request);
                long end = System.currentTimeMillis();
                log.debug("商品详情处理【".concat(plugin).concat("】执行时间:").concat(String.valueOf((end - start))).concat("毫秒"));
            } else {
                log.error("商品详情处理【".concat(plugin).concat("】没有实现IGoodsDetailPlugin接口"));
            }
        }
        return detailResponse;
    }

    /**
     * 热销商品列表
     *
     * @param goodsInfos
     * @param request
     * @return
     */
    public GoodsInfoListByGoodsInfoResponse distributionGoodsListFilter(List<GoodsInfoVO> goodsInfos, MarketingPluginRequest request) {
        if (!goodsDetailPlugins.contains(couponPlugin) || CollectionUtils.isEmpty(goodsInfos)) {
            return new GoodsInfoListByGoodsInfoResponse(Collections.emptyList());
        }

        //设定等级
        request.setLevelMap(this.getCustomerLevels(goodsInfos, KsBeanUtil.convert(request.getCustomer(), CustomerVO.class)));

        Object pluginObj = SpringContextHolder.getBean(couponPlugin);
        if (pluginObj instanceof IGoodsDetailPlugin) {
            long start = System.currentTimeMillis();
            ((IGoodsListPlugin) pluginObj).goodsListFilter(goodsInfos, request);
            long end = System.currentTimeMillis();
            log.debug("热销商品列表处理【".concat(couponPlugin).concat("】执行时间:").concat(String.valueOf((end - start))).concat("毫秒"));
        } else {
            log.error("热销商品列表处理【".concat(couponPlugin).concat("】没有实现IGoodsDetailPlugin接口"));
        }

        return new GoodsInfoListByGoodsInfoResponse(goodsInfos);

    }

    /**
     * 获取会员等级
     *
     * @param goodsInfoList 商品
     * @param customer      客户
     */
    public HashMap<Long, CommonLevelVO> getCustomerLevels(List<GoodsInfoVO> goodsInfoList, CustomerVO customer) {
        List<Long> storeIds = goodsInfoList.stream()
                .map(GoodsInfoVO::getStoreId)
                .filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());

        if (customer == null || StringUtils.isBlank(customer.getCustomerId()) || CollectionUtils.isEmpty(storeIds)) {
            return new HashMap<>();
        }
        CustomerLevelMapByCustomerIdAndStoreIdsRequest customerLevelMapByCustomerIdAndStoreIdsRequest = new CustomerLevelMapByCustomerIdAndStoreIdsRequest();
        customerLevelMapByCustomerIdAndStoreIdsRequest.setCustomerId(customer.getCustomerId());
        customerLevelMapByCustomerIdAndStoreIdsRequest.setStoreIds(storeIds);
        BaseResponse<CustomerLevelMapGetResponse> customerLevelMapGetResponseBaseResponse = customerLevelQueryProvider.listCustomerLevelMapByCustomerIdAndIds(customerLevelMapByCustomerIdAndStoreIdsRequest);
        return customerLevelMapGetResponseBaseResponse.getContext().getCommonLevelVOMap();
    }

    /**
     * 获取会员等级
     *
     * @param storeIds 店铺列表
     * @param customer 客户
     */
    public HashMap<Long, CommonLevelVO> getCustomerLevelsByStoreIds(List<Long> storeIds, CustomerVO customer) {
        if (customer == null || CollectionUtils.isEmpty(storeIds)) {
            return new HashMap<>();
        }

        CustomerLevelMapByCustomerIdAndStoreIdsRequest customerLevelMapByCustomerIdAndStoreIdsRequest = new CustomerLevelMapByCustomerIdAndStoreIdsRequest();
        customerLevelMapByCustomerIdAndStoreIdsRequest.setCustomerId(customer.getCustomerId());
        customerLevelMapByCustomerIdAndStoreIdsRequest.setStoreIds(storeIds);
        BaseResponse<CustomerLevelMapGetResponse> customerLevelMapGetResponseBaseResponse = customerLevelQueryProvider.listCustomerLevelMapByCustomerIdAndIds(customerLevelMapByCustomerIdAndStoreIdsRequest);
        return customerLevelMapGetResponseBaseResponse.getContext().getCommonLevelVOMap();
    }

    /**
     * 获取营销
     *
     * @param goodsInfoList 商品
     */
    public Map<String, List<MarketingResponse>> getMarketing(List<GoodsInfoVO> goodsInfoList, Map<Long, CommonLevelVO>
            levelMap, CustomerDTO customerDTO) {
        // 如果是商品积分兑换--积分价不计算-(满减＞折＞赠＞打包一口价＞第二件半价生效)
        goodsInfoList = goodsInfoList.stream().filter(goodsInfoVO ->
                Objects.isNull(goodsInfoVO.getBuyPoint()) || (goodsInfoVO.getBuyPoint().compareTo(0L) == 0)
        ).collect(Collectors.toList());

        // 排除预售预约
        LocalDateTime now = LocalDateTime.now();
        goodsInfoList = goodsInfoList.stream().filter(goodsInfoVO -> {
                    BookingSaleVO bookingSaleVO = goodsInfoVO.getBookingSaleVO();
                    //定金预售定金支付时间之外的不排除
                    boolean isBookingSale = Objects.nonNull(bookingSaleVO) && !(1 == bookingSaleVO.getBookingType()
                            && (now.isBefore(bookingSaleVO.getHandSelStartTime()) || now.isAfter(bookingSaleVO.getHandSelEndTime())));
                    if (Objects.isNull(goodsInfoVO.getAppointmentSaleVO()) && !isBookingSale) {
                        return true;
                    }
                    return false;
                }
        ).collect(Collectors.toList());

        //查询正在进行中的有效营销信息
        Map<String, List<MarketingResponse>> marketingMap = marketingService.getMarketingMapByGoodsId(
                MarketingRequest.builder()
                        .goodsInfoIdList(goodsInfoList.stream().map(GoodsInfoVO::getGoodsInfoId).distinct().collect(Collectors.toList()))
                        .deleteFlag(DeleteFlag.NO)
                        .cascadeLevel(true)
                        .marketingStatus(MarketingStatus.STARTED).build());

        Map<String, Long> goodsMap = goodsInfoList.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, GoodsInfoVO::getStoreId, (s, a) -> s));
        List<StoreVO> storeVOList;
        if (levelMap.isEmpty()) {
            List<Long> storeIds = goodsInfoList.stream().map(GoodsInfoVO::getStoreId).distinct().collect(Collectors.toList());
            storeVOList = storeQueryProvider.listNoDeleteStoreByIds(ListNoDeleteStoreByIdsRequest.builder().storeIds(storeIds).build()).getContext().getStoreVOList();
        } else {
            storeVOList = new ArrayList<>();
        }
        Map<Long, StoreVO> storeVOMap = storeVOList.stream().collect(Collectors.toMap(StoreVO::getStoreId, Function.identity()));
        Map<String, List<MarketingResponse>> newMarketingMap = new HashMap<>();
        if (MapUtils.isNotEmpty(marketingMap)) {
            marketingMap.forEach((skuId, marketingList) -> {
                if (goodsMap.containsKey(skuId)) {
                    CommonLevelVO level = levelMap.get(goodsMap.get(skuId));
                    //过滤出符合等级条件的营销信息
                    newMarketingMap.put(skuId, marketingList.stream().filter(marketing -> {
                        //会员未登录状态下，对第三方商家全平台及自营商家全等级的营销是可见的

                        if(String.valueOf(MarketingJoinLevel.PAID_CARD_CUSTOMER.toValue()).equals(marketing.getJoinLevel()) && Objects.nonNull(customerDTO)) {
                            //付费会员
                            List<PaidCardCustomerRelVO> relVOList = paidCardCustomerRelQueryProvider
                                    .list(PaidCardCustomerRelListRequest.builder()
                                            .delFlag(DeleteFlag.NO)
                                            .endTimeBegin(LocalDateTime.now())
                                            .customerId(customerDTO.getCustomerId()).build())
                                    .getContext().getPaidCardCustomerRelVOList();
                            if(CollectionUtils.isNotEmpty(relVOList)) {
                                return true;
                            }
                        } if(String.valueOf(MarketingJoinLevel.ENTERPRISE_CUSTOMER.toValue()).equals(marketing.getJoinLevel())
                                && Objects.nonNull(customerDTO) && EnterpriseCheckState.CHECKED.equals(customerDTO.getEnterpriseCheckState())) {
                            //企业会员
                            return true;
                        } else if ("-1".equals(marketing.getJoinLevel())) {
                            //全平台
                            return true;
                        } else if (levelMap.isEmpty()) {
                            StoreVO storeVO = storeVOMap.get(goodsMap.get(skuId));
                            return BoolFlag.NO == storeVO.getCompanyType() && "0".equals(marketing.getJoinLevel());
                        } else if (Objects.nonNull(level)) {
                            //不限等级
                            if ("0".equals(marketing.getJoinLevel())) {
                                return true;
                            } else if (Arrays.asList(marketing.getJoinLevel().split(",")).contains(String.valueOf(level.getLevelId()))) {
                                return true;
                            }
                        }
                        return false;
                    }).collect(Collectors.toList()));
                }
            });
        }
        return newMarketingMap;
    }
}
