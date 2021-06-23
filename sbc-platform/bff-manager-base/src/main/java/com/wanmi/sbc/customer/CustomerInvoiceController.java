package com.wanmi.sbc.customer;

import com.google.common.collect.Lists;
import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.common.base.BaseQueryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.invoice.CustomerInvoiceProvider;
import com.wanmi.sbc.customer.api.provider.invoice.CustomerInvoiceQueryProvider;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.invoice.*;
import com.wanmi.sbc.customer.api.response.invoice.*;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerInvoiceVO;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.response.InvoiceConfigGetResponse;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Collections;
import java.util.Objects;

import static java.util.Objects.nonNull;

/**
 * 会员增专票bff
 * Created by CHENLI on 2017/4/21.
 */
@Api(tags = "CustomerInvoiceController", description = "会员增专票")
@RestController
@RequestMapping("/customer")
@Validated
public class CustomerInvoiceController {

    @Autowired
    private CustomerInvoiceQueryProvider customerInvoiceQueryProvider;

    @Autowired
    private CustomerInvoiceProvider customerInvoiceProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 分页查询会员增专票信息
     *
     * @param queryRequest
     * @return
     */
    @ApiOperation(value = "分页查询会员增专票信息")
    @EmployeeCheck
    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> page(@RequestBody CustomerInvoicePageRequest queryRequest){
        queryRequest.putSort("createTime", SortType.DESC.toValue());
        BaseResponse<CustomerInvoicePageResponse> customerInvoicePageResponseBaseResponse = customerInvoiceQueryProvider.page(queryRequest);
        CustomerInvoicePageResponse customerInvoicePageResponse = customerInvoicePageResponseBaseResponse.getContext();
        BaseQueryResponse<CustomerInvoiceVO> baseQueryResponse = new BaseQueryResponse<>();
        if (Objects.isNull(customerInvoicePageResponse)){
            baseQueryResponse.setData(Collections.EMPTY_LIST);
            baseQueryResponse.setTotal(0L);
            baseQueryResponse.setPageNum(queryRequest.getPageNum());
            baseQueryResponse.setPageSize(queryRequest.getPageSize());
            return ResponseEntity.ok(BaseResponse.success(baseQueryResponse));
        }
        MicroServicePage<CustomerInvoiceVO> microServicePage =  customerInvoicePageResponse.getCustomerInvoiceVOPage();
        if (Objects.nonNull(microServicePage)){
            baseQueryResponse.setData(microServicePage.getContent());
            baseQueryResponse.setTotal(microServicePage.getTotal());
            baseQueryResponse.setPageNum(microServicePage.getNumber());
            baseQueryResponse.setPageSize(microServicePage.getSize());
        }
        return ResponseEntity.ok(BaseResponse.success(baseQueryResponse));
    }

    /**
     * 根据会员ID查询会员增专票
     *
     * @param customerId
     * @return ResponseEntity<CustomerInvoice>
     */
    @ApiOperation(value = "根据会员ID查询会员增专票")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员Id", required = true)
    @RequestMapping(value = "/invoice/{customerId}",method = RequestMethod.GET)
    public ResponseEntity<CustomerInvoiceByCustomerIdAndDelFlagResponse> findByCustomerIdAndDelFlag(@PathVariable("customerId") String customerId) {
        CustomerInvoiceByCustomerIdAndDelFlagRequest customerInvoiceByCustomerIdAndDelFlagRequest = new CustomerInvoiceByCustomerIdAndDelFlagRequest();
        customerInvoiceByCustomerIdAndDelFlagRequest.setCustomerId(customerId);
        BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagResponse> customerInvoiceByCustomerIdAndDelFlagResponseBaseResponse = customerInvoiceQueryProvider.getByCustomerIdAndDelFlag(customerInvoiceByCustomerIdAndDelFlagRequest);
        CustomerInvoiceByCustomerIdAndDelFlagResponse response = customerInvoiceByCustomerIdAndDelFlagResponseBaseResponse.getContext();
        return ResponseEntity.ok(Objects.nonNull(response) ? response : CustomerInvoiceByCustomerIdAndDelFlagResponse.builder().build());
    }

