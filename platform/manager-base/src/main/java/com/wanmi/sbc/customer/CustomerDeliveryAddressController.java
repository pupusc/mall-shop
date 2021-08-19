package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressProvider;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressQueryProvider;
import com.wanmi.sbc.customer.api.request.address.*;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressAddResponse;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressListResponse;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressModifyResponse;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerDeliveryAddressVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 客户地址
 * Created by CHENLI on 2017/4/20.
 */
@Api(tags = "CustomerDeliveryAddressController", description = "客户地址服务API")
@RestController
@RequestMapping("/customer")
@Validated
public class CustomerDeliveryAddressController {

    @Autowired
    private CustomerDeliveryAddressQueryProvider customerDeliveryAddressQueryProvider;

    @Autowired
    private CustomerDeliveryAddressProvider customerDeliveryAddressProvider;

    /**
     * 查询该客户的所有收货地址
     * @param customerId
     * @return
     */
    @ApiOperation(value = "查询该客户的所有收货地址")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员Id", required = true)
    @RequestMapping(value = "/addressList/{customerId}", method = RequestMethod.GET)
    public BaseResponse<List<CustomerDeliveryAddressVO>> findAddressList(@PathVariable String customerId){
        CustomerDeliveryAddressListRequest request = new CustomerDeliveryAddressListRequest();
        request.setCustomerId(customerId);
        BaseResponse<CustomerDeliveryAddressListResponse> customerDeliveryAddressListResponseBaseResponse = customerDeliveryAddressQueryProvider.listByCustomerId(request);
        CustomerDeliveryAddressListResponse customerDeliveryAddressListResponse = customerDeliveryAddressListResponseBaseResponse.getContext();
        if (Objects.nonNull(customerDeliveryAddressListResponse)){
                List<CustomerDeliveryAddressVO> customerDeliveryAddressVOList = customerDeliveryAddressListResponse.getCustomerDeliveryAddressVOList();
            if(CollectionUtils.isNotEmpty(customerDeliveryAddressVOList)) {
                // 过滤掉需要用户完善的收货地址
                List<CustomerDeliveryAddressVO> addressVOList =
                        customerDeliveryAddressVOList.stream().filter(v -> !Boolean.TRUE.equals(v.getNeedComplete())).collect(Collectors.toList());
                return BaseResponse.success(CollectionUtils.isNotEmpty(addressVOList) ? addressVOList : Collections.emptyList());
            }
        }
        return BaseResponse.success(Collections.emptyList());
    }

    /**
     * 查询客户默认收货地址
     * @param queryRequest
     * @return
     */
    @ApiOperation(value = "查询客户默认收货地址")
    @RequestMapping(value = "/addressinfo", method = RequestMethod.POST)
    public BaseResponse<CustomerDeliveryAddressResponse> findDefaultAddress(@RequestBody CustomerDeliveryAddressRequest queryRequest){
        BaseResponse<CustomerDeliveryAddressResponse> customerDeliveryAddressResponseBaseResponse = customerDeliveryAddressQueryProvider.getDefaultOrAnyOneByCustomerId(queryRequest);
        CustomerDeliveryAddressResponse customerDeliveryAddressResponse = customerDeliveryAddressResponseBaseResponse.getContext();
        if (Objects.nonNull(customerDeliveryAddressResponse)){
            return BaseResponse.success(customerDeliveryAddressResponse);
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 保存客户收货地址
     * @param editRequest
     * @return
     */
    @ApiOperation(value = "保存客户收货地址")
    @RequestMapping(value = "/address", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> saveAddress(@Valid @RequestBody CustomerDeliveryAddressAddRequest editRequest, HttpServletRequest request){
        //H5一个客户最多可以添加20条地址
        CustomerDeliveryAddressByCustomerIdRequest customerDeliveryAddressByCustomerIdRequest = new CustomerDeliveryAddressByCustomerIdRequest();
        customerDeliveryAddressByCustomerIdRequest.setCustomerId(editRequest.getCustomerId());

        if (  customerDeliveryAddressQueryProvider.countByCustomerId(customerDeliveryAddressByCustomerIdRequest)
                .getContext().getResult() >= 20){
            return ResponseEntity.ok(BaseResponse.error("最多可以添加20条收货地址"));
        }
        String employeeId = ((Claims) request.getAttribute("claims")).get("employeeId").toString();
        editRequest.setEmployeeId(employeeId);
        BaseResponse<CustomerDeliveryAddressAddResponse>  customerDeliveryAddressAddResponseBaseResponse = customerDeliveryAddressProvider.add(editRequest);
        CustomerDeliveryAddressAddResponse response = customerDeliveryAddressAddResponseBaseResponse.getContext();

        if(Objects.nonNull(response)){
            return ResponseEntity.ok(BaseResponse.success(response));
        }else{
            return ResponseEntity.ok(BaseResponse.FAILED());
        }
    }

    /**
     * 修改客户收货地址
     * @param editRequest
     * @return
     */
    @ApiOperation(value = "修改客户收货地址")
    @RequestMapping(value = "/address", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> updateAddress(@Valid @RequestBody CustomerDeliveryAddressModifyRequest editRequest, HttpServletRequest request){
        String employeeId = ((Claims) request.getAttribute("claims")).get("employeeId").toString();
        editRequest.setEmployeeId(employeeId);
        BaseResponse<CustomerDeliveryAddressModifyResponse> customerDeliveryAddressModifyResponseBaseResponse = customerDeliveryAddressProvider.modify(editRequest);
        CustomerDeliveryAddressModifyResponse response = customerDeliveryAddressModifyResponseBaseResponse.getContext();
        if(Objects.nonNull(response)){
            return ResponseEntity.ok(BaseResponse.success(response));
        }else{
            return ResponseEntity.ok(BaseResponse.FAILED());
        }
    }

    /**
     * 删除客户收货地址
     * @param addressId
     * @return
     */
    @ApiOperation(value = "删除客户收货地址")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "addressId", value = "收货地址Id", required = true)
    @RequestMapping(value = "/address/{addressId}", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponse> deleteAddress(@PathVariable String addressId){
        CustomerDeliveryAddressDeleteRequest customerDeliveryAddressDeleteRequest = new CustomerDeliveryAddressDeleteRequest();
        customerDeliveryAddressDeleteRequest.setAddressId(addressId);
        customerDeliveryAddressProvider.deleteById(customerDeliveryAddressDeleteRequest);
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }


    /**
     * 设置客户收货地址为默认
     * @param queryRequest
     * @return
     */
    @ApiOperation(value = "设置客户收货地址为默认")
    @RequestMapping(value = "/defaultAddress", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> setDefaultAddress(@RequestBody CustomerDeliveryAddressModifyDefaultRequest queryRequest){
        if(queryRequest.getDeliveryAddressId() == null && queryRequest.getCustomerId()==null){
            throw new SbcRuntimeException("K-000009");
        }
        customerDeliveryAddressProvider.modifyDefaultByIdAndCustomerId(queryRequest);
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

}
