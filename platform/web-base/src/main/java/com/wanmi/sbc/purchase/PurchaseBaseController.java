package com.wanmi.sbc.purchase;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.enums.VASConstants;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.distribute.DistributionCacheService;
import com.wanmi.sbc.distribute.DistributionService;
import com.wanmi.sbc.goods.api.enums.GoodsChannelTypeEnum;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedsale.GoodsRestrictedSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsPriceAssistProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleInProgressRequest;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleInProgressRequest;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedBatchValidateRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoFillGoodsStatusRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsPriceSetBatchByIepRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSalePurchaseResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsPriceSetBatchByIepResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsMarketingDTO;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsMarketingVO;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedPurchaseVO;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedValidateVO;
import com.wanmi.sbc.intervalprice.GoodsIntervalPriceService;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseProvider;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseQueryProvider;
import com.wanmi.sbc.order.api.request.purchase.Purchase4DistributionRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseAddFollowRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseBatchSaveRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseCalcAmountRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseCalcMarketingRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseClearLoseGoodsRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseCountGoodsRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseDeleteRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseFrontMiniRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseFrontRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseGetGoodsMarketingRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseGetStoreCouponExistRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseGetStoreMarketingRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseInfoRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseListRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseMergeRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseMiniListRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseModifyGoodsMarketingRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseQueryGoodsMarketingListRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseSaveRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseSyncGoodsMarketingsRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseUpdateNumRequest;
import com.wanmi.sbc.order.api.request.purchase.ValidateAndSetGoodsMarketingsRequest;
import com.wanmi.sbc.order.api.response.purchase.MiniPurchaseResponse;
import com.wanmi.sbc.order.api.response.purchase.Purchase4DistributionResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseCalcMarketingResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseCountGoodsResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseGetGoodsMarketingResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseGetStoreCouponExistResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseListResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseMiniListResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseQueryGoodsMarketingListResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseResponse;
import com.wanmi.sbc.order.bean.dto.PurchaseCalcAmountDTO;
import com.wanmi.sbc.order.bean.dto.PurchaseMergeDTO;
import com.wanmi.sbc.system.service.SystemPointsConfigService;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 采购单Controller
 * Created by daiyitian on 17/4/12.
 */
@Api(tags = "PurchaseBaseController", description = "采购单服务API")
@RestController
@RequestMapping("/site")
@Validated
@Slf4j
public class PurchaseBaseController {

    @Autowired
    private PurchaseQueryProvider purchaseQueryProvider;

    @Autowired
    private PurchaseProvider purchaseProvider;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private GoodsIntervalPriceService goodsIntervalPriceService;

    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private GoodsPriceAssistProvider goodsPriceAssistProvider;

    @Autowired
    private DistributionCacheService distributionCacheService;

