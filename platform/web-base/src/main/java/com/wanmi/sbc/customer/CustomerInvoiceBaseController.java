package com.wanmi.sbc.customer;

import com.wanmi.sbc.account.api.constant.AccountErrorCode;
import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectSwitchQueryProvider;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchListByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchByCompanyInfoIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchListByCompanyInfoIdResponse;
import com.wanmi.sbc.account.bean.vo.InvoiceProjectSwitchVO;
import com.wanmi.sbc.account.request.InvoiceProjectSwitchRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.provider.invoice.CustomerInvoiceProvider;
import com.wanmi.sbc.customer.api.provider.invoice.CustomerInvoiceQueryProvider;
import com.wanmi.sbc.customer.api.request.invoice.*;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceByCustomerIdAndDelFlagResponse;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceByTaxpayerNumberResponse;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.response.InvoiceConfigGetResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 赠票配置相关
 * Created by CHENLI on 2017/4/21.
 */
@RestController
@RequestMapping("/customer")
@Api(tags = "CustomerInvoiceBaseController", description = "S2B web公用-客户增票信息API")
public class CustomerInvoiceBaseController {

    @Autowired
    private CustomerInvoiceQueryProvider customerInvoiceQueryProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private CustomerInvoiceProvider customerInvoiceProvider;

    @Autowired
    private InvoiceProjectSwitchQueryProvider invoiceProjectSwitchQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 根据会员ID查询会员增专票
     * @return ResponseEntity<CustomerInvoice>
     */
    @ApiOperation(value = "根据会员ID查询未删除的会员增专票")
    @RequestMapping(value = "/invoice",method = RequestMethod.GET)
    public BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagResponse> findCustomerInvoiceByCustomerId() {
        CustomerInvoiceByCustomerIdAndDelFlagRequest customerInvoiceByCustomerIdAndDelFlagRequest = new CustomerInvoiceByCustomerIdAndDelFlagRequest();
        customerInvoiceByCustomerIdAndDelFlagRequest.setCustomerId(commonUtil.getOperatorId());
        BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagResponse> customerInvoiceByCustomerIdAndDelFlagResponseBaseResponse = customerInvoiceQueryProvider.getByCustomerIdAndDelFlag(customerInvoiceByCustomerIdAndDelFlagRequest);
        CustomerInvoiceByCustomerIdAndDelFlagResponse response = CustomerInvoiceByCustomerIdAndDelFlagResponse.builder().build();
        if(customerInvoiceByCustomerIdAndDelFlagResponseBaseResponse.getContext() != null){
            BeanUtils.copyProperties(customerInvoiceByCustomerIdAndDelFlagResponseBaseResponse.getContext(), response);
        }
        return BaseResponse.success(response);
    }

