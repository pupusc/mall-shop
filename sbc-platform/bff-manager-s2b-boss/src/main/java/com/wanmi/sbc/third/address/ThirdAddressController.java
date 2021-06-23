package com.wanmi.sbc.third.address;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.setting.api.provider.thirdaddress.ThirdAddressProvider;
import com.wanmi.sbc.setting.api.provider.thirdaddress.ThirdAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressMappingRequest;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressModifyRequest;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressPageRequest;
import com.wanmi.sbc.setting.api.response.thirdaddress.ThirdAddressPageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(description = "第三方地址映射表管理API", tags = "ThirdAddressController")
@RestController
@RequestMapping(value = "/third/address")
public class ThirdAddressController {

    @Autowired
    private ThirdAddressQueryProvider thirdAddressQueryProvider;

    @Autowired
    private ThirdAddressProvider thirdAddressProvider;

    @ApiOperation(value = "分页查询第三方地址表")
    @PostMapping("/linkedMall/page")
    public BaseResponse<ThirdAddressPageResponse> pageLinkedMall(@RequestBody @Valid ThirdAddressPageRequest pageReq) {
        pageReq.setThirdFlag(ThirdPlatformType.LINKED_MALL);
        return thirdAddressQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "修改第三方")
    @PutMapping("/modify")
    public BaseResponse modify(@RequestBody @Valid ThirdAddressModifyRequest request) {
        return thirdAddressProvider.modify(request);
    }

    @ApiOperation(value = "linkedMall地址全局更新")
    @PutMapping("/linkedMall/mapping")
    public BaseResponse mappingLinkedMall() {
        return thirdAddressProvider.mapping(ThirdAddressMappingRequest.builder()
                .thirdPlatformType(ThirdPlatformType.LINKED_MALL).build());
    }
}
