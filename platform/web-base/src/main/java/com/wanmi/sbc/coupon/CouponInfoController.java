package com.wanmi.sbc.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.coupon.request.CouponGoodsPageRequest;
import com.wanmi.sbc.coupon.response.CouponGoodsPageResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.distribute.DistributionCacheService;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsInfoResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.intervalprice.GoodsIntervalPriceService;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCacheProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCacheCenterPageRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCacheListForGoodsDetailRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCacheListForGoodsListRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponGoodsListRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCacheCenterPageResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCacheListForGoodsListResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponGoodsListResponse;
import com.wanmi.sbc.marketing.bean.enums.CouponSceneType;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseProvider;
import com.wanmi.sbc.order.api.request.purchase.PurchaseFillBuyCountRequest;
import com.wanmi.sbc.system.service.SystemPointsConfigService;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @menu Mobile优惠券
 * @tag coupon-info
 * @status undone
 */
@RestController
@RequestMapping("/coupon-info")
@Api(tags = "CouponInfoController", description = "S2B web公用-优惠券信息API")
public class CouponInfoController {

    @Autowired
    private CouponCacheProvider couponCacheProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    @Autowired
    private PurchaseProvider purchaseProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private GoodsIntervalPriceService goodsIntervalPriceService;

    @Autowired
    private DistributionCacheService distributionCacheService;

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;


    /**
     * @description 未登录时，领券中心列表
     * @menu Mobile优惠券
     * @param queryRequest
     * @status undone
     */
    @ApiOperation(value = "未登录时，领券中心列表")
    @RequestMapping(value = "/front/center", method = RequestMethod.POST)
    public BaseResponse<CouponCacheCenterPageResponse> getCouponStartedFront(@RequestBody CouponCacheCenterPageRequest queryRequest) {

        return couponCacheProvider.pageCouponStarted(queryRequest);
    }


    /**
     * @description 登录时，领券中心列表
     * @menu Mobile优惠券
     * @param queryRequest
     * @status undone
     */
    @ApiOperation(value = "登录后，领券中心列表")
    @RequestMapping(value = "/center", method = RequestMethod.POST)
    public BaseResponse<CouponCacheCenterPageResponse> getCouponStarted(@RequestBody CouponCacheCenterPageRequest queryRequest) {
        queryRequest.setCustomerId(commonUtil.getOperatorId());
        return couponCacheProvider.pageCouponStarted(queryRequest);
    }

    /**
     * 未登录时,通过商品id列表，查询与商品相关优惠券
     * 购物车 - 优惠券列表
     *
     * @return
     */
    @ApiOperation(value = "未登录时，通过商品id列表，查询与商品相关优惠券")
    @RequestMapping(value = "/front/goods-list", method = RequestMethod.POST)
    public BaseResponse<CouponCacheListForGoodsListResponse> listCouponForGoodsListFront(@RequestBody List<String> goodsInfoIds,@RequestParam("couponScene")Integer couponScene) {
        CouponCacheListForGoodsListRequest request = new CouponCacheListForGoodsListRequest();
        request.setGoodsInfoIds(goodsInfoIds);
        return couponCacheProvider.listCouponForGoodsList(request);
    }



    /**
     * 通过商品id列表，查询与商品相关优惠券
     * 购物车 - 优惠券列表
     *
     * @return
     */
    @ApiOperation(value = "登录后，通过商品id列表，查询与商品相关优惠券")
    @RequestMapping(value = "/goods-list", method = RequestMethod.POST)
    public BaseResponse<CouponCacheListForGoodsListResponse> listCouponForGoodsList(@RequestBody List<String> goodsInfoIds) {
        CouponCacheListForGoodsListRequest request = new CouponCacheListForGoodsListRequest();
        request.setGoodsInfoIds(goodsInfoIds);
        request.setCustomerId(commonUtil.getOperatorId());
        return couponCacheProvider.listCouponForGoodsList(request);
    }



    /**
     * @description 新-未登录时,通过商品id列表，查询与商品相关优惠
     * @menu Mobile优惠券
     * @param request
     * @status undone
     */
    @ApiOperation(value = "未登录时，通过商品id列表，查询与商品相关优惠券")
    @RequestMapping(value = "/front/goodslist", method = RequestMethod.POST)
    public BaseResponse<CouponCacheListForGoodsListResponse> listCouponForGoodsListFrontNew(@RequestBody CouponCacheListForGoodsListRequest request) {
        return couponCacheProvider.listCouponForGoodsList(request);
    }



    /**
     * @description 新-登录时,通过商品id列表，查询与商品相关优惠-新
     * @menu Mobile优惠券
     * @param request
     * @status undone
     */
    @ApiOperation(value = "登录后，通过商品id列表，查询与商品相关优惠券")
    @RequestMapping(value = "/goodslist", method = RequestMethod.POST)
    public BaseResponse<CouponCacheListForGoodsListResponse> listCouponForGoodsListNew(@RequestBody CouponCacheListForGoodsListRequest request) {
        request.setCustomerId(commonUtil.getOperatorId());
        return couponCacheProvider.listCouponForGoodsList(request);
    }

