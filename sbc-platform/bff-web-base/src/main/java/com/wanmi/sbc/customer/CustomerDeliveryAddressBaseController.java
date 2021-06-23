package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressProvider;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressQueryProvider;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.request.address.*;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailAddRequest;
import com.wanmi.sbc.customer.api.response.address.*;
import com.wanmi.sbc.customer.bean.enums.GrowthValueServiceType;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.bean.enums.PointsServiceType;
import com.wanmi.sbc.customer.bean.vo.CustomerDeliveryAddressVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 客户地址
 * Created by CHENLI on 2017/4/20.
 */
@RestController
@RequestMapping("/customer")
@Validated
@Api(tags = "CustomerDeliveryAddressBaseController", description = "S2B web公用-客户地址信息API")
public class CustomerDeliveryAddressBaseController {

    @Autowired
    private CustomerDeliveryAddressQueryProvider customerDeliveryAddressQueryProvider;

    @Autowired
    private CustomerDeliveryAddressProvider customerDeliveryAddressProvider;

    @Autowired
    private CustomerGrowthValueProvider customerGrowthValueProvider;

    @Autowired
    private CustomerPointsDetailSaveProvider customerPointsDetailSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 查询该客户的所有收货地址
     *
     * @return
     */
    @ApiOperation(value = "查询该客户的所有收货地址")
    @RequestMapping(value = "/addressList", method = RequestMethod.GET)
    public BaseResponse<List<CustomerDeliveryAddressVO>> findAddressList() {
        CustomerDeliveryAddressListRequest customerDeliveryAddressListRequest = new CustomerDeliveryAddressListRequest();
        customerDeliveryAddressListRequest.setCustomerId(commonUtil.getOperatorId());
        BaseResponse<CustomerDeliveryAddressListResponse> customerDeliveryAddressListResponseBaseResponse = customerDeliveryAddressQueryProvider.listByCustomerId(customerDeliveryAddressListRequest);
        CustomerDeliveryAddressListResponse customerDeliveryAddressListResponse = customerDeliveryAddressListResponseBaseResponse.getContext();
        List<CustomerDeliveryAddressVO> customerDeliveryAddressVOList = Collections.emptyList();
        if(Objects.nonNull(customerDeliveryAddressListResponse)){
           customerDeliveryAddressVOList = customerDeliveryAddressListResponse.getCustomerDeliveryAddressVOList()
                    .stream().sorted(Comparator.comparing(CustomerDeliveryAddressVO::getIsDefaltAddress).reversed()).collect(Collectors.toList());
        }

        return BaseResponse.success(customerDeliveryAddressVOList);
    }

    /**
     * 查询客户默认收货地址
     *
     * @return
     */
    @ApiOperation(value = "查询客户默认收货地址")
    @RequestMapping(value = "/addressinfo", method = RequestMethod.GET)
    public BaseResponse<CustomerDeliveryAddressResponse> findDefaultAddress() {
        CustomerDeliveryAddressRequest queryRequest = new CustomerDeliveryAddressRequest();
        String customerId = commonUtil.getOperatorId();
        queryRequest.setCustomerId(customerId);
        BaseResponse<CustomerDeliveryAddressResponse> customerDeliveryAddressResponseBaseResponse = customerDeliveryAddressQueryProvider.getDefaultOrAnyOneByCustomerId(queryRequest);
        CustomerDeliveryAddressResponse customerDeliveryAddressResponse = customerDeliveryAddressResponseBaseResponse.getContext();
        if (Objects.isNull(customerDeliveryAddressResponse)) {
            return BaseResponse.error("收货地址为空");
        } else {
            return BaseResponse.success(customerDeliveryAddressResponse);
        }
    }

