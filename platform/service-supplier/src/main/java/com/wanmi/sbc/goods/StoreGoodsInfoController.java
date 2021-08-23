package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.constant.CustomerErrorCode;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.groupon.EsGrouponActivityQueryProvider;
import com.wanmi.sbc.elastic.api.provider.sku.EsSkuQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoListRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoModifyAddedStatusRequest;
import com.wanmi.sbc.elastic.api.request.groupon.EsGrouponActivityPageRequest;
import com.wanmi.sbc.elastic.api.request.sku.EsSkuPageRequest;
import com.wanmi.sbc.elastic.api.response.sku.EsSkuPageResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.bookingsalegoods.BookingSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsaleactivity.FlashSaleActivityQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsQueryProvider;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.SynGoodsInfoRequest;
import com.wanmi.sbc.erp.api.response.SyncGoodsInfoResponse;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedsale.GoodsRestrictedSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleListRequest;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSalePageRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.flashsaleactivity.FlashSaleActivityListRequest;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedBatchValidateRequest;
import com.wanmi.sbc.goods.api.request.info.*;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSalePurchaseResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewListResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.enums.PresellSaleStatus;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.intervalprice.GoodsIntervalPriceService;
import com.wanmi.sbc.marketing.api.provider.markup.MarkupQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingIdRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupLevelByIdRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.bean.enums.AuditStatus;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityForManagerVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseQueryProvider;
import com.wanmi.sbc.order.api.request.purchase.PurchaseGetGoodsMarketingRequest;
import com.wanmi.sbc.order.api.response.purchase.PurchaseGetGoodsMarketingResponse;
import com.wanmi.sbc.system.service.SystemPointsConfigService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品服务
 * Created by daiyitian on 17/4/12.
 */
@Api(tags = "StoreGoodsInfoController", description = "商品服务 API")
@RestController
public class StoreGoodsInfoController {

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private GoodsIntervalPriceService goodsIntervalPriceService;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private MarketingLevelPluginProvider marketingLevelPluginProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private MarkupQueryProvider markupQueryProvider;

    @Autowired
    private PurchaseQueryProvider purchaseQueryProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private GoodsRestrictedSaleQueryProvider goodsRestrictedSaleQueryProvider;

    @Autowired
    private EsSkuQueryProvider esSkuQueryProvider;

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    @Autowired
    private BookingSaleGoodsQueryProvider bookingSaleGoodsQueryProvider;

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    @Autowired
    private FlashSaleActivityQueryProvider flashSaleActivityQueryProvider;
    @Autowired
    private FlashSaleGoodsQueryProvider flashSaleGoodsQueryProvider;
    @Autowired
    private EsGrouponActivityQueryProvider esGrouponActivityQueryProvider;

    private  Integer pageSize=1000;
    @Autowired
    private GuanyierpProvider guanyierpProvider;

    /**
     * 根据erp Spu编码查询sku列表
     * @param request
     * @return 商品详情
     */
    @ApiOperation(value = "根据erp Spu编码查询sku列表")
    @RequestMapping(value = "/erp/goods/syncGoodsInfo", method = RequestMethod.POST)
    public BaseResponse<SyncGoodsInfoResponse> syncGoodsInfo(@RequestBody SynGoodsInfoRequest request) {
        return guanyierpProvider.syncGoodsInfo(request);
    }

    /**
     * 分页显示商品
     *
     * @param queryRequest
     * @return 商品详情
     */
    @ApiOperation(value = "分页显示商品")
    @RequestMapping(value = "/goods/skus", method = RequestMethod.POST)
    public BaseResponse<EsSkuPageResponse> list(@RequestBody EsSkuPageRequest queryRequest) {
        queryRequest.setAddedFlag(AddedFlag.YES.toValue());//上架
        queryRequest.setAuditStatus(CheckStatus.CHECKED);//已审核
        return this.skuList(queryRequest);
    }

    /**
     * 分页显示商品，比上面的状态更灵活
     *
     * @param queryRequest 商品
     * @return 商品详情
     */
    @ApiOperation(value = "分页显示商品，比上面的状态更灵活")
    @RequestMapping(value = "/goods/list/sku", method = RequestMethod.POST)
    public BaseResponse<EsSkuPageResponse> skuList(@RequestBody EsSkuPageRequest queryRequest) {
        //获取会员
        CustomerGetByIdResponse customer = null;
        if (StringUtils.isNotBlank(queryRequest.getCustomerId())) {
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(queryRequest.getCustomerId())
            ).getContext();
            if (Objects.isNull(customer)) {
                throw new SbcRuntimeException(CustomerErrorCode.NOT_EXIST);
            }
        }