    /**
     * 新增会员增专票
     * @return employee
     */
    @ApiOperation(value = "新增会员增专票")
    @RequestMapping(value = "/invoice", method = RequestMethod.POST)
    public BaseResponse saveCustomerInvoice(@Valid @RequestBody CustomerInvoiceAddRequest saveRequest) {
        String customerId = commonUtil.getOperatorId();
        CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest
                customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest =
                CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest.builder().customerId(customerId).build();
        BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse> invoiceByCustomerIdAndDelFlagAndCheckStateResponseBaseResponse =  customerInvoiceQueryProvider.getByCustomerIdAndDelFlagAndCheckState(customerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest);
        CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse customerIdAndDelFlagAndCheckStateResponse = invoiceByCustomerIdAndDelFlagAndCheckStateResponseBaseResponse.getContext();
        if(Objects.nonNull(customerIdAndDelFlagAndCheckStateResponse)){
            return BaseResponse.error("每个客户只可保存一条增票资质");
        }
        CustomerInvoiceByTaxpayerNumberRequest customerInvoiceByTaxpayerNumberRequest = new CustomerInvoiceByTaxpayerNumberRequest();
        customerInvoiceByTaxpayerNumberRequest.setTaxpayerNumber(saveRequest.getTaxpayerNumber());
        BaseResponse<CustomerInvoiceByTaxpayerNumberResponse> invoiceByTaxpayerNumberResponseBaseResponse = customerInvoiceQueryProvider.getByTaxpayerNumber(customerInvoiceByTaxpayerNumberRequest);
        CustomerInvoiceByTaxpayerNumberResponse customerInvoiceByTaxpayerNumberResponse = invoiceByTaxpayerNumberResponseBaseResponse.getContext();
        if(Objects.nonNull(customerInvoiceByTaxpayerNumberResponse)){
            return BaseResponse.error("纳税人识别号已存在");
        }

        saveRequest.setCustomerId(customerId);
        saveRequest.setEmployeeId(null);
        customerInvoiceProvider.add(saveRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改会员增专票
     * @param saveRequest
     * @return employee
     */
    @ApiOperation(value = "修改会员增专票")
    @RequestMapping(value = "/invoice", method = RequestMethod.PUT)
    public BaseResponse updateCustomerInvoice(@Valid @RequestBody CustomerInvoiceModifyRequest saveRequest) {
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
            return BaseResponse.error("纳税人识别号已存在");
        }
        saveRequest.setEmployeeId(null);
        customerInvoiceProvider.modify(saveRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据会员ID查询审核通过的会员增专票
     * @return ResponseEntity<CustomerInvoiceInfoResponse>
     */
    @ApiOperation(value = "根据会员ID查询审核通过的会员增专票")
    @RequestMapping(value = "/invoiceInfo",method = RequestMethod.GET)
    public BaseResponse<CustomerInvoiceByCustomerIdResponse> findInfoByCustomerId() {
        BaseResponse<InvoiceConfigGetResponse> customerInvoiceConfigResponseBaseResponse = auditQueryProvider.getInvoiceConfig();
        InvoiceConfigGetResponse customerInvoiceConfigResponse = customerInvoiceConfigResponseBaseResponse.getContext();
        if (Objects.nonNull(customerInvoiceConfigResponse) && DefaultFlag.YES.toValue() == customerInvoiceConfigResponse.getStatus()){
            CustomerInvoiceByCustomerIdRequest request = new CustomerInvoiceByCustomerIdRequest();
            request.setCustomerId(commonUtil.getOperatorId());
            BaseResponse<CustomerInvoiceByCustomerIdResponse> customerInvoiceByCustomerIdResponseBaseResponse = customerInvoiceQueryProvider.getByCustomerId(request);
            CustomerInvoiceByCustomerIdResponse invoiceInfoResponse = customerInvoiceByCustomerIdResponseBaseResponse.getContext();
            invoiceInfoResponse.setConfigFlag(Boolean.TRUE);
            return BaseResponse.success(invoiceInfoResponse);
        }
        return BaseResponse.success(CustomerInvoiceByCustomerIdResponse.builder().build());
    }

    /**
     * 根据商家公司ID查询会员增专票
     * @return ResponseEntity<CustomerInvoiceInfoResponse>
     */
    @ApiOperation(value = "根据商家公司ID查询会员增专票")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "companyInfoId", value = "商家公司ID", required = true)
    @RequestMapping(value = "/invoiceInfo/{companyInfoId}",method = RequestMethod.GET)
    public BaseResponse<CustomerInvoiceByCustomerIdResponse> findInfoByCustomerId(@PathVariable("companyInfoId") Long companyInfoId) {
        InvoiceProjectSwitchByCompanyInfoIdRequest invoiceProjectSwitchByCompanyInfoIdRequest = new InvoiceProjectSwitchByCompanyInfoIdRequest();
        invoiceProjectSwitchByCompanyInfoIdRequest.setCompanyInfoId(companyInfoId);
        BaseResponse<InvoiceProjectSwitchByCompanyInfoIdResponse> baseResponse = invoiceProjectSwitchQueryProvider.getByCompanyInfoId(invoiceProjectSwitchByCompanyInfoIdRequest);
        InvoiceProjectSwitchByCompanyInfoIdResponse response = baseResponse.getContext();
        if (Objects.isNull(response)){
            throw new SbcRuntimeException(AccountErrorCode.INVOICE_SWITCH_NOT_EXIST);
        }
        //如果不支持开票
        if (response.getIsSupportInvoice() == DefaultFlag.NO){
            throw new SbcRuntimeException(AccountErrorCode.INVOICE_NOT_SUPPORT);
        }
        CustomerInvoiceByCustomerIdResponse invoiceInfoResponse = CustomerInvoiceByCustomerIdResponse.builder().build();
        //支持增值税发票
        if (DefaultFlag.YES == response.getIsValueAddedTaxInvoice()){
            CustomerInvoiceByCustomerIdRequest request = new CustomerInvoiceByCustomerIdRequest();
            request.setCustomerId(commonUtil.getOperatorId());
            BaseResponse<CustomerInvoiceByCustomerIdResponse> customerInvoiceByCustomerIdResponseBaseResponse = customerInvoiceQueryProvider.getByCustomerId(request);
             invoiceInfoResponse = customerInvoiceByCustomerIdResponseBaseResponse.getContext();
            invoiceInfoResponse.setConfigFlag(Boolean.TRUE);
        }
        //是否支持纸质发票
        if (DefaultFlag.YES == response.getIsPaperInvoice()){
            invoiceInfoResponse.setPaperInvoice(Boolean.TRUE);
        }
        return BaseResponse.success(invoiceInfoResponse);
    }

    /**
     * pc订单确认页面批量查询每个店铺下的发票状态
     * @return ResponseEntity<CustomerInvoiceInfoResponse>
     */
    @ApiOperation(value = "pc订单确认页面批量查询每个店铺下的发票状态")
    @RequestMapping(value = "/invoices",method = RequestMethod.POST)
    public BaseResponse<List<CustomerInvoiceByCustomerIdResponse>> findInvoices(@RequestBody InvoiceProjectSwitchRequest request) {
        List<Long> companyInfoIds = request.getCompanyInfoIds();
        CustomerInvoiceByCustomerIdRequest customerInvoiceByCustomerIdRequest = new CustomerInvoiceByCustomerIdRequest();
        customerInvoiceByCustomerIdRequest.setCustomerId(commonUtil.getOperatorId());
        BaseResponse<CustomerInvoiceByCustomerIdResponse> customerInvoiceByCustomerIdResponseBaseResponse = customerInvoiceQueryProvider.getByCustomerId(customerInvoiceByCustomerIdRequest);
        CustomerInvoiceByCustomerIdResponse invoice = customerInvoiceByCustomerIdResponseBaseResponse.getContext();
        InvoiceProjectSwitchListByCompanyInfoIdRequest invoiceProjectSwitchListByCompanyInfoIdRequest = new InvoiceProjectSwitchListByCompanyInfoIdRequest();
        invoiceProjectSwitchListByCompanyInfoIdRequest.setCompanyInfoIds(companyInfoIds);
        BaseResponse<InvoiceProjectSwitchListByCompanyInfoIdResponse> baseResponse = invoiceProjectSwitchQueryProvider.listByCompanyInfoId(invoiceProjectSwitchListByCompanyInfoIdRequest);
        InvoiceProjectSwitchListByCompanyInfoIdResponse invoiceProjectSwitchListByCompanyInfoIdResponse = baseResponse.getContext();
        if (Objects.isNull(invoiceProjectSwitchListByCompanyInfoIdResponse)){
            return null;
        }
        return BaseResponse.success(companyInfoIds.stream().map(id -> {
            InvoiceProjectSwitchVO project = invoiceProjectSwitchListByCompanyInfoIdResponse.getInvoiceProjectSwitchVOList().stream().
                    filter(p -> Objects.equals(id,p.getCompanyInfoId())).findFirst()
                    .orElse(new InvoiceProjectSwitchVO());
            CustomerInvoiceByCustomerIdResponse response = CustomerInvoiceByCustomerIdResponse.builder().build();
            //支持增值税发票
            if(project.getIsValueAddedTaxInvoice() == DefaultFlag.YES) {
                BeanUtils.copyProperties(invoice, response);
                response.setConfigFlag(Boolean.TRUE);
            }
            response.setCompanyInfoId(id);
            //是否开启发票
            response.setSupport(project.getIsSupportInvoice() == DefaultFlag.YES);
            //是否支持纸质发票
            response.setPaperInvoice(project.getIsPaperInvoice() == DefaultFlag.YES);
            return response;
        }).collect(Collectors.toList()));
    }
}
