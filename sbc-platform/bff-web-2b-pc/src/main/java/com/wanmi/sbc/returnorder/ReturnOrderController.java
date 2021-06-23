package com.wanmi.sbc.returnorder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderAddRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.bean.dto.CompanyDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.api.response.TradeConfigGetByTypeResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by sunkun on 2017/8/18.
 */
@Api(tags = "ReturnOrderController", description = "退单API")
@RestController
@RequestMapping("/return")
public class ReturnOrderController {

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    /**
     * 创建退单
     *
     * @param returnOrder
     * @return
     */
    @ApiOperation(value = "创建退单")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse<String> create(@RequestBody @Valid ReturnOrderDTO returnOrder) {
        verifyIsReturnable(returnOrder.getTid());
        returnOrder.setTerminalSource(commonUtil.getTerminal());
        //验证用户
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (commonUtil.getOperatorId())).getContext();
        if (!verifyTradeByCustomerId(returnOrder.getTid(), commonUtil.getOperatorId())) {
            throw new SbcRuntimeException("K-050204");
        }

        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(returnOrder.getTid()).build()).getContext().getTradeVO();
        returnOrder.setCompany(CompanyDTO.builder().companyInfoId(trade.getSupplier().getSupplierId())
                .companyCode(trade.getSupplier().getSupplierCode()).supplierName(trade.getSupplier().getSupplierName())
                .storeId(trade.getSupplier().getStoreId()).storeName(trade.getSupplier().getStoreName()).companyType(trade.getSupplier().getIsSelf() ? BoolFlag.NO : BoolFlag.YES).build());

        String rid = returnOrderProvider.add(ReturnOrderAddRequest.builder().returnOrder(returnOrder)
                .operator(commonUtil.getOperator()).build()).getContext().getReturnOrderId();
        return BaseResponse.success(rid);
    }


    /**
     * 是否可创建退单
     *
     * @return
     */
    @ApiOperation(value = "是否可创建退单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id",
            required = true)
    @RequestMapping(value = "/returnable/{tid}", method = RequestMethod.GET)
    public BaseResponse isReturnable(@PathVariable String tid) {
        verifyIsReturnable(tid);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 校验是否可退
     *
     * @param tid
     */
    private void verifyIsReturnable(String tid) {
        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        if (Objects.nonNull(trade.getTradeState().getDeliverStatus()) && (trade.getTradeState().getDeliverStatus() == DeliverStatus.SHIPPED || trade.getTradeState().getDeliverStatus() == DeliverStatus.PART_SHIPPED)) {
            TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
            request.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);
            TradeConfigGetByTypeResponse config = auditQueryProvider.getTradeConfigByType(request).getContext();
            // 是否支持退货
            if (config.getStatus() == 0) {
                throw new SbcRuntimeException("K-050208");
            }
            JSONObject content = JSON.parseObject(config.getContext());
            Integer day = content.getObject("day", Integer.class);

            if (Objects.isNull(trade.getTradeState().getEndTime())) {
                throw new SbcRuntimeException("K-050102");
            }
            // 是否在可退时间内
            Duration duration = Duration.between(trade.getTradeState().getEndTime(), LocalDateTime.now());
            if (duration.toDays() > day) {
                throw new SbcRuntimeException("K-050208");
            }
        }
    }


    private boolean verifyTradeByCustomerId(String tid, String customerId) {
        TradeVO trade = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        return trade.getBuyer().getId().equals(customerId);
    }
}
