package com.wanmi.sbc.platformaddress;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressListRequest;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressListResponse;
import com.wanmi.sbc.setting.bean.enums.AddrLevel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;


@Api(description = "平台地址信息管理API", tags = "PlatformAddressController")
@RestController
@RequestMapping(value = "/platformaddress")
public class PlatformAddressController {

    @Autowired
    private PlatformAddressQueryProvider platformAddressQueryProvider;

    @ApiOperation(value = "省份列表")
    @GetMapping("/province")
    public BaseResponse<PlatformAddressListResponse> getProvince() {
        return platformAddressQueryProvider.list(PlatformAddressListRequest.builder().delFlag(DeleteFlag.NO)
                .addrLevel(AddrLevel.PROVINCE).leafFlag(Boolean.TRUE).build());
    }

    @ApiOperation(value = "城市列表")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "addrId", value = "省份地址id", required = true)
    @GetMapping("/city/{addrId}")
    public BaseResponse<PlatformAddressListResponse> getCity(@PathVariable String addrId) {
        return platformAddressQueryProvider.list(PlatformAddressListRequest.builder().delFlag(DeleteFlag.NO)
                .addrLevel(AddrLevel.CITY).addrParentId(addrId).leafFlag(Boolean.TRUE).build());
    }

    @ApiOperation(value = "区县列表")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "addrId", value = "城市地址id", required = true)
    @GetMapping("/district/{addrId}")
    public BaseResponse<PlatformAddressListResponse> getDistrict(@PathVariable String addrId) {
        return platformAddressQueryProvider.list(PlatformAddressListRequest.builder().delFlag(DeleteFlag.NO)
                .addrLevel(AddrLevel.DISTRICT).addrParentId(addrId).leafFlag(Boolean.TRUE).build());
    }

    @ApiOperation(value = "乡镇街道列表")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "addrId", value = "区县地址id", required = true)
    @GetMapping("/street/{addrId}")
    public BaseResponse<PlatformAddressListResponse> getStreet(@PathVariable String addrId) {
        return platformAddressQueryProvider.list(PlatformAddressListRequest.builder().delFlag(DeleteFlag.NO)
                .addrLevel(AddrLevel.STREET).addrParentId(addrId).build());
    }

    @ApiOperation(value = "列表查询省市区")
    @PostMapping("/listByIds")
    public BaseResponse<PlatformAddressListResponse> listByIds(@RequestBody PlatformAddressListRequest request) {
        //没有任何id的情况下直接返回空集
        if(CollectionUtils.isEmpty(request.getAddrIdList()) && CollectionUtils.isEmpty(request.getAddrParentIdList())){
            return BaseResponse.success(PlatformAddressListResponse.builder().platformAddressVOList(Collections.emptyList()).build());
        }
        request.setDelFlag(DeleteFlag.NO);
        return platformAddressQueryProvider.list(request);
    }

    @ApiOperation(value = "仅查询省市区")
    @GetMapping("/listLimitDistrict")
    public BaseResponse<PlatformAddressListResponse> listByArea() {
        return platformAddressQueryProvider.list(PlatformAddressListRequest.builder().delFlag(DeleteFlag.NO)
                .addrLevels(Arrays.asList(AddrLevel.PROVINCE, AddrLevel.CITY, AddrLevel.DISTRICT)).build());
    }

}