        // 代客下单时，积分开关开启 并且 积分使用方式是订单抵扣，此时不需要过滤积分价商品
        if (Boolean.TRUE.equals(queryRequest.getIntegralPriceFlag()) && !systemPointsConfigService.isGoodsPoint()) {
            queryRequest.setIntegralPriceFlag(Boolean.FALSE);
        }
        // 排除预售
        if(queryRequest.getBookingSale()){
            ArrayList<BookingSaleVO> bookingSaleVOList = new ArrayList<>();
            BookingSalePageRequest bookingSaleListRequest = BookingSalePageRequest.builder().storeId(commonUtil.getStoreId()).queryTab(PresellSaleStatus.STARTED).build();
            bookingSaleListRequest.setPageSize(pageSize);
            bookingSaleVOList.addAll( bookingSaleQueryProvider.page(bookingSaleListRequest).getContext().getBookingSaleVOPage().getContent());
            bookingSaleListRequest.setQueryTab(PresellSaleStatus.NOT_START);
            bookingSaleVOList.addAll( bookingSaleQueryProvider.page(bookingSaleListRequest).getContext().getBookingSaleVOPage().getContent());

            List<Long> bookingSalesId = bookingSaleVOList.stream().map(g -> g.getId()).collect(Collectors.toList());

            List<BookingSaleGoodsVO> bookingSaleGoodsVOList = bookingSaleGoodsQueryProvider.list(BookingSaleGoodsListRequest.builder().bookingSaleIdList(bookingSalesId).build()).getContext().getBookingSaleGoodsVOList();
            List<String> bookingSalesSku = bookingSaleGoodsVOList.stream().map(b -> b.getGoodsInfoId()).collect(Collectors.toList());
            queryRequest.getNotGoodsInfoIds().addAll(bookingSalesSku);

        }

        // 排除加价购
        if(queryRequest.getMarkup()){
            MarketingIdRequest marketingIdRequest = new MarketingIdRequest();
            marketingIdRequest.setStoreId(  commonUtil.getStoreId());
            List<String> marketingMarkupSku = markupQueryProvider.getMarkupSku(marketingIdRequest).getContext().getLevelList();
            queryRequest.getNotGoodsInfoIds().addAll(marketingMarkupSku);
        }
        // 排除秒杀
        if(queryRequest.getFlashSale()){
            LocalDateTime startTime = LocalDateTime.now().minusHours(Constants.FLASH_SALE_LAST_HOUR);
            FlashSaleActivityListRequest flashSaleActivityListRequest =
                    FlashSaleActivityListRequest.builder().storeId(commonUtil.getStoreId()).fullTimeBegin(startTime).build();
            flashSaleActivityListRequest.setPageSize(pageSize);
            flashSaleActivityListRequest.setFullTimeEnd(LocalDateTime.now().plusMonths(1));
            List<FlashSaleActivityVO> flashSaleActivityVOList = flashSaleActivityQueryProvider.list(flashSaleActivityListRequest).getContext().getFlashSaleActivityVOList();
            List<FlashSaleGoodsVO> flashSaleGoodsVOList=new ArrayList<>();
            for (FlashSaleActivityVO flashSaleActivityVO : flashSaleActivityVOList) {
                FlashSaleGoodsListRequest flashSaleGoodsListRequest = FlashSaleGoodsListRequest.builder().activityDate(flashSaleActivityVO.getActivityDate()).activityTime(flashSaleActivityVO.getActivityTime()).build();
                flashSaleGoodsVOList.addAll(flashSaleGoodsQueryProvider.list(flashSaleGoodsListRequest).getContext().getFlashSaleGoodsVOList());
            }
            List<String> goodsInfoVOS = flashSaleGoodsVOList.stream().map(FlashSaleGoodsVO::getGoodsId).collect(Collectors.toList());
            queryRequest.getNotGoodsInfoIds().addAll(goodsInfoVOS);

        }
        // 排除拼团
        if(queryRequest.getGroupon()){
            EsGrouponActivityPageRequest esGrouponActivityPageRequest = new EsGrouponActivityPageRequest();
            esGrouponActivityPageRequest.setStoreId(Objects.toString(commonUtil.getStoreId()));
            esGrouponActivityPageRequest.setDelFlag(DeleteFlag.NO);
            esGrouponActivityPageRequest.setPageSize(pageSize);
            esGrouponActivityPageRequest.setEndTimeBegin(LocalDateTime.now());
            esGrouponActivityPageRequest.setAuditStatus(AuditStatus.CHECKED);
            List<GrouponActivityForManagerVO> grouponActivityForManagerVOS = esGrouponActivityQueryProvider
                    .page(esGrouponActivityPageRequest).getContext().getGrouponActivityVOPage().getContent();
            List<String> goodsInfoVOS = grouponActivityForManagerVOS.stream().map(GrouponActivityForManagerVO::getGoodsInfoId).collect(Collectors.toList());
            queryRequest.getNotGoodsInfoIds().addAll(goodsInfoVOS);
        }