    /**
     * 未登录时,通过商品Id，查询单个商品相关优惠券
     *
     * @return
     */
    @ApiOperation(value = "未登录时，通过商品Id，查询单个商品相关优惠券")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "goodsInfoId", value = "商品Id", required = true)
    @RequestMapping(value = "/front/goods-detail/{goodsInfoId}", method = RequestMethod.GET)
    public BaseResponse<CouponCacheListForGoodsListResponse> listCouponForGoodsDetailFront(@PathVariable String goodsInfoId) {

        CouponCacheListForGoodsDetailRequest request = new CouponCacheListForGoodsDetailRequest();
        request.setGoodsInfoId(goodsInfoId);
        CouponCacheListForGoodsListResponse goodsDetailResponse =
                couponCacheProvider.listCouponForGoodsDetail(request).getContext();

        return BaseResponse.success(goodsDetailResponse);
    }

    /**
     * 通过商品Id，查询单个商品相关优惠券
     *
     * @return
     */
    @ApiOperation(value = "登录后，通过商品Id，查询单个商品相关优惠券")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "goodsInfoId", value = "商品Id", required = true)
    @RequestMapping(value = "/goods-detail/{goodsInfoId}", method = RequestMethod.GET)
    public BaseResponse<CouponCacheListForGoodsListResponse> listCouponForGoodsDetail(@PathVariable String goodsInfoId) {
        CouponCacheListForGoodsDetailRequest request = new CouponCacheListForGoodsDetailRequest();
        request.setGoodsInfoId(goodsInfoId);
        request.setCustomerId(commonUtil.getOperatorId());
        return couponCacheProvider.listCouponForGoodsDetail(request);
    }

    /**
     * 优惠券凑单页
     *
     * @return
     */
    @ApiOperation(value = "优惠券凑单页")
    @RequestMapping(value = "/coupon-goods", method = RequestMethod.POST)
    public BaseResponse<CouponGoodsPageResponse> listGoodsByCouponId(@RequestBody CouponGoodsPageRequest request) {

        CouponGoodsPageResponse couponGoodsPageResponse = new CouponGoodsPageResponse();
        EsGoodsInfoResponse esGoodsInfoResponse = new EsGoodsInfoResponse();
        // 列表排序默认按最新上架的SKU排序
        // 凑单页面的筛选条件排序按照 默认、最新、价格排序
        CouponGoodsListRequest requ = CouponGoodsListRequest.builder()
                .activityId(request.getActivity())
                .customerId(commonUtil.getOperatorId())
                .couponId(request.getCouponId()).build();
        BaseResponse<CouponGoodsListResponse> baseResponsequeryResponse = couponCacheProvider.listGoodsByCouponId(requ);

        CouponGoodsListResponse queryResponse = baseResponsequeryResponse.getContext();

        couponGoodsPageResponse.setCouponInfo(queryResponse);
        if (CollectionUtils.isNotEmpty(queryResponse.getBrandIds()) && CollectionUtils.isEmpty(queryResponse.getQueryBrandIds())) {
            couponGoodsPageResponse.setEsGoodsInfoResponse(esGoodsInfoResponse);
            return BaseResponse.success(couponGoodsPageResponse);
        }

        EsGoodsInfoQueryRequest esGoodsInfoQueryRequest = new EsGoodsInfoQueryRequest();
        esGoodsInfoQueryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        esGoodsInfoQueryRequest.setStoreState(StoreState.OPENING.toValue());
        esGoodsInfoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
        esGoodsInfoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        esGoodsInfoQueryRequest.setSortFlag(request.getSortType() == null ? 0 : request.getSortType());
        esGoodsInfoQueryRequest.setPageNum(request.getPageNum());
        esGoodsInfoQueryRequest.setPageSize(request.getPageSize());
        esGoodsInfoQueryRequest.setCateAggFlag(true);
        esGoodsInfoQueryRequest.setStoreId(queryResponse.getStoreId());
        esGoodsInfoQueryRequest.setCompanyType(request.getCompanyType());
        String now = DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4);
        esGoodsInfoQueryRequest.setContractStartDate(now);
        esGoodsInfoQueryRequest.setContractEndDate(now);
        esGoodsInfoQueryRequest.setVendibility(Constants.yes);

        esGoodsInfoQueryRequest.setSortFlag(request.getSortFlag());
        if (CollectionUtils.isNotEmpty(request.getCateIds())) {
            esGoodsInfoQueryRequest.setCateIds(request.getCateIds());
        }
        if (CollectionUtils.isNotEmpty(request.getBrandIds())) {
            esGoodsInfoQueryRequest.setBrandIds(request.getBrandIds());
        }

        switch (queryResponse.getScopeType()) {
            case ALL:
                break;
            case BOSS_CATE:
                if (CollectionUtils.isEmpty(esGoodsInfoQueryRequest.getCateIds()) && CollectionUtils.isNotEmpty(queryResponse.getCateIds())) {
                    esGoodsInfoQueryRequest.setCateIds(queryResponse.getCateIds4es());
                }
                break;
            case BRAND:
                if (CollectionUtils.isEmpty(esGoodsInfoQueryRequest.getBrandIds()) && CollectionUtils.isNotEmpty(queryResponse.getQueryBrandIds())) {
                    esGoodsInfoQueryRequest.setBrandIds(queryResponse.getQueryBrandIds());
                }
                break;
            case SKU:
                if (CollectionUtils.isNotEmpty(queryResponse.getGoodsInfoId())) {
                    esGoodsInfoQueryRequest.setGoodsInfoIds(queryResponse.getGoodsInfoId());
                }
                break;
            case STORE_CATE:
                if (CollectionUtils.isNotEmpty(queryResponse.getStoreCateIds())) {
                    esGoodsInfoQueryRequest.setStoreCateIds(new ArrayList(queryResponse.getStoreCateIds()));
                }
                break;
            default:
                break;
        }
        // 优惠券凑单页过滤分销商品
