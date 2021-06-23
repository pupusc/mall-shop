package com.wanmi.sbc.customer.provider.impl.address;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.address.model.root.CustomerDeliveryAddress;
import com.wanmi.sbc.customer.address.request.CustomerDeliveryAddressQueryRequest;
import com.wanmi.sbc.customer.address.service.CustomerDeliveryAddressService;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressQueryProvider;
import com.wanmi.sbc.customer.api.request.address.*;
import com.wanmi.sbc.customer.api.response.address.*;
import com.wanmi.sbc.customer.bean.vo.CustomerDeliveryAddressVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/17 9:35
 * @Version: 1.0
 */
@Validated
@RestController
public class CustomerDeliveryAddressQueryController implements CustomerDeliveryAddressQueryProvider{

    @Autowired
    private CustomerDeliveryAddressService customerDeliveryAddressService;

    /**
     * 根据收货地址ID查询会员收货地址信息
     *
     * @param customerDeliveryAddressByIdRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerDeliveryAddressByIdResponse> getById(@RequestBody @Valid
                                                                      CustomerDeliveryAddressByIdRequest customerDeliveryAddressByIdRequest){
        CustomerDeliveryAddress customerDeliveryAddress = customerDeliveryAddressService.findById(customerDeliveryAddressByIdRequest.getDeliveryAddressId());
        CustomerDeliveryAddressByIdResponse customerDeliveryAddressByIdResponse = CustomerDeliveryAddressByIdResponse.builder().build();
        if (Objects.isNull(customerDeliveryAddress)){
            return BaseResponse.success(customerDeliveryAddressByIdResponse);
        }
        KsBeanUtil.copyPropertiesThird(customerDeliveryAddress,customerDeliveryAddressByIdResponse);
        return BaseResponse.success(customerDeliveryAddressByIdResponse);
    }

    /**
     * 查询客户默认的收货地址
     * 如果客户没有设置默认地址，则取该客户其中的一条收货地址
     *
     * @param customerDeliveryAddressRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerDeliveryAddressResponse> getDefaultOrAnyOneByCustomerId(@RequestBody @Valid
                                                                                         CustomerDeliveryAddressRequest customerDeliveryAddressRequest){
        CustomerDeliveryAddressQueryRequest customerDeliveryAddressQueryRequest = new CustomerDeliveryAddressQueryRequest();
        KsBeanUtil.copyPropertiesThird(customerDeliveryAddressRequest,customerDeliveryAddressQueryRequest);
        CustomerDeliveryAddress customerDeliveryAddress = customerDeliveryAddressService.findDefault(customerDeliveryAddressQueryRequest);
        CustomerDeliveryAddressResponse customerDeliveryAddressResponse = CustomerDeliveryAddressResponse.builder().build();
        if (Objects.isNull(customerDeliveryAddress)){
            return BaseResponse.SUCCESSFUL();
        }
        KsBeanUtil.copyPropertiesThird(customerDeliveryAddress,customerDeliveryAddressResponse);
        return BaseResponse.success(customerDeliveryAddressResponse);
    }

    /**
     * 根据用户ID查询用户收货地址列表
     *
     * @param customerDeliveryAddressListRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerDeliveryAddressListResponse> listByCustomerId(@RequestBody @Valid
                                                                               CustomerDeliveryAddressListRequest customerDeliveryAddressListRequest){
        List<CustomerDeliveryAddress> customerDeliveryAddressList = customerDeliveryAddressService.findAddressList(customerDeliveryAddressListRequest.getCustomerId());
        if (CollectionUtils.isEmpty(customerDeliveryAddressList)){
            return BaseResponse.success(new CustomerDeliveryAddressListResponse());
        }
        List<CustomerDeliveryAddressVO> customerDeliveryAddressVOList = KsBeanUtil.convert(customerDeliveryAddressList,CustomerDeliveryAddressVO.class);
        customerDeliveryAddressService.fillArea(customerDeliveryAddressVOList);
        customerDeliveryAddressService.verifyAddress(customerDeliveryAddressVOList);
        CustomerDeliveryAddressListResponse customerDeliveryAddressListResponse = CustomerDeliveryAddressListResponse.builder().customerDeliveryAddressVOList(customerDeliveryAddressVOList).build();
        return BaseResponse.success(customerDeliveryAddressListResponse);
    }

    /**
     * 只查询客户默认地址
     *
     * @param customerDeliveryAddressDefaultRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerDeliveryAddressDefaultResponse> getDefaultByCustomerId(@RequestBody @Valid
                                                                                        CustomerDeliveryAddressDefaultRequest customerDeliveryAddressDefaultRequest){
        CustomerDeliveryAddress customerDeliveryAddress = customerDeliveryAddressService.getDefault(customerDeliveryAddressDefaultRequest.getCustomerId());
        CustomerDeliveryAddressDefaultResponse customerDeliveryAddressDefaultResponse = CustomerDeliveryAddressDefaultResponse.builder().build();
        if (Objects.isNull(customerDeliveryAddress)){
            return BaseResponse.success(customerDeliveryAddressDefaultResponse);
        }
        KsBeanUtil.copyPropertiesThird(customerDeliveryAddress,customerDeliveryAddressDefaultResponse);
        return BaseResponse.success(customerDeliveryAddressDefaultResponse);
    }

    /**
     * 查询该客户有多少条收货地址
     *
     * @param customerDeliveryAddressByCustomerIdRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerDeliveryAddressByCustomerIdResponse> countByCustomerId(@RequestBody @Valid
                                                    CustomerDeliveryAddressByCustomerIdRequest
                                                    customerDeliveryAddressByCustomerIdRequest){
        Integer result = customerDeliveryAddressService.countCustomerAddress(customerDeliveryAddressByCustomerIdRequest.getCustomerId());
        return BaseResponse.success(new CustomerDeliveryAddressByCustomerIdResponse(result));
    }
}
