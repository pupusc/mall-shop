package com.wanmi.sbc.open;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.NoDeleteCustomerGetByFanDengRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengModifyCustomerRequest;
import com.wanmi.sbc.customer.api.request.store.StoreInfoByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.NoDeleteCustomerGetByAccountResponse;
import com.wanmi.sbc.customer.api.response.store.StoreInfoResponse;
import com.wanmi.sbc.customer.bean.dto.CompanyInfoDTO;
import com.wanmi.sbc.customer.bean.dto.StoreInfoDTO;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.elastic.api.provider.sku.EsSkuQueryProvider;
import com.wanmi.sbc.elastic.api.request.sku.EsSkuPageRequest;
import com.wanmi.sbc.elastic.api.response.sku.EsSkuPageResponse;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedsale.GoodsRestrictedSaleQueryProvider;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedBatchValidateRequest;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedValidateVO;
import com.wanmi.sbc.open.vo.ConsigneeResVO;
import com.wanmi.sbc.open.vo.DeliverItemResVO;
import com.wanmi.sbc.open.vo.DeliverResVO;
import com.wanmi.sbc.open.vo.GoodsQueryReqVO;
import com.wanmi.sbc.open.vo.GoodsQueryResVO;
import com.wanmi.sbc.open.vo.LogisticsResVO;
import com.wanmi.sbc.open.vo.OrderCreateReqVO;
import com.wanmi.sbc.open.vo.OrderCreateResVO;
import com.wanmi.sbc.open.vo.OrderDeliverInfoReqVO;
import com.wanmi.sbc.open.vo.OrderDeliverInfoResVO;
import com.wanmi.sbc.open.vo.TradeItemReqVO;
import com.wanmi.sbc.order.api.provider.open.OpenDeliverProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.open.OrderDeliverInfoReqBO;
import com.wanmi.sbc.order.api.request.trade.TradeAddBatchRequest;
import com.wanmi.sbc.order.api.request.trade.TradeWrapperBackendCommitRequest;
import com.wanmi.sbc.order.api.response.open.DeliverResBO;
import com.wanmi.sbc.order.api.response.open.OrderDeliverInfoResBO;
import com.wanmi.sbc.order.bean.dto.ConsigneeDTO;
import com.wanmi.sbc.order.bean.dto.InvoiceDTO;
import com.wanmi.sbc.order.bean.dto.TradeAddDTO;
import com.wanmi.sbc.order.bean.dto.TradeCreateDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.dto.TradePriceDTO;
import com.wanmi.sbc.order.bean.enums.OutTradePlatEnum;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @desc 实物履约
 * @date 2022-02-15 13:56:00
 */
@Slf4j
@RestController
@RequestMapping("open/deliver")
public class OpenDeliverController extends OpenBaseController {
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private TradeProvider tradeProvider;
    @Autowired
    private EsSkuQueryProvider esSkuQueryProvider;
    @Autowired
    private TradeQueryProvider tradeQueryProvider;
    @Autowired
    private StoreQueryProvider storeQueryProvider;
    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;
    @Autowired
    private CustomerQueryProvider customerQueryProvider;
    @Autowired
    private GoodsRestrictedSaleQueryProvider goodsRestrictedSaleQueryProvider;
    @Autowired
    private ExternalProvider externalProvider;
    @Autowired
    private OpenDeliverProvider openDeliverProvider;

    // TODO: 2022/2/16 增加赠品标记
    // TODO: 2022/2/23 确定商家id，店铺id

