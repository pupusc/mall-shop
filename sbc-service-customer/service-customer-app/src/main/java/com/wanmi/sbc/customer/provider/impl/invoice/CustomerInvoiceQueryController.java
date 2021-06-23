package com.wanmi.sbc.customer.provider.impl.invoice;

import com.wanmi.sbc.common.base.BaseQueryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.invoice.CustomerInvoiceQueryProvider;
import com.wanmi.sbc.customer.api.request.invoice.*;
import com.wanmi.sbc.customer.api.response.invoice.*;
import com.wanmi.sbc.customer.bean.vo.CustomerInvoiceVO;
import com.wanmi.sbc.customer.invoice.model.entity.CustomerInvoiceQueryRequest;
import com.wanmi.sbc.customer.invoice.model.root.CustomerInvoice;
import com.wanmi.sbc.customer.invoice.model.root.CustomerInvoiceInfoResponse;
import com.wanmi.sbc.customer.invoice.model.root.CustomerInvoiceResponse;
import com.wanmi.sbc.customer.invoice.service.CustomerInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/19 9:31
 * @Version: 1.0
 */
@Validated
@RestController
public class CustomerInvoiceQueryController implements CustomerInvoiceQueryProvider {

    @Autowired
    private CustomerInvoiceService customerInvoiceService;

