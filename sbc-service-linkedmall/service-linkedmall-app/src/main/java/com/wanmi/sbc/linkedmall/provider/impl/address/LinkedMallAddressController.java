package com.wanmi.sbc.linkedmall.provider.impl.address;

import com.aliyuncs.linkedmall.model.v20180116.QueryAddressResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.linkedmall.address.LinkedMallAddressService;
import com.wanmi.sbc.linkedmall.api.provider.address.LinkedMallAddressProvider;
import com.wanmi.sbc.linkedmall.api.request.address.SbcAddressQueryRequest;
import com.wanmi.sbc.setting.api.provider.thirdaddress.ThirdAddressProvider;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressMappingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LinkedMallAddressController implements LinkedMallAddressProvider {

    @Autowired
    private LinkedMallAddressService linkedMallAddressService;

    @Autowired
    private ThirdAddressProvider thirdAddressProvider;

    @Override
    public BaseResponse<List<QueryAddressResponse.DivisionAddressItem>> query(@RequestBody SbcAddressQueryRequest request) {
        return BaseResponse.success(linkedMallAddressService.queryAddress(request));
    }

    @Override
    public BaseResponse init(){
        linkedMallAddressService.init();
        thirdAddressProvider.mapping(ThirdAddressMappingRequest.builder().thirdPlatformType(ThirdPlatformType.LINKED_MALL).build());
        return BaseResponse.SUCCESSFUL();
    }
}