        queryRequest.setStoreId(commonUtil.getStoreId());
        //按创建时间倒序、ID升序
        queryRequest.putSort("addedTime", SortType.DESC.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
        queryRequest.setShowPointFlag(Boolean.TRUE);
        queryRequest.setShowProviderInfoFlag(Boolean.TRUE);
        queryRequest.setFillLmInfoFlag(Boolean.TRUE);
        queryRequest.setShowPointFlag(Boolean.TRUE);
        EsSkuPageResponse response = esSkuQueryProvider.page(queryRequest).getContext();

        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfoPage().getContent();

        if (customer != null && StringUtils.isNotBlank(customer.getCustomerId())) {
            GoodsIntervalPriceByCustomerIdResponse priceResponse =
                    goodsIntervalPriceService.getGoodsIntervalPriceVOList(goodsInfoVOList, customer.getCustomerId());
            response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
            goodsInfoVOList = priceResponse.getGoodsInfoVOList();
        } else {
            GoodsIntervalPriceResponse priceResponse =
                    goodsIntervalPriceService.getGoodsIntervalPriceVOList(goodsInfoVOList);
            response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
            goodsInfoVOList = priceResponse.getGoodsInfoVOList();
        }

        //计算会员价
        if (customer != null && StringUtils.isNotBlank(customer.getCustomerId())) {
            goodsInfoVOList = marketingLevelPluginProvider.goodsListFilter(
                    MarketingLevelGoodsListFilterRequest.builder()
                            .customerDTO(KsBeanUtil.convert(customer, CustomerDTO.class))
                            .goodsInfos(KsBeanUtil.convert(goodsInfoVOList, GoodsInfoDTO.class)).build())
                    .getContext().getGoodsInfoVOList();
        }
        if (Objects.nonNull(customer)) {
            goodsInfoVOList = this.setRestrictedNum(goodsInfoVOList, customer);
        }

        response.setGoodsInfoPage(new MicroServicePage<>(goodsInfoVOList, queryRequest.getPageRequest(),
                response.getGoodsInfoPage().getTotalElements()));
        return BaseResponse.success(response);
    }