    /**
     * 根据会员ID查询会员增专票
     *
     * @param customerId
     * @return ResponseEntity<CustomerInvoice>
     */
    @ApiOperation(value = "根据会员ID查询会员增专票")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员Id", required = true)
    @RequestMapping(value = "/invoiceInfos/{customerId}",method = RequestMethod.GET)
    public BaseResponse<CustomerInvoiceByCustomerIdResponse> findInfoByCustomerId(@PathVariable("customerId") String customerId) {
        CustomerInvoiceByCustomerIdRequest request = new CustomerInvoiceByCustomerIdRequest();
        request.setCustomerId(customerId);
        BaseResponse<CustomerInvoiceByCustomerIdResponse> customerInvoiceByCustomerIdResponseBaseResponse = customerInvoiceQueryProvider.getByCustomerId(request);
        CustomerInvoiceByCustomerIdResponse customerInvoiceByCustomerIdResponse = customerInvoiceByCustomerIdResponseBaseResponse.getContext();
        return BaseResponse.success(customerInvoiceByCustomerIdResponse);
    }

    /**
     * 根据增专票ID查询会员增专票
     *
     * @param customerInvoiceId
     * @return ResponseEntity<CustomerInvoice>
     */
    @ApiOperation(value = "根据会员ID查询会员增专票")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerInvoiceId", value = "会员增票Id",
            required = true)
    @RequestMapping(value = "/invoiceInfo/{customerInvoiceId}",method = RequestMethod.GET)
    public ResponseEntity<CustomerInvoiceByIdAndDelFlagResponse> findByCustomerInvoiceIdAndDelFlag(@PathVariable("customerInvoiceId") Long customerInvoiceId) {
        CustomerInvoiceByIdAndDelFlagRequest customerInvoiceByIdAndDelFlagRequest = new CustomerInvoiceByIdAndDelFlagRequest();
        customerInvoiceByIdAndDelFlagRequest.setCustomerInvoiceId(customerInvoiceId);
        BaseResponse<CustomerInvoiceByIdAndDelFlagResponse> customerInvoiceByIdAndDelFlagResponseBaseResponse = customerInvoiceQueryProvider.getByIdAndDelFlag(customerInvoiceByIdAndDelFlagRequest);
        CustomerInvoiceByIdAndDelFlagResponse customerInvoiceByIdAndDelFlagResponse = customerInvoiceByIdAndDelFlagResponseBaseResponse.getContext();
        return ResponseEntity.ok(customerInvoiceByIdAndDelFlagResponse);
    }

    /**
     * 新增会员增专票
     *
     * @param request
     * @return employee
     */
    @ApiOperation(value = "新增会员增专票")
    @RequestMapping(value = "/invoice", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> saveCustomerInvoice(@Valid @RequestBody CustomerInvoiceAddRequest saveRequest, HttpServletRequest request) {
        CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest
                customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest =
                CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest.builder().customerId(saveRequest
                        .getCustomerId()).build();
        BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse> invoiceByCustomerIdAndDelFlagAndCheckStateResponseBaseResponse =  customerInvoiceQueryProvider.getByCustomerIdAndDelFlagAndCheckState(customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest);
        CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse customerIdAndDelFlagAndCheckStateResponse = invoiceByCustomerIdAndDelFlagAndCheckStateResponseBaseResponse.getContext();
        if(Objects.nonNull(customerIdAndDelFlagAndCheckStateResponse)){
            return ResponseEntity.ok(BaseResponse.error("每个客户只可保存一条增票资质"));
        }
        CustomerInvoiceByTaxpayerNumberRequest customerInvoiceByTaxpayerNumberRequest = new CustomerInvoiceByTaxpayerNumberRequest();
        customerInvoiceByTaxpayerNumberRequest.setTaxpayerNumber(saveRequest.getTaxpayerNumber());
        BaseResponse<CustomerInvoiceByTaxpayerNumberResponse> invoiceByTaxpayerNumberResponseBaseResponse = customerInvoiceQueryProvider.getByTaxpayerNumber(customerInvoiceByTaxpayerNumberRequest);
        CustomerInvoiceByTaxpayerNumberResponse customerInvoiceByTaxpayerNumberResponse = invoiceByTaxpayerNumberResponseBaseResponse.getContext();
        if(Objects.nonNull(customerInvoiceByTaxpayerNumberResponse)){
            return ResponseEntity.ok(BaseResponse.error("纳税人识别号不允许重复"));
        }
        String employeeId = ((Claims) request.getAttribute("claims")).get("employeeId").toString();
        saveRequest.setEmployeeId(employeeId);
        BaseResponse<CustomerInvoiceAddResponse> customerInvoiceAddBaseResponse= customerInvoiceProvider.add(saveRequest);
        CustomerInvoiceAddResponse customerInvoiceAddResponse = customerInvoiceAddBaseResponse.getContext();

        //操作日志记录
        if (nonNull(customerInvoiceAddResponse)) {
            CustomerDetailVO customerDetail = customerDetailQueryProvider.getCustomerDetailByCustomerId(
                    CustomerDetailByCustomerIdRequest.builder().customerId(customerInvoiceAddResponse.getCustomerId()).build()).getContext();
            if (nonNull(customerDetail)) {
                operateLogMQUtil.convertAndSend("财务", "新增资质",
                        "新增资质：" + customerDetail.getCustomerName());
            }
        }
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 修改会员增专票
     *
     * @param saveRequest
     * @return employee
     */
    @ApiOperation(value = "修改会员增专票")
    @RequestMapping(value = "/invoice", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> updateCustomerInvoice(@Valid @RequestBody CustomerInvoiceModifyRequest saveRequest, HttpServletRequest request) {
        final boolean[] flag = {false};
        CustomerInvoiceByTaxpayerNumberRequest customerInvoiceByTaxpayerNumberRequest = new CustomerInvoiceByTaxpayerNumberRequest();
        customerInvoiceByTaxpayerNumberRequest.setTaxpayerNumber(saveRequest.getTaxpayerNumber());
        BaseResponse<CustomerInvoiceByTaxpayerNumberResponse> invoiceByTaxpayerNumberResponseBaseResponse = customerInvoiceQueryProvider.getByTaxpayerNumber(customerInvoiceByTaxpayerNumberRequest);
        CustomerInvoiceByTaxpayerNumberResponse customerInvoiceByTaxpayerNumberResponse = invoiceByTaxpayerNumberResponseBaseResponse.getContext();
        if (Objects.nonNull(customerInvoiceByTaxpayerNumberResponse)){
            if(!customerInvoiceByTaxpayerNumberResponse.getCustomerInvoiceId().equals(saveRequest.getCustomerInvoiceId())){
                flag[0] = true;
            }
        }
        if(flag[0]){
            return ResponseEntity.ok(BaseResponse.error("纳税人识别号不允许重复"));
        }
        String employeeId = ((Claims) request.getAttribute("claims")).get("employeeId").toString();
        saveRequest.setEmployeeId(employeeId);
        customerInvoiceProvider.modify(saveRequest);
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 单条 / 批量审核 增专票信息
     *
     * @param invoiceBatchRequest invoiceBatchRequest
     * @return
     */
    @ApiOperation(value = "单条 / 批量审核 增专票信息")
    @RequestMapping(value = "/invoice/checklist", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> checkCustomerInvoice(@RequestBody CustomerInvoiceAuditingRequest invoiceBatchRequest){
        customerInvoiceProvider.auditing(invoiceBatchRequest);

        if (CollectionUtils.size(invoiceBatchRequest.getCustomerInvoiceIds()) == 1) {
            CustomerDetailVO detail = this.queryCustomerDetail(invoiceBatchRequest.getCustomerInvoiceIds().get(0));
            if(nonNull(detail)){
                operateLogMQUtil.convertAndSend("财务", "审核资质",
                        "审核资质：" + detail.getCustomerName());

            }
        } else {
            operateLogMQUtil.convertAndSend("财务", "批量审核", "批量审核");
        }

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 驳回 增专票信息
     *
     * @param invoiceBatchRequest invoiceBatchRequest
     * @return
     */
    @ApiOperation(value = "驳回 增专票信息")
    @RequestMapping(value = "/invoice/reject", method = RequestMethod.PUT)
    public BaseResponse rejectInvoice(@RequestBody CustomerInvoiceRejectRequest invoiceBatchRequest){
        if (Objects.isNull(invoiceBatchRequest.getCustomerInvoiceId()) || StringUtils.isBlank(invoiceBatchRequest.getRejectReason())){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        customerInvoiceProvider.reject(invoiceBatchRequest);

        //操作日志记录
        CustomerDetailVO customerDetail = this.queryCustomerDetail(invoiceBatchRequest.getCustomerInvoiceId());
        if (nonNull(customerDetail)) {
                operateLogMQUtil.convertAndSend("财务", "驳回资质", "驳回资质：" + customerDetail.getCustomerName());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据customerInvoiceId 查询CustomerDetail
     *
     * @param customerInvoiceId
     * @return
     */
    private CustomerDetailVO queryCustomerDetail(Long customerInvoiceId) {
        CustomerInvoiceByIdAndDelFlagRequest customerInvoiceByIdAndDelFlagRequest = new CustomerInvoiceByIdAndDelFlagRequest();
        customerInvoiceByIdAndDelFlagRequest.setCustomerInvoiceId(customerInvoiceId);
        BaseResponse<CustomerInvoiceByIdAndDelFlagResponse> customerInvoiceByIdAndDelFlagResponseBaseResponse = customerInvoiceQueryProvider.getByIdAndDelFlag(customerInvoiceByIdAndDelFlagRequest);
        CustomerInvoiceByIdAndDelFlagResponse customerInvoiceByIdAndDelFlagResponse = customerInvoiceByIdAndDelFlagResponseBaseResponse.getContext();

        if (Objects.isNull(customerInvoiceByIdAndDelFlagResponse)) {
            return null;
        }
        //获取会员
        CustomerDetailVO customerDetail = customerDetailQueryProvider.getCustomerDetailByCustomerId(
                CustomerDetailByCustomerIdRequest.builder().customerId(customerInvoiceByIdAndDelFlagResponse.getCustomerId()).build()).getContext();

        if (nonNull(customerDetail))  {
            //如果存在
            return customerDetail;
        }
        return null;
    }

    /**
     * 作废 增专票信息
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "作废 增专票信息")
    @RequestMapping(value = "/invalidInvoice", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> invalidCustomerInvoice(@RequestBody CustomerInvoiceInvalidRequest request){
        customerInvoiceProvider.invalid(request);
        List<Long> customerInvoiceIds = request.getCustomerInvoiceIds();
        //操作日志记录
        if (CollectionUtils.isNotEmpty(customerInvoiceIds)) {
            for (Long customerInvoiceId : customerInvoiceIds) {
                CustomerDetailVO customerDetail = this.queryCustomerDetail(customerInvoiceId);
                if (nonNull(customerDetail)) {
                    operateLogMQUtil.convertAndSend("财务", "作废资质", "作废资质：" + customerDetail.getCustomerName());
                }
            }
        }
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 删除 增专票信息
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "删除 增专票信息")
    @RequestMapping(value = "/invoices", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponse> deleteCustomerInvoice(@RequestBody CustomerInvoiceDeleteRequest request){
        List<Long> customerInvoiceIds = request.getCustomerInvoiceIds();
        //查询客户详情
        List<CustomerDetailVO> detailList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(customerInvoiceIds)) {
            for (Long customerInvoiceId : customerInvoiceIds) {
                CustomerDetailVO customerDetail = this.queryCustomerDetail(customerInvoiceId);
                if (nonNull(customerDetail)) {
                    detailList.add(customerDetail);
                }
            }
        }

        customerInvoiceProvider.delete(request);

        //操作日志记录
        for (CustomerDetailVO customerDetail : detailList) {
            operateLogMQUtil.convertAndSend("财务", "删除资质", "删除资质：" + customerDetail.getCustomerName());
        }
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 保存订单开票配置
     *
     * @param status status
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "保存订单开票配置")
    @RequestMapping(value = "/invoiceConfig", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> saveInvoiceConfig(Integer status){
        CustomerInvoiceConfigAddRequest request = new CustomerInvoiceConfigAddRequest();
        request.setStatus(status);
        customerInvoiceProvider.addCustomerInvoiceConfig(request);
        operateLogMQUtil.convertAndSend("设置", "修改审核开关", "修改审核开关：增专资质审核设为" + (status == 1 ? "开" : "关"));
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 查询增专资质审核状态按钮
     *
     * @return ResponseEntity
     */
    @ApiOperation(value = "查询增专资质审核状态按钮")
    @RequestMapping(value = "/invoiceConfig", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse<InvoiceConfigGetResponse>> queryInvoiceConfig() {
        BaseResponse<InvoiceConfigGetResponse> customerInvoiceConfigResponseBaseResponse = auditQueryProvider.getInvoiceConfig();
        return ResponseEntity.ok(customerInvoiceConfigResponseBaseResponse);
    }
}
