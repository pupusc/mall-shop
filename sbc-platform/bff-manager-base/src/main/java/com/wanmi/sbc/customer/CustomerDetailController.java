package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.request.detail.*;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 会员详细信息
 * Created by CHENLI on 2017/4/20.
 */
@Api(tags = "CustomerDetailController", description = "会员详细信息")
@RestController
@RequestMapping("/customer")
public class CustomerDetailController {

    @Autowired
    private CustomerDetailProvider customerDetailProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    /**
     * 通过会员ID查询单条会员详细信息
     *
     * @param customerId
     * @return
     */
    @ApiOperation(value = "通过会员ID查询单条会员详细信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员Id", required = true)
    @RequestMapping(value = "/detail/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<CustomerDetailVO> findById(@PathVariable String customerId) {
        CustomerDetailVO customerDetail = customerDetailQueryProvider.getCustomerDetailWithNotDeleteByCustomerId(
                CustomerDetailWithNotDeleteByCustomerIdRequest.builder().customerId(customerId).build()
        ).getContext();
        return ResponseEntity.ok(customerDetail);
    }

    /**
     * 通过会员详情ID查询会员详情
     *
     * @param customerDetailId
     * @return
     */
    @ApiOperation(value = "通过会员详情ID查询会员详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerDetailId", value = "会员详情Id",
            required = true)
    @RequestMapping(value = "/detailInfo/{customerDetailId}", method = RequestMethod.GET)
    public ResponseEntity<CustomerDetailVO> findOne(@PathVariable String customerDetailId) {
        CustomerDetailVO customerDetail = customerDetailQueryProvider.getCustomerDetailById(
                CustomerDetailByIdRequest.builder().customerDetailId(customerDetailId).build()
        ).getContext();
        return ResponseEntity.ok(customerDetail);
    }

    /**
     * 保存会员详细信息
     *
     * @param customerDetailEditRequest
     * @return
     */
    @ApiOperation(value = "保存会员详细信息")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> add(@RequestBody CustomerDetailEditRequest customerDetailEditRequest,
                                            HttpServletRequest request) {
        String employeeId = ((Claims) request.getAttribute("claims")).get("employeeId").toString();
        CustomerDetailAddRequest addRequest = new CustomerDetailAddRequest();
        addRequest.setCreatePerson(employeeId);
        KsBeanUtil.copyPropertiesThird(customerDetailEditRequest, addRequest);
        return ResponseEntity.ok(customerDetailProvider.addCustomerDetail(addRequest));
    }

    /**
     * 批量删除会员详情
     *
     * @param customerIds
     * @return
     */
    @ApiOperation(value = "批量删除会员详情")
    @ApiImplicitParam(paramType = "path", dataType = "List", name = "customerIds", value = "会员Id集合",
            required = true)
    @RequestMapping(value = "/detail/{customerIds}", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> delete(@PathVariable List<String> customerIds) {
        if (CollectionUtils.isEmpty(customerIds)) {
            throw new SbcRuntimeException("K-000009");
        }
        CustomerDetailDeleteRequest request = CustomerDetailDeleteRequest.builder().customerIds(customerIds).build();
        return ResponseEntity.ok(customerDetailProvider.deleteCustomerDetailByCustomerIds(request));
    }

}