    @Autowired
    private GoodsRestrictedSaleQueryProvider goodsRestrictedSaleQueryProvider;

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    /**
     * 未登录时,根据采购单缓存信息获取采购单详情
     *
     * @return 采购单
     * @author bail
     */
    @ApiOperation(value = "未登录时,根据采购单缓存信息获取采购单详情")
    @RequestMapping(value = "/front/purchases", method = RequestMethod.POST)
    public BaseResponse<PurchaseResponse> frontInfo(@RequestBody @Valid PurchaseFrontRequest request) {
        request.setInviteeId(commonUtil.getPurchaseInviteeId());
        // 根据前端传入的采购单信息,查询并组装其他必要信息
        PurchaseResponse response = purchaseQueryProvider.listFront(request).getContext();
        CustomerVO customer = null;
        List<String> skuIdList = request.getGoodsInfoIds();

        if (CollectionUtils.isNotEmpty(response.getGoodsInfos())) {
            //设定SKU状态
            List<GoodsInfoVO> goodsInfoVOList = goodsInfoProvider.fillGoodsStatus(
                    new GoodsInfoFillGoodsStatusRequest(KsBeanUtil.convert(response.getGoodsInfos(),
                            GoodsInfoDTO.class))).getContext().getGoodsInfos();
            // 预售、预约营销优先级处理---只能先这样实现优先级了
            // 预约预售排除积分价商品
            List<String> goodInfoIdList = goodsInfoVOList.stream().filter(goodsInfoVO ->  Objects.isNull(goodsInfoVO.getBuyPoint())||(goodsInfoVO.getBuyPoint().compareTo(0L) == 0))
                    .map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
            List<AppointmentSaleVO> appointmentSaleVOList = new ArrayList<>();
            List<BookingSaleVO> bookingSaleVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(goodInfoIdList)){
                appointmentSaleVOList =appointmentSaleQueryProvider.inProgressAppointmentSaleInfoByGoodsInfoIdList
                        (AppointmentSaleInProgressRequest.builder().goodsInfoIdList(goodInfoIdList).build()).getContext().getAppointmentSaleVOList();
                bookingSaleVOList = bookingSaleQueryProvider.inProgressBookingSaleInfoByGoodsInfoIdList
                        (BookingSaleInProgressRequest.builder().goodsInfoIdList(goodInfoIdList).build()).getContext().getBookingSaleVOList();
            }
            // 商品信息冗余预约预售信息
            dealBookingAndAppointDate( goodsInfoVOList,appointmentSaleVOList,bookingSaleVOList);
            //根据开关重新设置分销商品标识
            distributionService.checkDistributionSwitch(goodsInfoVOList);

            //计算区间价
            GoodsIntervalPriceRequest goodsIntervalPriceRequest = new GoodsIntervalPriceRequest(
                    KsBeanUtil.convert(goodsInfoVOList, GoodsInfoDTO.class));
            GoodsIntervalPriceResponse priceResponse = goodsIntervalPriceProvider.put(goodsIntervalPriceRequest)
                    .getContext();
            response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
            response.setGoodsInfos(priceResponse.getGoodsInfoVOList());
            // 区间价重新查询了商品信息--预售信息丢了
            List<GoodsInfoVO> goodsInfosTemp =priceResponse.getGoodsInfoVOList();
            // 商品信息冗余预约预售信息
            dealBookingAndAppointDate( goodsInfosTemp,appointmentSaleVOList,bookingSaleVOList);
            response.setGoodsInfos(goodsInfosTemp);


            //社交分销业务
            Purchase4DistributionRequest purchase4DistributionRequest = new Purchase4DistributionRequest();
            purchase4DistributionRequest.setGoodsInfos(response.getGoodsInfos());
            purchase4DistributionRequest.setGoodsIntervalPrices(response.getGoodsIntervalPrices());
            purchase4DistributionRequest.setCustomer(customer);
            purchase4DistributionRequest.setDistributeChannel(commonUtil.getDistributeChannel());
            Purchase4DistributionResponse purchase4DistributionResponse =
                    purchaseQueryProvider.distribution(purchase4DistributionRequest).getContext();
            response.setGoodsInfos(purchase4DistributionResponse.getGoodsInfos());
            response.setGoodsIntervalPrices(purchase4DistributionResponse.getGoodsIntervalPrices());
            response.setSelfBuying(purchase4DistributionResponse.isSelfBuying());
            //排除分销商品
            List<GoodsInfoVO> goodsInfoComList = purchase4DistributionResponse.getGoodsInfoComList();
            /*if (commonUtil.findVASBuyOrNot(VASConstants.VAS_IEP_SETTING)) {
                //企业购分支，设置企业会员价
                GoodsPriceSetBatchByIepRequest iepRequest = GoodsPriceSetBatchByIepRequest.builder()
                        .customer(customer)
                        .goodsInfos(response.getGoodsInfos())
                        .goodsIntervalPrices(response.getGoodsIntervalPrices())
                        .build();
                GoodsPriceSetBatchByIepResponse iepResponse = goodsPriceAssistProvider.goodsPriceSetBatchByIep(iepRequest).getContext();
                response.setGoodsInfos(iepResponse.getGoodsInfos());
                response.setGoodsIntervalPrices(iepResponse.getGoodsIntervalPrices());
            }*/

            // 采购单商品编号，只包含有效的商品
            List<GoodsInfoVO> goodsInfos = goodsInfoComList.stream()
                    .filter(goodsInfo -> goodsInfo.getGoodsStatus() == GoodsStatus.OK).collect(Collectors.toList());

            // 获取商品对应的营销信息
            // if (goodsInfos.size() > 0) {
            response.setGoodsMarketingMap(purchaseQueryProvider.getGoodsMarketing(new
                    PurchaseGetGoodsMarketingRequest(goodsInfos, customer)).getContext().getMap());
            // }

            // 验证并设置 各商品使用的营销信息(满减/满折/满赠)
            ValidateAndSetGoodsMarketingsRequest validateAndSetGoodsMarketingsRequest =
                    new ValidateAndSetGoodsMarketingsRequest();
            validateAndSetGoodsMarketingsRequest.setGoodsMarketingDTOList(request.getGoodsMarketingDTOList());
            validateAndSetGoodsMarketingsRequest.setResponse(response);
            response =
                    purchaseQueryProvider.validateAndSetGoodsMarketings(validateAndSetGoodsMarketingsRequest).getContext();

            // 获取店铺对应的营销信息
            if (MapUtils.isNotEmpty(response.getGoodsMarketingMap())) {
                PurchaseGetStoreMarketingRequest purchaseGetStoreMarketingRequest =
                        new PurchaseGetStoreMarketingRequest();
                List<String> excludeIds =new ArrayList<>();
                // 预约信息
                if (CollectionUtils.isNotEmpty(appointmentSaleVOList)) {
                    List<String> appointmentGoodsInfos = appointmentSaleVOList.stream().map(appointmentSaleVO -> appointmentSaleVO.getAppointmentSaleGood().getGoodsInfoId()).collect(Collectors.toList());
                    excludeIds.addAll(appointmentGoodsInfos);
                }
                // 预售信息
                if (CollectionUtils.isNotEmpty(bookingSaleVOList)) {
                    List<String> bookingGoodsInfos = bookingSaleVOList.stream().map(bookingSaleVO -> bookingSaleVO.getBookingSaleGoods().getGoodsInfoId()).collect(Collectors.toList());
                    excludeIds.addAll(bookingGoodsInfos);
                }
                // 满折赠等排除预约、预售
                if(CollectionUtils.isNotEmpty(request.getGoodsInfoIds())){
                    // List<AppointmentSaleVO> appointmentSaleVOList = appointmentSaleQueryProvider.inProgressAppointmentSaleInfoByGoodsInfoIdList
                    //         (AppointmentSaleInProgressRequest.builder().goodsInfoIdList(request.getGoodsInfoIds()).build()).getContext().getAppointmentSaleVOList();
                    List<String> goodsInfoIds = new ArrayList<>();
                    request.getGoodsInfoIds().forEach(g -> {
                        if (!excludeIds.contains(g)) {
                            goodsInfoIds.add(g);
                        }
                    });
                    purchaseGetStoreMarketingRequest.setGoodsInfoIdList(goodsInfoIds);

                }
                purchaseGetStoreMarketingRequest.setCustomer(KsBeanUtil.convert(customer, CustomerDTO.class));
                purchaseGetStoreMarketingRequest.setFrontReq(request);
                if (response.getGoodsMarketings() != null) {
                    purchaseGetStoreMarketingRequest.setGoodsMarketings(
                            KsBeanUtil.convertList(response.getGoodsMarketings(), GoodsMarketingDTO.class));
                }
                response.setStoreMarketingMap(purchaseQueryProvider.getStoreMarketing(purchaseGetStoreMarketingRequest).getContext().getMap());
            } else {
                response.setStoreMarketingMap(new HashMap<>());
            }


            // 获取店铺下是否存在可以领取的优惠券，用于显示店铺旁的"优惠券"标识
//            PurchaseGetStoreCouponExistRequest purchaseGetStoreCouponExistRequest =
//                    new PurchaseGetStoreCouponExistRequest();
//            purchaseGetStoreCouponExistRequest.setCustomer(customer);
//            purchaseGetStoreCouponExistRequest.setGoodsInfoList(goodsInfoComList);
//            response.setStoreCouponMap(
//                    purchaseQueryProvider.getStoreCouponExist(purchaseGetStoreCouponExistRequest).getContext().getMap());

            //排除选中的商品id中无效的商品id
            List<GoodsInfoVO> goodsInfoVOS = response.getGoodsInfos().stream()
                    .filter(goodsInfo -> goodsInfo.getGoodsStatus() == GoodsStatus.OK).collect(Collectors.toList());
            request.setGoodsInfoIds(goodsInfoVOS.stream()
                    .filter(goodsInfo -> request.getGoodsInfoIds().contains(goodsInfo.getGoodsInfoId()))
                    .map(GoodsInfoVO::getGoodsInfoId)
                    .collect(Collectors.toList()));

            PurchaseCalcAmountRequest purchaseCalcAmountRequest = new PurchaseCalcAmountRequest();
            purchaseCalcAmountRequest.setPurchaseCalcAmount(KsBeanUtil.convert(response, PurchaseCalcAmountDTO.class));
            purchaseCalcAmountRequest.setGoodsInfoIds(request.getGoodsInfoIds());
            response = KsBeanUtil.convert(purchaseProvider.calcAmount(
                    purchaseCalcAmountRequest).getContext(), PurchaseResponse.class);
            if (CollectionUtils.isNotEmpty(goodInfoIdList)) {
                // 预约活动信息
                response.setAppointmentSaleVOList(appointmentSaleVOList);
                // 预售活动信息
                response.setBookingSaleVOList(bookingSaleVOList);
            }

        }

