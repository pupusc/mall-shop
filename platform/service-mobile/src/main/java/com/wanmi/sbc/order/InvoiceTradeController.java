package com.wanmi.sbc.order;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerSimplifyByIdRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengInvoiceRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerSimplifyByIdResponse;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdRequest;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateByIdResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradePageCriteriaRequest;
import com.wanmi.sbc.order.api.request.trade.TradePageQueryRequest;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.dto.TradeStateDTO;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.request.InvoiceRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = "InvoiceTradeController", description = "发票API")
@RestController
@RequestMapping("/trade/invoice")
@Slf4j
@Validated
public class InvoiceTradeController {

    @Resource
    private CommonUtil commonUtil;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private ExternalProvider externalProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;


    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    /**
     * 可以开票的订单列表
     */
    @ApiOperation(value = "分页查询订单")
    @RequestMapping(value = "/pageOrder", method = RequestMethod.POST)
    public BaseResponse<Page<TradeVO>> pageOrder(@RequestBody TradePageQueryRequest paramRequest) {
        TradeQueryDTO tradeQueryRequest = TradeQueryDTO.builder()
                .tradeState(TradeStateDTO.builder().flowState(FlowState.COMPLETED).payState(PayState.PAID).build())
                .buyerId(commonUtil.getOperatorId())
                .invoiceType(-1)
                .actualCashFlag(true)
                .build();
        tradeQueryRequest.setPageNum(paramRequest.getPageNum());
        tradeQueryRequest.setPageSize(paramRequest.getPageSize());

        MicroServicePage<TradeVO> tradePage = tradeQueryProvider.pageCriteriaOptimize(TradePageCriteriaRequest.builder()
                .tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage();
        return BaseResponse.success(tradePage);
    }

    /**
     * 发起开票
     */
    @ApiOperation(value = "发起开票")
    @PostMapping(value="/submit")
    public BaseResponse<String> submitInvoice(@RequestBody InvoiceRequest request){
        TradeQueryDTO tradeQueryRequest = TradeQueryDTO.builder()
                .tradeState(TradeStateDTO.builder().flowState(FlowState.COMPLETED).payState(PayState.PAID).build())
                .id(request.getOrderIds().get(0))
                .buyerId(commonUtil.getOperatorId())
                .invoiceType(-1)
                .actualCashFlag(true)
                .build();

        MicroServicePage<TradeVO> tradePage = tradeQueryProvider.pageCriteriaOptimize(TradePageCriteriaRequest.builder()
                .tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage();

        if(CollectionUtils.isEmpty(tradePage.getContent())){
            return BaseResponse.error("开票的订单当中数据不一致");
        }
        TradeVO tradeVO = tradePage.getContent().get(0);
        FanDengInvoiceRequest fanDengInvoiceRequest = new FanDengInvoiceRequest();

        CustomerSimplifyByIdResponse customer = customerQueryProvider.simplifyById(new CustomerSimplifyByIdRequest(commonUtil.getOperatorId())).getContext();
        fanDengInvoiceRequest.setUserId(customer.getFanDengUserNo());
        fanDengInvoiceRequest.setBusinessId(2);
        fanDengInvoiceRequest.setOrderId(tradeVO.getId());
        List<Long> collect = tradeVO.getTradeItems().stream().map(TradeItemVO::getCateId).collect(Collectors.toList());

        for (TradeItemVO itemVO : tradeVO.getTradeItems()) {
            FanDengInvoiceRequest.Item item = new FanDengInvoiceRequest.Item();

            item.setFee(itemVO.getSplitPrice().divide(new BigDecimal(itemVO.getNum())));
            item.setOrderCode(tradeVO.getId()+itemVO.getOid());
            item.setTotalFee(itemVO.getSplitPrice());
            item.setCount(itemVO.getNum().intValue());

            item.setProduct(itemVO.getSpuName());
            item.setProductNo(1);
            item.setProductType(getProductType(itemVO.getCateId()));
            item.setProductIcoon("");
            //暂时都定1
            item.setOrderType(1);
            item.setCompleteTime(Date.from(tradeVO.getTradeState().getEndTime().atZone(ZoneId.systemDefault()).toInstant()));
            fanDengInvoiceRequest.getOrderExtendBOS().add(item);
        }
        BaseResponse<String> result = externalProvider.submitInvoiceOrder(fanDengInvoiceRequest);
        return result;
    }

    private Integer getProductType(Long cateId) {
        if (Objects.isNull(cateId)) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "商品的分类信息不能为空");
        }

        GoodsCateByIdRequest goodsCateByIdRequest = new GoodsCateByIdRequest();
        goodsCateByIdRequest.setCateId(cateId);
        GoodsCateByIdResponse context = goodsCateQueryProvider.getById(goodsCateByIdRequest).getContext();
        if (Objects.isNull(context)) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "商品的分类信息没有找到");
        }
        return context.getTaxRateNo();
    }

}
