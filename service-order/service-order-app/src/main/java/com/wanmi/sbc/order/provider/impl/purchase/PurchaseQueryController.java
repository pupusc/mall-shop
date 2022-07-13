package com.wanmi.sbc.order.provider.impl.purchase;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DistributionCommissionUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributorLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerEnableByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelByCustomerIdRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerEnableByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelByCustomerIdResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsMarketingVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseQueryProvider;
import com.wanmi.sbc.order.api.request.purchase.Purchase4DistributionRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseCountGoodsRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseFrontMiniRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseFrontRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseGetGoodsMarketingRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseGetStoreCouponExistRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseGetStoreMarketingRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseInfoRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseListRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseMiniListRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseQueryGoodsMarketingListRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseQueryRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseSaveRequest;
import com.wanmi.sbc.order.api.request.purchase.ValidateAndSetGoodsMarketingsRequest;
import com.wanmi.sbc.order.api.response.purchase.MiniPurchaseResponse;
import com.wanmi.sbc.order.api.response.purchase.Purchase4DistributionResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseCountGoodsResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseGetGoodsMarketingResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseGetStoreCouponExistResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseGetStoreMarketingResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseListResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseMarketingCalcResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseMiniListResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseQueryGoodsMarketingListResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseQueryResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseResponse;
import com.wanmi.sbc.order.bean.vo.PurchaseMarketingCalcVO;
import com.wanmi.sbc.order.bean.vo.PurchaseVO;
import com.wanmi.sbc.order.constant.OrderErrorCode;
import com.wanmi.sbc.order.purchase.Purchase;
import com.wanmi.sbc.order.purchase.PurchaseService;
import com.wanmi.sbc.order.purchase.request.PurchaseRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-04
 */
@Validated
@RestController
public class PurchaseQueryController implements PurchaseQueryProvider {

    @Autowired
    private PurchaseService purchaseService;


    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Autowired
    private DistributorLevelQueryProvider distributorLevelQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    /**
     * @param request 查询迷你采购单请求结构 {@link PurchaseMiniListRequest}
     * @return
     */
    @Override
    public BaseResponse<PurchaseMiniListResponse> minilist(@RequestBody @Valid PurchaseMiniListRequest request) {
        MiniPurchaseResponse miniPurchaseResponse = purchaseService.miniList(KsBeanUtil.convert(request,
                PurchaseRequest.class),
                request.getCustomer());
        return BaseResponse.success(KsBeanUtil.convert(miniPurchaseResponse, PurchaseMiniListResponse.class));
    }

    /**
     * @param request 采购单列表请求结构 {@link PurchaseListRequest}
     * @return
     */
    @Override
    public BaseResponse<PurchaseListResponse> list(@RequestBody @Valid PurchaseListRequest request) {
        PurchaseResponse purchaseResponse = purchaseService.list(KsBeanUtil.convert(request, PurchaseRequest.class));
        return BaseResponse.success(KsBeanUtil.convert(purchaseResponse, PurchaseListResponse.class));
    }