    /**
     * @description: 实物商品查询
     * @menu 履约中台
     * @tag  v101
     * @status done
     */
    @PostMapping(value = "/goods/query")
    public BusinessResponse<List<GoodsQueryResVO>> goodsQuery(@RequestBody @Validated GoodsQueryReqVO param) {
        checkSign();

        Integer pageNo = Objects.nonNull(param.getPage()) && Objects.nonNull(param.getPage().getPageNo()) ? param.getPage().getPageNo() : 1;
        Integer pageSize = Objects.nonNull(param.getPage()) && Objects.nonNull(param.getPage().getPageSize()) ? param.getPage().getPageSize() : 20;

        EsSkuPageRequest queryRequest = new EsSkuPageRequest();
        queryRequest.setGoodsInfoNos(Arrays.asList(param.getSkuNo()));
        queryRequest.setLikeGoodsName(param.getGoodsName());
        queryRequest.setPageNum(pageNo < 1 ? 1 : pageNo);
        queryRequest.setPageSize(pageSize > 100 ? 100 : pageSize);
//        queryRequest.setIsGift(true); //赠品标记 TODO: 2022/2/16

        //按创建时间倒序、ID升序
        queryRequest.putSort("addedTime", SortType.DESC.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
        queryRequest.setAuditStatus(CheckStatus.CHECKED);//已审核
        queryRequest.setVendibility(Constants.yes);
        queryRequest.setShowPointFlag(Boolean.TRUE);
        queryRequest.setShowProviderInfoFlag(Boolean.TRUE);
        queryRequest.setFillLmInfoFlag(Boolean.TRUE);
        EsSkuPageResponse response = esSkuQueryProvider.page(queryRequest).getContext();

        MicroServicePage<GoodsInfoVO> goodsInfoPage = response.getGoodsInfoPage();
        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfoPage().getContent();

        Page retPage = new Page(goodsInfoPage.getNumber(), goodsInfoPage.getSize(), (int) goodsInfoPage.getTotal());
        if (CollectionUtils.isEmpty(goodsInfoVOList)) {
            return BusinessResponse.success(Lists.newArrayList(), retPage);
        }

        List<GoodsQueryResVO> result = goodsInfoVOList.stream().map(item -> {
            GoodsQueryResVO vo = new GoodsQueryResVO();
            vo.setSkuId(item.getGoodsInfoId());
            vo.setSkuNo(item.getGoodsInfoNo());
            vo.setSkuName(item.getGoodsInfoName());
            vo.setSalePrice(item.getSalePrice());
            vo.setCostPrice(item.getCostPrice());
            vo.setShelfStatus(item.getAddedFlag());
            return vo;
        }).collect(Collectors.toList());

        return BusinessResponse.success(result, retPage);
    }

    /**
     * @description: 商品发货下单
     * @menu 履约中台
     * @tag  v101
     * @status done
     */
    @PostMapping(value = "/order/create")
    public BusinessResponse<OrderCreateResVO> orderCreate(@RequestBody @Validated OrderCreateReqVO params) {
        checkSign();

        //商家id
        Long companyId = 0L;
        //店铺id
        Long storeId = 0L;

        log.info("第三方开始代客下单......");
        Operator operator = commonUtil.getOperator();
        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(companyId).build()
        ).getContext();
        StoreInfoResponse storeInfoResponse = storeQueryProvider.getStoreInfoById(new StoreInfoByIdRequest(storeId)).getContext();

        //1.校验与包装订单信息-与业务员app代客下单公用
        log.info("开始校验与包装订单信息......");

        //收件人
        if (Objects.isNull(params.getConsignee())) {
            return BusinessResponse.error("参数错误：缺少收件人信息");
        }
        ConsigneeDTO consigneeDTO = new ConsigneeDTO();
        consigneeDTO.setAddress(params.getConsignee().getAddress());
        consigneeDTO.setAreaId(params.getConsignee().getAreaId());
        consigneeDTO.setCityId(params.getConsignee().getCityId());
        consigneeDTO.setName(params.getConsignee().getName());
        consigneeDTO.setPhone(params.getConsignee().getPhone());
        consigneeDTO.setProvinceId(params.getConsignee().getProvinceId());
        //consigneeDTO.setStreetId(params.getConsignee().getStreetId());
        //用户信息
        CustomerVO customerVO = getCustomByFddsUser(params.getFddsUserId(), params.getConsignee().getName(), params.getConsignee().getPhone());
        //订单信息
        TradeCreateDTO tradeCreateParam = new TradeCreateDTO();
        tradeCreateParam.setBuyerRemark(params.getBuyerRemark());
        tradeCreateParam.setConsignee(consigneeDTO);  //收件人
        tradeCreateParam.setConsigneeAddress(params.getConsigneeAddress());
        tradeCreateParam.setCustom(customerVO.getCustomerId());
        tradeCreateParam.setDeliverWay(DeliverWay.EXPRESS);
        tradeCreateParam.setForceCommit(false); //是否强制提交，用于营销活动有效性校验，true: 无效依然提交， false: 无效做异常返回
        tradeCreateParam.setInvoice(InvoiceDTO.builder().type(-1).build());
        tradeCreateParam.setInvoiceConsignee(null);
        tradeCreateParam.setPayType(PayType.ONLINE);
        tradeCreateParam.setSellerRemark("履约中台订单");
        tradeCreateParam.setTradePrice(TradePriceDTO.builder().special(true).privilegePrice(BigDecimal.ZERO).build());
        tradeCreateParam.setTradeItems(new ArrayList<>());

        if (CollectionUtils.isEmpty(params.getTradeItems())) {
            return BusinessResponse.error("参数错误：缺少商品信息");
        }
        for (TradeItemReqVO item : params.getTradeItems()) {
            TradeItemDTO itemDTO = new TradeItemDTO();
            itemDTO.setSkuId(item.getSkuId());
            itemDTO.setNum(item.getNum());
            tradeCreateParam.getTradeItems().add(itemDTO);
        }

        TradeVO trade = tradeQueryProvider.wrapperBackendCommit(TradeWrapperBackendCommitRequest.builder()
                .operator(operator)
                .companyInfo(KsBeanUtil.convert(companyInfo, CompanyInfoDTO.class))
                .storeInfo(KsBeanUtil.convert(storeInfoResponse, StoreInfoDTO.class))
                .tradeCreate(tradeCreateParam)
                .build()
        ).getContext().getTradeVO();

        //2.订单入库(转换成list,传入批量创建订单的service方法,同一套逻辑,能够回滚)
        TradeAddDTO tradeAddDTO = KsBeanUtil.convert(trade, TradeAddDTO.class, SerializerFeature.DisableCircularReferenceDetect);
        tradeAddDTO.setOutTradeNo(params.getOutTradeNo());
        tradeAddDTO.setOutTradePlat(OutTradePlatEnum.FDDS_PERFORM.getCode());

        TradeAddBatchRequest tradeAddBatchRequest = TradeAddBatchRequest.builder()
                .tradeDTOList(Collections.singletonList(tradeAddDTO))
                .operator(operator)
                .build();

        // 3.限售校验
        log.info("校验与包装订单信息结束，开始校验订单中的限售商品......");
        this.validateRestrictedGoods(tradeCreateParam.getTradeItems(), customerVO);

        tradeProvider.addBatch(tradeAddBatchRequest);
        return BusinessResponse.success();
    }

