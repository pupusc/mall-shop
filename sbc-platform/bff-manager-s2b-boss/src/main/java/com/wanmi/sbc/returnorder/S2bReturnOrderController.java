package com.wanmi.sbc.returnorder;

import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.account.CustomerAccountQueryProvider;
import com.wanmi.sbc.customer.api.request.account.CustomerAccountRequest;
import com.wanmi.sbc.customer.api.response.account.CustomerAccountResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerAccountVO;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.request.areas.ReturnOrderRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderByReturnOrderCodeRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByIdRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderOfflineRefundForBossRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderPageRequest;
import com.wanmi.sbc.order.api.response.refund.RefundOrderByReturnCodeResponse;
import com.wanmi.sbc.order.api.response.returnorder.ReturnOrderByIdResponse;
import com.wanmi.sbc.order.bean.dto.RefundBillDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.vo.RefundBillVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.returnorder.request.ReturnOfflineRefundRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 退单
 * Created by sunkun on 2017/11/17.
 */
@RestController
@RequestMapping("/return")
@Api(description = "退单", tags = "S2bReturnOrderController")
public class S2bReturnOrderController {

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Resource
    private CommonUtil commonUtil;

    @Resource
    private RefundOrderQueryProvider refundOrderQueryProvider;

    @Resource
    private CustomerAccountQueryProvider customerAccountQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 分页查询 from ES
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "分页查询 from ES")
    @EmployeeCheck
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<ReturnOrderVO>> page(@RequestBody ReturnOrderPageRequest request) {
        return BaseResponse.success(returnOrderQueryProvider.page(request).getContext().getReturnOrderPage());
    }

    /**
     * 线下退款
     *
     * @param rid
     * @param request
     * @return
     */
    @ApiOperation(value = "线下退款")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid",
                    value = "退单Id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "ReturnOfflineRefundRequest", name = "request",
                    value = "线下退款", required = true),
    })
    @RequestMapping(value = "/refund/{rid}/offline", method = RequestMethod.POST)
    @GlobalTransactional
    public ResponseEntity<BaseResponse> refundOffline(@PathVariable String rid,
                                                      @RequestBody ReturnOfflineRefundRequest request) {
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid)
                .build()).getContext();
        RefundOrderByReturnCodeResponse refundOrder = refundOrderQueryProvider.getByReturnOrderCode(new RefundOrderByReturnOrderCodeRequest(returnOrder.getId())).getContext();

        //退款流水
        RefundBillVO refundBill = refundOrder.getRefundBill();
        if (Objects.isNull(refundBill)) {
            refundBill = new RefundBillVO();
            refundBill.setRefundId(refundOrder.getRefundId());
            refundBill.setActualReturnPrice(refundOrder.getReturnPrice());
            refundBill.setActualReturnPoints(refundOrder.getReturnPoints());
        }
        refundBill.setOfflineAccountId(request.getOfflineAccountId());
        refundBill.setCreateTime(DateUtil.parseDate(request.getCreateTime()));
        returnOrderProvider.offlineRefundForBoss(ReturnOrderOfflineRefundForBossRequest.builder().rid(rid)
                .refundBill(KsBeanUtil.convert(refundBill, RefundBillDTO.class))
                .operator(commonUtil.getOperator()).tid(returnOrder.getTid()).build());

        ReturnOrderByIdResponse returnOrderByIdResponse = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid).build()).getContext();

        ReturnOrderDTO requestDto = KsBeanUtil.convert(returnOrderByIdResponse, ReturnOrderDTO.class);

        ReturnOrderRequest providerrequest = new ReturnOrderRequest();

        providerrequest.setReturnOrderDTO(requestDto);

        //操作日志记录
        operateLogMQUtil.convertAndSend("财务", "确认退款", "确认退款：退单编号" + returnOrder.getId());

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 根据退单查询客户收款账户
     *
     * @param rid
     * @return
     */
    @ApiOperation(value = "根据退单查询客户收款账户")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid",
            value = "退单Id", required = true)
    @RequestMapping(value = "/customer/account/{rid}", method = RequestMethod.GET)
    public BaseResponse<CustomerAccountVO> findCustomerAccountBy(@PathVariable String rid) {
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid)
                .build()).getContext();
        if (Objects.isNull(returnOrder)) {
            //退单不存在
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        CustomerAccountVO customerAccount = null;
        if (Objects.isNull(returnOrder.getCustomerAccount())) {
            //不存在临时账号，从流水获取客户收款账号
            RefundOrderByReturnCodeResponse refundOrder = refundOrderQueryProvider.getByReturnOrderCode(new RefundOrderByReturnOrderCodeRequest(rid)).getContext();
            if (Objects.isNull(refundOrder.getRefundBill()) || Objects.isNull(refundOrder.getRefundBill().getCustomerAccountId())) {
                // 没有流水 活着
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            refundOrder.getRefundBill().getCustomerAccountId();
            CustomerAccountRequest customerAccountRequest = new CustomerAccountRequest();
            customerAccountRequest.setCustomerAccountId(refundOrder.getRefundBill().getCustomerAccountId());
            BaseResponse<CustomerAccountResponse> baseResponse = customerAccountQueryProvider.getByCustomerAccountId(customerAccountRequest);
            CustomerAccountResponse customerAccountResponse = baseResponse.getContext();
            KsBeanUtil.copyPropertiesThird(customerAccountResponse, customerAccount);
        } else {
            customerAccount = returnOrder.getCustomerAccount();
        }
        return BaseResponse.success(customerAccount);
    }
}
