package com.wanmi.sbc.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityVO;
import com.wanmi.sbc.order.api.provider.groupon.GrouponProvider;
import com.wanmi.sbc.order.api.request.groupon.GrouponOrderStatusGetByOrderIdRequest;
import com.wanmi.sbc.order.api.response.groupon.GrouponOrderStatusGetByOrderIdResponse;
import com.wanmi.sbc.order.api.request.groupon.GrouponActivityQueryRequest;
//import io.protostuff.runtime.ArraySchemas;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 拼团订单Controller
 */
@RestController
@RequestMapping("/groupon/order")
@Api(tags = "GrouponOrderController", description = "S2B web公用-拼团订单")
public class GrouponOrderController {


    @Autowired
    private GrouponProvider grouponProvider;

    /**
     * 验证拼团订单是否可支付
     */
    @ApiOperation(value = "验证拼团订单是否可支付")
    @RequestMapping(value = "/check/{orderId}", method = RequestMethod.POST)
    public BaseResponse<GrouponOrderStatusGetByOrderIdResponse> getGrouponOrderStatusByOrderId(@PathVariable String orderId) {
        return grouponProvider.getGrouponOrderStatusByOrderId(new GrouponOrderStatusGetByOrderIdRequest(orderId));
    }

    @ApiOperation(value="根据grouponNo查询拼团活动信息")
    @RequestMapping(value="/query/activity/{grouponNo}",method = RequestMethod.POST)
    public BaseResponse<GrouponActivityVO> getGrouponActivityByGrouponNo(@PathVariable String grouponNo){
        return grouponProvider.getGrouponActivityByGrouponNo(new GrouponActivityQueryRequest(grouponNo));
    }
}
