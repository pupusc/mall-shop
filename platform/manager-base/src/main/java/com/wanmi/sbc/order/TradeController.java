package com.wanmi.sbc.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.aop.PageNumCheck;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.OsUtil;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreInfoByIdRequest;
import com.wanmi.sbc.customer.api.request.store.ValidStoreByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.store.StoreInfoResponse;
import com.wanmi.sbc.customer.bean.dto.CompanyInfoDTO;
import com.wanmi.sbc.customer.bean.dto.StoreInfoDTO;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedsale.GoodsRestrictedSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedBatchValidateRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByIdsResponse;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedValidateVO;
import com.wanmi.sbc.linkedmall.api.provider.order.LinkedMallOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.order.SbcLogisticsQueryRequest;
import com.wanmi.sbc.linkedmall.api.response.order.SbcLogisticsQueryResponse;
import com.wanmi.sbc.order.api.enums.OrderTagEnum;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.thirdplatformtrade.ThirdPlatformTradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeProvider;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.payorder.FindPayOrderRequest;
import com.wanmi.sbc.order.api.request.trade.FindProviderTradeRequest;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeErpRequest;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeRemedyBuyerRemarkRequest;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeVerifyAfterProcessingRequest;
import com.wanmi.sbc.order.api.request.trade.ThirdPlatformTradeListByTradeIdsRequest;
import com.wanmi.sbc.order.api.request.trade.TradeAddBatchRequest;
import com.wanmi.sbc.order.api.request.trade.TradeConfirmReceiveRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDefaultPayRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDeliverRecordObsoleteRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDeliverRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDeliveryCheckRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetRemedyByTidRequest;
import com.wanmi.sbc.order.api.request.trade.TradeListExportRequest;
import com.wanmi.sbc.order.api.request.trade.TradeModifyPriceRequest;
import com.wanmi.sbc.order.api.request.trade.TradeModifyRemedyRequest;
import com.wanmi.sbc.order.api.request.trade.TradePageCriteriaRequest;
import com.wanmi.sbc.order.api.request.trade.TradeParamsRequest;
import com.wanmi.sbc.order.api.request.trade.TradePushRequest;
import com.wanmi.sbc.order.api.request.trade.TradeRemedyPartRequest;
import com.wanmi.sbc.order.api.request.trade.TradeRemedySellerRemarkRequest;
import com.wanmi.sbc.order.api.request.trade.TradeRetrialRequest;
import com.wanmi.sbc.order.api.request.trade.TradeVerifyAfterProcessingRequest;
import com.wanmi.sbc.order.api.request.trade.TradeWrapperBackendCommitRequest;
import com.wanmi.sbc.order.api.response.payorder.FindPayOrderResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetFreightResponse;
import com.wanmi.sbc.order.bean.dto.DisabledDTO;
import com.wanmi.sbc.order.bean.dto.ProviderTradeRemedyDTO;
import com.wanmi.sbc.order.bean.dto.SupplierDTO;
import com.wanmi.sbc.order.bean.dto.TradeAddDTO;
import com.wanmi.sbc.order.bean.dto.TradeCreateDTO;
import com.wanmi.sbc.order.bean.dto.TradeDeliverDTO;
import com.wanmi.sbc.order.bean.dto.TradeDeliverRequestDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.dto.TradePriceChangeDTO;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.dto.TradeRemedyDTO;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.enums.QueryOrderType;
import com.wanmi.sbc.order.bean.enums.ShipperType;
import com.wanmi.sbc.order.bean.vo.DeliverCalendarVO;
import com.wanmi.sbc.order.bean.vo.ProviderTradeExportVO;
import com.wanmi.sbc.order.bean.vo.ThirdPlatformTradeVO;
import com.wanmi.sbc.order.bean.vo.TradeCycleBuyInfoVO;
import com.wanmi.sbc.order.bean.vo.TradeDeliverVO;
import com.wanmi.sbc.order.bean.vo.TradeItemGroupVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeRemedyDetailsVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.request.DisabledExportRequest;
import com.wanmi.sbc.order.request.TradeAuditBatchRequest;
import com.wanmi.sbc.order.request.TradeAuditRequest;
import com.wanmi.sbc.order.request.TradeExportRequest;
import com.wanmi.sbc.order.service.ProviderTradeExportBaseService;
import com.wanmi.sbc.order.service.TradeExportService;
import com.wanmi.sbc.setting.api.provider.DeliveryQueryProvider;
import com.wanmi.sbc.setting.api.provider.expresscompany.ExpressCompanyQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.DeliveryQueryRequest;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyByIdRequest;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.LogisticsRopResponse;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/4/19.
 */
@Api(tags = "TradeController", description = "订单服务 Api")
@RestController
@RequestMapping("/trade")
@Slf4j
@Validated
public class TradeController {

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private ProviderTradeProvider providerTradeProvider;

    @Autowired
    private ProviderTradeQueryProvider providerTradeQueryProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private TradeExportService tradeExportService;

    @Autowired
    private ProviderTradeExportBaseService providerTradeExportBaseService;

    @Autowired
    private OsUtil osUtil;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private PayOrderQueryProvider payOrderQueryProvider;

    @Autowired
    private DeliveryQueryProvider deliveryQueryProvider;

    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    @Autowired
    private ExpressCompanyQueryProvider expressCompanyQueryProvider;

    @Autowired
    private GoodsRestrictedSaleQueryProvider goodsRestrictedSaleQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private LinkedMallOrderQueryProvider linkedMallOrderQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    @Autowired
    private ThirdPlatformTradeQueryProvider thirdPlatformTradeQueryProvider;

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private GuanyierpProvider guanyierpProvider;


    private AtomicInteger exportCount = new AtomicInteger(0);