    /**
     * @description: 订单发货信息
     * @menu 履约中台
     * @tag  v101
     * @status done
     */
    @PostMapping(value = "/order/deliverInfo")
    public BusinessResponse<OrderDeliverInfoResVO> deliverInfo(@RequestBody @Validated OrderDeliverInfoReqVO param) {
        checkSign();

        OrderDeliverInfoReqBO reqBO = new OrderDeliverInfoReqBO();
        reqBO.setOrderNo(param.getOrderNo());
        BusinessResponse<OrderDeliverInfoResBO> resResponse = openDeliverProvider.deliverInfo(reqBO);

        if (!CommonErrorCode.SUCCESSFUL.equals(resResponse.getCode())) {
            return BusinessResponse.error(resResponse.getCode(), resResponse.getMessage());
        }
        if (Objects.isNull(resResponse.getContext())) {
            return BusinessResponse.error(CommonErrorCode.DATA_NOT_EXISTS);
        }

        OrderDeliverInfoResBO deliverBO = resResponse.getContext();
        OrderDeliverInfoResVO resultVO = new OrderDeliverInfoResVO();
        resultVO.setTradeNo(deliverBO.getTradeNo());
        resultVO.setOutTradeNo(deliverBO.getOutTradeNo());
        resultVO.setOrderStatus(deliverBO.getOrderStatus());
        resultVO.setDeliverStatus(deliverBO.getDeliverStatus());
        resultVO.setDelivers(new ArrayList<>());

        if (!CollectionUtils.isEmpty(deliverBO.getDelivers())) {
            resultVO.setDelivers(deliverBO.getDelivers().stream().map(item -> buildDeliverResVO(item)).collect(Collectors.toList()));
        }
        return BusinessResponse.success(resultVO);
    }

