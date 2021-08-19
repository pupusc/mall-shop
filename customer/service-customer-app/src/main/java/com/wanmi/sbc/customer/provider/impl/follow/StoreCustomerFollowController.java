package com.wanmi.sbc.customer.provider.impl.follow;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.follow.StoreCustomerFollowProvider;
import com.wanmi.sbc.customer.api.request.follow.StoreCustomerFollowAddRequest;
import com.wanmi.sbc.customer.api.request.follow.StoreCustomerFollowDeleteRequest;
import com.wanmi.sbc.customer.api.request.follow.StoreCustomerFollowRequest;
import com.wanmi.sbc.customer.follow.service.StoreCustomerFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 店铺收藏-店铺收藏添加/修改/删除API
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@RestController
@Validated
public class StoreCustomerFollowController implements StoreCustomerFollowProvider {

    @Autowired
    private StoreCustomerFollowService storeCustomerFollowService;

    @Override
    public BaseResponse addStoreCustomerFollow(@RequestBody @Valid StoreCustomerFollowAddRequest request){
        StoreCustomerFollowRequest followRequest = new StoreCustomerFollowRequest();
        KsBeanUtil.copyPropertiesThird(request, followRequest);
        storeCustomerFollowService.save(followRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteStoreCustomerFollow(@RequestBody @Valid StoreCustomerFollowDeleteRequest request){
        StoreCustomerFollowRequest followRequest = new StoreCustomerFollowRequest();
        KsBeanUtil.copyPropertiesThird(request, followRequest);
        storeCustomerFollowService.delete(followRequest);
        return BaseResponse.SUCCESSFUL();
    }
}