    /**
     * 分页查询会员增票资质
     * @param customerInvoicePageRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerInvoicePageResponse> page(@RequestBody @Valid CustomerInvoicePageRequest customerInvoicePageRequest) {
        CustomerInvoiceQueryRequest queryRequest = new CustomerInvoiceQueryRequest();
        KsBeanUtil.copyPropertiesThird(customerInvoicePageRequest,queryRequest);
        BaseQueryResponse<CustomerInvoiceResponse> customerInvoiceResponseBaseQueryResponse = customerInvoiceService.page(queryRequest);
        if (CollectionUtils.isEmpty(customerInvoiceResponseBaseQueryResponse.getData())){
            return BaseResponse.SUCCESSFUL();
        }
        List<CustomerInvoiceVO> customerInvoiceVOList = new ArrayList<>();
        KsBeanUtil.copyList(customerInvoiceResponseBaseQueryResponse.getData(),customerInvoiceVOList);
        MicroServicePage<CustomerInvoiceVO> customerInvoiceVOPage = new MicroServicePage<>(customerInvoiceVOList,customerInvoicePageRequest.getPageable(),customerInvoiceResponseBaseQueryResponse.getTotal());
        CustomerInvoicePageResponse customerInvoicePageResponse = CustomerInvoicePageResponse.builder().customerInvoiceVOPage(customerInvoiceVOPage).build();
        return BaseResponse.success(customerInvoicePageResponse);
    }

    /**
     * 根据用户ID查询会员增票资质（未删除且已审核）
     * @param customerInvoiceByCustomerIdRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse> getByCustomerIdAndDelFlagAndCheckState(@RequestBody @Valid CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateRequest customerInvoiceByCustomerIdRequest) {
        CustomerInvoice customerInvoice = customerInvoiceService.findByCustomerIdAndDelFlag(customerInvoiceByCustomerIdRequest.getCustomerId()).orElse(null);
        CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse = CustomerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse.builder().build();
        if (Objects.isNull(customerInvoice)){
            return BaseResponse.SUCCESSFUL();
        }
        KsBeanUtil.copyPropertiesThird(customerInvoice,customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse);
        return BaseResponse.success(customerInvoiceByCustomerIdAndDelFlagAndCheckStateResponse);
    }

    /**
     * 根据会员ID查询会员增票资质（已审核）
     * @param customerInvoiceByCustomerIdAndCheckStateRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerInvoiceByCustomerIdAndCheckStateResponse> getByCustomerIdAndCheckState(@RequestBody @Valid CustomerInvoiceByCustomerIdAndCheckStateRequest customerInvoiceByCustomerIdAndCheckStateRequest) {
        CustomerInvoice customerInvoice = customerInvoiceService.findByCustomerIdAndCheckState(customerInvoiceByCustomerIdAndCheckStateRequest.getCustomerId()).orElse(null);
        CustomerInvoiceByCustomerIdAndCheckStateResponse customerInvoiceByCustomerIdAndCheckStateResponse = CustomerInvoiceByCustomerIdAndCheckStateResponse.builder().build();
        if (Objects.isNull(customerInvoice)){
            return BaseResponse.SUCCESSFUL();
        }
        KsBeanUtil.copyPropertiesThird(customerInvoice,customerInvoiceByCustomerIdAndCheckStateResponse);
        return BaseResponse.success(customerInvoiceByCustomerIdAndCheckStateResponse);
    }

    /**
     * 根据用户ID查询会员增票资质（未删除）
     * @param customerInvoiceByCustomerIdRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerInvoiceByCustomerIdAndDelFlagResponse> getByCustomerIdAndDelFlag(@RequestBody @Valid CustomerInvoiceByCustomerIdAndDelFlagRequest customerInvoiceByCustomerIdRequest) {
        CustomerInvoice customerInvoice = customerInvoiceService.findCustomerInvoiceByCustomerId(customerInvoiceByCustomerIdRequest.getCustomerId()).orElse(null);
        CustomerInvoiceByCustomerIdAndDelFlagResponse customerInvoiceByCustomerIdAndDelFlagResponse = CustomerInvoiceByCustomerIdAndDelFlagResponse.builder().build();
        if (Objects.isNull(customerInvoice)){
            return BaseResponse.SUCCESSFUL();
        }
        KsBeanUtil.copyPropertiesThird(customerInvoice,customerInvoiceByCustomerIdAndDelFlagResponse);
        return BaseResponse.success(customerInvoiceByCustomerIdAndDelFlagResponse);
    }

    /**
     * 根据用户ID查询是否有会员增票资质
     * @param customerInvoiceByCustomerIdRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerInvoiceByCustomerIdResponse> getByCustomerId(@RequestBody @Valid CustomerInvoiceByCustomerIdRequest customerInvoiceByCustomerIdRequest) {
        CustomerInvoiceInfoResponse customerInvoiceInfoResponse = customerInvoiceService.findByCustomerId(customerInvoiceByCustomerIdRequest.getCustomerId());
        CustomerInvoiceByCustomerIdResponse customerInvoiceByCustomerIdResponse = CustomerInvoiceByCustomerIdResponse.builder().build();
        if (Objects.isNull(customerInvoiceInfoResponse)){
            return BaseResponse.SUCCESSFUL();
        }
        CustomerInvoiceVO customerInvoiceVO = new CustomerInvoiceVO();
        KsBeanUtil.copyPropertiesThird(customerInvoiceInfoResponse.getCustomerInvoiceResponse(),customerInvoiceVO);
        KsBeanUtil.copyPropertiesThird(customerInvoiceInfoResponse,customerInvoiceByCustomerIdResponse);
        return BaseResponse.success(customerInvoiceByCustomerIdResponse.toBuilder().customerInvoiceResponse(customerInvoiceVO).build());
    }

    /**
     * 根据增票资质ID查询会员增票资质（未删除）
     * @param customerInvoiceByCustomerIdRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerInvoiceByIdAndDelFlagResponse> getByIdAndDelFlag(@RequestBody @Valid CustomerInvoiceByIdAndDelFlagRequest customerInvoiceByCustomerIdRequest) {
        CustomerInvoice customerInvoice = customerInvoiceService.findOne(customerInvoiceByCustomerIdRequest.getCustomerInvoiceId()).orElse(null);
        if (Objects.isNull(customerInvoice)){
            return BaseResponse.SUCCESSFUL();
        }
        CustomerInvoiceByIdAndDelFlagResponse customerInvoiceByIdAndDelFlagResponse = CustomerInvoiceByIdAndDelFlagResponse.builder().build();
        KsBeanUtil.copyPropertiesThird(customerInvoice,customerInvoiceByIdAndDelFlagResponse);
        return BaseResponse.success(customerInvoiceByIdAndDelFlagResponse);
    }

    /**
     * 根据纳税人识别号查询客户的增专票信息（未删除）
     * @param customerInvoiceByTaxpayerNumberRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerInvoiceByTaxpayerNumberResponse> getByTaxpayerNumber(@RequestBody @Valid CustomerInvoiceByTaxpayerNumberRequest customerInvoiceByTaxpayerNumberRequest) {
        CustomerInvoice customerInvoice = customerInvoiceService.findByTaxpayerNumberAndDelFlag(customerInvoiceByTaxpayerNumberRequest.getTaxpayerNumber()).orElse(null);
        if (Objects.isNull(customerInvoice)){
            return BaseResponse.SUCCESSFUL();
        }
        CustomerInvoiceByTaxpayerNumberResponse customerInvoiceByTaxpayerNumberResponse = CustomerInvoiceByTaxpayerNumberResponse.builder().build();
        KsBeanUtil.copyPropertiesThird(customerInvoice,customerInvoiceByTaxpayerNumberResponse);
        return BaseResponse.success(customerInvoiceByTaxpayerNumberResponse);
    }
}