        return BaseResponse.success(response);
    }

    @ApiOperation(value = "获取店铺下，是否有优惠券营销，展示优惠券标签")
    @RequestMapping(value = "/getStoreCouponExist", method = RequestMethod.GET)
    public BaseResponse<PurchaseGetStoreCouponExistResponse> getStoreCouponExist() {
        return purchaseQueryProvider.getStoreCouponExist(
                PurchaseGetStoreCouponExistRequest.builder().inviteeId(commonUtil.getPurchaseInviteeId()).customer(commonUtil.getCustomer()).build()
        );
    }

    /**
     * 获取采购单
     *
     * @return 采购单
     */
    @ApiOperation(value = "获取采购单")
    @RequestMapping(value = "/purchases", method = RequestMethod.POST)
    public BaseResponse<PurchaseListResponse> info(@RequestBody PurchaseListRequest request) {
        //获取会员
        CustomerVO customer = commonUtil.getCustomer();
        // 采购单列表
        PurchaseListRequest purchaseListRequest = new PurchaseListRequest();
        purchaseListRequest.setCustomerId(customer.getCustomerId());
        purchaseListRequest.setInviteeId(commonUtil.getPurchaseInviteeId());
        purchaseListRequest.setAreaId(request.getAreaId());
        PurchaseListResponse response = purchaseQueryProvider.list(purchaseListRequest).getContext();

        if (CollectionUtils.isNotEmpty(response.getGoodsInfos())) {
            //设定SKU状态
            List<GoodsInfoVO> goodsInfoVOList = goodsInfoProvider.fillGoodsStatus(
                    GoodsInfoFillGoodsStatusRequest.builder()
                            .goodsInfos(KsBeanUtil.convertList(response.getGoodsInfos(), GoodsInfoDTO.class)).build()
            ).getContext().getGoodsInfos();

            // 预售、预约营销优先级处理---只能先这样实现优先级了
            // 预约预售排除积分价商品
            List<String> goodInfoIdList = goodsInfoVOList.stream().filter(goodsInfoVO ->  Objects.isNull(goodsInfoVO.getBuyPoint())||(goodsInfoVO.getBuyPoint().compareTo(0L) == 0))
                    .map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
            List<AppointmentSaleVO> appointmentSaleVOList = new ArrayList<>();
            List<BookingSaleVO> bookingSaleVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(goodInfoIdList)){
                appointmentSaleVOList =appointmentSaleQueryProvider.inProgressAppointmentSaleInfoByGoodsInfoIdList
                        (AppointmentSaleInProgressRequest.builder().goodsInfoIdList(goodInfoIdList).build()).getContext().getAppointmentSaleVOList();
                bookingSaleVOList = bookingSaleQueryProvider.inProgressBookingSaleInfoByGoodsInfoIdList
                        (BookingSaleInProgressRequest.builder().goodsInfoIdList(goodInfoIdList).build()).getContext().getBookingSaleVOList();
            }
            // 商品信息冗余预约预售信息
            dealBookingAndAppointDate( goodsInfoVOList,appointmentSaleVOList,bookingSaleVOList);
            //根据开关重新设置分销商品标识
            distributionService.checkDistributionSwitch(goodsInfoVOList);

            //计算区间价
            GoodsIntervalPriceByCustomerIdResponse priceResponse =
                    goodsIntervalPriceService.getGoodsIntervalPriceVOList(goodsInfoVOList, customer.getCustomerId());
            if (Objects.nonNull(priceResponse.getGoodsIntervalPriceVOList())) {
                response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
            } else {
                response.setGoodsIntervalPrices(Collections.emptyList());
            }

            // 区间价重新查询了商品信息--预售信息丢了
            List<GoodsInfoVO> goodsInfosTemp =priceResponse.getGoodsInfoVOList();
           // 商品信息冗余预约预售信息
            dealBookingAndAppointDate( goodsInfosTemp,appointmentSaleVOList,bookingSaleVOList);
            response.setGoodsInfos(goodsInfosTemp);
            // 设置起订量和限售量
            response.setGoodsInfos(this.setRestrictedNum(goodsInfosTemp, customer));
            //社交分销业务
            Purchase4DistributionRequest purchase4DistributionRequest = new Purchase4DistributionRequest();
            purchase4DistributionRequest.setGoodsInfos(goodsInfosTemp);
            purchase4DistributionRequest.setGoodsIntervalPrices(response.getGoodsIntervalPrices());
            purchase4DistributionRequest.setCustomer(customer);
            purchase4DistributionRequest.setDistributeChannel(commonUtil.getDistributeChannel());
            Purchase4DistributionResponse purchase4DistributionResponse =
                    purchaseQueryProvider.distribution(purchase4DistributionRequest).getContext();
            response.setGoodsInfos(purchase4DistributionResponse.getGoodsInfos());
            response.setGoodsIntervalPrices(purchase4DistributionResponse.getGoodsIntervalPrices());

            //排除分销商品
            List<GoodsInfoVO> goodsInfoComList = purchase4DistributionResponse.getGoodsInfoComList();

            // 采购单商品编号，只包含有效的商品
            List<GoodsInfoVO> goodsInfos = goodsInfoComList.stream()
                    .filter(goodsInfo -> goodsInfo.getGoodsStatus() == GoodsStatus.OK).collect(Collectors.toList());

            // 获取商品对应的营销信息
            PurchaseGetGoodsMarketingRequest purchaseGetGoodsMarketingRequest = new PurchaseGetGoodsMarketingRequest();
            purchaseGetGoodsMarketingRequest.setGoodsInfos(goodsInfos);
            purchaseGetGoodsMarketingRequest.setCustomer(KsBeanUtil.convert(customer, CustomerVO.class));
            PurchaseGetGoodsMarketingResponse marketingResponse =
                    purchaseQueryProvider.getGoodsMarketing(purchaseGetGoodsMarketingRequest).getContext();
            response.setGoodsMarketingMap(marketingResponse.getMap());
            response.setSelfBuying(purchase4DistributionResponse.isSelfBuying());
            goodsInfos = response.getGoodsInfos().stream().map(goodsInfo ->
                    marketingResponse.getGoodsInfos().stream()
                            .filter(item -> item.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId()))
                            .findFirst().orElse(goodsInfo)
            ).collect(Collectors.toList());
            response.setGoodsInfos(goodsInfos);

            //企业购分支，设置企业会员价
            if (commonUtil.findVASBuyOrNot(VASConstants.VAS_IEP_SETTING)) {
                GoodsPriceSetBatchByIepRequest iepRequest = GoodsPriceSetBatchByIepRequest.builder()
                        .customer(customer)
                        .goodsInfos(response.getGoodsInfos())
                        .filteredGoodsInfoIds(goodsInfoComList.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()))
                        .goodsIntervalPrices(response.getGoodsIntervalPrices())
                        .build();
                GoodsPriceSetBatchByIepResponse iepResponse = goodsPriceAssistProvider.goodsPriceSetBatchByIep(iepRequest).getContext();
                response.setGoodsInfos(iepResponse.getGoodsInfos());
                response.setGoodsIntervalPrices(iepResponse.getGoodsIntervalPrices());
            }
            PurchaseSyncGoodsMarketingsRequest purchaseSyncGoodsMarketingsRequest =
                    new PurchaseSyncGoodsMarketingsRequest();
            purchaseSyncGoodsMarketingsRequest.setGoodsMarketingMap(response.getGoodsMarketingMap());
            purchaseSyncGoodsMarketingsRequest.setCustomerId(customer.getCustomerId());
            purchaseProvider.syncGoodsMarketings(purchaseSyncGoodsMarketingsRequest);

            PurchaseQueryGoodsMarketingListRequest purchaseQueryGoodsMarketingListRequest =
                    new PurchaseQueryGoodsMarketingListRequest();
            purchaseQueryGoodsMarketingListRequest.setCustomerId(customer.getCustomerId());
            response.setGoodsMarketings(purchaseQueryProvider.queryGoodsMarketingList(purchaseQueryGoodsMarketingListRequest).getContext().getGoodsMarketingList());

            // 获取店铺对应的营销信息
            if (CollectionUtils.isNotEmpty(response.getGoodsMarketings())) {
                PurchaseGetStoreMarketingRequest purchaseGetStoreMarketingRequest =
                        new PurchaseGetStoreMarketingRequest();
                purchaseGetStoreMarketingRequest.setGoodsInfoIdList(request.getGoodsInfoIds());
                List<String> excludeIds =new ArrayList<>();
                // 预约信息
                if (CollectionUtils.isNotEmpty(appointmentSaleVOList)) {
                    List<String> appointmentGoodsInfos = appointmentSaleVOList.stream().map(appointmentSaleVO -> appointmentSaleVO.getAppointmentSaleGood().getGoodsInfoId()).collect(Collectors.toList());
                    excludeIds.addAll(appointmentGoodsInfos);
                }
                // 预售信息
                if (CollectionUtils.isNotEmpty(bookingSaleVOList)) {
                    List<String> bookingGoodsInfos = bookingSaleVOList.stream().map(bookingSaleVO -> bookingSaleVO.getBookingSaleGoods().getGoodsInfoId()).collect(Collectors.toList());
                    excludeIds.addAll(bookingGoodsInfos);
                }
                // 满折赠等排除预约、预售
                if(CollectionUtils.isNotEmpty(request.getGoodsInfoIds())){

                    List<String> goodsInfoIds = new ArrayList<>();
                    request.getGoodsInfoIds().forEach(g -> {
                        if (!excludeIds.contains(g)) {
                            goodsInfoIds.add(g);
                        }
                    });
                    purchaseGetStoreMarketingRequest.setGoodsInfoIdList(goodsInfoIds);

                }
                purchaseGetStoreMarketingRequest.setCustomer(KsBeanUtil.convert(customer, CustomerDTO.class));
                purchaseGetStoreMarketingRequest.setGoodsMarketings(KsBeanUtil.convertList(response.getGoodsMarketings(), GoodsMarketingDTO.class));
                response.setStoreMarketingMap(purchaseQueryProvider.getStoreMarketing(purchaseGetStoreMarketingRequest).getContext().getMap());
            } else {
                response.setStoreMarketingMap(new HashMap<>());
            }

            //获取店铺下是否存在可以领取的优惠券，用于显示店铺旁的"优惠券"
