package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.follow.StoreCustomerFollowQueryProvider;
import com.wanmi.sbc.customer.api.request.follow.StoreCustomerFollowPageRequest;
import com.wanmi.sbc.customer.api.request.follow.StoreCustomerFollowRequest;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerFollowVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺关注Controller
 * Created by daiyitian on 17/4/12.
 */
@RestController
@RequestMapping("/store")
@Api(tags = "StoreFollowController", description = "mobile 店铺关注Controller")
public class StoreFollowController {

    @Autowired
    private StoreCustomerFollowQueryProvider storeCustomerFollowQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 获取店铺关注列表
     *
     * @param request 查询条件
     * @return 店铺关注分页
     */
    @ApiOperation(value = "获取店铺关注列表")
    @RequestMapping(value = "/storeFollows", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<StoreCustomerFollowVO>> info(@RequestBody StoreCustomerFollowRequest request) {
        StoreCustomerFollowPageRequest pageRequest = new StoreCustomerFollowPageRequest();
        KsBeanUtil.copyPropertiesThird(request, pageRequest);
        pageRequest.setCustomerId(commonUtil.getOperatorId());

        return BaseResponse.success(
                storeCustomerFollowQueryProvider.pageStoreCustomerFollow(pageRequest
                ).getContext().getCustomerFollowVOPage());
    }
}
