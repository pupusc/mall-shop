package com.wanmi.sbc.platformaddress;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressSaveProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressAddRequest;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressDelByIdRequest;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressModifyRequest;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressAddResponse;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressModifyResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(description = "平台地址信息管理API", tags = "BossPlatformAddressController")
@RestController
@RequestMapping(value = "/addressmanage")
public class BossPlatformAddressController {

    @Autowired
    private PlatformAddressSaveProvider platformAddressSaveProvider;

    @ApiOperation(value = "新增积分商品表")
    @PostMapping("/add")
    public BaseResponse<PlatformAddressAddResponse> add(@RequestBody @Valid PlatformAddressAddRequest addReq) {
        addReq.setSortNo(NumberUtils.toInt(addReq.getAddrId()));
        return platformAddressSaveProvider.add(addReq);
    }

    @ApiOperation(value = "修改积分商品表")
    @PutMapping("/modify")
    public BaseResponse<PlatformAddressModifyResponse> modify(@RequestBody @Valid PlatformAddressModifyRequest modifyReq) {
        return platformAddressSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除积分商品表")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable String id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PlatformAddressDelByIdRequest delByIdReq = new PlatformAddressDelByIdRequest();
        delByIdReq.setId(id);
        return platformAddressSaveProvider.deleteById(delByIdReq);
    }
}