//            PurchaseGetStoreCouponExistRequest purchaseGetStoreCouponExistRequest =
//                    new PurchaseGetStoreCouponExistRequest();
//            purchaseGetStoreCouponExistRequest.setGoodsInfoList(goodsInfoComList);
//            purchaseGetStoreCouponExistRequest.setCustomer(KsBeanUtil.convert(customer, CustomerVO.class));
//            response.setStoreCouponMap(purchaseQueryProvider.getStoreCouponExist(purchaseGetStoreCouponExistRequest).getContext().getMap());

            //排除选中的商品id中无效的商品id
            List<GoodsInfoVO> goodsInfoVOS = response.getGoodsInfos().stream()
                    .filter(goodsInfo -> goodsInfo.getGoodsStatus() == GoodsStatus.OK).collect(Collectors.toList());
            request.setGoodsInfoIds(goodsInfoVOS.stream()
                    .filter(goodsInfo -> request.getGoodsInfoIds().contains(goodsInfo.getGoodsInfoId()))
                    .map(GoodsInfoVO::getGoodsInfoId)
                    .collect(Collectors.toList()));

            //针对积分价商品特殊处理
            Boolean isGoodsPoint = systemPointsConfigService.isGoodsPoint();
            response.setGoodsInfos(response.getGoodsInfos().stream().map(goodsInfoVO -> {
                if (isGoodsPoint && Objects.nonNull(goodsInfoVO.getBuyPoint()) && goodsInfoVO.getBuyPoint() > 0){
                    goodsInfoVO.setDistributionCommission(BigDecimal.ZERO);
                    goodsInfoVO.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                }
                return goodsInfoVO;
            }).collect(Collectors.toList()));

            PurchaseCalcAmountRequest purchaseCalcAmountRequest = new PurchaseCalcAmountRequest();
            purchaseCalcAmountRequest.setPurchaseCalcAmount(KsBeanUtil.convert(response, PurchaseCalcAmountDTO.class));
            purchaseCalcAmountRequest.setGoodsInfoIds(request.getGoodsInfoIds());
            response = purchaseProvider.calcAmount(purchaseCalcAmountRequest).getContext();



            //TODO 脱敏