    /**
     * 保存客户收货地址
     *
     * @param editRequest
     * @return
     */
    @ApiOperation(value = "保存客户收货地址")
    @RequestMapping(value = "/address", method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse saveAddress(@Valid @RequestBody CustomerDeliveryAddressAddRequest editRequest) {
        String customerId = commonUtil.getOperatorId();
        //一个客户最多可以添加20条地址
        CustomerDeliveryAddressByCustomerIdRequest customerDeliveryAddressByCustomerIdRequest = new CustomerDeliveryAddressByCustomerIdRequest();
        customerDeliveryAddressByCustomerIdRequest.setCustomerId(customerId);

        if (Objects.isNull(editRequest.getDeliveryAddressId()) && customerDeliveryAddressQueryProvider.countByCustomerId(customerDeliveryAddressByCustomerIdRequest).getContext().getResult() >= 20) {
            return BaseResponse.error("最多可以添加20条收货地址");
        }
        
        if(StringUtils.isNotEmpty(editRequest.getConsigneeName())){
            if(editRequest.getConsigneeName().trim().length() < 2 ||
                    editRequest.getConsigneeName().trim().length() > 15){
                return BaseResponse.error("收货人长度必须为2-15个字符之间");
            }
        }else{
            return BaseResponse.error("收货人不能为空");
        }


        if(StringUtils.isNotEmpty(editRequest.getConsigneeNumber())){
            if(!Pattern.matches(commonUtil.REGEX_MOBILE,editRequest.getConsigneeNumber())){
                return BaseResponse.error("请输入正确的手机号码");
            }
        }else{
            return BaseResponse.error("手机号码不能为空");
        }

        editRequest.setCustomerId(customerId);
        BaseResponse<CustomerDeliveryAddressAddResponse> customerDeliveryAddressAddResponseBaseResponse = customerDeliveryAddressProvider.add(editRequest);
        CustomerDeliveryAddressAddResponse customerDeliveryAddressAddResponse = customerDeliveryAddressAddResponseBaseResponse.getContext();
        if (Objects.isNull(customerDeliveryAddressAddResponse)) {
            return BaseResponse.FAILED();
        } else {
            // 增加成长值
            customerGrowthValueProvider.increaseGrowthValue(CustomerGrowthValueAddRequest.builder()
                    .customerId(editRequest.getCustomerId())
                    .type(OperateType.GROWTH)
                    .serviceType(GrowthValueServiceType.ADDSHIPPINGADDRESS)
                    .build());
            // 增加积分
            customerPointsDetailSaveProvider.add(CustomerPointsDetailAddRequest.builder()
                    .customerId(editRequest.getCustomerId())
                    .type(OperateType.GROWTH)
                    .serviceType(PointsServiceType.ADDSHIPPINGADDRESS)
                    .build());

            return BaseResponse.success(customerDeliveryAddressAddResponse);
        }
    }

    /**
     * 根据地址id查询客户收货地址详情
     *
     * @param addressId
     * @return
     */
    @ApiOperation(value = "根据地址id查询客户收货地址详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "addressId", value = "地址id", required = true)
    @RequestMapping(value = "/address/{addressId}", method = RequestMethod.GET)
    public BaseResponse<CustomerDeliveryAddressByIdResponse> findById(@PathVariable String addressId) {
        CustomerDeliveryAddressByIdRequest customerDeliveryAddressByIdRequest = CustomerDeliveryAddressByIdRequest
                .builder().deliveryAddressId(addressId).build();
        BaseResponse<CustomerDeliveryAddressByIdResponse> customerDeliveryAddressByIdResponseBaseResponse = customerDeliveryAddressQueryProvider.getById(customerDeliveryAddressByIdRequest);
        CustomerDeliveryAddressByIdResponse  customerDeliveryAddressByIdResponse = customerDeliveryAddressByIdResponseBaseResponse.getContext();
        if (Objects.isNull(customerDeliveryAddressByIdResponse)) {
            return BaseResponse.error("该收货地址不存在");
        } else {
            return BaseResponse.success(customerDeliveryAddressByIdResponse);
        }
    }

    /**
     * 修改客户收货地址
     *
     * @param editRequest
     * @return
     */
    @ApiOperation(value = "修改客户收货地址")
    @RequestMapping(value = "/addressInfo", method = RequestMethod.PUT)
    public BaseResponse updateAddress(@Valid @RequestBody CustomerDeliveryAddressModifyRequest editRequest) {

        BaseResponse<CustomerDeliveryAddressByIdResponse> customerDeliveryAddressByIdResponseBaseResponse
                = customerDeliveryAddressQueryProvider.getById(CustomerDeliveryAddressByIdRequest.builder().deliveryAddressId(editRequest.getDeliveryAddressId()).build());

        CustomerDeliveryAddressByIdResponse  customerDeliveryAddressByIdResponse = customerDeliveryAddressByIdResponseBaseResponse.getContext();

        if(Objects.isNull(customerDeliveryAddressByIdResponse)){
            return BaseResponse.error("该收货地址不存在");
        }else{
            if(!commonUtil.getOperatorId().equals(customerDeliveryAddressByIdResponse.getCustomerId())){
                return BaseResponse.error("非法越权操作");
            }
        }

        editRequest.setEmployeeId(commonUtil.getOperatorId());

        if(StringUtils.isNotEmpty(editRequest.getConsigneeName())){
            if(editRequest.getConsigneeName().trim().length() < 2 ||
                    editRequest.getConsigneeName().trim().length() > 15){
                return BaseResponse.error("收货人长度必须为2-15个字符之间");
            }
        }else{
            return BaseResponse.error("收货人不能为空");
        }


        if(StringUtils.isNotEmpty(editRequest.getConsigneeNumber())){
            if(!Pattern.matches(commonUtil.REGEX_MOBILE,editRequest.getConsigneeNumber())){
                return BaseResponse.error("请输入正确的手机号码");
            }
        }else{
            return BaseResponse.error("手机号码不能为空");
        }

        BaseResponse<CustomerDeliveryAddressModifyResponse> customerDeliveryAddressModifyResponseBaseResponse = customerDeliveryAddressProvider.modify(editRequest);
        CustomerDeliveryAddressModifyResponse response = customerDeliveryAddressModifyResponseBaseResponse.getContext();
        if (Objects.isNull(response)) {
            return BaseResponse.FAILED();
        } else {
            return BaseResponse.success(response);
        }
    }

    /**
     * 设置客户收货地址为默认
     *
     * @return
     */
    @ApiOperation(value = "设置客户收货地址为默认")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "deliveryAddressId", value = "地址id", required = true)
    @RequestMapping(value = "/defaultAddress/{deliveryAddressId}", method = RequestMethod.POST)
    public BaseResponse setDefaultAddress(@PathVariable String deliveryAddressId) {
        CustomerDeliveryAddressModifyDefaultRequest queryRequest = new CustomerDeliveryAddressModifyDefaultRequest();
        queryRequest.setCustomerId(commonUtil.getOperatorId());
        queryRequest.setDeliveryAddressId(deliveryAddressId);
        customerDeliveryAddressProvider.modifyDefaultByIdAndCustomerId(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除客户收货地址
     *
     * @param addressId
     * @return
     */
    @ApiOperation(value = "删除客户收货地址")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "addressId", value = "地址id", required = true)
    @RequestMapping(value = "/addressInfo/{addressId}", method = RequestMethod.DELETE)
    public BaseResponse deleteAddress(@PathVariable String addressId) {

        BaseResponse<CustomerDeliveryAddressByIdResponse> customerDeliveryAddressByIdResponseBaseResponse
                = customerDeliveryAddressQueryProvider.getById(CustomerDeliveryAddressByIdRequest.builder().deliveryAddressId(addressId).build());

        CustomerDeliveryAddressByIdResponse  customerDeliveryAddressByIdResponse = customerDeliveryAddressByIdResponseBaseResponse.getContext();

        if(Objects.isNull(customerDeliveryAddressByIdResponse)){
            return BaseResponse.error("该收货地址不存在");
        }else{
            if(!commonUtil.getOperatorId().equals(customerDeliveryAddressByIdResponse.getCustomerId())){
                return BaseResponse.error("非法越权操作");
            }
        }

        CustomerDeliveryAddressDeleteRequest customerDeliveryAddressDeleteRequest = new CustomerDeliveryAddressDeleteRequest();
        customerDeliveryAddressDeleteRequest.setAddressId(addressId);
        customerDeliveryAddressProvider.deleteById(customerDeliveryAddressDeleteRequest);
        return BaseResponse.SUCCESSFUL();
    }


}