//        if (!Objects.equals(ChannelType.PC_MALL, commonUtil.getDistributeChannel().getChannelType()) && DefaultFlag.YES.equals(distributionCacheService.queryOpenFlag())) {
//            esGoodsInfoQueryRequest.setExcludeDistributionGoods(Boolean.TRUE);
//        }

        esGoodsInfoResponse = esGoodsInfoElasticQueryProvider.page(esGoodsInfoQueryRequest).getContext();
        List<EsGoodsInfoVO> goodsInfoVOs = esGoodsInfoResponse.getEsGoodsInfoPage().getContent();

        if (CollectionUtils.isNotEmpty(goodsInfoVOs)) {
            //未开启商品抵扣时，清零buyPoint
            systemPointsConfigService.clearBuyPoinsForEsSku(goodsInfoVOs);
            //获取会员和等级
            CustomerVO customer = commonUtil.getCustomer();

            //组装优惠券标签
            List<GoodsInfoVO> goodsInfoList = goodsInfoVOs.stream().filter(e -> Objects.nonNull(e.getGoodsInfo()))
                    .map(e -> KsBeanUtil.convert(e.getGoodsInfo(), GoodsInfoVO.class))
                    .collect(Collectors.toList());

            //计算营销
            MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
            filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfoList, GoodsInfoDTO.class));
            filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
            goodsInfoList = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();

            //计算区间价
            GoodsIntervalPriceByCustomerIdResponse priceResponse =
                    goodsIntervalPriceService.getGoodsIntervalPriceVOList(goodsInfoList, customer.getCustomerId());
            esGoodsInfoResponse.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
            goodsInfoList = priceResponse.getGoodsInfoVOList();

            //填充
            PurchaseFillBuyCountRequest purchaseFillBuyCountRequest = new PurchaseFillBuyCountRequest();
            purchaseFillBuyCountRequest.setCustomerId(commonUtil.getOperatorId());
            purchaseFillBuyCountRequest.setGoodsInfoList(goodsInfoList);
            goodsInfoList = purchaseProvider.fillBuyCount(purchaseFillBuyCountRequest).getContext().getGoodsInfoList();

            List<String> goodsIds = goodsInfoVOs.stream().map(EsGoodsInfoVO::getGoodsId).collect(Collectors.toList());
            List<GoodsVO> goodsVOList = goodsQueryProvider.listByIds(GoodsListByIdsRequest.builder().goodsIds(goodsIds).build()).getContext().getGoodsVOList();

            //重新赋值于Page内部对象
            Map<String, GoodsInfoVO> voMap = goodsInfoList.stream()
                    .collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> g));
            esGoodsInfoResponse.getEsGoodsInfoPage().getContent().forEach(esGoodsInfo -> {
                GoodsInfoVO vo = voMap.get(esGoodsInfo.getGoodsInfo().getGoodsInfoId());
                if (Objects.nonNull(vo)) {
                    esGoodsInfo.setGoodsInfo(KsBeanUtil.convert(vo, GoodsInfoNestVO.class));
                }

                goodsVOList.forEach(goodsVO -> {
                    if (Objects.equals(esGoodsInfo.getGoodsId(),goodsVO.getGoodsId()) && StringUtils.isNotBlank(goodsVO.getGoodsSubtitle())) {
                        esGoodsInfo.setGoodsSubtitle(goodsVO.getGoodsSubtitle());
                    }
                });
            });

            couponGoodsPageResponse.setEsGoodsInfoResponse(esGoodsInfoResponse);
            return BaseResponse.success(couponGoodsPageResponse);
        } else {
            couponGoodsPageResponse.setEsGoodsInfoResponse(esGoodsInfoResponse);
            return BaseResponse.success(couponGoodsPageResponse);
        }
    }
}