//            if (CollectionUtils.isNotEmpty(response.getCompanyInfos())) {
//                response.getCompanyInfos().forEach(
//                        companyInfoVO -> {
//                            if (CollectionUtils.isNotEmpty(companyInfoVO.getEmployeeVOList())) {
//                                companyInfoVO.getEmployeeVOList().forEach(
//                                        employeeVO -> {
//                                            employeeVO.setAccountPassword(null);
//                                            employeeVO.setEmployeeSaltVal(null);
//                                            employeeVO.setEmployeeName(null);
//                                            employeeVO.setEmployeeMobile(null);
//                                            employeeVO.setLoginTime(null);
//
//                                        }
//                                );
//                            }
//                            if (CollectionUtils.isNotEmpty(companyInfoVO.getStoreVOList())) {
//                                companyInfoVO.getStoreVOList().forEach(
//                                        storeVO -> {
//                                            storeVO.setContactEmail(null);
//                                            storeVO.setAddressDetail(null);
//                                            storeVO.setContactMobile(null);
//                                        }
//                                );
//                            }
//                        });
//            }
//            if (CollectionUtils.isNotEmpty(response.getStores())) {
//                response.getStores().forEach(storeVO -> {
//                    storeVO.setContactEmail(null);
//                    storeVO.setAddressDetail(null);
//                    storeVO.setContactMobile(null);
//                });
//            }

            if (CollectionUtils.isNotEmpty(goodInfoIdList)) {
                // 预约活动信息
                response.setAppointmentSaleVOList(appointmentSaleVOList);
                // 预售活动信息
                response.setBookingSaleVOList(bookingSaleVOList);
            }
        }
        return BaseResponse.success(response);
    }

    /**
     * 商品信息冗余预约预售信息
     * @param goodsInfoVOList
     * @param appointmentSaleVOList
     * @param bookingSaleVOList
     */
    private void  dealBookingAndAppointDate(List<GoodsInfoVO> goodsInfoVOList,  List<AppointmentSaleVO> appointmentSaleVOList,List<BookingSaleVO> bookingSaleVOList ){
        // 预约信息
        if (CollectionUtils.isNotEmpty(appointmentSaleVOList)) {
            Map<String, List<AppointmentSaleVO>> appointmentMap = appointmentSaleVOList.stream().collect(Collectors.groupingBy(a -> a.getAppointmentSaleGood().getGoodsInfoId()));
            goodsInfoVOList.forEach(g -> {
                if (appointmentMap.containsKey(g.getGoodsInfoId())) {
                    BigDecimal appointmentPrice = appointmentMap.get(g.getGoodsInfoId()).get(0).getAppointmentSaleGood().getPrice();
                    g.setAppointmentPrice(appointmentPrice);
                    g.setAppointmentSaleVO(appointmentMap.get(g.getGoodsInfoId()).get(0));
                }
            });
        }

        // 预售信息
        if (CollectionUtils.isNotEmpty(bookingSaleVOList)) {
            Map<String, List<BookingSaleVO>> bookingMap = bookingSaleVOList.stream().collect(Collectors.groupingBy(a -> a.getBookingSaleGoods().getGoodsInfoId()));
            goodsInfoVOList.forEach(g -> {
                if (bookingMap.containsKey(g.getGoodsInfoId())) {
                    BigDecimal bookingPrice = bookingMap.get(g.getGoodsInfoId()).get(0).getBookingSaleGoods().getBookingPrice();
                    g.setBookingPrice(bookingPrice);
                    g.setBookingSaleVO(bookingMap.get(g.getGoodsInfoId()).get(0));
                }
            });

        }
    }

    /**
     * 未登录时,根据参数获取迷你采购单信息
     *
     * @return
     */
    @ApiOperation(value = "未登录时,根据参数获取迷你采购单信息")
    @RequestMapping(value = "/front/miniPurchases", method = RequestMethod.POST)
    public BaseResponse<MiniPurchaseResponse> miniFrontInfo(@RequestBody @Valid PurchaseFrontMiniRequest request) {
        request.setInviteeId(commonUtil.getPurchaseInviteeId());
        MiniPurchaseResponse response = purchaseQueryProvider.miniListFront(request).getContext();
        List<GoodsInfoVO> goodsInfos = goodsInfoProvider.fillGoodsStatus(GoodsInfoFillGoodsStatusRequest.builder()
                .goodsInfos(KsBeanUtil.convertList(response.getGoodsList(), GoodsInfoDTO.class)).build()).getContext().getGoodsInfos();
        response.getGoodsList().forEach(goodsInfo -> {
            goodsInfos.forEach(info -> {
                if (info.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())) {
                    goodsInfo.setGoodsStatus(info.getGoodsStatus());
                }
            });
        });
        return BaseResponse.success(response);
    }

    /**
     * 获取迷你采购单
     *
     * @return
     */
    @ApiOperation(value = "获取迷你采购单")
    @RequestMapping(value = "/miniPurchases", method = RequestMethod.POST)
    public BaseResponse<PurchaseMiniListResponse> miniInfo() {
        CustomerVO customer = commonUtil.getCustomer();
        PurchaseMiniListRequest purchaseMiniListRequest = new PurchaseMiniListRequest();
        purchaseMiniListRequest.setCustomerId(customer.getCustomerId());

        purchaseMiniListRequest.setInviteeId(commonUtil.getPurchaseInviteeId());
        purchaseMiniListRequest.setCustomer(KsBeanUtil.convert(customer, CustomerDTO.class));

        //需要叠加访问端Pc\app不体现分销业务
        DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
        if (Objects.equals(ChannelType.PC_MALL, commonUtil.getDistributeChannel().getChannelType()) || DefaultFlag.NO.equals(openFlag)) {
            purchaseMiniListRequest.setPcAndNoOpenFlag(Boolean.TRUE);
        }

        PurchaseMiniListResponse purchaseMiniListResponse =
                purchaseQueryProvider.minilist(purchaseMiniListRequest).getContext();
        List<GoodsInfoVO> goodsInfos = goodsInfoProvider.fillGoodsStatus(GoodsInfoFillGoodsStatusRequest.builder()
                .goodsInfos(KsBeanUtil.convertList(purchaseMiniListResponse.getGoodsList(), GoodsInfoDTO.class)).build()).getContext().getGoodsInfos();
        purchaseMiniListResponse.getGoodsList().forEach(goodsInfo -> {
            goodsInfos.forEach(info -> {
                if (info.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())) {
                    goodsInfo.setGoodsStatus(info.getGoodsStatus());
                }
            });
        });
        return BaseResponse.success(purchaseMiniListResponse);
    }

    /**
     * 获取采购单商品数量
     *
     * @return
     */
    @ApiOperation(value = "获取采购单商品数量")
    @RequestMapping(value = "/countGoods", method = RequestMethod.GET)
    public BaseResponse<Integer> countGoods() {
        PurchaseCountGoodsRequest purchaseCountGoodsRequest = new PurchaseCountGoodsRequest();
        purchaseCountGoodsRequest.setCustomerId(commonUtil.getOperatorId());
        purchaseCountGoodsRequest.setInviteeId(commonUtil.getPurchaseInviteeId());

        purchaseCountGoodsRequest.setGoodsChannelType(TerminalSource.MINIPROGRAM.equals(commonUtil.getTerminal()) ?
                GoodsChannelTypeEnum.MALL_MINI.getCode() : GoodsChannelTypeEnum.MALL_H5.getCode());

        PurchaseCountGoodsResponse purchaseCountGoodsResponse = purchaseQueryProvider.countGoods(purchaseCountGoodsRequest).getContext();
        return BaseResponse.success(purchaseCountGoodsResponse.getTotal());
    }

    /**
     * 新增采购单
     *
     * @param request 数据
     * @return 结果
     */
    @ApiOperation(value = "新增采购单")
    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    public BaseResponse add(@RequestBody @Valid PurchaseSaveRequest request) {
        request.setCustomerId(commonUtil.getOperatorId());
        if (StringUtils.isBlank(request.getCustomerId()) || StringUtils.isBlank(request.getGoodsInfoId())) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
        }

        //校验是否可以加入购物车
        purchaseQueryProvider.checkAdd(request);
        request.setInviteeId(commonUtil.getPurchaseInviteeId());
        request.setTerminalSource(commonUtil.getTerminal());
        purchaseProvider.save(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量新增采购单
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "批量新增采购单")
    @RequestMapping(value = "/batchAdd", method = RequestMethod.POST)
    public BaseResponse batchAdd(@RequestBody PurchaseBatchSaveRequest request) {
        request.setCustomerId(commonUtil.getOperatorId());
        if (StringUtils.isBlank(request.getCustomerId()) || CollectionUtils.isEmpty(request.getGoodsInfos())) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
        }
        List<String> goodsInfoIds = request.getGoodsInfos().stream().map(GoodsInfoDTO::getGoodsInfoId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(goodsInfoIds)) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
        }
        request.setInviteeId(commonUtil.getPurchaseInviteeId());
        request.setTerminalSource(commonUtil.getTerminal());
        purchaseProvider.batchSave(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 调整数量
     *
     * @param request 数据
     * @return 结果
     */
    @ApiOperation(value = "调整数量")
    @RequestMapping(value = "/purchase", method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody @Valid PurchaseUpdateNumRequest request) {
        request.setCustomerId(commonUtil.getOperatorId());
        if (StringUtils.isBlank(request.getCustomerId()) || StringUtils.isEmpty(request.getGoodsInfoId())) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
        }

        purchaseQueryProvider.checkAdd(KsBeanUtil.convert(request, PurchaseSaveRequest.class));
        request.setInviteeId(commonUtil.getPurchaseInviteeId());
        request.setTerminalSource(commonUtil.getTerminal());
        purchaseProvider.updateNum(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除采购单
     *
     * @param request 数据
     * @return 结果
     */
    @ApiOperation(value = "删除采购单")
    @RequestMapping(value = "/purchase", method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody PurchaseDeleteRequest request) {
        request.setCustomerId(commonUtil.getOperatorId());
        if (CollectionUtils.isEmpty(request.getGoodsInfoIds()) || StringUtils.isBlank(request.getCustomerId())) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
        }

        request.setInviteeId(commonUtil.getPurchaseInviteeId());
        purchaseProvider.delete(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 清除失效商品
     *
     * @return
     */
    @ApiOperation(value = "清除失效商品")
    @RequestMapping(value = "/clearLoseGoods", method = RequestMethod.DELETE)
    public BaseResponse clearLoseGoods() {
        PurchaseClearLoseGoodsRequest purchaseClearLoseGoodsRequest = new PurchaseClearLoseGoodsRequest();
        purchaseClearLoseGoodsRequest.setUserId(commonUtil.getOperatorId());
        purchaseClearLoseGoodsRequest.setCustomerVO(commonUtil.getCustomer());
        DistributeChannel channel = commonUtil.getDistributeChannel();
        channel.setInviteeId(commonUtil.getPurchaseInviteeId());
        purchaseClearLoseGoodsRequest.setDistributeChannel(channel);
        purchaseProvider.clearLoseGoods(purchaseClearLoseGoodsRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 采购单商品移入收藏夹
     *
     * @param queryRequest
     * @return
     */
    @ApiOperation(value = "采购单商品移入收藏夹")
    @RequestMapping(value = "/addFollow", method = RequestMethod.PUT)
    public BaseResponse addFollow(@RequestBody PurchaseAddFollowRequest queryRequest) {
        queryRequest.setCustomerId(commonUtil.getOperatorId());

        queryRequest.setInviteeId(commonUtil.getPurchaseInviteeId());
        purchaseProvider.addFollow(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 合并登录前后采购单
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "合并登录前后采购单")
    @RequestMapping(value = "/mergePurchase", method = RequestMethod.POST)
    public BaseResponse mergePurchase(@RequestBody @Valid PurchaseMergeRequest request) {
        if (CollectionUtils.isEmpty(request.getPurchaseMergeDTOList())) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
        }
        List<String> goodsInfoIds = request.getPurchaseMergeDTOList().stream().map(PurchaseMergeDTO::getGoodsInfoId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(goodsInfoIds)) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
        }
        request.setCustomer(KsBeanUtil.convert(commonUtil.getCustomer(), CustomerDTO.class));
        request.setInviteeId(commonUtil.getPurchaseInviteeId());
        request.setTerminalSource(commonUtil.getTerminal());
        purchaseProvider.mergePurchase(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 未登录时,计算采购单中参加同种营销的商品列表/总额/优惠
     *
     * @param marketingId
     * @return
     */
    @ApiOperation(value = "未登录时,计算采购单中参加同种营销的商品列表/总额/优惠")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/{marketingId}/calcMarketingByMarketingIdFront", method = RequestMethod.POST)
    public BaseResponse<PurchaseCalcMarketingResponse> calcMarketingByMarketingIdFront(
            @PathVariable Long marketingId, @RequestBody @Valid PurchaseFrontRequest queryRequest) {
        PurchaseCalcMarketingRequest request = new PurchaseCalcMarketingRequest();
        request.setMarketingId(marketingId);
        request.setFrontRequest(queryRequest);
        request.setGoodsInfoIds(queryRequest.getGoodsInfoIds());
        request.setIsPurchase(false);
        return purchaseProvider.calcMarketingByMarketingId(request);
    }

    /**
     * 计算采购单中参加同种营销的商品列表/总额/优惠
     *
     * @param marketingId
     * @return
     */
    @ApiOperation(value = "计算采购单中参加同种营销的商品列表/总额/优惠")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "marketingId", value = "营销Id", required = true)
    @RequestMapping(value = "/{marketingId}/calcMarketingByMarketingId", method = RequestMethod.GET)
    public BaseResponse<PurchaseCalcMarketingResponse> calcMarketingByMarketingId(@PathVariable Long marketingId) {
        //获取会员
        CustomerVO customer = commonUtil.getCustomer();
        PurchaseCalcMarketingRequest purchaseCalcMarketingRequest = new PurchaseCalcMarketingRequest();
        purchaseCalcMarketingRequest.setCustomer(KsBeanUtil.convert(customer, CustomerDTO.class));
        purchaseCalcMarketingRequest.setGoodsInfoIds(null);
        purchaseCalcMarketingRequest.setIsPurchase(false);
        purchaseCalcMarketingRequest.setMarketingId(marketingId);
        return BaseResponse.success(purchaseProvider.calcMarketingByMarketingId(purchaseCalcMarketingRequest).getContext());
    }

    /**
     * 修改商品选择的营销
     *
     * @param goodsInfoId
     * @param marketingId
     * @return
     */
    @ApiOperation(value = "修改商品选择的营销")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Map",
                    name = "goodsInfoId", value = "sku Id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long",
                    name = "marketingId", value = "营销Id", required = true)
    })
    @RequestMapping(value = "/goodsMarketing/{goodsInfoId}/{marketingId}", method = RequestMethod.PUT)
    @GlobalTransactional
    public BaseResponse modifyGoodsMarketing(@PathVariable String goodsInfoId, @PathVariable Long marketingId) {
        if (goodsInfoId == null || marketingId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        //获取会员
        CustomerVO customer = commonUtil.getCustomer();
        PurchaseModifyGoodsMarketingRequest purchaseModifyGoodsMarketingRequest =
                new PurchaseModifyGoodsMarketingRequest();
        purchaseModifyGoodsMarketingRequest.setCustomer(KsBeanUtil.convert(customer, CustomerDTO.class));
        purchaseModifyGoodsMarketingRequest.setGoodsInfoId(goodsInfoId);
        purchaseModifyGoodsMarketingRequest.setMarketingId(marketingId);
        purchaseProvider.modifyGoodsMarketing(purchaseModifyGoodsMarketingRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 获取采购单所有商品使用的营销
     *
     * @return
     */
    @ApiOperation(value = "获取采购单所有商品使用的营销")
    @RequestMapping(value = "/goodsMarketing", method = RequestMethod.GET)
    public BaseResponse<List<GoodsMarketingVO>> queryGoodsMarketingList() {
        PurchaseQueryGoodsMarketingListRequest purchaseQueryGoodsMarketingListRequest =
                new PurchaseQueryGoodsMarketingListRequest();
        purchaseQueryGoodsMarketingListRequest.setCustomerId(commonUtil.getOperatorId());
        PurchaseQueryGoodsMarketingListResponse purchaseQueryGoodsMarketingListResponse =
                purchaseQueryProvider.queryGoodsMarketingList(purchaseQueryGoodsMarketingListRequest).getContext();
        return BaseResponse.success(purchaseQueryGoodsMarketingListResponse.getGoodsMarketingList());
    }


    /**
     * 设置限售数据
     *
     * @param goodsInfoVOS
     * @param customerVO
     * @return
     */
    private List<GoodsInfoVO> setRestrictedNum(List<GoodsInfoVO> goodsInfoVOS, CustomerVO customerVO) {
        if (CollectionUtils.isNotEmpty(goodsInfoVOS)) {
            List<GoodsRestrictedValidateVO> goodsRestrictedValidateVOS = new ArrayList<>();
            goodsInfoVOS.stream().forEach(g -> {
                GoodsRestrictedValidateVO rvv = new GoodsRestrictedValidateVO();
                rvv.setNum(g.getBuyCount());
                rvv.setSkuId(g.getGoodsInfoId());
                goodsRestrictedValidateVOS.add(rvv);
            });
            GoodsRestrictedSalePurchaseResponse response = goodsRestrictedSaleQueryProvider.validatePurchaseRestricted(
                    GoodsRestrictedBatchValidateRequest.builder()
                            .goodsRestrictedValidateVOS(goodsRestrictedValidateVOS)
                            .customerVO(customerVO)
                            .build()).getContext();
            if (Objects.nonNull(response) && CollectionUtils.isNotEmpty(response.getGoodsRestrictedPurchaseVOS())) {
                List<GoodsRestrictedPurchaseVO> goodsRestrictedPurchaseVOS = response.getGoodsRestrictedPurchaseVOS();
                Map<String, GoodsRestrictedPurchaseVO> purchaseMap = goodsRestrictedPurchaseVOS.stream().collect((Collectors.toMap(GoodsRestrictedPurchaseVO::getGoodsInfoId, g -> g)));
                goodsInfoVOS.stream().forEach(g -> {
                    GoodsRestrictedPurchaseVO goodsRestrictedPurchaseVO = purchaseMap.get(g.getGoodsInfoId());
                    if (Objects.nonNull(goodsRestrictedPurchaseVO)) {
                        if (DefaultFlag.YES.equals(goodsRestrictedPurchaseVO.getDefaultFlag())) {
                            g.setMaxCount(goodsRestrictedPurchaseVO.getRestrictedNum());
                            g.setCount(goodsRestrictedPurchaseVO.getStartSaleNum());
                        } else {
                            //限售没有资格购买时，h5需要的商品状态是正常
                            g.setMaxCount(0L);
                            //g.setGoodsStatus(GoodsStatus.INVALID);
                        }
                    }
                });
            }
            return goodsInfoVOS;
        }
        return goodsInfoVOS;
    }

    /**
     * 获取购物车信息
     */
    @ApiOperation(value = "获取购物车信息")
    @RequestMapping(value = "/purchaseInfo", method = RequestMethod.POST)
    public BaseResponse<PurchaseListResponse> purchaseInfo(@RequestBody PurchaseListRequest request) {
        return purchaseQueryProvider.purchaseInfo(PurchaseInfoRequest.builder()
                .customer(commonUtil.getCustomer())
                .areaId(request.getAreaId())
                .inviteeId(commonUtil.getPurchaseInviteeId()).build());
    }


    @ApiOperation(value = "获取购物车信息（开放访问）")
    @RequestMapping(value = "/front/purchaseInfo", method = RequestMethod.POST)
    public BaseResponse<PurchaseListResponse> purchaseInfo(@RequestBody @Valid PurchaseInfoRequest request) {
        return purchaseQueryProvider.purchaseInfo(PurchaseInfoRequest.builder()
                .goodsInfoIds(request.getGoodsInfoIds())
                .inviteeId(commonUtil.getPurchaseInviteeId()).build());
    }

}