    /**
     * 分页查询
     *
     * @param tradeQueryRequest
     * @return
     */
    @ApiOperation(value = "分页查询")
    @EmployeeCheck
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<TradeVO>> page(@RequestBody TradeQueryDTO tradeQueryRequest) {
        Long companyInfoId = commonUtil.getCompanyInfoId();
        if (companyInfoId != null) {
            tradeQueryRequest.setSupplierId(companyInfoId);
        }

        if (!tradeQueryRequest.getIsBoss()) {
            if (Objects.nonNull(tradeQueryRequest.getTradeState()) && Objects.nonNull(tradeQueryRequest.getTradeState
                    ().getPayState())) {
                tradeQueryRequest.setNotFlowStates(Arrays.asList(FlowState.VOID, FlowState.INIT));
            }
        }
        //设定状态条件逻辑,已审核状态需筛选出已审核与部分发货
        tradeQueryRequest.makeAllAuditFlow();
        return BaseResponse.success(tradeQueryProvider.pageCriteria(TradePageCriteriaRequest.builder().tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage());
    }

    /**
     * 分页查询supplier
     *
     * @param tradeQueryRequest
     * @return
     */
    @ApiOperation(value = "分页查询")
    @EmployeeCheck
    @PageNumCheck
    @RequestMapping(value = "/supplierPage", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<TradeVO>> supplierPage(@RequestBody TradeQueryDTO tradeQueryRequest) {
        if (QueryOrderType.GIFT_ORDER.equals(tradeQueryRequest.getQueryOrderType())) {
            tradeQueryRequest.setQueryOrderType(null);
            tradeQueryRequest.setTag(OrderTagEnum.GIFT.getCode());
        }

        Long companyInfoId = commonUtil.getCompanyInfoId();
        if (companyInfoId != null) {
            tradeQueryRequest.setSupplierId(companyInfoId);
        }

        if (!tradeQueryRequest.getIsBoss()) {
            if (Objects.nonNull(tradeQueryRequest.getTradeState()) && Objects.nonNull(tradeQueryRequest.getTradeState
                    ().getPayState())) {
                tradeQueryRequest.setNotFlowStates(Arrays.asList(FlowState.VOID, FlowState.INIT));
            }
        }
        //设定状态条件逻辑,已审核状态需筛选出已审核与部分发货
        tradeQueryRequest.makeAllAuditFlow();

        MicroServicePage<TradeVO> microServicePage = tradeQueryProvider.supplierPageCriteria(TradePageCriteriaRequest.builder().tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage();

        List<TradeVO> tradeVOList = microServicePage.getContent();
        tradeVOList.forEach(tradeVO -> {
            if (tradeVO.getCycleBuyFlag()) {
                TradeCycleBuyInfoVO tradeCycleBuyInfo = tradeVO.getTradeCycleBuyInfo();
                List<DeliverCalendarVO> deliverCalendarVOList=tradeCycleBuyInfo.getDeliverCalendar().stream().filter(deliverCalendarVO -> deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.NOT_SHIPPED
                        || deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.PUSHED
                        || deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.PUSHED).collect(Collectors.toList());
                //下期发货信息
                if (CollectionUtils.isNotEmpty(deliverCalendarVOList)) {
                    DeliverCalendarVO deliverCalendarVO= deliverCalendarVOList.get(0);
                    //获取周几
                    Integer week=deliverCalendarVO.getDeliverDate().getDayOfWeek().getValue();
                    tradeCycleBuyInfo.setWeek(week);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    //获取下一期发货的日期
                    String localTime = df.format(deliverCalendarVO.getDeliverDate());
                    tradeCycleBuyInfo.setLocalTime(localTime);
                    //下一期期数,过滤掉只有赠品的发货记录
                    List<TradeDeliverVO> tradeDelivers=tradeVO.getTradeDelivers().stream().filter(tradeDeliverVO ->CollectionUtils.isNotEmpty(tradeDeliverVO.getShippingItems())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(tradeDelivers)) {
                        TradeDeliverVO tradeDeliverVO= tradeDelivers.get(0);
                        if(Objects.nonNull(tradeDeliverVO.getCycleNum())) {
                            tradeCycleBuyInfo.setNumberPeriods(tradeDeliverVO.getCycleNum()+1);
                        }
                    }else {
                        tradeCycleBuyInfo.setNumberPeriods(1);
                    }
                }
            }
        });
        return BaseResponse.success(microServicePage);
    }

    /**
     * boss分页查询
     *
     * @param tradeQueryRequest
     * @return
     */
    @ApiOperation(value = "分页查询")
    @EmployeeCheck
    @PageNumCheck
    @RequestMapping(value = "/bossPage", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<TradeVO>> bossPage(@RequestBody TradeQueryDTO tradeQueryRequest) {
        Long companyInfoId = commonUtil.getCompanyInfoId();
        if (companyInfoId != null) {
            tradeQueryRequest.setSupplierId(companyInfoId);
        }

        if (!tradeQueryRequest.getIsBoss()) {
            if (Objects.nonNull(tradeQueryRequest.getTradeState()) && Objects.nonNull(tradeQueryRequest.getTradeState
                    ().getPayState())) {
                tradeQueryRequest.setNotFlowStates(Arrays.asList(FlowState.VOID, FlowState.INIT));
            }
        }
        //设定状态条件逻辑,已审核状态需筛选出已审核与部分发货
        tradeQueryRequest.makeAllAuditFlow();

        MicroServicePage<TradeVO> microServicePage = tradeQueryProvider.pageBossCriteria(TradePageCriteriaRequest.builder().tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage();

        List<TradeVO> tradeVOList = microServicePage.getContent();
        tradeVOList.forEach(tradeVO -> {
            if (tradeVO.getCycleBuyFlag() && Objects.nonNull(tradeVO.getTradeCycleBuyInfo())) {
                TradeCycleBuyInfoVO tradeCycleBuyInfo = tradeVO.getTradeCycleBuyInfo();
                List<DeliverCalendarVO> deliverCalendarVOList=tradeCycleBuyInfo.getDeliverCalendar().stream().filter(deliverCalendarVO -> deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.NOT_SHIPPED ||
                        deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.PUSHED || deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.PUSHED_FAIL ).collect(Collectors.toList());
                //下期发货信息
                if (CollectionUtils.isNotEmpty(deliverCalendarVOList)) {
                    DeliverCalendarVO deliverCalendarVO= deliverCalendarVOList.get(0);
                    //获取周几
                    Integer week=deliverCalendarVO.getDeliverDate().getDayOfWeek().getValue();
                    tradeCycleBuyInfo.setWeek(week);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    //获取下一期发货的日期
                    String localTime = df.format(deliverCalendarVO.getDeliverDate());
                    tradeCycleBuyInfo.setLocalTime(localTime);
                    //下一期期数,过滤掉只有赠品的发货记录
                    List<TradeDeliverVO> tradeDelivers=tradeVO.getTradeDelivers().stream().filter(tradeDeliverVO ->CollectionUtils.isNotEmpty(tradeDeliverVO.getShippingItems())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(tradeDelivers)) {
                        TradeDeliverVO tradeDeliverVO= tradeDelivers.get(0);
                        if(Objects.nonNull(tradeDeliverVO.getCycleNum())) {
                            tradeCycleBuyInfo.setNumberPeriods(tradeDeliverVO.getCycleNum()+1);
                        }
                    }else {
                        tradeCycleBuyInfo.setNumberPeriods(1);
                    }
                }
            }
        });
        return BaseResponse.success(microServicePage);
    }


    /**
     * 根据参数查询某订单的运费
     *
     * @param tradeParams
     * @return
     */
    @ApiOperation(value = "根据参数查询某订单的运费")
    @RequestMapping(value = "/getFreight", method = RequestMethod.POST)
    public BaseResponse<TradeGetFreightResponse> getFreight(@RequestBody TradeParamsRequest tradeParams) {
        Operator operator = commonUtil.getOperator();
        StoreVO store = storeQueryProvider.getValidStoreById(new ValidStoreByIdRequest(Long.parseLong(operator
                .getStoreId()))).getContext().getStoreVO();
        tradeParams.setSupplier(SupplierDTO.builder().storeId(store.getStoreId()).freightTemplateType(store
                .getFreightTemplateType()).build());

        return BaseResponse.success(tradeQueryProvider.getFreight(tradeParams).getContext());
    }


    /**
     * 创建订单
     *
     * @param tradeCreateRequest
     * @return
     */
    @ApiOperation(value = "创建订单")
    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> create(@RequestBody @Valid TradeCreateDTO tradeCreateRequest) {
        log.info("商家开始代客下单......");
        Operator operator = commonUtil.getOperator();
        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(commonUtil.getCompanyInfoId()).build()
        ).getContext();
        Long storeId = commonUtil.getStoreId();
        StoreInfoResponse storeInfoResponse = storeQueryProvider.getStoreInfoById(new StoreInfoByIdRequest(storeId))
                .getContext();

        //1.校验与包装订单信息-与业务员app代客下单公用
        log.info("开始校验与包装订单信息......");
        TradeVO trade = tradeQueryProvider.wrapperBackendCommit(TradeWrapperBackendCommitRequest.builder().operator(operator)
                .companyInfo(KsBeanUtil.convert(companyInfo, CompanyInfoDTO.class)).storeInfo(KsBeanUtil.convert(storeInfoResponse, StoreInfoDTO.class))
                .tradeCreate(tradeCreateRequest).build()).getContext().getTradeVO();

        //2.订单入库(转换成list,传入批量创建订单的service方法,同一套逻辑,能够回滚)
        TradeAddBatchRequest tradeAddBatchRequest = TradeAddBatchRequest.builder().tradeDTOList(Collections.singletonList(KsBeanUtil.convert(trade, TradeAddDTO.class, SerializerFeature.DisableCircularReferenceDetect)))
                .operator(operator)
                .build();


        // 3.限售校验
        log.info("校验与包装订单信息结束，开始校验订单中的限售商品......");
        List<TradeItemDTO> tradeItems = tradeCreateRequest.getTradeItems().stream().map(
                o -> TradeItemDTO.builder().skuId(o.getSkuId()).num(o.getNum())
                        .isAppointmentSaleGoods(o.getIsAppointmentSaleGoods()).appointmentSaleId(o.getAppointmentSaleId())
                        .isBookingSaleGoods(o.getIsBookingSaleGoods()).bookingSaleId(o.getBookingSaleId())
                        .build()
        ).collect(Collectors.toList());
        TradeItemGroupVO tradeItemGroupVOS = new TradeItemGroupVO();
        tradeItemGroupVOS.setTradeItems(KsBeanUtil.convert(tradeItems, TradeItemVO.class));
        CustomerGetByIdResponse customerGetByIdResponse =
                customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(tradeCreateRequest.getCustom()))
                        .getContext();
        this.validateRestrictedGoods(tradeItemGroupVOS, customerGetByIdResponse);

        // 4.校验积分价商品
        log.info("限售校验结束，开始校验订单中的积分价商品......");
        List<String> goodsInfoIds =
                tradeCreateRequest.getTradeItems().stream().map(TradeItemDTO::getSkuId).collect(Collectors.toList());
        GoodsInfoListByIdsResponse byIdsResponse =
                goodsInfoQueryProvider.listIntegralPriceGoodsByIds(GoodsInfoListByIdsRequest.builder().goodsInfoIds(goodsInfoIds).build()).getContext();
        if (CollectionUtils.isNotEmpty(byIdsResponse.getGoodsInfos())) {
            return ResponseEntity.ok(BaseResponse.info("K-030801",
                    "不可购买积分价格商品", byIdsResponse.getGoodsInfos()));
        }

        log.info("积分价商品校验结束，开始创建订单......");
        tradeProvider.addBatch(tradeAddBatchRequest);

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());

    }

    /**
     * 用于修改订单前的展示订单信息
     *
     * @param tid 订单id
     * @return 返回信息 {@link TradeRemedyDetailsVO}
     */
    @ApiOperation(value = "用于修改订单前的展示订单信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/remedy/{tid}", method = RequestMethod.GET)
    public BaseResponse<TradeRemedyDetailsVO> remedy(@PathVariable String tid) {
        checkOperatorByTrade(tid);
        return BaseResponse.success(tradeQueryProvider.getRemedyByTid(TradeGetRemedyByTidRequest.builder().tid(tid).build()).getContext().getTradeRemedyDetailsVO());
    }

    /**
     * 修改订单
     *
     * @param tradeRemedyRequest
     * @return
     */
    @ApiOperation(value = "修改订单")
    @RequestMapping(value = "/remedy", method = RequestMethod.PUT)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> remedy(@RequestBody @Valid TradeRemedyDTO tradeRemedyRequest) {
        Operator operator = commonUtil.getOperator();
        if ((osUtil.isS2b() && operator.getPlatform() != Platform.SUPPLIER) || (osUtil.isB2b() && operator.getPlatform()
                != Platform.BOSS)) {
            throw new SbcRuntimeException("K-000018");
        }
        Long storeId = commonUtil.getStoreId();
        StoreInfoResponse storeInfoResponse = storeQueryProvider.getStoreInfoById(new StoreInfoByIdRequest(storeId))
                .getContext();
        TradeModifyRemedyRequest tradeModifyRemedyRequest = TradeModifyRemedyRequest.builder()
                .tradeRemedyDTO(KsBeanUtil.convert(tradeRemedyRequest, TradeRemedyDTO.class))
                .operator(operator)
                .storeInfoDTO(KsBeanUtil.convert(storeInfoResponse, StoreInfoDTO.class))
                .build();
        tradeProvider.remedy(tradeModifyRemedyRequest);

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 修改订单（不修改商品、营销相关信息）
     *
     * @param tradeRemedyRequest
     * @return
     */
    @ApiOperation(value = "修改订单（不修改商品、营销相关信息）")
    @RequestMapping(value = "/remedy-part", method = RequestMethod.PUT)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> remedyPart(@RequestBody @Valid TradeRemedyDTO tradeRemedyRequest) {
        Operator operator = commonUtil.getOperator();
        if ((osUtil.isS2b() && operator.getPlatform() != Platform.SUPPLIER) || (osUtil.isB2b() && operator.getPlatform()
                != Platform.BOSS)) {
            throw new SbcRuntimeException("K-000018");
        }
        Long storeId = commonUtil.getStoreId();
        StoreInfoResponse storeInfoResponse = storeQueryProvider.getStoreInfoById(new StoreInfoByIdRequest(storeId))
                .getContext();

        TradeRemedyPartRequest tradeRemedyPartRequest = TradeRemedyPartRequest.builder()
                .tradeRemedyDTO(tradeRemedyRequest)
                .operator(operator)
                .storeInfoDTO(KsBeanUtil.convert(storeInfoResponse, StoreInfoDTO.class))
                .build();

        tradeProvider.remedyPart(tradeRemedyPartRequest);
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 校验商品限售信息
     *
     * @param tradeItemGroupVO
     * @param customerVO
     */
    private void validateRestrictedGoods(TradeItemGroupVO tradeItemGroupVO, CustomerVO customerVO) {
        //组装请求的数据
        List<TradeItemVO> tradeItemVOS = tradeItemGroupVO.getTradeItems();
        List<GoodsRestrictedValidateVO> list = KsBeanUtil.convert(tradeItemVOS, GoodsRestrictedValidateVO.class);
        Boolean openGroup = false;
        if (Objects.nonNull(tradeItemGroupVO.getGrouponForm()) && Objects.nonNull(tradeItemGroupVO.getGrouponForm().getOpenGroupon())) {
            openGroup = tradeItemGroupVO.getGrouponForm().getOpenGroupon();
        }
        goodsRestrictedSaleQueryProvider.validateOrderRestricted(GoodsRestrictedBatchValidateRequest.builder()
                .goodsRestrictedValidateVOS(list)
                .snapshotType(tradeItemGroupVO.getSnapshotType())
                .customerVO(customerVO)
                .openGroupon(openGroup)
                .build());
    }

    /**
     * 描述：    订单改价
     * 场景：    业务员助手|商家端针对订单运费和总金额重新设价
     *
     * @param request 改价参数结构
     * @return
     */
    @ApiOperation(value = "订单改价")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/price/{tid}", method = RequestMethod.PUT)
    @GlobalTransactional
    public BaseResponse changePrice(@PathVariable String tid, @RequestBody @Valid TradePriceChangeDTO request) {
        Operator operator = commonUtil.getOperator();
        if ((osUtil.isS2b() && operator.getPlatform() != Platform.SUPPLIER) || (osUtil.isB2b() && operator.getPlatform()
                != Platform.BOSS)) {
            throw new SbcRuntimeException("K-000018");
        }
        checkOperatorByTrade(tid);
        TradeModifyPriceRequest tradeModifyPriceRequest = TradeModifyPriceRequest.builder()
                .tradePriceChangeDTO(request)
                .tid(tid)
                .operator(operator)
                .build();

        tradeProvider.modifyPrice(tradeModifyPriceRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询订单付款记录
     */
    @ApiOperation(value = "查询订单付款记录")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/payOrder/{tid}", method = RequestMethod.GET)
    public BaseResponse<FindPayOrderResponse> payOrder(@PathVariable String tid) {
        TradeVO trade;
        if (commonUtil.getOperator().getPlatform().equals(Platform.SUPPLIER)) {
            trade = checkOperatorByTrade(tid);
        } else {
            trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        }

        FindPayOrderResponse payOrderResponse = null;
        try {
            BaseResponse<FindPayOrderResponse> response = payOrderQueryProvider.findPayOrder(FindPayOrderRequest.builder().value(trade.getId()).build());

            payOrderResponse = response.getContext();

        } catch (SbcRuntimeException e) {
            if ("K-070001".equals(e.getErrorCode())) {
                payOrderResponse = new FindPayOrderResponse();
                payOrderResponse.setPayType(PayType.fromValue(Integer.valueOf(trade.getPayInfo().getPayTypeId())));
                payOrderResponse.setTotalPrice(trade.getTradePrice().getTotalPrice());
            }
        }
        return BaseResponse.success(payOrderResponse);
    }

    /**
     * 修改卖家备注
     *
     * @param tid
     * @param tradeRemedyRequest
     * @return
     */
    @ApiOperation(value = "修改卖家备注")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/remark/{tid}", method = RequestMethod.POST)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> sellerRemark(@PathVariable String tid, @RequestBody TradeRemedyDTO
            tradeRemedyRequest) {
        checkOperatorByTrade(tid);
        TradeRemedySellerRemarkRequest tradeRemedySellerRemarkRequest = TradeRemedySellerRemarkRequest.builder()
                .sellerRemark(tradeRemedyRequest.getSellerRemark())
                .tid(tid)
                .operator(commonUtil.getOperator())
                .build();

        tradeProvider.remedySellerRemark(tradeRemedySellerRemarkRequest);

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 修改子单备注(供应商订单)
     *
     * @param tid
     * @param tradeRemedyRequest
     * @return
     */
    @ApiOperation(value = "修改子单备注(供应商订单)")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/provider/remark/{tid}", method = RequestMethod.POST)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> sellerProviderTradeRemark(@PathVariable String tid, @RequestBody ProviderTradeRemedyDTO
            tradeRemedyRequest) {
        checkOperatorByTrade(tid);
        ProviderTradeRemedyBuyerRemarkRequest tradeRemedySellerRemarkRequest = ProviderTradeRemedyBuyerRemarkRequest.builder()
                .buyRemark(tradeRemedyRequest.getBuyerRemark())
                .tid(tid)
                .operator(commonUtil.getOperator())
                .build();

        providerTradeProvider.remedyBuyerRemark(tradeRemedySellerRemarkRequest);

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 审核订单
     *
     * @param tid
     * @param request 订单审核参数结构
     * @return
     */
    @ApiOperation(value = "审核订单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/audit/{tid}", method = RequestMethod.POST)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> audit(@PathVariable String tid, @RequestBody TradeAuditRequest request) {
        checkOperatorByTrade(tid);
        com.wanmi.sbc.order.api.request.trade.TradeAuditRequest tradeAuditRequest
                = com.wanmi.sbc.order.api.request.trade.TradeAuditRequest.builder()
                .tid(tid)
                .auditState(request.getAuditState())
                .reason(request.getReason())
                .operator(commonUtil.getOperator())
                .build();
        tradeProvider.audit(tradeAuditRequest);

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 批量审核订单
     *
     * @param request 批量审核请求参数结构
     * @return
     */
    @ApiOperation(value = "批量审核订单")
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> batchAudit(@RequestBody TradeAuditBatchRequest request) {


        com.wanmi.sbc.order.api.request.trade.TradeAuditBatchRequest tradeAuditBatchRequest =

                com.wanmi.sbc.order.api.request.trade.TradeAuditBatchRequest.builder()
                        .auditState(request.getAuditState())
                        .ids(request.getIds())
                        .reason(request.getReason())
                        .operator(commonUtil.getOperator())
                        .build();

        tradeProvider.auditBatch(tradeAuditBatchRequest);

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 导出订单
     *
     * @return
     */
    @ApiOperation(value = "导出订单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}/{encryptedable}", method = RequestMethod.GET)
    public void export(@PathVariable String encrypted, @PathVariable String encryptedable, HttpServletResponse response) {
        log.info("=================主单，订单导出====================");
        try {
            if (exportCount.incrementAndGet() > 1) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
            }

            String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
            String decryptedable = new String(Base64.getUrlDecoder().decode(encryptedable.getBytes()));
            TradeExportRequest tradeExportRequest = JSON.parseObject(decrypted, TradeExportRequest.class);
            DisabledExportRequest disabledExportRequest = JSON.parseObject(decryptedable, DisabledExportRequest.class);
            Operator operator = commonUtil.getOperator();
            log.debug(String.format("/trade/export/params, employeeId=%s", operator.getUserId()));

            String adminId = operator.getAdminId();
            if (StringUtils.isNotEmpty(adminId)) {
                tradeExportRequest.setSupplierId(Long.valueOf(adminId));
            }

            //设定状态条件逻辑,已审核状态需筛选出已审核与部分发货
            tradeExportRequest.makeAllAuditFlow();
            TradeQueryDTO tradeQueryDTO = KsBeanUtil.convert(tradeExportRequest, TradeQueryDTO.class);
            DisabledDTO disabledDTO = KsBeanUtil.convert(disabledExportRequest, DisabledDTO.class);

            List<TradeVO> trades = tradeQueryProvider.listTradeExport(TradeListExportRequest.builder().tradeQueryDTO(tradeQueryDTO).build()).getContext().getTradeVOList();

            //按下单时间降序排列
            Comparator<TradeVO> c = Comparator.comparing(a -> a.getTradeState().getCreateTime());
            trades = trades.stream().sorted(
                    c.reversed()
            ).collect(Collectors.toList());

            String headerKey = "Content-Disposition";
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName = String.format("批量导出订单_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("/trade/export/params, fileName={},", fileName, e);
            }
            String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
            response.setHeader(headerKey, headerValue);

            //只导出子订单
            List<String> parentIdList = new ArrayList<>();
            if (disabledDTO.getDisabled().equals("true")) {
                // 第三方渠道设置
                ThirdPlatformConfigResponse config = thirdPlatformConfigQueryProvider.get(
                        ThirdPlatformConfigByTypeRequest.builder().configType(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue()).build()).getContext();

                trades.forEach(vo -> {
                    parentIdList.add(vo.getId());
                });
                List<TradeVO> tradeVOList = providerTradeProvider.findByParentIdList(FindProviderTradeRequest.builder().parentId(parentIdList).build()).getContext().getTradeVOList();
//                System.out.println(")))))))))))))))))))))))))"+tradeVOList);
//                try {
//                    providerTradeExportBaseService.export(tradeVOList, response.getOutputStream(),
//                            Platform.PLATFORM.equals(operator.getPlatform()));
//                    response.flushBuffer();
//                } catch (IOException e) {
//                    throw new SbcRuntimeException(e);
//                }

                Map<String, String> thirdPlatformOrderIdMap = new HashMap<>();
                Map<String, String> outOrderIdMap = new HashMap<>();
                if (Objects.nonNull(config)) {
                    // 对接了第三方渠道（目前只有LinkedMall），获取第三方渠道订单信息
                    List<ThirdPlatformTradeVO> tradeList =
                            thirdPlatformTradeQueryProvider.listByTradeIds(ThirdPlatformTradeListByTradeIdsRequest.builder().tradeIds(parentIdList).build()).getContext().getTradeList();

                    // 获取商品对应的第三方渠道订单编号（LinkedMall订单号）和外部订单号（淘宝订单号）
                    tradeList.forEach(thirdPlatformTradeVO -> {
                        thirdPlatformTradeVO.getTradeItems().forEach(tradeItemVO -> {
                            thirdPlatformOrderIdMap.put(tradeItemVO.getSkuId(),
                                    CollectionUtils.isNotEmpty(thirdPlatformTradeVO.getThirdPlatformOrderIds()) ?
                                            thirdPlatformTradeVO.getThirdPlatformOrderIds().get(0) : null);
                            outOrderIdMap.put(tradeItemVO.getSkuId(),
                                    CollectionUtils.isNotEmpty(thirdPlatformTradeVO.getOutOrderIds()) ?
                                            thirdPlatformTradeVO.getOutOrderIds().get(0) : null);
                        });
                    });
                }

                // 遍历封装导出信息
                List<ProviderTradeExportVO> tradeExportVOs = new ArrayList<>();
                tradeVOList.forEach(tradeVO -> {
                    ProviderTradeExportVO exportVO;
                    // 商家信息
                    String supplierInfo = "";
                    String supplierName = "";
                    String supplierCode = "";
                    if (Objects.nonNull(tradeVO.getSupplier())) {
                        supplierName = StringUtils.isNotEmpty(tradeVO.getSupplier().getSupplierName()) ? tradeVO.getSupplier().getSupplierName() : "";
                        supplierCode = StringUtils.isNotEmpty(tradeVO.getSupplier().getSupplierCode()) ? tradeVO.getSupplier().getSupplierCode() : "";
                        supplierInfo = supplierName.concat(supplierCode);
                    }
                    for (int i = 0; i < tradeVO.getTradeItems().size(); i++) {
                        TradeItemVO tradeItemVO = tradeVO.getTradeItems().get(i);
                        exportVO = new ProviderTradeExportVO();
//                        if (i == 0) {
                        KsBeanUtil.copyProperties(tradeVO, exportVO);
                        // 下单时间
                        exportVO.setCreateTime(tradeVO.getTradeState().getCreateTime());
                        // 商家信息
                        exportVO.setSupplierInfo(supplierInfo);
                        // 供应商名称
                        exportVO.setSupplierName(supplierName);
                        // 订单商品金额
                        exportVO.setOrderGoodsPrice(tradeVO.getTradePrice().getTotalPrice());
                        // 订单状态
                        exportVO.setFlowState(tradeVO.getTradeState().getFlowState());
                        // 发货状态
                        exportVO.setDeliverStatus(tradeVO.getTradeState().getDeliverStatus());
                        exportVO.setConsigneeName(tradeVO.getConsignee().getName());
                        exportVO.setDetailAddress(tradeVO.getConsignee().getDetailAddress());
                        exportVO.setConsigneePhone(tradeVO.getConsignee().getPhone());
                        log.info("=================子单，订单导出==============GoodsType:{}====directChargeMobile:{}==",tradeItemVO.getGoodsType(),tradeVO.getDirectChargeMobile());
                        //订单导出新增充值手机号
                        if (GoodsType.VIRTUAL_GOODS.equals(tradeItemVO.getGoodsType()) || GoodsType.VIRTUAL_COUPON.equals(tradeItemVO.getGoodsType())) {
                            exportVO.setDirectChargeMobile(Objects.nonNull(tradeVO.getDirectChargeMobile()) ? tradeVO.getDirectChargeMobile() : null);
                        }else {
                            exportVO.setDirectChargeMobile(null);
                        }
//                        }
                        exportVO.setTotalNum(tradeVO.getTradeItems().stream().collect(Collectors.summingLong(TradeItemVO::getNum)));
                        exportVO.setGoodsSpecies(Long.valueOf(tradeVO.getTradeItems().stream().map(TradeItemVO::getCateId).collect(Collectors.toSet()).size()));
                        exportVO.setSkuName(tradeItemVO.getSkuName());
                        exportVO.setSpecDetails(tradeItemVO.getSpecDetails());
                        exportVO.setSkuNo(tradeItemVO.getSkuNo());
                        exportVO.setNum(tradeItemVO.getNum());

                        if (Objects.nonNull(config)) {
                            exportVO.setThirdPlatformOrderId(thirdPlatformOrderIdMap.get(tradeItemVO.getSkuId()));
                            exportVO.setOutOrderId(outOrderIdMap.get(tradeItemVO.getSkuId()));
                        }

                        tradeExportVOs.add(exportVO);
                    }
                });

                try {
                    providerTradeExportBaseService.exportProvider(tradeExportVOs, response.getOutputStream(),
                            Platform.BOSS, config);
                    response.flushBuffer();
                } catch (IOException e) {
                    throw new SbcRuntimeException(e);
                }

            } else {
                try {
                    List<TradeVO> tradesnNew = new ArrayList<>();
                    trades.forEach(tradeVO -> {
                        List<TradeItemVO> tradeItems = tradeVO.getTradeItems();
                        List<TradeItemVO> giftList = tradeVO.getGifts();
                        if (CollectionUtils.isNotEmpty(giftList)) {
                            tradeItems.addAll(giftList);
                        }
                        tradeItems.forEach(tradeItemVO -> {
                            TradeVO tradeVONew = new TradeVO();
                            KsBeanUtil.copyProperties(tradeVO, tradeVONew);
                            List<TradeItemVO> tradeItemsNew = new ArrayList<TradeItemVO>();
                            log.info("=================主单，订单导出==============GoodsType:{}====directChargeMobile:{}==",tradeItemVO.getGoodsType(),tradeVO.getDirectChargeMobile());
                            //订单导出新增充值手机号
                            if (GoodsType.VIRTUAL_GOODS.equals(tradeItemVO.getGoodsType()) || GoodsType.VIRTUAL_COUPON.equals(tradeItemVO.getGoodsType())) {
                                tradeVONew.setDirectChargeMobile(Objects.nonNull(tradeVO.getDirectChargeMobile())? tradeVO.getDirectChargeMobile():null);
                            }else {
                                tradeVONew.setDirectChargeMobile(null);
                            }
                            tradeItemsNew.add(tradeItemVO);
                            tradeVONew.setTradeItems(tradeItemsNew);
                            tradesnNew.add(tradeVONew);
                        });
                    });

                    tradeExportService.export(tradesnNew, response.getOutputStream(),
                            Platform.PLATFORM.equals(operator.getPlatform()));
                    response.flushBuffer();
//                    tradeExportService.export(trades, response.getOutputStream(),
//                            Platform.PLATFORM.equals(operator.getPlatform()));
//                    response.flushBuffer();
                } catch (IOException e) {
                    throw new SbcRuntimeException(e);
                }
            }
        } catch (Exception e) {
            log.error("/trade/export/params error: ", e);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000001);
        } finally {
            exportCount.set(0);
        }
    }


    /**
     * 查看订单详情
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "查看订单详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/{tid}", method = RequestMethod.GET)
    public BaseResponse<TradeVO> detail(@PathVariable String tid) {
        TradeVO trade =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).needLmOrder(Boolean.TRUE).build()).getContext().getTradeVO();
        Operator operator = commonUtil.getOperator();
        if (operator.getPlatform() == Platform.SUPPLIER && Objects.nonNull(trade.getId()) && !Objects.equals(commonUtil.getStoreId(), trade.getSupplier().getStoreId())) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        if(PayState.NOT_PAID == trade.getTradeState().getPayState()
                || PayState.PAID_EARNEST == trade.getTradeState().getPayState()){
            trade.getTradeItems().stream()
                    .forEach(t-> t.setVirtualCoupons(Collections.EMPTY_LIST) );
            trade.getGifts().stream()
                    .forEach(t-> t.setVirtualCoupons(Collections.EMPTY_LIST) );
        }

        return BaseResponse.success(trade);
    }


    /**
     * 发货
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "发货")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/deliver/{tid}", method = RequestMethod.PUT)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> deliver(@PathVariable String tid, @Valid @RequestBody TradeDeliverRequestDTO
            tradeDeliverRequest) {
        checkOperatorByTrade(tid);
        if (tradeDeliverRequest.getShippingItemList().isEmpty() && tradeDeliverRequest.getGiftItemList().isEmpty()) {
            throw new SbcRuntimeException("K-050314");
        }
        TradeDeliveryCheckRequest tradeDeliveryCheckRequest = TradeDeliveryCheckRequest.builder()
                .tid(tid)
                .tradeDeliver(tradeDeliverRequest)
                .build();

        tradeQueryProvider.deliveryCheck(tradeDeliveryCheckRequest);


//        CompositeResponse<ExpressCompany> response
//                = sdkClient.buildClientRequest().post(queryRopRequest, ExpressCompany.class, "expressCompany.detail",
//                "1.0.0");
//        if (!response.isSuccessful()) {
//            throw new SbcRuntimeException(ResultCode.FAILED);
//        }
//
        //发货校验
        ExpressCompanyByIdRequest request = new ExpressCompanyByIdRequest();
        request.setExpressCompanyId(Long.valueOf(tradeDeliverRequest.getDeliverId()));
        ExpressCompanyVO expressCompanyVO = expressCompanyQueryProvider.getById(request).getContext().getExpressCompanyVO();
        TradeDeliverVO tradeDeliver = tradeDeliverRequest.toTradeDevlier(expressCompanyVO);
        tradeDeliver.setShipperType(ShipperType.SUPPLIER);

        TradeDeliverRequest tradeDeliverRequest1 = TradeDeliverRequest.builder()
                .tradeDeliver(KsBeanUtil.convert(tradeDeliver, TradeDeliverDTO.class))
                .tid(tid)
                .operator(commonUtil.getOperator())
                .build();

        String deliverId = tradeProvider.deliver(tradeDeliverRequest1).getContext().getDeliverId();

        return ResponseEntity.ok(BaseResponse.success(deliverId));
    }


    /**
     * 子单(子单是商家的)发货
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "子单发货")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/provider/deliver/{tid}", method = RequestMethod.PUT)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> providerTradedeliver(@PathVariable String tid, @Valid @RequestBody TradeDeliverRequestDTO
            tradeDeliverRequest) {
        checkOperatorByTrade(tid);
        if (tradeDeliverRequest.getShippingItemList().isEmpty() && tradeDeliverRequest.getGiftItemList().isEmpty()) {
            throw new SbcRuntimeException("K-050314");
        }

        TradeDeliveryCheckRequest tradeDeliveryCheckRequest = TradeDeliveryCheckRequest.builder()
                .tid(tid)
                .tradeDeliver(tradeDeliverRequest)
                .build();

        providerTradeProvider.providerDeliveryCheck(tradeDeliveryCheckRequest);

        // 发货校验
        ExpressCompanyByIdRequest request = new ExpressCompanyByIdRequest();
        request.setExpressCompanyId(Long.valueOf(tradeDeliverRequest.getDeliverId()));
        ExpressCompanyVO expressCompanyVO = expressCompanyQueryProvider.getById(request).getContext().getExpressCompanyVO();
        TradeDeliverVO tradeDeliver = tradeDeliverRequest.toTradeDevlier(expressCompanyVO);
        tradeDeliver.setShipperType(ShipperType.SUPPLIER);

        TradeDeliverRequest deliverRequest = TradeDeliverRequest.builder()
                .tradeDeliver(KsBeanUtil.convert(tradeDeliver, TradeDeliverDTO.class))
                .tid(tid)
                .operator(commonUtil.getOperator())
                .build();
        // 供应商发货处理
        String deliverId = providerTradeProvider.providerDeliver(deliverRequest).getContext().getDeliverId();

        //此处mongo事务无法控制，将逻辑移至子单发货逻辑中
        // 供应商订单信息
        /*TradeVO privateTradeVO = providerTradeQueryProvider.providerGetById(
                TradeGetByIdRequest.builder()
                        .tid(tid)
                        .build())
                .getContext()
                .getTradeVO();

        // 查询所有子订单信息
        List<TradeVO> tradeVOList = providerTradeQueryProvider.getProviderListByParentId(
                TradeListByParentIdRequest.builder()
                        .parentTid(privateTradeVO.getParentId())
                        .build())
                .getContext()
                .getTradeVOList();

        // 未发货订单数
        long notYetShippedNum = tradeVOList.stream().filter(tradeVO -> tradeVO.getTradeState().getDeliverStatus().equals(DeliverStatus.NOT_YET_SHIPPED)).count();
        // 已发货订单数
        long shippedNum = tradeVOList.stream().filter(tradeVO -> tradeVO.getTradeState().getDeliverStatus().equals(DeliverStatus.SHIPPED)).count();

        // 父订单发货状态
        DeliverStatus deliverStatus;
        if ((int) notYetShippedNum == tradeVOList.size()){
            deliverStatus = DeliverStatus.NOT_YET_SHIPPED;
        } else if ((int) shippedNum == tradeVOList.size()) {
            deliverStatus = DeliverStatus.SHIPPED;
        } else {
            deliverStatus = DeliverStatus.PART_SHIPPED;
        }

        TradeDeliverDTO parentTradeDeliverDTO = KsBeanUtil.convert(tradeDeliver, TradeDeliverDTO.class);
        parentTradeDeliverDTO.setStatus(deliverStatus);
        parentTradeDeliverDTO.setSunDeliverId(deliverId);
        parentTradeDeliverDTO.setShipperType(ShipperType.SUPPLIER);

        // 添加商家发货信息
        tradeProvider.deliver(TradeDeliverRequest.builder()
                .tradeDeliver(parentTradeDeliverDTO)
                .tid(privateTradeVO.getParentId())
                .operator(commonUtil.getOperator())
                .build());*/

        return ResponseEntity.ok(BaseResponse.success(deliverId));
    }


    /**
     * 验证订单是否存在售后申请
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "验证订单是否存在售后申请")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/deliver/verify/{tid}", method = RequestMethod.GET)
    public BaseResponse deliverVerify(@PathVariable String tid) {
        checkOperatorByTrade(tid);
        if (tradeQueryProvider.verifyAfterProcessing(TradeVerifyAfterProcessingRequest.builder().tid(tid).build()).getContext().getVerifyResult()) {
            throw new SbcRuntimeException("K-050136", new Object[]{tid});
        }
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 验证订单是否存在售后申请
     *
     * @param ptid
     * @return
     */
    @ApiOperation(value = "验证订单是否存在售后申请")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "ptid", value = "订单id", required = true)
    @RequestMapping(value = "/provider/deliver/verify/{ptid}", method = RequestMethod.GET)
    public BaseResponse providerDeliverVerify(@PathVariable String ptid) {
        if (tradeQueryProvider.providerVerifyAfterProcessing(ProviderTradeVerifyAfterProcessingRequest.builder().ptid(ptid).build()).getContext().getVerifyResult()) {
            throw new SbcRuntimeException("K-050136", new Object[]{ptid});
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 发货作废
     *
     * @param tid
     * @param tdId
     * @return
     */
    @ApiOperation(value = "发货作废")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "tid", value = "订单Id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "tdId", value = "发货单Id", required = true)
    })
    @RequestMapping(value = "/deliver/{tid}/void/{tdId}", method = RequestMethod.GET)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> deliverVoid(@PathVariable String tid, @PathVariable String tdId) {
        checkOperatorByTrade(tid);
        tradeProvider.deliverRecordObsolete(TradeDeliverRecordObsoleteRequest.builder()
                .deliverId(tdId).tid(tid).operator(commonUtil.getOperator()).build());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 子订单发货作废
     *
     * @param tid
     * @param tdId
     * @return
     */
    @ApiOperation(value = "子订单发货作废")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "tid", value = "订单Id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "tdId", value = "发货单Id", required = true)
    })
    @RequestMapping(value = "/providerTrade/deliver/{tid}/void/{tdId}", method = RequestMethod.GET)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> providerDeliverVoid(@PathVariable String tid, @PathVariable String tdId,
                                                            HttpServletRequest req) {
        checkOperatorByTrade(tid);
        providerTradeProvider.deliverRecordObsolete(TradeDeliverRecordObsoleteRequest.builder()
                .deliverId(tdId).tid(tid).operator(commonUtil.getOperator()).build());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 确认收货
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "确认收货")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单Id", required = true)
    @RequestMapping(value = "/confirm/{tid}", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> confirm(@PathVariable String tid) {
        checkOperatorByTrade(tid);
        tradeProvider.confirmReceive(TradeConfirmReceiveRequest.builder().tid(tid).operator(commonUtil.getOperator()).build());

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 回审
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "回审")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单Id", required = true)
    @RequestMapping(value = "/retrial/{tid}", method = RequestMethod.GET)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> retrial(@PathVariable String tid) {
        checkOperatorByTrade(tid);
        tradeProvider.retrial(TradeRetrialRequest.builder().tid(tid).operator(commonUtil.getOperator()).build());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 0元订单默认支付
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "0元订单默认支付")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单Id", required = true)
    @RequestMapping(value = "/default/pay/{tid}", method = RequestMethod.GET)
    @GlobalTransactional
    public BaseResponse<Boolean> defaultPay(@PathVariable String tid) {
        checkOperatorByTrade(tid);
        return BaseResponse.success(tradeProvider.defaultPay(TradeDefaultPayRequest.builder()
                .tid(tid)
                .payWay(PayWay.UNIONPAY)
                .build())
                .getContext().getPayResult());
    }

    /**
     * 验证
     *
     * @param tid tid
     * @return boolean
     */
    @ApiOperation(value = "验证")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单Id", required = true)
    @RequestMapping(value = "/verifyAfterProcessing/{tid}")
    public Boolean verifyAfterProcessing(@PathVariable String tid) {
        checkOperatorByTrade(tid);
        return tradeQueryProvider.verifyAfterProcessing(TradeVerifyAfterProcessingRequest.builder().tid(tid).build()).getContext().getVerifyResult();
    }

    /**
     * 根据快递公司及快递单号查询物流详情
     */
    @ApiOperation(value = "根据快递公司及快递单号查询物流详情", notes = "返回: 物流详情")
    @RequestMapping(value = "/deliveryInfos", method = RequestMethod.POST)
    public BaseResponse<List<Map<Object, Object>>> logistics(@RequestBody DeliveryQueryRequest queryRequest) {
        List<Map<Object, Object>> result = new ArrayList<>();

//        CompositeResponse<ConfigRopResponse> response = sdkClient.buildClientRequest().post(ConfigRopResponse.class,
//                "logistics.config", "1.0.0");
//        //如果快递设置为启用
//        if (Objects.nonNull(response.getSuccessResponse()) && DefaultFlag.YES.toValue() == response
//                .getSuccessResponse().getStatus()) {
//            result = deliveryQueryProvider.queryExpressInfoUrl(queryRequest).getContext().getOrderList();
//        }
        //获取快递100配置信息
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setConfigType(ConfigType.KUAIDI100.toValue());
        LogisticsRopResponse response = systemConfigQueryProvider.findKuaiDiConfig(request).getContext();
        //已启用
        if (response.getStatus() == DefaultFlag.YES.toValue()) {
            result = deliveryQueryProvider.queryExpressInfoUrl(queryRequest).getContext().getOrderList();
        }
        return BaseResponse.success(result);
    }

    /**
     * 根据linkedmall订单号及购买用户id查询linkedmall订单的物流详情
     */
    @ApiOperation(value = "根据linkedmall订单号及购买用户id查询物流详情", notes = "返回: 物流详情")
    @RequestMapping(value = "/linkedMall/deliveryInfos", method = RequestMethod.POST)
    public BaseResponse<SbcLogisticsQueryResponse> logistics4LinkedMall(@RequestBody SbcLogisticsQueryRequest request) {
        return linkedMallOrderQueryProvider.getOrderLogistics(request);
    }

    /**
     * 分页查询拼团订单
     */
    @ApiOperation(value = "分页查询拼团订单")
    @RequestMapping(value = "/groupon/page", method = RequestMethod.POST)
    public BaseResponse<Page<TradeVO>> grouponOrderPage(@RequestBody TradeQueryDTO tradeQueryRequest) {
        Long companyInfoId = commonUtil.getCompanyInfoId();
        if (companyInfoId != null) {
            tradeQueryRequest.setSupplierId(companyInfoId);
        }
        tradeQueryRequest.setGrouponFlag(Boolean.TRUE);
        tradeQueryRequest.putSort("grouponSuccessTime", "desc");
        tradeQueryRequest.putSort("createTime", "desc");
        Page<TradeVO> tradePage = tradeQueryProvider.pageCriteria(TradePageCriteriaRequest.builder()
                .tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage();
        return BaseResponse.success(tradePage);
    }


    /**
     * 导出订单
     *
     * @return
     */
    @ApiOperation(value = "导出订单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/providerTrade/{encrypted}/{encryptedable}", method = RequestMethod.GET)
    public void exportProviderTrade(@PathVariable String encrypted, @PathVariable String encryptedable, HttpServletResponse response) {
        log.info("=================子单，订单导出====================");
        try {
            if (exportCount.incrementAndGet() > 1) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
            }

            String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
            String decryptedable = new String(Base64.getUrlDecoder().decode(encryptedable.getBytes()));
            TradeExportRequest tradeExportRequest = JSON.parseObject(decrypted, TradeExportRequest.class);
            DisabledExportRequest disabledExportRequest = JSON.parseObject(decryptedable, DisabledExportRequest.class);

            Operator operator = commonUtil.getOperator();
            log.debug(String.format("/export/params/providerTrade, employeeId=%s", operator.getUserId()));

            String adminId = operator.getAdminId();
            if (StringUtils.isNotEmpty(adminId)) {
                tradeExportRequest.setSupplierId(Long.valueOf(adminId));
            }

            //设定状态条件逻辑,已审核状态需筛选出已审核与部分发货
            tradeExportRequest.makeAllAuditFlow();
            TradeQueryDTO tradeQueryDTO = KsBeanUtil.convert(tradeExportRequest, TradeQueryDTO.class);
            DisabledDTO disabledDTO = KsBeanUtil.convert(disabledExportRequest, DisabledDTO.class);
            List<TradeVO> trades = tradeQueryProvider.listTradeExport(TradeListExportRequest.builder().tradeQueryDTO(tradeQueryDTO).build()).getContext().getTradeVOList();

            //按下单时间降序排列
            Comparator<TradeVO> c = Comparator.comparing(a -> a.getTradeState().getCreateTime());
            trades = trades.stream().sorted(
                    c.reversed()
            ).collect(Collectors.toList());

            String headerKey = "Content-Disposition";
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName = String.format("批量导出订单_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("/prioviderTrade/export/params, fileName={},", fileName, e);
            }
            String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
            response.setHeader(headerKey, headerValue);

            List<String> parentIdList = new ArrayList<>();
            if (disabledDTO.getDisabled().equals("true")) {
                // 第三方渠道设置
                ThirdPlatformConfigResponse config = thirdPlatformConfigQueryProvider.get(
                        ThirdPlatformConfigByTypeRequest.builder().configType(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue()).build()).getContext();

                trades.forEach(vo -> {
                    parentIdList.add(vo.getId());
                });
                List<TradeVO> tradeVOList = providerTradeProvider.findByParentIdList(FindProviderTradeRequest.builder().parentId(parentIdList).build()).getContext().getTradeVOList();

                Map<String, String> thirdPlatformOrderIdMap = new HashMap<>();
                Map<String, String> outOrderIdMap = new HashMap<>();
                if (Objects.nonNull(config)) {
                    // 对接了第三方渠道（目前只有LinkedMall），获取第三方渠道订单信息
                    List<ThirdPlatformTradeVO> tradeList =
                            thirdPlatformTradeQueryProvider.listByTradeIds(ThirdPlatformTradeListByTradeIdsRequest.builder().tradeIds(parentIdList).build()).getContext().getTradeList();

                    // 获取商品对应的第三方渠道订单编号（LinkedMall订单号）和外部订单号（淘宝订单号）
                    tradeList.forEach(thirdPlatformTradeVO -> {
                        thirdPlatformTradeVO.getTradeItems().forEach(tradeItemVO -> {
                            thirdPlatformOrderIdMap.put(tradeItemVO.getSkuId(),
                                    CollectionUtils.isNotEmpty(thirdPlatformTradeVO.getThirdPlatformOrderIds()) ?
                                            thirdPlatformTradeVO.getThirdPlatformOrderIds().get(0) : null);
                            outOrderIdMap.put(tradeItemVO.getSkuId(),
                                    CollectionUtils.isNotEmpty(thirdPlatformTradeVO.getOutOrderIds()) ?
                                            thirdPlatformTradeVO.getOutOrderIds().get(0) : null);
                        });
                    });
                }

                // 遍历封装导出信息
                List<ProviderTradeExportVO> tradeExportVOs = new ArrayList<>();
                tradeVOList.forEach(tradeVO -> {
                    ProviderTradeExportVO exportVO;
                    // 商家信息
                    String supplierName = StringUtils.isNotEmpty(tradeVO.getSupplier().getSupplierName()) ? tradeVO.getSupplier().getSupplierName() : "";
                    String supplierCode = StringUtils.isNotEmpty(tradeVO.getSupplierCode()) ? tradeVO.getSupplierCode() : "";
                    String supplierInfo = supplierName + "  " + supplierCode;
                    List<TradeItemVO> tradeItems = tradeVO.getTradeItems();
                    tradeItems.addAll(tradeVO.getGifts());
                    for (int i = 0; i < tradeItems.size(); i++) {
                        TradeItemVO tradeItemVO = tradeItems.get(i);
                        exportVO = new ProviderTradeExportVO();
//                        if (i == 0) {
                        KsBeanUtil.copyProperties(tradeVO, exportVO);
                        // 下单时间
                        exportVO.setCreateTime(tradeVO.getTradeState().getCreateTime());
                        // 商家信息
                        exportVO.setSupplierInfo(supplierInfo);
                        // 供应商名称
                        exportVO.setSupplierName(supplierName);
                        // 订单商品金额
                        exportVO.setOrderGoodsPrice(tradeVO.getTradePrice().getTotalPrice());
                        // 订单状态
                        exportVO.setFlowState(tradeVO.getTradeState().getFlowState());
                        // 发货状态
                        exportVO.setDeliverStatus(tradeVO.getTradeState().getDeliverStatus());
                        exportVO.setConsigneeName(tradeVO.getConsignee().getName());
                        exportVO.setDetailAddress(tradeVO.getConsignee().getDetailAddress());
                        exportVO.setConsigneePhone(tradeVO.getConsignee().getPhone());

                        log.info("=================子单，订单导出==============GoodsType:{}====directChargeMobile:{}==",tradeItemVO.getGoodsType(),tradeVO.getDirectChargeMobile());
                        //订单导出新增充值手机号
                        if (GoodsType.VIRTUAL_GOODS.equals(tradeItemVO.getGoodsType()) || GoodsType.VIRTUAL_COUPON.equals(tradeItemVO.getGoodsType())) {
                            exportVO.setDirectChargeMobile(Objects.nonNull(tradeVO.getDirectChargeMobile())? tradeVO.getDirectChargeMobile():null);
                        } else {
                            exportVO.setDirectChargeMobile(null);
                        }

                        exportVO.setSkuName(tradeItemVO.getSkuName());
                        exportVO.setSpecDetails(tradeItemVO.getSpecDetails());
                        exportVO.setSkuNo(tradeItemVO.getSkuNo());
                        exportVO.setNum(tradeItemVO.getNum());
                        exportVO.setPayState(tradeVO.getTradeState().getPayState());

                        if (Objects.nonNull(config)) {
                            exportVO.setThirdPlatformOrderId(thirdPlatformOrderIdMap.get(tradeItemVO.getSkuId()));
                            exportVO.setOutOrderId(outOrderIdMap.get(tradeItemVO.getSkuId()));
                        }

                        tradeExportVOs.add(exportVO);
                    }
                });

                try {
                    providerTradeExportBaseService.exportProvider(tradeExportVOs, response.getOutputStream(),
                            Platform.SUPPLIER, config);
                    response.flushBuffer();
                } catch (IOException e) {
                    throw new SbcRuntimeException(e);
                }

            } else {
                try {
                    List<TradeVO> tradesnNew = new ArrayList<>();
                    trades.forEach(tradeVO -> {
                        List<TradeItemVO> tradeItems = tradeVO.getTradeItems();
                        tradeItems.addAll(tradeVO.getGifts());
                        tradeItems.forEach(tradeItemVO -> {
                            TradeVO tradeVONew = new TradeVO();
                            KsBeanUtil.copyProperties(tradeVO, tradeVONew);
                            List<TradeItemVO> tradeItemsNew = new ArrayList<TradeItemVO>();
                            log.info("=================主单，订单导出==============GoodsType:{}====directChargeMobile:{}==",tradeItemVO.getGoodsType(),tradeVO.getDirectChargeMobile());
                            //订单导出新增充值手机号
                            if (GoodsType.VIRTUAL_GOODS.equals(tradeItemVO.getGoodsType()) || GoodsType.VIRTUAL_COUPON.equals(tradeItemVO.getGoodsType())) {
                                tradeVONew.setDirectChargeMobile(Objects.nonNull(tradeVO.getDirectChargeMobile())? tradeVO.getDirectChargeMobile():null);
                            }else {
                                tradeVONew.setDirectChargeMobile(null);
                            }
                            tradeItemsNew.add(tradeItemVO);
                            tradeVONew.setTradeItems(tradeItemsNew);
                            tradesnNew.add(tradeVONew);
                        });
                    });


                    tradeExportService.export(tradesnNew, response.getOutputStream(),
                            Platform.PLATFORM.equals(operator.getPlatform()));
                    response.flushBuffer();
//                    tradeExportService.export(trades, response.getOutputStream(),
//                            Platform.PLATFORM.equals(operator.getPlatform()));
//                    response.flushBuffer();
                } catch (IOException e) {
                    throw new SbcRuntimeException(e);
                }
            }
        } catch (Exception e) {
            log.error("/prioviderTrade/export/params error: ", e);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000001);
        } finally {
            exportCount.set(0);
        }
    }

    private TradeVO checkOperatorByTrade(String tid) {
        TradeVO trade = null;
        Operator operator = commonUtil.getOperator();
        if (operator.getPlatform() == Platform.SUPPLIER) {
            if (tid.startsWith("O")) {
                trade =
                        tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
            } else if (tid.startsWith("S")) {
                trade =
                        providerTradeQueryProvider.providerGetById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
            }
            if (Objects.nonNull(trade) && !Objects.equals(commonUtil.getStoreId(), trade.getSupplier().getStoreId())) {
                throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
            }
        }
        return trade;
    }

    /**
     * 推送订单至ERP系统(测试)
     */
    @ApiOperation(value = "推送订单至ERP系统")
    @RequestMapping(value = "/erpTrade/pushTrade", method = RequestMethod.GET)
    public BaseResponse pushTrade(String tradeNO) {
        TradePushRequest request = TradePushRequest.builder().tid(tradeNO).build();
        BaseResponse baseResponse = tradeProvider.pushOrderToERP(request);
        return baseResponse;
    }

    /**
     * 推送订单至ERP系统(测试)
     */
    @ApiOperation(value = "同步商品库存")
    @RequestMapping(value = "/erpGoods/syncGoodsStock", method = RequestMethod.GET)
    public BaseResponse syncGoodsStock(String skuNo) {
        GoodsInfoListByIdRequest goodsInfoListByIdRequest = GoodsInfoListByIdRequest.builder()
                .goodsInfoNo(skuNo)
                .pageNum(0)
                .build();
        goodsProvider.syncERPStock(goodsInfoListByIdRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 推送订单至ERP系统
     */
    @ApiOperation(value = "同步发货状态")
    @RequestMapping(value = "/erpGoods/syncDeliveryStatus", method = RequestMethod.GET)
    public BaseResponse syncDeliveryStatus(String ptid){
        ProviderTradeErpRequest providerTradeErpRequest = ProviderTradeErpRequest.builder().ptid(ptid).build();
        providerTradeQueryProvider.batchSyncDeliveryStatus(providerTradeErpRequest);
        return BaseResponse.SUCCESSFUL();
    }
}
