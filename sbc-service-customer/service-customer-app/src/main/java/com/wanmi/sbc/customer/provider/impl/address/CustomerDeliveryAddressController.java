package com.wanmi.sbc.customer.provider.impl.address;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.address.model.root.CustomerDeliveryAddress;
import com.wanmi.sbc.customer.address.request.CustomerDeliveryAddressEditRequest;
import com.wanmi.sbc.customer.address.service.CustomerDeliveryAddressService;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressProvider;
import com.wanmi.sbc.customer.api.request.address.CustomerDeliveryAddressAddRequest;
import com.wanmi.sbc.customer.api.request.address.CustomerDeliveryAddressDeleteRequest;
import com.wanmi.sbc.customer.api.request.address.CustomerDeliveryAddressModifyDefaultRequest;
import com.wanmi.sbc.customer.api.request.address.CustomerDeliveryAddressModifyRequest;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressAddResponse;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressModifyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/17 9:35
 * @Version: 1.0
 */
@Validated
@RestController
public class CustomerDeliveryAddressController implements CustomerDeliveryAddressProvider{

    @Autowired
    private CustomerDeliveryAddressService customerDeliveryAddressService;

    /**
     * 新增会员收货地址信息
     * @param customerDeliveryAddressAddRequest
     * @return
     */

    @Override
   public BaseResponse<CustomerDeliveryAddressAddResponse> add(@RequestBody @Valid
                                                                 CustomerDeliveryAddressAddRequest customerDeliveryAddressAddRequest){
        CustomerDeliveryAddressEditRequest customerDeliveryAddressEditRequest = new CustomerDeliveryAddressEditRequest();
        KsBeanUtil.copyPropertiesThird(customerDeliveryAddressAddRequest,customerDeliveryAddressEditRequest);
        CustomerDeliveryAddress customerDeliveryAddress = customerDeliveryAddressService.saveAddress(customerDeliveryAddressEditRequest,customerDeliveryAddressAddRequest.getEmployeeId());
        CustomerDeliveryAddressAddResponse customerDeliveryAddressAddResponse = CustomerDeliveryAddressAddResponse.builder().build();
        if (Objects.isNull(customerDeliveryAddress)){
            return BaseResponse.success(customerDeliveryAddressAddResponse);
        }
        KsBeanUtil.copyPropertiesThird(customerDeliveryAddress,customerDeliveryAddressAddResponse);
        return BaseResponse.success(customerDeliveryAddressAddResponse);
    }

    /**
     * 修改会员收货地址信息
     * @param customerDeliveryAddressModifyRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerDeliveryAddressModifyResponse> modify(@RequestBody @Valid
                                                                       CustomerDeliveryAddressModifyRequest customerDeliveryAddressModifyRequest){
        CustomerDeliveryAddressEditRequest customerDeliveryAddressEditRequest = new CustomerDeliveryAddressEditRequest();
        KsBeanUtil.copyPropertiesThird(customerDeliveryAddressModifyRequest,customerDeliveryAddressEditRequest);
        CustomerDeliveryAddress customerDeliveryAddress = customerDeliveryAddressService.update(customerDeliveryAddressEditRequest,customerDeliveryAddressModifyRequest.getEmployeeId());
        CustomerDeliveryAddressModifyResponse customerDeliveryAddressModifyResponse = CustomerDeliveryAddressModifyResponse.builder().build();
        if (Objects.isNull(customerDeliveryAddress)){
            return BaseResponse.success(customerDeliveryAddressModifyResponse);
        }
        KsBeanUtil.copyPropertiesThird(customerDeliveryAddress,customerDeliveryAddressModifyResponse);
        return BaseResponse.success(customerDeliveryAddressModifyResponse);
    }

    /**
     * 根据收货地址ID删除会员收货地址信息
     * @param customerDeliveryAddressDeleteRequest
     * @return
     */

    @Override
    public BaseResponse deleteById(@RequestBody @Valid CustomerDeliveryAddressDeleteRequest
                                    customerDeliveryAddressDeleteRequest){
        customerDeliveryAddressService.deleteAddress(customerDeliveryAddressDeleteRequest.getAddressId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据收货地址ID和用户ID设置客户默认地址
     * @param customerDeliveryAddressModifyDefaultRequest
     * @return
     */

    @Override
    public BaseResponse modifyDefaultByIdAndCustomerId(@RequestBody @Valid CustomerDeliveryAddressModifyDefaultRequest
                                                        customerDeliveryAddressModifyDefaultRequest){
        customerDeliveryAddressService.setDefaultAddress(customerDeliveryAddressModifyDefaultRequest.getDeliveryAddressId(),customerDeliveryAddressModifyDefaultRequest.getCustomerId());
        return BaseResponse.SUCCESSFUL();
    }
}