    private DeliverResVO buildDeliverResVO(DeliverResBO item) {
        DeliverResVO resVO = new DeliverResVO();
        resVO.setDeliverId(item.getDeliverId());
        resVO.setDeliverTime(item.getDeliverTime());
        if (Objects.nonNull(item.getConsignee())) {
            resVO.setConsignee(KsBeanUtil.convert(item.getConsignee(), ConsigneeResVO.class));
        }
        if (Objects.nonNull(item.getLogistics())) {
            resVO.setLogistics(KsBeanUtil.convert(item.getLogistics(), LogisticsResVO.class));
        }
        if (Objects.nonNull(item.getDeliverItems())) {
            resVO.setDeliverItems(KsBeanUtil.convert(item.getDeliverItems(), DeliverItemResVO.class));
        }
        return resVO;
    }

    /**
     * 根据樊登读书用户获取商城用户信息
     */
    private CustomerVO getCustomByFddsUser(String fddsUserId, String name, String phone) {
        NoDeleteCustomerGetByFanDengRequest fddsId = new NoDeleteCustomerGetByFanDengRequest();
        fddsId.setFanDengId(fddsUserId);
        BaseResponse<NoDeleteCustomerGetByAccountResponse> queryResponse = customerQueryProvider.getNoDeleteCustomerByFanDengId(fddsId);
        if (!CommonErrorCode.SUCCESSFUL.equals(queryResponse.getCode())) {
            log.info("用户信息查询失败, fddsUserId = {}", fddsUserId);
            throw new SbcRuntimeException(queryResponse.getCode(), queryResponse.getMessage());
        }
        if (Objects.nonNull(queryResponse.getContext())) {
            return queryResponse.getContext();
        }
        //注册用户
        FanDengModifyCustomerRequest customerRequest = FanDengModifyCustomerRequest.builder()
                .fanDengUserNo(fddsUserId)
                .nickName(name)
                .customerAccount(phone)
                .build();
        BaseResponse<NoDeleteCustomerGetByAccountResponse> registResponse = externalProvider.modifyCustomer(customerRequest);
        if (!CommonErrorCode.SUCCESSFUL.equals(registResponse.getCode()) || Objects.isNull(registResponse.getContext())) {
            log.info("商城用户注册失败, fddsUserId = {}", fddsUserId);
            throw new SbcRuntimeException(registResponse.getCode(), registResponse.getMessage());
        }
        return registResponse.getContext();
    }

    /**
     * 校验商品限售信息
     */
    private void validateRestrictedGoods(List<TradeItemDTO> tradeItems, CustomerVO customerVO) {
        //组装请求的数据
        List<GoodsRestrictedValidateVO> list = KsBeanUtil.convert(tradeItems, GoodsRestrictedValidateVO.class);
        goodsRestrictedSaleQueryProvider.validateOrderRestricted(
                GoodsRestrictedBatchValidateRequest.builder()
                        .goodsRestrictedValidateVOS(list)
                        .customerVO(customerVO)
                        .openGroupon(false)
                        .build()
        );
    }
}