    /**
     * @param request 查询采购单请求结构 {@link PurchaseQueryRequest}
     * @return
     */
    @Override
    public BaseResponse<PurchaseQueryResponse> query(@RequestBody @Valid PurchaseQueryRequest request) {
        if(Objects.nonNull(request.getMarketingId())) {
            Long marketingId = request.getMarketingId();
            List<GoodsMarketingVO> goodsMarketingVOS = purchaseService.queryGoodsMarketingList(request.getCustomerId());
            if(CollectionUtils.isNotEmpty(goodsMarketingVOS)) {
                List<GoodsMarketingVO> newList = goodsMarketingVOS.stream().filter(vo -> vo.getMarketingId().equals(marketingId)).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(newList)) {
                    List<String> goodsIds = newList.stream().map(GoodsMarketingVO::getGoodsInfoId).collect(Collectors.toList());
                    request.setGoodsInfoIds(request.getGoodsInfoIds().stream().filter(id -> goodsIds.contains(id)).collect(Collectors.toList()));
                } else {
                    return BaseResponse.success(new PurchaseQueryResponse());
                }
            }
        }
        if(CollectionUtils.isEmpty(request.getGoodsInfoIds())) {
            return BaseResponse.success(new PurchaseQueryResponse());
        }
        List<Purchase> purchaseList = purchaseService.queryPurchase(request.getCustomerId(), request.getGoodsInfoIds
                (), request.getInviteeId());
        PurchaseQueryResponse purchaseQueryResponse = new PurchaseQueryResponse();
        purchaseQueryResponse.setPurchaseList(KsBeanUtil.convertList(purchaseList, PurchaseVO.class));
        return BaseResponse.success(purchaseQueryResponse);
    }

    /**
     * @param request 获取店铺下，是否有优惠券营销，展示优惠券标签请求结构 {@link PurchaseGetStoreCouponExistRequest}
     * @return
     */
    @Override
    public BaseResponse<PurchaseGetStoreCouponExistResponse> getStoreCouponExist(@RequestBody @Valid
                                                                                             PurchaseGetStoreCouponExistRequest request) {

        List<Purchase> purchases = purchaseService.queryPurchase(request.getCustomer().getCustomerId(), null, request.getInviteeId());
        if (CollectionUtils.isEmpty(purchases)) return BaseResponse.success(new PurchaseGetStoreCouponExistResponse());

        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByIds(GoodsInfoListByIdsRequest.builder().goodsInfoIds(
                purchases.stream().map(i -> i.getGoodsInfoId()).collect(Collectors.toList())
        ).build()).getContext().getGoodsInfos();

        Map<Long, Boolean> storeCouponExist = purchaseService.getStoreCouponExist(KsBeanUtil.convertList(goodsInfos, GoodsInfoVO.class),
                KsBeanUtil.convert(request.getCustomer(), CustomerVO.class));
        PurchaseGetStoreCouponExistResponse purchaseGetStoreCouponExistResponse = new
                PurchaseGetStoreCouponExistResponse();
        HashMap<Long, Boolean> map = new HashMap<>();
        storeCouponExist.forEach(map::put);
        purchaseGetStoreCouponExistResponse.setMap(map);
        return BaseResponse.success(purchaseGetStoreCouponExistResponse);
    }

    /**
     * @param request 获取店铺营销信息请求结构 {@link PurchaseGetStoreMarketingRequest}
     * @return
     */
    @Override
    public BaseResponse<PurchaseGetStoreMarketingResponse> getStoreMarketing(@RequestBody @Valid
                                                                                         PurchaseGetStoreMarketingRequest request) {
        Map<Long, List<PurchaseMarketingCalcResponse>> storeMarketing = purchaseService.getStoreMarketingBase(request
                        .getGoodsMarketings(),
                KsBeanUtil.convert(request.getCustomer(), CustomerVO.class), request.getFrontReq(), request
                        .getGoodsInfoIdList());
        HashMap<Long, List<PurchaseMarketingCalcVO>> map = new HashMap<>();
        storeMarketing.forEach((k, v) -> {
            map.put(k, KsBeanUtil.convertList(v, PurchaseMarketingCalcVO.class));
        });
        PurchaseGetStoreMarketingResponse purchaseGetStoreMarketingResponse = new PurchaseGetStoreMarketingResponse();
        purchaseGetStoreMarketingResponse.setMap(map);
        return BaseResponse.success(purchaseGetStoreMarketingResponse);
    }

    /**
     * @param request 获取采购单商品选择的营销请求结构 {@link PurchaseQueryGoodsMarketingListRequest}
     * @return
     */
    @Override
    public BaseResponse<PurchaseQueryGoodsMarketingListResponse> queryGoodsMarketingList(@RequestBody @Valid
                                                                                                     PurchaseQueryGoodsMarketingListRequest request) {
        List<GoodsMarketingVO> goodsMarketingVOS = purchaseService.queryGoodsMarketingList(request.getCustomerId());
        PurchaseQueryGoodsMarketingListResponse purchaseQueryGoodsMarketingListResponse = new
                PurchaseQueryGoodsMarketingListResponse();
        purchaseQueryGoodsMarketingListResponse.setGoodsMarketingList(goodsMarketingVOS);
        return BaseResponse.success(purchaseQueryGoodsMarketingListResponse);
    }

    /**
     * @param request 获取商品营销信息请求结构 {@link PurchaseGetGoodsMarketingRequest}
     * @return
     */
    @Override
    public BaseResponse<PurchaseGetGoodsMarketingResponse> getGoodsMarketing(@RequestBody @Valid
                                                                                         PurchaseGetGoodsMarketingRequest request) {
        List<GoodsInfoVO> goodsInfos = request.getGoodsInfos();
        return BaseResponse.success(purchaseService.getGoodsMarketing(goodsInfos, request.getCustomer()));
    }

    /**
     * @param request 获取采购单商品数量请求结构 {@link PurchaseCountGoodsRequest}
     * @return
     */
    @Override
    public BaseResponse<PurchaseCountGoodsResponse> countGoods(@RequestBody @Valid PurchaseCountGoodsRequest request) {
        PurchaseCountGoodsResponse purchaseCountGoodsResponse = new PurchaseCountGoodsResponse();
        purchaseCountGoodsResponse.setTotal(0);

        if (StringUtils.isBlank(request.getGoodsChannelType())) {
            purchaseCountGoodsResponse.setTotal(purchaseService.countGoods(request.getCustomerId(), request.getInviteeId()));
            return BaseResponse.success(purchaseCountGoodsResponse);
        }

        //数量要根据不同的channel判断
        List<Purchase> purchases = purchaseService.queryPurchase(request.getCustomerId(), Collections.EMPTY_LIST, request.getInviteeId());
        if (CollectionUtils.isEmpty(purchases)) {
            return BaseResponse.success(purchaseCountGoodsResponse);
        }

        List<String> spuIds = purchases.stream().map(Purchase::getGoodsId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(spuIds)) {
            return BaseResponse.success(purchaseCountGoodsResponse);
        }
        GoodsListByIdsResponse goodsResult = goodsQueryProvider.listByIds(GoodsListByIdsRequest.builder().goodsIds(spuIds).build()).getContext();
        if (goodsResult == null || CollectionUtils.isEmpty(goodsResult.getGoodsVOList())) {
            return BaseResponse.success(purchaseCountGoodsResponse);
        }

        List<String> hitIds = goodsResult.getGoodsVOList().stream()
                .filter(i -> i.getGoodsChannelType() != null && Arrays.asList(i.getGoodsChannelType().split(",")).contains(request.getGoodsChannelType()))
                .map(GoodsVO::getGoodsId).distinct().collect(Collectors.toList());

        purchaseCountGoodsResponse.setTotal((int) purchases.stream().filter(i->hitIds.contains(i.getGoodsId())).count());
        return BaseResponse.success(purchaseCountGoodsResponse);
    }

    /**
     * 未登录时,根据前端缓存信息查询迷你购物车信息
     *
     * @param frontReq
     * @return
     */
    @Override
    public BaseResponse<MiniPurchaseResponse> miniListFront(@RequestBody @Valid PurchaseFrontMiniRequest frontReq) {
        MiniPurchaseResponse response = purchaseService.miniListFront(frontReq);
        return BaseResponse.success(response);
    }

    /**
     * 未登陆时,根据前端传入的采购单信息,查询组装必要信息
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<PurchaseResponse> listFront(@RequestBody @Valid PurchaseFrontRequest request) {
        PurchaseResponse response = purchaseService.listFront(request);
        return BaseResponse.success(response);
    }

    /**
     * 未登录时,验证并设置前端传入的商品使用营销信息
     *
     * @return
     */
    @Override
    public BaseResponse<PurchaseResponse> validateAndSetGoodsMarketings(@RequestBody @Valid
                                                                                    ValidateAndSetGoodsMarketingsRequest request) {
        PurchaseResponse response = purchaseService.validateAndSetGoodsMarketings(
                request.getResponse(), request.getGoodsMarketingDTOList());
        return BaseResponse.success(response);
    }

    /**
     * 采购单社交分销信息
     *
     * @param request
     * @return <p>
     * 1.如果为社交分销    验证分销开关   关 显示营销信息
     * * 如果为社交分销    验证分销开关   开 显示分销价 非自购  分销员状态启用  不显示返利
     * 开 显示分销价 自购 分销员状态启用     显示返利
     * 分销员状态禁用    不显示返利
     * <p>
     * 2.如果为店铺精选购买  验证店铺状态
     * 验证分销店铺商品
     * 3.分销商品去除阶梯价等信息
     * 4.分销价叠加分销员等级
     */
    @Override
    public BaseResponse<Purchase4DistributionResponse> distribution(@RequestBody @Valid Purchase4DistributionRequest
                                                                                request) {
        DistributeChannel channel = request.getDistributeChannel();
        List<GoodsInfoVO> goodsInfoVOList = request.getGoodsInfos();
        List<GoodsInfoVO> goodsInfoComList = request.getGoodsInfos();
        List<GoodsIntervalPriceVO> goodsIntervalPrices = request.getGoodsIntervalPrices();
        CustomerVO customer = request.getCustomer();
        Purchase4DistributionResponse response = Purchase4DistributionResponse.builder().goodsInfos(goodsInfoVOList)
                .goodsInfoComList(goodsInfoComList).build();

        //1.如果为社交分销渠道
        if (null != channel && !Objects.equals(channel.getChannelType(), ChannelType.PC_MALL)) {
            response.setSelfBuying(Boolean.FALSE);
            //分销商品
            List<GoodsInfoVO> goodsInfoDistributionList = goodsInfoVOList.stream().filter(goodsInfo -> Objects
                    .equals(goodsInfo.getDistributionGoodsAudit(), DistributionGoodsAudit.CHECKED)).collect
                    (Collectors.toList());
            //验证自购
            if ((Objects.equals(channel.getInviteeId(), Constants.PURCHASE_DEFAULT) || Objects.isNull(channel
                    .getInviteeId())) && Objects.nonNull(customer)) {
                DistributionCustomerEnableByCustomerIdResponse customerEnableByCustomerIdResponse =
                        distributionCustomerQueryProvider.checkEnableByCustomerId
                                (DistributionCustomerEnableByCustomerIdRequest.builder().customerId(customer
                                        .getCustomerId()).build()).getContext();
                response.setSelfBuying(customerEnableByCustomerIdResponse.getDistributionEnable() && CollectionUtils
                        .isNotEmpty(goodsInfoDistributionList));
            }
            //排除分销商品
            if (channel.getChannelType() == ChannelType.SHOP) {
                goodsInfoComList = new ArrayList<>();
            } else {
                goodsInfoComList = goodsInfoVOList.stream().filter(goodsInfo -> !Objects.equals(goodsInfo
                        .getDistributionGoodsAudit(), DistributionGoodsAudit.CHECKED)).collect(Collectors.toList());
            }
            //3.分销商品去除阶梯价等信息
            goodsIntervalPrices = setDistributorPrice(goodsInfoVOList, goodsIntervalPrices);

            //2.如果为店铺精选购买
            purchaseService.verifyDistributorGoodsInfo(channel, goodsInfoVOList);

            //4.分销价叠加分销员等级
            if (Objects.nonNull(customer)) {
                BaseResponse<DistributorLevelByCustomerIdResponse> resultBaseResponse =
                        distributorLevelQueryProvider.getByCustomerId(new DistributorLevelByCustomerIdRequest
                                (customer.getCustomerId()));
                DistributorLevelVO distributorLevelVO = Objects.isNull(resultBaseResponse) ? null :
                        resultBaseResponse.getContext().getDistributorLevelVO();
                if (Objects.nonNull(distributorLevelVO) && Objects.nonNull(distributorLevelVO.getCommissionRate())) {
                    goodsInfoVOList.stream().forEach(goodsInfoVO -> {
                        if (DistributionGoodsAudit.CHECKED.equals(goodsInfoVO.getDistributionGoodsAudit())) {
                            BigDecimal commissionRate = distributorLevelVO.getCommissionRate();
                            BigDecimal distributionCommission = goodsInfoVO.getDistributionCommission();
                            distributionCommission = DistributionCommissionUtils.calDistributionCommission(distributionCommission,commissionRate);
                            goodsInfoVO.setDistributionCommission(distributionCommission);
                        }
                    });
                }
            }
        }
        response.setGoodsInfoComList(goodsInfoComList);
        response.setGoodsInfos(goodsInfoVOList);
        response.setGoodsIntervalPrices(goodsIntervalPrices);
        return BaseResponse.success(response);
    }


    /**
     * 分销商品去除阶梯价等信息
     *
     * @param goodsInfoVOList
     */
    private List<GoodsIntervalPriceVO> setDistributorPrice(List<GoodsInfoVO> goodsInfoVOList,
                                                           List<GoodsIntervalPriceVO> goodsIntervalPrices) {
        //        分销商品
        List<GoodsInfoVO> goodsInfoDistributionList = goodsInfoVOList.stream().filter(goodsInfo -> Objects.equals
                (goodsInfo.getDistributionGoodsAudit(), DistributionGoodsAudit.CHECKED)).collect(Collectors.toList());
        List<String> skuIdList = goodsInfoDistributionList.stream().map(GoodsInfoVO::getGoodsInfoId).collect
                (Collectors.toList());

        goodsInfoVOList.forEach(goodsInfo -> {
            if (skuIdList.contains(goodsInfo.getGoodsInfoId())) {
                goodsInfo.setIntervalPriceIds(null);
                goodsInfo.setIntervalMinPrice(null);
                goodsInfo.setIntervalMaxPrice(null);
                goodsInfo.setCount(null);
                goodsInfo.setMaxCount(null);
            }
        });
        return goodsIntervalPrices.stream().filter(intervalPrice -> !skuIdList.contains(intervalPrice.getGoodsInfoId
                ())).collect(Collectors.toList());
    }



    /**
     * 校验商品是否允许加入购物车
     *
     * @param request
     */
    @Override
    public BaseResponse checkAdd(PurchaseSaveRequest request) {
        String goodsInfoId = request.getGoodsInfoId();
        //根据goodsinfoid查询商品详情
        GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(goodsInfoId).build
                ()).getContext();

        if (goodsInfo.getGoodsType() == GoodsType.CYCLE_BUY.ordinal()) {
            throw new SbcRuntimeException(OrderErrorCode.PURCHASE_GOODS_NOT_ALLOWED);
        }
        return BaseResponse.SUCCESSFUL();

    }

    /**
     * 查询购物车信息
     * @param request
     * @return
     */
    @Override
    public BaseResponse<PurchaseListResponse> purchaseInfo(@RequestBody @Valid PurchaseInfoRequest request) {
        return BaseResponse.success(purchaseService.purchaseInfo(request));
    }
}