    /**
     * 设置限售数据
     *
     * @param goodsInfoVOS
     * @param customerVO
     * @return
     */
    private List<GoodsInfoVO> setRestrictedNum(List<GoodsInfoVO> goodsInfoVOS, CustomerVO customerVO) {
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(goodsInfoVOS)) {
            List<GoodsRestrictedValidateVO> goodsRestrictedValidateVOS = new ArrayList<>();
            goodsInfoVOS = goodsInfoVOS.stream().map(g -> {
                GoodsRestrictedValidateVO rvv = new GoodsRestrictedValidateVO();
                rvv.setNum(g.getBuyCount());
                rvv.setSkuId(g.getGoodsInfoId());
                goodsRestrictedValidateVOS.add(rvv);
                if (Objects.equals(DeleteFlag.NO, g.getDelFlag())
                        && Objects.equals(CheckStatus.CHECKED, g.getAuditStatus())) {
                    g.setGoodsStatus(GoodsStatus.OK);
                    if (Objects.isNull(g.getStock()) || g.getStock() < 1) {
                        g.setGoodsStatus(GoodsStatus.OUT_STOCK);
                    }
                } else {
                    g.setGoodsStatus(GoodsStatus.INVALID);
                }
                return g;
            }).collect(Collectors.toList());
            GoodsRestrictedSalePurchaseResponse response = goodsRestrictedSaleQueryProvider.validatePurchaseRestricted(
                    GoodsRestrictedBatchValidateRequest.builder()
                            .goodsRestrictedValidateVOS(goodsRestrictedValidateVOS)
                            .customerVO(customerVO)
                            .build()).getContext();
            if (Objects.nonNull(response) && org.apache.commons.collections4.CollectionUtils.isNotEmpty(response.getGoodsRestrictedPurchaseVOS())) {
                List<GoodsRestrictedPurchaseVO> goodsRestrictedPurchaseVOS = response.getGoodsRestrictedPurchaseVOS();
                Map<String, GoodsRestrictedPurchaseVO> purchaseMap =
                        goodsRestrictedPurchaseVOS.stream().collect((Collectors.toMap(GoodsRestrictedPurchaseVO::getGoodsInfoId, g -> g)));
                goodsInfoVOS = goodsInfoVOS.stream().map(g -> {
                    GoodsRestrictedPurchaseVO goodsRestrictedPurchaseVO = purchaseMap.get(g.getGoodsInfoId());
                    if (Objects.nonNull(goodsRestrictedPurchaseVO)) {
                        if (DefaultFlag.YES.equals(goodsRestrictedPurchaseVO.getDefaultFlag())) {
                            g.setMaxCount(goodsRestrictedPurchaseVO.getRestrictedNum());
                            g.setCount(goodsRestrictedPurchaseVO.getStartSaleNum());
                        } else {
                            g.setGoodsStatus(GoodsStatus.INVALID);
                        }
                    }
                    return g;
                }).collect(Collectors.toList());
            }
        }
        return goodsInfoVOS;
    }

    /**
     * 批量获取商品信息
     */
    @ApiOperation(value = "批量获取商品信息")
    @RequestMapping(value = "/order/skus", method = RequestMethod.POST)
    public BaseResponse<GoodsInfoViewListResponse> findSkus(@RequestBody GoodsInfoRequest request) {
        if (CollectionUtils.isEmpty(request.getGoodsInfoIds()) || StringUtils.isEmpty(request.getCustomerId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //获取会员
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(request
                .getCustomerId())).getContext();
        if (Objects.isNull(customer)) {
            throw new SbcRuntimeException(CustomerErrorCode.NOT_EXIST);
        }
        request.setStoreId(commonUtil.getStoreId());
        GoodsInfoViewByIdsResponse idsResponse = goodsInfoQueryProvider.listViewByIds(
                GoodsInfoViewByIdsRequest.builder().goodsInfoIds(request.getGoodsInfoIds()).build()).getContext();

        GoodsInfoViewListResponse response = new GoodsInfoViewListResponse();
        response.setGoodsInfos(idsResponse.getGoodsInfos());
        response.setGoodses(idsResponse.getGoodses());

        //计算区间价
        GoodsIntervalPriceByCustomerIdResponse priceResponse =
                goodsIntervalPriceService.getGoodsIntervalPriceVOList(response.getGoodsInfos(),
                        customer.getCustomerId());
        response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
        response.setGoodsInfos(priceResponse.getGoodsInfoVOList());
        //计算营销价格
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(response.getGoodsInfos(), GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        response.setGoodsInfos(marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList());
        return BaseResponse.success(response);
    }

    /**
     * 批量查询商品生效的营销活动
     */
    @ApiOperation(value = "批量查询商品生效的营销活动", notes = "返回为单品营销信息map, key为单品id，value为营销列表")
    @RequestMapping(value = "/goods/marketings", method = RequestMethod.POST)
    public BaseResponse<Map<String, List<MarketingViewVO>>> getGoodsMarketings(@RequestBody GoodsInfoRequest request) {
        //参数验证
        if (CollectionUtils.isEmpty(request.getGoodsInfoIds()) || StringUtils.isEmpty(request.getCustomerId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //获取会员
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(request
                .getCustomerId())).getContext();
        if (Objects.isNull(customer)) {
            throw new SbcRuntimeException(CustomerErrorCode.NOT_EXIST);
        }
        request.setStoreId(commonUtil.getStoreId());
        request.setDeleteFlag(DeleteFlag.NO);

        GoodsInfoViewByIdsRequest idsRequest = new GoodsInfoViewByIdsRequest();
        KsBeanUtil.copyPropertiesThird(request, idsRequest);
        GoodsInfoViewByIdsResponse idsResponse = goodsInfoQueryProvider.listViewByIds(idsRequest).getContext();
        PurchaseGetGoodsMarketingRequest purchaseGetGoodsMarketingRequest = new PurchaseGetGoodsMarketingRequest();
        purchaseGetGoodsMarketingRequest.setCustomer(customer);
        purchaseGetGoodsMarketingRequest.setGoodsInfos(idsResponse.getGoodsInfos());
        PurchaseGetGoodsMarketingResponse purchaseGetGoodsMarketingResponse =
                purchaseQueryProvider.getGoodsMarketing(purchaseGetGoodsMarketingRequest).getContext();
        return BaseResponse.success(purchaseGetGoodsMarketingResponse.getMap());
    }

    /**
     * 批量删除商品
     */
    @ApiOperation(value = "批量删除商品")
    @RequestMapping(value = "/goods/sku", method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody GoodsInfoRequest request) {
        if (CollectionUtils.isEmpty(request.getGoodsInfoIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        goodsInfoProvider.deleteByIds(GoodsInfoDeleteByIdsRequest.builder().goodsInfoIds(request.getGoodsInfoIds()).build());
        esGoodsInfoElasticProvider.delete(EsGoodsDeleteByIdsRequest.builder().deleteIds(request.getGoodsInfoIds()).build());

        if (1 == request.getGoodsInfoIds().size()) {
            GoodsInfoByIdRequest goodsByIdRequest = new GoodsInfoByIdRequest();
            goodsByIdRequest.setGoodsInfoId(request.getGoodsInfoIds().get(0));
            GoodsInfoByIdResponse response = goodsInfoQueryProvider.getById(goodsByIdRequest).getContext();
            operateLogMQUtil.convertAndSend("商品", "删除商品",
                    "删除商品：SKU编码" + response.getGoodsInfoNo());
        } else {
            operateLogMQUtil.convertAndSend("商品", "批量删除",
                    "批量删除");
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量上架商品
     */
    @ApiOperation(value = "批量上架商品")
    @RequestMapping(value = "/goods/sku/sale", method = RequestMethod.PUT)
    public BaseResponse onSale(@RequestBody GoodsInfoRequest request) {
        if (CollectionUtils.isEmpty(request.getGoodsInfoIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        goodsInfoProvider.modifyAddedStatus(
                GoodsInfoModifyAddedStatusRequest.builder().addedFlag(AddedFlag.YES.toValue())
                        .goodsInfoIds(request.getGoodsInfoIds())
                        .build()
        );
        esGoodsInfoElasticProvider.updateAddedStatus(EsGoodsInfoModifyAddedStatusRequest.builder().
                addedFlag(AddedFlag.YES.toValue()).goodsIds(null).goodsInfoIds(request.getGoodsInfoIds()).build());

        if (1 == request.getGoodsInfoIds().size()) {
            GoodsInfoByIdRequest goodsByIdRequest = new GoodsInfoByIdRequest();
            goodsByIdRequest.setGoodsInfoId(request.getGoodsInfoIds().get(0));
            GoodsInfoByIdResponse response = goodsInfoQueryProvider.getById(goodsByIdRequest).getContext();
            operateLogMQUtil.convertAndSend("商品", "上架",
                    "上架：SKU编码" + response.getGoodsInfoNo());
        } else {
            operateLogMQUtil.convertAndSend("商品", "批量上架", "批量上架");
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量下架商品
     */
    @ApiOperation(value = "批量下架商品")
    @RequestMapping(value = "/goods/sku/sale", method = RequestMethod.DELETE)
    public BaseResponse offSale(@RequestBody GoodsInfoRequest request) {

        if (CollectionUtils.isEmpty(request.getGoodsInfoIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        goodsInfoProvider.modifyAddedStatus(
                GoodsInfoModifyAddedStatusRequest.builder().addedFlag(AddedFlag.NO.toValue())
                        .goodsInfoIds(request.getGoodsInfoIds())
                        .build()
        );
        esGoodsInfoElasticProvider.updateAddedStatus(EsGoodsInfoModifyAddedStatusRequest.builder().
                addedFlag(AddedFlag.NO.toValue()).goodsIds(null).goodsInfoIds(request.getGoodsInfoIds()).build());

        if (1 == request.getGoodsInfoIds().size()) {
            GoodsInfoByIdRequest goodsByIdRequest = new GoodsInfoByIdRequest();
            goodsByIdRequest.setGoodsInfoId(request.getGoodsInfoIds().get(0));
            GoodsInfoByIdResponse response = goodsInfoQueryProvider.getById(goodsByIdRequest).getContext();
            operateLogMQUtil.convertAndSend("商品", "下架",
                    "下架：SKU编码" + response.getGoodsInfoNo());
        } else {
            operateLogMQUtil.convertAndSend("商品", "批量下架", "批量下架");
        }
        return BaseResponse.SUCCESSFUL();
    }

}