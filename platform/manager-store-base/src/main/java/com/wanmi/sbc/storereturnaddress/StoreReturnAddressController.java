package com.wanmi.sbc.storereturnaddress;

import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.storereturnaddress.StoreReturnAddressProvider;
import com.wanmi.sbc.customer.api.provider.storereturnaddress.StoreReturnAddressQueryProvider;
import com.wanmi.sbc.customer.api.request.storereturnaddress.*;
import com.wanmi.sbc.customer.api.response.storereturnaddress.StoreReturnAddressByIdResponse;
import com.wanmi.sbc.customer.api.response.storereturnaddress.StoreReturnAddressListResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;


@Api(description = "店铺退货地址表管理API", tags = "StoreReturnAddressController")
@RestController
@RequestMapping(value = "/storereturnaddress")
public class StoreReturnAddressController {

    @Autowired
    private StoreReturnAddressQueryProvider storeReturnAddressQueryProvider;

    @Autowired
    private StoreReturnAddressProvider storeReturnAddressProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "列表查询店铺退货地址表")
    @PostMapping("/list")
    public BaseResponse<StoreReturnAddressListResponse> getList(@RequestBody @Valid StoreReturnAddressListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
//        if(Objects.isNull(listReq.getStoreId())){
            listReq.setStoreId(commonUtil.getStoreId());
//        }
        listReq.setShowAreaNameFlag(Boolean.TRUE);
        return storeReturnAddressQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询店铺退货地址表")
    @GetMapping("/{addressId}")
    public BaseResponse<StoreReturnAddressByIdResponse> getById(@PathVariable String addressId) {
        if (addressId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreReturnAddressByIdRequest idReq = new StoreReturnAddressByIdRequest();
        idReq.setAddressId(addressId);
        idReq.setStoreId(commonUtil.getStoreId());
        idReq.setShowAreaName(Boolean.TRUE);
        return storeReturnAddressQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增店铺退货地址表")
    @MultiSubmit
    @PostMapping("/add")
    public BaseResponse add(@RequestBody @Valid StoreReturnAddressAddRequest addReq) {
        addReq.setCompanyInfoId(commonUtil.getCompanyInfoId());
        addReq.setStoreId(commonUtil.getStoreId());
        addReq.setCreatePerson(commonUtil.getOperatorId());
        return storeReturnAddressProvider.add(addReq);
    }

    @ApiOperation(value = "修改店铺退货地址表")
    @MultiSubmit
    @PutMapping("/modify")
    public BaseResponse modify(@RequestBody @Valid StoreReturnAddressModifyRequest modifyReq) {
        modifyReq.setCompanyInfoId(commonUtil.getCompanyInfoId());
        modifyReq.setStoreId(commonUtil.getStoreId());
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        return storeReturnAddressProvider.modify(modifyReq);
    }

    @ApiOperation(value = "默认店铺退货地址表")
    @MultiSubmit
    @PutMapping("/modifyDefault")
    public BaseResponse modifyDefault(@RequestBody @Valid StoreReturnAddressDefaultRequest modifyReq) {
        modifyReq.setStoreId(commonUtil.getStoreId());
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        return storeReturnAddressProvider.modifyDefault(modifyReq);
    }

    @ApiOperation(value = "根据id删除店铺退货地址表")
    @MultiSubmit
    @DeleteMapping("/{addressId}")
    public BaseResponse deleteById(@PathVariable String addressId) {
        if (addressId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreReturnAddressDelByIdRequest delByIdReq = new StoreReturnAddressDelByIdRequest();
        delByIdReq.setStoreId(commonUtil.getStoreId());
        delByIdReq.setAddressId(addressId);
        delByIdReq.setDeletePerson(commonUtil.getOperatorId());
        return storeReturnAddressProvider.deleteById(delByIdReq);
    }
}
